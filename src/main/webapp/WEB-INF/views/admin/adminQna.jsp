<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main" style="padding-top: 100px;">
    <div class="container">
        <div class="row mb-4 align-items-center">
            <div class="col-md-3">
                <select class="form-select border-2">
                    <option selected>필터</option>
                    <option>전체</option>
                    <option>미답변</option>
                    <option>신고</option>
                    <option>숨긴 항목</option>
                </select>
            </div>
            <div class="col-md-6 offset-md-3 text-end">
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
                    <th style="width: 15%;">게시글 ID</th>
                    <th style="width: 20%;">상태</th>
                    <th style="width: 20%;">신고 횟수</th>
                    <th>관리</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>정상</td>
                    <td>-</td>
                    <td>
                        <small class="text-muted">보기 | 숨김/복구 | 삭제</small>
                    </td>
                </tr>
                <tr>
                    <td>2</td>
                    <td><span class="text-danger">신고</span></td>
                    <td>1</td>
                    <td>
                        <small class="text-muted">보기 | 숨김/복구 | 삭제</small>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />