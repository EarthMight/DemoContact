package com.quad14.democontact;

public class ContactModel {

    String Id;
    String Name;
    String Number;
    int Color;
    int FSize;
    int DataIndex;


    public ContactModel(String id, String name, String number, int color, int FSize) {
        Id = id;
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
    }

    public ContactModel(String name, String number, int color, int FSize) {
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
    }

    public ContactModel(String name, String number) {
        Name = name;
        Number = number;
    }

    public ContactModel(int color, int FSize) {
        Color = color;
        this.FSize = FSize;
    }

    public ContactModel(String name, String number, int color, int FSize, int dataIndex) {
        Name = name;
        Number = number;
        Color = color;
        this.FSize = FSize;
        DataIndex = dataIndex;
    }

    public ContactModel(int dataIndex) {
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

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
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
