package com.example.jyn.hearhere;

import android.app.Application;

public class DataClass_room_list extends Application {

    private int broadCast_no;               // 방송 번호
    private int BJ_user_no;                 // 비제이 회원 번호
    private String BJ_nickName;             // 비제이 닉네임
    private String broadCast_title;         // 방송 제목
    private String welcome_word;            // 환영인사 멘트
    private String broadCast_img_fileName;  // 방송 이미지 파일이름
    private String broadCast_save_orNot;    // 방송 저장 여부
    private int final_guest_count;          // 최종 게스트 숫자
    private int like_count;                 // 좋아요 숫자
    private int received_report_count;      // 접수된 신고 숫자
    private String broadCast_start_time;    // 방송 시작 시간
    private String broadCast_end_time;      // 방송 종료 시간
    private String total_broadCast_time;    // 총 방송 시간
    private String broadCast_end_orNot;     // 방송 종료 여부
    private int total_broadCast_time_mil;   // 총 방송 시간 밀리세컨

    public DataClass_room_list() {}

    public DataClass_room_list(int broadCast_no, int BJ_user_no, String BJ_nickName, String broadCast_title, String welcome_word, String broadCast_img_fileName, String broadCast_save_orNot, int final_guest_count, int like_count, int received_report_count, String broadCast_start_time, String broadCast_end_time, String total_broadCast_time, String broadCast_end_orNot, int total_broadCast_time_mil) {
        this.broadCast_no = broadCast_no;
        this.BJ_user_no = BJ_user_no;
        this.BJ_nickName = BJ_nickName;
        this.broadCast_title = broadCast_title;
        this.welcome_word = welcome_word;
        this.broadCast_img_fileName = broadCast_img_fileName;
        this.broadCast_save_orNot = broadCast_save_orNot;
        this.final_guest_count = final_guest_count;
        this.like_count = like_count;
        this.received_report_count = received_report_count;
        this.broadCast_start_time = broadCast_start_time;
        this.broadCast_end_time = broadCast_end_time;
        this.total_broadCast_time = total_broadCast_time;
        this.broadCast_end_orNot = broadCast_end_orNot;
        this.total_broadCast_time_mil = total_broadCast_time_mil;
    }

    public int getBroadCast_no() {
        return broadCast_no;
    }

    public void setBroadCast_no(int broadCast_no) {
        this.broadCast_no = broadCast_no;
    }

    public int getBJ_user_no() {
        return BJ_user_no;
    }

    public void setBJ_user_no(int BJ_user_no) {
        this.BJ_user_no = BJ_user_no;
    }

    public String getBJ_nickName() {
        return BJ_nickName;
    }

    public void setBJ_nickName(String BJ_nickName) {
        this.BJ_nickName = BJ_nickName;
    }

    public String getBroadCast_title() {
        return broadCast_title;
    }

    public void setBroadCast_title(String broadCast_title) {
        this.broadCast_title = broadCast_title;
    }

    public String getWelcome_word() {
        return welcome_word;
    }

    public void setWelcome_word(String welcome_word) {
        this.welcome_word = welcome_word;
    }

    public String getBroadCast_img_fileName() {
        return broadCast_img_fileName;
    }

    public void setBroadCast_img_fileName(String broadCast_img_fileName) {
        this.broadCast_img_fileName = broadCast_img_fileName;
    }

    public String getBroadCast_save_orNot() {
        return broadCast_save_orNot;
    }

    public void setBroadCast_save_orNot(String broadCast_save_orNot) {
        this.broadCast_save_orNot = broadCast_save_orNot;
    }

    public int getFinal_guest_count() {
        return final_guest_count;
    }

    public void setFinal_guest_count(int final_guest_count) {
        this.final_guest_count = final_guest_count;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getReceived_report_count() {
        return received_report_count;
    }

    public void setReceived_report_count(int received_report_count) {
        this.received_report_count = received_report_count;
    }

    public String getBroadCast_start_time() {
        return broadCast_start_time;
    }

    public void setBroadCast_start_time(String broadCast_start_time) {
        this.broadCast_start_time = broadCast_start_time;
    }

    public String getBroadCast_end_time() {
        return broadCast_end_time;
    }

    public void setBroadCast_end_time(String broadCast_end_time) {
        this.broadCast_end_time = broadCast_end_time;
    }

    public String getTotal_broadCast_time() {
        return total_broadCast_time;
    }

    public void setTotal_broadCast_time(String total_broadCast_time) {
        this.total_broadCast_time = total_broadCast_time;
    }

    public String getBroadCast_end_orNot() {
        return broadCast_end_orNot;
    }

    public void setBroadCast_end_orNot(String broadCast_end_orNot) {
        this.broadCast_end_orNot = broadCast_end_orNot;
    }

    public int getTotal_broadCast_time_mil() {
        return total_broadCast_time_mil;
    }

    public void setTotal_broadCast_time_mil(int total_broadCast_time_mil) {
        this.total_broadCast_time_mil = total_broadCast_time_mil;
    }
}
