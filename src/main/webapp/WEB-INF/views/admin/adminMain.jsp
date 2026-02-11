<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container" data-aos="fade-up">
        <h3 class="fw-bold mb-4">운영 현황 요약</h3>
        <div class="row mb-5 text-center">
            <div class="col-md-6">
                <div class="p-5 border bg-light rounded shadow-sm">
                    <h2 class="display-6 fw-bold">미답변</h2>
                    <hr>
                    <p class="h1">12</p>
                </div>
            </div>
            <div class="col-md-6">
                <div class="p-5 border bg-light rounded shadow-sm">
                    <h2 class="display-6 fw-bold text-danger">신고글</h2>
                    <hr>
                    <p class="h1">3</p>
                </div>
            </div>
        </div>

        <h3 class="fw-bold mb-4">빠른 관리 메뉴</h3>
        <div class="d-grid gap-3 col-lg-4">
            <a href="/admin/qna" class="btn btn-secondary py-3 fw-bold">게시판 운영</a>
            <a href="/admin/users" class="btn btn-secondary py-3 fw-bold">사용자 관리</a>
            <a href="#" class="btn btn-secondary py-3 fw-bold">변호사 권한 승인</a>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />