<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container" data-aos="fade-up">

        <div class="row mb-5 align-items-center">
            <div class="col-lg-6 offset-lg-3">
                <div class="search-bar shadow-sm p-2 bg-white rounded-pill d-flex align-items-center border">
                    <input type="text" class="form-control border-0 ms-3" placeholder="검색어를 입력해주세요." style="box-shadow: none;">
                    <button class="btn btn-link text-dark me-2"><i class="bi bi-search fs-4"></i></button>
                </div>
            </div>
            <div class="col-lg-3 text-end">
                <div class="dropdown">
                    <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                        정렬
                    </button>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#">최신순</a></li>
                        <li><a class="dropdown-item" href="#">답변 많은 순</a></li>
                        <li><a class="dropdown-item" href="#">공감 많은 순</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="qna-list mb-5">
            <%-- 반복될 질문 아이템 --%>
            <div class="p-4 bg-light border rounded mb-3 shadow-sm">
                <div class="bg-white p-2 rounded border mb-2" style="display: inline-block; min-width: 200px;">
                    <h5 class="mb-0">질문 제목 1</h5>
                </div>
                <div class="text-muted small">
                    작성자 | 작성 일 | 답변 수 | 공감 수
                </div>
            </div>

            <div class="p-4 bg-light border rounded mb-3 shadow-sm">
                <div class="bg-white p-2 rounded border mb-2" style="display: inline-block; min-width: 200px;">
                    <h5 class="mb-0">질문 제목 2</h5>
                </div>
                <div class="text-muted small">
                    작성자 | 작성 일 | 답변 수 | 공감 수
                </div>
            </div>
        </div>

        <div class="text-center">
            <a href="/qna/register" class="btn btn-secondary w-100 py-3 fw-bold">신규 질문 등록</a>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />