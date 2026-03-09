<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LawMate - MyPage</title>

    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <link rel="stylesheet" href="/js/vue-app/index.css">

    <style>
        /* 1. ⭐ 아이콘 폰트 스타일 (fontstyle) 복구 */
        @font-face {
            font-family: 'nucleo-icons';
            /* image_2ecd54.png에서 확인된 경로와 파일명입니다 */
            src: url('/js/vue-app/nucleo-icons.woff2') format('woff2'),
            url('/js/vue-app/nucleo-icons.woff') format('woff'),
            url('/js/vue-app/nucleo-icons.ttf') format('truetype');
            font-weight: normal;
            font-style: normal;
        }

        /* 2. 닉네임 버튼 스타일: 평소에도 파란 배경에 흰 글씨 고정 */
        .header .btn-primary,
        .navbar .dropdown-toggle {
            background-color: #007bff !important; /* LawMate 공식 파란색 */
            color: #ffffff !important;           /* 흰색 글자 강제 */
            border: 1px solid #007bff !important;
            visibility: visible !important;
            opacity: 1 !important;
            display: inline-flex !important;
            align-items: center;
            padding: 8px 20px !important;
            border-radius: 25px !important;
        }

        /* 3. 드롭다운 메뉴 레이어 및 위치 설정 */
        .header .dropdown-menu {
            z-index: 10000 !important;
            display: none; /* 기본 숨김 */
            background-color: white !important;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15) !important;
            right: 0 !important; /* 오른쪽 정렬 */
            left: auto !important;
        }

        /* 클릭 시 'show' 클래스가 붙으면 강제로 표시 */
        .header .dropdown.show .dropdown-menu,
        .header .dropdown-menu.show {
            display: block !important;
        }

        /* 4. 레이아웃 간섭 방지 */
        #app { margin-top: 80px !important; z-index: 1 !important; position: relative; }
        .sidebar { top: 80px !important; z-index: 1000 !important; }
    </style>
</head>
<body>
<script>
    // Vue 앱에서 사용할 유저 데이터
    window.LAW_MATE_USER = {
        userId: "${loginUser.userId}",
        name: "${loginUser.name}",
        role: "${loginUser.role}"
    };
</script>

<div id="app"></div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>

<script>
    $(document).ready(function() {
        // ⭐ 드롭다운 강제 실행 (BS4/BS5 속성 모두 대응)
        $(document).on('click', '[data-toggle="dropdown"], [data-bs-toggle="dropdown"]', function(e) {
            e.preventDefault();
            e.stopPropagation();

            var $parent = $(this).closest('.dropdown');
            var isShown = $parent.hasClass('show');

            $('.dropdown').removeClass('show'); // 다른 거 닫기
            if (!isShown) {
                $parent.addClass('show');
                $(this).next('.dropdown-menu').addClass('show');
            }
        });
    });
</script>

<script type="module" src="/js/vue-app/main.js"></script>
</body>
</html>