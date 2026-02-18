<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
</head>
<body>

<main class="main">
    <section class="section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-5">

                    <h2 class="text-center mb-4">회원가입</h2>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger text-center">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <form action="/signup" method="post">

                        <div class="mb-3">
                            <label class="form-label">아이디 *</label>
                            <input type="text" name="userId" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">비밀번호 *</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">비밀번호 확인 *</label>
                            <input type="text" name="name" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">이메일 *</label>
                            <input type="email" name="email" class="form-control" required>
                        </div>

                        <div class="text-center mt-3">
                            <a href="/login">이미 계정이 있으신가요? → 로그인</a>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">회원가입</button>
                    </form>

                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>