package edu.temple.soundgram;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	int TAKE_PICTURE_REQUEST_CODE = 11111111;
	
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
		
		File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg"); // Temporary file name
		pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(photo));
		
		imageUri = Uri.fromFile(photo);
		startActivityForResult(pictureIntent, TAKE_PICTURE_REQUEST_CODE); // Launches an external activity/application to take a picture
		
		Toast.makeText(this, "Creating new SoundGram", Toast.LENGTH_LONG).show();
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PICTURE_REQUEST_CODE) {
			
			
			ImageView imageView = new ImageView(this);
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200); // Set our image view to thumbnail size

			imageView.setLayoutParams(lp);

			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.HORIZONTAL);

			ll.addView(imageView);
			
			
			ContentResolver cr = getContentResolver();
			Bitmap bitmap;
			try {
				bitmap = android.provider.MediaStore.Images.Media
						.getBitmap(cr, imageUri);

				imageView.setImageBitmap(bitmap);
				Toast.makeText(this, imageUri.toString(),
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(this, "Could not load image", Toast.LENGTH_LONG)
				.show();
				e.printStackTrace();
			}
			
		}	
	}
}











