보내주신 JSP 코드 내의 자바스크립트 함수 정의와 호출부의 인자 불일치 문제를 해결하여 전체 코드를 다시 구성해 드립니다.

핵심 수정 사항은 editReply 함수가 호출될 때 **내용(content)**을 직접 인자로 받도록 하고, 자바스크립트 내에서 이를 정상적으로 처리하도록 변경한 것입니다.

## qnaDetail.jsp (전체 코드)
Java
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
                    <button id="favBtn"
                            class="btn btn-sm btn-outline-danger me-1"
                            onclick="toggleFavorite(${question.id})">
                        <i id="heartIcon"
                           class="bi ${isFavorite == true ? 'bi-heart-fill text-danger' : 'bi-heart'}">
                        </i>
                        <span id="favoriteCount">${question.favoriteCount}</span>
                    </button>
                    <button class="btn btn-sm btn-outline-primary"
                            onclick="shareQuestion(${question.id})">
                        <i class="bi bi-share"></i> 공유
                    </button>
                </div>
            </div>

            <%-- 답변 영역 --%>
            <h5 class="fw-bold mb-3">답변 (${replyCount})</h5>
            <div id="answerList">
                <c:choose>
                    <c:when test="${not empty replies}">
                        <c:forEach var="reply" items="${replies}">
                            <div class="border rounded p-3 mb-3">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="fw-bold text-primary">
                                        ${reply.userId}
                                        (${reply.userId == 'admin' ? '관리자' : '전문가'})
                                    </span>
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="text-muted small">
                                            <fmt:parseDate value="${reply.createdAt}" pattern="yyyy-MM-dd'T'HH:mm" var="rDate" type="both" />
                                            <fmt:formatDate value="${rDate}" pattern="yyyy.MM.dd HH:mm" />
                                        </span>
                                            <%-- 관리자 또는 승인된 변호사만 수정/삭제 버튼 보임 --%>
                                        <c:if test="${sessionScope.loginUser.role == 'ROLE_ADMIN' or
                                            (sessionScope.loginUser.role == 'ROLE_LAWYER' and
                                            sessionScope.loginUser.lawyerStatus == 'APPROVED')}">
                                            <%-- 수정: 내용을 인자로 직접 전달하도록 수정 --%>
                                            <button class="btn btn-sm btn-outline-primary"
                                                    onclick="editReply(${reply.id}, '${reply.content.replace("'", "\\'")}')">수정</button>
                                            <button class="btn btn-sm btn-outline-danger"
                                                    onclick="deleteReply(${reply.id})">삭제</button>
                                        </c:if>
                                    </div>
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

            <%-- 관리자/승인된 변호사만 답변 작성 가능 --%>
            <c:if test="${sessionScope.loginUser.role == 'ROLE_ADMIN' or
                (sessionScope.loginUser.role == 'ROLE_LAWYER' and
                sessionScope.loginUser.lawyerStatus == 'APPROVED')}">
                <h5 class="fw-bold mb-3 mt-4">답변 작성</h5>
                <form action="/qna/reply/${question.id}" method="post">
                    <div class="input-group mb-3 shadow-sm">
                        <textarea name="content" class="form-control" rows="2"
                                  placeholder="답변 내용을 입력해 주세요." required></textarea>
                        <button class="btn btn-dark px-4" type="submit">등록</button>
                    </div>
                </form>
            </c:if>

            <%-- 돌아가기 --%>
            <div class="text-center mt-4">
                <a href="/qna/list?sort=${sort}&keyword=${keyword}"
                   class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> 목록으로 돌아가기
                </a>
            </div>
        </div>
    </div>
</main>

<script>
    // 수정된 editReply 함수: ID와 기존 내용을 직접 받습니다.
    function editReply(replyId, currentContent) {
        const newContent = prompt("답변을 수정해주세요:", currentContent);

        if (newContent === null) return; // 취소 클릭 시
        if (newContent.trim() === '') {
            alert("내용을 입력해 주세요.");
            return;
        }

        fetch('/reply/update', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'id=' + replyId + '&content=' + encodeURIComponent(newContent)
        })
            .then(res => res.text())
            .then(result => {
                if (result === 'success') {
                    alert("답변이 수정되었습니다.");
                    location.reload();
                } else if (result === 'fail_auth') {
                    alert("수정 권한이 없습니다.");
                } else {
                    alert("수정 실패");
                }
            })
            .catch(err => {
                console.error("Error:", err);
                alert("수정 중 오류가 발생했습니다.");
            });
    }

    function deleteReply(replyId) {
        if(confirm("이 답변을 삭제하시겠습니까?")) {
            fetch('/reply/delete?id=' + replyId, { method: 'POST' })
                .then(res => res.text())
                .then(result => {
                    if(result === 'success') {
                        alert("답변이 삭제되었습니다.");
                        location.reload();
                    } else if (result === 'fail_auth') {
                        alert("삭제 권한이 없습니다.");
                    } else {
                        alert("삭제 실패");
                    }
                });
        }
    }

    function reportQuestion(id) {
        const reason = prompt("신고 사유를 입력해주세요:");
        if (!reason) return;

        $.ajax({
            url: '/qna/report/' + id,
            type: 'POST',
            data: { reason: reason },
            success: function(data) {
                if(data === "success") {
                    alert("신고가 정상적으로 접수되었습니다.");
                } else {
                    alert("처리 실패: " + data);
                }
            },
            error: function(xhr) {
                alert("서버 오류가 발생했습니다.");
            }
        });
    }

    function toggleFavorite(id) {
        $.ajax({
            url: '/qna/favorite/' + id,
            type: 'POST',
            success: function(data) {
                if (data.status === 'login_required') {
                    alert("로그인이 필요한 기능입니다.");
                    location.href = '/login';
                    return;
                }

                const icon = document.getElementById('heartIcon');
                const countSpan = document.getElementById('favoriteCount');

                if (data.status === 'added') {
                    icon.classList.remove('bi-heart');
                    icon.classList.add('bi-heart-fill', 'text-danger');
                } else {
                    icon.classList.remove('bi-heart-fill', 'text-danger');
                    icon.classList.add('bi-heart');
                }
                countSpan.innerText = data.count;
            }
        });
    }

    function shareQuestion(questionId) {
        const url = window.location.origin + "/qna/detail/" + questionId;
        if (navigator.share) {
            navigator.share({
                title: document.title,
                text: "이 질문을 확인해보세요!",
                url: url
            }).catch(err => console.log(err));
        } else {
            navigator.clipboard.writeText(url);
            alert("링크가 복사되었습니다!");
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />