<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>일반 회원 마이페이지</title>

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
            background-color: #1e2a38;
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
            margin-bottom: 20px;
        }

        .box {
            background-color: #fff;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
        }
    </style>
</head>

<body>
<div class="container">

    <div class="sidebar">
        <h2>마이페이지</h2>
        <a href="/mypage/user">대시보드</a>
        <a href="/mypage/user/consult">상담 내역</a>
        <a href="/mypage/user/profile">회원 정보</a>
    </div>

    <div class="content">
        <h1>일반 회원 마이페이지</h1>
        <div class="box">
            <p>상담 내역 및 다운로드 문서 이력을 확인할 수 있습니다.</p>
        </div>
    </div>

</div>
</body>
</html>