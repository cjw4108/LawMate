<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<style>
    * { box-sizing: border-box; }
    body { font-family: 'Nanum Gothic', sans-serif; background: #f4f6fb; margin: 0; padding: 30px; }
    .container { max-width: 860px; margin: 0 auto; }

    /* 카드 헤더: 프로필 사진 + 이름/등록번호 */
    .profile-card {
        background: linear-gradient(135deg, #1a3c6b 0%, #2d5fa8 100%);
        border-radius: 12px 12px 0 0;
        padding: 36px 48px;
        display: flex; align-items: center; gap: 32px;
        color: #fff;
    }
    .profile-img {
        width: 100px; height: 100px; border-radius: 50%;
        object-fit: cover; border: 3px solid rgba(255,255,255,0.6);
        background: #4a6fa5; display: flex; align-items: center;
        justify-content: center; font-size: 42px; flex-shrink: 0;
    }
    .profile-img img { width: 100%; height: 100%; border-radius: 50%; object-fit: cover; }
    .profile-info h1 { margin: 0 0 4px; font-size: 26px; }
    .profile-info .name-en { font-size: 15px; opacity: 0.8; margin-bottom: 8px; }
    .profile-info .bar-number { font-size: 13px; background: rgba(255,255,255,0.18);
        display: inline-block; padding: 3px 12px; border-radius: 20px; }
    .badge-status { display: inline-block; padding: 3px 12px; border-radius: 20px;
        font-size: 12px; font-weight: bold; margin-left: 10px; }
    .badge-active   { background: #28c76f; color: #fff; }
    .badge-inactive { background: #aaa;    color: #fff; }

    /* 본문 */
    .body-card {
        background: #fff; border-radius: 0 0 12px 12px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.10);
        padding: 36px 48px;
    }

    .section { margin-bottom: 28px; }
    .section-title-2 {
        font-size: 13px; font-weight: bold; color: #1a3c6b;
        background: #eef2fb; padding: 6px 14px;
        border-left: 4px solid #1a3c6b; border-radius: 0 4px 4px 0;
        margin-bottom: 16px;
    }

    /* 2열 그리드 */
    .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px 30px; }
    .info-item { display: flex; flex-direction: column; }
    .info-item.full { grid-column: 1 / -1; }
    .info-label { font-size: 12px; color: #888; margin-bottom: 3px; }
    .info-value { font-size: 15px; color: #222; font-weight: 500;
        background: #f8f9fc; border-radius: 5px; padding: 8px 12px;
        min-height: 38px; white-space: pre-line; word-break: break-all; }
    .info-value.empty { color: #bbb; font-style: italic; font-size: 13px; }

    /* 버튼 영역 */
    .btn-area { display: flex; gap: 12px; margin-top: 32px; justify-content: flex-end; }
    .btn { padding: 9px 26px; border: none; border-radius: 5px; font-size: 14px;
        cursor: pointer; font-family: inherit; font-weight: bold;
        text-decoration: none; display: inline-block; transition: opacity 0.2s; }
    .btn:hover { opacity: 0.85; }
    .btn-primary   { background: #1a3c6b; color: #fff; }
    .btn-secondary { background: #888;    color: #fff; }
    .btn-danger    { background: #dc3545; color: #fff; }

    .msg-success { color: green; background: #d4edda; border-radius: 5px;
        padding: 10px 16px; margin-bottom: 16px; font-size: 14px; }

    /* 전문분야 태그 */
    .tags { display: flex; flex-wrap: wrap; gap: 7px; padding: 8px 12px;
        background: #f8f9fc; border-radius: 5px; min-height: 38px; }
    .tag { background: #1a3c6b; color: #fff; font-size: 13px;
        padding: 3px 12px; border-radius: 20px; }
</style>
<body>
<br><br>
<div class="container">

    <c:if test="${not empty successMsg}">
        <div class="msg-success">✔ ${successMsg}</div>
    </c:if>

    <!-- 프로필 헤더 -->
    <div class="profile-card">
        <div class="profile-img">
            <c:choose>
                <c:when test="${not empty lawyer.profileImage}">
                    <img src="${lawyer.profileImage}" alt="프로필 이미지"
                         onerror="this.parentNode.innerHTML='⚖️'"/>
                </c:when>
                <c:otherwise>⚖️</c:otherwise>
            </c:choose>
        </div>
        <div class="profile-info">
            <h1 style="color: #fff;">
                ${lawyer.name}
                <span class="badge-status ${lawyer.status == 'ACTIVE' ? 'badge-active' : 'badge-inactive'}">
                    ${lawyer.status == 'ACTIVE' ? '활성' : '비활성'}
                </span>
            </h1>
            <c:if test="${not empty lawyer.nameEn}">
                <div class="name-en">${lawyer.nameEn}</div>
            </c:if>
            <div class="bar-number">등록번호 : ${lawyer.barNumber}</div>
        </div>
    </div>

    <!-- 상세 본문 -->
    <div class="body-card">

        <!-- 기본 정보 -->
        <div class="section">
            <div class="section-title-2">📋 기본 정보</div>
            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">이메일</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${not empty lawyer.email}">
                                <a href="mailto:${lawyer.email}" style="color:#1a3c6b;">${lawyer.email}</a>
                            </c:when>
                            <c:otherwise><span class="empty">-</span></c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="info-item">
                    <span class="info-label">전화번호</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${not empty lawyer.phone}">${lawyer.phone}</c:when>
                            <c:otherwise><span class="empty">-</span></c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="info-item">
                    <span class="info-label">변호사 등록일</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${not empty lawyer.admissionDate}">
                                <fmt:formatDate value="${lawyer.admissionDate}" pattern="yyyy년 MM월 dd일"/>
                            </c:when>
                            <c:otherwise><span class="empty">-</span></c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="info-item">
                    <span class="info-label">등록/수정일시</span>
                    <span class="info-value" style="font-size:13px;">
                        등록: <fmt:formatDate value="${lawyer.createdAt}" pattern="yyyy-MM-dd HH:mm"/><br/>
                        수정: <fmt:formatDate value="${lawyer.updatedAt}" pattern="yyyy-MM-dd HH:mm"/>
                    </span>
                </div>
            </div>
        </div>

        <!-- 전문 분야 -->
        <div class="section">
            <div class="section-title-2">⚖️ 전문 분야</div>
            <c:choose>
                <c:when test="${not empty lawyer.specialty}">
                    <div class="tags">
                        <%-- 콤마 구분 태그 표시 --%>
                        <c:forEach var="tag" items="${fn:split(lawyer.specialty, ',')}">
                            <span class="tag">${fn:trim(tag)}</span>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="info-value empty">등록된 전문 분야가 없습니다.</div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 학력 -->
        <div class="section">
            <div class="section-title-2">🎓 학력</div>
            <div class="info-value">
                <c:choose>
                    <c:when test="${not empty lawyer.education}">${lawyer.education}</c:when>
                    <c:otherwise><span class="empty">-</span></c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- 경력 -->
        <div class="section">
            <div class="section-title-2">💼 경력</div>
            <div class="info-value" style="min-height:80px;">
                <c:choose>
                    <c:when test="${not empty lawyer.career}">${lawyer.career}</c:when>
                    <c:otherwise><span class="empty">-</span></c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- 소개 -->
        <div class="section">
            <div class="section-title-2">📝 소개</div>
            <div class="info-value" style="min-height:80px;">
                <c:choose>
                    <c:when test="${not empty lawyer.introduction}">${lawyer.introduction}</c:when>
                    <c:otherwise><span class="empty">-</span></c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- 버튼 -->
        <div class="btn-area">
            <a href="/lawyer/list" class="btn btn-secondary">← 목록</a>
            <a href="/lawyer/modify/${lawyer.lawyerId}" class="btn btn-primary">✏ 수정</a>
            <form method="post" action="/lawyer/delete/${lawyer.lawyerId}" style="margin:0;"
                  onsubmit="return confirm('${lawyer.name} 변호사를 삭제하시겠습니까?')">
                <button type="submit" class="btn btn-danger">🗑 삭제</button>
            </form>
        </div>

    </div><!-- /body-card -->
</div><!-- /container -->
</body>
</html>
