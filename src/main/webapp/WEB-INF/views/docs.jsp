<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main">

    <section id="hero" class="hero section">
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
        <div class="col-lg-8 offset-lg-2" data-aos="fade-up" data-aos-delay="200" >
            <div class="d-flex gap-4">

                <div class="dropdown">
                    <button class="btn btn-outline-secondary dropdown-toggle rounded-pill shadow-sm category-btn"
                            type="button"
                            id="categoryDropdown"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                            style="height: 60px; min-width: 180px; border-color: #dee2e6;">
                        카테고리
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="categoryDropdown">
                        <li><a class="dropdown-item" href="#">전체</a></li>
                        <li><a class="dropdown-item" href="#">제품</a></li>
                        <li><a class="dropdown-item" href="#">서비스</a></li>
                        <li><a class="dropdown-item" href="#">고객지원</a></li>
                    </ul>
                </div>

                <div class="search-bar shadow-sm p-2 bg-white rounded-pill d-flex align-items-center border flex-grow-1"
                     style="height: 60px;">
                    <input type="text"
                           class="form-control border-0 ms-3"
                           placeholder="검색어를 입력해주세요."
                           style="box-shadow: none; background: transparent;">
                    <button class="btn btn-link text-dark me-2">
                        <i class="bi bi-search fs-4"></i>
                    </button>
                </div>
            </div>
            <div class="col-lg-8 offset-lg-3" data-aos="fade-up" data-aos-delay="200" style="margin-top: 20px;">
                <a href="#" class="bg-brand-softer hover:bg-brand-soft border border-brand-subtle text-fg-brand-strong text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 8px; margin-bottom: 8px; display: inline-block;">소장</a>
                <a href="#" class="bg-neutral-primary-soft hover:bg-neutral-secondary-medium border border-default text-heading text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 8px; margin-bottom: 8px; display: inline-block;">준비서면</a>
                <a href="#" class="bg-neutral-secondary-medium hover:bg-neutral-tertiary-medium border border-default-medium text-heading text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 8px; margin-bottom: 8px; display: inline-block;">고소장</a>
                <a href="#" class="bg-danger-soft hover:bg-danger-medium border border-danger-subtle text-fg-danger-strong text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 8px; margin-bottom: 8px; display: inline-block;">채권가압류</a>
                <a href="#" class="bg-success-soft hover:bg-success-medium border border-success-subtle text-fg-success-strong text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 8px; margin-bottom: 8px; display: inline-block;">내용증명</a>
                <a href="#" class="bg-warning-soft hover:bg-warning-medium border border-warning-subtle text-fg-warning text-sm font-medium px-3 py-1.5 rounded" style="margin-right: 8px; margin-bottom: 8px; display: inline-block;">합의서</a>
            </div>

            <div class="row stats-row gy-1 mt-5" data-aos="fade-up" data-aos-delay="500">
                <div class="col-lg-3 col-md-6"><div class="stat-item"><div class="stat-icon"><i class="bi bi-trophy"></i></div><div class="stat-content"><h4>3x Won Awards</h4><p class="mb-0">Vestibulum ante ipsum</p></div></div></div>
                <div class="col-lg-3 col-md-6"><div class="stat-item"><div class="stat-icon"><i class="bi bi-briefcase"></i></div><div class="stat-content"><h4>6.5k Faucibus</h4><p class="mb-0">Nullam quis ante</p></div></div></div>
                <div class="col-lg-3 col-md-6"><div class="stat-item"><div class="stat-icon"><i class="bi bi-graph-up"></i></div><div class="stat-content"><h4>80k Mauris</h4><p class="mb-0">Etiam sit amet orci</p></div></div></div>
                <div class="col-lg-3 col-md-6"><div class="stat-item"><div class="stat-icon"><i class="bi bi-award"></i></div><div class="stat-content"><h4>6x Phasellus</h4><p class="mb-0">Vestibulum ante ipsum</p></div></div></div>
            </div>
        </div>
    </section>

    <section id="about" class="about section" data-aos="zoom-out" data-aos-delay="300">
        <div class="container">
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

                    const cardBadge = document.createElement('span');
                    cardBadge.className = 'card-badge';
                    cardBadge.textContent = card.badge;

                    const cardTitle = document.createElement('h3');
                    cardTitle.className = 'card-title';
                    cardTitle.textContent = card.title;

                    const cardInfo = document.createElement('div');
                    cardInfo.className = 'card-info';
                    cardInfo.innerHTML = '등록: ' + card.date + ' | (조회수) ' + card.views + '회<br>' + card.description;

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