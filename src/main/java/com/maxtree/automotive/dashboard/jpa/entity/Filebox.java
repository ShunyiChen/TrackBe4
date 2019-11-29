package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "file_boxes")
public class Filebox {
    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 车辆识别代号 */
    private String vin;
    /** 上架号 */
    private String number;
    /** 单元格 */
    @ManyToOne(fetch=FetchType.LAZY)
    private Cell cell;

    public Filebox() {}

    public Filebox(String vin, String number, Cell cell) {
        this.vin = vin;
        this.number = number;
        this.cell = cell;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public String toString() {
        return "Filebox{" +
                "id=" + id +
                ", vin='" + vin + '\'' +
                ", number='" + number + '\'' +
                ", cell=" + cell +
                '}';
    }
}
