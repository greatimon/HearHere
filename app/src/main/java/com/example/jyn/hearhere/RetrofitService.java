package com.example.jyn.hearhere;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("/{url}")
    /** 검색액티비티 - 검색결과 가져오기 */
    Call<ResponseBody> get_search_result (
        @Path("url") String url,
        @Field(value = "search_word_str", encoded=true) String search_word_str,
        @Field(value = "user_no", encoded=true) String user_no
    );


    @FormUrlEncoded
    @POST("/{url}")
    /** 방송 중, 좋아요 누른사람 닉네임 가져오기 */
    Call<ResponseBody> get_nickName_who_clicked_like (
        @Path("url") String url,
        @Field(value = "like_clicked_user_no", encoded=true) String like_clicked_user_no
    );


    @FormUrlEncoded
    @POST("/{url}")
    /** 강퇴 유저 추가 */
    Call<ResponseBody> insert_red_card_user (
            @Path("url") String url, //broadCast_no, red_card_user_no
            @Field(value = "broadCast_no", encoded=true) String broadCast_no,
            @Field(value = "red_card_user_no", encoded=true) String red_card_user_no
    );


    @FormUrlEncoded
    @POST("/{url}")
    /** 현재 방송 참여중인지 아닌지 확인하기 */
    Call<ResponseBody> in_broadCast_orNot (
            @Path("url") String url,
            @Field(value = "user_no", encoded=true) String user_no
    );


    @FormUrlEncoded
    @POST("/{url}")
    /** 내 방송인지 아닌지 확인하기 -- 캐스트모드 */
    Call<ResponseBody> is_this_my_cast (
            @Path("url") String url,
            @Field(value = "user_no", encoded=true) String user_no,
            @Field(value = "broadCast_no", encoded=true) String broadCast_no
    );

    @FormUrlEncoded
    @POST("/{url}")
    /** 내 방송 삭제하기 -- 캐스트모드 */
    Call<ResponseBody> delete_my_cast (
            @Path("url") String url,
            @Field(value = "broadCast_no", encoded=true) String broadCast_no
    );

}
