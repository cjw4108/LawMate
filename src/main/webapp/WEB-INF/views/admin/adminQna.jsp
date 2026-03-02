<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cp" value="${pageContext.request.contextPath}" />
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px; min-height: 80vh;">
    <div class="container">
        <h3 class="fw-bold mb-4">
            <i class="bi bi-shield-lock-fill text-danger me-2"></i> QnA 관리자 관리
        </h3>

        <form method="get" action="${cp}/admin/qna" class="row g-3 mb-4">
            <div class="col-md-3">
                <select name="filter" class="form-select border-2">
                    <option value="all" ${currentFilter == 'all' ? 'selected' : ''}>전체 게시글</option>
                    <option value="deleted" ${currentFilter == 'deleted' ? 'selected' : ''}>삭제된 글만</option>
                </select>
            </div>
            <div class="col-md-3">
                <select name="sort" class="form-select border-2">
                    <option value="latest" ${currentSort == 'latest' ? 'selected' : ''}>최신순</option>
                    <option value="report" ${currentSort == 'report' ? 'selected' : ''}>신고 많은 순</option>
                </select>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-dark w-100">적용</button>
            </div>
        </form>

        <div class="table-responsive bg-white shadow-sm rounded border">
            <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                <tr>
                    <th style="width:5%">ID</th>
                    <th style="width:30%">제목</th>
                    <th style="width:10%">상태</th>
                    <th style="width:10%">신고</th>
                    <th style="width:20%">관리</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="q" items="${qnaList}">
                    <tr class="${q.deleted != 0 ? 'table-secondary' : ''}">
                        <td>${q.id}</td>
                        <td class="text-start">
                            <span class="${q.deleted != 0 ? 'text-decoration-line-through text-muted' : ''}">
                                    ${q.title}
                            </span>
                        </td>
                        <td>
                            <span class="badge ${q.deleted == 0 ? 'bg-success' : 'bg-danger'}">
                                    ${q.deleted == 0 ? '정상' : '삭제됨'}
                            </span>
                        </td>
                        <td>
                            <button type="button" class="btn btn-sm btn-outline-secondary" onclick="loadReportReasons(${q.id})">
                                    ${q.reportCount}건
                            </button>
                        </td>
                        <td>
                            <div class="btn-group">
                                <c:choose>
                                    <c:when test="${q.deleted == 0}">
                                        <button type="button" class="btn btn-sm btn-danger" onclick="deletePost(${q.id})">삭제</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-sm btn-success" onclick="restorePost(${q.id})">복구</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</main>

<div class="modal fade" id="reportModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow border-0">
            <div class="modal-header bg-light">
                <h5 class="modal-title fw-bold">신고 상세 내역</h5>
            </div>
            <div class="modal-body p-0" id="reportModalBody">
            </div>
            <div class="modal-footer border-0">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<script>
    function loadReportReasons(qnaId) {
        const modalBody = document.getElementById('reportModalBody');
        const modalEl = document.getElementById('reportModal');

        modalBody.innerHTML = '<div class="p-5 text-center"><div class="spinner-border text-primary"></div></div>';
        try {
            const modalInst = new bootstrap.Modal(modalEl);
            modalInst.show();
        } catch (e) {
            // 만약 위에서도 에러가 나면 구버전(v4) 방식 시도
            $(modalEl).modal('show');
        }

        fetch('${cp}/admin/qna/reports/' + qnaId)
            .then(res => res.json())
            .then(data => {
                if(!data || data.length === 0) {
                    modalBody.innerHTML = '<div class="p-4 text-center text-muted">신고 내역이 없습니다.</div>';
                    return;
                }
                let html = '<ul class="list-group list-group-flush">';
                data.forEach(report => {
                    // report[0]: 사유, report[1]: 아이디, report[2]: 날짜
                    html += '<li class="list-group-item p-3">' +
                        '  <div class="fw-bold mb-1">' + (report[0] || '내용 없음') + '</div>' +
                        '  <div class="small text-muted">신고자: ' + (report[1] || '익명') + ' | ' + new Date(report[2]).toLocaleString() + '</div>' +
                        '</li>';
                });
                html += '</ul>';
                modalBody.innerHTML = html;
            })
            .catch(err => {
                modalBody.innerHTML = '<div class="p-4 text-center text-danger">데이터를 불러올 수 없습니다.</div>';
            });
    }

    function deletePost(id) {
        if(confirm("이 글을 삭제하시겠습니까?")) sendPost('${cp}/admin/qna/delete/' + id);
    }

    function restorePost(id) {
        if(confirm("이 글을 복구하시겠습니까?")) sendPost('${cp}/admin/qna/restore/' + id);
    }

    function sendPost(url) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = url + "?filter=${currentFilter}&sort=${currentSort}";
        document.body.appendChild(form);
        form.submit();
    }
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />