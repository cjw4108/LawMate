<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 80px;">
    <section id="hero" class="hero section py-5">
        <div class="container text-center" data-aos="fade-up">
            <h2 class="mb-4" style="font-weight: 700;">통합 검색창</h2>
            <div class="row justify-content-center">
                <div class="col-lg-6">
                    <div class="search-bar shadow-sm p-2 bg-white rounded-pill d-flex align-items-center border" style="height: 60px;">
                        <input type="text" class="form-control border-0 ms-3" placeholder="검색어를 입력해주세요." style="box-shadow: none; background: transparent;">
                        <button class="btn btn-link text-dark me-2"><i class="bi bi-search fs-4"></i></button>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="stats" class="stats section py-5">
        <div class="container" data-aos="fade-up">
            <div class="section-title text-center mb-5">
                <h2 style="font-weight: 700;">주요 서비스 바로가기</h2>
            </div>
            <div class="row gy-4 justify-content-center">
                <%-- 서비스 카드 반복 --%>
                <div class="col-lg-2 col-md-4">
                    <div class="p-4 border text-center bg-light rounded shadow-sm">
                        <h5 class="fs-6 fw-bold">법률 백과/가이드</h5>
                        <a href="#" class="btn btn-secondary btn-sm mt-3 px-4">바로가기</a>
                    </div>
                </div>
                <div class="col-lg-2 col-md-4">
                    <div class="p-4 border text-center bg-light rounded shadow-sm">
                        <h5 class="fs-6 fw-bold">Q&A 게시판</h5>
                        <a href="#" class="btn btn-secondary btn-sm mt-3 px-4">바로가기</a>
                    </div>
                </div>
                <div class="col-lg-2 col-md-4">
                    <div class="p-4 border text-center bg-light rounded shadow-sm">
                        <h5 class="fs-6 fw-bold">상담 요청</h5>
                        <a href="#" class="btn btn-secondary btn-sm mt-3 px-4">바로가기</a>
                    </div>
                </div>
                <div class="col-lg-2 col-md-4">
                    <div class="p-4 border text-center bg-light rounded shadow-sm">
                        <h5 class="fs-6 fw-bold">서류양식 다운로드</h5>
                        <a href="#" class="btn btn-secondary btn-sm mt-3 px-4">바로가기</a>
                    </div>
                </div>
                <div class="col-lg-2 col-md-4">
                    <div class="p-4 border text-center bg-light rounded shadow-sm">
                        <h5 class="fs-6 fw-bold">AI 상담(2차)</h5>
                        <a href="#" class="btn btn-secondary btn-sm mt-3 px-4">바로가기</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <hr class="container my-5">

    <section id="features" class="features section pb-5">
        <div class="container">
            <div class="row gy-5">
                <div class="col-lg-6" data-aos="fade-up">
                    <h3 class="mb-4 text-center fw-bold">인기 콘텐츠</h3>
                    <div class="p-4 bg-white border rounded shadow-sm mb-3 d-flex justify-content-between align-items-center">
                        <span class="fw-bold">인기 Q&A 질문 제목</span>
                        <small class="text-muted">답변 수 | 조회 수</small>
                    </div>
                    <div class="p-4 bg-white border rounded shadow-sm d-flex justify-content-between align-items-center">
                        <span class="fw-bold">많이 찾는 법률 가이드</span>
                        <small class="text-muted">카테고리 | 조회 수</small>
                    </div>
                </div>

                <div class="col-lg-6" data-aos="fade-up" data-aos-delay="200">
                    <h3 class="mb-4 text-center fw-bold">최신 법률 문서</h3>
                    <div class="row g-3 mb-3">
                        <div class="col-4">
                            <div class="p-3 border text-center bg-light rounded shadow-sm">
                                <p class="mb-2 fw-bold">이혼 합의서</p>
                                <a href="#" class="btn btn-secondary btn-sm">바로가기</a>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="p-3 border text-center bg-light rounded shadow-sm">
                                <p class="mb-2 fw-bold">진정서</p>
                                <a href="#" class="btn btn-secondary btn-sm">바로가기</a>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="p-3 border text-center bg-light rounded shadow-sm">
                                <p class="mb-2 fw-bold">근로계약서</p>
                                <a href="#" class="btn btn-secondary btn-sm">바로가기</a>
                            </div>
                        </div>
                    </div>
                    <div class="text-center">
                        <button class="btn btn-dark px-5 py-2">문서 더보기</button>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>