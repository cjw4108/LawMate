<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        :root {
            --accent: #4f6ef7;
            --accent-light: #eef0fe;
            --bg: #f5f7ff;
            --card: #ffffff;
            --border: #e8eaf0;
            --text: #1a1f36;
            --muted: #8b92a9;
            --radius: 16px;
            --green-light: #ecfdf5;
            --orange-light: #fffbeb;
            --red-light: #fef2f2;
        }
        body { font-family: 'Noto Sans KR', sans-serif; background: var(--bg); color: var(--text); }

        /* ── 전체 레이아웃 ── */
        .layout {
            max-width: 980px;
            margin: 0 auto;
            padding: 80px 20px 80px;
            display: grid;
            grid-template-columns: 240px 1fr;
            gap: 24px;
            align-items: start;
        }

        /* ── 사이드바 ── */
        .sidebar {
            position: sticky;
            top: 24px;
        }
        .profile-card {
            background: linear-gradient(150deg, #1a2a4a 0%, #2a3f6f 100%);
            border-radius: var(--radius);
            padding: 28px 24px;
            color: #fff;
            margin-bottom: 12px;
            position: relative;
            overflow: hidden;
        }
        .profile-card::before {
            content: ''; position: absolute; top: -30px; right: -30px;
            width: 120px; height: 120px; background: rgba(79,110,247,0.18); border-radius: 50%;
        }
        .profile-avatar {
            width: 52px; height: 52px;
            background: rgba(255,255,255,0.12);
            border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            font-size: 24px; margin-bottom: 12px;
            border: 2px solid rgba(255,255,255,0.2);
        }
        .profile-name { font-size: 16px; font-weight: 700; margin-bottom: 3px; }
        .profile-email { font-size: 12px; color: rgba(255,255,255,0.55); word-break: break-all; }
        .badge-row { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 12px; }
        .badge {
            font-size: 11px; font-weight: 600; padding: 3px 10px; border-radius: 20px;
        }
        .badge-lawyer   { background: rgba(79,110,247,0.3); border: 1px solid rgba(79,110,247,0.5); color: #a5b4fc; }
        .badge-approved { background: rgba(16,185,129,0.3); border: 1px solid rgba(16,185,129,0.5); color: #6ee7b7; }
        .badge-pending  { background: rgba(245,158,11,0.3); border: 1px solid rgba(245,158,11,0.5); color: #fcd34d; }
        .badge-rejected { background: rgba(239,68,68,0.3);  border: 1px solid rgba(239,68,68,0.5);  color: #fca5a5; }

        /* 사이드 탭 메뉴 */
        .side-menu {
            background: var(--card);
            border-radius: var(--radius);
            border: 1px solid var(--border);
            overflow: hidden;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
        }
        .side-menu-item {
            display: flex; align-items: center; gap: 10px;
            padding: 14px 18px;
            font-size: 14px; font-weight: 500; color: var(--muted);
            cursor: pointer; border: none; background: transparent;
            width: 100%; text-align: left;
            border-bottom: 1px solid var(--border);
            transition: all 0.18s;
            font-family: 'Noto Sans KR', sans-serif;
        }
        .side-menu-item:last-child { border-bottom: none; }
        .side-menu-item:hover { background: var(--bg); color: var(--text); }
        .side-menu-item.active {
            background: var(--accent-light);
            color: var(--accent);
            font-weight: 700;
            border-left: 3px solid var(--accent);
        }
        .side-menu-item .menu-icon { font-size: 16px; flex-shrink: 0; }

        /* ── 메인 컨텐츠 ── */
        .main-content {}
        .tab-content { display: none; }
        .tab-content.active { display: block; animation: fadeUp 0.3s ease both; }
        @keyframes fadeUp {
            from { opacity: 0; transform: translateY(10px); }
            to   { opacity: 1; transform: translateY(0); }
        }

        /* 카드 */
        .card {
            background: var(--card); border-radius: var(--radius); padding: 28px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.04), 0 4px 16px rgba(0,0,0,0.04);
            margin-bottom: 16px; border: 1px solid var(--border);
        }
        .card-title {
            font-size: 15px; font-weight: 700; color: var(--text);
            margin-bottom: 20px; padding-bottom: 14px; border-bottom: 1px solid var(--border);
            display: flex; align-items: center; gap: 8px;
        }
        .card-title-icon {
            width: 30px; height: 30px; background: var(--accent-light);
            border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 15px;
        }

        /* 정보 행 */
        .info-row { display: flex; align-items: center; padding: 12px 0; border-bottom: 1px solid var(--border); }
        .info-row:last-of-type { border-bottom: none; }
        .info-label { width: 80px; font-size: 13px; color: var(--muted); font-weight: 500; flex-shrink: 0; }
        .info-value { font-size: 14px; font-weight: 500; color: var(--text); }
        .info-value.empty { color: var(--muted); font-style: italic; }

        /* 상태 배너 */
        .status-banner {
            border-radius: 10px; padding: 14px 18px;
            display: flex; align-items: center; gap: 12px; margin-bottom: 16px;
        }
        .status-banner.pending  { background: var(--orange-light); border: 1px solid #fde68a; }
        .status-banner.approved { background: var(--green-light);  border: 1px solid #a7f3d0; }
        .status-banner.rejected { background: var(--red-light);    border: 1px solid #fecaca; }
        .status-banner.none     { background: #f9fafb;             border: 1px solid #e5e7eb; }
        .status-icon { font-size: 22px; flex-shrink: 0; }
        .status-text strong { display: block; font-size: 14px; font-weight: 700; margin-bottom: 2px; }
        .status-text span   { font-size: 13px; }
        .status-banner.pending  .status-text strong,
        .status-banner.pending  .status-text span   { color: #92400e; }
        .status-banner.approved .status-text strong,
        .status-banner.approved .status-text span   { color: #065f46; }
        .status-banner.rejected .status-text strong,
        .status-banner.rejected .status-text span   { color: #991b1b; }
        .status-banner.none     .status-text strong,
        .status-banner.none     .status-text span   { color: #374151; }

        /* 신청 횟수 바 */
        .apply-bar-wrap { margin-bottom: 16px; }
        .apply-bar-label { display: flex; justify-content: space-between; font-size: 13px; color: var(--muted); margin-bottom: 6px; }
        .apply-bar-label strong { color: var(--text); }
        .apply-bar-bg { background: var(--border); border-radius: 99px; height: 7px; overflow: hidden; }
        .apply-bar-fill { height: 100%; border-radius: 99px; background: var(--accent); }

        /* 폼 */
        .form-group { margin-bottom: 14px; }
        .form-group label { display: block; font-size: 13px; font-weight: 600; color: var(--muted); margin-bottom: 5px; }
        .form-group input {
            width: 100%; padding: 10px 14px; border: 1px solid var(--border);
            border-radius: 8px; font-size: 14px; background: #f9f9f9;
            font-family: 'Noto Sans KR', sans-serif; transition: border 0.2s;
        }
        .form-group input:focus { outline: none; border-color: var(--accent); background: #fff; }
        .form-group input:disabled { background: #f3f4f6; color: var(--muted); cursor: not-allowed; }

        /* 테이블 */
        .data-table { width: 100%; border-collapse: collapse; font-size: 14px; }
        .data-table th { background: #f8f9ff; padding: 10px 12px; text-align: center; font-weight: 600; color: var(--muted); border-bottom: 2px solid var(--border); }
        .data-table td { padding: 12px; text-align: center; border-bottom: 1px solid var(--border); }
        .data-table tr:last-child td { border-bottom: none; }
        .data-table tr:hover td { background: #fafbff; }
        .empty-msg { text-align: center; color: var(--muted); padding: 40px 0; font-size: 14px; }
        .btn-sm {
            padding: 5px 12px; font-size: 12px; border: 1.5px solid var(--accent);
            background: #fff; color: var(--accent); border-radius: 6px; cursor: pointer; text-decoration: none; transition: all 0.2s;
        }
        .btn-sm:hover { background: var(--accent); color: #fff; }

        /* 버튼 */
        .btn-primary {
            display: block; width: 100%; padding: 12px;
            background: var(--accent); color: #fff; border: none; border-radius: 10px;
            font-size: 14px; font-weight: 600; cursor: pointer; text-align: center;
            text-decoration: none; margin-top: 14px; transition: all 0.2s;
            box-shadow: 0 2px 8px rgba(79,110,247,0.25); font-family: 'Noto Sans KR', sans-serif;
        }
        .btn-primary:hover { background: #3d5ce5; transform: translateY(-1px); }
        .btn-disabled {
            display: block; width: 100%; padding: 12px;
            background: #e5e7eb; color: #9ca3af; border: none; border-radius: 10px;
            font-size: 14px; font-weight: 600; text-align: center; margin-top: 14px; cursor: not-allowed;
        }

        /* 두 컬럼 */
        .two-col { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        @media (max-width: 640px) {
            .layout { grid-template-columns: 1fr; }
            .sidebar { position: static; }
            .two-col { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
<div class="layout">

    <!-- ── 사이드바 ── -->
    <aside class="sidebar">
        <!-- 프로필 카드 -->
        <div class="profile-card">
            <div class="profile-avatar">⚖️</div>
            <div class="profile-name">${not empty user.userName ? user.userName : user.userId} 변호사님</div>
            <div class="profile-email">${user.email}</div>
            <div class="badge-row">
                <span class="badge badge-lawyer">변호사</span>
                <c:choose>
                    <c:when test="${user.lawyerStatus == 'APPROVED'}"><span class="badge badge-approved">✓ 승인</span></c:when>
                    <c:when test="${user.lawyerStatus == 'PENDING'}"><span class="badge badge-pending">⏳ 심사중</span></c:when>
                    <c:when test="${user.lawyerStatus == 'REJECTED'}"><span class="badge badge-rejected">✗ 반려</span></c:when>
                    <c:otherwise><span class="badge badge-pending">미신청</span></c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- 사이드 메뉴 -->
        <nav class="side-menu">
            <button class="side-menu-item active" onclick="switchTab('tab-main', this)">
                <span class="menu-icon">🏠</span> 마이페이지
            </button>
            <button class="side-menu-item" onclick="switchTab('tab-consult', this)">
                <span class="menu-icon">💬</span> 상담 내역
            </button>
            <button class="side-menu-item" onclick="switchTab('tab-profile', this)">
                <span class="menu-icon">✏️</span> 프로필/자격신청
            </button>
        </nav>
    </aside>

    <!-- ── 메인 컨텐츠 ── -->
    <main class="main-content">

        <!-- ① 마이페이지 -->
        <div id="tab-main" class="tab-content active">
            <div class="card">
                <div class="card-title"><div class="card-title-icon">👤</div> 기본 정보</div>
                <div class="info-row"><span class="info-label">아이디</span><span class="info-value">${user.userId}</span></div>
                <div class="info-row">
                    <span class="info-label">이름</span>
                    <span class="info-value ${empty user.userName ? 'empty' : ''}">${not empty user.userName ? user.userName : '미입력'}</span>
                </div>
                <div class="info-row"><span class="info-label">이메일</span><span class="info-value">${user.email}</span></div>
                <div class="info-row"><span class="info-label">가입일</span><span class="info-value">${user.joinDate}</span></div>
                <button class="btn-primary" onclick="switchTab('tab-profile', document.querySelectorAll('.side-menu-item')[2])">정보 수정</button>
            </div>

            <div class="card">
                <div class="card-title"><div class="card-title-icon">🔖</div> 변호사 권한 승인 상태</div>
                <c:choose>
                    <c:when test="${user.lawyerStatus == 'APPROVED'}">
                        <div class="status-banner approved"><span class="status-icon">✅</span><div class="status-text"><strong>승인 완료</strong><span>변호사 자격이 확인되었습니다.</span></div></div>
                    </c:when>
                    <c:when test="${user.lawyerStatus == 'PENDING'}">
                        <div class="status-banner pending"><span class="status-icon">⏳</span><div class="status-text"><strong>심사 중</strong><span>자격 서류 검토 중입니다. 승인 후 서비스 이용 가능합니다.</span></div></div>
                    </c:when>
                    <c:when test="${user.lawyerStatus == 'REJECTED'}">
                        <div class="status-banner rejected"><span class="status-icon">❌</span><div class="status-text"><strong>반려됨</strong><span>사유: ${not empty user.rejectReason ? user.rejectReason : '관리자에게 문의하세요.'}</span></div></div>
                    </c:when>
                    <c:otherwise>
                        <div class="status-banner none"><span class="status-icon">📋</span><div class="status-text"><strong>미신청</strong><span>자격 증빙 서류를 제출하여 변호사 권한을 신청하세요.</span></div></div>
                    </c:otherwise>
                </c:choose>
                <div class="apply-bar-wrap">
                    <div class="apply-bar-label"><span>신청 횟수</span><strong>${user.applyCount} / 3</strong></div>
                    <div class="apply-bar-bg"><div class="apply-bar-fill" style="width:${user.applyCount * 33}%"></div></div>
                </div>
                <c:choose>
                    <c:when test="${user.lawyerStatus == 'APPROVED'}"><span class="btn-disabled">이미 승인된 상태입니다</span></c:when>
                    <c:when test="${user.applyCount >= 3}"><span class="btn-disabled">신청 횟수(3회) 초과 — 관리자 문의</span></c:when>
                    <c:otherwise>
                        <button class="btn-primary" onclick="switchTab('tab-profile', document.querySelectorAll('.side-menu-item')[2])">
                                ${user.lawyerStatus == 'REJECTED' ? '재신청하러 가기 →' : '자격 신청하러 가기 →'}
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- ② 상담 내역 -->
        <div id="tab-consult" class="tab-content">
            <div class="card">
                <div class="card-title"><div class="card-title-icon">💬</div> 상담 내역</div>
                <c:choose>
                    <c:when test="${empty consultList}">
                        <p class="empty-msg">배정된 상담이 없습니다.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="data-table">
                            <thead><tr><th>상담번호</th><th>의뢰인</th><th>상태</th><th>상세</th></tr></thead>
                            <tbody>
                            <c:forEach var="c" items="${consultList}">
                                <tr>
                                    <td>${c.consultNo}</td>
                                    <td>${c.clientName}</td>
                                    <td>${c.status}</td>
                                    <td><a href="${pageContext.request.contextPath}/consult/${c.consultNo}" class="btn-sm">보기</a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- ③ 프로필/자격신청 -->
        <div id="tab-profile" class="tab-content">
            <div class="two-col">
                <div class="card">
                    <div class="card-title"><div class="card-title-icon">✏️</div> 프로필 수정</div>
                    <form action="${pageContext.request.contextPath}/mypage/lawyer/profile/update" method="post">
                        <div class="form-group">
                            <label>아이디 (수정 불가)</label>
                            <input type="text" value="${user.userId}" disabled>
                        </div>
                        <div class="form-group">
                            <label>이름</label>
                            <input type="text" name="userName" value="${user.userName}" placeholder="이름 입력">
                        </div>
                        <div class="form-group">
                            <label>이메일</label>
                            <input type="email" name="email" value="${user.email}">
                        </div>
                        <div class="form-group">
                            <label>가입일 (수정 불가)</label>
                            <input type="text" value="${user.joinDate}" disabled>
                        </div>
                        <button type="submit" class="btn-primary">수정 저장</button>
                    </form>
                </div>

                <div class="card">
                    <div class="card-title"><div class="card-title-icon">📋</div> 자격 신청/재신청</div>
                    <div class="form-group">
                        <label>신청 횟수</label>
                        <input type="text" value="${user.applyCount} / 3" disabled>
                    </div>
                    <c:if test="${not empty user.rejectReason}">
                        <div class="form-group">
                            <label>반려 사유</label>
                            <input type="text" value="${user.rejectReason}" disabled>
                        </div>
                    </c:if>
                    <c:choose>
                        <c:when test="${user.lawyerStatus == 'APPROVED'}">
                            <span class="btn-disabled">이미 승인된 상태입니다</span>
                        </c:when>
                        <c:when test="${user.applyCount >= 3}">
                            <span class="btn-disabled">신청 횟수 초과</span>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/mypage/lawyer/cert/apply" method="post" enctype="multipart/form-data">
                                <div class="form-group" style="margin-top:10px;">
                                    <label>자격 증빙 파일 <span style="color:red">*</span></label>
                                    <input type="file" name="licenseFile" accept=".pdf,image/*" required>
                                </div>
                                <button type="submit" class="btn-primary">제출</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

    </main>
</div>

<script>
    function switchTab(tabId, btn) {
        document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
        document.querySelectorAll('.side-menu-item').forEach(el => el.classList.remove('active'));
        document.getElementById(tabId).classList.add('active');
        btn.classList.add('active');
    }
</script>
</body>
</html>
