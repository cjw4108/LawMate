<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <style>
        .lawyer-wrapper {
            min-height: 70vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #f8f9fa;
            padding: 50px 0;
        }
        .lawyer-card {
            width: 100%;
            max-width: 420px;
            padding: 40px 35px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.08);
        }
        .signup-links {
            text-align: center;
            margin-top: 18px;
            font-size: 14px;
        }
        .signup-links a {
            color: #0d6efd;
            text-decoration: none;
        }
        .signup-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body class="bg-light">
<div class="lawyer-wrapper">
    <div class="lawyer-card">
        <h3 class="text-center mb-4">회원가입(변호사)</h3>

        <form action="${pageContext.request.contextPath}/signup/lawyer" method="post" enctype="multipart/form-data">
            <input type="hidden" name="role" value="ROLE_LAWYER">
            <input type="hidden" name="lawyerStatus" value="PENDING">

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
            <div class="mb-3">
                <label class="form-label">이메일 <span class="text-danger">*</span></label>
                <input type="email" name="email" class="form-control" placeholder="example@mail.com" required>
            </div>
            <div class="mb-4">
                <label class="form-label">자격 증빙 파일 업로드 <span class="text-danger">*</span></label>
                <input type="file" name="uploadFile" class="form-control" accept=".pdf, image/*" required>
            </div>

            <button type="submit" class="btn btn-dark w-100 py-2">회원가입</button>
        </form>

        <div class="signup-links mt-3">
            <a href="${pageContext.request.contextPath}/login">이미 계정이 있으신가요? → 로그인</a>
        </div>
    </div>
</div>
</body>
</html>