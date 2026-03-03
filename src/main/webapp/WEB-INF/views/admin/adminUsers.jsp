<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cp" value="${pageContext.request.contextPath}" />
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px; min-height: 80vh;">
    <div class="container">
        <h3 class="fw-bold mb-4"><i class="bi bi-people-fill me-2"></i>회원 관리</h3>

        <div class="row mb-4 align-items-center">
            <div class="col-md-3">
                <select class="form-select border-2" onchange="location.href='${cp}/admin/users?filter=' + this.value">
                    <option value="all" ${param.filter == 'all' ? 'selected' : ''}>전체 유저</option>
                    <option value="active" ${param.filter == 'active' ? 'selected' : ''}>정상 유저</option>
                    <option value="banned" ${param.filter == 'banned' ? 'selected' : ''}>정지 유저</option>
                </select>
            </div>
            <div class="col-md-6 offset-md-3">
                <form action="${cp}/admin/users" method="get" class="input-group shadow-sm rounded-pill border overflow-hidden">
                    <input type="hidden" name="filter" value="${param.filter != null ? param.filter : 'all'}">
                    <input type="text" name="keyword" class="form-control border-0 px-4" value="${param.keyword}" placeholder="아이디로 유저 검색">
                    <button class="btn btn-white border-0 py-2 px-3"><i class="bi bi-search"></i></button>
                </form>
            </div>
        </div>

        <div class="table-responsive bg-white shadow-sm rounded border">
            <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                <tr>
                    <th>유저 ID</th>
                    <th>유형</th>
                    <th>상태</th>
                    <th>가입일</th>
                    <th>조치</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="u" items="${userList}">
                    <tr>
                        <td class="fw-bold">${u.userId}</td>
                        <td>
                            <span class="badge ${u.role == 'LAWYER' ? 'bg-primary' : 'bg-secondary'}">
                                    ${u.role == 'LAWYER' ? '변호사' : '일반'}
                            </span>
                        </td>
                        <td>
                            <span id="status-${u.userId}" class="badge ${u.status == '정상' ? 'bg-success' : 'bg-danger'}">
                                    ${u.status}
                            </span>
                        </td>
                        <td class="small text-muted">${u.createdAt}</td>
                        <td>
                            <div id="btn-group-${u.userId}">
                                <c:choose>
                                    <c:when test="${u.status == '정상'}">
                                        <button type="button" class="btn btn-sm btn-outline-danger"
                                                onclick="updateUserStatus('${u.userId}', '정지')">
                                            <i class="bi bi-person-x"></i> 정지
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-sm btn-outline-success"
                                                onclick="updateUserStatus('${u.userId}', '정상')">
                                            <i class="bi bi-person-check"></i> 해제
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty userList}">
                    <tr><td colspan="5" class="py-5 text-center text-muted">해당하는 회원이 없습니다.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>

<script>
    /**
     * 유저 상태 변경 (Ajax)
     * 페이지 새로고침 없이 상태 텍스트와 버튼을 즉시 변경합니다.
     */
    function updateUserStatus(userId, newStatus) {
        if(!confirm(userId + " 회원을 [" + newStatus + "] 상태로 변경하시겠습니까?")) return;

        fetch('${cp}/admin/users/status', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'userId=' + userId + '&status=' + encodeURIComponent(newStatus)
        })
            .then(res => {
                if(!res.ok) throw new Error();
                alert("처리가 완료되었습니다.");
                location.reload(); // 가장 확실한 반영을 위해 새로고침
            })
            .catch(err => alert("상태 변경 중 오류가 발생했습니다."));
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />