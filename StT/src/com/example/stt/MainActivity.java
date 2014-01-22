package com.example.stt;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends Activity implements OnClickListener, OnInitListener {
protected static final int REQUEST_OK = 1;
private TextToSpeech myTTS;
private int MY_DATA_CHECK_CODE = 0;
SeekBar sbPitch,sbSpeed;
EditText txt;
float speedValue=0.0f;
float pitchValue=1.0f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sbPitch=(SeekBar)findViewById(R.id.seekBar1);
		sbPitch.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
		{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				pitchValue=(float)progress;
				pitchValue=pitchValue/100;
			}

			public void onStartTrackingTouch(SeekBar seekBar)
			{                                                                                                    
			  }

			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
			});
		sbSpeed=(SeekBar)findViewById(R.id.seekBar2);
		sbSpeed.setMax(300);
		sbSpeed.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
		{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				speedValue=(float)progress;
				speedValue=speedValue/100;
				//txt.setText("sp Value"+speedValue);
			}

			public void onStartTrackingTouch(SeekBar seekBar)
			{
			                                                              
			                                              
			  }

			public void onStopTrackingTouch(SeekBar seekBar)
			{

			}
			});
		findViewById(R.id.btnSpeak).setOnClickListener(this);
		findViewById(R.id.btnListen).setOnClickListener(this);
        txt = (EditText)findViewById(R.id.txtText);
        txt.setText("Testing");
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnSpeak:
			Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
	       	 try {
	            startActivityForResult(i, REQUEST_OK);
	        } catch (Exception e) {
	       	 	Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
	        }
			break;
		case R.id.btnListen:
			
            String words = txt.getText().toString();
            speakWords(words);
		break;
		}
		
	}
    private void speakWords(String speech) {
  
//    	 if (myTTS.isLanguageAvailable(Locale.CHINA) >= 0) 
//    	 myTTS.setLanguage(Locale.CHINA);
		myTTS.setSpeechRate(speedValue);
    	myTTS.setPitch(pitchValue);
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);  
}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) {
	        		ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	        		((TextView)findViewById(R.id.txtText)).setText(thingsYouSaid.get(0));
	        }
	        
	        if (requestCode == MY_DATA_CHECK_CODE) {
	            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	                //the user has the necessary data - create the TTS
	            myTTS = new TextToSpeech(this, this);
	            }
	            else {
	                    //no data - install it now
	                Intent installTTSIntent = new Intent();
	                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	                startActivity(installTTSIntent);
	            }
	        }  
	    }
    //setup TTS
public void onInit(int initStatus) {
        //check for successful instantiation
    if (initStatus == TextToSpeech.SUCCESS) {
        if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
            myTTS.setLanguage(Locale.US);
    }
    else if (initStatus == TextToSpeech.ERROR) {
        Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
    }
}
}




