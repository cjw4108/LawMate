<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<br><br><br><br>
<div class="container">
    <form id="frm01" class="form"  method="post">
        <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
            <input placeholder="제목" name="title"  value="${param.title}" class="form-control mr-sm-2" />
            <button class="btn btn-info" type="submit">Search</button>
            <button class="btn btn-success" id="regBtn" type="button">등록</button>
        </nav>
    </form>
    <table class="table table-hover table-striped">
        <col width="20%">
        <col width="20%">
        <col width="20%">
        <col width="20%">
        <col width="20%">
        <thead>
        <tr class="table-success text-center">
            <th>ID</th>
            <th>계정</th>
            <th>제목</th>
            <th>답변여부</th>
            <th>생성일자</th>
        </tr>
        </thead>
        <tbody>
        <%-- // groupNo groupName debutDate fandomName --%>
        <c:forEach var="grp" items="${consultList}">
            <tr ondblclick="goDetail(${consult.id})"><td>${consult.id}</td><td>${consult.user_id}</td>
                <td>${consult.title}</td><td>${consult.answered}</td>
            <td><fmt:formatDate value="${consult.created_at}"/></td></tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>