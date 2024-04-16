package com.example.newEcom.model;

public class CategoryModel {
    private String name, icon, color, brief;
    private int categoryId;

    public CategoryModel() {
    }

    public CategoryModel(String name, String icon, String color, String brief, int categoryId) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.brief = brief;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
