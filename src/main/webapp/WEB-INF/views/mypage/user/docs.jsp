<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <style>
        body { background: #f4f6f9; }
        .mypage-container { max-width: 860px; margin: 40px auto; padding: 0 16px; }
        .tab-nav {
            display: flex; gap: 6px; margin-bottom: 24px;
            background: #fff; padding: 8px; border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.06);
        }
        .tab-nav a {
            flex: 1; padding: 10px 0; border-radius: 7px;
            font-size: 15px; font-weight: 500; color: #888;
            text-align: center; text-decoration: none; transition: all 0.2s;
        }
        .tab-nav a.active { background: #212529; color: #fff; font-weight: 700; }
        .tab-nav a:hover:not(.active) { background: #f0f0f0; color: #333; }
        .card-box {
            background: #fff; border-radius: 12px; padding: 32px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.07);
        }
        .section-title { font-size: 20px; font-weight: 700; margin-bottom: 24px; }
        .data-table { width: 100%; border-collapse: collapse; font-size: 14px; }
        .data-table th {
            background: #f4f6f9; padding: 10px 12px; text-align: center;
            font-weight: 600; color: #555; border-bottom: 2px solid #e0e0e0;
        }
        .data-table td { padding: 11px 12px; text-align: center; border-bottom: 1px solid #f0f0f0; }
        .data-table tr:hover td { background: #fafafa; }
        .btn-sm {
            padding: 4px 12px; font-size: 13px; border: 1px solid #212529;
            background: #fff; border-radius: 5px; cursor: pointer; text-decoration: none; color: #212529;
        }
        .btn-sm:hover { background: #212529; color: #fff; }
        .empty-msg { text-align: center; color: #aaa; padding: 40px 0; }
    </style>
</head>
<body>
<div class="mypage-container">
    <%-- 탭 영역: 기획안 1번 항목 반영 --%>
    <div class="tab-nav">
        <a href="${pageContext.request.contextPath}/mypage/my-page">마이페이지</a>
        <a href="${pageContext.request.contextPath}/mypage/my-page/consult">상담 내역</a>
        <a href="${pageContext.request.contextPath}/mypage/my-page/docs" class="active">문서</a>
        <a href="${pageContext.request.contextPath}/mypage/my-page/profile">관리</a>
    </div>

    <%-- 문서 이력 영역: 기획안 4번 항목 반영 --%>
    <div class="card-box">
        <div class="section-title">📄 문서 이력</div>
        <c:choose>
            <c:when test="${empty docList}">
                <div class="empty-msg">다운로드한 문서 내역이 없습니다.</div>
            </c:otherwise>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                    <tr>
                        <th style="width: 55%;">문서명</th>
                        <th style="width: 25%;">생성일</th>
                        <th style="width: 20%;">다운로드</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="doc" items="${docList}">
                        <tr>
                            <td>${doc.title}</td>
                                <%-- DB에서 가져온 Date 객체를 YYYY-MM-DD 형식으로 출력 --%>
                            <td><fmt:formatDate value="${doc.createdAt}" pattern="yyyy-MM-dd"/></td>
                            <td>
                                    <%-- 본인이 구현한 DocumentLoaderService.getFilePath() 호출용 URL --%>
                                <a href="${pageContext.request.contextPath}/docs/download?id=${doc.id}" class="btn-download">[다운로드]</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>