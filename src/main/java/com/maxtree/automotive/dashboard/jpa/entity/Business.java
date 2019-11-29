package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "businesses")
public class Business {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 材料名称 */
    private String name;
    /** 材料编码 */
    private String code;

    @OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY)
    private List<Material> required = new ArrayList<>();

    @OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY)
    private List<Material> deviceUnits = new ArrayList<>();

    public Business() {}

    public Business(String name, String code, List<Material> required, List<Material> deviceUnits) {
        this.name = name;
        this.code = code;
        this.required = required;
        this.deviceUnits = deviceUnits;
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

    public List<Material> getRequired() {
        return required;
    }

    public void setRequired(List<Material> required) {
        this.required = required;
    }

    public List<Material> getDeviceUnits() {
        return deviceUnits;
    }

    public void setDeviceUnits(List<Material> deviceUnits) {
        this.deviceUnits = deviceUnits;
    }

    @Override
    public String toString() {
//        return "Business{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", code='" + code + '\'' +
//                ", required=" + required +
//                ", deviceUnits=" + deviceUnits +
//                '}';
        return code+"-"+name;
    }
}
