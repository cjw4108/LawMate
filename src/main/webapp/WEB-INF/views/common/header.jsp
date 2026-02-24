<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="loginUser" value="${sessionScope.loginUser}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>LawMate - 스마트 법률 파트너</title>
    <link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
    <link href="/css/main.css" rel="stylesheet">
    <link href="/css/custom.css" rel="stylesheet">
</head>
<body class="index-page">
<header id="header" class="header d-flex align-items-center fixed-top bg-white border-bottom shadow-sm" style="height: 70px;">
    <div class="container-xl d-flex align-items-center justify-content-between">
        <a href="/home" class="logo d-flex align-items-center me-auto">
            <img src="/img/logo.png" alt="LawMate" style="max-height: 35px;">
        </a>
        <nav id="navmenu" class="navmenu">
            <ul class="d-flex align-items-center mb-0">
                <!-- 좌측 메뉴: 법률백과, 가이드, Q&A, 상담신청, 서류양식 다운로드, AI 상담 -->
                <li style="margin-right: 20px;"><a href="/legal-dictionary" class="nav-link text-dark">법률백과</a></li>
                <li style="margin-right: 20px;"><a href="/category" class="nav-link text-dark">가이드</a></li>
                <li style="margin-right: 20px;"><a href="/qna/list" class="nav-link text-dark">Q&A</a></li>
                <li style="margin-right: 20px;"><a href="/consult/consultList" class="nav-link text-dark">상담신청</a></li>
                <li style="margin-right: 20px;"><a href="/docs" class="nav-link text-dark">서류양식</a></li>
                <li style="margin-right: 20px;"><a href="/home#" class="nav-link text-dark">AI 상담</a></li>

                <!-- 우측 메뉴: 서류함, 로그인/마이페이지 -->
                <li style="margin-right: 30px; position: relative;">
                    <a href="/cart" title="내 서류함" style="text-decoration: none;">
                        <i class="bi bi-folder2" style="font-size: 28px; color: #1e293b;"></i>
                        <span id="cartCount" style="position: absolute; top: 0px; right: -5px; background: #ef4444; color: white; border-radius: 10px; min-width: 18px; height: 18px; padding: 0 4px; font-size: 10px; display: flex; align-items: center; justify-content: center; font-weight: 600; line-height: 1;">0</span>
                    </a>
                </li>

                <li class="dropdown">
                <li class="dropdown">

                    <!-- 로그인 안 된 경우 -->
                    <c:if test="${empty loginUser}">
                        <a href="/login" class="btn btn-primary rounded-pill px-4 text-white">
                            로그인
                        </a>
                    </c:if>

                    <!-- 로그인 된 경우 -->
                    <c:if test="${not empty loginUser}">
                        <a href="#" class="btn btn-primary rounded-pill px-4 text-white" data-bs-toggle="dropdown">
                                ${loginUser.userId}님
                            <i class="bi bi-chevron-down ms-1"></i>
                        </a>

                        <ul class="dropdown-menu shadow border-0">

                            <!-- 일반 회원 -->
                            <c:if test="${loginUser.role eq 'ROLE_USER'}">
                                <li><a class="dropdown-item" href="#">마이페이지</a></li>
                                <li><a class="dropdown-item" href="#">상담 내역</a></li>
                                <li><a class="dropdown-item" href="#">문서 관리</a></li>
                            </c:if>

                            <!-- 변호사 -->
                            <c:if test="${loginUser.role eq 'ROLE_LAWYER'}">
                                <li><a class="dropdown-item" href="#">변호사 마이페이지</a></li>
                            </c:if>

                            <!-- 관리자 -->
                            <c:if test="${loginUser.role eq 'ROLE_ADMIN'}">
                                <li><a class="dropdown-item text-danger fw-bold" href="/admin/main">관리자 페이지</a></li>
                            </c:if>

                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="/logout">로그아웃</a></li>
                        </ul>
                    </c:if>

                </li>

                </li>
            </ul>
        </nav>
    </div>
</header>