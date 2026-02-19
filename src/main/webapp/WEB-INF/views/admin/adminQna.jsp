<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px; min-height: 80vh;">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold"><i class="bi bi-exclamation-triangle-fill text-danger me-2"></i>신고된 질문 관리</h3>
            <span class="badge bg-secondary">전체 신고 건수: ${reportedList.size()}건</span>
        </div>

        <div class="table-responsive bg-white shadow-sm rounded border">
            <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                <tr>
                    <th style="width: 10%;">ID</th>
                    <th style="width: 45%;">질문 제목</th>
                    <th style="width: 10%;">상태</th>
                    <th style="width: 15%;">신고 횟수</th>
                    <th style="width: 20%;">관리 액션</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="q" items="${reportedList}">
                    <tr>
                        <td class="text-muted">${q.id}</td>
                        <td class="text-start">
                            <div class="text-truncate" style="max-width: 400px;">
                                <a href="/qna/detail/${q.id}" class="text-decoration-none fw-semibold text-dark">
                                        ${q.title}
                                </a>
                            </div>
                        </td>
                        <td>
                            <span class="badge rounded-pill bg-danger">신고 접수</span>
                        </td>
                        <td>
                            <span class="fw-bold text-danger fs-5">${q.reportCount}</span> <small class="text-muted">회</small>
                        </td>
                        <td>
                            <div class="btn-group" role="group">
                                <button type="button" class="btn btn-sm btn-outline-secondary" onclick="hidePost(${q.id})">숨김</button>
                                <button type="button" class="btn btn-sm btn-outline-danger" onclick="deletePost(${q.id})">삭제</button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <%-- 데이터가 없을 경우 --%>
                <c:if test="${empty reportedList}">
                    <tr>
                        <td colspan="5" class="py-5 text-center">
                            <div class="text-muted">
                                <i class="bi bi-check2-circle fs-1 d-block mb-3"></i>
                                신고된 게시글이 없습니다.
                            </div>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <div class="mt-4 text-end">
            <a href="/qna/list" class="btn btn-secondary">질문 목록으로 돌아가기</a>
        </div>
    </div>
</main>

<script>
    // 삭제 버튼 클릭 시 동작 (예시)
    function deletePost(id) {
        if(confirm(id + "번 게시글을 정말 삭제하시겠습니까? 관련 데이터가 모두 삭제됩니다.")) {
            // 여기에 삭제 API 호출 로직(Ajax)을 추가하면 됩니다.
            alert("삭제 요청이 전달되었습니다. (현재 기능 구현 전)");
        }
    }

    // 숨김 버튼 클릭 시 동작 (예시)
    function hidePost(id) {
        if(confirm(id + "번 게시글을 사용자들에게 보이지 않게 숨기시겠습니까?")) {
            alert("숨김 처리가 완료되었습니다. (현재 기능 구현 전)");
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />