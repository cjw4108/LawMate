<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<style>
    * { box-sizing: border-box; }
    body { font-family: 'Nanum Gothic', sans-serif; background: #f4f6fb; margin: 0; padding: 30px; }
    .container { max-width: 820px; margin: 0 auto; background: #fff; border-radius: 10px;
        box-shadow: 0 2px 16px rgba(0,0,0,0.10); padding: 40px 48px; }
    h2 { color: #1a3c6b; border-bottom: 2px solid #1a3c6b; padding-bottom: 10px; margin-bottom: 28px; font-size: 22px; }
    .form-section { margin-bottom: 10px; }
    .section-title-2 { font-size: 13px; font-weight: bold; color: #1a3c6b; background: #eef2fb;
        padding: 6px 12px; border-radius: 4px; margin: 24px 0 14px; }
    .form-row { display: flex; gap: 20px; margin-bottom: 16px; }
    .form-group { display: flex; flex-direction: column; flex: 1; }
    .form-group.full { flex: 1 1 100%; }
    label { font-size: 13px; color: #444; margin-bottom: 5px; font-weight: bold; }
    label .required { color: #e03; margin-left: 2px; }
    input[type="text"], input[type="email"], input[type="date"],
    select, textarea {
        padding: 9px 12px; border: 1px solid #ccc; border-radius: 5px;
        font-size: 14px; font-family: inherit; transition: border 0.2s;
        width: 100%;
    }
    input:focus, select:focus, textarea:focus { outline: none; border-color: #1a3c6b; }
    textarea { resize: vertical; min-height: 90px; }
    .btn-area { display: flex; justify-content: center; gap: 14px; margin-top: 34px; }
    .btn { padding: 10px 32px; border: none; border-radius: 5px; font-size: 15px;
        cursor: pointer; font-family: inherit; font-weight: bold; transition: opacity 0.2s; }
    .btn:hover { opacity: 0.85; }
    .btn-primary { background: #1a3c6b; color: #fff; }
    .btn-secondary { background: #888; color: #fff; }
    .msg-error { color: #c00; background: #fff0f0; border: 1px solid #fcc;
        padding: 10px 16px; border-radius: 5px; margin-bottom: 18px; font-size: 14px; }
    .edit-badge { display: inline-block; background: #f0a500; color: #fff;
        font-size: 12px; padding: 2px 10px; border-radius: 10px;
        margin-left: 10px; vertical-align: middle; }
</style>
<main class="main">
    <br>
<div class="container">

    <h2>
        ⚖️
        <c:choose>
            <c:when test="${not empty lawyer.lawyerId}">
                변호사 정보 수정 <span class="edit-badge">수정</span>
            </c:when>
            <c:otherwise>변호사 등록</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty errorMsg}">
        <div class="msg-error">⚠ ${errorMsg}</div>
    </c:if>

    <c:choose>
        <c:when test="${not empty lawyer.lawyerId}">
            <form method="post" action="/lawyer/modify">
            <input type="hidden" name="lawyerId" value="${lawyer.lawyerId}"/>
        </c:when>
        <c:otherwise>
            <form method="post" action="/lawyer/register">
        </c:otherwise>
    </c:choose>

        <!-- 기본 정보 -->
        <div class="section-title-2">📋 기본 정보</div>

        <div class="form-row">
            <div class="form-group">
                <label>이름 <span class="required">*</span></label>
                <input type="text" name="name" value="${lawyer.name}"
                       placeholder="홍길동" required maxlength="50"/>
            </div>
            <div class="form-group">
                <label>영문 이름</label>
                <input type="text" name="nameEn" value="${lawyer.nameEn}"
                       placeholder="Hong Gildong" maxlength="100"/>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>변호사 등록번호 <span class="required">*</span></label>
                <input type="text" name="barNumber" value="${lawyer.barNumber}"
                       placeholder="2010-서울-12345" required maxlength="30"
                       <c:if test="${not empty lawyer.lawyerId}">readonly style="background:#f5f5f5"</c:if>/>
            </div>
            <div class="form-group">
                <label>변호사 등록일</label>
                <input type="date" name="admissionDate"
                       value="<fmt:formatDate value='${lawyer.admissionDate}' pattern='yyyy-MM-dd'/>"/>
            </div>
        </div>

        <!-- 연락처 -->
        <div class="section-title-2">📞 연락처</div>

        <div class="form-row">
            <div class="form-group">
                <label>이메일</label>
                <input type="email" name="email" value="${lawyer.email}"
                       placeholder="example@lawfirm.com" maxlength="100"/>
            </div>
            <div class="form-group">
                <label>전화번호</label>
                <input type="text" name="phone" value="${lawyer.phone}"
                       placeholder="02-1234-5678" maxlength="20"/>
            </div>
        </div>

        <!-- 전문 분야 / 상태 -->
        <div class="section-title-2">⚖️ 전문 분야 및 상태</div>

        <div class="form-row">
            <div class="form-group">
                <label>전문 분야</label>
                <input type="text" name="specialty" value="${lawyer.specialty}"
                       placeholder="형사, 기업법무, 민사 ..." maxlength="200"/>
            </div>
            <div class="form-group" style="max-width:150px;">
                <label>상태</label>
                <select name="status" readonly onChange='this.selectedIndex = this.initialSelect;'>
                    <option value="ACTIVE"   <c:if test="${lawyer.status == 'ACTIVE'}">selected</c:if>>활성</option>
                    <option value="INACTIVE" <c:if test="${lawyer.status == 'INACTIVE' || empty lawyer.status}">selected</c:if>>비활성</option>
                </select>
            </div>
        </div>

        <!-- 학력 / 경력 -->
        <div class="section-title-2">🎓 학력 및 경력</div>

        <div class="form-row">
            <div class="form-group full">
                <label>학력</label>
                <input type="text" name="education" value="${lawyer.education}"
                       placeholder="서울대학교 법학전문대학원 졸업" maxlength="500"/>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group full">
                <label>경력</label>
                <textarea name="career" placeholder="법무법인 대한 파트너 변호사 (2015-현재)&#10;서울중앙지방법원 국선변호인 (2012-2015)">${lawyer.career}</textarea>
            </div>
        </div>

        <!-- 소개 -->
        <div class="section-title-2">📝 소개</div>

        <div class="form-row">
            <div class="form-group full">
                <label>소개글</label>
                <textarea name="introduction" style="min-height:120px;"
                          placeholder="변호사 소개 내용을 입력하세요.">${lawyer.introduction}</textarea>
            </div>
        </div>

        <!-- 프로필 이미지 경로 -->
        <div class="form-row">
            <div class="form-group full">
                <label>프로필 이미지 경로</label>
                <input type="text" name="profileImage" value="${lawyer.profileImage}"
                       placeholder="/images/profile/lawyer_001.jpg" maxlength="300"/>
            </div>
        </div>

        <!-- 버튼 -->
        <div class="btn-area">
            <button id="reggo" type="submit" class="btn btn-primary">
                <c:choose>
                    <c:when test="${not empty lawyer.lawyerId}">✔ 수정 완료</c:when>
                    <c:otherwise>✔ 등록</c:otherwise>
                </c:choose>
            </button>
            <c:choose>
                <c:when test="${not empty lawyer.lawyerId}">
                    <a href="/lawyer/detail/${lawyer.lawyerId}" class="btn btn-secondary" style="text-decoration:none;">✖ 취소</a>
                </c:when>
                <c:otherwise>
                    <a href="/lawyer/list" class="btn btn-secondary" style="text-decoration:none;">✖ 취소</a>
                </c:otherwise>
            </c:choose>
        </div>

    </form>
</div>
</main>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script type="text/javascript">
    $(document).ready(function() {
        if ("${loginUser.role}" == "ROLE_LAWYER") {
            if ("${loginUser.email}" == "${lawyer.email}") {
                $("#reggo").show();
            } else {
                $("#reggo").hide();
            }
        }
    });
</script>
</html>
