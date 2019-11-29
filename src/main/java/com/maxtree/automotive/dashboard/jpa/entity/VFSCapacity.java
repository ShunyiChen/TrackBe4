package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "vfs_capacities")
public class VFSCapacity {
    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 总字节数 */
    private Long totalBytes;
    /** 已用字节数 */
    private Long bytesUsed;
    /** 容量单位 */
    private String unit;
    /** 容量大小 */
    private Float size;
    /** 上次更新时间 */
    private String updateDateTime;

    public VFSCapacity() {}

    public VFSCapacity(Long id, Long totalBytes, Long bytesUsed, String unit, Float size, String updateDateTime) {
        this.id = id;
        this.totalBytes = totalBytes;
        this.bytesUsed = bytesUsed;
        this.unit = unit;
        this.size = size;
        this.updateDateTime = updateDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(Long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public Long getBytesUsed() {
        return bytesUsed;
    }

    public void setBytesUsed(Long bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public String toString() {
        return "VFSCapacity{" +
                "id=" + id +
                ", totalBytes=" + totalBytes +
                ", bytesUsed=" + bytesUsed +
                ", unit='" + unit + '\'' +
                ", size=" + size +
                ", updateDateTime='" + updateDateTime + '\'' +
                '}';
    }
}
