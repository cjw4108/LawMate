<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<fmt:formatDate value="${consult.createdAt}" pattern="yyyy-MM-dd'T'HH:mm"
                var="fmtDate" />
<script type="text/javascript">
    $(document).ready(function() {
        // 버튼 이벤트 핸들러 (기능에 맞게 action 설정 필요)
        $("#uptBtn").click(function() {
            if (confirm("수정하시겠습니까?")) {
                $("#frm").attr("action", "updateConsult");
                $("#frm").submit();
            }
        });

        $("#delBtn").click(function() {
            //alert(${consult.id})
            if (confirm("정말로 삭제하시겠습니까?")) {
                $("#frm").attr("action", "deleteConsult");
                $("#frm").submit();
            }
        });

        $("#lstBtn").click(function() {
            location.href = "consultList"; // 예시 이동 경로
        });
        let msg = "${msg}"
        if(msg!=""){
            if(msg.indexOf("수정")>-1){
                if(confirm(msg+"조회화면으로 이동하시겠습니까?")){
                    location.href = "consultList";
                }
            }
            if(msg.indexOf("삭제")>-1){
                //alert(msg)
                location.href = "consultList";
            }
        }
    });
</script>

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
                            <input type="datetime-local" value='${fmtDate}' pattern="yyyy-MM-dd'T'HH:mm"
                                   class="form-control" id="createdAt" name="createdAt"/>
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
                    <textarea class="form-control" rows="5" id="content" name="content">${consult.content}</textarea>
                </div>
                <div class="d-flex justify-content-between">
                    <button id="uptBtn" type="button" class="btn btn-success btn-lg">수정</button>
                    <button id="delBtn" type="button" class="btn btn-danger btn-lg">삭제</button>
                    <button id="lstBtn" type="button" class="btn btn-info btn-lg">메인으로</button>
                </div>
            </form>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />