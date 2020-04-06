package com.example.maplefi.ui;

public class Apinfo {
    String ssid = "";
    String pwEncType = "wpa";//와이파이 패스워드 암호화 type
    String packetEncRule = "";//ex) ccmp
    String packetEncType = ""; //ex)eap
    int rssi = 0;
    int grade = 0;//AP 보안성 점수

    //set ap info
    public void setSsid(String ssid){
        this.ssid = ssid;
    }

    public void setPwEncType(String pwEncType){
        this.packetEncType = pwEncType;
    }

    public void setProtocolEncType(String packetEncRule) {
        this.packetEncRule = packetEncRule;
    }

    public void setPacketEncType(String packetEncType){
        this.packetEncType = packetEncType;
    }

    public void setRssi(int rssi){
        this.rssi = rssi;
    }

    public void setGradeZero() {
        this.grade = 0;
    }

    //get apinfo
    public String getSsid(){
        return ssid;
    }

    public String getPwEncType(){
        return pwEncType;
    }

    public String getProtocolEncType() {
        return packetEncRule;
    }

    public String getPacketEncType(){
        return packetEncType;
    }

    public int getRssi(){
        return rssi;
    }

    //점수
    public void addGrade(int grade){
        this.grade += grade;
    }

    public int getGrade(){
        return this.grade;
    }

}
