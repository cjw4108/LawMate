<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <style>
        .signup-wrapper {
            min-height: 70vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .signup-card {
            width: 100%;
            max-width: 420px;
            padding: 40px 35px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.08);
        }
        .signup-title {
            text-align: center;
            font-weight: 700;
            margin-bottom: 30px;
        }
        .login-links {
            text-align: center;
            margin-top: 18px;
            font-size: 14px;
        }
        .login-links a {
            color: #0d6efd;
            text-decoration: none;
        }
        .login-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body class="bg-light">
<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <div class="card p-4" style="width: 400px; border-radius: 15px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
        <h3 class="text-center mb-4">회원가입(일반)</h3>

        <form action="${pageContext.request.contextPath}/signup" method="post">
            <%-- DTO 자동입력을 위한 히든 필드 --%>
            <input type="hidden" name="role" value="ROLE_USER">
            <input type="hidden" name="lawyerStatus" value="NONE">

            <div class="mb-3">
                <label class="form-label">아이디 <span class="text-danger">*</span></label>
                <input type="text" name="userId" class="form-control" placeholder="아이디 입력" required>
            </div>

            <div class="mb-3">
                <label class="form-label">비밀번호 <span class="text-danger">*</span></label>
                <input type="password" name="password" class="form-control" placeholder="비밀번호 입력" required>
            </div>

            <div class="mb-3">
                <label class="form-label">비밀번호 확인 <span class="text-danger">*</span></label>
                <input type="password" name="passwordConfirm" class="form-control" placeholder="비밀번호 재 입력" required>
            </div>

            <div class="mb-4">
                <label class="form-label">이메일 <span class="text-danger">*</span></label>
                <input type="email" name="email" class="form-control" placeholder="example@mail.com" required>
            </div>

            <button type="submit" class="btn btn-dark w-100 py-2">회원가입</button>
        </form>

        <div class="login-links mt-3">
            <a href="${pageContext.request.contextPath}/login">이미 계정이 있으신가요? → 로그인</a>
        </div>
    </div>
</div>
</body>
</html>
