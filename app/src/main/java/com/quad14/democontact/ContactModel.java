package com.quad14.democontact;

public class ContactModel {

    String Id;
    String Name;
    String Number;
    String Color;
    int FSize;
    int DataIndex;

    public ContactModel(String id, String name, String number, String color, int FSize, int dataIndex) {
        Id = id;
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
        DataIndex = dataIndex;
    }

    public ContactModel(String name, String number, String color, int FSize, int dataIndex) {
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
        DataIndex = dataIndex;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public int getDataIndex() {
        return DataIndex;
    }

    public void setDataIndex(int dataIndex) {
        DataIndex = dataIndex;
    }
}
