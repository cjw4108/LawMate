<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<script type="text/javascript">
    $(document).ready(function(){
        let msg = "${msg}"  // 모델에 등록 성공 메시지를 가져올 때 처리 하는 내용..
        if(msg!=''){	// 초기 등록화면과 구분하기 위해..
            if(confirm(msg+"\n조회화면 이동하시겠습니까?")){
                location.href="consultList"
            }
        }
        $("#lstBtn").click(function(){
            location.href="consultList"
        })
    });
</script>

<main class="main" style="padding-top: 100px;">
    <div class="container shadow-sm border rounded p-0" style="max-width: 600px;">
        <div class="bg-light p-4 text-center border-bottom">
            <h3 class="mb-0">상담 등록 화면</h3>
        </div>
        <div class="p-4 bg-white">
            <form action="/consult/consultInsert" method="post">
                <div class="mb-4">
                    <label class="fw-bold mb-2">회원ID</label>
                    <input type="text" class="form-control" id="userId" name="userId" placeholder="회원ID를 입력해 주세요.">
                </div>
                <div class="mb-4">
                    <label class="fw-bold mb-2">제목</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="텍스트를 입력해 주세요.">
                </div>
                <div class="mb-4">
                    <label class="fw-bold mb-2">질문 내용</label>
                    <textarea class="form-control" rows="10" id="content" name="content" placeholder="텍스트를 입력해 주세요."></textarea>
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