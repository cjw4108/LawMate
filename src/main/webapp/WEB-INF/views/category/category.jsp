<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    <div class="col-lg-4 col-md-6">
                        <div class="stat-item">
                            <div class="stat-content">
                                <h4>이혼/가족</h4>
                                <p class="mb-3">부부관계, 양육권, 재산분할</p>
                                <p class="mb-3">조회수 : </p><p class="mb-3">업데이트 날짜 : </p>
                                <a href="/categorylist" class="btn btn-secondary btn-sm mt-3 px-4 float-end">바로가기</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="stat-item">
                            <div class="stat-content">
                                <h4>민사</h4>
                                <p class="mb-3">계약, 손해배상, 소송절차</p>
                                <p class="mb-3">조회수 : </p><p class="mb-3">업데이트 날짜 : </p>
                                <a href="#" class="btn btn-secondary btn-sm mt-3 px-4 float-end">바로가기</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="stat-item">
                            <div class="stat-content">
                                <h4>형사</h4>
                                <p class="mb-3">고소, 고발, 형사절차</p>
                                <p class="mb-3">조회수 : </p><p class="mb-3">업데이트 날짜 : </p>
                                <a href="#" class="btn btn-secondary btn-sm mt-3 px-4 float-end">바로가기</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row stats-row gy-4 mt-5 justify-content-center" data-aos="fade-up" data-aos-delay="200">
                    <div class="col-lg-4 col-md-6">
                        <div class="stat-item">
                            <div class="stat-content">
                                <h4>노동</h4>
                                <p class="mb-3">근로계약, 임금, 부당해고</p>
                                <p class="mb-3">조회수 : </p><p class="mb-3">업데이트 날짜 : </p>
                                <a href="#" class="btn btn-secondary btn-sm mt-3 px-4 float-end">바로가기</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="stat-item">
                            <div class="stat-content">
                                <h4>부동산</h4>
                                <p class="mb-3">매매, 임대차, 등기</p>
                                <p class="mb-3">조회수 : </p><p class="mb-3">업데이트 날짜 : </p>
                                <a href="#" class="btn btn-secondary btn-sm mt-3 px-4 float-end">바로가기</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="stat-item">
                            <div class="stat-content">
                                <h4>기타</h4>
                                <p class="mb-3">지식재산, 행정, 세무</p>
                                <p class="mb-3">조회수 : </p><p class="mb-3">업데이트 날짜 : </p>
                                <a href="#" class="btn btn-secondary btn-sm mt-3 px-4 float-end">바로가기</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </section>


</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>