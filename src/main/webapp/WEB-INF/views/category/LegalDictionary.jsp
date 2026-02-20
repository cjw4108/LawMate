<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<style>
    .chosung-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(60px, 1fr));
        gap: 8px;
    }

    .cho-btn {
        background-color: #f8f9fa;
        border: 1px solid #d0d5dd;   /* 항상 동일 */
        color: #333;
        font-weight: 600;
        padding: 8px 0;
        transition: background-color 0.2s ease, color 0.2s ease;
    }

    .cho-btn:hover {
        background-color: #e9ecef;
    }

    /* 🔥 여기서 border 절대 건드리지 않음 */
    .active-cho {
        background-color: #0d6efd;
        color: #ffffff;
    }
</style>
<main class="main">

    <%--
        서버 데이터를 hidden div의 data 속성으로 전달.
        script 태그 안에 JSTL/EL을 쓰면 Jasper 컴파일 오류가 발생하므로
        HTML 속성으로 내보낸 뒤 JS에서 읽는 방식을 사용함.
    --%>
    <div id="termDataWrapper" style="display:none;">
        <c:forEach items="${termList}" var="t">
            <div class="term-item"
                 data-term="${t.term}"
                 data-law="${t.law}"
                 data-desc="${t.description}"></div>
        </c:forEach>
    </div>

    <section id="hero" class="hero section">
        <div class="container justify-content-center" data-aos="fade-up" data-aos-delay="100">
            <div class="row align-items-center">

                <div class="col-lg-12">
                    <div class="hero-content" data-aos="fade-up" data-aos-delay="200">
                        <h1 class="mb-3" style="text-align:center">법률 용어 백과사전</h1>
                        <h3 style="text-align:center">
                            <span class="accent-text">법률 용어와 개념을 쉽게 이해하세요</span>
                        </h3>
                    </div>
                </div>

                <%-- 검색창 --%>
                <div class="col-lg-12 mt-4" data-aos="fade-up" data-aos-delay="250">
                    <div class="d-flex justify-content-center">
                        <div class="input-group" style="max-width: 680px;">
                            <input type="text" id="searchInput" class="form-control form-control-lg"
                                   placeholder="법률 용어 검색... (예: 집행유예, 가압류, 변론)"
                                   value="${keyword}"
                                   onkeydown="if(event.key==='Enter') doSearch()">
                            <button class="btn btn-primary px-4" onclick="doSearch()">검색</button>
                        </div>
                    </div>
                </div>

                <%-- 가나다순 초성 버튼 --%>
                <div class="col-lg-12 mt-4" data-aos="fade-up" data-aos-delay="300">
                    <div class="card shadow-sm p-3">
                        <p class="text-muted mb-3" style="font-size:13px; font-weight:500;">가나다순 찾기</p>

                        <div class="chosung-grid">

                            <button class="btn cho-btn ${empty chosung ? 'active-cho' : ''}"
                                    onclick="filterCho(this, '')">전체</button>

                            <button class="btn cho-btn ${chosung eq 'ㄱ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㄱ')">ㄱ</button>

                            <button class="btn cho-btn ${chosung eq 'ㄴ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㄴ')">ㄴ</button>

                            <button class="btn cho-btn ${chosung eq 'ㄷ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㄷ')">ㄷ</button>

                            <button class="btn cho-btn ${chosung eq 'ㄹ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㄹ')">ㄹ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅁ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅁ')">ㅁ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅂ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅂ')">ㅂ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅅ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅅ')">ㅅ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅇ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅇ')">ㅇ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅈ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅈ')">ㅈ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅊ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅊ')">ㅊ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅋ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅋ')">ㅋ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅌ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅌ')">ㅌ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅍ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅍ')">ㅍ</button>

                            <button class="btn cho-btn ${chosung eq 'ㅎ' ? 'active-cho' : ''}"
                                    onclick="filterCho(this, 'ㅎ')">ㅎ</button>

                        </div>


                <%-- 인기 검색어 --%>
                <div class="col-lg-12 mt-3" data-aos="fade-up" data-aos-delay="320">
                    <div class="card shadow-sm p-3" style="background:#f0f4ff; border-color:#d0dcf8;">
                        <div class="d-flex align-items-center flex-wrap gap-2">
                            <span class="fw-bold text-primary me-2" style="font-size:13px;">인기 검색어</span>
                            <span class="badge rounded-pill border border-primary-subtle text-dark bg-white px-3 py-2" style="cursor:pointer; font-size:13px;" onclick="quickSearch('집행유예')">집행유예</span>
                            <span class="badge rounded-pill border border-primary-subtle text-dark bg-white px-3 py-2" style="cursor:pointer; font-size:13px;" onclick="quickSearch('가압류')">가압류</span>
                            <span class="badge rounded-pill border border-primary-subtle text-dark bg-white px-3 py-2" style="cursor:pointer; font-size:13px;" onclick="quickSearch('변론')">변론</span>
                            <span class="badge rounded-pill border border-primary-subtle text-dark bg-white px-3 py-2" style="cursor:pointer; font-size:13px;" onclick="quickSearch('상소')">상소</span>
                            <span class="badge rounded-pill border border-primary-subtle text-dark bg-white px-3 py-2" style="cursor:pointer; font-size:13px;" onclick="quickSearch('몰수')">몰수</span>
                            <span class="badge rounded-pill border border-primary-subtle text-dark bg-white px-3 py-2" style="cursor:pointer; font-size:13px;" onclick="quickSearch('공시송달')">공시송달</span>
                        </div>
                    </div>
                </div>

                <%-- 결과 수 --%>
                <div class="col-lg-12 mt-3">
                    <p class="text-muted" style="font-size:13px;">
                        <c:choose>
                            <c:when test="${mode == 'search'}">
                                <strong>"${keyword}"</strong> 검색 결과 &nbsp;·&nbsp; 총 <strong class="text-primary">${totalCount}</strong>개 용어
                            </c:when>
                            <c:when test="${mode == 'chosung'}">
                                <strong>${chosung}</strong> 초성 &nbsp;·&nbsp; 총 <strong class="text-primary">${totalCount}</strong>개 용어
                            </c:when>
                            <c:otherwise>
                                전체 <strong class="text-primary">${totalCount}</strong>개 용어
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <%-- 카드 그리드 --%>
                <div class="row gy-4 mt-1" data-aos="fade-up" data-aos-delay="350">

                    <c:choose>
                        <c:when test="${empty termList}">
                            <div class="col-12 text-center py-5 text-muted">
                                <div style="font-size:36px;">📋</div>
                                <p class="mt-3 fw-bold">검색 결과가 없습니다</p>
                                <p style="font-size:13px;">다른 검색어를 입력해 보세요.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${termList}" var="t" varStatus="st">
                                <div class="col-lg-6 col-md-12">
                                    <div class="stat-item h-100">
                                        <div class="stat-content" style="position:relative; padding-bottom:56px;">

                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <h4 style="margin:0;">${t.term}</h4>
                                                <c:if test="${not empty t.law}">
                                                    <span class="badge bg-light text-secondary border"
                                                          style="font-size:11px; max-width:220px; white-space:normal; text-align:right;">
                                                            ${t.law}
                                                    </span>
                                                </c:if>
                                            </div>

                                            <p class="mb-0" style="font-size:13.5px; line-height:1.75; color:#374151;">
                                                    ${t.description}
                                            </p>



                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                </div>
                        <c:if test="${totalPage > 1}">
                            <div class="col-12 mt-4">
                                <nav>
                                    <ul class="pagination justify-content-center">

                                            <%-- 이전 버튼 --%>
                                        <c:if test="${currentPage > 1}">
                                            <li class="page-item">
                                                <a class="page-link"
                                                   href="?page=${currentPage - 1}<c:if test='${mode eq "search"}'>&keyword=${keyword}</c:if><c:if test='${mode eq "chosung"}'>&chosung=${chosung}</c:if>">
                                                    이전
                                                </a>
                                            </li>
                                        </c:if>

                                            <%-- 페이지 번호 --%>
                                        <c:forEach begin="1" end="${totalPage}" var="i">
                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="?page=${i}<c:if test='${mode eq "search"}'>&keyword=${keyword}</c:if><c:if test='${mode eq "chosung"}'>&chosung=${chosung}</c:if>">
                                                        ${i}
                                                </a>
                                            </li>
                                        </c:forEach>

                                            <%-- 다음 버튼 --%>
                                        <c:if test="${currentPage < totalPage}">
                                            <li class="page-item">
                                                <a class="page-link"
                                                   href="?page=${currentPage + 1}<c:if test='${mode eq "search"}'>&keyword=${keyword}</c:if><c:if test='${mode eq "chosung"}'>&chosung=${chosung}</c:if>">
                                                    다음
                                                </a>
                                            </li>
                                        </c:if>

                                    </ul>
                                </nav>
                            </div>
                        </c:if>
            </div>
        </div>
    </section>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>

<script>
    /* 검색: 서버에 요청 */
    function doSearch() {
        var keyword = document.getElementById('searchInput').value.trim();
        if (keyword === '') {
            location.href = '/legal-dictionary';
        } else {
            location.href = '/legal-dictionary?keyword=' + encodeURIComponent(keyword);
        }
    }

    /* 초성 필터: 서버에 요청 */
    function filterCho(btn, cho) {
        if (!cho) {
            location.href = '/legal-dictionary';
        } else {
            location.href = '/legal-dictionary?chosung=' + encodeURIComponent(cho);
        }
    }

    /* 인기 검색어 클릭 */
    function quickSearch(keyword) {
        location.href = '/legal-dictionary?keyword=' + encodeURIComponent(keyword);
    }
</script>
