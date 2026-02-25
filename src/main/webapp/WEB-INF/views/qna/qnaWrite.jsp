<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container shadow-sm border rounded p-0" style="max-width: 600px;">
        <div class="bg-light p-4 text-center border-bottom">
            <h3 class="mb-0">질문 등록 화면</h3>
        </div>
        <div class="p-4 bg-white">
            <form action="/qna/write" method="post">
                <div class="mb-4">
                    <label class="fw-bold mb-2">제목</label>
                    <input type="text" name="title" class="form-control" placeholder="텍스트를 입력해 주세요.">
                </div>
                <div class="mb-4">
                    <label class="fw-bold mb-2">질문 내용</label>
                    <textarea name="content" class="form-control" rows="10" placeholder="텍스트를 입력해 주세요."></textarea>
                </div>
                <div class="d-flex justify-content-between">
                    <button type="submit" class="btn btn-dark px-4">등록</button>
                    <button type="button" class="btn btn-secondary px-4" onclick="history.back();">취소</button>
                </div>
            </form>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />