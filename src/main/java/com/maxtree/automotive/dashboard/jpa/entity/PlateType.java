package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "plate_types")
public class PlateType {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 号牌种类名称 */
    private String name;
    /** 号牌种类编码 */
    private String code;

    public PlateType() {}

    public PlateType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
//        return "Material{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", code='" + code + '\'' +
//                '}';
        return code+"-"+name;
    }
}
