<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container">

        <!-- 검색 영역 -->
        <div class="row mb-5 justify-content-center">
            <div class="col-lg-8">
                <form action="/qna/list" method="get" id="searchForm">

                    <div class="search-wrapper bg-white shadow-sm rounded-pill p-2 d-flex border mb-3">
                        <input type="text"
                               name="keyword"
                               class="form-control border-0 ms-3"
                               placeholder="궁금한 법률 내용을 검색해보세요."
                               value="${param.keyword}">
                        <button type="submit" class="btn btn-primary rounded-pill px-4">
                            <i class="bi bi-search me-1"></i> 검색
                        </button>
                    </div>

                    <!-- 정렬 버튼 -->
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div class="btn-group shadow-sm bg-white p-1 rounded">

                            <a href="/qna/list?sort=latest&keyword=${param.keyword}"
                               class="btn btn-sm ${currentSort == 'latest' ? 'btn-dark' : 'btn-outline-dark border-0'}">
                                최신순
                            </a>

                            <a href="/qna/list?sort=replies&keyword=${param.keyword}"
                               class="btn btn-sm ${currentSort == 'replies' ? 'btn-dark' : 'btn-outline-dark border-0'}">
                                답변 많은순
                            </a>

                            <a href="/qna/list?sort=likes&keyword=${param.keyword}"
                               class="btn btn-sm ${currentSort == 'likes' ? 'btn-dark' : 'btn-outline-dark border-0'}">
                                공감 많은순
                            </a>

                            <a href="/qna/list?sort=favorite&keyword=${param.keyword}"
                               class="btn btn-sm ${currentSort == 'favorite' ? 'btn-dark' : 'btn-outline-dark border-0'}">
                                내가 찜한 글
                            </a>

                        </div>
                    </div>

                </form>
            </div>
        </div>

        <!-- QNA 리스트 -->
        <div class="qna-list mb-5">

            <c:choose>

                <c:when test="${not empty list}">

                    <c:forEach var="question" items="${list}">

                        <div class="card border-0 shadow-sm mb-3 hover-up"
                             onclick="location.href='/qna/detail/${question.id}'"
                             style="cursor:pointer;">

                            <div class="card-body p-4">
                                <div class="d-flex justify-content-between align-items-start">

                                    <!-- 왼쪽 영역 -->
                                    <div>

                                        <!-- 답변 상태 뱃지 -->
                                        <span class="badge ${
                                                question.replyCount > 0
                                                ? 'bg-success-subtle text-success'
                                                : 'bg-primary-subtle text-primary'
                                            } mb-2">

                                                ${question.replyCount > 0 ? '답변 완료' : '답변 대기'}

                                        </span>

                                        <h5 class="fw-bold">${question.title}</h5>

                                        <p class="text-muted small mb-0">
                                            작성자: ${question.userId} |

                                            <fmt:parseDate value="${question.createdAt}"
                                                           pattern="yyyy-MM-dd'T'HH:mm"
                                                           var="parsedDate"
                                                           type="both"/>

                                            <fmt:formatDate value="${parsedDate}"
                                                            pattern="yyyy.MM.dd"/>
                                        </p>
                                    </div>

                                    <!-- 오른쪽 영역 (답변수 + 찜수) -->
                                    <div class="d-flex gap-2">

                                        <!-- 답변 수 -->
                                        <div class="text-center bg-light p-2 rounded"
                                             style="min-width: 70px;">
                                            <span class="d-block fw-bold small">답변</span>
                                            <span class="text-primary fw-bold">
                                                    ${question.replyCount != null ? question.replyCount : 0}
                                            </span>
                                        </div>

                                        <!-- 찜 수 -->
                                        <div class="text-center bg-light p-2 rounded"
                                             style="min-width: 70px;">
                                            <span class="d-block fw-bold small">
                                                <i class="bi bi-heart-fill text-danger"></i>
                                            </span>
                                            <span class="text-danger fw-bold">
                                                    ${question.favoriteCount}
                                            </span>
                                        </div>

                                    </div>

                                </div>
                            </div>
                        </div>

                    </c:forEach>

                </c:when>

                <c:otherwise>

                    <div class="text-center py-5 bg-white rounded shadow-sm">
                        <i class="bi bi-search text-muted" style="font-size: 3rem;"></i>
                        <p class="mt-3 text-muted">검색 결과가 없습니다.</p>
                        <a href="/qna/list" class="btn btn-sm btn-outline-primary">
                            전체 목록보기
                        </a>
                    </div>

                </c:otherwise>

            </c:choose>

        </div>

        <!-- 글쓰기 버튼 -->
        <a href="/qna/write" class="btn btn-dark w-100 py-3 rounded-3 shadow">
            <i class="bi bi-pencil-square me-2"></i> 새로운 질문 등록하기
        </a>

    </div>
</main>

<script>
    function handleFilterChange() {
        document.getElementById('searchForm').submit();
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />