package com.quad14.democontact;

import java.io.Serializable;

public class CustomDataListModel implements Serializable {

    private String id;
    String Name;
    String Number;
    String Color;
    int FSize;
    int Index;

    public CustomDataListModel(String id, String name, String number) {
        this.id = id;
        Name = name;
        Number = number;
    }

    public CustomDataListModel(String name, String number) {
        Name = name;
        Number = number;
    }

    public CustomDataListModel(String id, String name, String number, String color, int FSize) {
        this.id = id;
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
    }

    public CustomDataListModel(String name, String number, String color, int FSize) {
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
    }


    public CustomDataListModel(String name, String number, String color, int FSize, int index) {
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
        Index = index;
    }

    public CustomDataListModel(String id, String name, String number, String color, int FSize, int index) {
        this.id = id;
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
        Index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public int getFSize() {
        return FSize;
    }

    public void setFSize(int FSize) {
        this.FSize = FSize;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }
}
