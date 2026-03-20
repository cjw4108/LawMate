<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <link rel="stylesheet" href="/css/custom.css">
</head>
<body class="index-page">

<main class="main" style="padding-top: 100px;">
    <section class="py-5 bg-light">
        <div class="container text-center" data-aos="fade-up">
            <h2 class="display-5 fw-bold mb-4" style="color: #2d465e;">어떤 법률 도움이 필요하신가요?</h2>
            <div class="row justify-content-center">
                <div class="col-lg-7">
                    <form action="/search" method="get">
                        <div class="search-wrapper bg-white shadow-sm rounded-pill p-2 d-flex border">
                            <input type="text" name="q"
                                   class="form-control border-0 px-4"
                                   placeholder="사건 키워드, 법률 문서를 검색해보세요."
                                   style="background: transparent; box-shadow: none;">
                            <button type="submit" class="btn btn-primary rounded-pill px-4">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <%-- 검색 결과 표시 --%>
            <c:if test="${not empty searchResults}">
                <div class="container mt-4" data-aos="fade-up">
                    <h5 class="fw-bold mb-3">"${query}" 검색 결과</h5>
                    <div class="list-group">
                        <c:forEach items="${searchResults}" var="result">
                            <a href="/content/${result.ref_id}"
                               class="list-group-item list-group-item-action py-3">
                                <div class="d-flex justify-content-between">
                                    <span class="fw-bold">${result.document}</span>
                                    <span class="badge bg-secondary rounded-pill ms-2">${result.source}</span>
                                </div>
                                <small class="text-muted">유사도: ${result.distance}</small>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty searchError}">
                <div class="container mt-3">
                    <div class="alert alert-warning">${searchError}</div>
                </div>
            </c:if>
        </div>
    </section>

    <section class="py-5">
        <div class="container" data-aos="fade-up">
            <div class="service-container">
                <div class="service-card shadow-sm" onclick="location.href='/category'">
                    <i class="bi bi-book text-primary"></i>
                    <h6>법률 백과/가이드</h6>
                </div>
                <div class="service-card shadow-sm" onclick="location.href='/qna/list'">
                    <i class="bi bi-chat-left-text text-info"></i>
                    <h6>Q&A 게시판</h6>
                </div>
                <div class="service-card shadow-sm" onclick="location.href='/lawyer/list'">
                    <i class="bi bi-person-video3 text-warning"></i>
                    <h6>변호사 열람페이지</h6>
                </div>
                <div class="service-card shadow-sm" onclick="location.href='/docs'">
                    <i class="bi bi-file-earmark-check text-success"></i>
                    <h6>서류양식 다운로드</h6>
                </div>
                <div class="service-card shadow-sm" onclick="location.href='/ai/consult'">
                    <i class="bi bi-robot text-danger"></i>
                    <h6>AI 상담(2차)</h6>
                </div>
            </div>
        </div>
    </section>

    <section class="py-5 bg-white border-top">
        <div class="container" data-aos="fade-up">
            <div class="row">
                <div class="col-lg-12">
                    <h4 class="fw-bold mb-4">인기 콘텐츠 <i class="bi bi-fire text-danger"></i></h4>
                    <div class="list-group list-group-flush border-top">
                        <c:choose>
                            <c:when test="${not empty popularList}">
                                <c:forEach items="${popularList}" var="item" varStatus="st">
                                    <a href="/content/${item.contentId}"
                                       class="list-group-item list-group-item-action py-3 d-flex justify-content-between align-items-center">
                                        <span>
                                            <span class="text-muted me-3" style="font-size:13px; font-weight: bold;">${st.index + 1}.</span>
                                            ${item.title}
                                        </span>
                                        <span class="badge bg-light text-dark rounded-pill border flex-shrink-0 ms-2">
                                            조회 ${item.viewCount}
                                        </span>
                                    </a>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="list-group-item py-4 text-muted text-center">
                                    등록된 인기 콘텐츠가 없습니다.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>