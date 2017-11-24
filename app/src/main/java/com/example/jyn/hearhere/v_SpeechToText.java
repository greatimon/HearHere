package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by JYN on 2017-08-22.
 */

public class v_SpeechToText extends Activity {

    String clicked_user_no;
    String clicked_nicnName;

    SpeechRecognizer speechRecognizer;

    Animation mic_icon_1_anim;
    RelativeLayout mic_on_REL;
    ImageView mic_on_IV_1, mic_on_IV_2, mic_on_IV_3, mic_on_IV_4;

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_speechtotext);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFinishOnTouchOutside(true);

        // 인텐트 값 전달 받기
        Intent intent = getIntent();
        clicked_user_no = intent.getExtras().getString("clicked_user_no");
        clicked_nicnName = intent.getExtras().getString("clicked_nicnName");
        Log.d("강퇴", "clicked_user_no: " + clicked_user_no);
        Log.d("강퇴", "clicked_nicnName: " + clicked_nicnName);

        // 뷰 찾기
        mic_on_REL = (RelativeLayout)findViewById(R.id.mic_on_layout);
        mic_on_IV_1 = (ImageView)findViewById(R.id.mic_on_1);
        mic_on_IV_2 = (ImageView)findViewById(R.id.mic_on_2);
        mic_on_IV_3 = (ImageView)findViewById(R.id.mic_on_3);
        mic_on_IV_4 = (ImageView)findViewById(R.id.mic_on_4);

        // 뷰 초기 설정
        mic_on_REL.setClickable(true);

        // 애니메이션 설정
        mic_icon_1_anim = AnimationUtils.loadAnimation(this, R.anim.mic_icon_1);
        mic_icon_1_anim.setAnimationListener(myAnimationListener);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 강제 키워드 인식 버튼-- mic
     ---------------------------------------------------------------------------*/
    public void mic_on_clicked(View view) {
        start();
//        promptSpeechInput();

        // 애니메이션 시작
        mic_on_IV_1.clearAnimation();
        mic_on_IV_1.startAnimation(mic_icon_1_anim);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 다이얼로그 닫기
     ---------------------------------------------------------------------------*/
    public void cancel_clicked(View view) {
        cancel();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 음성 인식 시작
     ---------------------------------------------------------------------------*/
    public void start() {
        //초기화 및 인식 시작
        Intent intent;
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN.toString());

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(recognitionListener);
        speechRecognizer.startListening(intent);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 음성 인식 종료
     ---------------------------------------------------------------------------*/
    public void cancel() {
        //인식 종료
        speechRecognizer.cancel();
    }


    /**---------------------------------------------------------------------------
     콜백메소드 ==> 음성인식 결과 콜백
     ---------------------------------------------------------------------------*/
    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            //인식 준비완료 - startListening() 호출 하면
            Log.d("강퇴", "onReadyForSpeech");
        }
        @Override
        public void onBeginningOfSpeech() {
            //인식 시작 - 사용자가 말을 시작 하면
            Log.d("강퇴", "onBeginningOfSpeech");
        }
        @Override
        public void onRmsChanged(float v) {

        }
        @Override
        public void onBufferReceived(byte[] bytes) {

        }
        @Override
        public void onEndOfSpeech() {
            //인식 끝 - 사용자가 말을 끝내면
            Log.d("강퇴", "onEndOfSpeech");
        }
        @Override
        public void onError(int i) {
            Log.d("강퇴", "onError: " + i);
            //에러 났을 때
            switch (i) {
                case SpeechRecognizer.ERROR_AUDIO:
                    //오디오 녹음 오류 3
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    //클라이언트 오류 5
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    //불충분한 권한 9
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    //네트워크 오류 2
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    //네트워크 작업 시간 초과 1
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    //인식 결과 불일치 7
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    //인식이 많음8
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    //서버 에러상태 4
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    //인식되지 않음 6
            }
        }

        @Override
        public void onResults(Bundle bundle) {
            //인식 결과
            String key;
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            for(int i=0; i<mResult.size(); i++) {
                Log.d("강퇴", "result["+i+"]: " + mResult.get(i));
            }

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
//            textView.setText(String.valueOf(rs[0]));
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            //인식중 결과 반환
        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };


    /**---------------------------------------------------------------------------
     콜백메소드 ==> 애니메이션 콜백
     ---------------------------------------------------------------------------*/
    Animation.AnimationListener myAnimationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            mic_on_REL.setClickable(false);

            if(animation==mic_icon_1_anim && !mic_on_REL.isClickable()) {
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mic_on_REL.setClickable(true); // 테스트용 코드
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    };






















    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "강제키워드를 말씀하세요");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "음성인식 지원이 되지 않습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txtSpeechInput.setText(result.get(0));

                    for(int i=0; i<result.size(); i++) {
                        Log.d("result", "result["+i+"]: " + result.get(i));
                    }
                }
                break;
            }

        }
    }
}
