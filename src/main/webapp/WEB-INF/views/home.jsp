<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
</head>
<body class="index-page">

<main class="main">
    <section id="hero" class="hero section">
        <div class="container" data-aos="fade-up">
            <div class="row justify-content-center text-center">
                <div class="col-lg-8">
                    <h2 class="mb-4" style="font-weight: 700;">통합 검색창</h2>
                    <div class="search-bar shadow-sm p-3 mb-5 bg-white rounded-5 d-flex align-items-center" style="border: 1px solid #ddd;">
                        <input type="text" class="form-control border-0 ms-3" placeholder="검색어를 입력해주세요." style="box-shadow: none;">
                        <button class="btn btn-link text-dark me-2"><i class="bi bi-search fs-4"></i></button>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="stats" class="stats section">
        <div class="container" data-aos="fade-up" data-aos-delay="100">
            <div class="section-title text-center mb-5">
                <h2>주요 서비스 바로가기</h2>
            </div>
            <div class="row gy-4 justify-content-center">
                <div class="col-lg-2 col-md-4"><div class="stat-item text-center"><h4>법률 백과/가이드</h4><a href="#" class="btn btn-sm btn-outline-secondary mt-2">바로가기</a></div></div>
                <div class="col-lg-2 col-md-4"><div class="stat-item text-center"><h4>Q&A 게시판</h4><a href="#" class="btn btn-sm btn-outline-secondary mt-2">바로가기</a></div></div>
                <div class="col-lg-2 col-md-4"><div class="stat-item text-center"><h4>상담 요청</h4><a href="#" class="btn btn-sm btn-outline-secondary mt-2">바로가기</a></div></div>
                <div class="col-lg-2 col-md-4"><div class="stat-item text-center"><h4>서류양식 다운로드</h4><a href="#" class="btn btn-sm btn-outline-secondary mt-2">바로가기</a></div></div>
                <div class="col-lg-2 col-md-4"><div class="stat-item text-center"><h4>AI 상담(2차)</h4><a href="#" class="btn btn-sm btn-outline-secondary mt-2">바로가기</a></div></div>
            </div>
        </div>
    </section>

    <section id="features" class="features section light-background">
        <div class="container">
            <div class="row gy-4">
                <div class="col-lg-6" data-aos="fade-up">
                    <h3 class="mb-3">인기 콘텐츠</h3>
                    <div class="p-4 bg-white border rounded">인기 Q&A 질문 제목 <span class="text-muted float-end">조회수 12k</span></div>
                    <div class="p-4 bg-white border rounded mt-2">많이 찾는 법률 가이드 <span class="text-muted float-end">조회수 8k</span></div>
                </div>
                <div class="col-lg-6" data-aos="fade-up" data-aos-delay="200">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h3>최신 법률 문서</h3>
                        <button class="btn btn-dark btn-sm">문서 더보기</button>
                    </div>
                    <div class="row g-2">
                        <div class="col-4"><div class="p-3 border text-center bg-white">이혼 합의서</div></div>
                        <div class="col-4"><div class="p-3 border text-center bg-white">진정서</div></div>
                        <div class="col-4"><div class="p-3 border text-center bg-white">근로계약서</div></div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>