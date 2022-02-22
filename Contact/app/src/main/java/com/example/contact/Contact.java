package com.example.contact;

public class Contact {
    private int id;
    private String name;
    private String company;
    private String phoneNum;
    private int vip;

    public Contact(int id, String name, String phoneNum, String company, int vip){
        this.id = id;
        this.name = name;
        this.phoneNum = phoneNum;
        this.company = company;
        this.vip = vip;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNum() { return phoneNum; }

    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

    public String getCompany() {return company; }

    public void setCompany(String company){
        this.company = company;
    }

    public int getVip() { return vip; }

    public void setVip(int vip){
        this.vip = vip;
    }
}
