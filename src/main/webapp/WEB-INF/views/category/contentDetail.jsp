<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<style>
    .sidebar-sticky {
        position: sticky;
        top: 20px;
    }

    .category-badge-pink {
        background-color: #ffe6f0;
        color: #d63384;
    }

    .content-box {
        background-color: #f8f9fa;
        padding: 2rem;
        margin: 1.5rem 0;
    }

    /* 텍스트 정렬 강제 적용 */
    .content-text {
        text-align: left !important;
        line-height: 1.8;
        font-size: 1.05rem;
        white-space: pre-wrap;
        text-indent: 0;
    }
</style>

<main class="main">
    <section id="hero" class="hero section" data-aos="fade-up" data-aos-delay="100">
        <div class="container-fluid px-4">
            <div class="row g-4">
                <!-- 사이드바 (왼쪽) -->
                <div class="col-lg-3 col-xl-2">
                    <div class="sidebar-sticky" data-aos="fade-up" data-aos-delay="200">
                        <!-- 목차 -->
                        <div class="card mb-3">
                            <div class="card-header bg-primary text-white text-center py-3">
                                <strong>목차</strong>
                            </div>
                            <div class="card-body p-3">
                                <ul class="list-unstyled mb-0">
                                    <li class="mb-2"><a href="#section1" class="text-decoration-none">1. 개요</a></li>
                                    <c:if test="${not empty content.content}">
                                        <li class="mb-2"><a href="#section2" class="text-decoration-none">2. 상세 내용</a></li>
                                    </c:if>
                                    <c:if test="${not empty content.process}">
                                        <li class="mb-2"><a href="#section3" class="text-decoration-none">3. 진행 절차</a></li>
                                    </c:if>
                                    <c:if test="${not empty content.documents}">
                                        <li class="mb-2"><a href="#section4" class="text-decoration-none">4. 필요 서류</a></li>
                                    </c:if>
                                    <li class="mb-0"><a href="#section5" class="text-decoration-none">5. 주의사항</a></li>
                                </ul>
                            </div>
                        </div>

                        <!-- 핵심 포인트 -->
                        <div class="card border-0" style="background-color: #d4edda;">
                            <div class="card-header text-success border-0" style="background-color: #d4edda;">
                                <strong>핵심 포인트</strong>
                            </div>
                            <div class="card-body">
                                <ul class="mb-0" style="padding-left: 1.2rem;">
                                    <li class="mb-2">전문가 상담 권장</li>
                                    <li class="mb-2">법적 절차 확인</li>
                                    <li class="mb-2">서류 사전 준비</li>
                                    <li class="mb-0">기한 준수 필수</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 메인 컨텐츠 (오른쪽) -->
                <div class="col-lg-9 col-xl-10">
                    <!-- 브레드크럼 -->
                    <nav aria-label="breadcrumb" data-aos="fade-up" data-aos-delay="300">
                        <ol class="breadcrumb bg-transparent p-0 mb-4">
                            <li class="breadcrumb-item"><a href="/">홈</a></li>
                            <li class="breadcrumb-item"><a href="/category">법률정보</a></li>
                            <li class="breadcrumb-item">
                                <a href="/categorylist?categoryId=${content.categoryId}">${content.categoryName}</a>
                            </li>
                            <li class="breadcrumb-item active">${content.title}</li>
                        </ol>
                    </nav>

                    <!-- 상단 제목 영역 -->
                    <div class="mb-5 p-4 bg-white border rounded" data-aos="fade-up" data-aos-delay="400">
                        <span class="badge category-badge-pink mb-3">${content.deepCategory}</span>
                        <h2 class="mb-3">${content.title}</h2>
                        <p class="text-muted small mb-0">
                            <c:choose>
                                <c:when test="${not empty content.updatedAt}">
                                    업데이트: <fmt:formatDate value="${content.updatedAt}" pattern="yyyy.MM.dd"/>
                                </c:when>
                                <c:otherwise>
                                    작성일: <fmt:formatDate value="${content.createdAt}" pattern="yyyy.MM.dd"/>
                                </c:otherwise>
                            </c:choose>
                            | 조회수: ${content.viewCount} | 작성자: 관리자
                        </p>
                    </div>

                    <!-- 1. 개요 -->
                    <div class="mb-5" id="section1" data-aos="fade-up" data-aos-delay="500">
                        <h3 class="mb-4" style="background-color: #f0f0f0; padding: 1rem;">1. 개요</h3>
                        <div class="content-text">${content.summary}</div>
                    </div>

                    <!-- 2. 상세 내용 (content가 있을 때만) -->
                    <c:if test="${not empty content.content}">
                        <div class="mb-5" id="section2" data-aos="fade-up" data-aos-delay="600">
                            <h3 class="mb-4" style="background-color: #f0f0f0; padding: 1rem;">2. 상세 내용</h3>
                            <div class="content-text">${content.content}</div>
                        </div>
                    </c:if>

                    <!-- 3. 진행 절차 (process가 있을 때만) -->
                    <c:if test="${not empty content.process}">
                        <div class="mb-5" id="section3" data-aos="fade-up" data-aos-delay="700">
                            <h3 class="mb-4" style="background-color: #f0f0f0; padding: 1rem;">3. 진행 절차</h3>
                            <div class="content-box">
                                <div class="content-text">${content.process}</div>
                            </div>
                        </div>
                    </c:if>

                    <!-- 4. 필요 서류 (documents가 있을 때만) -->
                    <c:if test="${not empty content.documents}">
                        <div class="mb-5" id="section4" data-aos="fade-up" data-aos-delay="800">
                            <h3 class="mb-4" style="background-color: #f0f0f0; padding: 1rem;">4. 필요 서류</h3>
                            <div class="content-text">${content.documents}</div>
                        </div>
                    </c:if>

                    <!-- 5. 주의사항 -->
                    <div class="mb-5" id="section5" data-aos="fade-up" data-aos-delay="900">
                        <h3 class="mb-4" style="background-color: #f0f0f0; padding: 1rem;">
                            <c:choose>
                                <c:when test="${not empty content.content and not empty content.process and not empty content.documents}">5. 주의사항</c:when>
                                <c:when test="${not empty content.content or not empty content.process or not empty content.documents}">
                                    ${(not empty content.content ? 1 : 0) + (not empty content.process ? 1 : 0) + (not empty content.documents ? 1 : 0) + 2}. 주의사항
                                </c:when>
                                <c:otherwise>2. 주의사항</c:otherwise>
                            </c:choose>
                        </h3>
                        <div class="alert alert-warning" role="alert">
                            <h6 class="alert-heading mb-3">진행 시 주의해야 할 사항</h6>
                            <ul class="mb-0" style="line-height: 1.8;">
                                <li>법적 기한을 반드시 준수하세요</li>
                                <li>필요한 서류를 미리 준비하세요</li>
                                <li>복잡한 경우 전문가 상담을 받으세요</li>
                            </ul>
                        </div>
                    </div>

                    <!-- 목록으로 돌아가기 버튼 -->
                    <div class="text-center mb-5">
                        <a href="/categorylist?categoryId=${content.categoryId}" class="btn btn-primary btn-lg">
                            목록으로 돌아가기
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    // 부드러운 스크롤
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
</script>

</html>