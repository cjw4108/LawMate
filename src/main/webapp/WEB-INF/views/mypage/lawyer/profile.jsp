<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <style>
        body { background: #f4f6f9; }
        .mypage-container { max-width: 860px; margin: 40px auto; padding: 0 16px; }
        .tab-nav {
            display: flex; gap: 6px; margin-bottom: 24px;
            background: #fff; padding: 8px; border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.06);
        }
        .tab-nav a {
            flex: 1; padding: 10px 0; border-radius: 7px;
            font-size: 15px; font-weight: 500; color: #888;
            text-align: center; text-decoration: none; transition: all 0.2s;
        }
        .tab-nav a.active { background: #212529; color: #fff; font-weight: 700; }
        .tab-nav a:hover:not(.active) { background: #f0f0f0; color: #333; }
        .card-box {
            background: #fff; border-radius: 12px; padding: 32px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.07); margin-bottom: 20px;
        }
        .two-col { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        .section-title { font-size: 20px; font-weight: 700; margin-bottom: 24px; }
        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; font-size: 13px; font-weight: 600; color: #555; margin-bottom: 6px; }
        .form-group input, .form-group textarea {
            width: 100%; padding: 10px 14px; border: 1px solid #ddd;
            border-radius: 8px; font-size: 14px; background: #f9f9f9; box-sizing: border-box;
        }
        .form-group input:disabled { background: #f0f0f0; color: #999; cursor: not-allowed; }
        .form-group input:focus, .form-group textarea:focus { outline: none; border-color: #212529; background: #fff; }
        .btn-dark {
            width: 100%; padding: 12px; background: #212529; color: #fff;
            border: none; border-radius: 8px; font-size: 15px; font-weight: 600;
            cursor: pointer; margin-top: 8px;
        }
        .btn-dark:hover { background: #343a40; }
        .alert-box { padding: 12px 16px; border-radius: 8px; font-size: 14px; margin-bottom: 16px; }
        .alert-danger { background: #ffebee; color: #c62828; border: 1px solid #ef9a9a; }
        .alert-success { background: #e8f5e9; color: #2e7d32; border: 1px solid #a5d6a7; }
    </style>
</head>
<body>
<div class="mypage-container">

    <div class="tab-nav">
        <a href="${pageContext.request.contextPath}/mypage/lawyer">마이페이지</a>
        <a href="${pageContext.request.contextPath}/mypage/lawyer/consult">상담 내역</a>
        <a href="${pageContext.request.contextPath}/mypage/lawyer/profile" class="active">프로필/자격신청</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert-box alert-danger">${error}</div>
    </c:if>
    <c:if test="${param.msg == 'reapplySuccess'}">
        <div class="alert-box alert-success">✅ 자격 재신청이 완료되었습니다. 심사 후 결과를 안내해드립니다.</div>
    </c:if>

    <div class="two-col">
        <!-- 프로필 수정 -->
        <div class="card-box">
            <div class="section-title">👤 프로필 수정</div>
            <form action="${pageContext.request.contextPath}/mypage/lawyer/profile/update" method="post">
                <div class="form-group">
                    <label>아이디 (수정 불가)</label>
                    <input type="text" value="${user.userId}" disabled>
                </div>
                <div class="form-group">
                    <label>이름</label>
                    <input type="text" name="userName" value="${user.userName}">
                </div>
                <div class="form-group">
                    <label>이메일</label>
                    <input type="email" name="email" value="${user.email}">
                </div>
                <div class="form-group">
                    <label>가입일 (수정 불가)</label>
                    <input type="text" value="${user.joinDate}" disabled>
                </div>
                <button type="submit" class="btn-dark">수정</button>
            </form>
        </div>

        <!-- 자격 신청/재신청 -->
        <div class="card-box">
            <div class="section-title">📋 자격 신청/재신청</div>
            <div class="form-group">
                <label>신청 횟수</label>
                <input type="text" value="${user.applyCount} / 3" disabled>
            </div>
            <div class="form-group">
                <label>반려 사유</label>
                <input type="text" value="${user.rejectReason}" placeholder="반려 시 표시" disabled>
            </div>
            <c:choose>
                <c:when test="${user.applyCount >= 3}">
                    <div class="alert-box alert-danger">신청 횟수(3회)를 초과하였습니다. 관리자에게 문의하세요.</div>
                </c:when>
                <c:when test="${user.lawyerStatus == 'APPROVED'}">
                    <div class="alert-box alert-success">✅ 이미 승인된 상태입니다.</div>
                </c:when>
                <c:otherwise>
                    <form action="${pageContext.request.contextPath}/mypage/lawyer/cert/apply" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label>자격 증빙 파일 업로드 <span style="color:red">*</span></label>
                            <input type="file" name="licenseFile" accept=".pdf, image/*" required>
                        </div>
                        <button type="submit" class="btn-dark">제출</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</div>
</body>
</html>
