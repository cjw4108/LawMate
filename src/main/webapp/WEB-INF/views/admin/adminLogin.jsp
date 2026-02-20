<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 로그인 | LAWMATE</title>

    <!-- 공통 헤더 -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <style>
        body {
            background-color: #f1f3f5;
        }

        .admin-login-wrapper {
            min-height: 80vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .admin-login-box {
            width: 420px;
            padding: 40px;
            background: #ffffff;
            border-radius: 10px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.08);
        }

        .admin-login-title {
            text-align: center;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .admin-login-sub {
            text-align: center;
            font-size: 14px;
            color: #777;
            margin-bottom: 30px;
        }

        .form-group {
            margin-bottom: 18px;
        }

        .form-label {
            font-weight: 600;
            margin-bottom: 6px;
            display: block;
        }

        .form-control {
            height: 44px;
        }

        .login-btn {
            width: 100%;
            height: 46px;
            background-color: #212529;
            color: #fff;
            border: none;
            font-size: 15px;
            font-weight: 600;
        }

        .login-btn:hover {
            background-color: #000;
        }

        .error-msg {
            margin-top: 20px;
            color: #d63333;
            font-size: 14px;
            text-align: center;
        }

        .security-text {
            margin-top: 25px;
            font-size: 12px;
            color: #999;
            text-align: center;
            line-height: 1.6;
        }
    </style>
</head>

<body>

<main class="admin-login-wrapper">
    <div class="admin-login-box">

        <h2 class="admin-login-title">관리자 로그인</h2>
        <p class="admin-login-sub">
            본 페이지는 관리자 전용입니다.
        </p>

        <form action="${pageContext.request.contextPath}/admin/login"
              method="post"
              autocomplete="off">

            <!-- 관리자 아이디 -->
            <div class="form-group">
                <label class="form-label" for="adminId">아이디</label>
                <input type="text"
                       class="form-control"
                       id="adminId"
                       name="adminId"
                       placeholder="관리자 아이디"
                       required
                       autofocus>
            </div>

            <!-- 관리자 비밀번호 -->
            <div class="form-group">
                <label class="form-label" for="adminPw">비밀번호</label>
                <input type="password"
                       class="form-control"
                       id="adminPw"
                       name="adminPw"
                       placeholder="비밀번호"
                       required>
            </div>

            <!-- CSRF 토큰 (Spring Security 사용 시 자동 주입) -->
            <c:if test="${not empty _csrf}">
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}">
            </c:if>

            <button type="submit" class="login-btn">
                로그인
            </button>
        </form>

        <!-- 로그인 실패 메시지 -->
        <c:if test="${not empty errorMsg}">
            <div class="error-msg">
                    ${errorMsg}
            </div>
        </c:if>

        <div class="security-text">
            ⓒ LAWMATE ADMIN SYSTEM<br>
            무단 접근 시 법적 책임이 발생할 수 있습니다.
        </div>

    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>