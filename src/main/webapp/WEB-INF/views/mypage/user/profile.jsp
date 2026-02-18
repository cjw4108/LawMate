<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>자격 검증</title>

    <style>
        body {
            margin: 0;
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f5f6f8;
        }

        .container {
            max-width: 700px;
            margin: 60px auto;
            background: #fff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.08);
        }

        h1 {
            font-size: 22px;
            margin-bottom: 30px;
        }

        .row {
            margin-bottom: 20px;
        }

        .label {
            display: block;
            font-weight: 600;
            margin-bottom: 8px;
        }

        input[type="text"],
        textarea,
        input[type="file"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        textarea {
            resize: vertical;
        }

        .status {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 13px;
            background-color: #ffe9c7;
            color: #9a6a00;
            font-weight: bold;
        }

        button {
            margin-top: 30px;
            padding: 12px 30px;
            background-color: #2b3e50;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #1f2e3d;
        }
    </style>
</head>

<body>

<div class="container">
    <h1>자격 검증 신청</h1>

    <!-- 현재 상태 -->
    <div class="row">
        <span class="label">현재 상태</span>
        <span class="status">승인 대기</span>
    </div>

    <!-- 신청 사유 -->
    <div class="row">
        <label class="label">신청 사유</label>
        <textarea rows="4" placeholder="자격 검증 신청 사유를 입력하세요."></textarea>
    </div>

    <!-- 증빙 파일 -->
    <div class="row">
        <label class="label">증빙 파일 업로드</label>
        <input type="file">
    </div>

    <button type="button">자격 검증 요청</button>
</div>

</body>
</html>