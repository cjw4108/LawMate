<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <style>
        .admin-wrapper { padding: 60px 0; }
        .admin-title { font-weight: 700; text-align: center; margin-bottom: 40px; }
        .filter-box { display: flex; gap: 10px; margin-bottom: 30px; }
        .user-list { border: 1px solid #ddd; border-radius: 6px; padding: 15px; height: 400px; overflow-y: auto; background: #fff; }
        .user-item { padding: 12px; border-bottom: 1px solid #eee; cursor: pointer; transition: 0.2s; display: flex; justify-content: space-between; }
        .user-item:hover { background-color: #f8f9fa; }
        .user-item.active { background-color: #e9ecef; border-left: 4px solid #212529; }
        .detail-box { border: 1px solid #ddd; border-radius: 6px; padding: 30px; background: #fff; min-height: 400px; }
        .btn-group-custom { display: flex; gap: 10px; margin-top: 20px; }
        .status-badge { font-size: 12px; padding: 2px 8px; border-radius: 12px; background: #eee; }
    </style>
</head>
<body>
<main class="main admin-wrapper">
    <section class="section">
        <div class="container">
            <h2 class="admin-title">관리자 변호사 승인</h2>
            <p class="text-center text-muted mb-4">(회원 검색 및 변호사 승인)</p>

            <form action="/admin/lawyer/approve" method="get" class="filter-box">
                <select name="role" class="form-select w-auto">
                    <option value="">회원유형</option>
                    <option value="USER">일반</option>
                    <option value="LAWYER">변호사</option>
                </select>
                <select name="status" class="form-select w-auto">
                    <option value="">승인상태</option>
                    <option value="PENDING">승인 대기</option>
                    <option value="APPROVED">승인 완료</option>
                </select>
                <input type="text" name="keyword" class="form-control" placeholder="검색어를 입력해주세요.">
                <button type="submit" class="btn btn-secondary">검색</button>
            </form>

            <div class="row">
                <div class="col-md-4">
                    <h5 class="mb-3">사용자 목록</h5>
                    <div class="user-list">
                        <c:forEach var="user" items="${pendingList}">
                            <div class="user-item" onclick="viewDetail('${user.userId}', '${user.userName}', '${user.licenseFile}', '${user.status}')">
                                <span>${user.userName} | ${user.role}</span>
                                <span class="status-badge">${user.status == 'PENDING' ? '대기' : '활성'}</span>
                            </div>
                        </c:forEach>
                        <c:if test="${empty pendingList}">
                            <p class="text-center text-muted mt-5">대기 중인 회원이 없습니다.</p>
                        </c:if>
                    </div>
                </div>

                <div class="col-md-8">
                    <h5 class="mb-3">사용자 상세 / 승인</h5>
                    <div class="detail-box">
                        <form id="approveForm" action="/admin/lawyer/process" method="post">
                            <div class="mb-3">
                                <label class="form-label">아이디</label>
                                <input type="text" id="detailId" name="userId" class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">이름</label>
                                <input type="text" id="detailName" class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">업로드 한 서류</label>
                                <div class="input-group">
                                    <input type="text" id="detailFile" class="form-control" readonly placeholder="[ PDF / IMAGE ]">
                                    <button type="button" class="btn btn-outline-dark" onclick="openFile()">보기</button>
                                </div>
                            </div>
                            <div class="mb-4">
                                <label class="form-label">승인 상태 / 반려 사유</label>
                                <input type="text" id="detailStatus" class="form-control mb-2" readonly>
                                <input type="text" name="rejectReason" class="form-control" placeholder="반려 시 사유를 입력해주세요.">
                            </div>

                            <div class="btn-group-custom">
                                <button type="button" onclick="processStatus('APPROVED')" class="btn btn-dark w-50">승인</button>
                                <button type="button" onclick="processStatus('REJECTED')" class="btn btn-outline-secondary w-50">반려</button>
                                <input type="hidden" name="targetStatus" id="targetStatus">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    // 목록 클릭 시 상세 정보 바인딩
    function viewDetail(id, name, file, status) {
        document.getElementById('detailId').value = id;
        document.getElementById('detailName').value = name;
        document.getElementById('detailFile').value = file || '첨부파일 없음';
        document.getElementById('detailStatus').value = status === 'PENDING' ? '승인 대기' : status;

        // 클릭 효과
        event.currentTarget.parentElement.querySelectorAll('.user-item').forEach(el => el.classList.remove('active'));
        event.currentTarget.classList.add('active');
    }

    // 승인/반려 처리 전송
    function processStatus(status) {
        const id = document.getElementById('detailId').value;
        if(!id) {
            alert('사용자를 선택해주세요.');
            return;
        }

        const msg = status === 'APPROVED' ? '승인하시겠습니까?' : '반려하시겠습니까?';
        if(confirm(msg)) {
            document.getElementById('targetStatus').value = status;
            document.getElementById('approveForm').submit();
        }
    }

    // 파일 보기 팝업 (가상)
    function openFile() {
        const fileName = document.getElementById('detailFile').value;
        if(fileName && fileName !== '첨부파일 없음') {
            window.open('/upload/license/' + fileName, '_blank');
        } else {
            alert('확인할 파일이 없습니다.');
        }
    }
</script>
</body>
</html>