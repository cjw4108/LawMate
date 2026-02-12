<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main">

    <section id="hero" class="hero section" style="padding-bottom: 0 !important;">
        <div class="container" data-aos="fade-up" data-aos-delay="100">
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
        </div>
        <div class="col-lg-8 offset-lg-2" data-aos="fade-up" data-aos-delay="200" style="margin-bottom: 0 !important;">
            <div class="d-flex gap-4">

                <div class="dropdown" style="width: 20%; min-width: 150px;">
                    <button class="btn btn-outline-secondary dropdown-toggle rounded-pill shadow-sm category-btn w-100"
                            type="button"
                            id="categoryDropdown"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                            style="padding: 1rem 1.5rem; border-color: #dee2e6;">
                        카테고리
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="categoryDropdown">
                        <li><a class="dropdown-item" href="#">전체</a></li>
                        <li><a class="dropdown-item" href="#">제품</a></li>
                        <li><a class="dropdown-item" href="#">서비스</a></li>
                        <li><a class="dropdown-item" href="#">고객지원</a></li>
                    </ul>
                </div>

                <div class="search-bar shadow-sm bg-white rounded-pill d-flex align-items-center border flex-grow-1"
                     style="padding: 0.75rem 1.25rem;">
                    <input type="text"
                           class="form-control border-0 ms-3"
                           placeholder="검색어를 입력해주세요."
                           style="box-shadow: none; background: transparent; padding: 0.5rem 0;">
                    <button class="btn btn-link text-dark me-2">
                        <i class="bi bi-search fs-4"></i>
                    </button>
                </div>
            </div>
            <div class="col-lg-8 offset-lg-3" data-aos="fade-up" data-aos-delay="200" style="margin-top: 1.5rem;">
                <a href="#" class="bg-brand-softer hover:bg-brand-soft border border-brand-subtle text-fg-brand-strong text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 0.5rem; margin-bottom: 0.5rem; display: inline-block;">소장</a>
                <a href="#" class="bg-neutral-primary-soft hover:bg-neutral-secondary-medium border border-default text-heading text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 0.5rem; margin-bottom: 0.5rem; display: inline-block;">준비서면</a>
                <a href="#" class="bg-neutral-secondary-medium hover:bg-neutral-tertiary-medium border border-default-medium text-heading text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 0.5rem; margin-bottom: 0.5rem; display: inline-block;">고소장</a>
                <a href="#" class="bg-danger-soft hover:bg-danger-medium border border-danger-subtle text-fg-danger-strong text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 0.5rem; margin-bottom: 0.5rem; display: inline-block;">채권가압류</a>
                <a href="#" class="bg-success-soft hover:bg-success-medium border border-success-subtle text-fg-success-strong text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 0.5rem; margin-bottom: 0.5rem; display: inline-block;">내용증명</a>
                <a href="#" class="bg-warning-soft hover:bg-warning-medium border border-warning-subtle text-fg-warning text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 0.5rem; margin-bottom: 0.5rem; display: inline-block;">합의서</a>
            </div>
        </div>
    </section>

    <section class="quick-forms-section" data-aos="zoom-out" data-aos-delay="300" style="padding: 3rem 0;">
        <div class="container">
            <h3 class="mb-4 text-center fw-bold">자주 쓰는 양식 바로 작성하기</h3>

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

                /* 서류 양식 카드 전용 스타일 */
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
            </style>
        </div>
    </section>

    <section id="about" class="about section" data-aos="zoom-out" data-aos-delay="300" style="padding-top: 0 !important;">
        <div class="container">
            <h3 class="mb-4 text-center fw-bold">전체 서류 양식 목록</h3>

            <div class="card-grid" id="cardGrid">
            </div>

            <div id="pagination">
            </div>
        </div>

        <script>
            const allCards = [
                { id: 1, badge: '계약 관련 서류', title: '임대차 계약서', date: '2024. 01. 15', views: 123, description: '표준 임대차 계약서 양식' },
                { id: 2, badge: '소송 관련 서류', title: '소장 양식', date: '2024. 01. 16', views: 456, description: '민사소송 소장 작성 양식' },
                { id: 3, badge: '계약 관련 서류', title: '근로계약서', date: '2024. 01. 17', views: 789, description: '표준 근로계약서 양식' },
                { id: 4, badge: '소송 관련 서류', title: '답변서 양식', date: '2024. 01. 18', views: 234, description: '민사소송 답변서 작성 양식' },
                { id: 5, badge: '계약 관련 서류', title: '매매계약서', date: '2024. 01. 19', views: 567, description: '부동산 매매계약서 양식' },
                { id: 6, badge: '고소/고발 서류', title: '고소장 양식', date: '2024. 01. 20', views: 890, description: '형사 고소장 작성 양식' },
                { id: 7, badge: '계약 관련 서류', title: '위임장', date: '2024. 01. 21', views: 345, description: '법률 대리 위임장 양식' },
                { id: 8, badge: '소송 관련 서류', title: '준비서면', date: '2024. 01. 22', views: 678, description: '소송 준비서면 작성 양식' },
                { id: 9, badge: '고소/고발 서류', title: '고발장 양식', date: '2024. 01. 23', views: 901, description: '형사 고발장 작성 양식' },
                { id: 10, badge: '계약 관련 서류', title: '합의서', date: '2024. 01. 24', views: 432, description: '분쟁 합의서 양식' },
                { id: 11, badge: '소송 관련 서류', title: '항소장', date: '2024. 01. 25', views: 765, description: '항소장 작성 양식' },
                { id: 12, badge: '계약 관련 서류', title: '비밀유지계약서', date: '2024. 01. 26', views: 321, description: 'NDA 계약서 양식' },
                { id: 13, badge: '소송 관련 서류', title: '상고장', date: '2024. 01. 27', views: 654, description: '상고장 작성 양식' },
            ];

            const cardsPerPage = 6;
            let currentPage = 1;
            const totalPages = Math.ceil(allCards.length / cardsPerPage);

            // 카테고리별 배지 색상 매핑
            function getBadgeClass(badge) {
                const badgeColorMap = {
                    '계약 관련 서류': 'bg-primary',
                    '소송 관련 서류': 'bg-success',
                    '고소/고발 서류': 'bg-danger',
                    '이혼절차': 'bg-warning',
                    '기타': 'bg-secondary'
                };
                return badgeColorMap[badge] || 'bg-primary'; // 기본값은 bg-primary
            }

            function renderCards(page) {
                const cardGrid = document.getElementById('cardGrid');
                cardGrid.innerHTML = '';

                const startIndex = (page - 1) * cardsPerPage;
                const endIndex = startIndex + cardsPerPage;
                const cardsToShow = allCards.slice(startIndex, endIndex);

                cardsToShow.forEach(card => {
                    const cardElement = document.createElement('a');
                    cardElement.href = 'detail.html?id=' + card.id;
                    cardElement.className = 'card';

                    // 배지
                    const cardBadge = document.createElement('span');
                    cardBadge.className = 'badge ' + getBadgeClass(card.badge);
                    cardBadge.textContent = card.badge;

                    // 제목
                    const cardTitle = document.createElement('h3');
                    cardTitle.className = 'card-title';
                    cardTitle.textContent = card.title;

                    // 정보
                    const cardInfo = document.createElement('div');
                    cardInfo.className = 'card-info';
                    cardInfo.innerHTML = '등록: ' + card.date + ' | (조회수) ' + card.views + '회<br>' + card.description;

                    // 다운로드 버튼
                    const cardButton = document.createElement('button');
                    cardButton.className = 'card-button';
                    cardButton.textContent = '다운로드';
                    cardButton.onclick = function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        downloadFile(card.id);
                    };

                    cardElement.appendChild(cardBadge);
                    cardElement.appendChild(cardTitle);
                    cardElement.appendChild(cardInfo);
                    cardElement.appendChild(cardButton);

                    cardGrid.appendChild(cardElement);
                });
            }

            function renderPagination() {
                const pagination = document.getElementById('pagination');
                pagination.innerHTML = '';

                const prevBtn = document.createElement('button');
                prevBtn.className = 'pagination-btn';
                prevBtn.innerHTML = '&lt;';
                prevBtn.disabled = currentPage === 1;
                prevBtn.onclick = function() {
                    goToPage(currentPage - 1);
                };
                pagination.appendChild(prevBtn);

                for (let i = 1; i <= totalPages; i++) {
                    const pageBtn = document.createElement('button');
                    pageBtn.className = 'pagination-btn';
                    if (i === currentPage) {
                        pageBtn.className += ' active';
                    }
                    pageBtn.textContent = i;
                    pageBtn.onclick = (function(pageNum) {
                        return function() {
                            goToPage(pageNum);
                        };
                    })(i);
                    pagination.appendChild(pageBtn);
                }

                // 다음 버튼
                const nextBtn = document.createElement('button');
                nextBtn.className = 'pagination-btn';
                nextBtn.innerHTML = '&gt;';
                nextBtn.disabled = currentPage === totalPages;
                nextBtn.onclick = function() {
                    goToPage(currentPage + 1);
                };
                pagination.appendChild(nextBtn);
            }

            function goToPage(page) {
                if (page < 1 || page > totalPages) return;

                currentPage = page;
                renderCards(currentPage);
                renderPagination();

            }

            function downloadFile(id) {
                console.log('다운로드:', id);
                alert('파일 ' + id + ' 다운로드');

            }

            renderCards(currentPage);
            renderPagination();
        </script>
    </section>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
