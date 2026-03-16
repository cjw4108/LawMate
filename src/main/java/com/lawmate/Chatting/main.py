import os

os.environ["GRPC_VERBOSITY"] = "ERROR"
os.environ["GLOG_minloglevel"] = "2"

from fastapi import FastAPI, UploadFile, File
from google import genai
import io
from PIL import Image
import fitz

app = FastAPI()


raw_api_key = "AIzaSyDU3Ij6EMTpXt3nODrrfxLDFlQskFGiJko" # 👈 여기에 실제 키를 정확히 입력하세요
api_key = raw_api_key.strip().replace("\n", "").replace("\r", "")
client = genai.Client(api_key=api_key)

@app.post("/analyze")
async def analyze_file(file: UploadFile = File(...)):
    try:
        file_bytes = await file.read()
        ext = file.filename.split('.')[-1].lower()

        # 분석 메시지 공통화
        prompt = "이 파일의 내용을 법률적인 관점에서 분석해줘."

        if ext in ['jpg', 'jpeg', 'png', 'webp']:
            image = Image.open(io.BytesIO(file_bytes))
            response = client.models.generate_content(
                model="gemini-3-flash-preview",
                contents=[prompt, image]
            )
        elif ext == 'pdf':
            doc = fitz.open(stream=file_bytes, filetype="pdf")
            text = "".join([page.get_text() for page in doc])
            response = client.models.generate_content(
                model="gemini-3-flash-preview",
                contents=[f"{prompt}\n\n내용:\n{text[:10000]}"]
            )
        elif ext == 'txt':
            text = file_bytes.decode('utf-8', errors='ignore')
            response = client.models.generate_content(
                model="gemini-3-flash-preview",
                contents=[f"{prompt}\n\n내용:\n{text}"]
            )
        else:
            return {"status": "error", "analysis": "지원하지 않는 파일입니다."}

        return {"status": "success", "analysis": response.text}

    except Exception as e:
        return {"status": "error", "analysis": f"분석 중 오류: {str(e)}"}