package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "work_stack_1")
public class Work1 {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 流程实例ID */
    private String workId;
    /** 条形码（业务流水号） */
    private String barcode;
    /** 号牌种类 */
    private String plateType;
    /** 号码号牌 */
    private String plateNumber;
    /** 车辆识别代码 */
    private String vin;
    /** 办理的业务 */
    private String business;
    /** 业务状态 */
    private String businessStatus;
    /** 户籍地 */
    private String placeOfDomicile;
    /** 站点ID */
    private Long siteId;
    /** 社区ID */
    private Long communityId;
    /** 公司ID */
    private Long companyId;
    /** 所有人 */
    private String owner;

    protected Work1() {}

    public Work1(String workId, String barcode, String plateType, String plateNumber, String vin, String business, String businessStatus, String placeOfDomicile, Long siteId, Long communityId, Long companyId, String owner) {
        this.workId = workId;
        this.barcode = barcode;
        this.plateType = plateType;
        this.plateNumber = plateNumber;
        this.vin = vin;
        this.business = business;
        this.businessStatus = businessStatus;
        this.placeOfDomicile = placeOfDomicile;
        this.siteId = siteId;
        this.communityId = communityId;
        this.companyId = companyId;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getPlaceOfDomicile() {
        return placeOfDomicile;
    }

    public void setPlaceOfDomicile(String placeOfDomicile) {
        this.placeOfDomicile = placeOfDomicile;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Work1{" +
                "id=" + id +
                ", workId='" + workId + '\'' +
                ", barcode='" + barcode + '\'' +
                ", plateType='" + plateType + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", vin='" + vin + '\'' +
                ", business='" + business + '\'' +
                ", businessStatus='" + businessStatus + '\'' +
                ", placeOfDomicile='" + placeOfDomicile + '\'' +
                ", siteId=" + siteId +
                ", communityId=" + communityId +
                ", companyId=" + companyId +
                ", owner=" + owner +
                '}';
    }
}
