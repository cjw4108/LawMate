<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main">
    <section id="hero" class="hero section" data-aos="fade-up" data-aos-delay="100">
        <div class="container">
            <!-- 상단 제목 영역 -->
            <div class="mb-5" data-aos="fade-up" data-aos-delay="200">
                <h2 class="mb-2">${category.name} 법률정보</h2>
                <p class="text-muted">${category.description}</p>
            </div>

            <!-- 법률정보 개수 -->
            <p class="mb-4" data-aos="fade-up" data-aos-delay="400">
                총 <strong>${totalContents}건</strong>의 법률정보
            </p>

            <!-- 법률정보 리스트 -->
            <div class="list-group mb-4" data-aos="fade-up" data-aos-delay="500">
                <c:forEach items="${contentList}" var="content">
                    <a href="/content/${content.contentId}" class="list-group-item list-group-item-action p-4">
                        <div class="d-flex w-100 justify-content-between align-items-start mb-2">
                            <span class="badge bg-primary">${content.deepCategory}</span>
                        </div>
                        <h5 class="mb-2">${content.title}</h5>
                        <p class="mb-3 text-muted">${content.summary}</p>
                        <div class="d-flex justify-content-between text-muted small">
                            <span>조회수 ${content.viewCount}</span>
                            <span>작성일 <fmt:formatDate value="${content.createdAt}" pattern="yyyy.MM.dd"/></span>
                        </div>
                    </a>
                </c:forEach>
            </div>

            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <!-- 이전 버튼 -->
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?categoryId=${category.categoryId}&page=${currentPage - 1}">이전</a>
                        </li>
                    </c:if>

                    <!-- 페이지 번호 -->
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?categoryId=${category.categoryId}&page=${i}">${i}</a>
                        </li>
                    </c:forEach>

                    <!-- 다음 버튼 -->
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="?categoryId=${category.categoryId}&page=${currentPage + 1}">다음</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</html>