# search_query.py
import sqlite3
import sqlite_vec
import struct
from google import genai
from pathlib import Path
from dotenv import load_dotenv
import os

load_dotenv(dotenv_path=Path(__file__).resolve().parent / ".env")

def search(query: str, top_k: int = 5, source_filter: str = None):
    client = genai.Client(
        api_key=os.getenv("GEMINI_API_KEY"),
        http_options={"api_version": "v1beta"}
    )
    response = client.models.embed_content(
        model="gemini-embedding-001",
        contents=query
    )
    blob = struct.pack("3072f", *response.embeddings[0].values)

    db = sqlite3.connect("./lawmate_vec.db")
    db.enable_load_extension(True)
    sqlite_vec.load(db)
    db.enable_load_extension(False)

    if source_filter:
        # 특정 테이블만 검색
        results = db.execute("""
                             SELECT i.id, i.document, i.source, i.ref_id, v.distance
                             FROM vec_items v
                                      JOIN items i ON i.rowid = v.rowid
                             WHERE v.embedding MATCH ?
                               AND k = ?
                               AND i.source = ?
                             ORDER BY v.distance
                             """, (blob, top_k, source_filter)).fetchall()
    else:
        # 전체 테이블 통합 검색
        results = db.execute("""
                             SELECT i.id, i.document, i.source, i.ref_id, v.distance
                             FROM vec_items v
                                      JOIN items i ON i.rowid = v.rowid
                             WHERE v.embedding MATCH ?
                               AND k = ?
                             ORDER BY v.distance
                             """, (blob, top_k)).fetchall()

    db.close()
    return results


if __name__ == "__main__":
    query = input("검색어: ")
    results = search(query, top_k=5)

    print(f"\n🔍 '{query}' 검색 결과:\n")
    for rank, (uid, document, source, ref_id, distance) in enumerate(results, 1):
        print(f"[{rank}] 출처: {source} (ID: {ref_id})  유사도: {distance:.4f}")
        print(f"     {document[:120]}...")
        print()