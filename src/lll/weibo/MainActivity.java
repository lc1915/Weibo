package lll.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lll.weibo.API;
import lll.weibo.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Environment;
import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
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
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 0;
	private static int RESULT_LOAD_IMAGE1 = 1;

	private DBManager mgr;
	private int num = 0;

	Uri selectedImage = null;
	Uri selectedImage1 = null;
	Uri uri1 = null;
	ArrayList<HashMap<String, Object>> mTweets = null;

	SimpleAdapter mAdapter = null;
	ImageView beijing = null;
	ImageView touxiang = null;
	ImageView imageView1 = null;
	ImageView imageView2 = null;
	ListView mTweetList = null;
	Button jia = null;
	ArrayList<Tweet> tweets = null;
	String tweet;

	private String nicheng = null;
	EditText xingbiexx = null;
	private String gender = null;
	private String guanzhushu = null;
	private String fensishu = null;
	private String jianjie = null;
	private String nianling = null;
	private String nation;
	int age;
	int followings;
	int followers;

	private File file = null;
	private static final String FILENAME = "theFirstMessage.json";
	private StringBuffer info = null;

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
				/* 将Bitmap设定到ImageView */
				imageView1.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}

		if (requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK
				&& null != data) {
			selectedImage1 = data.getData();
			Log.e("uri", selectedImage1.toString());
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(selectedImage1));
				imageView2 = (ImageView) findViewById(R.id.imageView2);
				/* 将Bitmap设定到ImageView */
				imageView2.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTweets = new ArrayList<HashMap<String, Object>>();
		mgr = new DBManager(this);
		tweets=new ArrayList<Tweet>();

		// 点击选择背景按钮（调用sd卡）
		ImageButton xzbj = (ImageButton) findViewById(R.id.imageButton1);
		xzbj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		// 点击选择头像按钮（调用sd卡）
		ImageButton xztx = (ImageButton) findViewById(R.id.imageButton2);
		xztx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, RESULT_LOAD_IMAGE1);
			}
		});

		mTweetList = (ListView) findViewById(R.id.listView1);

		// 点击添加微博按钮
		jia = (Button) findViewById(R.id.button4);
		jia.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.SaveTweetList();
				MainActivity.this.AddATweet();
				MainActivity.this.UpdateTweetList();
			}
		});

		mAdapter = new SimpleAdapter(this, mTweets, R.layout.listview,
				new String[] { "tweet" },
				new int[] { R.id.tweet_list_item_tweet });
		mTweetList.setAdapter(mAdapter);

		// 点击“确认并保存”按钮
		Button queren = (Button) findViewById(R.id.button2);
		queren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nicheng = ((EditText) findViewById(R.id.editText1)).getText()
						.toString();
				RadioButton xingbie = (RadioButton) findViewById(R.id.radioButton1);
				RadioButton guoji = (RadioButton) findViewById(R.id.radioButton3);
				guanzhushu = ((EditText) findViewById(R.id.editText3))
						.getText().toString();
				fensishu = ((EditText) findViewById(R.id.editTextfs)).getText()
						.toString();
				followings = Integer.parseInt(guanzhushu);
				followers = Integer.parseInt(fensishu);
				jianjie = ((EditText) findViewById(R.id.editText6)).getText()
						.toString();
				nianling = ((EditText) findViewById(R.id.editText4)).getText()
						.toString();
				age = Integer.parseInt(nianling);
				gender = xingbie.isChecked() ? "   男" : "   女";
				nation = guoji.isChecked() ? "   中国" : "   美国";
				
				ArrayList<Person> persons = new ArrayList<Person>();
				Person person1 = new Person(selectedImage.toString(),
						selectedImage1.toString(), nicheng, gender, nation, age,
						followings, followers, jianjie,null,null);
				persons.add(person1);
				mgr.add(persons);
				
				ListAdapter listAdapter = mTweetList.getAdapter();
				
				for (int i = 0; i < listAdapter.getCount(); i++) {
					View listItem = mTweetList.getChildAt(i);
					tweet = ((EditText) listItem
							.findViewById(R.id.tweet_list_item_tweet)).getText()
							.toString();
					Tweet tweet1=new Tweet(nicheng,tweet);
					tweets.add(tweet1);
					mgr.add0(tweets);
					Log.w("SetupActivity", "Current tweet is \"" + tweet + "\".");
				}
				
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, Dierge.class);

				//String json = SaveAsJSON();
				//intent.putExtra("json", json.toString());

				startActivity(intent);
				finish();
			}
		});

		Button duqu = (Button) findViewById(R.id.buttondq);
		duqu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				info = new StringBuffer();

				// 判断外部存储卡是否存在
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					Toast.makeText(getApplicationContext(), "读取失败，SD存储卡不存在！",
							Toast.LENGTH_LONG).show();
					return;
				}

				// 判断文件是否存在
				String path = Environment.getExternalStorageDirectory()
						.toString()
						+ File.separator
						+ "aaaaaa"
						+ File.separator + FILENAME;
				file = new File(path);

				if (!file.exists()) {
					Toast.makeText(getApplicationContext(), "XML文件不存在！",
							Toast.LENGTH_LONG).show();
					return;
				}
				// 解析JSON文件
				LoadViaJSON(json.toString());

				Toast.makeText(getApplicationContext(), info.toString(),
						Toast.LENGTH_LONG).show();

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB
		mgr.closeDB();
	}

	void SaveTweetList() {

		ListAdapter listAdapter = mTweetList.getAdapter();

		for (int i = 0; i < listAdapter.getCount(); i++) {

			HashMap<String, Object> item = mTweets.get(i);
			View listItem = mTweetList.getChildAt(i);
			String tweet = ((EditText) listItem
					.findViewById(R.id.tweet_list_item_tweet)).getText()
					.toString();
			Log.w("SetupActivity", "Current tweet is \"" + tweet + "\".");
			item.put("tweet", tweet);

		}

	}

	@SuppressLint("NewApi")
	void UpdateTweetList() {

		// Update.
		Log.w("SetupActivity", "Current mTweets size is " + mTweets.size()
				+ ".");

		mAdapter.notifyDataSetChanged();

		ListAdapter listAdapter = mTweetList.getAdapter();
		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, mTweetList);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();

		}

		android.view.ViewGroup.LayoutParams params = mTweetList
				.getLayoutParams();
		params.height = totalHeight
				+ (mTweetList.getDividerHeight() * (listAdapter.getCount() - 1));

		Log.w("SetupActivity",
				"Current mTweetList size is " + mTweetList.getCount() + ".");

	}

	void AddATweet() {

		HashMap<String, Object> tweetMap = new HashMap<String, Object>();
		tweetMap.put("tweet", "");

		mTweets.add(tweetMap);

	}

	void Settupian(Uri uri) {
		selectedImage = uri;
		imageView1.setImageURI(selectedImage);
	}

	void SetNickname(String nickname) {
		nicheng = nickname;
	}

	void SetSex(String sex) {
		gender = sex;
	}

	void SetFollowings(String followings) {
		guanzhushu = followings;
	}

	void SetFollowers(String followers) {
		fensishu = followers;
	}

	void SetDescription(String description) {
		jianjie = description;
	}

	static JSONObject json;

	static JSONObject Getjson(JSONObject json0) {
		return json0 = json;
	}

	String SaveAsJSON() {
		PrintStream ps = null;
		try {
			json = new JSONObject();
			json.put("backgroundUri", selectedImage == null ? ""
					: selectedImage.toString());
			json.put("iconUri",
					selectedImage1 == null ? "" : selectedImage1.toString());
			json.put("nickname", MainActivity.this.nicheng);
			json.put("sex", MainActivity.this.gender);
			json.put("followings", MainActivity.this.guanzhushu);
			json.put("followers", MainActivity.this.fensishu);
			json.put("nianling", MainActivity.this.nianling);
			json.put("description", MainActivity.this.jianjie);

			SaveTweetList();
			JSONArray tweets = new JSONArray();
			for (int i = 0; i < mTweets.size(); i++) {

				HashMap<String, Object> tweetItem = (HashMap<String, Object>) mTweets
						.get(i);
				tweets.put(tweetItem.get("tweet"));

			}
			json.put("tweets", tweets);

			return json.toString(4);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 判断外部存储卡是否存在
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getApplicationContext(), "读取失败，SD存储卡不存在！",
					Toast.LENGTH_LONG).show();
			return null;
		}

		// 初始化File
		String path = Environment.getExternalStorageDirectory().toString()
				+ File.separator + "aaaaaa" + File.separator + FILENAME;
		file = new File(path);

		// 如果当前文件的父文件夹不存在，则创建文件夹
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

		try {
			ps = new PrintStream(new FileOutputStream(file));
			ps.print(json.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ps != null)
				ps.close();
		}
		return null;

	}

	void LoadViaJSON(String jsonString) {

		try {
			JSONObject json = new JSONObject(jsonString);

			Settupian(Uri.parse(json.getString("iconUri")));

			SetNickname(json.getString("nickname"));

			SetSex(json.getString("sex"));

			SetFollowings(json.getString("followings"));

			SetFollowers(json.getString("followers"));

			SetDescription(json.getString("description"));

			mTweets.clear();
			JSONArray tweets = json.getJSONArray("tweets");
			for (int i = 0; i < tweets.length(); i++) {

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("tweet", tweets.getString(i));
				mTweets.add(map);

			}
			UpdateTweetList();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
