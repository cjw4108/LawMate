import oracledb
import sqlite3
import sqlite_vec
import struct
from google import genai
import sys
import traceback
import time
import json
import os
from pathlib import Path
from dotenv import load_dotenv

load_dotenv(dotenv_path=Path(__file__).resolve().parent / ".env")

PROGRESS_FILE = Path("./indexing_progress.json")
DB_PATH = "./lawmate_vec.db"
BATCH_SIZE = 10
MAX_RETRIES = 3
RETRY_DELAY = 2.0


def load_progress() -> set:
    if PROGRESS_FILE.exists():
        with open(PROGRESS_FILE) as f:
            return set(json.load(f).get("done_ids", []))
    return set()


def save_progress(done_ids: set):
    with open(PROGRESS_FILE, "w") as f:
        json.dump({"done_ids": list(done_ids)}, f)


def init_db():
    db = sqlite3.connect(DB_PATH)
    db.enable_load_extension(True)
    sqlite_vec.load(db)
    db.enable_load_extension(False)
    db.execute("""
               CREATE TABLE IF NOT EXISTS items (
                                                    id TEXT PRIMARY KEY,
                                                    document TEXT,
                                                    source TEXT,      -- 테이블 출처 (lawyer/documents/law_content/legal_terms)
                                                    ref_id TEXT       -- 원본 테이블의 PK
               )
               """)
    db.execute("""
        CREATE VIRTUAL TABLE IF NOT EXISTS vec_items
        USING vec0(embedding float[3072])
    """)
    db.commit()
    return db


def embed_with_retry(client, text: str):
    for attempt in range(1, MAX_RETRIES + 1):
        try:
            response = client.models.embed_content(
                model="gemini-embedding-001",
                contents=text
            )
            return response.embeddings[0].values
        except Exception as e:
            if attempt == MAX_RETRIES:
                raise
            wait = RETRY_DELAY * (2 ** (attempt - 1))
            print(f"  ⚠️ 재시도 {attempt}/{MAX_RETRIES} ({wait:.0f}초 대기)", flush=True)
            time.sleep(wait)


def save_to_db(db, unique_id: str, document: str, source: str, ref_id: str, vector: list):
    blob = struct.pack(f"{len(vector)}f", *vector)
    db.execute(
        "INSERT OR REPLACE INTO items (id, document, source, ref_id) VALUES (?, ?, ?, ?)",
        (unique_id, document, source, ref_id)
    )
    rowid = db.execute("SELECT rowid FROM items WHERE id = ?", (unique_id,)).fetchone()[0]
    db.execute(
        "INSERT OR REPLACE INTO vec_items(rowid, embedding) VALUES (?, ?)",
        (rowid, blob)
    )
    db.commit()


def process_table(label: str, rows: list, client, db, done_ids: set, text_fn):
    """
    label   : 'lawyer' / 'documents' / 'law_content' / 'legal_terms'
    text_fn : row → (unique_id, ref_id, display_text) 반환하는 함수
    """
    total = len(rows)
    newly_done = set()

    for i, row in enumerate(rows, 1):
        try:
            unique_id, ref_id, text = text_fn(row)
        except Exception as e:
            print(f"  ❌ [{label}] 행 파싱 실패: {e}", flush=True)
            continue

        if unique_id in done_ids:
            print(f"⏭️  [{label} {i}/{total}] 건너뜀", flush=True)
            continue

        try:
            print(f"🔄 [{label} {i}/{total}] 임베딩 중...", flush=True)
            vector = embed_with_retry(client, text)
            save_to_db(db, unique_id, text, label, ref_id, vector)
            print(f"✅ [{label} {i}/{total}] 완료", flush=True)

            newly_done.add(unique_id)
            done_ids.add(unique_id)

            if len(newly_done) % BATCH_SIZE == 0:
                save_progress(done_ids)

            time.sleep(0.1)

        except Exception as e:
            print(f"\n❌ [{label}] ID {unique_id} 실패: {e}", flush=True)
            traceback.print_exc()
            continue

    save_progress(done_ids)
    print(f"✨ [{label}] 완료 — {len(newly_done)}개 신규 처리\n", flush=True)
    return done_ids


# ─── 각 테이블별 텍스트 추출 함수 ───────────────────────

