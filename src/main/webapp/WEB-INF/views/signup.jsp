<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <style>
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
        .id-check-wrapper {
            display: flex;
            gap: 8px;
        }
        .id-check-wrapper input {
            flex: 1;
        }
        #idCheckMsg {
            font-size: 12px;
            margin-top: 4px;
        }
    </style>
</head>
<body class="bg-light">
<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <div class="card p-4" style="width: 400px; border-radius: 15px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
        <h3 class="text-center mb-4">회원가입(일반)</h3>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">${error}</div>
        <% } %>

        <form action="${pageContext.request.contextPath}/signup" method="post" id="signupForm">
            <input type="hidden" name="role" value="ROLE_USER">
            <input type="hidden" name="lawyerStatus" value="NONE">
            <input type="hidden" name="status" value="정상">
            <input type="hidden" name="idChecked" id="idChecked" value="false">

            <!-- 1. 아이디 입력 + 중복확인 버튼 -->
            <div class="mb-3">
                <label class="form-label">아이디 <span class="text-danger">*</span></label>
                <div class="id-check-wrapper">
                    <input type="text" name="userId" id="userId" class="form-control" placeholder="아이디 입력" required>
                    <button type="button" class="btn btn-outline-secondary" onclick="checkDuplicate()">중복확인</button>
                </div>
                <div id="idCheckMsg"></div>
            </div>

            <!-- 2. 이름 입력 -->
            <div class="mb-3">
                <label class="form-label">이름 <span class="text-danger">*</span></label>
                <input type="text" name="name" class="form-control" placeholder="성명을 입력해주세요" required>
            </div>

            <!-- 3. 전화번호 입력 -->
            <div class="mb-3">
                <label class="form-label">전화번호 <span class="text-danger">*</span></label>
                <input type="text" name="phone" class="form-control" placeholder="010-0000-0000" required>
            </div>

            <!-- 4. 비밀번호 입력 -->
            <div class="mb-3">
                <label class="form-label">비밀번호 <span class="text-danger">*</span></label>
                <input type="password" name="password" id="password" class="form-control" placeholder="비밀번호 입력" required>
            </div>

            <!-- 5. 비밀번호 확인 -->
            <div class="mb-3">
                <label class="form-label">비밀번호 확인 <span class="text-danger">*</span></label>
                <input type="password" name="passwordConfirm" id="passwordConfirm" class="form-control" placeholder="비밀번호 재 입력" required>
                <div id="pwHint" style="font-size:12px; margin-top:4px;"></div>
            </div>

            <!-- 6. 이메일 입력 -->
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

<script>
    // 1. 아이디 중복확인
    function checkDuplicate() {
        const userId = document.getElementById('userId').value.trim();
        const msg = document.getElementById('idCheckMsg');

        if (!userId) {
            msg.textContent = '아이디를 입력해주세요.';
            msg.style.color = '#dc3545';
            return;
        }

        fetch('${pageContext.request.contextPath}/check-id?userId=' + encodeURIComponent(userId))
            .then(res => res.json())
            .then(data => {
                if (data.available) {
                    msg.textContent = '사용 가능한 아이디입니다.';
                    msg.style.color = '#198754';
                    document.getElementById('idChecked').value = 'true';
                } else {
                    msg.textContent = '이미 사용 중인 아이디입니다.';
                    msg.style.color = '#dc3545';
                    document.getElementById('idChecked').value = 'false';
                }
            })
            .catch(() => {
                msg.textContent = '중복확인 중 오류가 발생했습니다.';
                msg.style.color = '#dc3545';
            });
    }

    // 아이디 변경 시 중복확인 초기화
    document.getElementById('userId').addEventListener('input', function() {
        document.getElementById('idChecked').value = 'false';
        document.getElementById('idCheckMsg').textContent = '';
    });

    // 2. 전화번호 자동 하이픈
    const phoneInput = document.querySelector('[name=phone]');
    phoneInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/[^0-9]/g, '');
        if (value.length < 4) {
            e.target.value = value;
        } else if (value.length < 8) {
            e.target.value = value.slice(0, 3) + '-' + value.slice(3);
        } else {
            e.target.value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
        }
    });

    // 3. 비밀번호 확인 실시간 체크
    document.getElementById('passwordConfirm').addEventListener('input', function() {
        var pw   = document.getElementById('password').value;
        var hint = document.getElementById('pwHint');
        if (!this.value) { hint.textContent = ''; return; }
        if (pw === this.value) {
            hint.textContent = '비밀번호가 일치합니다.';
            hint.style.color = '#198754';
        } else {
            hint.textContent = '비밀번호가 일치하지 않습니다.';
            hint.style.color = '#dc3545';
        }
    });

    // 4. 제출 전 최종 검증
    document.getElementById('signupForm').addEventListener('submit', function(e) {
        const pw      = document.getElementById('password').value;
        const pwc     = document.getElementById('passwordConfirm').value;
        const phone   = phoneInput.value;
        const checked = document.getElementById('idChecked').value;

        // 중복확인 여부 체크
        if (checked !== 'true') {
            e.preventDefault();
            alert('아이디 중복확인을 해주세요.');
            return;
        }

        // 비밀번호 일치 확인
        if (pw !== pwc) {
            e.preventDefault();
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        // 전화번호 글자수 확인
        if (phone.length < 12) {
            e.preventDefault();
            alert('전화번호를 정확히 입력해주세요.');
            return;
        }
    });
</script>
</body>
</html>

