<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container">
        <div class="row mb-5 justify-content-center">
            <div class="col-lg-8">
                <div class="search-wrapper bg-white shadow-sm rounded-pill p-2 d-flex border">
                    <input type="text" class="form-control border-0 ms-3" placeholder="궁금한 법률 내용을 검색해보세요.">
                    <button class="btn btn-primary rounded-pill px-4">질문 찾기</button>
                </div>
            </div>
        </div>

        <div class="qna-list mb-5">
            <div class="card border-0 shadow-sm mb-3 hover-up" onclick="location.href='/qna/detail'">
                <div class="card-body p-4">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <span class="badge bg-primary-subtle text-primary mb-2">답변 대기</span>
                            <h5 class="fw-bold">전세 사기 관련 법적 조치 방법이 궁금합니다.</h5>
                            <p class="text-muted small mb-0">작성자: 홍길동 | 2024.11.20 | 조회 45</p>
                        </div>
                        <div class="text-center bg-light p-2 rounded">
                            <span class="d-block fw-bold">답변</span>
                            <span class="text-primary">0</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <a href="/qna/register" class="btn btn-dark w-100 py-3 rounded-3 shadow">
            <i class="bi bi-pencil-square me-2"></i> 새로운 질문 등록하기
        </a>
    </div>
</main>