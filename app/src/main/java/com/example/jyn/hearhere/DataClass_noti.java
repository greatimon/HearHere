package com.example.jyn.hearhere;

import android.app.Application;

/**
 * Created by JYN on 2017-07-31.
 */

public class DataClass_noti extends Application {

    private String Noti_type;           // 알림 타입
    private int User_no;                // 회원 번호
    private String timeStamp;           // 타임스탬프
    private int Noti_user_no;           // 알림 회원 번호
    private String Noti_user_nickName;  // 알림 회원 닉네임
    private String Noti_user_img_url;   // 알림 회원 이미지 URL
    private boolean Read;               // 읽음
    private int BroadCast_no;           // 방송 번호
    private String BroadCast_title;     // 방송 제목
    private boolean BroadCast_onAir;    // 방송 상태
    private String No;                  // noti 번호(PK)

    DataClass_noti() {}

    public DataClass_noti(String noti_type, int user_no, String timeStamp, int noti_user_no, String noti_user_nickName, String noti_user_img_url, boolean read, int broadCast_no, String broadCast_title, boolean broadCast_onAir, String no) {
        Noti_type = noti_type;
        User_no = user_no;
        this.timeStamp = timeStamp;
        Noti_user_no = noti_user_no;
        Noti_user_nickName = noti_user_nickName;
        Noti_user_img_url = noti_user_img_url;
        Read = read;
        BroadCast_no = broadCast_no;
        BroadCast_title = broadCast_title;
        BroadCast_onAir = broadCast_onAir;
        No = no;
    }

    public String getNoti_type() {
        return Noti_type;
    }

    public void setNoti_type(String noti_type) {
        Noti_type = noti_type;
    }

    public int getUser_no() {
        return User_no;
    }

    public void setUser_no(int user_no) {
        User_no = user_no;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNoti_user_no() {
        return Noti_user_no;
    }

    public void setNoti_user_no(int noti_user_no) {
        Noti_user_no = noti_user_no;
    }

    public String getNoti_user_nickName() {
        return Noti_user_nickName;
    }

    public void setNoti_user_nickName(String noti_user_nickName) {
        Noti_user_nickName = noti_user_nickName;
    }

    public String getNoti_user_img_url() {
        return Noti_user_img_url;
    }

    public void setNoti_user_img_url(String noti_user_img_url) {
        Noti_user_img_url = noti_user_img_url;
    }

    public boolean isRead() {
        return Read;
    }

    public void setRead(boolean read) {
        Read = read;
    }

    public int getBroadCast_no() {
        return BroadCast_no;
    }

    public void setBroadCast_no(int broadCast_no) {
        BroadCast_no = broadCast_no;
    }

    public String getBroadCast_title() {
        return BroadCast_title;
    }

    public void setBroadCast_title(String broadCast_title) {
        BroadCast_title = broadCast_title;
    }

    public boolean isBroadCast_onAir() {
        return BroadCast_onAir;
    }

    public void setBroadCast_onAir(boolean broadCast_onAir) {
        BroadCast_onAir = broadCast_onAir;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }
}