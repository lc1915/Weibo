package lll.weibo;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lll.weibo.R.string;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class WriteWeibo extends Activity {
	
	private static int RESULT_LOAD_IMAGE = 0;
	Uri selectedImage = null;

	Button exit = null;
	Button submit = null;
	Button addImage = null;
	SimpleAdapter mAdapter = null;
	ArrayList<HashMap<String, Object>> mTweets = null;
	String newWeibo = null;
	String newWeibo1 = null;
	ImageView imageView1=null;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			selectedImage = data.getData();
			Log.e("uri", selectedImage.toString());
			Log.e("path", selectedImage.getPath());
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(selectedImage));
				imageView1 = (ImageView) findViewById(R.id.imageView1);
				/* ��Bitmap�趨��ImageView */
				imageView1.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_weibo);

		// �ǳ�
		Intent intent = getIntent();
		String nicheng = intent.getStringExtra("nicheng");
		TextView nichenga = (TextView) findViewById(R.id.textView1);
		nichenga.setText(nicheng);
		
		addImage=(Button) findViewById(R.id.button3);
		addImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		// ������ȡ�������¼�
		exit = (Button) findViewById(R.id.button1);
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WriteWeibo.this, Dierge.class);
				setResult(3, intent);
				finish();
			}
		});

		// ������ȷ�ϡ����¼�
		submit = (Button) findViewById(R.id.button2);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newWeibo = ((EditText) findViewById(R.id.editText1)).getText()
						.toString();

				Pattern pattern = Pattern.compile("�Ҳ�|����|�Բ�|����");
				Matcher matcher = pattern.matcher(newWeibo);
				StringBuffer sbr = new StringBuffer();
				while (matcher.find()) {
					matcher.appendReplacement(sbr, "ôô��");
				}
				matcher.appendTail(sbr);
				newWeibo1 = sbr.toString();

				Intent intent = new Intent();
				intent.setClass(WriteWeibo.this, Dierge.class);
				intent.putExtra("newweibo", newWeibo1);
				setResult(2, intent);
				finish();
			}
		});
	}

	String SaveAsJSON() {

		try {
			JSONObject json = new JSONObject();
			json.put("newweibo", WriteWeibo.this.newWeibo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}