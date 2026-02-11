<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container">
        <div class="row mb-4 align-items-center">
            <div class="col-md-3">
                <select class="form-select border-2">
                    <option selected>필터</option>
                    <option>전체</option>
                    <option>정지</option>
                    <option>정상</option>
                </select>
            </div>
            <div class="col-md-6 offset-md-3">
                <div class="input-group shadow-sm rounded-pill border overflow-hidden">
                    <input type="text" class="form-control border-0 px-4" placeholder="검색어를 입력해주세요.">
                    <button class="btn btn-white border-0 py-2 px-3"><i class="bi bi-search"></i></button>
                </div>
            </div>
        </div>

        <div class="table-responsive bg-white shadow-sm rounded border">
            <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                <tr>
                    <th>유저 ID</th>
                    <th>유형</th>
                    <th>상태</th>
                    <th>조치</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>일반</td>
                    <td>정상</td>
                    <td><button class="btn btn-sm btn-outline-dark">[제한]</button></td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>변호사</td>
                    <td>정지(3개월)</td>
                    <td><button class="btn btn-sm btn-outline-primary">[해제]</button></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />