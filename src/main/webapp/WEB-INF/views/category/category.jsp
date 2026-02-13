<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main">

    <section id="hero" class="hero section">
        <div class="container justify-content-center" data-aos="fade-up" data-aos-delay="100">
            <div class="row align-items-center">
                <div class="col-lg-12">
                    <div class="hero-content" data-aos="fade-up" data-aos-delay="200">
                        <h1 class="mb-12" style="text-align:center">법률 카테고리 목록<br></h1>
                        <h3 style="text-align:center"><span class="accent-text">아래의 카테고리 바로가기로 원하는 법률 가이드를 찾아보세요.</span></h3>
                    </div>
                </div>

                <div class="row stats-row gy-4 mt-5 justify-content-center" data-aos="fade-up" data-aos-delay="200">
                    <c:forEach items="${categoryList}" var="category" varStatus="status">
                    <c:if test="${status.index % 3 == 0 && status.index != 0}">
                </div>
                <div class="row stats-row gy-4 mt-5 justify-content-center" data-aos="fade-up" data-aos-delay="200">
                    </c:if>

                        <div class="col-lg-4 col-md-6">
                            <div class="stat-item">
                                <div class="stat-content" style="position: relative; padding-bottom: 60px;">
                                    <h4>${category.name}</h4>
                                    <p class="mb-3">${category.description}</p>
                                    <p class="mb-3">업데이트 날짜 : ${category.createdAt}</p>
                                    <p>조회수 : </p>

                                    <div class="service-card shadow-sm"
                                         style="position: absolute; border:1px solid skyblue; bottom: 10px; right: 10px; width: 100px; height: 40px; cursor: pointer; display: flex; align-items: center; justify-content: center;"
                                         onclick="location.href='/categorylist?categoryId=${category.categoryId}'">
                                        <h6 style="font-size: 0.8rem; margin: 0;">바로가기</h6>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </section>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>