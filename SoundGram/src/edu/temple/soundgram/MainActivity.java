package edu.temple.soundgram;

import java.io.File;
import java.io.IOException;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.temple.soundgram.util.UploadSoundGramService;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	int userId = 111;
	

	int TAKE_PICTURE_REQUEST_CODE = 11111111;
	int RECORD_AUDIO_REQUEST_CODE = 11111112;
	
	File photo, audio;
	
	LinearLayout ll;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ll = (LinearLayout) findViewById(R.id.imageLinearLayout);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_soundgram:
			newSoundGram();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	Uri imageUri;
	private void newSoundGram(){
		
		Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		File storageDirectory = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
		
		storageDirectory.mkdir();
		
		photo = new File(storageDirectory, String.valueOf(System.currentTimeMillis()) + ".jpg"); // Temporary file name
		pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(photo));
		
		imageUri = Uri.fromFile(photo);
		startActivityForResult(pictureIntent, TAKE_PICTURE_REQUEST_CODE); // Launches an external activity/application to take a picture
		
		Toast.makeText(this, "Creating new SoundGram", Toast.LENGTH_LONG).show();
	}
	ImageView imageView;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PICTURE_REQUEST_CODE) {
			
			imageView = new ImageView(this);
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(600, 600); // Set our image view to thumbnail size

			imageView.setLayoutParams(lp);

			ImageLoader.getInstance().displayImage(imageUri.toString(), imageView);
			getAudioClip();
			
			
		} else if (resultCode == Activity.RESULT_OK && requestCode == RECORD_AUDIO_REQUEST_CODE){
			
			imageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MediaPlayer mPlayer = new MediaPlayer();
			        try {
			            mPlayer.setDataSource(audio.toString());
			            mPlayer.prepare();
			            mPlayer.start();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
					
				}
			});
			
			//addViewToStream(imageView);
			
			uploadSoundGram();
		}
		
	}
	
	private void getAudioClip(){
		Intent audioIntent = new Intent(this, RecordAudio.class);
		File storageDirectory = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
		audio = new File(storageDirectory, String.valueOf(System.currentTimeMillis())); // Temporary file name
		
		audioIntent.putExtra("fileName", audio.getAbsolutePath());
		
		startActivityForResult(audioIntent, RECORD_AUDIO_REQUEST_CODE);
	}
	
	
	private void addViewToStream(View view){
		ll.addView(view);
	}
	
	private void uploadSoundGram(){
		
		Intent uploadSoundGramIntent = new Intent(this, UploadSoundGramService.class);
		uploadSoundGramIntent.putExtra(UploadSoundGramService.directory, Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
		uploadSoundGramIntent.putExtra(UploadSoundGramService.image, photo.getAbsolutePath());
		uploadSoundGramIntent.putExtra(UploadSoundGramService.audio, audio.getAbsolutePath());

		startService(uploadSoundGramIntent);
		Toast.makeText(this, "Uploading SoundGram", Toast.LENGTH_SHORT).show();
	}
}











