<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<style>
    .main {
        margin-top: 100px;
        min-height: 600px;
    }

    .cart-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 40px 20px;
    }

    .cart-title {
        font-size: 28px;
        font-weight: 700;
        margin-bottom: 30px;
        color: #1e293b;
    }

    .cart-empty {
        text-align: center;
        padding: 100px 20px;
        color: #94a3b8;
    }

    .cart-empty i {
        font-size: 80px;
        margin-bottom: 20px;
    }

    .cart-table {
        background: white;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        overflow: hidden;
    }

    .cart-item {
        display: flex;
        align-items: center;
        padding: 20px;
        border-bottom: 1px solid #e5e7eb;
        transition: background 0.2s;
    }

    .cart-item:hover {
        background: #f8fafc;
    }

    .cart-item:last-child {
        border-bottom: none;
    }

    .cart-item-info {
        flex: 1;
        margin-left: 20px;
    }

    .cart-item-category {
        display: inline-block;
        padding: 4px 12px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 500;
        margin-bottom: 8px;
    }

    .cart-item-title {
        font-size: 18px;
        font-weight: 600;
        color: #1e293b;
        margin-bottom: 4px;
    }

    .cart-item-date {
        font-size: 13px;
        color: #94a3b8;
    }

    .cart-item-actions {
        display: flex;
        gap: 10px;
    }

    .btn-download {
        padding: 10px 20px;
        background: #3b82f6;
        color: white;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.2s;
    }

    .btn-download:hover {
        background: #2563eb;
    }

    .btn-delete {
        padding: 10px 20px;
        background: white;
        color: #ef4444;
        border: 1px solid #ef4444;
        border-radius: 8px;
        cursor: pointer;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.2s;
    }

    .btn-delete:hover {
        background: #ef4444;
        color: white;
    }

    .cart-footer {
        margin-top: 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .cart-summary {
        font-size: 18px;
        font-weight: 600;
        color: #1e293b;
    }

    .cart-actions {
        display: flex;
        gap: 10px;
    }

    .btn-clear {
        padding: 12px 24px;
        background: white;
        color: #64748b;
        border: 1px solid #cbd5e1;
        border-radius: 8px;
        cursor: pointer;
        font-size: 14px;
        font-weight: 500;
    }

    .btn-download-all {
        padding: 12px 24px;
        background: #3b82f6;
        color: white;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-size: 14px;
        font-weight: 600;
    }
</style>

<main class="main">
    <div class="cart-container">
        <h1 class="cart-title">내 서류함</h1>
        <c:choose>
            <c:when test="${empty cartList}">
                <div class="cart-empty">
                    <i class="bi bi-folder-x"></i>
                    <h3>서류함이 비어있습니다</h3>
                    <p>필요한 서류를 담아보세요!</p>
                    <a href="/docs" class="btn btn-primary mt-3">서류 목록 보기</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="cart-table">
                    <c:forEach items="${cartList}" var="item">
                        <div class="cart-item" data-id="${item.id}">
                            <div class="cart-item-info">
                                <span class="cart-item-category bg-primary text-white">${item.categoryName}</span>
                                <div class="cart-item-title">${item.documentTitle}</div>
                                <div class="cart-item-date">
                                    담은 날짜: ${item.createdAt}
                                </div>
                            </div>
                            <div class="cart-item-actions">
                                <button class="btn-download" onclick="downloadFile(${item.documentId})">
                                     다운로드
                                </button>
                                <button class="btn-delete" onclick="deleteCartItem(${item.id})">
                                     삭제
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="cart-footer">
                    <div class="cart-summary">
                        총 <strong>${cartList.size()}</strong>개의 서류
                    </div>
                    <div class="cart-actions">
                        <button class="btn-clear" onclick="clearCart()">전체 삭제</button>
                        <button class="btn-download-all" onclick="downloadAll()">전체 다운로드</button>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>

    function downloadFile(documentId) {
        window.location.href = '/docs/download/' + documentId;
    }

    // 개별 삭제
    function deleteCartItem(cartId) {
        if (!confirm('서류함에서 삭제하시겠습니까?')) return;

        $.ajax({
            url: '/api/cart/delete/' + cartId,
            type: 'DELETE',
            success: function(response) {
                if (response.success) {
                    alert('✅ 삭제되었습니다.');
                    location.reload();
                }
            },
            error: function() {
                alert('❌ 오류가 발생했습니다.');
            }
        });
    }

    function clearCart() {
        if (!confirm('서류함을 비우시겠습니까?')) return;

        $.ajax({
            url: '/api/cart/clear',
            type: 'DELETE',
            success: function(response) {
                if (response.success) {
                    alert('✅ 서류함이 비워졌습니다.');
                    location.reload();
                }
            }
        });
    }

    // 전체 다운로드 (TODO: ZIP 구현)
    function downloadAll() {
        alert('전체 다운로드 기능은 구현 예정입니다.');
        // TODO: ZIP 다운로드 API 연결
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>