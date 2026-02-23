<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <style>
        .login-wrapper {
            min-height: 70vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-card {
            width: 100%;
            max-width: 420px;
            padding: 40px 35px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.08);
        }
        .login-title {
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
<body>
<main class="main">
    <section class="section">
        <div class="container login-wrapper">
            <div class="login-card">
                <h2 class="text-center mb-4">변호사 회원가입</h2>

                <%-- 서버에서 전달된 에러 메시지가 있을 경우 표시 --%>
                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger text-center"><%= request.getAttribute("error") %></div>
                <% } %>

                <%-- 파일 업로드를 위해 enctype="multipart/form-data" 필수 적용 --%>
                <form action="${pageContext.request.contextPath}/signup" method="post" enctype="multipart/form-data">

                    <%-- UserDTO의 role 필드에 'LAWYER' 값 전달 --%>
                    <input type="hidden" name="role" value="LAWYER">

                    <div class="mb-3">
                        <label class="form-label">성함 *</label>
                        <input type="text" name="userName" class="form-control" placeholder="성함을 입력해주세요" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">아이디 *</label>
                        <input type="text" name="userId" class="form-control" placeholder="사용할 아이디 입력" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">비밀번호 *</label>
                        <input type="password" name="password" class="form-control" placeholder="비밀번호 입력" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">비밀번호 확인 *</label>
                        <input type="password" name="passwordConfirm" class="form-control" placeholder="비밀번호 재입력" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">이메일 *</label>
                        <input type="email" name="email" class="form-control" placeholder="example@lawmate.com" required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label">변호사 자격 증빙 파일 *</label>
                        <input type="file" name="licenseFile" class="form-control" required>
                    </div>

                    <button type="submit" class="btn btn-dark w-100 mb-3">변호사 회원가입 신청</button>

                    <div class="text-center">
                        <a href="${pageContext.request.contextPath}/login" style="text-decoration: none; font-size: 14px; color: #666;">
                            이미 계정이 있으신가요? → <span style="color: #0d6efd;">로그인</span>
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </section>
</main>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>