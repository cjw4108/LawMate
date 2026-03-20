<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="loginUser" value="${sessionScope.loginUser}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>LawMate - 스마트 법률 파트너</title>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="/css/main.css" rel="stylesheet">

    <style>
        /* ✅ 사용자님이 마음에 들어하신 '마이페이지 버튼' 크기 통일 */
        .btn-lawmate-main {
            background-color: #007bff !important;
            color: #ffffff !important;
            font-size: 14px !important;
            font-weight: 600 !important;
            padding: 10px 25px !important; /* 이 패딩이 버튼 크기를 결정합니다 */
            height: 42px !important;
            border-radius: 50px !important;
            display: inline-flex !important;
            align-items: center;
            justify-content: center;
            border: none !important;
            text-decoration: none !important;
        }

        /* 서류함 숫자 배지 (id="cartCount") */
        #cartCount {
            position: absolute; top: 0px; right: -5px;
            background: #ef4444; color: white;
            border-radius: 10px; min-width: 18px; height: 18px;
            padding: 0 4px; font-size: 10px;
            display: flex; align-items: center; justify-content: center;
            font-weight: 600; line-height: 1;
        }

        /* 네비게이션 간격 조절 */
        .navmenu ul li { margin-right: 20px; }
        .navmenu ul li:last-child { margin-right: 0; }
    </style>
</head>

<body class="index-page">
<header id="header" class="header d-flex align-items-center fixed-top bg-white border-bottom shadow-sm" style="height: 70px;">
    <div class="container-xl d-flex align-items-center justify-content-between">
        <a href="/home" class="logo d-flex align-items-center me-auto">
            <img src="/img/logo.png" alt="LawMate" style="max-height: 35px;">
        </a>

        <nav id="navmenu" class="navmenu">
            <ul class="d-flex align-items-center mb-0" style="list-style: none;">
                <li><a href="/legal-dictionary" class="nav-link text-dark">법률백과</a></li>
                <li><a href="/category" class="nav-link text-dark">가이드</a></li>
                <li><a href="/qna/list" class="nav-link text-dark">Q&A</a></li>
                <li><a href="/lawyer/list" class="nav-link text-dark">변호사 열람페이지</a></li>
                <li><a href="/docs" class="nav-link text-dark">서류양식</a></li>
                <li><a href="/ai/consult" class="nav-link text-dark">AI 상담</a></li>

                <li style="margin-right: 30px; position: relative;">
                    <a href="/cart" title="내 서류함" style="text-decoration: none;">
                        <i class="bi bi-folder2" style="font-size: 28px; color: #1e293b;"></i>
                        <span id="cartCount">${cartCount != null ? cartCount : 0}</span>
                    </a>
                </li>

                <li class="dropdown">
                    <c:choose>
                        <c:when test="${empty loginUser}">
                            <a href="/login" class="btn-lawmate-main">로그인</a>
                        </c:when>
                        <c:otherwise>
                            <a href="#" class="btn-lawmate-main" data-toggle="dropdown" data-bs-toggle="dropdown">
                                    ${loginUser.userId}님 <i class="bi bi-chevron-down ms-1" style="font-size: 12px;"></i>
                            </a>
                            <ul class="dropdown-menu shadow border-0">
                                <c:if test="${loginUser.role eq 'ROLE_USER'}">
                                    <li><a class="dropdown-item" href="/mypage">마이페이지</a></li>
                                    <li><a class="dropdown-item" href="#">상담 내역</a></li>
                                </c:if>
                                <c:if test="${loginUser.role eq 'ROLE_LAWYER'}">
                                    <li><a class="dropdown-item" href="/mypage">변호사 마이페이지</a></li>
                                </c:if>
                                <c:if test="${loginUser.role eq 'ROLE_ADMIN'}">
                                    <li><a class="dropdown-item" href="/admin/main">관리자페이지</a></li>
                                </c:if>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="/logout">로그아웃</a></li>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </nav>
    </div>
</header>