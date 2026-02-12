<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    <div class="search-wrapper bg-white shadow-sm rounded-pill p-2 d-flex border">
                        <input type="text" class="form-control border-0 px-4" placeholder="사건 키워드, 변호사, 법률 문서를 검색해보세요." style="background: transparent; box-shadow: none;">
                        <button class="btn btn-primary rounded-pill px-4"><i class="bi bi-search"></i></button>
                    </div>
                </div>
            </div>
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
                <div class="service-card shadow-sm" onclick="location.href='/consult/request'">
                    <i class="bi bi-person-video3 text-warning"></i>
                    <h6>상담 요청</h6>
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
            <div class="row g-5">
                <div class="col-lg-6">
                    <h4 class="fw-bold mb-4">인기 콘텐츠 <i class="bi bi-fire text-danger"></i></h4>
                    <div class="list-group list-group-flush border-top">
                        <a href="#" class="list-group-item list-group-item-action py-3 d-flex justify-content-between align-items-center">
                            <span>이혼 시 재산분할 비율은 어떻게 결정되나요?</span>
                            <span class="badge bg-light text-dark rounded-pill border">조회 1.2k</span>
                        </a>
                        <a href="#" class="list-group-item list-group-item-action py-3 d-flex justify-content-between align-items-center">
                            <span>상가 임대차 계약 갱신 요구권 가이드</span>
                            <span class="badge bg-light text-dark rounded-pill border">조회 980</span>
                        </a>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h4 class="fw-bold">최신 법률 문서</h4>
                        <button class="btn btn-outline-dark btn-sm rounded-pill">전체보기</button>
                    </div>
                    <div class="row g-3">
                        <div class="col-6">
                            <div class="p-4 border rounded shadow-sm text-center" style="cursor:pointer; transition: 0.3s;" onmouseover="this.style.borderColor='#0d83fd'" onmouseout="this.style.borderColor='#dee2e6'">
                                <i class="bi bi-file-earmark-text fs-3 mb-2 d-block"></i>
                                <span>임대차 계약서</span>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="p-4 border rounded shadow-sm text-center" style="cursor:pointer; transition: 0.3s;" onmouseover="this.style.borderColor='#0d83fd'" onmouseout="this.style.borderColor='#dee2e6'">
                                <i class="bi bi-file-earmark-medical fs-3 mb-2 d-block"></i>
                                <span>고소장 표준양식</span>
                            </div>
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