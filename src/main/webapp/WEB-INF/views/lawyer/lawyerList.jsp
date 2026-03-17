<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    Object loginUser = session.getAttribute("loginUser");

    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return; // **중요: 코드 실행 중단**
    }
%>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<style>
    body { font-family: 'Nanum Gothic', sans-serif; margin: 20px; }
    h2 { border-bottom: 2px solid #1a3c6b; padding-bottom: 8px; color: #1a3c6b; }
    .search-area { display: flex; gap: 10px; margin-bottom: 16px; }
    .search-area input, .search-area select { padding: 7px 12px; border: 1px solid #ccc; border-radius: 4px; }
    .btn { padding: 7px 16px; border: none; border-radius: 4px; cursor: pointer; }
    .btn-primary { background: #1a3c6b; color: #fff; }
    .btn-success { background: #28a745; color: #fff; }
    .btn-danger  { background: #dc3545; color: #fff; }
    table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
    th { background: #1a3c6b; color: #fff; padding: 10px; text-align: center; }
    td { padding: 9px 10px; border-bottom: 1px solid #ddd; text-align: center; }
    tr:hover td { background: #f0f5ff; }
    .badge-active   { background: #28a745; color: #fff; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
    .badge-inactive { background: #aaa;    color: #fff; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
    .paging { text-align: center; margin-top: 10px; }
    .paging a { margin: 0 3px; padding: 5px 10px; border: 1px solid #ccc; border-radius: 3px; text-decoration: none; color: #333; }
    .paging a.active { background: #1a3c6b; color: #fff; border-color: #1a3c6b; }
    .total-count { float: right; color: #555; }
    .msg-success { color: green; padding: 8px; background: #d4edda; border-radius: 4px; margin-bottom: 10px; }
    .msg-error   { color: red;   padding: 8px; background: #f8d7da; border-radius: 4px; margin-bottom: 10px; }
</style>
<main class="main">
    <br><br>
<h2>⚖️ 변호사 프로필 목록</h2>

<c:if test="${not empty successMsg}">
    <div class="msg-success">${successMsg}</div>
</c:if>
<c:if test="${not empty errorMsg}">
    <div class="msg-error">${errorMsg}</div>
</c:if>

<!-- 검색 폼 -->
<form method="get" action="/lawyer/list">
    <div class="search-area">
        <input type="text" name="searchKeyword" value="${searchDTO.searchKeyword}"
               placeholder="이름 / 전문분야 / 경력 검색"/>
        <select name="status">
            <option value="">-- 상태 전체 --</option>
            <option value="ACTIVE"   <c:if test="${searchDTO.status == 'ACTIVE'}">selected</c:if>>활성</option>
            <option value="상담중" <c:if test="${searchDTO.status == '상담중'}">selected</c:if>>상담중</option>
            <option value="INACTIVE" <c:if test="${searchDTO.status == 'INACTIVE'}">selected</c:if>>비활성</option>
        </select>
        <input type="hidden" name="pageNo"   value="1"/>
        <input type="hidden" name="pageSize" value="${searchDTO.pageSize}"/>
        <button type="submit" class="btn btn-primary">🔍 검색</button>
        <a id="reggo" href="/lawyer/register" class="btn btn-success" style="text-decoration:none">+ 변호사 등록</a>
    </div>
</form>
<div class="total-count">총 <strong>${totalCount}</strong> 건</div>

<!-- 목록 테이블 -->
<table>
    <thead>
        <tr>
            <th>NO</th>
            <th>이름</th>
            <th>등록번호</th>
            <th>전문 분야</th>
            <th>이메일</th>
            <th>전화번호</th>
            <th>등록일</th>
            <th>상태</th>
            <th>관리</th>
        </tr>
    </thead>
    <tbody>
        <c:set var="restCnt" value="${searchDTO.pageSize}"/>
        <c:choose>
            <c:when test="${empty lawyerList}">
                <tr><td colspan="9">조회된  변호사가 없습니다.</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="lawyer" items="${lawyerList}" varStatus="st">
                    <c:set var="restCnt" value="${restCnt-1}"/>
                    <tr>
                        <td>${(searchDTO.pageNo - 1) * searchDTO.pageSize + st.index + 1}</td>
                        <td><a href="/lawyer/detail/${lawyer.lawyerId}">${lawyer.name}</a></td>
                        <td>${lawyer.barNumber}</td>
                        <td>${lawyer.specialty}</td>
                        <td>${lawyer.email}</td>
                        <td>${lawyer.phone}</td>
                        <td><fmt:formatDate value="${lawyer.admissionDate}" pattern="yyyy-MM-dd"/></td>
                        <td>

                            <c:choose>
                                <c:when test="${lawyer.status == 'ACTIVE'}">
                                    <span class="badge-active">활성</span>
                                </c:when>
                                <c:when test="${lawyer.status == '상담중'}">
                                    <span class="badge-active" style="background: #fd7e14;">상담중</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge-inactive">비활성</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                                <%-- 1. [상담 재개/신청 버튼] 일반 사용자(의뢰인) 전용 로직 --%>
                            <c:if test="${loginUser.role != 'ROLE_LAWYER' && loginUser.role != 'ROLE_ADMIN'}">
                                <c:choose>
                                    <%-- 이미 이 변호사와 생성된 방 ID가 있는 경우 (상태 불문 우선 표시) --%>
                                    <c:when test="${not empty lawyer.existingRoomId}">
                                        <a href="/direct/consult?roomId=${lawyer.existingRoomId}&lawyerId=${fn:split(lawyer.email, '@')[0]}"
                                           class="btn btn-primary" style="font-size:12px; background: #007bff;">상담 재개</a>
                                    </c:when>

                                    <%-- 방은 없는데 변호사가 현재 다른 사람과 상담 중인 경우 --%>
                                    <c:when test="${lawyer.status == '상담중'}">
                                        <button class="btn btn-secondary" style="font-size:12px; background:#6c757d; cursor:not-allowed;" disabled>상담 중</button>
                                    </c:when>

                                    <%-- 변호사가 활성 상태이고 생성된 방이 없는 경우 --%>
                                    <c:when test="${lawyer.status == 'ACTIVE'}">
                                        <a href="/consult/apply?lawyerId=${lawyer.lawyerId}" class="btn btn-success" style="font-size:12px;">상담 신청</a>
                                    </c:when>
                                </c:choose>
                            </c:if>

                                <%-- 2. [수정/삭제 버튼] 기존 관리자 및 변호사 본인 로직 --%>
                            <c:choose>
                                <c:when test="${loginUser.role eq 'ROLE_LAWYER'}">
                                    <c:if test="${loginUser.email eq lawyer.email}">
                                        <a href="/lawyer/modify/${lawyer.lawyerId}" class="btn btn-primary" style="font-size:12px;">수정</a>
                                        <c:if test="${lawyer.status == 'ACTIVE'}">
                                            <form method="post" action="/lawyer/delete/${lawyer.lawyerId}" style="display:inline" onsubmit="return confirm('삭제하시겠습니까?')">
                                                <button type="submit" class="btn btn-danger" style="font-size:12px;">삭제</button>
                                            </form>
                                        </c:if>
                                    </c:if>
                                </c:when>
                                <c:when test="${loginUser.role eq 'ROLE_ADMIN'}">
                                    <a href="/lawyer/modify/${lawyer.lawyerId}" class="btn btn-primary" style="font-size:12px;">수정</a>
                                    <c:if test="${lawyer.status == 'ACTIVE'}">
                                        <form method="post" action="/lawyer/delete/${lawyer.lawyerId}" style="display:inline" onsubmit="return confirm('삭제하시겠습니까?')">
                                            <button type="submit" class="btn btn-danger" style="font-size:12px;">삭제</button>
                                        </form>
                                    </c:if>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        <c:if test="${restCnt>0}">
            <c:forEach begin="1" end="${restCnt}">
                <tr ><td>&nbsp;</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
            </c:forEach>
        </c:if>
    </tbody>
</table>

<!-- 페이징 -->
<div class="paging">
    <c:if test="${searchDTO.pageNo > searchDTO.blockSize}">
        <a href="/lawyer/list?pageNo=1&pageSize=${searchDTO.pageSize}&searchKeyword=${searchDTO.searchKeyword}&status=${searchDTO.status}">처음</a>
        <a href="/lawyer/list?pageNo=${startPage - 1}&pageSize=${searchDTO.pageSize}&searchKeyword=${searchDTO.searchKeyword}&status=${searchDTO.status}">이전</a>
    </c:if>
    <c:forEach begin="${startPage}" end="${endPage}" var="i">
        <a href="/lawyer/list?pageNo=${i}&pageSize=${searchDTO.pageSize}&searchKeyword=${searchDTO.searchKeyword}&status=${searchDTO.status}"
           class="${i == searchDTO.pageNo ? 'active' : ''}">${i}</a>
    </c:forEach>
    <c:if test="${searchDTO.pageNo < last1Page}">
        <a href="/lawyer/list?pageNo=${endPage + 1}&pageSize=${searchDTO.pageSize}&searchKeyword=${searchDTO.searchKeyword}&status=${searchDTO.status}">다음</a>
        <a href="/lawyer/list?pageNo=${totalPages}&pageSize=${searchDTO.pageSize}&searchKeyword=${searchDTO.searchKeyword}&status=${searchDTO.status}">마지막</a>
    </c:if>
</div>
</main>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script type="text/javascript">
    $(document).ready(function() {
        if ("${loginUser.role}" == "ROLE_LAWYER") {
            $("#reggo").hide();
        } else {
            $("#reggo").show();
        }
    });
</script>
</html>
