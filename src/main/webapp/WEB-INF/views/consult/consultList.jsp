<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<script type="text/javascript">
    $(document).ready(function(){
        $("#regBtn").click(function(){
            location.href="consultInsert"
        })
    });
    function goDetail(id){
        //alert(id)
        location.href="consultDetail?id="+id
    }
</script>

<main class="main" style="padding-top: 100px;">
<h1 style="text-align:center;">상담신청 목록</h1>
<br>
<div class="container">
    <form id="frm01" class="form"  method="post">
        <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
            <input placeholder="제목" name="schTitle"  value="${param.schTitle}" class="form-control mr-sm-3" />
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
        <c:forEach items="${consultList}" var="consult">
            <tr ondblclick="goDetail(${consult.id})"><td>${consult.id}</td><td>${consult.userId}</td>
                <td>${consult.title}</td><td>${consult.answered}</td>
                <td><fmt:formatDate value="${consult.createdAt}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</main>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</html>