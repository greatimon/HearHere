package com.example.jyn.hearhere;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Get_presentTime {

    static public String present_time() {

        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // nowDate 변수에 값을 저장한다.
        String today_string = sdf.format(date);

        return today_string;
    }
}
