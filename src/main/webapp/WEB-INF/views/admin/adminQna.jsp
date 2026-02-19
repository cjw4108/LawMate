<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container">
        <h3 class="mb-4 fw-bold">신고된 질문 관리</h3>

        <div class="table-responsive bg-white shadow-sm rounded border">
            <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                <tr>
                    <th style="width: 10%;">ID</th>
                    <th style="width: 40%;">제목</th>
                    <th style="width: 15%;">상태</th>
                    <th style="width: 15%;">신고 횟수</th>
                    <th>관리</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="report" items="${reportedList}">
                    <tr>
                        <td>${q.id}</td>
                        <td class="text-start text-truncate" style="max-width: 300px;">
                            <a href="/qna/detail/${q.id}" class="text-dark">${q.title}</a>
                        </td>
                        <td><span class="badge bg-danger">신고됨</span></td>
                        <td class="fw-bold text-danger">${q.reportCount}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-secondary">숨김</button>
                            <button class="btn btn-sm btn-outline-danger">삭제</button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty reportedList}">
                    <tr>
                        <td colspan="5" class="py-5 text-muted">신고된 게시글이 없습니다.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />