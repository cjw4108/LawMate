<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container shadow-sm border rounded p-0" style="max-width: 700px;">
        <div class="bg-light p-4 text-center border-bottom">
            <h3 class="mb-2">질문 제목</h3>
            <span class="text-muted">작성자 | 작성일</span>
        </div>

        <div class="p-4 bg-white">
            <label class="fw-bold mb-2">질문 내용</label>
            <div class="border rounded p-3 mb-3" style="min-height: 300px;">
                텍스트가 표시됩니다.
            </div>

            <div class="d-flex justify-content-between align-items-center mb-5">
                <button class="btn btn-sm btn-outline-secondary">신고</button>
                <div>
                    <button class="btn btn-sm btn-outline-danger me-1"><i class="bi bi-heart"></i></button>
                    <button class="btn btn-sm btn-outline-primary"><i class="bi bi-share"></i> 공유</button>
                </div>
            </div>

            <h5 class="fw-bold mb-3">답변</h5>
            <div class="border rounded p-3 mb-4 d-flex justify-content-between align-items-center">
                <span>달린 답변들...</span>
                <div>
                    <button class="btn btn-sm btn-link text-dark p-0 me-2">신고</button>
                    <i class="bi bi-heart"></i>
                </div>
            </div>

            <h5 class="fw-bold mb-3">답변 작성</h5>
            <div class="input-group">
                <input type="text" class="form-control" placeholder="텍스트를 입력해 주세요.">
                <button class="btn btn-dark" type="button">답변 등록</button>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />