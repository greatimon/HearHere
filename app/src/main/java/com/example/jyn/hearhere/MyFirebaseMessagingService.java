package com.example.jyn.hearhere;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    static String broadCast_no="";
    static String fcm_user_no="";

    private static final String TAG = "MyFirebaseMsgService";

    AsyncTask_insert_noti task;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload
        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().get("message"));
        }
        // Check if message contains a notification payload
        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }


    private void sendNotification(String messageBody) {

        // 설정시간대 FCM 체킹 변수
        boolean setting_time_check_result = false;
        // 라이브 FCM 체킹 변수
        boolean Fcm_live_check_result = false;
        // Fan 추가 FCM 체킹 변수
        boolean Fcm_Fan_check_result = false;

        Log.d(TAG, "messageBody: " + messageBody);

        String[] after_split = messageBody.split("팀노바3기전용남");
        String noti_type = after_split[0];
        String message = after_split[1];
        String send_user_no = after_split[3];

        if(noti_type.equals("broadCast")) {
            broadCast_no = after_split[2];
        }
        Log.d(TAG, "message: " + message);
        Log.d(TAG, "broadCast_no: " + broadCast_no);



        // 현재 액티비티 정보 가져오기
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        ComponentName cn = info.get(0).topActivity;

        String packageName = cn.getPackageName();
        String activityName = cn.getShortClassName().substring(1);
        Log.d(TAG, "top_activity_ packageName: " + packageName);
        Log.d(TAG, "top_activity_ activityName: " + activityName);

        // 쉐어드에서 user_no 가져오기
        SharedPreferences fireBase_token_shared = getSharedPreferences("fireBase_token", MODE_PRIVATE);
        fcm_user_no = fireBase_token_shared.getString("token_user_no", "");


        /** DB에 noti 저장하기 */
        task = new AsyncTask_insert_noti();
        String fcm_no = "";
        try {
            String temp = task.execute(noti_type, fcm_user_no, send_user_no, broadCast_no).get();
            Log.d(TAG, "insert_noti_noti_type: " + noti_type);
            Log.d(TAG, "insert_noti_fcm_user_no: " + fcm_user_no);
            Log.d(TAG, "insert_noti_send_user_no: " + send_user_no);
            Log.d(TAG, "insert_noti_broadCast_no: " + broadCast_no);
            Log.d(TAG, "insert_noti_result: " + temp);

            if(noti_type.equals("broadCast")) {
                String[] after_split_insert_noti = temp.split("&");
                fcm_no = after_split_insert_noti[1];
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        /** **********************************************************
         * Notification 보내기 => 쉐어드에서 notification 설정 확인하기_ (총 3단계)
         * ***********************************************************/

        /******************************* 1. TIME_sharedPreference check FOR notification ******************************/
        SharedPreferences Fcm_time_shared = getSharedPreferences("Fcm_time", MODE_PRIVATE);

        String Fcm_Time_filter = Fcm_time_shared.getString("Fcm_time", "false");
        if(Fcm_Time_filter.equals("false")) {
            setting_time_check_result = false;
        }
        if(Fcm_Time_filter.equals("true")) {
            String Fcm_start_Time_hour = Fcm_time_shared.getString("start_hour", "22");
            String Fcm_start_Time_min = Fcm_time_shared.getString("start_min", "0");
            String Fcm_end_Time_hour = Fcm_time_shared.getString("end_hour", "8");
            String Fcm_end_Time_min = Fcm_time_shared.getString("end_min", "0");

            // 현재시간을 msec 으로 구한다.
            long now = System.currentTimeMillis();
            // 현재시간을 date 변수에 저장한다.
            Date date = new Date(now);
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+09:00"));
            // nowDate 변수에 값을 저장한다.
            String time = sdf.format(date);
            Log.d("TImePicker_time", "time: " + time);

            // 1. 현재시간 "HH:mm" 형식으로 to Millis
            Date time_dt = null;
            try {
                time_dt = new SimpleDateFormat("HH:mm").parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long time_long = time_dt.getTime();
            Log.d("TImePicker_time", "time_long: " + time_long);

            // 2. Start_ 설정시간 "HH:mm" 형식으로 to Millis
            String Fcm_start_time = Fcm_start_Time_hour + ":" + Fcm_start_Time_min;
            Date setting_start_time_dt = null;
            try {
                setting_start_time_dt = new SimpleDateFormat("HH:mm").parse(Fcm_start_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long start_time_long = setting_start_time_dt.getTime();
            Log.d("TImePicker_time", "start_time_long: " + start_time_long);

            // 3. End_ 설정시간 "HH:mm" 형식으로 to Millis
            String Fcm_end_time = Fcm_end_Time_hour + ":" + Fcm_end_Time_min;
            Date setting_end_time_dt = null;
            try {
                setting_end_time_dt = new SimpleDateFormat("HH:mm").parse(Fcm_end_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long end_time_long = setting_end_time_dt.getTime();
            Log.d("TImePicker_time", "end_time_long: " + end_time_long);

            // 4. 비교
            // ex> 22:00 ~ 08:00
            if(start_time_long > end_time_long) {
                if(time_long < start_time_long && time_long > end_time_long) {
                    Log.d("TImePicker_time", "설정시간에 해당!!");
                    setting_time_check_result = true;
                }
                else {
                    Log.d("TImePicker_time", "설정시간에 해당되지 않음!!");
                    setting_time_check_result = false;
                }
            }
            // ex> 01:00 ~ 08:00
            if(start_time_long < end_time_long) {
                if(time_long > start_time_long && time_long < end_time_long) {
                    Log.d("TImePicker_time", "설정시간에 해당!!");
                    setting_time_check_result = true;
                }
                else {
                    Log.d("TImePicker_time", "설정시간에 해당되지 않음!!");
                    setting_time_check_result = false;
                }
            }
        }
        Log.d("notification_check", "1. setting_time_check_result: " + setting_time_check_result);

        /******************************* 1. TIME_sharedPreference check FOR notification ******************************/


        /******************************* 2. LIVE_sharedPreference check FOR notification ******************************/
        SharedPreferences Fcm_live_shared = getSharedPreferences("Fcm_live", MODE_PRIVATE);
        String Fcm_live_Accept = Fcm_live_shared.getString("Fcm_live", "true");
        Log.d("live_FCM", "Fcm_live_Accept: " + Fcm_live_Accept);

        if(Fcm_live_Accept.equals("true")) {
            Fcm_live_check_result = true;
        }
        if(Fcm_live_Accept.equals("false")) {
            Fcm_live_check_result = false;
        }
        Log.d("notification_check", "2. Fcm_live_check_result: " + Fcm_live_check_result);
        /******************************* 2. LIVE_sharedPreference check FOR notification ******************************/


        /******************************* 3. FAN_sharedPreference check FOR notification ******************************/
        SharedPreferences Fcm_Fan_shared = getSharedPreferences("Fcm_Fan", MODE_PRIVATE);
        String Fcm_Fan_Accept = Fcm_Fan_shared.getString("Fcm_Fan", "true");
        Log.d("Fan_FCM", "Fcm_Fan_Accept: " + Fcm_Fan_Accept);

        if(Fcm_Fan_Accept.equals("true")) {
            Fcm_Fan_check_result = true;
        }
        if(Fcm_Fan_Accept.equals("false")) {
            Fcm_Fan_check_result = false;
        }
        Log.d("notification_check", "3. Fcm_Fan_check_result: " + Fcm_Fan_check_result);
        /******************************* 3. FAN_sharedPreference check FOR notification ******************************/


        // 앱이 foreGround에 있을 때
        if(packageName.equals("com.example.jyn.hearhere") && !setting_time_check_result) {
            Log.d(TAG, "com.example.jyn.hearhere 로직 들어옴");

            if(noti_type.equals("broadCast")) {
                if(!Fcm_live_check_result) {
                    Log.d("notification_check_2", "live- false 로 걸려서 fcm 발송하지 않음");
                    return;
                }
                Log.d("notification_check_2", "live- true 여서 fcm 발송함");
            }

            if(noti_type.equals("fan")) {
                if(!Fcm_Fan_check_result) {
                    Log.d("notification_check_3", "fan- false 로 걸려서 fcm 발송하지 않음");
                    return;
                }
                Log.d("notification_check_3", "fan- true 여서 fcm 발송함");
            }

            Intent intent = null;

            intent = new Intent(this, a_main_after_login.class);
            intent.putExtra("fcm_user_no", fcm_user_no);
            intent.putExtra("fcm_broadCast_no", broadCast_no);
            intent.putExtra("fcm_no", fcm_no);

            String ticker_msg = "";

            // 방송알림 노티일 때
            if(noti_type.equals("broadCast")) {
                intent.putExtra("type", "live");
                ticker_msg = "HearHere, 방송 알림이 도착하였습니다";
            }
            // Fan 추가 노티일 때
            if(noti_type.equals("fan")) {
                intent.putExtra("type", "noti");
                ticker_msg = "HearHere, 당신의 Fan이 생겼습니다.";
            }
            intent.putExtra("fcm_orNot", "true");
            Log.d(TAG, "fcm_user_no: " + fcm_user_no);
            Log.d(TAG, "broadCast_no: " + broadCast_no);
            Log.d(TAG, "live: live");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

            // notification id 중복되지 않게 하기
            SharedPreferences notification_id = getSharedPreferences("notification_id", MODE_PRIVATE);
            SharedPreferences.Editor notification_id_edit = notification_id.edit();
            int notification_id_int = notification_id.getInt("notification_id", 1);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, notification_id_int/* Request code*/, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getNotificationIcon())
                    .setColor(Color.parseColor("#FFB300"))
                    .setContentTitle("HearHere 라디오")
                    .setTicker(ticker_msg)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notificationBuilder.setPriority(Notification.PRIORITY_MAX); //헤드업 알림

            NotificationManager notificationManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);



            notificationManager.notify(notification_id_int /* ID of notification */, notificationBuilder.build());
            Log.d("notification", "notification_id: " + notification_id_int);
            notification_id_int++;
            notification_id_edit.putInt("notification_id", notification_id_int).apply();


        }

        // 앱이 backGround에 있을 때
        if(!packageName.equals("com.example.jyn.hearhere") && !setting_time_check_result) {
            Log.d(TAG, "com.example.jyn.hearhere 로직 아님!!");

            if(noti_type.equals("broadCast")) {
                if(!Fcm_live_check_result) {
                    Log.d("notification_check_2", "live- false 로 걸려서 fcm 발송하지 않음");
                    return;
                }
                Log.d("notification_check_2", "live- true 여서 fcm 발송함");
            }

            if(noti_type.equals("fan")) {
                if(!Fcm_Fan_check_result) {
                    Log.d("notification_check_3", "fan- false 로 걸려서 fcm 발송하지 않음");
                    return;
                }
                Log.d("notification_check_3", "fan- true 여서 fcm 발송함");
            }

            Intent intent = null;

            intent = new Intent(this, a_main_before_login.class);
            intent.putExtra("fcm_user_no", fcm_user_no);
            intent.putExtra("fcm_broadCast_no", broadCast_no);
            intent.putExtra("fcm_no", fcm_no);

            String ticker_msg = "";

            // 방송알림 노티일 때
            if(noti_type.equals("broadCast")) {
                intent.putExtra("type", "live");
                ticker_msg = "HearHere, 방송 알림이 도착하였습니다";
            }
            // Fan 추가 노티일 때
            if(noti_type.equals("fan")) {
                intent.putExtra("type", "noti");
                ticker_msg = "HearHere, 당신의 Fan이 생겼습니다.";
            }
            intent.putExtra("fcm_orNot", "true");
            Log.d(TAG, "fcm_user_no: " + fcm_user_no);
            Log.d(TAG, "broadCast_no: " + broadCast_no);
            Log.d(TAG, "live: live");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

            // notification id 중복되지 않게 하기
            SharedPreferences notification_id = getSharedPreferences("notification_id", MODE_PRIVATE);
            SharedPreferences.Editor notification_id_edit = notification_id.edit();
            int notification_id_int = notification_id.getInt("notification_id", 1);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, notification_id_int/* Request code*/, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(getNotificationIcon())
                    .setColor(Color.parseColor("#FFB300"))
                    .setContentTitle("HearHere 라디오")
                    .setTicker(ticker_msg)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notificationBuilder.setPriority(Notification.PRIORITY_MAX); //헤드업 알림

            NotificationManager notificationManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);



            notificationManager.notify(notification_id_int /* ID of notification */, notificationBuilder.build());
            Log.d("notification", "notification_id: " + notification_id_int);
            notification_id_int++;
            notification_id_edit.putInt("notification_id", notification_id_int).apply();
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.fcm1 : R.mipmap.launcher;
    }

}
