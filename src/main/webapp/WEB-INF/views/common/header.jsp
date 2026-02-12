<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
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
                <li class="dropdown">
                    <a href="/login" class="btn btn-primary rounded-pill px-4 text-white">
                        <span>로그인</span> <i class="bi bi-chevron-down ms-1"></i>
                    </a>
                    <ul class="shadow border-0">
                        <li><a href="#">마이페이지</a></li>
                        <li><a href="#">상담 내역</a></li>
                        <li><a href="#">문서 관리</a></li>
                        <hr class="dropdown-divider">
                        <li><a href="/admin/main" class="text-danger fw-bold">관리자 페이지</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
    </div>
</header>