<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px; min-height: 80vh;">
    <div class="container">

        <!-- ================= 상단 타이틀 ================= -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold">
                <i class="bi bi-shield-lock-fill text-danger me-2"></i>
                QnA 관리자 관리 페이지
            </h3>
        </div>

        <!-- ================= 필터 영역 ================= -->
        <form method="get" action="/admin/qna" class="row g-3 mb-3">

            <div class="col-md-3">
                <select name="filter" class="form-select">
                    <option value="all" ${param.filter == 'all' ? 'selected' : ''}>전체 게시글</option>
                    <option value="deleted" ${param.filter == 'deleted' ? 'selected' : ''}>삭제된 글만</option>
                </select>
            </div>

            <div class="col-md-3">
                <select name="sort" class="form-select">
                    <option value="latest" ${param.sort == 'latest' ? 'selected' : ''}>최신순</option>
                    <option value="report" ${param.sort == 'report' ? 'selected' : ''}>신고 많은 순</option>
                </select>
            </div>

            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">
                    필터 적용
                </button>
            </div>

        </form>

        <!-- ================= 게시글 테이블 ================= -->
        <div class="table-responsive bg-white shadow-sm rounded border">
            <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                <tr>
                    <th style="width:5%">ID</th>
                    <th style="width:20%">제목</th>
                    <th style="width:10%">작성자</th>
                    <th style="width:10%">상태</th>
                    <th style="width:10%">신고횟수</th>
                    <th style="width:25%">신고사유</th>
                    <th style="width:20%">관리</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="q" items="${qnaList}">
                    <tr>
                        <td>${q.id}</td>

                        <!-- 제목 -->
                        <td class="text-start">
                            <c:choose>
                                <c:when test="${q.deleted == 1}">
                                    <span class="text-decoration-line-through text-muted">
                                            ${q.title}
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    ${q.title}
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <!-- 작성자 -->
                        <td>${q.writer}</td>

                        <!-- 상태 -->
                        <td>
                            <c:choose>
                                <c:when test="${q.deleted == 1}">
                                    <span class="badge bg-danger">삭제됨</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-success">정상</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <!-- 신고 횟수 -->
                        <td>
                            <span class="fw-bold text-danger">${q.reportCount}</span>
                        </td>

                        <!-- 신고 사유 -->
                        <td>
                            <button class="btn btn-sm btn-outline-secondary"
                                    data-bs-toggle="modal"
                                    data-bs-target="#reportModal${q.id}">
                                보기
                            </button>

                            <!-- 모달 -->
                            <div class="modal fade" id="reportModal${q.id}" tabindex="-1">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">신고 사유</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <div class="modal-body text-start">
                                            <c:choose>
                                                <c:when test="${not empty q.reportReason}">
                                                    ${q.reportReason}
                                                </c:when>
                                                <c:otherwise>
                                                    신고 내역 없음
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>

                        <!-- 관리 버튼 -->
                        <td>
                            <c:choose>
                                <c:when test="${q.deleted == 0}">
                                    <button class="btn btn-sm btn-outline-danger"
                                            onclick="deletePost(${q.id})">
                                        삭제
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-sm btn-outline-success"
                                            onclick="restorePost(${q.id})">
                                        복구
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </td>

                    </tr>
                </c:forEach>

                <c:if test="${empty qnaList}">
                    <tr>
                        <td colspan="7" class="py-5 text-center text-muted">
                            게시글이 없습니다.
                        </td>
                    </tr>
                </c:if>

                </tbody>
            </table>
        </div>

    </div>
</main>

<script>
    function deletePost(id) {
        if(confirm("정말 삭제하시겠습니까?")) {
            fetch("/admin/qna/delete/" + id, { method: "POST" })
                .then(() => location.reload());
        }
    }

    function restorePost(id) {
        if(confirm("복구하시겠습니까?")) {
            fetch("/admin/qna/restore/" + id, { method: "POST" })
                .then(() => location.reload());
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />