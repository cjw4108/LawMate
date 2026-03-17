# search_server.py
from flask import Flask, request, jsonify
import sqlite3
import sqlite_vec
import struct
from google import genai
from pathlib import Path
from dotenv import load_dotenv
import os

load_dotenv(dotenv_path=Path(__file__).resolve().parent / ".env")

app = Flask(__name__)

DISTANCE_THRESHOLD = 0.85

def get_db():
    db = sqlite3.connect("./lawmate_vec.db")
    db.enable_load_extension(True)
    sqlite_vec.load(db)
    db.enable_load_extension(False)
    return db

def embed(text: str):
    client = genai.Client(
        api_key=os.getenv("GEMINI_API_KEY"),
        http_options={"api_version": "v1beta"}
    )
    response = client.models.embed_content(
        model="gemini-embedding-001",
        contents=text
    )
    return response.embeddings[0].values

@app.route("/search", methods=["GET"])
def search():
    query = request.args.get("q", "").strip()
    top_k = int(request.args.get("k", 5))
    source = request.args.get("source", None)

    if not query:
        return jsonify({"error": "검색어를 입력하세요."}), 400

    try:
        vector = embed(query)
        blob = struct.pack("3072f", *vector)
        db = get_db()

        if source:
            rows = db.execute("""
                              SELECT i.id, i.document, i.source, i.ref_id, v.distance
                              FROM vec_items v
                                       JOIN items i ON i.rowid = v.rowid
                              WHERE v.embedding MATCH ? AND k = ? AND i.source = ?
                              ORDER BY v.distance
                              """, (blob, top_k, source)).fetchall()
        else:
            rows = db.execute("""
                              SELECT i.id, i.document, i.source, i.ref_id, v.distance
                              FROM vec_items v
                                       JOIN items i ON i.rowid = v.rowid
                              WHERE v.embedding MATCH ? AND k = ?
                              ORDER BY v.distance
                              """, (blob, top_k)).fetchall()

        db.close()

        print(f"\nQuery: {query}")
        for r in rows:
            print(f"  distance: {r[4]:.4f} | {r[1][:50] if r[1] else 'NULL'}")

        results = []
        for r in rows:
            if r[4] > DISTANCE_THRESHOLD:
                continue

            doc = r[1] or ""
            parts = doc.split(": ", 1)
            raw_title = parts[0].strip()
            raw_content = parts[1].strip() if len(parts) > 1 else doc

            if raw_title.isdigit():
                words = raw_content.split(" ")
                title = " ".join(words[:6])
                preview = raw_content[:120] + "..." if len(raw_content) > 120 else raw_content
            else:
                title = raw_title
                preview = raw_content[:120] + "..." if len(raw_content) > 120 else raw_content

            results.append({
                "id": r[0],
                "title": title,
                "preview": preview,
                "source": r[2],
                "ref_id": r[3],
                "distance": round(r[4], 4)
            })

        return jsonify({"query": query, "results": results})

    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8001, debug=False)