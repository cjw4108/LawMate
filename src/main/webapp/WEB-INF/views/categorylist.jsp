<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<style>
    body {
        padding-top: 50px;
    }

</style>
<main class="main">
    <section class="section">
        <div class="container">
            <!-- 상단 제목 영역 -->
            <div class="mb-5">

                <h2 class="mb-2">이혼/가족 법률정보</h2>
                <p class="text-muted">이혼, 양육권, 재산분할 등 가족 관계 법률 정보</p>
            </div>

            <!-- 탭 메뉴 -->
            <ul class="nav nav-tabs mb-4">
                <li class="nav-item">
                    <a class="nav-link active" href="#">전체</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">이혼절차</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">재산분할</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">양육권</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">위자료</a>
                </li>
                <li class="nav-item ms-auto">
                    <a class="nav-link" href="#">최신순</a>
                </li>
            </ul>

            <!-- 법률정보 개수 -->
            <p class="mb-4">총 <strong>482건</strong>의 법률정보</p>

            <!-- 법률정보 리스트 -->
            <div class="list-group mb-4">
                <!-- 항목 1 -->
                <a href="#" class="list-group-item list-group-item-action p-4">
                    <div class="d-flex w-100 justify-content-between align-items-start mb-2">
                        <span class="badge bg-primary">이혼절차</span>
                    </div>
                    <h5 class="mb-2">협의이혼 절차 및 필요서류 완벽 가이드</h5>
                    <p class="mb-3 text-muted">협의이혼의 전체 절차, 필요 서류, 소요 기간, 비용 등을 상세히 안내합니다.</p>
                    <div class="d-flex justify-content-between text-muted small">
                        <span>조회수 12,345</span>
                        <span>업데이트 2026.01.28</span>
                    </div>
                </a>

                <!-- 항목 2 -->
                <a href="#" class="list-group-item list-group-item-action p-4">
                    <div class="d-flex w-100 justify-content-between align-items-start mb-2">
                        <span class="badge bg-success">재산분할</span>
                    </div>
                    <h5 class="mb-2">재산분할 청구권과 계산 방법</h5>
                    <p class="mb-3 text-muted">재산분할의 법적 근거, 분할 대상 재산, 기여도 산정 방법을 설명합니다.</p>
                    <div class="d-flex justify-content-between text-muted small">
                        <span>조회수 9,876</span>
                        <span>업데이트 2026.01.25</span>
                    </div>
                </a>

                <!-- 항목 3 -->
                <a href="#" class="list-group-item list-group-item-action p-4">
                    <div class="d-flex w-100 justify-content-between align-items-start mb-2">
                        <span class="badge bg-info">양육권</span>
                    </div>
                    <h5 class="mb-2">양육권과 친권의 차이점 이해하기</h5>
                    <p class="mb-3 text-muted">양육권과 친권의 법적 개념, 결정 기준, 변경 절차를 정리했습니다.</p>
                    <div class="d-flex justify-content-between text-muted small">
                        <span>조회수 15,234</span>
                        <span>업데이트 2026.01.20</span>
                    </div>
                </a>

                <!-- 항목 4 -->
                <a href="#" class="list-group-item list-group-item-action p-4">
                    <div class="d-flex w-100 justify-content-between align-items-start mb-2">
                        <span class="badge bg-warning">위자료</span>
                    </div>
                    <h5 class="mb-2">위자료 청구 요건과 산정 기준</h5>
                    <p class="mb-3 text-muted">위자료 청구가 가능한 경우, 산정 기준, 청구 절차를 다룹니다.</p>
                    <div class="d-flex justify-content-between text-muted small">
                        <span>조회수 8,543</span>
                        <span>업데이트 2026.01.18</span>
                    </div>
                </a>
            </div>

            <!-- 페이지네이션 -->
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item disabled">
                        <a class="page-link" href="#">이전</a>
                    </li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                    <li class="page-item"><a class="page-link" href="#">4</a></li>
                    <li class="page-item"><a class="page-link" href="#">5</a></li>
                    <li class="page-item">
                        <a class="page-link" href="#">다음</a>
                    </li>
                </ul>
            </nav>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>