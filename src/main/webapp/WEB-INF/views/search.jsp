<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <link rel="stylesheet" href="/css/custom.css">
    <style>
        .result-card { transition: box-shadow 0.2s; border-left: 4px solid transparent; }
        .result-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.10); }
        .source-badge-lawyer    { border-color: #0d83fd; }
        .source-badge-documents { border-color: #198754; }
        .source-badge-law_content { border-color: #fd7e14; }
        .source-badge-legal_terms { border-color: #6f42c1; }
        .highlight { background: #fff3cd; border-radius: 3px; padding: 0 2px; }
    </style>
</head>
<body class="index-page">
<main class="main" style="padding-top: 100px;">

    <%-- 검색창 --%>
    <section class="py-4 bg-light border-bottom">
        <div class="container">
            <form action="/search" method="get">
                <div class="row justify-content-center">
                    <div class="col-lg-7">
                        <div class="search-wrapper bg-white shadow-sm rounded-pill p-2 d-flex border">
                            <input type="text" name="q" value="${query}"
                                   class="form-control border-0 px-4"
                                   placeholder="사건 키워드, 법률 문서를 검색해보세요."
                                   style="background: transparent; box-shadow: none;" autofocus>
                            <button type="submit" class="btn btn-primary rounded-pill px-4">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>

    <%-- 필터 탭 --%>
    <section class="py-3 bg-white border-bottom">
        <div class="container">
            <div class="d-flex gap-2 flex-wrap align-items-center">
                <span class="text-muted me-2" style="font-size:14px;">필터:</span>
                <a href="/search?q=${query}" class="btn btn-sm ${empty sourceFilter ? 'btn-dark' : 'btn-outline-secondary'} rounded-pill">전체</a>
                <a href="/search?q=${query}&source=legal_terms" class="btn btn-sm ${sourceFilter == 'legal_terms' ? 'btn-dark' : 'btn-outline-secondary'} rounded-pill">
                    <span style="color:#6f42c1;">●</span> 법률 용어
                </a>
                <a href="/search?q=${query}&source=law_content" class="btn btn-sm ${sourceFilter == 'law_content' ? 'btn-dark' : 'btn-outline-secondary'} rounded-pill">
                    <span style="color:#fd7e14;">●</span> 법률 가이드
                </a>
                <a href="/search?q=${query}&source=documents" class="btn btn-sm ${sourceFilter == 'documents' ? 'btn-dark' : 'btn-outline-secondary'} rounded-pill">
                    <span style="color:#198754;">●</span> 서류 양식
                </a>
                <a href="/search?q=${query}&source=lawyer" class="btn btn-sm ${sourceFilter == 'lawyer' ? 'btn-dark' : 'btn-outline-secondary'} rounded-pill">
                    <span style="color:#0d83fd;">●</span> 변호사
                </a>
            </div>
        </div>
    </section>

    <%-- 결과 본문 --%>
    <section class="py-5">
        <div class="container">

            <c:choose>
                <%-- 검색어 없음 --%>
                <c:when test="${empty query}">
                    <div class="text-center py-5 text-muted">
                        <i class="bi bi-search fs-1 d-block mb-3"></i>
                        검색어를 입력해주세요.
                    </div>
                </c:when>

                <%-- 오류 --%>
                <c:when test="${not empty searchError}">
                    <div class="alert alert-warning">${searchError}</div>
                </c:when>

                <%-- 결과 없음 --%>
                <c:when test="${empty searchResults}">
                    <div class="text-center py-5 text-muted">
                        <i class="bi bi-emoji-frown fs-1 d-block mb-3"></i>
                        <strong>"${query}"</strong>에 대한 검색 결과가 없습니다.<br>
                        <span style="font-size:14px;">다른 키워드로 검색해보세요.</span>
                    </div>
                </c:when>

                <%-- 결과 있음 --%>
                <c:otherwise>
                    <p class="text-muted mb-4" style="font-size:14px;">
                        <strong>"${query}"</strong> 검색 결과 ${resultCount}건
                    </p>
                    <div class="row g-3">
                        <c:forEach items="${searchResults}" var="result" varStatus="st">
                            <div class="col-12">
                                <div class="result-card card border rounded-3 p-4 source-badge-${result.source}">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div style="flex:1; min-width:0;">

                                                <%-- 출처 배지 --%>
                                            <c:choose>
                                                <c:when test="${result.source == 'lawyer'}">
                                                    <span class="badge rounded-pill mb-2" style="background:#e8f3ff; color:#0d83fd;">변호사</span>
                                                </c:when>
                                                <c:when test="${result.source == 'documents'}">
                                                    <span class="badge rounded-pill mb-2" style="background:#e9f7ef; color:#198754;">서류 양식</span>
                                                </c:when>
                                                <c:when test="${result.source == 'law_content'}">
                                                    <span class="badge rounded-pill mb-2" style="background:#fff3e0; color:#fd7e14;">법률 가이드</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge rounded-pill mb-2" style="background:#f0ebff; color:#6f42c1;">법률 용어</span>
                                                </c:otherwise>
                                            </c:choose>

                                                <%-- 제목 --%>
                                            <h6 class="fw-bold mb-1" style="font-size:16px;">
                                                    ${result.title}
                                            </h6>

                                                <%-- 내용 미리보기 --%>
                                            <p class="text-muted mb-2" style="font-size:14px; line-height:1.6;">
                                                    ${result.preview}
                                            </p>

                                                <%-- 유사도 --%>
                                            <small class="text-muted">
                                                유사도
                                                <span class="fw-bold" style="color:#0d83fd;">
                                                    <fmt:formatNumber value="${(1 - result.distance) * 100}" maxFractionDigits="1"/>%
                                                </span>
                                            </small>
                                        </div>

                                            <%-- 상세보기 버튼 --%>
                                        <div class="ms-3 flex-shrink-0">
                                            <c:choose>
                                                <c:when test="${result.source == 'lawyer'}">
                                                    <%-- TB_LAWYER → LAWYER_ID --%>
                                                    <a href="/lawyer/detail/${result['ref_id']}" class="btn btn-outline-primary btn-sm rounded-pill">상세보기</a>
                                                </c:when>
                                                <c:when test="${result.source == 'documents'}">
                                                    <button
                                                            onclick="event.stopPropagation(); handleDownload(${result['ref_id']})"
                                                            class="btn btn-outline-success btn-sm rounded-pill">
                                                        다운로드
                                                    </button>
                                                </c:when>
                                                <c:when test="${result.source == 'law_content'}">
                                                    <%-- LAW_CONTENT → CONTENT_ID --%>
                                                    <a href="/content/${result['ref_id']}" class="btn btn-outline-warning btn-sm rounded-pill">상세보기</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <%-- LEGAL_TERMS → ID --%>
                                                    <a href="/legal-terms/${result['ref_id']}" class="btn btn-outline-secondary btn-sm rounded-pill">상세보기</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </section>
</main>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    var isLoggedIn = ${not empty sessionScope.loginUser ? 'true' : 'false'};

    function downloadFile(id) {
        window.location.href = '/docs/download/' + id;
    }

    function handleDownload(id) {
        if (!isLoggedIn) {
            if (confirm('로그인이 필요합니다.\n로그인 페이지로 이동하시겠습니까?')) {
                location.href = '/login';
            }
            return;
        }
        downloadFile(id);
    }
</script>
</body>
</html>