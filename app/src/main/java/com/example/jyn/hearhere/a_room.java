package com.example.jyn.hearhere;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import io.antmedia.android.broadcaster.ILiveVideoBroadcaster;
import io.antmedia.android.broadcaster.LiveVideoBroadcaster;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class a_room extends AppCompatActivity implements IVLCVout.Callback {

    ImageView host_profile_img, like_img, report_img, deleteCast_img, host_out_img, show_guests_img, broadCast_background_img, imageView3_img, present_guests_icon_img;
    EditText send_msg_ET;
    TextView BJ_nic_TV, title_TV, send_btn_TV, total_guests_TV, like_count_TV, present_guests_TV, live_broadCast_time_TV;
    TextView test_divider_for_cast_TV, total_broadCast_time_TV;
    View sound_ctl_background_V, progressWheel_cover_V;
    LinearLayout sound_on_Lin, sound_off_Lin, sound_ctl_bar_Lin, message_input_Lin, seekbar_Lin, test_layout_Lin;
    RelativeLayout sound_ctl_bar_layout_Rel, test_layout_2_Lin;
    View heart_left_space_for_cast, test_layout_3_V;

    // 좋아요 클릭 관련 서피스뷰 변수
    private SurfaceView_Panel panel;
    static int x, y;
    ViewGroup root;

    static long animation_start_milTime = 0;
    static Handler surfaceview_handler;
    boolean ball_insert_check = false;
    Random random;

    int X_half_width;
    int X_till_imageView;
    int Y_start_position;
    // 좋아요 클릭 관련 서피스뷰 변수

    Handler check_my_cast_handler;

    int like = 0;
    int sound_status = 0;
    final int REQUEST_OUT=1000;
    final int REQUEST_NETWORK_DISCONNECT=2000;
    final int REQUEST_FORCE_TO_OUT=1234;
    static int REQUEST_OUT_FROM_CAST=1023;
    static int REQUEST_ALREADY_END_BROADCAST=9797;
    static int REQUEST_GET_OUT_RED=12341;

    ListView listView;
    Adapter_msg adapter_msg;
    ArrayList<DataClass_msg> msg_aryLi;
    ArrayList<Long> msg_time_long;

    JSONArray jArray_cast;

    String title="";
    String welcome_word="";
    String comment="";
    String save="";
    String nickName="";
    String user_no="";
    String my_img_fileName;
    String BJ_img_fileName ="";
    String broadCast_img_fileName="";
    String broadCast_no="";
    String host_orNot="";
    String BJ_nickName="";
    static String from="";
    long onAir_Elapsed_time_mil;
    String onAir_Elapsed_start_time;
    String onAir_Elapsed_time;
    String total_guests="";
    String like_count="";
    String present_guests="";
    String didn_i_click_like="";
    String broadCast_start_time="";
    String total_broadCast_time="";
    String like_clicked_user_nickName="";

    String received_serial_orNot,
            received_host_orNot,
            received_user_no,
            received_nickName,
            received_user_img_fileName,
            received_msg_content,
            received_msg_send_time,
            received_broadCast_no,
            received_msg_type,
            received_msg_no;


    String received_previous_user_no = "";

    static Handler force_to_out_handler;
    int red_card_user_no;
    String red_card_user_nickName;
    String insert_red_card_user_result;

    Socket socket;
    final String ip = "115.71.232.211";
    final int port = 7777;
    private BufferedReader inMsg;
    private PrintWriter outMsg;
    private boolean status;
    private Thread thread;

    AsyncTask_delete_room task;
    String get_cast_task_result;
    static final String TAG_JSON = "cast";
    static final String TAG_JSON_2 = "unreceived_msg";

    BroadcastReceiver broadcastReceiver;
    String network_status;

    AsyncTask_connect_retry task_2;

    Chronometer timeElapsed;

    Thread mute_delay;
    Thread progressDialog_thread;
    Thread t;

    static long received_ping_time;
    ProgressDialog progressDialog;
    private ProgressWheel progressWheel;

    int REQUEST_GET_PHOTO_FROM_ALBUM = 4000;
    int REQUEST_SELECT_PHOTO_FROM_PREVIEW = 4001;
    PermissionListener permissionListener;
    String photoPath_on_myPhone;
    String upload_img_url_result;

    /******************************************************************************/
    boolean mIsRecording = false;
    private Intent mLiveVideoBroadcasterServiceIntent;
    private GLSurfaceView mGLView;
    private ILiveVideoBroadcaster mLiveVideoBroadcaster;
    private Button mBroadcastControlButton;

    public static final String RTMP_BASE_URL = "rtmp://115.71.232.211/live/";
//    public static final String RTMP_BASE_URL = "rtmp://115.68.230.195/live/";

    /** Defines callbacks for service binding, passed to bindService() */
    // startService() 실행 시, 호출되는 콜백 메소드
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.d("streaming","onServiceConnected");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LiveVideoBroadcaster.LocalBinder binder = (LiveVideoBroadcaster.LocalBinder) service;
            mLiveVideoBroadcaster = binder.getService();
            mLiveVideoBroadcaster.init(a_room.this, mGLView);
            mLiveVideoBroadcaster.setAdaptiveStreaming(true);
            mLiveVideoBroadcaster.openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            broadCastctl_btn_clkicked();
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("streaming","onServiceDisconnected");
            mLiveVideoBroadcaster = null;
        }
    };

    public void broadCastctl_btn_clkicked() {
        if(host_orNot.equals("true") && from.equals("live")) {
            mBroadcastControlButton.performClick();
        }
    }

    ////////////////////////////////////// 캐스트 플레이어 //////////////////////////////////////

    private boolean onPause_check = false;

    /** live */
    static final String CAST_AUDIO_PATH = "http://115.71.232.211/cast/";

    public final static String TAG = "player";
    private String mFilePath;
    private SurfaceView mSurface;
    private SurfaceHolder holder;
    private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    private int mVideoWidth;
    private int mVideoHeight;

    // 음소거 로직 테스트
    AudioManager audioManager;
    int oldAudioMode;

    /** cast */
    static final String CAST_AUDIO_BASIC_PATH = "http://115.71.232.211/cast/";
    String AUDIO_PATH;
    ImageView play_IV, pause_IV;
    TextView cast_playTime_TV;

    // 미디어 플레이어 객체
    private android.media.MediaPlayer mp;

    // 재생을 멈춘 시점
    private int pos = 0;
    private int sb_pos = 0;

    // pause를 눌렀는지 확인할 변수
    boolean pause_clicked = false;

    // 최초 재생을 확인할 변수
    int check_start = 0;

    long time_flow = 0;
    long broadCast_start_time_mil;
    int j = 0;
    boolean time_run = true;
    Thread time;
    Thread cast_timeLapse;
    int default_index;

    ////////////////////////////////////// 시크바 컨트롤러 //////////////////////////////////////
    // 유저가 클릭한 시크바 포지션
    int user_seekbar_clicked_position;

    // 캐스트 플레이어 시크바
    SeekBar sb;
    // 재생중인지 확인하는 변수
    boolean isPlaying = false;

    MyThread seekBar_thread;

    /** 미디어리코드로 따로 녹음하는 코드- 시작 */
//    private MediaRecorder mRecorder;
//    private File mOutputFile;
    /** 미디어리코드로 따로 녹음하는 코드- 끝 */

    /**---------------------------------------------------------------------------
     클래스 ==> MyThread -- 캐스트용 시크바 스레드
     ---------------------------------------------------------------------------*/
    // 시크바 조금씩 움직이기 (재생중에는 무한 반복)
    private class MyThread extends Thread  {
        int i =0;
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); //volume은 0~15 사이어야 함
        boolean gotMute_before = false;
        boolean resume_volume = false;


        @Override
        public void run() throws IllegalStateException {
            Log.d("check_volume", "volume: " + volume);

            while(isPlaying) {
                try {
                    if(i==0) {
                        sb.setProgress(sb_pos);
                        Log.d("pause_play", "MyThread_ sb_pos: " + sb_pos);
                        i++;
                    }

                    while(sb_pos>mp.getCurrentPosition()) {
                        if(!gotMute_before) {
                            if(sb_pos-mp.getCurrentPosition() > 300) {  // 그 갭이 0.3초 이상이라면,
                                handler.sendEmptyMessage(12);   // 프로그레스 휠 보여주기
                            }
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND);
                            Log.d("check_volume", "[[[ MUTE ]]]");
                            gotMute_before = true;
                        }
                        sb.setProgress(sb_pos);
                        Log.d("pause_play", "MyThread_ sb_pos>>>>>>>>>>mp.getCurrentPosition()");
                        Log.d("pause_play", "MyThread_ mp.getCurrentPosition(): " + mp.getCurrentPosition());
                        Log.d("pause_play", "MyThread_ sb_pos: " + sb_pos);
                    }

                    if(sb_pos<=mp.getCurrentPosition()) {
                        sb.setProgress(mp.getCurrentPosition());
                    }

                    if(gotMute_before && !resume_volume) {
                        handler.sendEmptyMessage(13);   // 프로그레스 휠 가리기
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
                        Log.d("check_volume", "[[[ RESUME_VOLUME ]]]");
                        resume_volume = true;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        }
    }
    /******************************************************************************/

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_room);

        // 핸들러 객체 생성 - 강퇴 시키기
        force_to_out_handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.what == 0) {
                    red_card_user_no = msg.arg1;
                    red_card_user_nickName = (String)msg.obj;
                    Log.d("강퇴", String.valueOf(red_card_user_no));
                    Log.d("강퇴", red_card_user_nickName);

                    send_data("out", "red_card");
                    handler.removeMessages(0);
                }
            }
        };

        /** 화면에 켜진 상태를 유지하는 플래그 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //  오디오 매니저 객체 생성
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        // 볼륨이 5이하면 13으로 셋팅하기
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(volume <=0 ) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 13, AudioManager.FLAG_PLAY_SOUND);
        }

        // intent 정보 넘겨 받기
        Intent intent = getIntent();

        // 퍼미션 리스너(테드_ 라이브러리)
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(a_profile.this, "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(a_profile.this, "권한 거부", Toast.LENGTH_SHORT).show();
            }
        };

        // 인텐트 정보 받기
        broadCast_no = intent.getExtras().getString("BROADCAST_NO");
        user_no = intent.getExtras().getString("USER_NO");
        nickName = intent.getExtras().getString("NICKNAME");
        host_orNot = intent.getExtras().getString("HOST_ORNOT");
        BJ_img_fileName = intent.getExtras().getString("BJ_IMG_FILENAME");
        my_img_fileName = intent.getExtras().getString("MY_IMG_FILENAME");
        broadCast_img_fileName = intent.getExtras().getString("BROADCAST_IMG_FILENAME");
        from = intent.getExtras().getString("FROM");
        Log.d("broadCast_info", "broadCast_no: "+ broadCast_no);
        Log.d("broadCast_info", "user_no: "+ user_no);
        Log.d("broadCast_info", "nickName: "+ nickName);
        Log.d("broadCast_info", "host_orNot: "+ host_orNot);
        Log.d("broadCast_info", "BJ_IMG_FILENAME: "+ BJ_img_fileName);
        Log.d("broadCast_info", "my_img_fileName: "+ my_img_fileName);
        Log.d("broadCast_info", "broadCast_img_fileName: "+ broadCast_img_fileName);
        Log.d("broadCast_info", "from: "+ from);

        if(host_orNot.equals("true")) {
            title = intent.getExtras().getString("TITLE");
            comment = intent.getExtras().getString("COMMENT");
            welcome_word = comment;
            BJ_nickName = nickName;
            save = intent.getExtras().getString("SAVE");
        }
        if(host_orNot.equals("false")) {
            title = intent.getExtras().getString("BROADCAST_TITLE");
            welcome_word = intent.getExtras().getString("WELCOME_WORD");
            BJ_nickName = intent.getExtras().getString("BJ_NICKNAME");
            like_count = intent.getExtras().getString("LIKE_COUNT");
            present_guests = intent.getExtras().getString("PRESENT_GUEST_COUNT");
            total_guests = intent.getExtras().getString("TOTAL_GUEST_COUNT");
            didn_i_click_like = intent.getExtras().getString("DIDN_I_CLICK_LIKE");
        }


        /* 상태바 안 보이게 하는법- 안쓰려고 함*/
//        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
//        getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;

        /******************************************************************************/
        /** LiveVideoBroadcaster 서비스 돌리는 로직 */
        // 라이브 방송이고, 내가 방장일 때 방송서비스 start
        if(from.equals("live") && host_orNot.equals("true")) {
            //binding on resume not to having leaked service connection
            mLiveVideoBroadcasterServiceIntent = new Intent(this, LiveVideoBroadcaster.class);
            //this makes service do its job until done
            startService(mLiveVideoBroadcasterServiceIntent);
            Log.d("streaming","startService");
            /** 미디어리코드로 따로 녹음하는 코드- 시작 */
//            startRecording();
            /** 미디어리코드로 따로 녹음하는 코드- 끝 */


            /** openGL 관련 로직 - 패스 */
            // Configure the GLSurfaceView.  This will start the Renderer thread, with an
            // appropriate EGL activity.
            mGLView = (GLSurfaceView) findViewById(R.id.cameraPreview_surfaceView);
            if (mGLView != null) {
                mGLView.setEGLContextClientVersion(2);     // select GLES 2.0
            }
            mGLView.setVisibility(View.GONE);
        }
        mBroadcastControlButton = (Button)findViewById(R.id.toggle_broadcasting);


        ////////////////////////////////////// 플레이어 //////////////////////////////////////

        // title의 공백을 없앤 후, utf-8로 인코딩 처리
        String utf8 = null;
        try {
            utf8 = new String(title.trim().replace(" ", "").getBytes("euc-kr"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /** live */
        mFilePath = RTMP_BASE_URL + utf8;

        Log.d(TAG, "Playing: " + mFilePath);
        mSurface = (SurfaceView) findViewById(R.id.surface);
        holder = mSurface.getHolder();

        /** cast */
        /** 원래 코드 */
        AUDIO_PATH = CAST_AUDIO_BASIC_PATH + utf8 + ".mp4";
        /** 재생테스트용 코드: URL - 노래 */
//        AUDIO_PATH = CAST_AUDIO_BASIC_PATH + "4.mp3";

        Log.d(TAG, "AUDIO_PATH: " + AUDIO_PATH);

        /******************************************************************************/


        host_profile_img = (ImageView)findViewById(R.id.host_profile_img);
        like_img = (ImageView)findViewById(R.id.like);
        sound_ctl_background_V = (View)findViewById(R.id.sound_ctl_background);
        sound_on_Lin = (LinearLayout)findViewById(R.id.sound_on);
        sound_off_Lin = (LinearLayout)findViewById(R.id.sound_off);
        listView = (ListView)findViewById(R.id.listView);
        BJ_nic_TV = (TextView)findViewById(R.id.BJ_nic);
        title_TV = (TextView)findViewById(R.id.title);
        report_img = (ImageView)findViewById(R.id.report);
        deleteCast_img = (ImageView)findViewById(R.id.delete_cast);
        send_msg_ET = (EditText)findViewById(R.id.send_msg);
        host_out_img = (ImageView)findViewById(R.id.host_out);
        sound_ctl_bar_Lin = (LinearLayout)findViewById(R.id.sound_ctl_bar);
        send_btn_TV = (TextView)findViewById(R.id.send_btn);
        show_guests_img = (ImageView)findViewById(R.id.show_guests);
        broadCast_background_img = (ImageView)findViewById(R.id.broadCast_background_img);
        sound_ctl_bar_layout_Rel = (RelativeLayout)findViewById(R.id.sound_ctl_bar_layout);
        message_input_Lin = (LinearLayout)findViewById(R.id.message_input_layout);
        total_guests_TV = (TextView)findViewById(R.id.total_guests);
        like_count_TV = (TextView)findViewById(R.id.like_count);
        present_guests_TV = (TextView)findViewById(R.id.present_guests);
        imageView3_img = (ImageView)findViewById(R.id.imageView3);
        present_guests_icon_img = (ImageView)findViewById(R.id.present_guests_icon);
        timeElapsed = (Chronometer)findViewById(R.id.chronometer);
        live_broadCast_time_TV = (TextView)findViewById(R.id.live_broadCast_time);
        seekbar_Lin = (LinearLayout)findViewById(R.id.seekbar_outline);
        test_divider_for_cast_TV = (TextView)findViewById(R.id.test_divider_for_cast);
        total_broadCast_time_TV = (TextView)findViewById(R.id.total_broadCast_time);
        heart_left_space_for_cast = (View)findViewById(R.id.heart_left_space_for_cast);
        sb = (SeekBar)findViewById(R.id.seekBar);
        play_IV = (ImageView)findViewById(R.id.play);
        pause_IV = (ImageView)findViewById(R.id.pause);
        cast_playTime_TV = (TextView)findViewById(R.id.cast_playTime);
        test_layout_Lin = (LinearLayout)findViewById(R.id.test_layout);
        test_layout_2_Lin = (RelativeLayout)findViewById(R.id.test_layout_2);
        test_layout_3_V = (View)findViewById(R.id.test_layout_3);
        progressWheel_cover_V = (View)findViewById(R.id.progressWheel_cover);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        progressWheel.setSpinSpeed(0.7f);
        progressWheel.setBarWidth(10);
        progressWheel.setVisibility(View.GONE);


        if(BJ_img_fileName.equals("none")) {
            // BJ 이미지, 디폴트 이미지로 넣기
            Glide
                    .with(this)
                    .load(R.drawable.default_profile)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(host_profile_img);
        }
        if(!BJ_img_fileName.equals("none")) {
            if(BJ_img_fileName.contains("http")) {
                // BJ 이미지, 해당 서비스 웹서버로부터 다운 받아 넣기
                Glide
                        .with(this)
                        .load(BJ_img_fileName)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(host_profile_img);
            }
            if(!BJ_img_fileName.contains("http")) {
                // BJ 이미지, 서버로부터 다운 받아 넣기
                Glide
                        .with(this)
                        .load(Static.SERVER_URL_PROFILE_FOLDER + BJ_img_fileName)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(host_profile_img);
            }
        }

        // 방 배경 이미지, 서버로부터 다운 받아 넣기
        if(!broadCast_img_fileName.equals("none")) {
            Glide
                    .with(this)
                    .load(Static.SERVER_URL_BROADCAST_IMG_FOLDER + broadCast_img_fileName)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(broadCast_background_img);
        }
        else if(broadCast_img_fileName.equals("none")) {
            Glide
                    .with(this)
                    .load(R.drawable.headphone2)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(broadCast_background_img);
        }
        // 방 제목 넣기
        title_TV.setText(title);

        // 좋아요수, 총 게스트수, 현재 게스트수 넣기
        if(!like_count.equals("") && !present_guests.equals("") && !total_guests.equals("")) {
            like_count_TV.setText(like_count);
            // 호스트 수 빼기(-1)
            int present_guests_subtract_host = Integer.parseInt(present_guests) - 1;
            present_guests_TV.setText(String.valueOf(present_guests_subtract_host));
            int total_guests_subtract_host = Integer.parseInt(total_guests) - 1;
            total_guests_TV.setText(String.valueOf(total_guests_subtract_host));
        }


        /** 라이브 방송으로 들어올 때 */
        if(from.equals("live")) {
            if(host_orNot.equals("true")) {
                report_img.setVisibility(View.GONE);
                host_out_img.setVisibility(View.VISIBLE);
                BJ_nic_TV.setText(nickName);
                sound_on_Lin.setVisibility(View.VISIBLE);
                sound_ctl_bar_Lin.setClickable(true);
                like_img.setVisibility(View.GONE);
                show_guests_img.setVisibility(View.VISIBLE);
//            sound_ctl_bar_Lin.setVisibility(View.VISIBLE);

            }
            if(host_orNot.equals("false")) {
                report_img.setVisibility(View.VISIBLE);
                host_out_img.setVisibility(View.GONE);
                BJ_nic_TV.setText(BJ_nickName);
                sound_on_Lin.setVisibility(View.GONE);
                sound_ctl_bar_Lin.setClickable(false);
                like_img.setVisibility(View.VISIBLE);
                show_guests_img.setVisibility(View.GONE);
//            sound_ctl_bar_Lin.setVisibility(View.GONE);

                // 이전에 좋아요 눌렀었는지 확인하는 변수 값 기준으로, 좋아요 누르기 활성화 or 비활성화
                if(didn_i_click_like.equals("false")) {
                    like_img.setClickable(true);
                }
                if(didn_i_click_like.equals("true")) {
//                    like_img.setClickable(false); // Surface 테스트
                    like_img.setImageResource(R.drawable.like_clicked);
                }
            }

            msg_aryLi = new ArrayList<DataClass_msg>();

//            adapter_msg = new Adapter_msg(a_room.this, msg_aryLi);
//            adapter_msg.notifyDataSetChanged();
//            listView.setAdapter(adapter_msg);

            /**---------------------------------------------------------------------------
             메소드실행 ==> TCP 연결
             ---------------------------------------------------------------------------*/
            send_data("enter", "none");

            /** 채팅 메세지 입력에 따른 '전송'버튼 활성화 여부_ 텍스트왓쳐 */
            send_msg_ET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int length = send_msg_ET.getText().length();
                    String check_1 = send_msg_ET.getText().toString();

                    String check_2 = check_1.replace(" ", "");
                    boolean check_3 = check_2.replace("\n","").equals("");

                    if(length==0) {
                        send_btn_TV.setClickable(false);
                        send_btn_TV.setTextColor(Color.parseColor("#999999"));
                    }
                    if(length>0) {
                        if(check_3) {
                            send_btn_TV.setClickable(false);
                            send_btn_TV.setTextColor(Color.parseColor("#999999"));
                        }
                        else if(!check_3) {
                            send_btn_TV.setClickable(true);
                            send_btn_TV.setTextColor(Color.parseColor("#FF8F00"));
                        }
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        /** 캐스트 방송으로 들어올 때 */
        if(from.equals("cast")) {
//            sound_ctl_bar_layout_Rel.setVisibility(View.GONE);
//            host_out_img.setVisibility(View.GONE);
//            show_guests_img.setVisibility(View.GONE);
//            like_img.setVisibility(View.GONE);
//            message_input_Lin.setVisibility(View.GONE);

            // 핸들러 객체 생성 - 캐스트 모드일 때 내 캐스트인지 확인
            check_my_cast_handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 내 캐스트일 때
                    if(msg.what == 0){
                        Log.d("내캐스트", "YES_ 내 캐스트임!");
                        report_img.setVisibility(View.GONE);
                        deleteCast_img.setVisibility(View.VISIBLE);
                    }
                    // 내 캐스트가 아닐 때
                    else if(msg.what == 1) {
                        Log.d("내캐스트", "NO_ 내 캐스트 아님!");
                        report_img.setVisibility(View.VISIBLE);
                        deleteCast_img.setVisibility(View.GONE);
                    }
                }
            };

            // 캐스트 방송일 때, 내 방송이면 report 버튼 숨기고, delete_cast 버튼 보여주기
            // 하아.. 내 방송인지 아닌지 또 확인해야 하는건가....
            is_this_my_cast();

//            report_img.setVisibility(View.VISIBLE);
            host_out_img.setVisibility(View.GONE);
            BJ_nic_TV.setText(BJ_nickName);
            sound_on_Lin.setVisibility(View.GONE);
            sound_ctl_bar_Lin.setClickable(false);
            like_img.setVisibility(View.GONE);
            show_guests_img.setVisibility(View.GONE);
            message_input_Lin.setVisibility(View.GONE);
            imageView3_img.setVisibility(View.GONE);
            present_guests_icon_img.setVisibility(View.GONE);
            present_guests_TV.setVisibility(View.GONE);
            seekbar_Lin.setVisibility(View.VISIBLE);
            heart_left_space_for_cast.setVisibility(View.VISIBLE);
            timeElapsed.setVisibility(View.GONE);

            msg_aryLi = new ArrayList<>();
            msg_time_long = new ArrayList<>();

            // 방송시작 시간 가져오기
            broadCast_start_time = intent.getExtras().getString("BROADCAST_START_TIME");
            total_broadCast_time = intent.getExtras().getString("TOTAL_BROADCAST_TIME");
//

            // 캐스트 정보 가져와서 msg_aryLi에 일단 넣어만 놓고,
            // Msg_send_time 따로 빼서 msg_time_long 배열에 넣기
            try {
                get_cast_task_result = new AsyncTask_get_cast(a_room.this).execute(broadCast_no).get();
//                Log.d("AsyncTask_get_cast", get_cast_task_result);

                try {
                    JSONObject jObject_cast = new JSONObject(get_cast_task_result);
                    Log.d("AsyncTask_get_cast", "jObject_cast.length(): "+String.valueOf(jObject_cast.length()));

                    jArray_cast = jObject_cast.getJSONArray(TAG_JSON);
                    Log.d("AsyncTask_get_cast", "jArray_cast.length(): "+String.valueOf(jArray_cast.length()));

//                    msgList_aryLi.clear();

                    // 방송 시작시간 가져와서 long type으로 변환
                    try {
                        Date date_start_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(broadCast_start_time);
                        broadCast_start_time_mil = date_start_time.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    /**---------------------------------------------------------------------------
                     jArray_cast 담긴 JSONObject를 하나씩 가져와,
                     그 안에서 Msg_send_time을 따로 빼서 msg_time_long 배열에 넣는다
                     ---------------------------------------------------------------------------*/
                    for (int i=0; i<jArray_cast.length(); i++) {
                        JSONObject jGroup_cast = jArray_cast.getJSONObject(i);

                        String msg_object_json = jGroup_cast.getString("msg_object_json");
                        JSONObject jsonObject_msg = new JSONObject(msg_object_json);

                        String Msg_send_time = jsonObject_msg.getString("Msg_send_time");
                        String Type = jsonObject_msg.getString("Type");

                        // Msg_send_time를 Data객체화
                        Date temp_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(Msg_send_time);
                        // Data 객체에서 getTime()으로 밀리세컨드로 변환하여 msg_time_long에 차곡차곡 넣음
//                        msg_time_long.add(temp_time.getTime());
                        msg_time_long.add(temp_time.getTime() - broadCast_start_time_mil);
                        Log.d("msg_time_long", "Type: " + Type);
                        Log.d("msg_time_long", "Msg_send_time: " + Msg_send_time);
                        Log.d("msg_time_long", "msg_time_long 개수: " + String.valueOf(msg_time_long.size()));
                        Log.d("msg_time_long", String.valueOf(msg_time_long.get(i)));
                    }

                } catch (JSONException e) {
                    Log.d("AsyncTask_get_cast", "JSONException: " + e);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // 여기에 캐스트 프로그래스 다이얼로그 띄우기
            progressDialog_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(7);
                }
            });
            progressDialog_thread.start();

        }

        /** 리스트뷰 아이템 클릭리스너 (프로필 이미지뷰) - v_profile 다이얼로그 액티비티 팝업*/
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.sender_profile_img:
                        String clicked_user_no = v.getTag(R.string.user_no).toString();
                        Log.d("listView_clicked", "clicked_user_no: " + clicked_user_no);

                        if(clicked_user_no.equals("0")) {
                            // 클릭한 item이 BJ 인사말인 경우임
                            try {
                                String BJ_user_no_str = new AsyncTask_get_BJ_user_no().execute(broadCast_no).get();
                                clicked_user_no = BJ_user_no_str;
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                        Intent v_profile_intent = new Intent(a_room.this, v_profile.class);
                        v_profile_intent.putExtra("REQUEST_FROM", "a_room");
                        v_profile_intent.putExtra("CLICKED_USER_NO", clicked_user_no);
                        v_profile_intent.putExtra("MY_USER_NO", user_no);
                        v_profile_intent.putExtra("HOST_ORNOT", host_orNot);
                        v_profile_intent.putExtra("BROADCAST_NO", broadCast_no);
                        startActivity(v_profile_intent);
                        break;
                    case R.id.send_img:
                        String clicked_send_img = v.getTag(R.string.send_img_url).toString();
                        String clicked_send_user_nickName = v.getTag(R.string.send_user_nickName).toString();
                        String clicked_send_user_profile_img_url = v.getTag(R.string.send_user_profile_img_url).toString();
                        Log.d("listView_clicked", "clicked_send_img: " + clicked_send_img);
                        Log.d("listView_clicked", "clicked_send_user_nickName: " + clicked_send_user_nickName);
                        Log.d("listView_clicked", "clicked_send_user_profile_img_url: " + clicked_send_user_profile_img_url);

                        Intent intent = new Intent(a_room.this, a_send_img.class);
                        intent.putExtra("send_img", clicked_send_img);
                        intent.putExtra("send_user_nickName", clicked_send_user_nickName);
                        intent.putExtra("send_user_profile_img_url", clicked_send_user_profile_img_url);
                        startActivity(intent);
                }
            }
        };

        adapter_msg = new Adapter_msg(a_room.this, msg_aryLi, mOnClickListener);
        adapter_msg.notifyDataSetChanged();
        listView.setAdapter(adapter_msg);

        // 브로드캐스트 인텐트 필터 설정
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        // 브로드캐스트 리시버 설정
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                network_status = NetworkUtil.getConnectivityStatusString(context);
//                Log.d("TCP", "network_status: " + network_status);
//                Toast.makeText(getBaseContext(), "network_status: "+network_status, Toast.LENGTH_SHORT).show();
            }
        };
        // 동적 브로드캐스트 리시버 등록
        registerReceiver(broadcastReceiver, intentFilter);

        // 방송시간 뷰 GONE 처리(시작되기 전까지)
        live_broadCast_time_TV.setVisibility(View.VISIBLE);
        timeElapsed.setVisibility(View.GONE);

        // 좋아요 클릭 관련 서피스뷰 로직 - 시작
        random = new Random();

        root = (ViewGroup)findViewById(R.id.root);

        add_surfaceView();

        surfaceview_handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    if (ball_insert_check) {
                        Log.d("surfaceView_log", "핸들러 '1' 메시지 전달 받음");
                        delete_surfaceView();
                        ball_insert_check = false;
                    }
                }
            }
        };
        // 좋아요 클릭 관련 서피스뷰 로직 - 끝
    }

    /** 미디어리코드로 따로 녹음하는 코드- 시작 */
//    private void startRecording() {
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
//            mRecorder.setAudioEncodingBitRate(48000);
//        } else {
//            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//            mRecorder.setAudioEncodingBitRate(64000);
//        }
//        mRecorder.setAudioSamplingRate(44100);
//        mOutputFile = getOutputFile();
//        mOutputFile.getParentFile().mkdirs();
//        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());
//
//        try {
//            mRecorder.prepare();
//            mRecorder.start();
//            Log.d("Voice Recorder","started recording to "+mOutputFile.getAbsolutePath());
//        } catch (IOException e) {
//            Log.e("Voice Recorder", "prepare() failed "+e.getMessage());
//        }
//    }
//
//    protected  void stopRecording(boolean saveFile) {
//        mRecorder.stop();
//        mRecorder.release();
//        mRecorder = null;
//        if (!saveFile && mOutputFile != null) {
//            mOutputFile.delete();
//        }
//    }
//
//    private File getOutputFile() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.KOREA);
//        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
//                + "/Voice Recorder/RECORDING_"
//                + dateFormat.format(new Date())
//                + ".m4a");
//    }
    /** 미디어리코드로 따로 녹음하는 코드- 끝 */

    /******************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("streaming", "onStart");
        // 라이브 방송이고, 내가 방장일 때 방송서비스 bind, onPause를 거치면 Pass~!
        if(host_orNot.equals("true") && from.equals("live") && !onPause_check) {

            /** 방송전 프로그레스 휠 띄우기 테스팅 코드 */
            progressWheel_cover_V.setVisibility(View.VISIBLE);
            progressWheel.setVisibility(View.VISIBLE);
            /** 방송전 프로그레스 휠 띄우기 테스팅 코드 */

            //this lets activity bind,
            /** 세번째 생성자 '0' 의미: 옵션 없음 */
            /** 세번째 생성자의 다른 옵션들: BIND_AUTO_CREATE, BIND_DEBUG_UNBIND, BIND_NOT_FOREGROUND */
            bindService(mLiveVideoBroadcasterServiceIntent, mConnection, 0);
            Log.d("streaming", "bindService");
        }

        // 라이브 방송이고, 내가 게스트일때, onPause를 거치면 Pass~!
        if(from.equals("live") && host_orNot.equals("false") && !onPause_check) {

            // 현재 방송중인 라이브 방송인지, 강퇴유저인지 확인
            String get_broadCast_end_orNot = "";
            String red_user_orNot = "";
            try {
                String temp_AsyncTask_result = new AsyncTask_broadCast_end_orNot().execute(broadCast_no, a_main_after_login.user_no).get();
                Log.d("강퇴", "AsyncTask_broadCast_end_orNot_result: " + temp_AsyncTask_result);
                String[] result_split = temp_AsyncTask_result.split("&");

                get_broadCast_end_orNot = result_split[0];
                red_user_orNot = result_split[1];

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // 현재 방송중이고, 강퇴유저가 아닐 때 player 재생
            if(get_broadCast_end_orNot.equals("false") && red_user_orNot.equals("not_red")) {
                createPlayer(mFilePath);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("streaming", "onResume");

        // 캐스트 방송일 때 player 재생, onPause를 거치면 Pass~!
        if(from.equals("cast") && !onPause_check) {

            playAudio(AUDIO_PATH);

            /** MediaPlayer 이벤트 리스너 등록 */
            mp.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(android.media.MediaPlayer mp) {
                    Log.d("player", "setOnPreparedListener_onCompletion");

                    killMediaPlayer();
                    timeElapsed.stop();

                    if(from.equals("cast")) {
                        // 다시 듣기(캐스트)가 종료 된 후, 종료 알림 팝업 액티비티 띄우기
                        Intent intent = new Intent(getBaseContext(), v_end_broadcast.class);
                        intent.putExtra("BJ_out", "false");
                        intent.putExtra("cast", "true");
                        startActivityForResult(intent, REQUEST_FORCE_TO_OUT);
                    }
                }
            });

            mp.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener(){
                @Override public void onPrepared(android.media.MediaPlayer player) {
                    Log.d("player", "setOnPreparedListener_onPrepared");
                    //            player.start();
                    //
                    //            // 시크바 스레드 시작
                    //            isPlaying = true;
                    //            new MyThread().start();

                    if(check_start==0) {
                        if(from.equals("cast")) {
                            // 프로그레스다이얼로그 내리기
                            progressDialog_thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(8);
                                }
                            });
                            progressDialog_thread.setDaemon(true);
                            progressDialog_thread.start();
                        }

                        // 채팅 더하는 스레드 로직 시작
                        add_chat_thread(0);

                        // 타임랩스 표시 - 00:00 부터 표시 / 전체방송 시간 표시
                        if (from.equals("cast")) {
                            cast_timeLapse();
                        }
                    }
                    check_start++;
                }
            });


            // 시크바 이벤트 리스너 등록
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.d("seekbar", "StartTrackingTouch");

                    // 일시정지
                    mp.pause();
                    // 시크바 스레드 종료
                    isPlaying = false;
                    seekBar_thread = null;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.d("seekbar", "StopTrackingTouch");

                    // 재생 중일 때
                    if(pause_IV.getVisibility() == View.VISIBLE) {
                        // 사용자가 움직여 놓은 위치 저장
                        sb_pos = seekBar.getProgress();
                        Log.d("pause_play", "onStopTrackingTouch_seekBar.getProgress(): " + sb_pos);

                        // 플레이어 재생 위치 재지정 및 재생 시작
                        mp.seekTo(sb_pos);

                        // 시크바 스레드 재시작
                        isPlaying = true;
                        seekBar_thread = new MyThread();
                        seekBar_thread.start();
//                        new MyThread().start();

                        // 시크바 위치 재지정
//                        sb.setProgress(pos);

                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mp.start();
                        cast_timeLapse();

                        // 채팅 추가 스레드 다시 시작
                        add_chat_thread(sb_pos);
                    }

                    // 일시정지 중일 때
                    if(play_IV.getVisibility() == View.VISIBLE) {
                        // 사용자가 움직여 놓은 위치 저장
                        sb_pos = seekBar.getProgress();
                        // 시크바 위치 재지정
                        sb.setProgress(sb_pos);

                        // 이동한 위치의 time 표시하기
                        String total_broadCast_time_check = total_broadCast_time.substring(0,2);

                        SimpleDateFormat time = null;
                        if(total_broadCast_time_check.equals("00")) {
                            time = new SimpleDateFormat("mm:ss");
                        }
                        if(!total_broadCast_time_check.equals("00")) {
                            time = new SimpleDateFormat("HH:mm:ss");
                        }
                        time.setTimeZone(TimeZone.getTimeZone("UTC"));

                        cast_playTime_TV.setText(time.format(sb_pos));
                    }

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(seekBar.getMax() == progress) {
                        Log.d("ProgressChanged", "재생 끝");
                        // 시크바 스레드 종료
                        cast_timeLapse = null;
                        isPlaying = false;
                        seekBar_thread = null;
                        //            mp.stop();
                    }
                }
            });
        }

