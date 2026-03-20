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
<div class="lawyer-wrapper">
    <div class="lawyer-card">
        <h3 class="text-center mb-4">회원가입(변호사)</h3>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">${error}</div>
        <% } %>

        <form action="${pageContext.request.contextPath}/signup/lawyer" method="post" enctype="multipart/form-data" id="lawyerForm">
            <input type="hidden" name="role" value="ROLE_LAWYER">
            <input type="hidden" name="lawyerStatus" value="PENDING">
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

            <!-- 2. 성함 입력 -->
            <div class="mb-3">
                <label class="form-label">성함 <span class="text-danger">*</span></label>
                <input type="text" name="name" class="form-control" placeholder="성함을 입력해주세요" required>
            </div>

            <!-- 3. 전화번호 입력 -->
            <div class="mb-3">
                <label class="form-label">전화번호 <span class="text-danger">*</span></label>
                <input type="text" name="phone" class="form-control" placeholder="010-0000-0000" maxlength="13" required>
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
                <div id="pwHint"></div>
            </div>

            <!-- 6. 이메일 입력 -->
            <div class="mb-3">
                <label class="form-label">이메일 <span class="text-danger">*</span></label>
                <input type="email" name="email" class="form-control" placeholder="example@mail.com" required>
            </div>

            <!-- 7. 자격 증빙 파일 업로드 -->
            <div class="mb-4">
                <label class="form-label">자격 증빙 파일 업로드 <span class="text-danger">*</span></label>
                <input type="file" name="uploadFile" class="form-control" accept=".pdf, image/*" required>
                <small class="text-muted">PDF 또는 이미지 파일만 가능합니다.</small>
            </div>

            <button type="submit" class="btn btn-dark w-100 py-2">회원가입 신청</button>
        </form>

        <div class="signup-links mt-3">
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
    const phoneInput = document.querySelector('input[name="phone"]');
    phoneInput.addEventListener('input', function(e) {
        let val = e.target.value.replace(/[^0-9]/g, '').substring(0, 11);
        let result = '';
        if (val.length < 4) {
            result = val;
        } else if (val.length < 8) {
            result = val.substring(0, 3) + '-' + val.substring(3);
        } else {
            result = val.substring(0, 3) + '-' + val.substring(3, 7) + '-' + val.substring(7);
        }
        e.target.value = result;
    });

    // 3. 비밀번호 일치 실시간 체크
    const pwInput = document.getElementById('password');
    const pwConfirmInput = document.getElementById('passwordConfirm');
    const pwHint = document.getElementById('pwHint');

    function checkPassword() {
        if (!pwConfirmInput.value) {
            pwHint.textContent = '';
            return;
        }
        if (pwInput.value === pwConfirmInput.value) {
            pwHint.textContent = '비밀번호가 일치합니다.';
            pwHint.style.color = '#198754';
            pwHint.style.fontSize = '12px';
        } else {
            pwHint.textContent = '비밀번호가 일치하지 않습니다.';
            pwHint.style.color = '#dc3545';
            pwHint.style.fontSize = '12px';
        }
    }

    pwConfirmInput.addEventListener('input', checkPassword);
    pwInput.addEventListener('input', checkPassword);

    // 4. 폼 제출 전 최종 유효성 검사
    document.getElementById('lawyerForm').addEventListener('submit', function(e) {
        const phone = phoneInput.value;
        const phoneReg = /^010-\d{3,4}-\d{4}$/;
        const checked = document.getElementById('idChecked').value;

        // 중복확인 여부 체크
        if (checked !== 'true') {
            e.preventDefault();
            alert('아이디 중복확인을 해주세요.');
            return;
        }

        // 비밀번호 최종 체크
        if (pwInput.value !== pwConfirmInput.value) {
            e.preventDefault();
            alert('비밀번호가 일치하지 않습니다.');
            pwConfirmInput.focus();
            return;
        }

        // 전화번호 형식 최종 체크
        if (!phoneReg.test(phone)) {
            e.preventDefault();
            alert('전화번호 11자리를 모두 입력해주세요. (예: 010-1234-5678)');
            phoneInput.focus();
            return;
        }

        // 파일 업로드 여부 재확인
        const fileInput = document.querySelector('input[name="uploadFile"]');
        if (!fileInput.files.length) {
            e.preventDefault();
            alert('자격 증빙 파일을 업로드해주세요.');
            return;
        }
    });
</script>
</body>
</html>
