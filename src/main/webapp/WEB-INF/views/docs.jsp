<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="/css/liveSearch.css">
    <style>
        .quick-form-card { display: block; background: white; border-radius: 16px; padding: 1.75rem; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05); transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); border: 2px solid transparent; height: 100%; }
        .quick-form-card:hover { transform: translateY(-8px); box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12); }
        .quick-form-icon { width: 64px; height: 64px; border-radius: 14px; display: flex; align-items: center; justify-content: center; margin-bottom: 1.25rem; box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15); }
        .quick-form-content h4 { transition: color 0.2s; }
        .quick-form-card:hover .quick-form-content h4 { color: #3b82f6; }
        .card-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; margin-bottom: 40px; padding: 20px 0; }
        .card-grid .card { background: white; border: 1px solid #e5e7eb; border-radius: 12px; padding: 28px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); transition: all 0.3s ease; cursor: pointer; text-decoration: none; color: inherit; display: block; }
        .card-grid .card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(59, 130, 246, 0.15); border-color: #3b82f6; }
        .card-grid .card .badge { display: inline-block; padding: 6px 14px; border-radius: 20px; font-size: 12px; font-weight: 500; margin-bottom: 16px; }
        .card-grid .card-title { font-size: 18px; font-weight: 600; color: #1e293b; margin-bottom: 12px; margin-top: 0; }
        .card-grid .card-info { font-size: 13px; color: #64748b; line-height: 1.7; margin-bottom: 24px; }
        .card-grid .card-button { width: 100%; background-color: #3b82f6; color: white; border: none; padding: 12px 16px; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.2s; }
        #pagination { display: flex; justify-content: center; align-items: center; gap: 10px; margin-top: 40px; padding-bottom: 40px; }
        #pagination .pagination-btn { width: 40px; height: 40px; border: 1px solid #e5e7eb; background: white; border-radius: 8px; cursor: pointer; font-size: 15px; color: #475569; display: flex; align-items: center; justify-content: center; font-weight: 500; }
        #pagination .pagination-btn.active { background-color: #3b82f6; color: white; border-color: #3b82f6; box-shadow: 0 2px 8px rgba(59, 130, 246, 0.25); }
        .dropdown-menu { z-index: 99999 !important; position: absolute !important; top: 100% !important; left: 0 !important; min-width: 100% !important; }
        #categoryMenu.show { display: block !important; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<main class="main">
    <section id="hero" class="hero section" style="padding-bottom: 0 !important; overflow: visible;">
        <div class="container" style="overflow: visible;">
            <div class="row align-items-center">
                <div class="col-lg-6 offset-lg-1">
                    <div class="hero-content">
                        <h1 class="mb-4 text-left fw-bold">서류 양식</h1>
                        <div class="company-badge mb-4"><i class="bi bi-gear-fill me-2"></i>법정 제출에 필요한 서류 양식을 작성하고 다운로드할 수 있습니다.</div>
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="hero-image">
                        <img src="/img/illustration-1.png" alt="Hero Image" class="img-fluid">
                    </div>
                </div>
            </div>

            <div class="d-flex gap-4 mb-4 align-items-stretch" style="position: relative; overflow: visible;">
                <div class="dropdown" style="width: 20%; min-width: 150px; position: relative;">
                    <button class="btn btn-outline-secondary dropdown-toggle rounded-pill shadow-sm category-btn w-100"
                            type="button" id="categoryDropdown"
                            style="padding: 0.65rem 1.25rem; border-color: #dee2e6; height: 100%;">
                        카테고리
                    </button>
                    <ul class="dropdown-menu" id="categoryMenu" aria-labelledby="categoryDropdown">
                        <li><a class="dropdown-item" href="#" data-category-id="" data-category-name="전체">전체</a></li>
                    </ul>
                </div>

                <div class="search-bar shadow-sm bg-white rounded-pill d-flex align-items-center border flex-grow-1" style="padding: 0.65rem 1.25rem;">
                    <input type="text" id="searchInput" class="form-control border-0 ms-3" placeholder="검색어를 입력해주세요." style="box-shadow: none; background: transparent;">
                    <button class="btn btn-link text-Fdark me-2" onclick="doSearch()">
                        <i class="bi bi-search fs-4"></i>
                    </button>
                </div>
            </div>
            <div id="rank-container" class="d-flex flex-wrap justify-content-center gap-3"></div>
        </div>
    </section>

    <section id="about" class="about section" style="padding-top: 0 !important; margin-top: 3rem">
        <div class="container">
            <h3 class="mb-4 text-left fw-bold">전체 서류 양식 목록</h3>
            <div class="card-grid" id="cardGrid"></div>
            <div id="pagination"></div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    var isLoggedIn = ${not empty sessionScope.loginUser ? 'true' : 'false'};
    let allCards = [];
    let currentPage = 1;
    const cardsPerPage = 6;
    let totalPages = 1;
    let selectedCategoryId = null;
    let searchKeyword = '';
    let searchTimer = null;

    $(document).ready(function() {
        // 카테고리 목록을 API에서 받아와 드롭다운 동적 생성
        loadCategories(function() {
            loadDocuments(1, null, null);
        });
        loadRanking();
        updateCartCount();

        // 카테고리 드롭다운 토글
        $('#categoryDropdown').on('click', function(e) {
            e.stopPropagation();
            const menu = $('#categoryMenu');
            const isOpen = menu.hasClass('show');
            menu.toggleClass('show', !isOpen);
            $(this).attr('aria-expanded', !isOpen);
        });

        // 외부 클릭 시 드롭다운 닫기
        $(document).on('click', function(e) {
            if (!$(e.target).closest('#categoryDropdown, #categoryMenu').length) {
                $('#categoryMenu').removeClass('show');
                $('#categoryDropdown').attr('aria-expanded', 'false');
            }
        });

        // 카테고리 선택 (동적으로 추가된 항목도 처리되도록 위임 이벤트)
        $(document).on('click', '#categoryMenu .dropdown-item', function(e) {
            e.preventDefault();
            e.stopPropagation();

            const categoryId = $(this).data('category-id');
            const categoryName = $(this).data('category-name');

            // 드롭다운 닫기
            $('#categoryMenu').removeClass('show');
            $('#categoryDropdown').attr('aria-expanded', 'false');

            if (!categoryId) {
                selectedCategoryId = null;
                $('#categoryDropdown').text('카테고리');
            } else {
                selectedCategoryId = categoryId;
                $('#categoryDropdown').text(categoryName);
            }

            loadDocuments(1, selectedCategoryId, searchKeyword);
        });

        $('#searchInput').on('input', function() {
            const keyword = $(this).val().trim();
            clearTimeout(searchTimer);
            if (keyword.length === 0) {
                removeAutocomplete();
                searchKeyword = '';
                loadDocuments(1, selectedCategoryId, null);
                return;
            }
            searchTimer = setTimeout(() => showAutocomplete(keyword), 200);
        });

        $('#searchInput').on('keydown', function(e) {
            if (e.keyCode === 13) doSearch();
        });
    });

    function getBadgeClass(badge) {
        const map = {'부동산': 'bg-primary', '민사': 'bg-success', '형사': 'bg-danger', '이혼/가족': 'bg-warning', '노동': 'bg-info'};
        return map[badge] || 'bg-secondary';
    }

    function loadCategories(callback) {
        $.ajax({
            url: '/api/documents?page=1&size=1',
            type: 'GET',
            success: function(data) {
                const categories = data.categories || [];
                const menu = $('#categoryMenu');
                // 기존 전체 항목은 유지하고 나머지 동적 추가
                menu.find('li:not(:first)').remove();
                categories.forEach(function(cat) {
                    menu.append(
                        '<li><a class="dropdown-item" href="#" data-category-id="' + cat.id + '" data-category-name="' + cat.name + '">' + cat.name + '</a></li>'
                    );
                });
                if (typeof callback === 'function') callback();
            },
            error: function() {
                // API에 categories 없으면 하드코딩 폴백
                const fallback = [{id:'부동산',name:'부동산'},{id:'민사',name:'민사'},{id:'형사',name:'형사'},{id:'이혼/가족',name:'이혼/가족'},{id:'노동',name:'노동'},{id:'기타',name:'기타'}];
                const menu = $('#categoryMenu');
                menu.find('li:not(:first)').remove();
                fallback.forEach(function(cat) {
                    menu.append(
                        '<li><a class="dropdown-item" href="#" data-category-id="' + cat.id + '" data-category-name="' + cat.name + '">' + cat.name + '</a></li>'
                    );
                });
                if (typeof callback === 'function') callback();
            }
        });
    }

    function loadDocuments(page, categoryId, keyword) {
        let url = '/api/documents?page=' + page + '&size=' + cardsPerPage;
        if (categoryId) url += '&categoryId=' + categoryId;
        if (keyword) url += '&keyword=' + encodeURIComponent(keyword);

        $.ajax({
            url: url,
            type: 'GET',
            success: function(data) {
                allCards = data.documents;
                totalPages = data.totalPages;
                currentPage = data.currentPage;
                renderCards();
                renderPagination();
            }
        });
    }

    function renderCards() {
        const cardGrid = document.getElementById('cardGrid');
        cardGrid.innerHTML = '';

        if (allCards.length === 0) {
            cardGrid.innerHTML = '<div style="grid-column: 1/-1; text-align:center; padding: 60px; color: #94a3b8;">검색 결과가 없습니다.</div>';
            return;
        }

        allCards.forEach(function(card) {
            const cardElement = document.createElement('div');
            cardElement.className = 'card';
            cardElement.onclick = () => addToCart(card.id);

            const categoryName = card.categoryName || '기타';

            cardElement.innerHTML = `
                <span class="badge \${getBadgeClass(categoryName)}">\${categoryName}</span>
                <h3 class="card-title">\${card.title}</h3>
                <div class="card-info">\${card.description || ''}</div>
                <div style="display: flex; justify-content: flex-end; margin-top: auto;">
                    <button class="btn btn-outline-primary" style="display: inline-flex; align-items: center; padding: 5px 12px; font-size: 14px;"
                            onclick="event.stopPropagation(); handleDownload(\${card.id})">
                        <i class="bi bi-download"></i><span style="margin-left: 5px;"> 다운로드</span>
                    </button>
                </div>`;
            cardGrid.appendChild(cardElement);
        });
    }

    function renderPagination() {
        const pagination = document.getElementById('pagination');
        pagination.innerHTML = '';
        if (totalPages <= 1) return;

        const pageGroupSize = 5;
        const currentGroup = Math.floor((currentPage - 1) / pageGroupSize);
        const startPage = currentGroup * pageGroupSize + 1;
        const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

        for (let i = startPage; i <= endPage; i++) {
            const btn = document.createElement('button');
            btn.className = 'pagination-btn' + (i === currentPage ? ' active' : '');
            btn.textContent = i;
            btn.onclick = () => loadDocuments(i, selectedCategoryId, searchKeyword);
            pagination.appendChild(btn);
        }
    }

    function doSearch() {
        searchKeyword = $('#searchInput').val().trim();
        removeAutocomplete();
        loadDocuments(1, selectedCategoryId, searchKeyword);
        if (searchKeyword) $.ajax({ url: "/api/search/log", type: "POST", data: { query: searchKeyword } });
    }

    function showAutocomplete(keyword) {
        removeAutocomplete();
        $.ajax({
            url: '/api/documents/autocomplete?keyword=' + keyword,
            type: 'GET',
            success: function(data) {
                if (data.length === 0) return;
                const box = document.createElement('div');
                box.className = 'search-autocomplete';
                box.id = 'autocompleteBox';
                data.forEach(doc => {
                    const item = document.createElement('div');
                    item.className = 'search-autocomplete-item';
                    item.innerHTML = `<span class="badge \${getBadgeClass(doc.categoryName)}" style="font-size:11px;">\${doc.categoryName || ''}</span><span>\${doc.title}</span>`;
                    item.onclick = () => {
                        $('#searchInput').val(doc.title);
                        doSearch();
                    };
                    box.appendChild(item);
                });
                const searchBar = document.querySelector('.search-bar');
                box.style.top = searchBar.offsetHeight + 'px';
                searchBar.appendChild(box);
            }
        });
    }

    function removeAutocomplete() { const ex = document.getElementById('autocompleteBox'); if (ex) ex.remove(); }

    function handleDownload(id) {
        if (!isLoggedIn) { if (confirm('로그인이 필요합니다.')) location.href = '/login'; return; }
        window.location.href = '/docs/download/' + id;
    }

    function loadRanking() {
        $.ajax({
            url: "/api/ranking",
            type: "GET",
            success: function(data) {
                $("#rank-container").empty();
                const colors = ['primary', 'secondary', 'info', 'danger', 'success', 'warning'];
                data.slice(0, 6).forEach((kw, i) => {
                    $("#rank-container").append(`<a href="javascript:void(0);" class="btn btn-outline-\${colors[i]} rounded-pill px-4 py-2 rank-tag" data-keyword="\${kw}"># \${kw}</a>`);
                });
            }
        });
    }

    $(document).on('click', '.rank-tag', function() {
        $('#searchInput').val($(this).data('keyword'));
        doSearch();
    });

    function addToCart(documentId) {
        $.ajax({
            url: '/api/cart/add',
            type: 'POST',
            data: { documentId: documentId },
            success: function(res) {
                if (res.success) { alert('📁 내 서류함에 담았습니다!'); updateCartCount(); }
                else if (res.message.includes('로그인')) { if (confirm('로그인하시겠습니까?')) location.href = '/login'; }
                else alert('⚠️ ' + res.message);
            }
        });
    }

    function updateCartCount() {
        $.ajax({
            url: '/api/cart/count',
            type: 'GET',
            success: function(res) {
                const count = res.count || 0;
                const badge = $('#cartCount');
                if (count > 0) badge.text(count).show(); else badge.hide();
            }
        });
    }
</script>
</body>
</html>
