"""
법률구조공단 법률서식 크롤링 스크립트
- 카테고리별 10개씩 크롤링 (총 60개)
- 파일 자동 다운로드
- JSON 메타데이터 생성
"""

import requests
from bs4 import BeautifulSoup
import json
import time
import os
from urllib.parse import unquote
import re

# 설정
BASE_URL = "https://www.klac.or.kr/legalinfo/legalFrm.do"
DOWNLOAD_FOLDER = "./src/main/resources/legal-forms/files"
JSON_OUTPUT = "./src/main/resources/legal-forms/legal_forms.json"

# 카테고리 매칭 (내 사이트 카테고리 → 법률구조공단 코드)
CATEGORIES = {
    '부동산': ['002', '003', '006'],      # 주택임대차, 상가임대차, 물권
    '민사': ['005', '009', '010'],         # 민사일반, 상사, 민사소송
    '형사': ['018', '019'],                # 형법, 형사소송
    '이혼/가족': ['011', '012', '013'],    # 친족, 상속, 가사소송
    '노동': ['001'],                       # 노동
    '기타': ['022']                        # 기타
}

def setup_folders():
    """필요한 폴더 생성"""
    os.makedirs(DOWNLOAD_FOLDER, exist_ok=True)
    print(f"✓ 다운로드 폴더 준비: {DOWNLOAD_FOLDER}")

def clean_filename(text):
    """파일명에서 특수문자 제거"""
    # Windows에서 사용 불가능한 문자 제거
    text = re.sub(r'[<>:"/\\|?*]', '', text)
    # 공백을 언더스코어로
    text = text.replace(' ', '_')
    # 너무 긴 파일명 자르기 (최대 100자)
    if len(text) > 100:
        text = text[:100]
    return text

def crawl_category(folder_id, category_name, max_items=10):
    """특정 카테고리의 양식 크롤링"""
    print(f"\n{'='*60}")
    print(f"[{category_name}] 카테고리 크롤링 시작 (코드: {folder_id})")
    print(f"{'='*60}")
    
    forms_data = []
    
    try:
        # POST 요청으로 카테고리 필터링
        data = {
            'folderId': folder_id,
            'listNm': '',
            'pageIndex': '1',
            'scdFolderId': '',
            'srchInput': ''
        }
        
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
            'Content-Type': 'application/x-www-form-urlencoded',
            'Referer': BASE_URL
        }
        
        response = requests.post(BASE_URL, data=data, headers=headers, timeout=10)
        response.raise_for_status()
        
        # HTML 파싱
        soup = BeautifulSoup(response.content, 'html.parser')
        
        # 디버깅: HTML 구조 확인
        print(f"\n[디버깅] 응답 상태 코드: {response.status_code}")
        print(f"[디버깅] 응답 길이: {len(response.content)} bytes")
        
        # 테이블 찾기 시도
        tables = soup.find_all('table')
        print(f"[디버깅] 찾은 table 수: {len(tables)}")
        
        # tr 찾기 시도
        all_rows = soup.find_all('tr')
        print(f"[디버깅] 찾은 모든 tr 수: {len(all_rows)}")
        
        # 테이블에서 양식 목록 추출
        rows = soup.select('table tbody tr')
        
        if not rows:
            print(f"⚠ tbody tr을 찾을 수 없습니다. 다른 셀렉터 시도...")
            # tbody 없이 시도
            rows = soup.select('table tr')
            # 첫 번째 행(헤더) 제외
            if rows and len(rows) > 1:
                rows = rows[1:]
                print(f"✓ table tr로 {len(rows)}개 발견")
            else:
                print(f"✗ 데이터를 찾을 수 없습니다.")
                return forms_data
        
        print(f"✓ {len(rows)}개 항목 발견")
        
        # 최대 max_items개까지만 수집
        for idx, row in enumerate(rows[:max_items], 1):
            try:
                # 카테고리명과 제목 추출
                cols = row.find_all('td')
                if len(cols) < 3:
                    continue
                
                subcategory = cols[0].get_text(strip=True)
                title = cols[1].get_text(strip=True)
                
                # 다운로드 링크 추출
                download_link = cols[2].find('a', class_='btn_down_none_float')
                if not download_link or not download_link.get('href'):
                    print(f"  ⚠ [{idx}] 다운로드 링크 없음: {title}")
                    continue
                
                download_url = download_link['href']
                
                # 상대 경로를 절대 경로로 변환
                if not download_url.startswith('http'):
                    download_url = 'https://support.klac.or.kr' + download_url
                
                # 파일 다운로드
                file_path = download_file(download_url, category_name, title, idx)
                
                if file_path:
                    form_info = {
                        'category': category_name,
                        'subcategory': subcategory,
                        'title': title,
                        'file_path': file_path,
                        'download_url': download_url
                    }
                    forms_data.append(form_info)
                    print(f"  ✓ [{idx}/{max_items}] {title[:40]}...")
                else:
                    print(f"  ✗ [{idx}/{max_items}] 다운로드 실패: {title}")
                
                # 서버 부담 방지
                time.sleep(1)
                
            except Exception as e:
                print(f"  ✗ 항목 처리 중 오류: {e}")
                continue
        
        print(f"\n✓ {category_name} 완료: {len(forms_data)}개 수집")
        
    except Exception as e:
        print(f"✗ 카테고리 크롤링 실패: {e}")
    
    return forms_data

def download_file(url, category, title, idx):
    """파일 다운로드"""
    try:
        response = requests.get(url, timeout=30)
        response.raise_for_status()
        
        # 파일명 생성
        safe_category = clean_filename(category)
        safe_title = clean_filename(title)
        filename = f"{safe_category}_{idx:02d}_{safe_title}.hwp"
        file_path = os.path.join(DOWNLOAD_FOLDER, filename)
        
        # 파일 저장
        with open(file_path, 'wb') as f:
            f.write(response.content)
        
        # 상대 경로로 변환 (JSON에 저장용)
        relative_path = f"legal-forms/files/{filename}"
        return relative_path
        
    except Exception as e:
        print(f"    다운로드 오류: {e}")
        return None

def main():
    """메인 실행 함수"""
    print("\n" + "="*60)
    print("법률구조공단 양식 크롤링 시작")
    print("="*60)
    
    # 폴더 생성
    setup_folders()
    
    all_forms = []
    total_count = 0
    
    # 각 카테고리별로 크롤링
    for my_category, folder_ids in CATEGORIES.items():
        for folder_id in folder_ids:
            forms = crawl_category(folder_id, my_category, max_items=10)
            all_forms.extend(forms)
            total_count += len(forms)
            
            # 카테고리 간 대기
            time.sleep(2)
    
    # JSON 파일 저장
    json_dir = os.path.dirname(JSON_OUTPUT)
    os.makedirs(json_dir, exist_ok=True)
    
    with open(JSON_OUTPUT, 'w', encoding='utf-8') as f:
        json.dump(all_forms, f, ensure_ascii=False, indent=2)
    
    # 결과 출력
    print("\n" + "="*60)
    print("크롤링 완료!")
    print("="*60)
    print(f"✓ 총 수집: {total_count}개")
    print(f"✓ 파일 저장: {DOWNLOAD_FOLDER}")
    print(f"✓ JSON 저장: {JSON_OUTPUT}")
    print("\n카테고리별 수집 현황:")
    
    category_counts = {}
    for form in all_forms:
        cat = form['category']
        category_counts[cat] = category_counts.get(cat, 0) + 1
    
    for cat, count in category_counts.items():
        print(f"  - {cat}: {count}개")
    
    print("\n" + "="*60)

if __name__ == "__main__":
    main()
