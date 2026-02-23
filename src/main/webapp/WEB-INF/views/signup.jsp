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
        보내주신 코드는 일반 회원가입을 담당하는 signup.jsp 파일의 내용입니다. 디자인(style)은 그대로 유지하면서 서버와 데이터가 잘 연결되도록 딱 한 줄만 추가해서 정리해 드릴게요.

        이 코드는 프로젝트 폴더 내의 WEB-INF/views/ 폴더 아래에 있는 signup.jsp 파일에 전체 복사해서 덮어씌우시면 됩니다.

        🛠️ signup.jsp (최종본)
        Java
        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        <!DOCTYPE html>
        <html lang="ko">
        <head>
            <jsp:include page="/WEB-INF/views/common/header.jsp" />

            <style>
                /* 기존 스타일 유지 */
                .login-wrapper { min-height: 70vh; display: flex; align-items: center; justify-content: center; }
                .login-card { width: 100%; max-width: 420px; padding: 40px 35px; background: #fff; border-radius: 12px; box-shadow: 0 8px 24px rgba(0,0,0,0.08); }
                .login-title { text-align: center; font-weight: 700; margin-bottom: 30px; }
                .login-links { text-align: center; margin-top: 18px; font-size: 14px; }
                .login-links a { color: #0d6efd; text-decoration: none; }
                .login-links a:hover { text-decoration: underline; }
            </style>
        </head>
        <body>

        <main class="main">
            <section class="section">
                <div class="container login-wrapper">
                    <div class="login-card">

                        <h2 class="text-center mb-4">회원가입</h2>

                        <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger text-center">
                            <%= request.getAttribute("error") %>
                        </div>
                        <% } %>

                        <form action="${pageContext.request.contextPath}/signup" method="post">

                            <input type="hidden" name="role" value="USER">

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
                                <input type="password" name="passwordConfirm" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">이메일 *</label>
                                <input type="email" name="email" class="form-control" required>
                            </div>

                            <div class="text-center mt-3 mb-3">
                                <a href="${pageContext.request.contextPath}/login">이미 계정이 있으신가요? → 로그인</a>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">회원가입</button>
                        </form>

                    </div>
                </div>
            </section>
        </main>

        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        </body>
        </html>