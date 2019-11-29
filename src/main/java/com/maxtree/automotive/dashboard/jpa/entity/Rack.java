package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "racks")
public class Rack {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 密集架名称 */
    private String name;
    /** 库房 */
    @ManyToOne(fetch=FetchType.LAZY)
    private Storeroom storeroom;

    public Rack() {}

    public Rack(String name, Storeroom storeroom) {
        this.name = name;
        this.storeroom = storeroom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Storeroom getStoreroom() {
        return storeroom;
    }

    public void setStoreroom(Storeroom storeroom) {
        this.storeroom = storeroom;
    }

    @Override
    public String toString() {
        return "Rack{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", storeroom=" + storeroom +
                '}';
    }
}
