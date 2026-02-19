<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container">
        <div class="qna-list mb-5">
            <c:forEach var="question" items="${list}">
                <div class="card border-0 shadow-sm mb-3 hover-up"
                     onclick="location.href='/qna/detail/${question.id}'" style="cursor:pointer;">
                    <div class="card-body p-4">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <span class="badge bg-primary-subtle text-primary mb-2">
                                        ${question.answered > 0 ? '답변 완료' : '답변 대기'}
                                </span>
                                <h5 class="fw-bold">${question.title}</h5>
                                <p class="text-muted small mb-0">작성자: ${question.userId} | ${question.createdAt}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <a href="/qna/write" class="btn btn-dark w-100 py-3 rounded-3 shadow">
            <i class="bi bi-pencil-square me-2"></i> 새로운 질문 등록하기
        </a>
    </div>
</main>