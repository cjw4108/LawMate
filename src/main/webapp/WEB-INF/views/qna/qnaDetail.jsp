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
                    <%-- 찜하기 버튼: 비동기 처리 --%>
                    <button id="favBtn" class="btn btn-sm btn-outline-danger me-1" onclick="toggleFavorite(${question.id})">
                        <i id="heartIcon" class="bi bi-heart"></i> 찜하기
                    </button>
                    <button class="btn btn-sm btn-outline-primary"><i class="bi bi-share"></i> 공유</button>
                </div>
            </div>

            <%-- 답변 영역 (예시 데이터) --%>
            <h5 class="fw-bold mb-3">답변 (${question.answered})</h5>
            <div id="answerList">
                <c:choose>
                    <c:when test="${question.answered > 0}">
                        <div class="border rounded p-3 mb-4 d-flex justify-content-between align-items-center">
                            <span>전문가 답변이 등록되어 있습니다.</span>
                            <div>
                                <button class="btn btn-sm btn-link text-dark p-0 me-2">신고</button>
                                <i class="bi bi-heart"></i>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-4 text-muted border rounded mb-4">
                            아직 등록된 답변이 없습니다.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- 답변 작성 영역 --%>
            <h5 class="fw-bold mb-3">답변 작성</h5>
            <div class="input-group mb-3">
                <input type="text" id="answerInput" class="form-control" placeholder="답변 내용을 입력해 주세요.">
                <button class="btn btn-dark" type="button" onclick="alert('준비 중인 기능입니다.')">답변 등록</button>
            </div>
        </div>
    </div>
</main>

<script>
    // 1. 신고 기능 (AJAX)
    function reportQuestion(id) {
        const reason = prompt("신고 사유를 입력해주세요:");
        if (!reason) return;

        $.post("/qna/report/" + id, { reason: reason }, function(data) {
            if(data === "success") {
                alert("신고가 접수되었습니다.");
            }
        });
    }

    // 2. 찜하기 토글 기능 (AJAX)
    function toggleFavorite(id) {
        fetch('/qna/favorite/' + id, {
            method: 'POST'
        })
            .then(res => res.text())
            .then(data => {
                const icon = document.getElementById('heartIcon');
                if(data === 'added') {
                    icon.classList.replace('bi-heart', 'bi-heart-fill');
                    alert("찜 목록에 추가되었습니다.");
                    updateHeaderCount(1); // 헤더 숫자 증가 함수 (필요 시 구현)
                } else if(data === 'removed') {
                    icon.classList.replace('bi-heart-fill', 'bi-heart');
                    alert("찜 목록에서 제거되었습니다.");
                    updateHeaderCount(-1);
                }
            })
            .catch(err => alert("로그인이 필요한 기능입니다."));
    }

    // 헤더의 찜 개수 배지 업데이트 (선택 사항)
    function updateHeaderCount(diff) {
        const countBadge = document.getElementById('cartCount');
        if(countBadge) {
            let current = parseInt(countBadge.innerText) || 0;
            countBadge.innerText = Math.max(0, current + diff);
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />