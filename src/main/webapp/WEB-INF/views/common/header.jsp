<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>LawMate - 서비스</title>

    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&family=Inter:wght@400;700&family=Nunito:wght@400;700&display=swap" rel="stylesheet">
    <link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
    <link href="/vendor/aos/aos.css" rel="stylesheet">
    <link href="/css/main.css" rel="stylesheet">
</head>

<body class="index-page">
<header id="header" class="header d-flex align-items-center fixed-top bg-white border-bottom">
    <div class="header-container container-fluid container-xl position-relative d-flex align-items-center justify-content-between">
        <a href="/" class="logo d-flex align-items-center me-auto">
            <img src="/img/logo.png" alt="LawMate Logo" style="max-height: 40px;">
        </a>

        <nav id="navmenu" class="navmenu">
            <ul>
                <li class="dropdown">
                    <a href="#" class="btn-getstarted text-dark"><span>로그인</span> <i class="bi bi-chevron-down toggle-dropdown"></i></a>
                    <ul>
                        <li><a href="#">마이페이지</a></li>
                        <li><a href="#">상담 내역</a></li>
                        <li><a href="#">문서</a></li>
                        <li><a href="#">관리</a></li>
                        <li><a href="#">관리자페이지(관리자만)</a></li>
                    </ul>
                </li>
            </ul>
            <i class="mobile-nav-toggle d-xl-none bi bi-list"></i>
        </nav>
    </div>
</header>