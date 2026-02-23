<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <style>
        body { background: #f4f6f9; }
        .admin-container { max-width: 1100px; margin: 40px auto; padding: 0 16px; }
        h2 { text-align: center; font-size: 24px; font-weight: 700; margin-bottom: 6px; }
        .sub-title { text-align: center; color: #888; font-size: 14px; margin-bottom: 28px; }

        .alert-box { padding: 12px 16px; border-radius: 8px; font-size: 14px; margin-bottom: 20px; }
        .alert-danger  { background: #ffebee; color: #c62828; border: 1px solid #ef9a9a; }
        .alert-success { background: #e8f5e9; color: #2e7d32; border: 1px solid #a5d6a7; }

        .admin-grid { display: grid; grid-template-columns: 300px 1fr; gap: 20px; }
        .card-box {
            background: #fff; border-radius: 12px; padding: 24px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.07);
        }
        .list-title { font-size: 14px; font-weight: 700; color: #555; margin-bottom: 12px; }

        .user-list { list-style: none; padding: 0; margin: 0; }
        .user-list li {
            padding: 10px 12px; border-radius: 7px; cursor: pointer;
            font-size: 14px; display: flex; justify-content: space-between;
            align-items: center; border-bottom: 1px solid #f0f0f0; transition: background 0.15s;
        }
        .user-list li:hover { background: #f4f6f9; }
        .user-list li.selected { background: #212529; color: #fff; }

        .badge-pending  { background: #fff3e0; color: #e65100; padding: 2px 8px; border-radius: 20px; font-size: 11px; }
        .badge-approved { background: #e8f5e9; color: #2e7d32; padding: 2px 8px; border-radius: 20px; font-size: 11px; }
        .badge-rejected { background: #ffebee; color: #c62828; padding: 2px 8px; border-radius: 20px; font-size: 11px; }

        .detail-title { font-size: 15px; font-weight: 700; color: #555; margin-bottom: 16px; }
        .form-group { margin-bottom: 14px; }
        .form-group label { display: block; font-size: 13px; font-weight: 600; color: #555; margin-bottom: 5px; }
        .form-group input, .form-group textarea {
            width: 100%; padding: 9px 12px; border: 1px solid #ddd;
            border-radius: 7px; font-size: 14px; background: #f9f9f9; box-sizing: border-box;
        }
        .form-group input:disabled { color: #999; cursor: not-allowed; }
        .form-group textarea { resize: none; height: 60px; }
        .btn-row { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-top: 10px; }
        .btn-approve {
            padding: 11px; background: #212529; color: #fff;
            border: none; border-radius: 7px; font-size: 15px; font-weight: 600; cursor: pointer;
        }
        .btn-approve:hover { background: #343a40; }
        .btn-reject {
            padding: 11px; background: #fff; color: #e74c3c;
            border: 2px solid #e74c3c; border-radius: 7px; font-size: 15px; font-weight: 600; cursor: pointer;
        }
        .btn-reject:hover { background: #e74c3c; color: #fff; }
        .empty-detail { display: flex; align-items: center; justify-content: center; height: 200px; color: #bbb; font-size: 14px; }

        .file-link { font-size: 13px; color: #0d6efd; text-decoration: none; margin-top: 4px; display: inline-block; }
        .file-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="admin-container">
    <h2>관리자 변호사 승인</h2>
    <p class="sub-title">(회원 검색 및 변호사 승인)</p>

    <c:if test="${not empty error}">
        <div class="alert-box alert-danger">${error}</div>
    </c:if>
    <c:if test="${param.msg == 'approved'}">
        <div class="alert-box alert-success">✅ 승인이 완료되었습니다.</div>
    </c:if>
    <c:if test="${param.msg == 'rejected'}">
        <div class="alert-box alert-danger">반려 처리되었습니다.</div>
    </c:if>

    <div class="admin-grid">
        <!-- 사용자 목록 -->
        <div class="card-box">
            <div class="list-title">승인 대기 변호사 목록</div>
            <ul class="user-list">
                <c:choose>
                    <c:when test="${empty lawyers}">
                        <li style="color:#bbb; justify-content:center;">대기 중인 신청이 없습니다.</li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="lawyer" items="${lawyers}">
                            <li onclick="selectLawyer('${lawyer.userId}', '${lawyer.userName}', '${lawyer.lawyerStatus}', '${lawyer.licenseFile}', '${lawyer.rejectReason}', this)">
                                <span>${lawyer.userName} (${lawyer.userId})</span>
                                <span class="badge-pending">대기</span>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>

            <div class="list-title" style="margin-top:20px;">전체 변호사 목록</div>
            <ul class="user-list">
                <c:choose>
                    <c:when test="${empty allLawyers}">
                        <li style="color:#bbb; justify-content:center;">변호사가 없습니다.</li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="lawyer" items="${allLawyers}">
                            <li onclick="selectLawyer('${lawyer.userId}', '${lawyer.userName}', '${lawyer.lawyerStatus}', '${lawyer.licenseFile}', '${lawyer.rejectReason}', this)">
                                <span>${lawyer.userName} (${lawyer.userId})</span>
                                <span class="
                                    <c:choose>
                                        <c:when test='${lawyer.lawyerStatus == \"APPROVED\"}'>badge-approved</c:when>
                                        <c:when test='${lawyer.lawyerStatus == \"REJECTED\"}'>badge-rejected</c:when>
                                        <c:otherwise>badge-pending</c:otherwise>
                                    </c:choose>
                                ">
                                    <c:choose>
                                        <c:when test="${lawyer.lawyerStatus == 'APPROVED'}">승인</c:when>
                                        <c:when test="${lawyer.lawyerStatus == 'REJECTED'}">반려</c:when>
                                        <c:otherwise>대기</c:otherwise>
                                    </c:choose>
                                </span>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>

        <!-- 상세/승인 패널 -->
        <div class="card-box" id="detailPanel">
            <div class="empty-detail">목록에서 변호사를 선택하세요.</div>
        </div>
    </div>
</div>

<script>
    function selectLawyer(userId, userName, status, licenseFile, rejectReason, el) {
        document.querySelectorAll('.user-list li').forEach(li => li.classList.remove('selected'));
        el.classList.add('selected');

        const statusLabel = status === 'APPROVED' ? '승인 완료' : status === 'REJECTED' ? '반려' : '승인 대기';
        const fileHtml = licenseFile && licenseFile !== 'null'
            ? `<input type="text" value="${licenseFile}" disabled>
               <a href="${pageContext.request.contextPath}/admin/lawyer/file/${userId}" target="_blank" class="file-link">📎 파일 보기</a>`
            : `<input type="text" value="첨부 파일 없음" disabled>`;

        document.getElementById('detailPanel').innerHTML = `
            <div class="detail-title">사용자 상세 / 승인 처리</div>
            <div class="form-group">
                <label>아이디</label>
                <input type="text" value="${userId}" disabled>
            </div>
            <div class="form-group">
                <label>이름</label>
                <input type="text" value="${userName}" disabled>
            </div>
            <div class="form-group">
                <label>업로드 한 서류</label>
                ${fileHtml}
            </div>
            <div class="form-group">
                <label>승인 상태</label>
                <input type="text" value="${statusLabel}" disabled>
            </div>
            <div class="form-group">
                <label>반려 사유 입력 (반려 시 필수)</label>
                <textarea id="rejectReasonInput" placeholder="반려 사유를 입력하세요..."></textarea>
            </div>
            <div class="btn-row">
                <button class="btn-approve" onclick="submitApprove('${userId}')">승인</button>
                <button class="btn-reject"  onclick="submitReject('${userId}')">반려</button>
            </div>
        `;
    }

    function submitApprove(lawyerId) {
        if (!confirm(lawyerId + ' 을(를) 승인하시겠습니까?')) return;
        const form = document.createElement('form');
        form.method = 'post';
        form.action = '${pageContext.request.contextPath}/admin/lawyer/approve';
        const input = document.createElement('input');
        input.type = 'hidden'; input.name = 'lawyerId'; input.value = lawyerId;
        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }

    function submitReject(lawyerId) {
        const reason = document.getElementById('rejectReasonInput')?.value?.trim();
        if (!reason) { alert('반려 사유를 입력해주세요.'); return; }
        if (!confirm(lawyerId + ' 을(를) 반려하시겠습니까?')) return;
        const form = document.createElement('form');
        form.method = 'post';
        form.action = '${pageContext.request.contextPath}/admin/lawyer/reject';
        const id = document.createElement('input');
        id.type = 'hidden'; id.name = 'lawyerId'; id.value = lawyerId;
        const r = document.createElement('input');
        r.type = 'hidden'; r.name = 'rejectReason'; r.value = reason;
        form.appendChild(id); form.appendChild(r);
        document.body.appendChild(form);
        form.submit();
    }
</script>
</body>
</html>
