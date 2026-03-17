import sqlite3
import sqlite_vec
import struct
import sys

print("1. 시작", flush=True)

db = sqlite3.connect("./lawmate_vec.db")
db.enable_load_extension(True)
sqlite_vec.load(db)
db.enable_load_extension(False)
print("2. sqlite-vec 로드 완료", flush=True)

# 벡터 테이블 생성
db.execute("CREATE VIRTUAL TABLE IF NOT EXISTS vec_items USING vec0(embedding float[3072])")
db.execute("CREATE TABLE IF NOT EXISTS items (id TEXT PRIMARY KEY, document TEXT, meta_type TEXT)")
print("3. 테이블 생성 완료", flush=True)

# 테스트 데이터 삽입
vector = [0.1] * 3072
blob = struct.pack(f"{len(vector)}f", *vector)
db.execute("INSERT OR REPLACE INTO items VALUES (?, ?, ?)", ("test_1", "테스트 문서", "law"))
db.execute("INSERT OR REPLACE INTO vec_items(rowid, embedding) SELECT rowid, ? FROM items WHERE id = ?", (blob, "test_1"))
db.commit()
print("4. 삽입 완료", flush=True)

count = db.execute("SELECT COUNT(*) FROM items").fetchone()[0]
print(f"5. 총 데이터 수: {count}", flush=True)

db.close()
print("6. 정상 종료", flush=True)