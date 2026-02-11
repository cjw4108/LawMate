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

        /* 사용자 목록 */
        .user-list {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 12px;
            height: 320px;
            overflow-y: auto;
            background: #fff;
        }

        .user-item {
            padding: 12px 14px;
            border-radius: 6px;
            margin-bottom: 8px;
            border: 1px solid #e5e5e5;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .user-item:hover {
            background-color: #f1f3f5;
        }

        .user-item.active {
            background-color: #e9f0ff;
            border-left: 4px solid #0d6efd;
        }

        /* 상세 영역 */
        .detail-box {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 24px;
            background: #fff;
        }

        .btn-group-custom {
            display: flex;
            gap: 12px;
            margin-top: 30px;
        }
    </style>
</head>

<body>

<main class="main admin-wrapper">
    <section class="section">
        <div class="container">

            <h2 class="admin-title">관리자 변호사 승인</h2>
            <p class="text-center text-muted mb-4">(회원 검색 및 변호사 승인)</p>

            <!-- 필터 / 검색 -->
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
                        <div class="user-item active">
                            <div class="fw-bold">김민정</div>
                            <small class="text-muted">변호사 · 승인 대기</small>
                        </div>

                        <div class="user-item">
                            <div class="fw-bold">이철수</div>
                            <small class="text-muted">일반 · 활성</small>
                        </div>

                        <div class="user-item">
                            <div class="fw-bold">박영희</div>
                            <small class="text-muted">변호사 · 승인 대기</small>
                        </div>
                    </div>
                </div>

                <!-- 상세 / 승인 -->
                <div class="col-md-8">
                    <h5 class="mb-2">사용자 상세 / 승인</h5>
                    <div class="detail-box">

                        <div class="mb-3">
                            <label class="form-label">아이디</label>
                            <input type="text" class="form-control" value="lawyer_kmj" readonly>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">업로드 한 서류</label>
                            <input type="text" class="form-control" value="[ PDF / IMAGE ]" readonly>
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

<script>
    const items = document.querySelectorAll('.user-item');

    items.forEach(item => {
        item.addEventListener('click', () => {
            items.forEach(i => i.classList.remove('active'));
            item.classList.add('active');
        });
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>