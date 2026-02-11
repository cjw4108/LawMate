<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>변호사 프로필 · 자격 검증</title>

    <style>
        body {
            margin: 0;
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f5f6f8;
        }

        .container {
            display: flex;
            min-height: 100vh;
        }

        /* 사이드바 */
        .sidebar {
            width: 220px;
            background-color: #2b3e50;
            color: #fff;
            padding: 30px 20px;
        }

        .sidebar h2 {
            font-size: 18px;
            margin-bottom: 30px;
        }

        .sidebar a {
            display: block;
            color: #ddd;
            text-decoration: none;
            margin-bottom: 15px;
        }

        .sidebar a:hover {
            color: #fff;
        }

        /* 콘텐츠 */
        .content {
            flex: 1;
            padding: 40px;
        }

        .content h1 {
            font-size: 24px;
            margin-bottom: 30px;
        }

        .box {
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            margin-bottom: 25px;
        }

        .row {
            margin-bottom: 20px;
        }

        .label {
            font-weight: 600;
            margin-bottom: 8px;
            display: block;
        }

        input[type="text"],
        select,
        input[type="file"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .status {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
        }

        .wait {
            background-color: #ffe9c7;
            color: #9a6a00;
        }

        .approve {
            background-color: #d4f4dd;
            color: #1f7a3f;
        }

        .reject {
            background-color: #f8d7da;
            color: #842029;
        }

        button {
            padding: 10px 25px;
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

    <!-- 사이드바 -->
    <div class="sidebar">
        <h2>변호사 페이지</h2>
        <a href="/mypage/lawyer">대시보드</a>
        <a href="/mypage/lawyer/consult">상담 관리</a>
        <a href="/mypage/lawyer/profile">프로필 · 자격 검증</a>
    </div>

    <!-- 콘텐츠 -->
    <div class="content">
        <h1>변호사 프로필 및 자격 검증</h1>

        <!-- 기본 정보 -->
        <div class="box">
            <div class="row">
                <span class="label">이름</span>
                <input type="text" value="홍길동" readonly>
            </div>

            <div class="row">
                <span class="label">전문 분야</span>
                <select>
                    <option>민사</option>
                    <option>형사</option>
                    <option>가사</option>
                    <option>행정</option>
                </select>
            </div>
        </div>

        <!-- 자격 검증 -->
        <div class="box">
            <div class="row">
                <span class="label">변호사 자격 증빙 서류</span>
                <input type="file">
                <p style="font-size: 13px; color: #666; margin-top: 6px;">
                    ※ 변호사 자격증 또는 등록증 사본을 업로드해주세요.
                </p>
            </div>

            <div class="row">
                <span class="label">승인 상태</span>
                <!-- 기본값: 승인 대기 -->
                <span class="status wait">승인 대기</span>
                <!--
                승인 완료 시: <span class="status approve">승인 완료</span>
                반려 시: <span class="status reject">반려</span>
                -->
            </div>

            <button>자격 검증 요청</button>
        </div>

    </div>
</div>
</body>
</html>