package io.antmedia.android.broadcaster;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Message;
import android.util.Log;

import io.antmedia.android.broadcaster.encoder.AudioHandler;

/**
 * Created by mekya on 28/03/2017.
 */

class AudioRecorderThread extends Thread {

    private static final String TAG = AudioRecorderThread.class.getSimpleName();
    private final int mSampleRate;
    private final long startTime;
    private volatile boolean stopThread = false;

    private android.media.AudioRecord audioRecord;
    private android.media.AudioRecord recorder;
    private AudioHandler audioHandler;

    /** 미디어리코드로 따로 녹음하는 코드- 시작 */
//    private Thread recordingThread = null;
//    private boolean isRecording = false;
//    private MediaRecorder mRecorder;
//    private File mOutputFile;

//    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
//    int BytesPerElement = 2; // 2 bytes in 16bit format
//    private boolean isRecording = false;
//    private Thread recordingThread = null;
    /** 미디어리코드로 따로 녹음하는 코드- 끝 */

    public AudioRecorderThread(int sampleRate, long recordStartTime, AudioHandler audioHandler) {
        this.mSampleRate = sampleRate;
        this.startTime = recordStartTime;
        this.audioHandler = audioHandler;
    }


    @Override
    public void run() {

        int bufferSize = android.media.AudioRecord
                .getMinBufferSize(mSampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
        byte[][] audioData;
        int bufferReadResult;

        audioRecord = new android.media.AudioRecord(MediaRecorder.AudioSource.MIC,
                mSampleRate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);


        // divide byte buffersize to 2 to make it short buffer
        audioData = new byte[1000][bufferSize];

        audioRecord.startRecording();

        ///////////////////////////////////////////////////////////////////////////////////////
        /** 미디어리코드로 따로 녹음하는 코드- 시작 */
//        startRecording();
//        startRecording();
        /** 미디어리코드로 따로 녹음하는 코드- 끝*/

        int i = 0;
        byte[] data;
        while ((bufferReadResult = audioRecord.read(audioData[i], 0, audioData[i].length)) > 0) {

            data = audioData[i];
            Message msg = Message.obtain(audioHandler, AudioHandler.RECORD_AUDIO, data);
            msg.arg1 = bufferReadResult;
            msg.arg2 = (int)(System.currentTimeMillis() - startTime);
            audioHandler.sendMessage(msg);

            i++;
            if (i == 1000) {
                i = 0;
            }
            if (stopThread) {
                break;
            }
        }

        Log.d(TAG, "AudioThread Finished, release audioRecord");
    }

    public void stopAudioRecording() {
        if (audioRecord != null && audioRecord.getRecordingState() == android.media.AudioRecord.RECORDSTATE_RECORDING) {
            stopThread = true;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            /** 미디어리코드로 따로 녹음하는 코드- 시작 */
//            stopRecording(true);

//            isRecording = false;
//            recordingThread = null;
            /** 미디어리코드로 따로 녹음하는 코드- 끝 */
        }
    }

    /** 미디어리코드로 따로 녹음하는 코드- 시작 */
//    private void startRecording() {
//        isRecording = true;
//        recordingThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                writeAudioDataToFile();
//            }
//        });
//        recordingThread.start();
//    }
//
//    private void writeAudioDataToFile() {
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
//        mRecorder.setAudioEncodingBitRate(48000);
//        mRecorder.setAudioSamplingRate(44100);
//        mOutputFile = getOutputFile();
//        mOutputFile.getParentFile().mkdirs();
//        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());
//        Log.d("Voice Recorder","started recording to "+mOutputFile.getAbsolutePath());
//
//        try {
//            mRecorder.prepare();
//            mRecorder.start();
////            Log.d("Voice Recorder","started recording to "+mOutputFile.getAbsolutePath());
//        } catch (IOException e) {
//            Log.e("Voice Recorder", "prepare() failed "+e.getMessage());
//        }
//    }
//
//    protected  void stopRecording(boolean saveFile) {
//        if(mRecorder != null) {
//            mRecorder.stop();
//            mRecorder.release();
//            mRecorder = null;
//        }
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

//    private void startRecording() {
//        isRecording = true;
//        recordingThread = new Thread(new Runnable() {
//            public void run() {
//                writeAudioDataToFile();
//            }
//        }, "AudioRecorder Thread");
//        recordingThread.start();
//    }
//
//
//    //convert short to byte
//    private byte[] short2byte(short[] sData) {
//
//        int shortArrsize = sData.length;
//        byte[] bytes = new byte[shortArrsize * 2];
//
//        for (int i = 0; i < shortArrsize; i++) {
//            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
//            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
//            sData[i] = 0;
//        }
//        return bytes;
//    }
//
//    private void writeAudioDataToFile() {
//        // Write the output audio in byte
//
////        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/voice8K16bitmono.pcm";
//        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/testtest.pcm";
//        Log.d("path_path", filePath);
//        short sData[] = new short[BufferElements2Rec];
//
//        FileOutputStream os = null;
//        try {
//            os = new FileOutputStream(filePath);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        while (isRecording) {
//            // gets the voice output from microphone to byte format
//
//            audioRecord.read(sData, 0, BufferElements2Rec);
//            try {
//                // // writes the data to file from buffer
//                // // stores the voice buffer
//                byte bData[] = short2byte(sData);
//                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    /** 미디어리코드로 따로 녹음하는 코드- 끝*/

}
