<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main">

    <section id="hero" class="hero section">
        <div class="container" data-aos="fade-up" data-aos-delay="100">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <div class="hero-content" data-aos="fade-up" data-aos-delay="200">
                        <h1 class="mb-4"><span class="accent-text">서류 양식</span></h1>
                        <div class="company-badge mb-4"><i class="bi bi-gear-fill me-2"></i>법정 제출에 필요한 서류 양식을 작성하고 다운로드할 수 있습니다.</div>
                    </div>
                </div>

                <div class="col-lg-6">
                    <div class="hero-image" data-aos="zoom-out" data-aos-delay="300">
                        <img src="/img/illustration-1.webp" alt="Hero Image" class="img-fluid">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="search-bar shadow-sm p-2 bg-white rounded-pill d-flex align-items-center border" style="height: 60px;">
                    <input type="text" class="form-control border-0 ms-3" placeholder="검색어를 입력해주세요." style="box-shadow: none; background: transparent;">
                    <button class="btn btn-link text-dark me-2"><i class="bi bi-search fs-4"></i></button>
                </div>
            </div>
            <div class="row stats-row gy-4 mt-5" data-aos="fade-up" data-aos-delay="500">
                <div class="col-lg-3 col-md-6"><div class="stat-item"><div class="stat-icon"><i class="bi bi-trophy"></i></div><div class="stat-content"><h4>3x Won Awards</h4><p class="mb-0">Vestibulum ante ipsum</p></div></div></div>
                <div class="col-lg-3 col-md-6"><div class="stat-item"><div class="stat-icon"><i class="bi bi-briefcase"></i></div><div class="stat-content"><h4>6.5k Faucibus</h4><p class="mb-0">Nullam quis ante</p></div></div></div>
                <div class="col-lg-3 col-md-6"><div class="stat-item"><div class="stat-icon"><i class="bi bi-graph-up"></i></div><div class="stat-content"><h4>80k Mauris</h4><p class="mb-0">Etiam sit amet orci</p></div></div></div>
                <div class="col-lg-3 col-md-6"><div class="stat-item"><div class="stat-icon"><i class="bi bi-award"></i></div><div class="stat-content"><h4>6x Phasellus</h4><p class="mb-0">Vestibulum ante ipsum</p></div></div></div>
            </div>
        </div>
    </section>

    <section id="about" class="about section">
        <div class="container" data-aos="fade-up" data-aos-delay="100">
            <div class="row gy-4 align-items-center justify-content-between">
                <div class="col-xl-5" data-aos="fade-up" data-aos-delay="200">
                    <span class="about-meta">MORE ABOUT US</span>
                    <h2 class="about-title">Voluptas enim suscipit temporibus</h2>
                    <p class="about-description">Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.</p>
                    <div class="info-wrapper">
                        <div class="row gy-4">
                            <div class="col-lg-5"><div class="profile d-flex align-items-center gap-3"><img src="/img/avatar-1.webp" alt="" class="profile-image"><div><h4 class="profile-name">Mario Smith</h4><p class="profile-position">CEO &amp; Founder</p></div></div></div>
                        </div>
                    </div>
                </div>
                <div class="col-xl-6" data-aos="fade-up" data-aos-delay="300">
                    <div class="image-wrapper">
                        <div class="images position-relative" data-aos="zoom-out" data-aos-delay="400">
                            <img src="/img/about-5.webp" alt="" class="img-fluid main-image rounded-4">
                            <img src="/img/about-2.webp" alt="" class="img-fluid small-image rounded-4">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="features" class="features section">
        <div class="container section-title" data-aos="fade-up">
            <h2>Features</h2>
            <p>Necessitatibus eius consequatur ex aliquid fuga eum quidem sint consectetur velit</p>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-lg-6 order-2 order-lg-1 d-flex flex-column justify-content-center" data-aos="fade-up">
                    <h3>Voluptatem dignissimos provident</h3>
                    <p class="fst-italic">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
                    <ul>
                        <li><i class="bi bi-check2-all"></i> <span>Ullamco laboris nisi ut aliquip ex ea commodo consequat.</span></li>
                        <li><i class="bi bi-check2-all"></i> <span>Duis aute irure dolor in reprehenderit in voluptate velit.</span></li>
                    </ul>
                </div>
                <div class="col-lg-6 order-1 order-lg-2 text-center" data-aos="zoom-out">
                    <img src="/img/features-illustration-1.webp" alt="" class="img-fluid">
                </div>
            </div>
        </div>
    </section>

    <section id="pricing" class="pricing section light-background">
        <div class="container section-title" data-aos="fade-up">
            <h2>Pricing</h2>
            <p>Necessitatibus eius consequatur ex aliquid fuga eum quidem sint consectetur velit</p>
        </div>
        <div class="container" data-aos="fade-up" data-aos-delay="100">
            <div class="row g-4 justify-content-center">
                <div class="col-lg-4">
                    <div class="pricing-card">
                        <h3>Basic Plan</h3>
                        <div class="price"><span class="currency">$</span><span class="amount">9.9</span><span class="period">/ month</span></div>
                        <ul class="features-list">
                            <li><i class="bi bi-check-circle-fill"></i> Duis aute irure dolor</li>
                            <li><i class="bi bi-check-circle-fill"></i> Excepteur sint occaecat</li>
                        </ul>
                        <a href="#" class="btn btn-primary">Buy Now <i class="bi bi-arrow-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="contact" class="contact section">
        <div class="container section-title" data-aos="fade-up">
            <h2>Contact</h2>
            <p>Necessitatibus eius consequatur ex aliquid fuga eum quidem sint consectetur velit</p>
        </div>
        <div class="container" data-aos="fade-up" data-aos-delay="100">
            <div class="row g-4 g-lg-5">
                <div class="col-lg-5">
                    <div class="info-box">
                        <h3>Contact Info</h3>
                        <div class="info-item"><i class="bi bi-geo-alt"></i><div><h4>Our Location</h4><p>A108 Adam Street, New York, NY 535022</p></div></div>
                        <div class="info-item"><i class="bi bi-envelope"></i><div><h4>Email Address</h4><p>info@example.com</p></div></div>
                    </div>
                </div>
            </div>
        </div>
    </section>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>