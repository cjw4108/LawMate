<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="/css/liveSearch.css">
    <style>
        .quick-form-card {
            display: block;
            background: white;
            border-radius: 16px;
            padding: 1.75rem;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            border: 2px solid transparent;
            height: 100%;
        }

        .quick-form-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12);
            border-color: #3b82f6;
        }

        .quick-form-icon {
            width: 64px;
            height: 64px;
            border-radius: 14px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1.25rem;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
        }

        .quick-form-content h4 {
            transition: color 0.2s;
        }

        .quick-form-card:hover .quick-form-content h4 {
            color: #3b82f6;
        }

        @media (max-width: 768px) {
            .quick-form-row {
                gap: 1rem;
            }
        }

        .card-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 24px;
            margin-bottom: 40px;
            padding: 20px 0;
        }

        .card-grid .card {
            background: white;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            padding: 28px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.06);
            transition: all 0.3s ease;
            cursor: pointer;
            text-decoration: none;
            color: inherit;
            display: block;
        }

        .card-grid .card:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 24px rgba(59, 130, 246, 0.15);
            border-color: #3b82f6;
        }

        .card-grid .card .badge {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 500;
            margin-bottom: 16px;
        }

        .card-grid .card-title {
            font-size: 18px;
            font-weight: 600;
            color: #1e293b;
            margin-bottom: 12px;
            margin-top: 0;
        }

        .card-grid .card-info {
            font-size: 13px;
            color: #64748b;
            line-height: 1.7;
            margin-bottom: 24px;
        }

        .card-grid .card-button {
            width: 100%;
            background-color: #3b82f6;
            color: white;
            border: none;
            padding: 12px 16px;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.2s;
        }

        .card-grid .card-button:hover {
            background-color: #2563eb;
            box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
        }

        #pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
            margin-top: 40px;
            padding-bottom: 40px;
        }

        #pagination .pagination-btn {
            width: 40px;
            height: 40px;
            border: 1px solid #e5e7eb;
            background: white;
            border-radius: 8px;
            cursor: pointer;
            font-size: 15px;
            color: #475569;
            transition: all 0.2s;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 500;
        }

        #pagination .pagination-btn:hover:not(.active):not(:disabled) {
            background-color: #f1f5f9;
            border-color: #cbd5e1;
        }

        #pagination .pagination-btn.active {
            background-color: #3b82f6;
            color: white;
            border-color: #3b82f6;
            box-shadow: 0 2px 8px rgba(59, 130, 246, 0.25);
        }

        #pagination .pagination-btn:disabled {
            opacity: 0.4;
            cursor: not-allowed;
        }

        @media (max-width: 968px) {
            .card-grid {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 640px) {
            .card-grid {
                grid-template-columns: 1fr;
            }
        }

        .hero.section {
            padding-top: 80px !important;
        }

        #hero .container {
            overflow: visible !important;
        }

        #hero .row {
            overflow: visible !important;
        }

        .d-flex.gap-4.mb-4 {
            overflow: visible !important;
            position: relative;
        }

        .dropdown-menu {
            z-index: 99999 !important;
            position: absolute !important;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
<main class="main">

    <section id="hero" class="hero section" style="padding-bottom: 0 !important; overflow: visible;">
        <div class="container" data-aos="fade-up" data-aos-delay="100" style="overflow: visible;">
            <div class="row align-items-center">
                <div class="col-lg-6 offset-lg-1">
                    <div class="hero-content" data-aos="fade-up" data-aos-delay="200">
                        <h1 class="mb-4"><span class="accent-text">서류 양식</span></h1>
                        <div class="company-badge mb-4"><i class="bi bi-gear-fill me-2"></i>법정 제출에 필요한 서류 양식을 작성하고 다운로드할 수 있습니다.</div>
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="hero-image" data-aos="zoom-out" data-aos-delay="300">
                        <img src="/img/illustration-1.png" alt="Hero Image" class="img-fluid">
                    </div>
                </div>
            </div>

            <div class="d-flex gap-4 mb-4 align-items-stretch" style="position: relative; overflow: visible;">
                <div class="dropdown" style="width: 20%; min-width: 150px;">
                    <button class="btn btn-outline-secondary dropdown-toggle rounded-pill shadow-sm category-btn w-100"
                            type="button"
                            id="categoryDropdown"
                            data-bs-toggle="dropdown"
                            data-bs-auto-close="true"
                            aria-expanded="false"
                            style="padding: 0.65rem 1.25rem; border-color: #dee2e6; height: 100%;">
                        카테고리
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="categoryDropdown" style="z-index: 99999;">
                        <li><a class="dropdown-item" href="#">전체</a></li>
                        <li><a class="dropdown-item" href="#">부동산</a></li>
                        <li><a class="dropdown-item" href="#">민사</a></li>
                        <li><a class="dropdown-item" href="#">형사</a></li>
                        <li><a class="dropdown-item" href="#">이혼/가족</a></li>
                        <li><a class="dropdown-item" href="#">노동</a></li>
                        <li><a class="dropdown-item" href="#">기타</a></li>
                    </ul>
                </div>

                <div class="search-bar shadow-sm bg-white rounded-pill d-flex align-items-center border flex-grow-1"
                     style="padding: 0.65rem 1.25rem; position: relative;">
                    <input type="text"
                           id="searchInput"
                           class="form-control border-0 ms-3"
                           placeholder="검색어를 입력해주세요."
                           style="box-shadow: none; background: transparent; padding: 0.25rem 0;">
                    <button class="btn btn-link text-Fdark me-2">
                        <i class="bi bi-search fs-4"></i>
                    </button>
                </div>
            </div>

            <div id="rank-container" class="d-flex flex-wrap justify-content-center gap-3">
            </div>
        </div>
    </section>

    <section class="quick-forms-section" data-aos="zoom-out" data-aos-delay="300" style="padding: 3rem 0;">
        <div class="container">
            <h3 class="mb-4 text-left fw-bold">자주 쓰는 양식 간편 작성 (2차예정)</h3>
            <div class="row quick-form-row gy-4" data-aos="fade-up" data-aos-delay="500">
                <div class="col-lg-3 col-md-6">
                    <a href="/forms/complaint" class="quick-form-card" style="text-decoration: none;">
                        <div class="quick-form-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <i class="bi bi-file-text" style="font-size: 1.8rem; color: white;"></i>
                        </div>
                        <div class="quick-form-content">
                            <h4 style="font-size: 1.1rem; font-weight: 600; color: #1e293b; margin-bottom: 0.5rem;">소장</h4>
                            <p style="font-size: 0.85rem; color: #64748b; margin: 0;">민사소송 소장 작성</p>
                        </div>
                    </a>
                </div>
                <div class="col-lg-3 col-md-6">
                    <a href="/forms/written-preparation" class="quick-form-card" style="text-decoration: none;">
                        <div class="quick-form-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <i class="bi bi-file-earmark-text" style="font-size: 1.8rem; color: white;"></i>
                        </div>
                        <div class="quick-form-content">
                            <h4 style="font-size: 1.1rem; font-weight: 600; color: #1e293b; margin-bottom: 0.5rem;">준비서면</h4>
                            <p style="font-size: 0.85rem; color: #64748b; margin: 0;">소송 준비서면 작성</p>
                        </div>
                    </a>
                </div>
                <div class="col-lg-3 col-md-6">
                    <a href="/forms/accusation" class="quick-form-card" style="text-decoration: none;">
                        <div class="quick-form-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <i class="bi bi-shield-exclamation" style="font-size: 1.8rem; color: white;"></i>
                        </div>
                        <div class="quick-form-content">
                            <h4 style="font-size: 1.1rem; font-weight: 600; color: #1e293b; margin-bottom: 0.5rem;">고소장</h4>
                            <p style="font-size: 0.85rem; color: #64748b; margin: 0;">형사 고소장 작성</p>
                        </div>
                    </a>
                </div>
                <div class="col-lg-3 col-md-6">
                    <a href="/forms/content-certification" class="quick-form-card" style="text-decoration: none;">
                        <div class="quick-form-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                            <i class="bi bi-envelope-check" style="font-size: 1.8rem; color: white;"></i>
                        </div>
                        <div class="quick-form-content">
                            <h4 style="font-size: 1.1rem; font-weight: 600; color: #1e293b; margin-bottom: 0.5rem;">내용증명</h4>
                            <p style="font-size: 0.85rem; color: #64748b; margin: 0;">내용증명 우편 작성</p>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </section>

    <section id="about" class="about section" data-aos="zoom-out" data-aos-delay="300" style="padding-top: 0 !important;">
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
        loadDocuments(1, null, null);
        loadRanking();
        updateCartCount();

        $('#categoryDropdown').closest('.dropdown').find('.dropdown-item').on('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            const categoryName = $(this).text();

            if (categoryName === '전체') {
                selectedCategoryId = null;
                $('#categoryDropdown').text('카테고리');
                loadDocuments(1, null, searchKeyword);
                return;
            }

            $.ajax({
                url: '/api/documents?page=1&size=1',
                type: 'GET',
                success: function(data) {
                    const category = data.categories.find(c => c.name === categoryName);
                    if (category) {
                        selectedCategoryId = category.id;
                        $('#categoryDropdown').text(categoryName);
                        loadDocuments(1, selectedCategoryId, searchKeyword);
                    }
                }
            });
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
            searchTimer = setTimeout(function() {
                showAutocomplete(keyword);
            }, 200);
        });

        $('#searchInput').on('keydown', function(e) {
            if (e.keyCode === 13) {
                searchKeyword = $(this).val().trim();
                removeAutocomplete();
                loadDocuments(1, selectedCategoryId, searchKeyword);
                if (searchKeyword) {
                    $.ajax({ url: "/api/search/log", type: "POST", data: { query: searchKeyword } });
                }
            }
        });

        $(document).on('click', function(e) {
            if (!$(e.target).closest('.search-bar').length) {
                removeAutocomplete();
            }
        });
    });

    function getBadgeClass(badge) {
        const badgeColorMap = {
            '부동산': 'bg-primary',
            '민사': 'bg-success',
            '형사': 'bg-danger',
            '이혼/가족': 'bg-warning',
            '노동': 'bg-info',
            '기타': 'bg-secondary'
        };
        return badgeColorMap[badge] || 'bg-primary';
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
            cardElement.style.cursor = 'pointer';
            cardElement.onclick = function(e) {
                e.stopPropagation();
                addToCart(card.id);
            };

            const categoryName = card.categoryName || '기타';
            cardElement.innerHTML =
                '<span class="badge ' + getBadgeClass(categoryName) + '">' + categoryName + '</span>' +
                '<h3 class="card-title">' + card.title + '</h3>' +
                '<div class="card-info">' + (card.description || '') + '</div>' +
                '<button class="card-button" onclick="event.stopPropagation(); handleDownload(' + card.id + ')" style="background: #3b82f6;">다운로드</button>';

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

        if (currentGroup > 0) {
            addPagBtn(pagination, '&laquo;', function() { loadDocuments(1, selectedCategoryId, searchKeyword); });
            addPagBtn(pagination, '&lt;', function() { loadDocuments(startPage - 1, selectedCategoryId, searchKeyword); });
        }

        for (let i = startPage; i <= endPage; i++) {
            const btn = document.createElement('button');
            btn.className = 'pagination-btn' + (i === currentPage ? ' active' : '');
            btn.textContent = i;
            btn.onclick = (function(p) { return function() { loadDocuments(p, selectedCategoryId, searchKeyword); }; })(i);
            pagination.appendChild(btn);
        }

        if (endPage < totalPages) {
            addPagBtn(pagination, '&gt;', function() { loadDocuments(endPage + 1, selectedCategoryId, searchKeyword); });
            addPagBtn(pagination, '&raquo;', function() { loadDocuments(totalPages, selectedCategoryId, searchKeyword); });
        }
    }

    function addPagBtn(parent, html, onclick) {
        const btn = document.createElement('button');
        btn.className = 'pagination-btn';
        btn.innerHTML = html;
        btn.onclick = onclick;
        parent.appendChild(btn);
    }

    function downloadFile(id) {
        window.location.href = '/docs/download/' + id;
    }

    function showAutocomplete(keyword) {
        removeAutocomplete();
        if (!keyword || keyword.length < 1) return;

        $.ajax({
            url: '/api/documents?page=1&size=100',
            type: 'GET',
            success: function(data) {
                const kw = keyword.toLowerCase();
                const matched = data.documents.filter(d => d.title.toLowerCase().includes(kw)).slice(0, 8);

                if (matched.length === 0) return;

                const box = document.createElement('div');
                box.className = 'search-autocomplete';
                box.id = 'autocompleteBox';

                matched.forEach(function(doc) {
                    const item = document.createElement('div');
                    item.className = 'search-autocomplete-item';
                    item.innerHTML =
                        '<span class="badge ' + getBadgeClass(doc.categoryName) + '" style="font-size:11px;">' + (doc.categoryName || '') + '</span>' +
                        '<span>' + doc.title + '</span>';
                    item.onclick = function() {
                        $('#searchInput').val(doc.title);
                        searchKeyword = doc.title;
                        removeAutocomplete();
                        loadDocuments(1, selectedCategoryId, searchKeyword);
                    };
                    box.appendChild(item);
                });

                const searchBar = document.querySelector('.search-bar');
                searchBar.style.position = 'relative';
                box.style.top = searchBar.offsetHeight + 'px';
                box.style.left = '0';
                box.style.right = '0';
                searchBar.appendChild(box);
            }
        });
    }

    function removeAutocomplete() {
        const existing = document.getElementById('autocompleteBox');
        if (existing) existing.remove();
    }

    function handleDownload(id) {
        if (!isLoggedIn) {
            if (confirm('로그인이 필요합니다.\n로그인 페이지로 이동하시겠습니까?')) {
                location.href = '/login';
            }
            return;
        }
        downloadFile(id);
    }

    function loadRanking() {
        $.ajax({
            url: "/api/ranking",
            type: "GET",
            success: function(data) {
                $("#rank-container").empty();
                const colors = ['primary', 'secondary', 'info', 'danger', 'success', 'warning'];
                if (!data || data.length === 0) return;
                for (let i = 0; i < data.length; i++) {
                    if (data[i] == null || i >= 6) break;
                    let color = colors[i % colors.length];
                    $("#rank-container").append(
                        '<a href="#" class="btn btn-outline-' + color + ' rounded-pill px-4 py-2"># ' + data[i] + '</a>'
                    );
                }
            }
        });
    }

    function addToCart(documentId) {
        $.ajax({
            url: '/api/cart/add',
            type: 'POST',
            data: { documentId: documentId },
            success: function(response) {
                if (response.success) {
                    alert('📁 내 서류함에 담았습니다!');
                    updateCartCount();
                } else {
                    if (response.message.includes('로그인')) {
                        if (confirm(response.message + '\n로그인 페이지로 이동하시겠습니까?')) {
                            location.href = '/login';
                        }
                    } else {
                        alert('⚠️ ' + response.message);
                    }
                }
            }
        });
    }

    function updateCartCount() {
        $.ajax({
            url: '/api/cart/count',
            type: 'GET',
            success: function(response) {
                const count = response.count || 0;
                if (count > 0) {
                    $('#cartCount').text(count).show();
                } else {
                    $('#cartCount').hide();
                }
            }
        });
    }
</script>

</body>
</html>
