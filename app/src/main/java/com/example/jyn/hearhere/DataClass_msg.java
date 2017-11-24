package com.example.jyn.hearhere;

import android.app.Application;

public class DataClass_msg extends Application {

    private int BroadCast_no;       // 방송 번호-
    private int User_no;            // 회원 번호-
    private String User_nicName;    // 회원 닉네임-
    private String User_img_file;   // 회원 이미지 파일이름-
    private String Msg_content;     // 메시지 내용-
    private String Msg_send_time;   // 메시지 전달 시간-
    private String Serial_orNot;    // 연속 여부-
    private String Host_orNot;      // 호스트 여부-
    private String Type;            // 메시지 타입-
    private int No;                 // 메세지 넘버링(auto increment)

    public DataClass_msg() {}

    public DataClass_msg(int broadCast_no, int user_no, String user_nicName, String user_img_file, String msg_content, String msg_send_time, String serial_orNot, String host_orNot, String type, int no) {
        BroadCast_no = broadCast_no;
        User_no = user_no;
        User_nicName = user_nicName;
        User_img_file = user_img_file;
        Msg_content = msg_content;
        Msg_send_time = msg_send_time;
        Serial_orNot = serial_orNot;
        Host_orNot = host_orNot;
        Type = type;
        No = no;
    }

    public int getBroadCast_no() {
        return BroadCast_no;
    }

    public void setBroadCast_no(int broadCast_no) {
        BroadCast_no = broadCast_no;
    }

    public int getUser_no() {
        return User_no;
    }

    public void setUser_no(int user_no) {
        User_no = user_no;
    }

    public String getUser_nicName() {
        return User_nicName;
    }

    public void setUser_nicName(String user_nicName) {
        User_nicName = user_nicName;
    }

    public String getUser_img_file() {
        return User_img_file;
    }

    public void setUser_img_file(String user_img_file) {
        User_img_file = user_img_file;
    }

    public String getMsg_content() {
        return Msg_content;
    }

    public void setMsg_content(String msg_content) {
        Msg_content = msg_content;
    }

    public String getMsg_send_time() {
        return Msg_send_time;
    }

    public void setMsg_send_time(String msg_send_time) {
        Msg_send_time = msg_send_time;
    }

    public String getSerial_orNot() {
        return Serial_orNot;
    }

    public void setSerial_orNot(String serial_orNot) {
        Serial_orNot = serial_orNot;
    }

    public String getHost_orNot() {
        return Host_orNot;
    }

    public void setHost_orNot(String host_orNot) {
        Host_orNot = host_orNot;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }
}



