package edu.temple.soundgram;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecordAudio extends Activity {
	TextView textViewRecordInstructions = null;
    Button buttonStart = null, buttonStop = null;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    File mySoundGramFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiorecord);

        textViewRecordInstructions = (TextView) findViewById(R.id.textView_recordinstructions);

        buttonStart = (Button) findViewById(R.id.button_start);
        buttonStop = (Button) findViewById(R.id.button_stop);

        buttonStop.setEnabled(false);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);
                textViewRecordInstructions.setText("Tap STOP to stop recording");

                startRecording();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                textViewRecordInstructions.setText("Tap START to start recording");

                stopRecording();
                startPlaying();

                AlertDialog alertDialog = new AlertDialog.Builder(RecordAudio.this).create();
                alertDialog.setMessage("Keep this Audio Tag?");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                 
                        
                    	setResult(RESULT_OK);
                    	finish();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                try {
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startRecording(){
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        
        try {
			mySoundGramFile = new File (getIntent().getStringExtra("fileName"));
			mySoundGramFile.createNewFile();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        mRecorder.setOutputFile(mySoundGramFile.toString());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void stopRecording(){
    	try {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }

    public void startPlaying(){	
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mySoundGramFile.toString());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying(){
        mPlayer.release();
        mPlayer = null;
    }
}
