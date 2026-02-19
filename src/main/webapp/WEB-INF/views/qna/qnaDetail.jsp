<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container shadow-sm border rounded p-0" style="max-width: 700px;">
        <%-- 질문 헤더 --%>
        <div class="bg-light p-4 text-center border-bottom">
            <h3 class="mb-2">${question.title}</h3>
            <span class="text-muted">
                작성자: ${question.userId} |
                작성일: <fmt:parseDate value="${question.createdAt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                <fmt:formatDate value="${parsedDate}" pattern="yyyy.MM.dd HH:mm" />
            </span>
        </div>

        <div class="p-4 bg-white">
            <label class="fw-bold mb-2">질문 내용</label>
            <div class="border rounded p-3 mb-3" style="min-height: 300px; white-space: pre-wrap;">${question.content}</div>

            <%-- 버튼 영역: 신고 및 찜하기 --%>
            <div class="d-flex justify-content-between align-items-center mb-5">
                <button class="btn btn-sm btn-outline-secondary" onclick="reportQuestion(${question.id})">
                    <i class="bi bi-exclamation-triangle"></i> 신고
                </button>
                <div>
                    <%-- 찜하기 버튼: 서버에서 전달받은 isFavorite 상태에 따라 아이콘 변경 --%>
                    <button id="favBtn" class="btn btn-sm btn-outline-danger me-1" onclick="toggleFavorite(${question.id})">
                        <i id="heartIcon" class="bi ${isFavorite ? 'bi-heart-fill' : 'bi-heart'}"></i> 찜하기
                    </button>
                    <button class="btn btn-sm btn-outline-primary"><i class="bi bi-share"></i> 공유</button>
                </div>
            </div>

            <%-- 답변 영역 --%>
            <h5 class="fw-bold mb-3">답변 (${question.answered})</h5>
            <div id="answerList">
                <c:choose>
                    <c:when test="${not empty replies}">
                        <c:forEach var="reply" items="${replies}">
                            <div class="border rounded p-3 mb-3">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="fw-bold text-primary">${reply.userId} (전문가)</span>
                                    <span class="text-muted small">
                                        <fmt:parseDate value="${reply.createdAt}" pattern="yyyy-MM-dd'T'HH:mm" var="rDate" type="both" />
                                        <fmt:formatDate value="${rDate}" pattern="yyyy.MM.dd HH:mm" />
                                    </span>
                                </div>
                                <div style="white-space: pre-wrap;">${reply.content}</div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5 text-muted border rounded mb-4">
                            아직 등록된 답변이 없습니다. 첫 번째 답변을 남겨주세요!
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- 답변 작성 영역 --%>
            <h5 class="fw-bold mb-3 mt-4">답변 작성</h5>
            <form action="/qna/reply/${question.id}" method="post">
                <div class="input-group mb-3 shadow-sm">
                    <textarea name="content" class="form-control" rows="2" placeholder="답변 내용을 입력해 주세요." required></textarea>
                    <button class="btn btn-dark px-4" type="submit">등록</button>
                </div>
            </form>
        </div>
    </div>
</main>



<script>
    // 1. 신고 기능 (AJAX)
    function reportQuestion(id) {
        const reason = prompt("신고 사유를 입력해주세요:");
        if (!reason) return;

        // 절대 경로인 /qna/report/ 를 사용합니다.
        $.ajax({
            url: '/qna/report/' + id,
            type: 'POST',
            data: { reason: reason }, // 서버의 @RequestParam("reason")과 일치해야 함
            success: function(data) {
                if(data === "success") {
                    alert("신고가 정상적으로 접수되었습니다.");
                } else {
                    alert("처리 실패: " + data);
                }
            },
            error: function(xhr) {
                // 여기서 404가 뜨면 url 주소가 틀린 것입니다.
                console.log("에러 상태코드: " + xhr.status);
                alert("서버 주소를 찾을 수 없습니다. (404 Error)");
            }
        });
    }

    // 2. 찜하기 토글 기능 (AJAX)
    function toggleFavorite(id) {
        $.ajax({
            url: '/qna/favorite/' + id,
            type: 'POST',
            success: function(data) {
                const icon = document.getElementById('heartIcon');
                if(data === 'added') {
                    icon.classList.replace('bi-heart', 'bi-heart-fill');
                    alert("찜 목록에 추가되었습니다.");
                } else if(data === 'removed') {
                    icon.classList.replace('bi-heart-fill', 'bi-heart');
                    alert("찜 목록에서 제거되었습니다.");
                }
            },
            error: function() { alert("로그인이 필요한 기능입니다."); }
        });
    }

    // 헤더의 찜 개수 배지 실시간 업데이트
    function updateHeaderCount(diff) {
        const countBadge = document.getElementById('cartCount');
        if(countBadge) {
            let current = parseInt(countBadge.innerText) || 0;
            countBadge.innerText = Math.max(0, current + diff);
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />