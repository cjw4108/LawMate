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
        .sidebar { position: sticky; top: 24px; }
        .profile-card {
            background: linear-gradient(150deg, #1a1f36 0%, #2d3561 100%);
            border-radius: var(--radius); padding: 28px 24px;
            color: #fff; margin-bottom: 12px;
            position: relative; overflow: hidden;
        }
        .profile-card::before {
            content: ''; position: absolute; top: -30px; right: -30px;
            width: 120px; height: 120px; background: rgba(79,110,247,0.18); border-radius: 50%;
        }
        .profile-avatar {
            width: 52px; height: 52px; background: rgba(255,255,255,0.12);
            border-radius: 50%; display: flex; align-items: center; justify-content: center;
            font-size: 24px; margin-bottom: 12px; border: 2px solid rgba(255,255,255,0.2);
        }
        .profile-name { font-size: 16px; font-weight: 700; margin-bottom: 3px; }
        .profile-email { font-size: 12px; color: rgba(255,255,255,0.55); word-break: break-all; }
        .badge-row { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 12px; }
        .badge { font-size: 11px; font-weight: 600; padding: 3px 10px; border-radius: 20px; }
        .badge-user { background: rgba(79,110,247,0.3); border: 1px solid rgba(79,110,247,0.5); color: #a5b4fc; }

        .side-menu {
            background: var(--card); border-radius: var(--radius);
            border: 1px solid var(--border); overflow: hidden;
            box-shadow: 0 1px 4px rgba(0,0,0,0.05);
        }
        .side-menu-item {
            display: flex; align-items: center; gap: 10px;
            padding: 14px 18px; font-size: 14px; font-weight: 500; color: var(--muted);
            cursor: pointer; border: none; background: transparent;
            width: 100%; text-align: left; border-bottom: 1px solid var(--border);
            transition: all 0.18s; font-family: 'Noto Sans KR', sans-serif;
        }
        .side-menu-item:last-child { border-bottom: none; }
        .side-menu-item:hover { background: var(--bg); color: var(--text); }
        .side-menu-item.active {
            background: var(--accent-light); color: var(--accent);
            font-weight: 700; border-left: 3px solid var(--accent);
        }
        .menu-icon { font-size: 16px; flex-shrink: 0; }

        /* ── 컨텐츠 ── */
        .tab-content { display: none; }
        .tab-content.active { display: block; animation: fadeUp 0.3s ease both; }
        @keyframes fadeUp {
            from { opacity: 0; transform: translateY(10px); }
            to   { opacity: 1; transform: translateY(0); }
        }

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

        /* 통계 */
        .stat-card {
            background: var(--accent-light); border-radius: 12px; padding: 20px 24px;
            display: flex; align-items: center; gap: 16px;
        }
        .stat-icon {
            width: 48px; height: 48px; background: var(--accent); border-radius: 12px;
            display: flex; align-items: center; justify-content: center; font-size: 22px; flex-shrink: 0;
        }
        .stat-num { font-size: 30px; font-weight: 700; color: var(--accent); line-height: 1; }
        .stat-label { font-size: 13px; color: var(--muted); margin-top: 4px; }

        /* 테이블 */
        .data-table { width: 100%; border-collapse: collapse; font-size: 14px; }
        .data-table th {
            background: #f8f9ff; padding: 10px 12px; text-align: center;
            font-weight: 600; color: var(--muted); border-bottom: 2px solid var(--border);
        }
        .data-table td { padding: 12px; text-align: center; border-bottom: 1px solid var(--border); }
        .data-table tr:last-child td { border-bottom: none; }
        .data-table tr:hover td { background: #fafbff; }
        .empty-msg { text-align: center; color: var(--muted); padding: 40px 0; font-size: 14px; }

        .status-done { background: #e8f5e9; color: #2e7d32; padding: 3px 10px; border-radius: 20px; font-size: 12px; font-weight: 600; }
        .status-ing  { background: #fff3e0; color: #e65100; padding: 3px 10px; border-radius: 20px; font-size: 12px; font-weight: 600; }

        .btn-sm {
            padding: 5px 12px; font-size: 12px; border: 1.5px solid var(--accent);
            background: #fff; color: var(--accent); border-radius: 6px;
            cursor: pointer; text-decoration: none; transition: all 0.2s;
        }
        .btn-sm:hover { background: var(--accent); color: #fff; }

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

        /* 버튼 */
        .btn-primary {
            display: block; width: 100%; padding: 12px;
            background: var(--accent); color: #fff; border: none; border-radius: 10px;
            font-size: 14px; font-weight: 600; cursor: pointer; text-align: center;
            text-decoration: none; margin-top: 14px; transition: all 0.2s;
            box-shadow: 0 2px 8px rgba(79,110,247,0.25); font-family: 'Noto Sans KR', sans-serif;
        }
        .btn-primary:hover { background: #3d5ce5; transform: translateY(-1px); }
        .btn-outline {
            display: block; width: 100%; padding: 12px;
            background: #fff; color: var(--accent); border: 1.5px solid var(--accent);
            border-radius: 10px; font-size: 14px; font-weight: 600;
            cursor: pointer; text-align: center; text-decoration: none;
            margin-top: 10px; transition: all 0.2s; font-family: 'Noto Sans KR', sans-serif;
        }
        .btn-outline:hover { background: var(--accent-light); }

        @media (max-width: 640px) {
            .layout { grid-template-columns: 1fr; }
            .sidebar { position: static; }
        }
    </style>
</head>
<body>
<div class="layout">
    <aside class="sidebar">
        <div class="profile-card">
            <div class="profile-avatar">🙍</div>
            <div class="profile-name">${not empty user.userName ? user.userName : user.userId} 님</div>
            <div class="profile-email">${user.email}</div>
        </div>
        <nav class="side-menu">
            <button class="side-menu-item active" onclick="switchTab('tab-main', this)">🏠 마이페이지</button>
            <button class="side-menu-item" onclick="switchTab('tab-consult', this)">💬 상담 내역</button>
            <button class="side-menu-item" onclick="switchTab('tab-docs', this)">📄 문서</button>
            <button class="side-menu-item" onclick="switchTab('tab-profile', this)">✏️ 프로필 수정</button>
        </nav>
    </aside>

    <main>
        <div id="tab-main" class="tab-content active">
            <div class="card">
                <h3>기본 정보</h3>
                <p>아이디: ${user.userId}</p>
                <p>이메일: ${user.email}</p>
                <p>가입일: ${user.joinDate}</p>
            </div>
        </div>

        <div id="tab-consult" class="tab-content">
            <div class="card">
                <c:choose>
                    <c:when test="${empty consultList}">
                        <p>상담 내역이 없습니다.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="data-table">
                            <thead><tr><th>번호</th><th>유형</th><th>날짜</th><th>상태</th></tr></thead>
                            <tbody>
                            <c:forEach var="c" items="${consultList}">
                                <tr>
                                    <td>${c.consultNo}</td>
                                    <td>${c.consultType}</td>
                                    <td>${c.consultDate}</td>
                                    <td><span class="${c.status == '완료' ? 'status-done' : 'status-ing'}">${c.status}</span></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div id="tab-docs" class="tab-content">
            <div class="card">
                <h3>문서 다운로드 이력</h3>
                <c:choose>
                    <c:when test="${empty docList}">
                        <p>다운로드한 문서가 없습니다.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="data-table">
                            <thead><tr><th>문서명</th><th>생성일</th><th>관리</th></tr></thead>
                            <tbody>
                            <c:forEach var="doc" items="${docList}">
                                <tr>
                                    <td>${doc.title}</td> <%-- docName -> title 로 수정 --%>
                                    <td>${doc.createdAt}</td>
                                    <td><a href="${pageContext.request.contextPath}/docs/download/${doc.id}" class="btn-sm">다운로드</a></td> <%-- docId -> id 로 수정 --%>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
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
