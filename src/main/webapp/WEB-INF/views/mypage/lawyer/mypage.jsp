<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <meta charset="UTF-8">
    <title>관리 · 변호사 자격 검증</title>

    <style>
        body {
            margin: 0;
            font-family: 'Noto Sans KR', sans-serif;
            background: #f5f6f8;
        }
        .wrap {
            padding: 40px;
        }
        .tab-title {
            font-size: 22px;
            margin-bottom: 30px;
        }
        .section {
            background: #fff;
            padding: 25px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .section h3 {
            margin-bottom: 20px;
        }
        .row {
            margin-bottom: 15px;
        }
        .label {
            display: inline-block;
            width: 120px;
            font-weight: 600;
        }
        .status {
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 13px;
        }
        .wait { background: #ffe9c7; }
        .approve { background: #d4f4dd; }
        .reject { background: #f8d7da; }
        button {
            padding: 8px 22px;
        }
        textarea {
            width: 100%;
        }
    </style>
</head>

<body>
<div class="wrap">

    <div class="tab-title">관리</div>

    <!-- 승인 상태 -->
    <div class="section">
        <h3>변호사 권한 승인 상태</h3>

        <div class="row">
            <span class="label">승인 상태</span>
            <span class="status wait">승인 대기</span>
        </div>
        <div class="row">
            <span class="label">신청일</span>
            <span>YYYY-MM-DD</span>
        </div>
        <div class="row">
            <span class="label">승인일</span>
            <span>-</span>
        </div>
        <div class="row">
            <span class="label">반려 사유</span>
            <span>-</span>
        </div>

        <button>자격 검증 / 재신청</button>
    </div>

    <!-- 권한 신청 -->
    <div class="section">
        <h3>변호사 권한 신청</h3>

        <div class="row">
            <span class="label">신청 사유</span><br>
            <textarea rows="4"></textarea>
        </div>

        <div class="row">
            <span class="label">자격 증빙</span>
            <input type="file">
        </div>

        <button>제출</button>
    </div>

</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>