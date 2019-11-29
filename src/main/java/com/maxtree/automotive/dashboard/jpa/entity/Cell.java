package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "cells")
public class Cell {
    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 单元格名称 */
    private String name;
    /** 行坐标 */
    private int x;
    /** 纵坐标 */
    private int y;
    /** 密集架 */
    @ManyToOne(fetch=FetchType.LAZY)
    private Rack rack;

    public Cell() {}

    public Cell(String name, int x, int y, Rack rack) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.rack = rack;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", rack=" + rack +
                '}';
    }
}
