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

                <h2 class="login-title">로그인</h2>

                <%-- 로그인 실패 메시지 --%>
                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger text-center">
                    <%= request.getAttribute("error") %>
                </div>
                <% } %>

                <!-- 🔴 GET → POST 로 수정 -->
                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label class="form-label">아이디 *</label>
                        <input type="text" name="userId" class="form-control"
                               placeholder="아이디 입력" required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label">비밀번호 *</label>
                        <input type="password" name="password" class="form-control"
                               placeholder="비밀번호 입력" required>
                    </div>

                    <button type="submit" class="btn btn-secondary w-100">
                        로그인
                    </button>
                </form>

                <div class="login-links">
                    <div>
                        아직 회원이 아니신가요?
                        <a href="${pageContext.request.contextPath}/signup">회원가입</a>
                    </div>
                    <div class="mt-1">
                        변호사이신가요?
                        <a href="${pageContext.request.contextPath}/lawyer">
                            변호사 회원가입
                        </a>
                    </div>
                </div>

            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>