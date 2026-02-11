<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<style>
    body {
        background-color: #f8f9fa;
    }
</style>

<main class="main">
    <section id="hero" class="hero section" data-aos="fade-up" data-aos-delay="100">
        <div class="container">
            <!-- 상단 제목 -->
            <div class="mb-5" data-aos="fade-up" data-aos-delay="200">
                <h2 class="mb-4" style="color: #4169E1;">법률정보 플랫폼</h2>
            </div>

            <!-- 검색창 -->
            <div class="mb-5" data-aos="fade-up" data-aos-delay="300">
                <div class="input-group" style="max-width: 700px; margin: 0 auto;">
                    <input type="text" class="form-control form-control-lg" placeholder="양육권" style="padding: 15px 20px;">
                    <button class="btn btn-primary px-4" type="button" style="background-color: #4169E1; border-color: #4169E1;">검색</button>
                </div>
            </div>

            <!-- 검색 결과 헤더 -->
            <div class="mb-4" data-aos="fade-up" data-aos-delay="400">
                <h3>'양육권' 검색 결과</h3>
                <p class="text-muted">총 23건의 문서를 찾았습니다</p>
            </div>

            <!-- 카테고리 탭 -->
            <div class="mb-4" data-aos="fade-up" data-aos-delay="500">
                <div class="d-flex gap-2 flex-wrap">
                    <button class="btn btn-primary rounded-pill px-4">전체 (23)</button>
                    <button class="btn btn-outline-secondary rounded-pill px-4">법률정보 (15)</button>
                    <button class="btn btn-outline-secondary rounded-pill px-4">판례 (8)</button>
                    <button class="btn btn-outline-secondary rounded-pill px-4">용어 (5)</button>
                    <button class="btn btn-outline-secondary rounded-pill px-4 ms-auto">관련도순</button>
                </div>
            </div>

            <!-- 검색 결과 리스트 -->
            <div class="list-group mb-4" data-aos="fade-up" data-aos-delay="600">
                <!-- 항목 1 -->
                <a href="#" class="list-group-item list-group-item-action p-4 mb-3 border rounded">
                    <div class="mb-2">
                        <span class="badge bg-primary me-2">법률정보</span>
                        <span class="text-muted">/ 이혼/가족</span>
                    </div>
                    <h5 class="mb-2">양육권과 친권의 차이점 완벽 이해</h5>
                    <p class="mb-3 text-muted">양육권은 자녀를 직접 키우고 보호할 권리이며, 친권은 법적 권리와 의무를 포함합니다.</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <a href="#" class="text-primary text-decoration-none">#양육권</a>
                        <span class="text-muted small">조회 15,234 | 2026.01.20</span>
                    </div>
                </a>

                <!-- 항목 2 -->
                <a href="#" class="list-group-item list-group-item-action p-4 mb-3 border rounded">
                    <div class="mb-2">
                        <span class="badge bg-primary me-2">법률정보</span>
                        <span class="text-muted">/ 이혼/가족</span>
                    </div>
                    <h5 class="mb-2">양육권 결정 기준과 변경 절차</h5>
                    <p class="mb-3 text-muted">법원이 양육권을 결정할 때는 자녀의 복리를 최우선으로 고려합니다.</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <a href="#" class="text-primary text-decoration-none">#양육권</a>
                        <span class="text-muted small">조회 11,892 | 2026.01.18</span>
                    </div>
                </a>

                <!-- 항목 3 -->
                <a href="#" class="list-group-item list-group-item-action p-4 mb-3 border rounded">
                    <div class="mb-2">
                        <span class="badge me-2" style="background-color: #9370DB; color: white;">판례</span>
                        <span class="text-muted">/ 가사</span>
                    </div>
                    <h5 class="mb-2">대법원 2023다98765 - 양육권 변경 청구</h5>
                    <p class="mb-3 text-muted">양육 환경 변경 시 자녀 복리를 위해 양육권 변경이 인정될 수 있습니다.</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <a href="#" class="text-primary text-decoration-none">#양육권</a>
                        <span class="text-muted small">선고일 2023.05.15</span>
                    </div>
                </a>

                <!-- 항목 4 -->
                <a href="#" class="list-group-item list-group-item-action p-4 mb-3 border rounded">
                    <div class="mb-2">
                        <span class="badge me-2" style="background-color: #20B2AA; color: white;">용어</span>
                        <span class="text-muted">/ 법률용어</span>
                    </div>
                    <h5 class="mb-2">양육권 (Custody)</h5>
                    <p class="mb-3 text-muted">미성년 자녀를 직접 키우고 보호할 권리입니다.</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <a href="#" class="text-primary text-decoration-none">#양육권</a>
                        <span class="text-muted small">백과사전</span>
                    </div>
                </a>
            </div>

            <!-- 더보기 버튼 -->
            <div class="text-center mb-5" data-aos="fade-up" data-aos-delay="700">
                <button class="btn btn-outline-secondary rounded-pill px-5">더보기</button>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</html>