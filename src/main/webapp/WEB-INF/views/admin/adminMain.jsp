<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container">
        <div class="d-flex justify-content-between align-items-end mb-4">
            <h3 class="fw-bold mb-0">운영 현황 요약</h3>
            <span class="text-muted small">최종 업데이트: 2024-11-20 14:00</span>
        </div>

        <div class="row g-4 mb-5 text-center">
            <div class="col-md-6">
                <div class="stat-box p-5 bg-white shadow-sm rounded border">
                    <h5 class="text-muted">미답변 질문</h5>
                    <p class="display-4 fw-bold text-primary mb-0">12</p>
                </div>
            </div>
            <div class="col-md-6">
                <div class="stat-box danger p-5 bg-white shadow-sm rounded border">
                    <h5 class="text-muted">신규 신고 건수</h5>
                    <p class="display-4 fw-bold text-danger mb-0">3</p>
                </div>
            </div>
        </div>

        <h3 class="fw-bold mb-4">시스템 관리</h3>
        <div class="row g-3">
            <div class="col-md-4">
                <a href="/admin/qna" class="btn btn-outline-dark w-100 py-4 fs-5 fw-bold shadow-sm">
                    <i class="bi bi-layout-text-window me-2"></i> 게시판 운영
                </a>
            </div>
            <div class="col-md-4">
                <a href="/admin/users" class="btn btn-outline-dark w-100 py-4 fs-5 fw-bold shadow-sm">
                    <i class="bi bi-people me-2"></i> 사용자 관리
                </a>
            </div>
        </div>
    </div>
</main>