//        // 좋아요 클릭 관련 서피스뷰 로직 - 시작
//        X_half_width = root.getWidth()/2;
//        X_till_imageView = X_half_width + (X_half_width-like_img.getWidth());
//
//        Y_start_position = root.getHeight() + like_img.getHeight() + like_img.getHeight()/2;
//        // 좋아요 클릭 관련 서피스뷰 로직 - 끝
    }


    /**---------------------------------------------------------------------------
     메소드 ==> add_chat_thread -- player_currentPosition 에 따라 채팅목록들을 추가하는 메소드
     ---------------------------------------------------------------------------*/
    public void add_chat_thread(int player_currentPosition) throws IllegalStateException {
        Log.d("check_msg_add", "add_player_currentPosition: " + player_currentPosition);

        int this_player_currentPosition = -1;

        if(player_currentPosition == 0) {
            this_player_currentPosition = player_currentPosition+ 20;
            // 20은 여유시간, 초반에 0 -> 4 -> 0 과 같이 msg_time_long 값이 들어가는 경우가 있었음
        }
        if(player_currentPosition != 0) {
            this_player_currentPosition = player_currentPosition;
        }

//        msg_aryLi.clear();

        default_index = -1;

        for(int i=0; i<msg_time_long.size(); i++) {
            if(msg_time_long.get(i)-2000 < this_player_currentPosition) { // 음성과 채팅의 딜레이가 약 2초라, 강제로 2000밀리세컨을 앞당겨서 채팅을 뿌림
                Log.d("check_msg_add", "msg_time_long.get(i) <<<<<<< player_currentPosition");
                Log.d("check_msg_add", "i: " + i);
                Log.d("check_msg_add", "msg_time_long.get(i): " + msg_time_long.get(i));
            }
            if(msg_time_long.get(i)-2000 >= this_player_currentPosition) { // 음성과 채팅의 딜레이가 약 2초라, 강제로 2000밀리세컨을 앞당겨서 채팅을 뿌림
                Log.d("demo_test", "msg_time_long.get(i) >=>=>=>=>=>=>= player_currentPosition");
                Log.d("demo_test", "i: " + i);
                Log.d("demo_test", "msg_time_long.get(i): " + msg_time_long.get(i));

                if(default_index == -1) {
                    default_index = i;
                    Log.d("demo_test", "default_index: " + default_index);
                }

                // arraylist에 아이템 add -- 핸들러 sendMessage
                Message msg_1 = handler.obtainMessage();
                msg_1.what = 11;
                msg_1.arg1 = i-1;
                handler.sendMessage(msg_1);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            }
        }


        // 무한 반복문 -- time_flow와 비교해서 채팅 아이템 추가하기
        t = new Thread(new Runnable() {
            @Override
            public void run() throws IllegalStateException {
                boolean have_chat_msg = true;
                while (isPlaying && have_chat_msg) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(isPlaying && mp != null && default_index!=-1) {
                        while(msg_time_long.get(default_index) - mp.getCurrentPosition()-2000 < 200 && t !=null) { // 음성과 채팅의 딜레이가 약 2초라, 강제로 2000밀리세컨을 앞당겨서 채팅을 뿌림
                            Log.d("msg_time_long", "start_index: " + default_index);
//                            Log.d("msg_time_long", "time_flow: " + String.valueOf(time_flow));
                            Log.d("msg_time_long", "msg_time_from_broadCast_start: " + msg_time_long.get(default_index));
                            Log.d("msg_time_long", "onChronometerTick_ mp.getCurrentPosition(): " + mp.getCurrentPosition());
                            Log.d("msg_time_long", "gap: " + (msg_time_long.get(default_index) - mp.getCurrentPosition()));

                            // arraylist에 아이템 add
                            Message msg = handler.obtainMessage();
                            msg.what = 9;
                            msg.arg1 = default_index;
                            handler.sendMessage(msg);
                            if(default_index < msg_time_long.size() - 1) {
                                default_index++;
                            }
                            if(default_index == msg_time_long.size() - 1) {
                                have_chat_msg = false;
                                break;
                            }
                        }
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();

//        // 무한 반복문 -- time_flow와 비교해서 채팅 아이템 추가하기
//        t = new Thread(new Runnable() {
//            public void run() {
//                while (!(t.isInterrupted())) {
//
//                    // => 타임스레드가 10밀리 세컨단위로 동작하기 때문에,
//                    // 혹시 msg_time_long의 다음 array값의 time 차이값이 또 10보다 작다면 다시 timeLapse_add_chat_msg() 호출
//                    // 당연히 msg_time_long의 다음 array 값이 존재할때를 조건으로 잡았으며, 혹시 그 다음것도 있을수도 있으니 while문으로
//                    // 무한반복 시킴
//
//                    //                                for(int j=0; j<msg_time_long.size(); j++) {
//                    //                                    long temp = msg_time_long.get(j);
//                    //                                    Log.d("msg_time_long", "msg_time_long[" + String.valueOf(j) + "]: " + String.valueOf(temp));
//                    //                                }
//
//                    // time 차이 값이 혹시 0이거나 0보다 작으면 바로 timeLapse_add_chat_msg() 호출
//                    // 방송시작시간보다 메세지 보낸시간이 더 빠르다는게 논리적으로 말이 안되지만
//                    // 내가 로직을 잘못 짰는지 실제 타임을 확인해보니 그런 경우가 있더라
//                    // 그래서 이 if문을 둠
//                    while(msg_time_long.get(j) - broadCast_start_time_mil <= 0) {
//                        long temp = msg_time_long.get(j) - broadCast_start_time_mil;
//                        Log.d("msg_time_long ", "j: " + String.valueOf(j));
//                        Log.d("msg_time_long", "time_gap <= 0: " + String.valueOf(temp));
//                        Log.d("msg_time_long", "time_flow: " + String.valueOf(time_flow));
//
//                        // arraylist에 아이템 add -- 핸들러 sendMessage
//                        Message msg_1 = handler.obtainMessage();
//                        msg_1.what = 9;
//                        msg_1.arg1 = j;
//                        handler.sendMessage(msg_1);
//
//                        if(j < msg_time_long.size() - 1) {
//                            j++;
//                        }
//                        if(j == msg_time_long.size() - 1) {
//                            t.interrupt();
//                        }
//                    }
//
//                    if(msg_time_long.get(j) - broadCast_start_time_mil > 0) {
////                        while((msg_time_long.get(j) - broadCast_start_time_mil) - time_flow < 200) {
//                        while((msg_time_long.get(j) - broadCast_start_time_mil) - mp.getCurrentPosition() < 200) {
//
//                            long temp = msg_time_long.get(j) - broadCast_start_time_mil;
//                            Log.d("msg_time_long", "j: " + String.valueOf(j));
//                            Log.d("msg_time_long", "time_gap > 0: " + String.valueOf(temp));
//                            Log.d("msg_time_long", "time_flow: " + String.valueOf(time_flow));
//                            Log.d("msg_time_long", "gap: " + String.valueOf((msg_time_long.get(j) - broadCast_start_time_mil) - time_flow));
//                            Log.d("msg_time_long", "onChronometerTick_ mp.getCurrentPosition(): " + mp.getCurrentPosition());
//                            Log.d("msg_time_long", "플레이어 시간과 time_gap의 시간 차이: " + (mp.getCurrentPosition()-temp));
//
//                            // arraylist에 아이템 add
//                            Message msg = handler.obtainMessage();
//                            msg.what = 9;
//                            msg.arg1 = j;
//                            handler.sendMessage(msg);
//
//                            if(j < msg_time_long.size() - 1) {
//                                j++;
//                            }
//                            if(j == msg_time_long.size() - 1) {
//                                t.interrupt();
//                            }
//                        }
//                    }
//                }
//            }
//        });
//        t.setDaemon(true);
//        t.start();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> cast_timelapse -- cast 일 때, 재생시간 표시
     ---------------------------------------------------------------------------*/
    public void cast_timeLapse() { //mp.getCurrentPosition()

        cast_timeLapse = new Thread() {
            public void run() {
                while(mp != null && mp.isPlaying()) {
                    cast_playTime_TV.post(new Runnable() {
                        public void run() {

                            String total_broadCast_time_check = total_broadCast_time.substring(0,2);

                            SimpleDateFormat time = null;
                            if(total_broadCast_time_check.equals("00")) {
                                time = new SimpleDateFormat("mm:ss");
                            }
                            if(!total_broadCast_time_check.equals("00")) {
                                time = new SimpleDateFormat("HH:mm:ss");
                            }
                            time.setTimeZone(TimeZone.getTimeZone("UTC"));

                            cast_playTime_TV.setText(time.format(mp.getCurrentPosition()));
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        cast_timeLapse.setDaemon(true);
        cast_timeLapse.start();

        // 처음 재생시에만
        if(check_start == 0) {

            // 캐스트 재생시간(밀리세컨드)을 HH:mm:ss 형식으로 변환한다
            SimpleDateFormat format_for_display = new SimpleDateFormat("HH:mm:ss");
            format_for_display.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date total_play_time = new Date(mp.getDuration());
            String total_play_time_for_display = format_for_display.format(total_play_time);
            Log.d("total_play_time", "total_play_time: " + total_play_time_for_display);

            String check_HOUR = total_play_time_for_display.substring(0,2);
            Log.d("broadCast_info", "total_broadCast_time: "+ total_broadCast_time);

            // 방송 시작되고 1초 뒤에 전체방송시간 visible하기 -- 핸들러 sendMessage
            // 크로노미터가 00:01부터 보이기 때문에 인위적으로 visible 되는 타이밍을 sync 하기 위해
            Message msg = handler.obtainMessage();
            msg.what = 10;

            if(check_HOUR.equals("00")) {
                Log.d("broadCast_info", "after_total_broadCast_time_check: "+ total_broadCast_time.substring(3,8));
                msg.obj = total_play_time_for_display.substring(3,8);
                handler.sendMessage(msg);
            }
            if(!check_HOUR.equals("00")) {
                msg.obj = total_play_time_for_display;
                handler.sendMessage(msg);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("streaming", "onPause");
        onPause_check = true;


    }

    @Override
    protected void onStop() {
        super.onStop();

        // 좋아요 클릭 관련 서피스뷰 로직 - 시작
        if(panel.thread != null) {
            panel.thread.stopSafely();
            delete_surfaceView();
        }
        // 좋아요 클릭 관련 서피스뷰 로직 - 끝
    }

    /**---------------------------------------------------------------------------
     메소드 ==> 좋아요 서피스뷰 addVIew
     ---------------------------------------------------------------------------*/
    public void add_surfaceView() {
        Log.d("surfaceView_log", "add_surfaceView 메소드 들어옴");
        panel = new SurfaceView_Panel(this);
        SurfaceHolder holder = panel.getHolder();

        /** SurfaceView 투명처리 하려고 추가한 코드 */
        panel.setZOrderOnTop(true); // necessary
        holder.setFormat(PixelFormat.TRANSLUCENT);
        /** SurfaceView 투명처리 하려고 추가한 코드 */

        panel.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        Log.d("surfaceView_log", "서피스뷰 추가 전, getChildCount: " + root.getChildCount());
        root.addView(panel, 0);
        Log.d("surfaceView_log", "서피스뷰 추가 후, getChildCount: " + root.getChildCount());
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 좋아요 서피스뷰 removeView
     ---------------------------------------------------------------------------*/
    public void delete_surfaceView() {
        Log.d("surfaceView_log", "delete_surfaceView 메소드 들어옴");
        root.removeAllViews();
        Log.d("surfaceView_log", "서피스뷰 삭제 후, getChildCount: " + root.getChildCount());

        add_surfaceView();
    }



    /** 방송 시작, 정지 버튼(토글) */
    public void toggleBroadcasting(View v) {
        if (!mIsRecording)
        {
            if (mLiveVideoBroadcaster != null) {
                Log.d("streaming", "toggleBroadcasting clicked");
                /** mLiveVideoBroadcaster 객체가 존재하지만 연결중이 아닐 때 EditText에 있는 방송 제목을 가져와
                 방송을 시작한다(AsyncTask)*/
                if (!mLiveVideoBroadcaster.isConnected()) {

                    // title의 공백을 없앤 후, utf-8로 인코딩 처리
                    String utf8 = null;
                    try {
                        utf8 = new String(title.trim().replace(" ", "").getBytes("euc-kr"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    mFilePath = RTMP_BASE_URL + utf8;

//                String streamName = title.trim().replace(" ", "");
//                Log.d("streaming", "title: "+title);

                    String streamName = utf8;
                    Log.d("streaming", "title: "+utf8);

                    Log.d("streaming", RTMP_BASE_URL + streamName);

                    final String finalUtf = utf8;
                    new AsyncTask<String, String, Boolean>() {

                        ContentLoadingProgressBar progressBar;

                        @Override
                        protected void onPreExecute() {
                            progressBar = new ContentLoadingProgressBar(a_room.this);
                            progressBar.show();
                        }

                        @Override
                        protected Boolean doInBackground(String... url) {
                            Log.d("streaming", "startBroadcasting?");

                            return mLiveVideoBroadcaster.startBroadcasting(url[0]);
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            progressBar.hide();
                            mIsRecording = result;
                            if (result) {

                            }
                            else {
                                triggerStopRecording();
                            }
                        }
//                }.execute(RTMP_BASE_URL + streamName);
                    }.execute(RTMP_BASE_URL + utf8);
                }
                else {
//                Snackbar.make(mRootView, R.string.streaming_not_finished, Snackbar.LENGTH_LONG).show();
                }
            }
            else {
//            Snackbar.make(mRootView, R.string.oopps_shouldnt_happen, Snackbar.LENGTH_LONG).show();
            }
        }
        //
        else
        {
            triggerStopRecording();
        }

    }



    public void triggerStopRecording() {
        if (mIsRecording) {
            mLiveVideoBroadcaster.stopBroadcasting();
        }

        mIsRecording = false;
    }

    ////////////////////////////////////// 플레이어 //////////////////////////////////////
    /** live */
    private void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        if (holder == null || mSurface == null)
            return;

        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        holder.setFixedSize(mVideoWidth, mVideoHeight);
        ViewGroup.LayoutParams lp = mSurface.getLayoutParams();
        lp.width = w;
        lp.height = h;
        mSurface.setLayoutParams(lp);
        mSurface.invalidate();
    }

    private void createPlayer(String media) {
        releasePlayer();
        try {
            if (media.length() > 0) {
                // RTMP 주소 토스트
//            Toast toast = Toast.makeText(this, media, Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
//                    0);
//            toast.show();
            }

            // Create LibVLC
            // TODO: make this more robust, and sync with audio demo
            ArrayList<String> options = new ArrayList<String>();
            //options.add("--subsdec-encoding <encoding>");
            options.add("--aout=opensles");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            libvlc = new LibVLC(this, options);
            holder.setKeepScreenOn(true);

            // Creating media player org.videolan.libvlc.media.MediaPlayer
            mMediaPlayer = new MediaPlayer(libvlc);
//            mMediaPlayer.setEventListener(mPlayerListener);

            // Seting up video output
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mSurface);
            //vout.setSubtitlesView(mSurfaceSubtitles);
            vout.addCallback(this);
            vout.attachViews();

            Media m = new Media(libvlc, Uri.parse(media));
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in creating player!", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        holder = null;
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    @Override
    public void onNewLayout(IVLCVout vout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        if (width * height == 0)
            return;

        // store video size
        mVideoWidth = width;
        mVideoHeight = height;
        setSize(mVideoWidth, mVideoHeight);
    }

    @Override
    public void onSurfacesCreated(IVLCVout vout) {}

    @Override
    public void onSurfacesDestroyed(IVLCVout vout) {}

    @Override
    public void onHardwareAccelerationError(IVLCVout vlcVout) {
        Log.e(TAG, "Error with hardware acceleration");
        this.releasePlayer();
        Toast.makeText(this, "Error with hardware acceleration", Toast.LENGTH_LONG).show();
    }

/** cast */
    /**---------------------------------------------------------------------------
     메소드 ==> 캐스트일 때 -- 최초 재생 시작하기
     ---------------------------------------------------------------------------*/
    private void playAudio(String url) {
        killMediaPlayer();

        try {
            sb.setProgress(0);

            mp = new android.media.MediaPlayer();
            mp.reset();
            mp.setDataSource(url);
            mp.setLooping(false);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.start();
            Log.d("pause_play", "mp.start(): " + mp.getCurrentPosition());

            // 캐스트 재생시간
            int a = mp.getDuration();
            Log.d("pause_play", "mp.getDuration(): " + mp.getDuration());
            // 시크바의 최대 범위를 캐스트의 재생시간으로 설정
            sb.setMax(a);
            // 시크바 스레드 시작
            isPlaying = true;
            seekBar_thread = new MyThread();
            seekBar_thread.start();
//            new MyThread().start();

            if(mp.isPlaying()) {
                play_IV.setVisibility(View.GONE);
                pause_IV.setVisibility(View.VISIBLE);
            }
            else {
                play_IV.setVisibility(View.VISIBLE);
                pause_IV.setVisibility(View.GONE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 캐스트일 때 -- 플레이어 죽이기
     ---------------------------------------------------------------------------*/
    private void killMediaPlayer() {
        if(mp != null) {
            try {
                mp.stop();
                mp.prepare();
                // MediaPlayer 해제
                mp.release();
                mp = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(mp == null) {
            Log.d("메소드", "killMediaPlayer_null");
        }
        if(mp != null) {
            Log.d("메소드", "killMediaPlayer_NOT NULL");
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 캐스트일 때 -- 재생 재개
     ---------------------------------------------------------------------------*/
    public void play_clicked(View view) throws InterruptedException {
        // MediaPlayer 객체가 존재하고, 현재 재생중이 아닐 때
        if(mp != null && !mp.isPlaying()) {

            Thread.sleep(300);

            play_IV.setVisibility(View.GONE);
            pause_IV.setVisibility(View.VISIBLE);

            // 일시정지 직전 저장된 position 값으로 셋팅
//            mp.seekTo(pos);
//            Log.d("pause_play", "play_again_position _pos: " + pos);
            mp.seekTo(sb_pos);
            Log.d("pause_play", "play_again_position _sb_pos: " + sb_pos);

            mp.start();
            Log.d("pause_play", "mp.start()_getCurrentPosition: " + mp.getCurrentPosition());
            cast_timeLapse();

            // 시크바 스레드 시작
            isPlaying = true;
            seekBar_thread = new MyThread();
            seekBar_thread.start();
//            new MyThread().start();

            // 채팅 추가 스레드 다시 시작
            add_chat_thread(sb_pos);
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 캐스트일 때 -- 일시정지
     ---------------------------------------------------------------------------*/
    public void pause_clicked(View view) {
        if(mp != null && mp.isPlaying()) {

            pause_clicked = true;

            play_IV.setVisibility(View.VISIBLE);
            pause_IV.setVisibility(View.GONE);

            // 일시 정지
            mp.pause();

            // 시크바 스레드 종료
            isPlaying = false;
            seekBar_thread = null;

            // 일시정지할때의 position 값을 저장
//            pos = sb.getProgress();
            pos = mp.getCurrentPosition();
            sb_pos = sb.getProgress();
            Log.d("pause_play", "pause_position_ sb.getProgress(): " + sb.getProgress());
            Log.d("pause_play", "pause_position_ mp.getCurrentPosition(): " + mp.getCurrentPosition());


        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> timeLapse_add_chat_msg -- timeLapse 정보에 따라 채팅 추가하기
     ---------------------------------------------------------------------------*/
    public void timeLapse_add_chat_msg(int array_num) {
        try {
            JSONObject jGroup_cast = jArray_cast.getJSONObject(array_num);

            String msg_object_json = jGroup_cast.getString("msg_object_json");
            Log.d("AsyncTask_get_cast", "msg_object_json: "+msg_object_json);

            JSONObject jsonObject_msg = new JSONObject(msg_object_json);

            int BroadCast_no = jsonObject_msg.getInt("BroadCast_no");
            int User_no = jsonObject_msg.getInt("User_no");
            String User_nicName = jsonObject_msg.getString("User_nicName");
            String User_img_file = jsonObject_msg.getString("User_img_file");
            String Msg_content = jsonObject_msg.getString("Msg_content");
            String Msg_send_time = jsonObject_msg.getString("Msg_send_time");
            String Serial_orNot = jsonObject_msg.getString("Serial_orNot");
            String Host_orNot = jsonObject_msg.getString("Host_orNot");
            String Type = jsonObject_msg.getString("Type");

            int No = jGroup_cast.getInt("no");

            Log.d("AsyncTask_get_cast", "BroadCast_no: "+BroadCast_no);
            Log.d("AsyncTask_get_cast", "User_no: "+User_no);
            Log.d("AsyncTask_get_cast", "User_nicName: "+User_nicName);
            Log.d("AsyncTask_get_cast", "User_img_file: "+User_img_file);
            Log.d("AsyncTask_get_cast", "Msg_content: "+Msg_content);
            Log.d("AsyncTask_get_cast", "Msg_send_time: "+Msg_send_time);
            Log.d("AsyncTask_get_cast", "Serial_orNot: "+Serial_orNot);
            Log.d("AsyncTask_get_cast", "Host_orNot: "+Host_orNot);
            Log.d("AsyncTask_get_cast", "Type: "+Type);
            Log.d("AsyncTask_get_cast", "No: "+No);
            Log.d("AsyncTask_get_cast", "**************************************");

            msg_aryLi.add(new DataClass_msg(
                    BroadCast_no,
                    User_no,
                    User_nicName,
                    User_img_file,
                    Msg_content,
                    Msg_send_time,
                    Serial_orNot,
                    Host_orNot,
                    Type,
                    No
            ));

            if(Type.equals("send_img")) {
                // msg_aryLi에 추가
                msg_aryLi.add(new DataClass_msg(
                        BroadCast_no,
                        User_no,
                        User_nicName,
                        User_img_file,
                        Msg_content,
                        Msg_send_time,
                        Serial_orNot,
                        Host_orNot,
                        "v_for_img",
                        No
                ));
            }

        } catch (JSONException e) {
            Log.d("AsyncTask_get_cast", "JSONException: " + e);
        }

        adapter_msg.notifyDataSetChanged();
    }

    /**---------------------------------------------------------------------------
     메소드 ==> timeLapse_add_chat_msg_update -- update된 정보에 따라 채팅 추가하기
     ---------------------------------------------------------------------------*/
    public void timeLapse_add_chat_msg_update(int array_num) {

        msg_aryLi.clear();

        try {
            for(int i=0; i<=array_num; i++) {
                JSONObject jGroup_cast = jArray_cast.getJSONObject(i);

                String msg_object_json = jGroup_cast.getString("msg_object_json");
                Log.d("AsyncTask_get_cast", "msg_object_json: "+msg_object_json);

                JSONObject jsonObject_msg = new JSONObject(msg_object_json);

                int BroadCast_no = jsonObject_msg.getInt("BroadCast_no");
                int User_no = jsonObject_msg.getInt("User_no");
                String User_nicName = jsonObject_msg.getString("User_nicName");
                String User_img_file = jsonObject_msg.getString("User_img_file");
                String Msg_content = jsonObject_msg.getString("Msg_content");
                String Msg_send_time = jsonObject_msg.getString("Msg_send_time");
                String Serial_orNot = jsonObject_msg.getString("Serial_orNot");
                String Host_orNot = jsonObject_msg.getString("Host_orNot");
                String Type = jsonObject_msg.getString("Type");

                int No = jGroup_cast.getInt("no");

                Log.d("AsyncTask_get_cast", "BroadCast_no: "+BroadCast_no);
                Log.d("AsyncTask_get_cast", "User_no: "+User_no);
                Log.d("AsyncTask_get_cast", "User_nicName: "+User_nicName);
                Log.d("AsyncTask_get_cast", "User_img_file: "+User_img_file);
                Log.d("AsyncTask_get_cast", "Msg_content: "+Msg_content);
                Log.d("AsyncTask_get_cast", "Msg_send_time: "+Msg_send_time);
                Log.d("AsyncTask_get_cast", "Serial_orNot: "+Serial_orNot);
                Log.d("AsyncTask_get_cast", "Host_orNot: "+Host_orNot);
                Log.d("add_chat_msg_update", "Type: "+Type);
                Log.d("AsyncTask_get_cast", "No: "+No);
                Log.d("AsyncTask_get_cast", "**************************************");

                msg_aryLi.add(new DataClass_msg(
                        BroadCast_no,
                        User_no,
                        User_nicName,
                        User_img_file,
                        Msg_content,
                        Msg_send_time,
                        Serial_orNot,
                        Host_orNot,
                        Type,
                        No
                ));

                if(Type.equals("send_img")) {
                    // msg_aryLi에 추가
                    msg_aryLi.add(new DataClass_msg(
                            BroadCast_no,
                            User_no,
                            User_nicName,
                            User_img_file,
                            Msg_content,
                            Msg_send_time,
                            Serial_orNot,
                            Host_orNot,
                            "v_for_img",
                            No
                    ));
                }
            }
        } catch (JSONException e) {
            Log.d("AsyncTask_get_cast", "JSONException: " + e);
        }

        adapter_msg.notifyDataSetChanged();
        Log.d("add_chat_msg_update", "-----------------------------------------------------------------------------");
    }
/**********************************************************************************************/

    /**---------------------------------------------------------------------------
     메소드 ==> 서버 TCP_ 방 들어가기/ 방 나가기/ 메세지 전송하기 -- 스레드
     ---------------------------------------------------------------------------*/
    private void send_data(String type, final String sub_type) {

        /** == 방 들어가기 == */
        if(type.equals("enter")) {

            String get_broadCast_end_orNot = "";
            String red_user_orNot = "";
            // 방이 현재 방송중인지, 종료되었는지 확인
            try {
                String temp_AsyncTask_result = new AsyncTask_broadCast_end_orNot().execute(broadCast_no, a_main_after_login.user_no).get();
                Log.d("강퇴", "AsyncTask_broadCast_end_orNot_result: " + temp_AsyncTask_result);
                String[] result_split = temp_AsyncTask_result.split("&");

                get_broadCast_end_orNot = result_split[0];
                red_user_orNot = result_split[1];
                Log.d("TCP", "get_broadCast_end_orNot: "+get_broadCast_end_orNot);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // 이미 방송이 끝난 방일 떄
            if(get_broadCast_end_orNot.equals("true")) {
                Intent intent = new Intent(a_room.this, v_broadcast_end_already.class);
                startActivityForResult(intent, REQUEST_ALREADY_END_BROADCAST);
            }

            // 현재 방송중인데, 강퇴 유저일 때
            else if(get_broadCast_end_orNot.equals("false") && red_user_orNot.equals("red")) {
                Intent intent = new Intent(getBaseContext(), v_you_are_red_user.class);
                startActivityForResult(intent, REQUEST_GET_OUT_RED);
            }

            // 현재 방송중이고, 강퇴 유저가 아닐 때
            else if(get_broadCast_end_orNot.equals("false") && red_user_orNot.equals("not_red")) {
                if(sub_type.equals("retry") || sub_type.equals("continue")) {
                    type = sub_type;
                }
                final String finalType = type;

                new Thread() {
                    public void run() {
                        try {
                            socket = new Socket(ip, port);
                            Log.d("[user_no]: ", user_no + "- Server connected!!");

                            inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            outMsg = new PrintWriter(socket.getOutputStream(), true);

                            String last_received_msg_no = get_last_received_msg_no();

                            String temp = broadCast_no+Static.split+
                                    user_no+Static.split+
                                    nickName+Static.split+
                                    my_img_fileName+Static.split+
                                    "0"+Static.split+
                                    String.valueOf(Get_presentTime.present_time())+Static.split+
                                    "0"+Static.split+
                                    host_orNot+Static.split+
                                    finalType+Static.split+
                                    last_received_msg_no;
                            outMsg.println(temp);

                            /** 메시지 받는 쓰레드 시작 */
                            thread = new Thread(new ReceiveMsg());
                            thread.setDaemon(true);
                            thread.start();

                            /** 테스트 -- ping_msg_term_check */
//                            ping_msg_term_check_thread = new Thread(new ping_msg_term_check());
//                            ping_msg_term_check_thread.setDaemon(true);
//                            ping_msg_term_check_thread.start();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("TCP", "connectServer() Exception !!" + e);
                        }
                    }
                }.start();
            }

            else if(get_broadCast_end_orNot.equals("fail")) {
                Log.d("TCP", "방송번호 조회 실패(에러)");
            }


        }

        /** == 방 나가기 == */
        if(type.equals("out") && !sub_type.equals("red_card")){
            new Thread() {
                public void run() {
                    try {
                        String last_received_msg_no = get_last_received_msg_no();

                        String red_card = "none";

                        String temp = broadCast_no+Static.split+
                                user_no+Static.split+
                                nickName+Static.split+
                                my_img_fileName+Static.split+
                                "0"+Static.split+
                                String.valueOf(Get_presentTime.present_time())+Static.split+
                                "0"+Static.split+
                                host_orNot+Static.split+
                                "out"+Static.split+
                                last_received_msg_no+Static.split+
                                red_card;
                        outMsg.println(temp);

                        /** 소켓 닫기 */
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        status = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("TCP", "out_send Exception !!" + e);
                    }
                }
            }.start();
        }

        /** == 강퇴시키기 - BJ전용 == */
        if(type.equals("out") && sub_type.equals("red_card")) {
            new Thread() {
                public void run() {
                    try {
                        String red_card = "red_card";

                        String temp = broadCast_no+Static.split+
                                red_card_user_no+Static.split+
                                red_card_user_nickName+Static.split+
                                ""+Static.split+
                                "0"+Static.split+
                                String.valueOf(Get_presentTime.present_time())+Static.split+
                                "0"+Static.split+
                                "false"+Static.split+
                                "out"+Static.split+
                                "0"+Static.split+
                                red_card;
                        outMsg.println(temp);

                        red_card_user_no = 0;
                        red_card_user_nickName = "";

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("TCP", "red_card_send Exception !!" + e);
                    }
                }
            }.start();
        }


        /** 일반 채팅 메세지 전송 */
        if(type.equals("normal")) {

            // 일반 텍스트 전송일 때
            if(sub_type.equals("none")) {
                String temp_msg = send_msg_ET.getText().toString();
                final String final_msg = temp_msg.replace("\n","\\r\\n");
                Log.d("채팅메세지", final_msg);
                new Thread() {
                    public void run() {
                        try {
                            String last_received_msg_no = get_last_received_msg_no();

                            String temp = broadCast_no+Static.split+
                                    user_no+Static.split+
                                    nickName+Static.split+
                                    my_img_fileName+Static.split+
                                    final_msg+Static.split+
                                    String.valueOf(Get_presentTime.present_time())+Static.split+
                                    "0"+Static.split+
                                    host_orNot+Static.split+
                                    "normal"+Static.split+
                                    last_received_msg_no;
                            outMsg.println(temp);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("TCP", "normal_send Exception !!" + e);
                        }
                    }
                }.start();
                send_msg_ET.setText("");
                send_btn_TV.setClickable(false);
                send_btn_TV.setTextColor(Color.parseColor("#999999"));
            }
            // 이미지 전송일 때
            if(sub_type.equals("send_img")) {
                //photoPath_on_myPhone
                Log.d("send_img", "이미지 서버 전송_ upload_img_url_result: " + upload_img_url_result);

                new Thread() {
                    public void run() {
                        try {
                        String last_received_msg_no = get_last_received_msg_no();

                            String temp = broadCast_no + Static.split +
                                    user_no + Static.split +
                                    nickName + Static.split +
                                    my_img_fileName + Static.split +
                                    upload_img_url_result + Static.split +   // 메시지 내용 대신 업로드한 이미지 파일 이름을 보낸다
                                    String.valueOf(Get_presentTime.present_time()) + Static.split +
                                    "0" + Static.split +
                                    host_orNot + Static.split +
                                    "send_img" + Static.split +
                                    last_received_msg_no;
                            outMsg.println(temp);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("TCP", "normal_send Exception !!" + e);
                        }
                    }
                }.start();
            }
        }

        // 소켓 연결 확인용, 리턴 메세지
        if(type.equals("check")) {
            String check = sub_type;
            outMsg.println(check);
//            Log.d("TCP", "check: " + check);
        }

        // 좋아요 눌렀을 때
        if(type.equals("like_clicked")) {
            final String finalType1 = type;
            new Thread() {
                public void run() {
                    try {
                        String like_clicked = finalType1 + "&" + user_no + "&" + broadCast_no;
                        outMsg.println(like_clicked);
                        Log.d("TCP", "like_clicked: 좋아요 버튼 클릭 => 서버에 알림!");

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("TCP", "like_clicked_send Exception !!" + e);
                    }
                }
            }.start();
        }

        // BJ가 muteOn/Off 눌렀을 때
        if(type.equals("sound_ctl_clicked")) {
            final String finalType2 = type;
            final String finalSub_Type2 = sub_type;
            new Thread() {
                public void run() {
                    try {
                        String sound_ctl_clicked_msg = finalType2 + "&" + user_no + "&" + broadCast_no + "&" + finalSub_Type2;
                        Log.d("TCP", "sound_ctl_clicked: 버튼 클릭 => 서버에 알림!");
                        Log.d("TCP", "sound_ctl_clicked: "+sound_ctl_clicked_msg);
                        outMsg.println(sound_ctl_clicked_msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("TCP", "sound_ctl_clicked_send Exception !!" + e);
                    }
                }
            }.start();
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 메시지 수신 -- 스레드
     ---------------------------------------------------------------------------*/
    private class ReceiveMsg implements Runnable {

        @Override
        public void run() {
            String msg;
            status = true;

            // 무한루프 돌면서 메시지 수신을 받음
            while(status) {
                try {
                    msg = inMsg.readLine();
                    String[] check_type =  msg.split("&");
                    String check_type_0 = check_type[0];

//                    Log.d("TCP", "check_type_0: " + check_type_0);


                    /** ping 체크 메시지일때 */
                    if(check_type_0.equals("ping")) {
//                        Log.d("TCP", "msg: "+msg);

                        String ping_check_type_1 = check_type[1];
                        String ping_check_type_2 = check_type[2];

//                        Log.d("TCP", "ping_check_type_1: " + check_type_1);
//                        Log.d("TCP", "ping_check_type_2: " + check_type_2);

                        String replaced_msg = msg.replace("not_checked", broadCast_no);
                        send_data("check", replaced_msg);

                        // 핑 받은 시간을 체크
                        received_ping_time = System.currentTimeMillis();
                        Log.d("ping", String.valueOf(received_ping_time));
                    }

                    /** do_refresh 메시지일때 */
                    if(check_type_0.equals("do_refresh")) {
                        String broadCast_no_check_type_1 = check_type[1];
                        String target = check_type[2];

                        // mute 체크 메시지일 때 (호스트 본인 레이아웃은 변경하지 않음, 따로 클릭이벤트에서 변경함)
                        if(target.equals("sound_ctl_clicked") && host_orNot.equals("false")) {
                            Log.d("TCP", "target: " + target);
                            String mute_state = check_type[3];
                            Log.d("TCP", "mute_state: " + mute_state);

                            // mute 상태일때
                            if(mute_state.equals("1")) {
                                handler.sendEmptyMessage(4);
                            }
                            // mute 상태가 아닐 때
                            if(mute_state.equals("0")) {

                                if(from.equals("live")) {
                                    mMediaPlayer.play();
//                                  createPlayer(mFilePath);
                                }

                                //  RTMP Player 딜레이 때문에 게스트에게 unmute되는 상태를 5초정도 뒤에 표시하기
                                // -> 나중에 RTMP Player의 성능 개선이 되어 딜레이가 적어지면 이렇게 안할거임
                                try {
                                    Thread.sleep(5000);
                                    handler.sendEmptyMessage(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        // mute 체크 메시지가 아닐 때(현재 게스트수, 좋아요 클릭, 총 게스트수 변경 메시지일 때)
                        if(!target.equals("sound_ctl_clicked")) {
                            Log.d("TCP", "target: " + target);

                            // 정보 새로 받아오기(현재 게스트수, 좋아요 수, 총 게스트수)
                            try {
                                String do_refresh_result = new AsyncTask_do_refresh().execute(broadCast_no_check_type_1, user_no).get();
                                Log.d("TCP", "do_refresh_result: " + do_refresh_result);

                                String[] after_split_do_refresh_result = do_refresh_result.split("&");
                                Log.d("TCP", "after_split_do_refresh_result_[0]: " + after_split_do_refresh_result[0]);
                                Log.d("TCP", "after_split_do_refresh_result_[1]: " + after_split_do_refresh_result[1]);
                                Log.d("TCP", "after_split_do_refresh_result_[2]: " + after_split_do_refresh_result[2]);

                                present_guests = after_split_do_refresh_result[0];
                                total_guests = after_split_do_refresh_result[1];
                                like_count = after_split_do_refresh_result[2];

                                handler.sendEmptyMessage(1);

                                // 이전 핸들러 로직이 다 처리저기 전에 새로운 핸들러 메시지가 전달 되는걸 막기 위해 sleep을 적용
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                        // 좋아요 클릭 메시지일 때 - 방장에게만 적용하기
                        if(target.equals("update_like_count") && host_orNot.equals("true")) {
                            // 네번째 인수 - user_no
                            String like_clicked_user_no = check_type[3];
                            Log.d("like_clicked_user_no", ": " + like_clicked_user_no);

                            // 레트로핏으로 user_nickName 가져오기
                            get_nickName_who_clicked_like(like_clicked_user_no);

                        }
                    }

                    /** 방송 경과시간 정보 메시지 일 때 */
                    if(check_type_0.equals("onAir_Elapsed_time")) {
                        String BroadCast_no_check_type_1 = check_type[1];           // 방송번호
                        String onAir_Elapsed_time_mil_check_type_2 = check_type[2]; // 방송경과시간(String 형태)
                        onAir_Elapsed_start_time = check_type[3];                   // 방송경과시간(String 형태)
                        String onAir_time_msg_user_no = check_type[4];              // 최초 메시지 근원지(user_no)

                        // 내가 진입했을 때만, 받아온 onAir_Elapsed_time UI에 표시하기
                        if(onAir_time_msg_user_no.equals(user_no)) {
                            // 방송경과시간 long 형태로 변환
                            onAir_Elapsed_time_mil = Long.parseLong(onAir_Elapsed_time_mil_check_type_2);
                            Log.d("TCP", "--onAir_Elapsed_time_mil: " + onAir_Elapsed_time_mil);

                            // '00:00:00' String 포멧으로 변환
                            SimpleDateFormat format_for_display = new SimpleDateFormat("HH:mm:ss");
                            format_for_display.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date onAir_Elapsed_time_date = new Date(onAir_Elapsed_time_mil);
                            onAir_Elapsed_time = format_for_display.format(onAir_Elapsed_time_date);
                            Log.d("TCP", "--onAir_Elapsed_time: " + onAir_Elapsed_time);

                            handler.sendEmptyMessage(2);
                        }

                    }

                    /**위 메시지 종류에 해당하지 않을 때*/
                    else if(!check_type_0.equals("ping") && !check_type_0.equals("do_refresh") && !check_type_0.equals("onAir_Elapsed_time")) {
                        if(msg == null) {
                            Log.d("TCP", msg);
                        }
                        String[] result_after_split =  msg.split(Static.split);
                        String final_msg = result_after_split[4].replace("\\r\\n","\n");

                        for(int i=0; i<result_after_split.length; i++) {
//                        Log.d("받은메세지", result_after_split[i]);
                        }
//                    Log.d("받은메세지", "===============");

                        /**
                         == 스플릿하기 (스플릿 정보) ==
                         0 BroadCast_no;	// 방송 번호
                         1 User_no;		// 회원 번호
                         2 User_nicName;	// 회원 닉네임
                         3 User_img_file;	// 회원 이미지 파일(파일이름)
                         4 Msg_content;	// 메시지 내용
                         5 Msg_send_time;	// 메시지 전달 시간
                         6 Serial_orNot;	// 연속 여부
                         7 Host_orNot;		// 호스트 여부
                         8 Type;			// 메시지 타입
                         8 No;			// 메시지 넘버링
                         */

                        received_broadCast_no = result_after_split[0];
                        received_user_no = result_after_split[1];
                        received_nickName = result_after_split[2];
                        received_user_img_fileName = result_after_split[3];
                        received_msg_content = final_msg;
                        received_msg_send_time = result_after_split[5];
                        received_serial_orNot = result_after_split[6];
                        received_host_orNot = result_after_split[7];
                        received_msg_type = result_after_split[8];
                        received_msg_no = result_after_split[9];
                        Log.d("TCP", "received_msg_type: "+received_msg_type);
                        Log.d("TCP", "received_msg_no: "+received_msg_no);
                        Log.d("TCP", "received_serial_orNot: "+received_serial_orNot);

                        // 날 강퇴시킨 메세지인지 확인
                        if(received_msg_type.equals("red_card") && a_main_after_login.user_no.equals(received_user_no)) {
                            Log.d("강퇴", "응, "+received_nickName+" 강퇴당함");
                            Log.d("강퇴", "강퇴 유저 번호: " + received_user_no);
                            Log.d("강퇴", "강퇴 유저 닉네임: " + received_nickName);

                            new Thread() {
                                public void run() {
                                    /** 소켓 닫기 */
                                    try {
                                        socket.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    status = false;
                                }
                            }.start();

                            // 방송듣기 플레이어 중지
                            releasePlayer();

                            // 강퇴유저로 추가 - 핸들러 what == 15
                            insert_red_card_user(a_main_after_login.user_no);

                        }

                        // BJ가 네트워크가 끊긴 메시지인지 확인
                        if(received_msg_type.equals("disconnect") && received_nickName.equals(BJ_nickName)) {
                            Log.d("TCP", "BJ가 네트워크 끊김!!!!!!!!!!!");
                            Intent intent = new Intent(getBaseContext(), v_end_broadcast.class);
                            intent.putExtra("BJ_out", "true");
                            intent.putExtra("cast", "false");
                            startActivityForResult(intent, REQUEST_FORCE_TO_OUT);

                            handler.sendEmptyMessage(6);
                        }

                        // 재접속 성공 시, 좋아요 클릭 활성화(단, 이전에 좋아요 누른적이 없을때만)
                        if(didn_i_click_like.equals("false") && (received_msg_type.equals("retry") || received_msg_type.equals("continue"))) {
                            // 좋아요 수 누르기 활성화
                            like_img.setClickable(true);
                        }

                        // send_img 로직 적용 전 코드
                        /** 채팅 메세지 msg_ary에 add */
                        handler.sendEmptyMessage(0);

                        // 이전 핸들러 로직이 다 처리저기 전에 새로운 핸들러 메시지가 전달 되는걸 막기 위해 sleep을 적용
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (IOException e) {
                    Log.d("TCP", "메시지 수신 예외!" + e);
                    status = false;
                    timeElapsed.stop();

                    // 좋아요 수 누르기 비활성화
                    like_img.setClickable(false);

                    // socket closed가 아닌 exception이 발생 시 (클라이언트 네트워크 환경 변경)
                    if(!e.getMessage().equals("Socket closed")) {

                        long time_long=0;
                        boolean network_check=false;

                        while(time_long<20) {
                            try {
                                Thread.sleep(100);
                                time_long++;
                                network_check = isNetWork();
                                Log.d("TCP", "network_check: "+network_check+"_ "+String.valueOf(time_long));
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Log.d("TCP", "network_check: final_"+network_check+"_ "+String.valueOf(time_long));

                        // 2초 뒤에 네트워크 상태가 연결 가능한 상태이면 별도의 메시지 출력이나 표시 없이, 다시 소켓 연결 시도
                        // ex> wifi to 3g/4g
                        if(network_check) {
                            send_data("enter", "continue");
                        }
                        // 2초 뒤에도 네트워크 상태가 연결 불가능한 상태이면 재접속 다이얼로그 띄우기
                        if(!network_check) {
                            // 게스트일 때: 재접속 시도 가능한 액티비티 띄우기
                            if(host_orNot.equals("false")) {
                                Intent intent = new Intent(a_room.this, v_network_disconnect.class);
                                intent.putExtra("again", "first");
                                startActivityForResult(intent, REQUEST_NETWORK_DISCONNECT);
                            }
                            // BJ일 때: 재접속 불가능한 액티비티 띄우기
                            if(host_orNot.equals("true")) {
                                Intent host_intent = new Intent(getBaseContext(), v_end_broadcast.class);
                                host_intent.putExtra("BJ_out", "true");
                                host_intent.putExtra("cast", "false");
                                startActivityForResult(host_intent, REQUEST_FORCE_TO_OUT);
                            }
                        }
                    }
                }
            }
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 핸들러 -- UI 변경
     - msg_aryLi 리스트 업데이트 / 어댑터 갱신
     - 방 정보 업데이트(좋아요수, 현재_총 게스트수) / 변수 갱신
     - 현재까지 라이브 방송시간 업데이트 / 변수 갱신
     - 음소거 버튼 다시 활성화
     ---------------------------------------------------------------------------*/
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        //        public void handleMessage(android.os.Message msg) {
        public void handleMessage(Message msg) {

            /** msg_aryLi 리스트 업데이트 / 어댑터를 통해 UI에 메시지 리스트뷰 다시 그리기 */
            if (msg.what == 0) {
                // 리스트를 초기화한다
//            msg_aryLi.clear();

//                Toast.makeText(getBaseContext(), received_msg_type, Toast.LENGTH_SHORT).show();

                Log.d("메소드", "received_msg_type: " + received_msg_type);
                if(received_msg_type.equals("end")) {

                    // 라이브 방송이거나, 혹은 내가 게스트일 때 플레이어 해제
                    if(host_orNot.equals("false") || from.equals("live")) {
                        releasePlayer();
                    }

                    // 방송종료 알림 다이얼로그 액티비티 띄우기
                    Intent intent = new Intent(getBaseContext(), v_end_broadcast.class);
                    intent.putExtra("BJ_out", "false");
                    intent.putExtra("cast", "false");
                    startActivityForResult(intent, REQUEST_FORCE_TO_OUT);
                }

                else {
                    // 혹시, 나갔다가 다시 들어 왔을 때, 이전 채팅의 프로필 사진이나 닉네임도 현재 걸로 변경
                    for(int i=0; i<msg_aryLi.size(); i++) {
                        if(msg_aryLi.get(i).getUser_no()==Integer.parseInt(received_user_no)) {
                            //                        if(!msg_aryLi.get(i).getUser_img_file().equals(received_user_img_fileName)) {
                            msg_aryLi.get(i).setUser_img_file(received_user_img_fileName);
                            //                        }
                            //                        if(!msg_aryLi.get(i).getUser_nicName().equals(received_nickName)) {
                            msg_aryLi.get(i).setUser_nicName(received_nickName);
                            //                        }
                        }
                    }

                    // 이전 메시지와 지금 받은 메시지의 회원이 일치하는지 확인
                    String serial_orNot = "false";
                    if((received_msg_type.equals("normal") || received_msg_type.equals("send_img")) && received_previous_user_no.equals(received_user_no)) {
                        serial_orNot = "true";
                        received_previous_user_no = received_user_no;
                    }
                    if((received_msg_type.equals("normal") || received_msg_type.equals("send_img")) && !received_previous_user_no.equals(received_user_no)) {
                        serial_orNot = "false";
                        received_previous_user_no = received_user_no;
                    }


                    // msg_aryLi에 추가
                    msg_aryLi.add(new DataClass_msg(
                            Integer.parseInt(received_broadCast_no),// 방송 번호
                            Integer.parseInt(received_user_no),     // 회원 번호
                            received_nickName,                      // 회원 닉네임
                            received_user_img_fileName,             // 회원 이미지 파일(파일이름)
                            received_msg_content,                   // 메시지 내용
                            received_msg_send_time,                 // 메시지 전달 시간
                            serial_orNot,                           // 연속 여부
                            received_host_orNot,                    // 호스트 여부
                            received_msg_type,                      // 메시지 타입
                            Integer.parseInt(received_msg_no)       // 메시지 넘버링
                    ));

                    if(received_msg_type.equals("send_img")) {
                        // msg_aryLi에 추가
                        msg_aryLi.add(new DataClass_msg(
                                Integer.parseInt(received_broadCast_no),// 방송 번호
                                Integer.parseInt(received_user_no),     // 회원 번호
                                received_nickName,                      // 회원 닉네임
                                received_user_img_fileName,             // 회원 이미지 파일(파일이름)
                                received_msg_content,                   // 메시지 내용
                                received_msg_send_time,                 // 메시지 전달 시간
                                serial_orNot,                           // 연속 여부
                                received_host_orNot,                    // 호스트 여부
                                "v_for_img",                              // 메시지 타입
                                Integer.parseInt(received_msg_no)       // 메시지 넘버링
                        ));
                    }


                    // msg_type이 'onAir' 일때 && 내가 입장하면서 보낸 메시지일때 && 호스트 본인이 입장했을때는 제외 -- 인사말 추가
                    if(received_msg_type.equals("onAir") && received_user_no.equals(user_no) && received_host_orNot.equals("false")) {

                        String custom_welcome_word = "";
                        if(!welcome_word.equals("none")) {
                            custom_welcome_word = received_nickName + "님\n" + welcome_word;
                        }
                        if(welcome_word.equals("none")) {
                            custom_welcome_word = received_nickName + "님\n어서오세요~";
                        }

                        msg_aryLi.add(new DataClass_msg(
                                Integer.parseInt(received_broadCast_no),// 방송 번호
                                0,                                      // 회원 번호
                                BJ_nickName,                            // 회원 닉네임
                                BJ_img_fileName,                        // 회원 이미지 파일(파일이름)
                                custom_welcome_word,                    // 메시지 내용: 인사말
                                received_msg_send_time,                 // 메시지 전달 시간
                                serial_orNot,                           // 연속 여부
                                "true",                                 // 호스트 여부
                                "welcome_word",                         // 메시지 타입
                                Integer.parseInt(received_msg_no)       // 메시지 넘버링
                        ));
                    }
                    adapter_msg.notifyDataSetChanged();
                }
                handler.removeMessages(0);
            }

            /** 방 정보 업데이트(좋아요수, 현재_총 게스트수) // UI에 해당 변수 갱신 */
            if(msg.what == 1) {
                like_count_TV.setText(like_count);
                // 호스트 수 빼기(-1)
                int present_guests_subtract_host = Integer.parseInt(present_guests) - 1;
                present_guests_TV.setText(String.valueOf(present_guests_subtract_host));
                int total_guests_subtract_host = Integer.parseInt(total_guests) - 1;
                total_guests_TV.setText(String.valueOf(total_guests_subtract_host));

                handler.removeMessages(1);
            }

            /** onAir_Elapsed_time, 업데이트 */
            if(msg.what == 2) {

                timeElapsed.setBase(SystemClock.elapsedRealtime());
                timeElapsed.start();

                /** 방송전 프로그레스 휠 띄우기 테스팅 코드 */
                progressWheel_cover_V.setVisibility(View.GONE);
                progressWheel.setVisibility(View.GONE);
                /** 방송전 프로그레스 휠 띄우기 테스팅 코드 */

                live_broadCast_time_TV.setText(onAir_Elapsed_start_time);

                timeElapsed.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer cArg) {
                        live_broadCast_time_TV.setVisibility(View.GONE);
                        timeElapsed.setVisibility(View.VISIBLE);
                        long time = SystemClock.elapsedRealtime() - cArg.getBase() + onAir_Elapsed_time_mil;
//                        Log.d("TCP", "onAir_Elapsed_time_mil: " + String.valueOf(onAir_Elapsed_time_mil));
//                        Log.d("TCP", "cArg.getBase(): " + String.valueOf(cArg.getBase()));
//                        Log.d("TCP", "time: " + String.valueOf(time));

                        int h = (int)(time /3600000);
                        int m = (int)(time - h*3600000)/60000;
                        int s = (int)(time - h*3600000- m*60000)/1000;
                        String hh = h < 10 ? "0"+h: h+"";
                        String mm = m < 10 ? "0"+m: m+"";
                        String ss = s < 10 ? "0"+s: s+"";
                        cArg.setText(hh+":"+mm+":"+ss);
//                        Log.d("TCP", "hh+\":\"+mm+\":\"+ss_ " + hh+":"+mm+":"+ss);
                    }
                });

                handler.removeMessages(2);
            }

            /** 음소거 버튼 다시 활성화 - BJ만 해당 */
            if(msg.what == 3) {
                /******************************************************************************/
                /** 음소거 로직 테스트 */
                audioManager.setMicrophoneMute(false);
                audioManager.setMode(oldAudioMode);
                /******************************************************************************/

                sound_ctl_bar_Lin.setClickable(true);
                handler.removeMessages(3);
                mute_delay = null;
            }

            /** 음소거 상태로 보이게 하기 - 게스트만 해당*/
            if(msg.what == 4) {
                sound_ctl_background_V.setBackgroundColor(Color.parseColor("#ff3d3d"));
//                sound_on_Lin.setVisibility(View.GONE);
                sound_off_Lin.setVisibility(View.VISIBLE);
                handler.removeMessages(4);
                // mMediaPlayer 테스트
                // 즉각적으로 음소거 상태로 하기
                // 원래는 BJ 발송하는 음성을 그대로 전송이 되게 해야함(어차피 BJ쪽에서 마이크를 끄니)
                // 근데 딜레이가 심해서, BJ가 일단 음소거 상태인것을 알면 그냥 플래이어를 중지 시키는 걸로 시도해봄
                // 어차피 cast에서는 딜레이는 의미가 없으니 cast는 신경쓰지 않아도 됨
                if(from.equals("live")) {
                    mMediaPlayer.stop();
//                    releasePlayer();
                }
            }

            /** 소리켜짐 상태로 보이게 하기 - 게스트만 해당*/
            if(msg.what == 5) {
                sound_ctl_background_V.setBackgroundColor(Color.parseColor("#000000"));
                sound_off_Lin.setVisibility(View.GONE);
                // 즉각적으로 다시 플레이어 재생
                // 원래는 BJ 발송하는 음성을 그대로 전송이 되게 해야함(어차피 BJ쪽에서 마이크를 끄니)
                // 근데 딜레이가 심해서, BJ가 일단 다시 소리 킨 상태인것을 알면 그냥 플래이어를 다시 start 시키는 걸로 시도해봄
                // 어차피 cast에서는 딜레이는 의미가 없으니 cast는 신경쓰지 않아도 됨
                // 그런데 바로 하더라도, BJ에서 딜레이가 있어서 어차피 5~6초 정도 후에 소리가 들릴것으로 예상
                handler.removeMessages(5);
            }

            if(msg.what == 6) {
                // DB에 `방송종료`로 업데이트
                task = new AsyncTask_delete_room(a_room.this);
                task.execute(broadCast_no, "force", "toEnd");
                handler.removeMessages(6);
            }

            if(msg.what == 7) {
                progressDialog = ProgressDialog.show(a_room.this,
                        "캐스트 정보를 불러 오는 중입니다", null, true, true);
                handler.removeMessages(7);
            }

            if(msg.what == 8) {
                progressDialog.dismiss();
                handler.removeMessages(8);
            }
            if(msg.what == 9) {
                timeLapse_add_chat_msg(msg.arg1);
                handler.removeMessages(9);
            }
            if(msg.what == 10) {
                // 전체 방송시간 표시하기, 1초뒤에
                try {
                    Thread.sleep(1000);
                    test_divider_for_cast_TV.setVisibility(View.VISIBLE);
                    total_broadCast_time_TV.setVisibility(View.VISIBLE);
                    total_broadCast_time_TV.setText((String)msg.obj);
                    cast_playTime_TV.setVisibility(View.VISIBLE);
                    handler.removeMessages(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(msg.what == 11) {
                timeLapse_add_chat_msg_update(msg.arg1);
                handler.removeMessages(11);
            }
            if(msg.what == 12) {
                progressWheel_cover_V.setVisibility(View.VISIBLE);
                progressWheel.setVisibility(View.VISIBLE);
                handler.removeMessages(12);
            }
            if(msg.what == 13) {
                if(progressWheel.getVisibility() == View.VISIBLE) {
                    progressWheel_cover_V.setVisibility(View.GONE);
                    progressWheel.setVisibility(View.GONE);
                }
                handler.removeMessages(13);
            }
            if(msg.what == 14) {// 좋아요 누른 닉네임 표시 item add(BJ 전용)
                msg_aryLi.add(new DataClass_msg(
                        Integer.parseInt(received_broadCast_no),// 방송 번호
                        0,                                      // 회원 번호
                        "",                                     // 회원 닉네임
                        "",                                     // 회원 이미지 파일(파일이름)
                        like_clicked_user_nickName,             // 메시지 내용: 좋아요 클릭한 사람 닉네임
                        "",                                     // 메시지 전달 시간
                        "",                                     // 연속 여부
                        "false",                                // 호스트 여부
                        "like_clicked",                         // 메시지 타입
                        0                                       // 메시지 넘버링
                ));
                adapter_msg.notifyDataSetChanged();
                handler.removeMessages(14);

                // 하트 뿌리기
                display_hearts(show_guests_img);
            }
            if(msg.what == 15) { // 강퇴 유저 추가 결과
                Log.d("강퇴", "강퇴 유저 추가 결과: " + insert_red_card_user_result);
                if(insert_red_card_user_result.equals("success")) {
                    // 방송종료 알림 다이얼로그 액티비티 띄우기
                    Intent intent = new Intent(getBaseContext(), v_get_out.class);
                    startActivityForResult(intent, REQUEST_FORCE_TO_OUT);
                }
            }

        }
    };


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 채팅메시지 전송 -- 스레드
     ---------------------------------------------------------------------------*/
    public void send_clicked(View view) {
        send_data("normal", "none");
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 신고하기 -- 레이아웃 다이얼로그로 띄우기
     ---------------------------------------------------------------------------*/
    public void report_clicked(View view) {
        Intent intent = new Intent(a_room.this, v_report.class);
        startActivity(intent);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 캐스트 삭제하기 -- a_profile 액티비티에서 본인 캐스트 지울 때 사용
     ---------------------------------------------------------------------------*/
    public void delete_clicked(View view) {
        Log.d("내캐스트", "delete_clicked 클릭!!!");
        Intent intent = new Intent(a_room.this, v_delete_cast_confirm.class);
        intent.putExtra("broadCast_no", broadCast_no);
        startActivityForResult(intent, REQUEST_OUT);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 소프트 키보드 백버튼 오버라이드 -- 방송 나가기 컨펌 -- 레이아웃 다이얼로그로 띄우기
     ---------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(a_room.this, v_out_confirm.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        if(from.equals("live")) {
            if(host_orNot.equals("true")) {
                intent.putExtra("host_orNot", "true");
            }
            if(!host_orNot.equals("true")) {
                intent.putExtra("host_orNot", "false");
            }
        }
        if(from.equals("cast")) {
            intent.putExtra("host_orNot", "cast");
        }

        startActivityForResult(intent, REQUEST_OUT);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 소프트 키보드 백버튼 오버라이드
     ---------------------------------------------------------------------------*/
    public void out_clicked(View view) {
        onBackPressed();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 선택된 Uri의 사진 `절대경로`를 가져온다
     ---------------------------------------------------------------------------*/
    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> onActivityResult -- 방송 종료 or 방송 나가기 로직
     ---------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent intent) {
        Log.d("메소드","==== onActivityResult");


        /** 자발적 OUT */
        if(requestCode==REQUEST_OUT && resultCode==RESULT_OK) {
            broadCastctl_btn_clkicked(); // 테스트
            Log.d("메소드","==== REQUEST_OUT_ RESULT_OK");
            // TCP, 방에서 나가기_TCP 연결 종료
            if(from.equals("live")) {
                send_data("out", "none");
            }

            if(from.equals("cast")) {
                Log.d("메소드", "from.equals(\"cast\") => finish()");
                if(mp != null && mp.isPlaying()) {
                    mp.stop();
                }
                finish();
                return;
            }

            // HTTP, 내가 방장인 경우 DB에 방 종료 알림
            if(host_orNot.equals("true")){
                task = new AsyncTask_delete_room(a_room.this);
                task.execute(broadCast_no, save, user_no);
            }
            Log.d("메소드", "cast로 들어왔을땐, 여기로 오면 안됨!");
            Intent intent_main = new Intent(a_room.this, a_main_after_login.class);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_main);
            finish();
        }
        if(requestCode==REQUEST_OUT && resultCode==RESULT_CANCELED) {
            Log.d("메소드","==== REQUEST_OUT_ RESULT_CANCELED");
        }


        /** 강제 OUT */
        if(requestCode==REQUEST_FORCE_TO_OUT && resultCode==RESULT_OK) {
            broadCastctl_btn_clkicked(); // 테스트
            Log.d("메소드","==== REQUEST_FORCE_TO_OUT RESULT_OK");
            // TCP, 방에서 나가기_TCP 연결 종료
            if(from.equals("live")) {
                send_data("out", "none");
            }
            finish();
        }
        if(requestCode==REQUEST_FORCE_TO_OUT && resultCode==RESULT_CANCELED) {
            Log.d("메소드","==== REQUEST_FORCE_TO_OUT_ RESULT_CANCELED");
        }


        /** 네트워크 재접속 시도 */
        if(requestCode==REQUEST_NETWORK_DISCONNECT && resultCode==RESULT_OK) {
//            Toast.makeText(getBaseContext(), "재접속 시도 할거야!!", Toast.LENGTH_SHORT).show();
            Log.d("TCP", "재접속 시도 할거야_ network_status: "+network_status);

            task_2 = new AsyncTask_connect_retry();
            task_2.execute();

        }
        if(requestCode==REQUEST_NETWORK_DISCONNECT && resultCode==RESULT_CANCELED) {}


        /** 방 들어가려고 했는데, 이미 방송 종료된 방일 때 (TCP 연결 전) */
        if(requestCode==REQUEST_ALREADY_END_BROADCAST && resultCode==RESULT_OK) {
            Intent intent_main = new Intent(a_room.this, a_main_after_login.class);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_main);
            finish();
        }
        if(requestCode==REQUEST_ALREADY_END_BROADCAST && resultCode==RESULT_CANCELED) {}


        /** 강퇴유저일 때(TCP 연결 전) */
        if(requestCode==REQUEST_GET_OUT_RED && resultCode==RESULT_OK) {
            Intent intent_main = new Intent(a_room.this, a_main_after_login.class);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_main);
            finish();
        }
        if(requestCode==REQUEST_GET_OUT_RED && resultCode==RESULT_CANCELED) {}

        /** 앨범에서 사진 선택하여 이미지 가져옴 */
        if(requestCode==REQUEST_GET_PHOTO_FROM_ALBUM && resultCode==RESULT_OK) {
            // 이미지의 절대경로 취득
            if(intent!=null) {
                photoPath_on_myPhone = getPath(intent.getData());
            }
            // 선택 이미지 프리뷰 액티비티 띄우기
            Intent preview_intent = new Intent(getBaseContext(), a_preview_broadcast_img.class);
            preview_intent.putExtra("preview_intent", photoPath_on_myPhone);
            preview_intent.putExtra("request_activity", "room");
            startActivityForResult(preview_intent, REQUEST_SELECT_PHOTO_FROM_PREVIEW);
        }


        /** 이미지 프리뷰 액티비티로 부터 사진을 선택하여 가져왔을 때 처리 */
        /** 이미지 전송하고, 섬네일 띄우기 (썸네일은 서버에 업로드 완료 된 후 띄워야 할듯? 카톡처럼 구현하기?) */
        /** 일단은 전송이 성공되면 바로 섬네일 띄우는 걸로 */
        if(requestCode==REQUEST_SELECT_PHOTO_FROM_PREVIEW && resultCode==RESULT_OK) {
            Log.d("send_img", "photoPath_on_myPhone: " + photoPath_on_myPhone);

            try {
                Log.d("send_img_time_check", "request(a_room_activity): " + System.currentTimeMillis());
                upload_img_url_result = new AsyncTask_sending_img().execute(photoPath_on_myPhone, user_no, "room").get();
                Log.d("send_img", "서버 사진 경로: " + upload_img_url_result);

                // 이미지 업로드가 완료됐다면
                if(!upload_img_url_result.equals("fail")) {
//                    // 내 msg_ary에 바로 추가
//                    sending_img_thumbNail();

                    // 다른 게스트들이나 BJ들에게도 내가 이미지가 전송했음을 알리는 서버메시지 보내기
                    send_data("normal", "send_img");
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 방 리스트 보기 -- BJ 전용
     ---------------------------------------------------------------------------*/
    public void show_guests(View view) {
        Log.d("v_show_guests", "a_room 액티비티에서 버튼 클릭!");
        Intent intent = new Intent(a_room.this, v_show_guests.class);
        intent.putExtra("BROADCAST_NO", broadCast_no);
        intent.putExtra("USER_NO", user_no);
        startActivity(intent);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 방 하트 주기
     ---------------------------------------------------------------------------*/
    public void like_cliked(View view) {
//        like_img.setClickable(false); // Surface 테스트
        Log.d("메소드","==== 하트주기 버튼 클릭");
        if(like==0) {
            like_img.setImageResource(R.drawable.like_clicked);
            like = 1;

            send_data("like_clicked", "none");
        }
//        else if(like==1) {
//            like_img.setImageResource(R.drawable.like_yet3);
//            like = 0;
//        }
        else {}

        // 좋아요 클릭 관련 서피스뷰 로직 - 시작
        display_hearts(view);
        // 좋아요 클릭 관련 서피스뷰 로직 - 끝
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 하트날리기
     ---------------------------------------------------------------------------*/
    public void display_hearts(View view) {

        ball_insert_check = true;
        panel.thread = new SurfaceView_Heart_DrawingThread(panel);
        panel.thread.start();

        x = (int) view.getX() + (view.getWidth()/2);
        y = (int) view.getY() + (view.getHeight()/2);
        Log.d("surfaceView_log", "like_cliked_ X:" + x);
        Log.d("surfaceView_log", "like_cliked_ Y:" + y);


        final View finalView = view;
        new Thread (new Runnable() {

            int X_half_width = root.getWidth()/2;
            int X_till_imageView = X_half_width + (X_half_width- finalView.getWidth());

            int Y_start_position = root.getHeight() + finalView.getHeight() + finalView.getHeight()/2;

            @Override
            public void run() {
                int count = 0;
                while(count<35) {
                    try {
                        Random random = new Random();
                        int sleep_time2 = 0;
                        if(count == 0) {
                            sleep_time2 = 10;
                        }
                        if(count == 1) {
                            sleep_time2 = 300;
                        }
                        else if(count>1 && count<12) {
                            sleep_time2 = random.nextInt((400 - 100) + 1) + 100;
                        }
                        else if(count>=12 && count<19) {
                            sleep_time2 = random.nextInt((120 - 70) + 1) + 70;
                        }
                        else if(count>=19 && count<27) {
                            sleep_time2 = random.nextInt((300 - 150) + 1) + 150;
                        }
                        else if(count>=27) {
                            sleep_time2 = random.nextInt((300 - 70) + 1) + 70;
                        }
                        Log.d("surfaceView_log", "sleep_time:" + sleep_time2);

                        Thread.sleep(sleep_time2);
//                        panel.like_clicked_event(x,y);
                        panel.like_clicked_event(random.nextInt((X_till_imageView - X_half_width) + 1) + X_half_width, Y_start_position);
                        count++;
                        Log.d("surfaceView_log", "count:" + count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> (BJ 전용)음소거 기능
     ---------------------------------------------------------------------------*/
    public void sound_ctl_clicked(View view) {

        if(sound_status==0) {
            sound_ctl_background_V.setBackgroundColor(Color.parseColor("#ff3d3d"));
            sound_status = 1;
            sound_on_Lin.setVisibility(View.GONE);
            sound_off_Lin.setVisibility(View.VISIBLE);
            Toast.makeText(a_room.this, "\n[안내]\n\n음소거는 5초에 한번씩만 가능합니다\n", Toast.LENGTH_SHORT).show();

            send_data("sound_ctl_clicked", "mute");

            /******************************************************************************/
            /** 음소거 로직 테스트 */
//            audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            oldAudioMode = audioManager.getMode();
            audioManager.setMicrophoneMute(true);
            /******************************************************************************/
        }
        else if(sound_status==1) {
            sound_ctl_background_V.setBackgroundColor(Color.parseColor("#000000"));
            sound_status = 0;
            sound_on_Lin.setVisibility(View.VISIBLE);
            sound_off_Lin.setVisibility(View.GONE);

//            send_data("sound_ctl_clicked", "un_mute");

            // 5초동안 클릭막기 시작 - 클릭 비활성화
            sound_ctl_bar_Lin.setClickable(false);
            // 5초동안 텀 주기 - 스레드 (5초 뒤에 클릭 재활성화시키기)

            mute_delay = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        handler.sendEmptyMessage(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            mute_delay.start();

            send_data("sound_ctl_clicked", "un_mute");
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("streaming", "onDestroy");
        super.onDestroy();

        // 타이머 스레드 중지
        time_run = false;

        // 캐스트 타이머 중지
        cast_timeLapse = null;
        // 스레드 중지
        t = null;
        time = null;

        // timeElapsed 중지
        if(from.equals("live")) {
            timeElapsed.stop();
        }

        // 등록된 동적 리시버 해제
        unregisterReceiver(broadcastReceiver);

        // 라이브 방송이고 내가 방장일 때 방송서비스 해제
        if(host_orNot.equals("true") && from.equals("live")) {
            unbindService(mConnection);
            Log.d("streaming", "onStop");
            /** 미디어리코드로 따로 녹음하는 코드- 시작 */
//            stopRecording(true);
//            Uri uri = Uri.parse("file://" + mOutputFile.getAbsolutePath());
//            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            scanIntent.setData(uri);
//            sendBroadcast(scanIntent);
//            setResult(Activity.RESULT_OK, new Intent().setData(uri));
//            finish();
            /** 미디어리코드로 따로 녹음하는 코드- 끝 */
        }
        // 라이브 방송이고 혹은 내가 게스트일 때 플레이어 해제
//        if(host_orNot.equals("false") || from.equals("cast")) {
        if(host_orNot.equals("false") || from.equals("live")) {
            releasePlayer();
        }

        // 라이브 방송이고 내가 방장일 때 서비스 중지
        if(host_orNot.equals("true") && from.equals("live")) {
            stopService(mLiveVideoBroadcasterServiceIntent);
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 네트워크 상태 확인 -- 연결 가능한 상태인지 확인
     ---------------------------------------------------------------------------*/
    private Boolean isNetWork(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileAvailable = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isMobileConnect = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiAvailable = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if ((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)){
            return true;
        }else{
            return false;
        }
    }


    /**---------------------------------------------------------------------------
     클래스 ==> AsyncTask -- 네트워크 재연결
     ---------------------------------------------------------------------------*/
    private class AsyncTask_connect_retry extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        AsyncTask_connect_retry() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(a_room.this,
                    "재접속 중입니다", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            String isNetWork_str = null;

            try {
                Thread.sleep(1000);
                isNetWork_str = String.valueOf(isNetWork());
                Log.d("TCP", "isNetWork_str: "+isNetWork_str);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return isNetWork_str;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if(result.equals("false")) {
                Intent intent = new Intent(a_room.this, v_network_disconnect.class);
                intent.putExtra("again", "again");
                startActivityForResult(intent, REQUEST_NETWORK_DISCONNECT);
            }
            else if(result.equals("true")) {
                /** TCP 접속 메소드 실행 */

                // type 값이 'retry, continue'일 경우, 받지 못한 메세지가 있는지 확인하여, 해당 메세지들을 보내준다 get_unreceived_msg(
                // String No, String broadCast_no, String user_no

                String last_received_msg_no = get_last_received_msg_no();

                if(!last_received_msg_no.equals("none")) {
                    try {
                        String get_unreceived_msg = new AsyncTask_get_unreceived_msg().execute(last_received_msg_no, broadCast_no, user_no).get();
                        Log.d("AsyncTask_unrecvd_msg", get_unreceived_msg);

                        try {
                            JSONObject jObject_unreceived_msg = new JSONObject(get_unreceived_msg);
                            Log.d("AsyncTask_unrecvd_msg", "jObject_cast.length(): "+String.valueOf(jObject_unreceived_msg.length()));

                            JSONArray jArray_jObject_unreceived_msg = jObject_unreceived_msg.getJSONArray(TAG_JSON_2);
                            Log.d("AsyncTask_unrecvd_msg", "jArray_cast.length(): "+String.valueOf(jArray_jObject_unreceived_msg.length()));

//                            msgList_aryLi.clear();

                            /**---------------------------------------------------------------------------
                             jArray_unrecvd_msg 담긴 JSONObject를 하나씩 가져와,
                             가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
                             그 변수들을 토대로, 초기화한 msgList_aryLi
                             새로운 'DataClass_msg' 데이터 객체를 하나씩 생성해서
                             msg_no를 기준으로 역순 정렬(sort)한다
                             ---------------------------------------------------------------------------*/
                            for (int i=0; i<jArray_jObject_unreceived_msg.length(); i++) {
                                JSONObject jGroup_unreceived_msg = jArray_jObject_unreceived_msg.getJSONObject(i);

                                // 음성 파일이름
                                if(i==0) {
                                    String record_fileName = jGroup_unreceived_msg.getString("record_fileName");
                                    Log.d("AsyncTask_unrecvd_msg", "record_fileName: "+record_fileName);
                                }
                                // 메세지 jsonString to jsonObject
                                String msg_object_json = jGroup_unreceived_msg.getString("msg_object_json");
                                Log.d("AsyncTask_unrecvd_msg", "msg_object_json: "+msg_object_json);

                                JSONObject jsonObject_msg = new JSONObject(msg_object_json);

                                int BroadCast_no = jsonObject_msg.getInt("BroadCast_no");
                                int User_no = jsonObject_msg.getInt("User_no");
                                String User_nicName = jsonObject_msg.getString("User_nicName");
                                String User_img_file = jsonObject_msg.getString("User_img_file");
                                String Msg_content = jsonObject_msg.getString("Msg_content");
                                String Msg_send_time = jsonObject_msg.getString("Msg_send_time");
                                String Serial_orNot = jsonObject_msg.getString("Serial_orNot");
                                String Host_orNot = jsonObject_msg.getString("Host_orNot");
                                String Type = jsonObject_msg.getString("Type");

                                int No = jGroup_unreceived_msg.getInt("no");

                                Log.d("AsyncTask_unrecvd_msg", "BroadCast_no: "+BroadCast_no);
                                Log.d("AsyncTask_unrecvd_msg", "User_no: "+User_no);
                                Log.d("AsyncTask_unrecvd_msg", "User_nicName: "+User_nicName);
                                Log.d("AsyncTask_unrecvd_msg", "User_img_file: "+User_img_file);
                                Log.d("AsyncTask_unrecvd_msg", "Msg_content: "+Msg_content);
                                Log.d("AsyncTask_unrecvd_msg", "Msg_send_time: "+Msg_send_time);
                                Log.d("AsyncTask_unrecvd_msg", "Serial_orNot: "+Serial_orNot);
                                Log.d("AsyncTask_unrecvd_msg", "Host_orNot: "+Host_orNot);
                                Log.d("AsyncTask_unrecvd_msg", "Type: "+Type);
                                Log.d("AsyncTask_unrecvd_msg", "No: "+No);
                                Log.d("AsyncTask_unrecvd_msg", "**************************************");

                                msg_aryLi.add(new DataClass_msg(
                                        BroadCast_no,
                                        User_no,
                                        User_nicName,
                                        User_img_file,
                                        Msg_content,
                                        Msg_send_time,
                                        Serial_orNot,
                                        Host_orNot,
                                        Type,
                                        No
                                ));

                                if(Type.equals("send_img")) {
                                    // msg_aryLi에 추가
                                    msg_aryLi.add(new DataClass_msg(
                                            BroadCast_no,
                                            User_no,
                                            User_nicName,
                                            User_img_file,
                                            Msg_content,
                                            Msg_send_time,
                                            Serial_orNot,
                                            Host_orNot,
                                            "v_for_img",
                                            No
                                    ));
                                }

                            }

                        } catch (JSONException e) {
                            Log.d("AsyncTask_unrecvd_msg", "JSONException: " + e);
                        }

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                adapter_msg.notifyDataSetChanged();

                send_data("enter", "retry");
            }
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 마지막 msg_no 가져오기
     ---------------------------------------------------------------------------*/
    public String get_last_received_msg_no() {
        if(msg_aryLi.size()>0) {
            for(int i=0; i<msg_aryLi.size(); i++) {
                int temp = msg_aryLi.get(i).getNo();
                Log.d("msg_aryLi.getNo(): ", String.valueOf(temp));
            }
            Log.d("msg_aryLi.getNo(): ", "------------------------------------------");
            int last_msg_no = msg_aryLi.get(msg_aryLi.size()-1).getNo();
            return String.valueOf(last_msg_no);
        }
        else if(msg_aryLi.size()==0) {
            return "none";
        }
        return null;
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> BJ_info - BJ정보 보기(팝업 액티비티 다이얼로그)
     ---------------------------------------------------------------------------*/
    public void BJ_info_clicked(View view) throws ExecutionException, InterruptedException {
        String BJ_user_no = new AsyncTask_get_BJ_user_no().execute(broadCast_no).get();

        Intent v_profile_intent = new Intent(a_room.this, v_profile.class);
        v_profile_intent.putExtra("REQUEST_FROM", "a_room");
        v_profile_intent.putExtra("CLICKED_USER_NO", String.valueOf(BJ_user_no));
        v_profile_intent.putExtra("MY_USER_NO", user_no);
        v_profile_intent.putExtra("HOST_ORNOT", host_orNot);
        v_profile_intent.putExtra("BROADCAST_NO", broadCast_no);
        startActivity(v_profile_intent);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 퍼미션 체크
     ---------------------------------------------------------------------------*/
    public void permission_check() {
        // 퍼미션 확인(테드_ 라이브러리)
        new TedPermission(this)
                .setPermissionListener(permissionListener)
//                .setRationaleMessage("다음 작업을 허용하시겠습니까? 기기 사진, 미디어, 파일 액세스")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다")
                .setGotoSettingButton(true)
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> send_img -- 라이브 방송중 이미지 전송하기
     ---------------------------------------------------------------------------*/
    public void send_img(View view) {
        Log.d("send_img", "clicked!!");

        permission_check();

        Intent get_photo_intent = new Intent(Intent.ACTION_PICK);
        get_photo_intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        get_photo_intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(get_photo_intent, REQUEST_GET_PHOTO_FROM_ALBUM);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 방송 중, 좋아요 누른사람 닉네임 가져오기 -- 방장만 이용 (레트로핏2)
     ---------------------------------------------------------------------------*/
    public void get_nickName_who_clicked_like(String like_clicked_user_no) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Static.SERVER_URL_HEADER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<ResponseBody> result = retrofitService.get_nickName_who_clicked_like(Static.GET_NICKNAME_WHO_CLICKED_LIKE, like_clicked_user_no);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    like_clicked_user_nickName = response.body().string();
                    Log.d("retrofit_result", "retrofit_result: "+like_clicked_user_nickName);
                    handler.sendEmptyMessage(14);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofit_result", "onFailure_result: " + t.getMessage());
            }
        });
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 강퇴 유저 추가 (레트로핏2)
     ---------------------------------------------------------------------------*/
    public void insert_red_card_user(String red_card_user_no) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Static.SERVER_URL_HEADER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<ResponseBody> result = retrofitService.insert_red_card_user(Static.INSERT_RED_CARD_USER, broadCast_no, red_card_user_no);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    insert_red_card_user_result = response.body().string();
                    handler.sendEmptyMessage(15);

                } catch (IOException e) {
                    e.printStackTrace();
                    insert_red_card_user_result = "Exception_occur";
                    handler.sendEmptyMessage(15);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofit_result", "onFailure_result: " + t.getMessage());
                insert_red_card_user_result = t.getMessage();
                handler.sendEmptyMessage(15);
            }
        });
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 내방송인지 아닌지 확인 -- 캐스트일 때 (레트로핏2)
     ---------------------------------------------------------------------------*/
    public void is_this_my_cast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Static.SERVER_URL_HEADER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<ResponseBody> result = retrofitService.is_this_my_cast(Static.IS_THIS_MY_CAST, a_main_after_login.user_no, broadCast_no);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("내캐스트", "result: " + result);
                    if(result.equals("yes")) {
                        check_my_cast_handler.sendEmptyMessage(0);
                    }
                    else if(result.equals("no")) {
                        check_my_cast_handler.sendEmptyMessage(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofit_result", "onFailure_result: " + t.getMessage());
            }
        });
    }
}