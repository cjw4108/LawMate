<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<fmt:formatDate value="${consult.createdAt}" pattern="yyyy-MM-dd'T'HH:mm"
                var="fmtDate" />

<main class="main" style="padding-top: 100px;">
    <div class="container shadow-sm border rounded p-0" style="max-width: 600px;">
        <div class="bg-light p-4 text-center border-bottom">
            <h3 class="mb-0">상담 상세 화면</h3>
        </div>
        <div class="p-4 bg-white">
            <form method="post" id="frm">
                <div class="row">
                    <input type="hidden" class="form-control" id="id" name="id" value='${consult.id}'>
                    <div class="col-md-6">
                        <div class="mb-4">
                            <label class="fw-bold mb-2">회원ID</label>
                            <input type="text" class="form-control" id="userId" name="userId" value='${consult.userId}' readonly>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-4">
                            <label class="fw-bold mb-2">생성일자</label>
                            <input type="text" class="form-control"  readonly
                                   value="<fmt:formatDate  pattern="yyyy-MM-dd'T'HH:mm" value='${fmtDate}'/>">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-4">
                            <label class="fw-bold mb-2">답변여부</label>
                            <input type="text" class="form-control" id="answered" name="answered" value='${consult.answered}' readonly>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-4">
                            <label class="fw-bold mb-2">답변ID</label>
                            <input type="text" class="form-control" id="adoptedAnswer" name="adoptedAnswer" value='${consult.adoptedAnswer}' readonly>
                        </div>
                    </div>
                </div>
                <div class="mb-4">
                    <label class="fw-bold mb-2">제목</label>
                    <input type="text" class="form-control" id="title" name="title" value='${consult.title}' placeholder="텍스트를 입력해 주세요.">
                </div>
                <div class="mb-4">
                    <label class="fw-bold mb-2">질문 내용</label>
                    <textarea class="form-control" rows="5" id="content" name="content" value='${consult.content}' ></textarea>
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