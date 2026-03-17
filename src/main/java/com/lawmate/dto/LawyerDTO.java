package com.lawmate.dto;

import java.util.Date;

/**
 * 변호사 프로필 DTO (Data Transfer Object)
 */
public class LawyerDTO {

    private Long lawyerId;           // 변호사 ID
    private String name;             // 이름
    private String nameEn;           // 영문 이름
    private String barNumber;        // 변호사 등록번호
    private String email;            // 이메일
    private String phone;            // 전화번호
    private String specialty;        // 전문 분야
    private String education;        // 학력
    private String career;           // 경력
    private String profileImage;     // 프로필 이미지 경로
    private String introduction;     // 소개
    private String status;           // 상태 (ACTIVE / INACTIVE)
    private Date admissionDate;      // 변호사 등록일
    private Date createdAt;          // 등록일시
    private Date updatedAt;          // 수정일시
    private String existingRoomId;
    private boolean isConsulting;

    // 검색용 추가 필드
    private String searchKeyword;    // 검색 키워드
    private int pageNo;              // 페이지 번호
    private int pageSize;            // 페이지 크기
    private int totalCount;          // 전체 건수
    private int blockSize; // 페이지블럭의 크기 [설정]

    public LawyerDTO() {}
    public LawyerDTO(int blockSize) {}

    // ===================== Getter / Setter =====================

    public Long getLawyerId() { return lawyerId; }
    public void setLawyerId(Long lawyerId) { this.lawyerId = lawyerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }

    public String getBarNumber() { return barNumber; }
    public void setBarNumber(String barNumber) { this.barNumber = barNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getCareer() { return career; }
    public void setCareer(String career) { this.career = career; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(Date admissionDate) { this.admissionDate = admissionDate; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public String getSearchKeyword() { return searchKeyword; }
    public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }

    public int getPageNo() { return pageNo; }
    public void setPageNo(int pageNo) { this.pageNo = pageNo; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public int getBlockSize() { return blockSize; }
    public void setBlockSize(int blockSize) { this.blockSize = blockSize; }

    public boolean isConsulting() { return isConsulting; }
    public void setConsulting(boolean consulting) { isConsulting = consulting; }

    public String getExistingRoomId() {
        return existingRoomId;
    }

    public void setExistingRoomId(String existingRoomId) {
        this.existingRoomId = existingRoomId;
    }

    @Override
    public String toString() {
        return "LawyerDTO{" +
                "lawyerId=" + lawyerId +
                ", name='" + name + '\'' +
                ", barNumber='" + barNumber + '\'' +
                ", specialty='" + specialty + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
