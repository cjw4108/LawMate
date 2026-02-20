<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <style>
        .admin-wrapper {
            padding: 60px 0;
        }

        .admin-title {
            font-weight: 700;
            text-align: center;
            margin-bottom: 40px;
        }

        .filter-box {
            display: flex;
            gap: 10px;
            margin-bottom: 30px;
        }

        .user-list {
            border: 1px solid #ddd;
            border-radius: 6px;
            padding: 15px;
            height: 300px;
            overflow-y: auto;
        }

        .user-item {
            padding: 10px;
            border-bottom: 1px solid #eee;
            cursor: pointer;
        }

        .user-item:last-child {
            border-bottom: none;
        }

        .user-item:hover {
            background-color: #f8f9fa;
        }

        .detail-box {
            border: 1px solid #ddd;
            border-radius: 6px;
            padding: 20px;
        }

        .btn-group-custom {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
    </style>
</head>

<body>

<main class="main admin-wrapper">
    <section class="section">
        <div class="container">

            <h2 class="admin-title">관리자 변호사 승인</h2>
            <p class="text-center text-muted mb-4">(회원 검색 및 변호사 승인)</p>

            <!-- 검색 / 필터 -->
            <div class="filter-box">
                <select class="form-select w-auto">
                    <option>회원유형</option>
                    <option>일반</option>
                    <option>변호사</option>
                </select>

                <select class="form-select w-auto">
                    <option>승인상태</option>
                    <option>승인 대기</option>
                    <option>승인 완료</option>
                </select>

                <input type="text" class="form-control" placeholder="검색어를 입력해주세요.">
                <button class="btn btn-secondary">검색</button>
            </div>

            <div class="row">
                <!-- 사용자 목록 -->
                <div class="col-md-4">
                    <h5 class="mb-2">사용자 목록</h5>
                    <div class="user-list">
                        <div class="user-item">이름 | 유형 | 상태</div>
                        <div class="user-item">이철수 | 변호사 | 대기</div>
                        <div class="user-item">박영희 | 일반 | 활성</div>
                    </div>
                </div>

                <!-- 상세 / 승인 -->
                <div class="col-md-8">
                    <h5 class="mb-2">사용자 상세 / 승인</h5>
                    <div class="detail-box">

                        <div class="mb-3">
                            <label class="form-label">아이디</label>
                            <input type="text" class="form-control" placeholder="아이디" readonly>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">업로드 한 서류</label>
                            <input type="text" class="form-control" placeholder="[ PDF / IMAGE ]" readonly>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">승인 상태</label>
                            <input type="text" class="form-control" value="승인 대기" readonly>
                        </div>

                        <div class="btn-group-custom">
                            <button class="btn btn-dark w-50">승인</button>
                            <button class="btn btn-outline-secondary w-50">반려</button>
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>