def parse_lawyer(row):
    ref_id = str(row[0])
    name   = row[1].read() if hasattr(row[1], "read") else str(row[1] or "")
    # 추가 컬럼이 있으면 여기에 붙이세요 (전문분야, 소개 등)
    text = f"변호사: {name}"
    return f"lawyer_{ref_id}", ref_id, text


def parse_documents(row):
    ref_id      = str(row[0])
    title       = row[1].read() if hasattr(row[1], "read") else str(row[1] or "")
    description = row[2].read() if hasattr(row[2], "read") else str(row[2] or "")
    text = f"{title}: {description}"  # ← title이 실제 제목인지 확인 필요
    return f"documents_{ref_id}", ref_id, text

def parse_law_content(row):
    ref_id    = str(row[0])
    title     = row[1].read() if hasattr(row[1], "read") else str(row[1] or "")
    content   = row[2].read() if hasattr(row[2], "read") else str(row[2] or "")
    process   = row[3].read() if hasattr(row[3], "read") else str(row[3] or "")
    documents = row[4].read() if hasattr(row[4], "read") else str(row[4] or "")
    text = f"{title}: {content} {process} {documents}".strip()
    return f"law_content_{ref_id}", ref_id, text


def parse_legal_terms(row):
    ref_id  = str(row[0])
    title   = row[1].read() if hasattr(row[1], "read") else str(row[1] or "")
    content = row[2].read() if hasattr(row[2], "read") else str(row[2] or "")
    text = f"{title}: {content}"
    return f"legal_terms_{ref_id}", ref_id, text


# ─── 메인 ────────────────────────────────────────────────

def main():
    required_env = ["GEMINI_API_KEY", "ORACLE_USER", "ORACLE_PASSWORD", "ORACLE_DSN"]
    missing = [k for k in required_env if not os.getenv(k)]
    if missing:
        print(f"❌ 환경 변수 누락: {missing}")
        sys.exit(1)

    try:
        client = genai.Client(
            api_key=os.getenv("GEMINI_API_KEY"),
            http_options={"api_version": "v1beta"}
        )
        db = init_db()
        print("✅ 환경 설정 완료", flush=True)
    except Exception as e:
        print(f"❌ 초기 설정 에러: {e}")
        sys.exit(1)

    done_ids = load_progress()
    if done_ids:
        print(f"🔁 이전 진행 상황: {len(done_ids)}개 처리됨, 이어서 시작.\n", flush=True)

    # 인덱싱할 테이블 목록 (쿼리, 파싱함수, 레이블)
    tables = [
        ("SELECT * FROM LEGAL_TERMS",  parse_legal_terms, "legal_terms"),
        ("SELECT * FROM TB_LAWYER",    parse_lawyer,      "lawyer"),
        ("SELECT * FROM DOCUMENTS",    parse_documents,   "documents"),
        ("SELECT * FROM LAW_CONTENT",  parse_law_content, "law_content"),
    ]

    conn = None
    try:
        print("🔗 Oracle DB 연결 중...", flush=True)
        conn = oracledb.connect(
            user=os.getenv("ORACLE_USER"),
            password=os.getenv("ORACLE_PASSWORD"),
            dsn=os.getenv("ORACLE_DSN")
        )
        cursor = conn.cursor()

        for sql, parse_fn, label in tables:
            print(f"\n📂 [{label}] 조회 중...", flush=True)
            cursor.execute(sql)
            rows = cursor.fetchall()
            if not rows:
                print(f"  ⚠️ [{label}] 데이터 없음, 건너뜀", flush=True)
                continue
            print(f"  총 {len(rows)}개 발견", flush=True)
            done_ids = process_table(label, rows, client, db, done_ids, parse_fn)

        count = db.execute("SELECT COUNT(*) FROM items").fetchone()[0]
        print(f"\n🎉 전체 인덱싱 완료! 총 저장 데이터: {count}개", flush=True)

        # 출처별 통계
        stats = db.execute(
            "SELECT source, COUNT(*) FROM items GROUP BY source"
        ).fetchall()
        for source, cnt in stats:
            print(f"   {source}: {cnt}개", flush=True)

    except Exception:
        print("\n❌ 치명적 에러:")
        traceback.print_exc()
    finally:
        if conn:
            conn.close()
            print("\n🔌 Oracle DB 연결 종료.")
        db.close()


if __name__ == "__main__":
    main()