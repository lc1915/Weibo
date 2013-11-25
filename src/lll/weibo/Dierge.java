package lll.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Dierge extends Activity {

	ImageView selectedImage = null;
	ImageView selectedImage1 = null;
	ImageButton writeWeibo = null;
	ListView mTweetList = null;
	TextView nicheng = null;
	TextView gender = null;
	TextView guanzhushu = null;
	TextView fensishu = null;
	TextView nianling = null;
	TextView jianjie = null;
	TextView mTweets1 = null;
	TextView wbs = null;
	TextView xxzl = null;
	TextView wb = null;
	TextView jj = null;
	Button weiboButton = null;
	ArrayList<HashMap<String, Object>> mTweets = null;
	SimpleAdapter mAdapter = null;
	HashMap<String, Object> item = null;

	String a = null;
	TextView t = null;

	JSONObject json0;
	String mSession = null;
	int mUserId = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {

			mSession = getIntent().getStringExtra("session");
			mUserId = getIntent().getIntExtra("userId", API.USER_ID_UNKNOWN);

		} else {

			mSession = savedInstanceState.getString("session");
			mUserId = savedInstanceState.getInt("userId", API.USER_ID_UNKNOWN);

		}
		setContentView(R.layout.dierge);

		selectedImage = (ImageView) findViewById(R.id.imageView1);
		selectedImage1 = (ImageView) findViewById(R.id.imageButton1);

		nicheng = (TextView) findViewById(R.id.textView1);
		gender = (TextView) findViewById(R.id.textView9);
		guanzhushu = (TextView) findViewById(R.id.textView5);
		fensishu = (TextView) findViewById(R.id.textView6);
		jianjie = (TextView) findViewById(R.id.textView2);
		nianling = (TextView) findViewById(R.id.nianling);
		mTweetList = (ListView) findViewById(R.id.listView1);
		mTweets1 = (TextView) findViewById(R.id.textView8);
		wbs = (TextView) findViewById(R.id.textView4);
		mTweets = new ArrayList<HashMap<String, Object>>();

		Display(getIntent().getStringExtra("json"));

		// 点击ImageButton“写微博”
		final String nichengs = nicheng.getText().toString();

		writeWeibo = (ImageButton) findViewById(R.id.imageButton2);
		writeWeibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Dierge.this, WriteWeibo.class);
				intent.putExtra("nicheng", nichengs);
				startActivityForResult(intent, 2);
			}
		});

		// 点击Button“微博”
		weiboButton = (Button) findViewById(R.id.buttonWeibo);
		weiboButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(Dierge.this, WeiboList.class);

				json0 = MainActivity.Getjson(json0);

				intent.putExtra("json", json0.toString());

				startActivity(intent);

			}
		});
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void Display(String jsonString) {

		String newTweets = null;
		String newTweets1 = null;

		try {
			JSONObject json = new JSONObject(jsonString);

			selectedImage
					.setImageURI(Uri.parse(json.getString("backgroundUri")));
			selectedImage1.setImageURI(Uri.parse(json.getString("iconUri")));
			nicheng.setText(json.getString("nickname"));
			gender.setText(json.getString("sex"));
			nianling.setText(" " + json.getString("nianling") + "岁");
			guanzhushu.setText(json.getString("followings"));
			fensishu.setText(json.getString("followers"));
			jianjie.setText("简介：" + json.getString("description"));

			JSONArray tweets = json.getJSONArray("tweets");
			mTweets1.setText("微博 (" + tweets.length() + ")");
			wbs.setText(" " + tweets.length() + " ");

			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < tweets.length(); i++) {
				item = new HashMap<String, Object>();
				newTweets = tweets.getString(i);

				// 正则表达式替换字符
				Pattern pattern = Pattern.compile("我操|你妈|卧槽|你妹");
				Matcher matcher = pattern.matcher(newTweets);
				StringBuffer sbr = new StringBuffer();
				while (matcher.find()) {
					matcher.appendReplacement(sbr, "么么哒");
				}
				matcher.appendTail(sbr);
				newTweets1 = sbr.toString();

				item.put("tweet", newTweets1);
				list.add(item);
			}

			mTweetList
					.setAdapter(new SimpleAdapter(this, list,
							android.R.layout.simple_list_item_1,
							new String[] { "tweet" },
							new int[] { android.R.id.text1 }));
			Log.w("DisplayActivity",
					"mTweetList's length is " + mTweetList.getCount() + ".");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void SaveTweetList() {

		ListAdapter listAdapter = mTweetList.getAdapter();

		for (int i = 0; i < listAdapter.getCount(); i++) {

			mTweets.add(item);

		}
		HashMap<String, Object> tweetMap = new HashMap<String, Object>();
		tweetMap.put("tweet", "");

		mTweets.add(tweetMap);
	}

	String SaveAsJSON() {

		try {
			JSONObject json = new JSONObject();

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
		return null;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		setContentView(R.layout.dierge);

		// 写微博的页面（WriteWeibo.java）中点击发送之后
		if (2 == requestCode && resultCode == 2) {
			a = intent.getStringExtra("newweibo");
			t = (TextView) findViewById(R.id.textView3a);
			t.setText(a);

			selectedImage = (ImageView) findViewById(R.id.imageView1);
			selectedImage1 = (ImageView) findViewById(R.id.imageButton1);

			nicheng = (TextView) findViewById(R.id.textView1);
			gender = (TextView) findViewById(R.id.textView9);
			guanzhushu = (TextView) findViewById(R.id.textView5);
			fensishu = (TextView) findViewById(R.id.textView6);
			jianjie = (TextView) findViewById(R.id.textView2);
			nianling = (TextView) findViewById(R.id.nianling);
			mTweetList = (ListView) findViewById(R.id.listView1);
			mTweets1 = (TextView) findViewById(R.id.textView8);
			wbs = (TextView) findViewById(R.id.textView4);

			Display(getIntent().getStringExtra("json"));

			final String nichengs = nicheng.getText().toString();

			writeWeibo = (ImageButton) findViewById(R.id.imageButton2);
			writeWeibo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(Dierge.this, WriteWeibo.class);
					intent.putExtra("nicheng", nichengs);
					startActivityForResult(intent, 2);
				}
			});

			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("tweet", intent.getStringExtra("newweibo"));
			list.add(item);

			// 点击Button“微博”
			weiboButton = (Button) findViewById(R.id.buttonWeibo);
			weiboButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setClass(Dierge.this, WeiboList.class);

					json0 = MainActivity.Getjson(json0);

					intent.putExtra("json", json0.toString());
					startActivity(intent);

				}
			});
		}

		// 写微博的页面（WriteWeibo.java）点击取消之后
		if (2 == requestCode && resultCode == 3) {
			selectedImage = (ImageView) findViewById(R.id.imageView1);
			selectedImage1 = (ImageView) findViewById(R.id.imageButton1);

			nicheng = (TextView) findViewById(R.id.textView1);
			gender = (TextView) findViewById(R.id.textView9);
			guanzhushu = (TextView) findViewById(R.id.textView5);
			fensishu = (TextView) findViewById(R.id.textView6);
			jianjie = (TextView) findViewById(R.id.textView2);
			nianling = (TextView) findViewById(R.id.nianling);
			mTweetList = (ListView) findViewById(R.id.listView1);
			mTweets1 = (TextView) findViewById(R.id.textView8);
			wbs = (TextView) findViewById(R.id.textView4);

			Display(getIntent().getStringExtra("json"));

			final String nichengs = nicheng.getText().toString();

			writeWeibo = (ImageButton) findViewById(R.id.imageButton2);
			writeWeibo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(Dierge.this, WriteWeibo.class);
					intent.putExtra("nicheng", nichengs);
					startActivityForResult(intent, 2);
				}
			});

			// 点击Button“微博”
			weiboButton = (Button) findViewById(R.id.buttonWeibo);
			weiboButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setClass(Dierge.this, WeiboList.class);

					json0 = MainActivity.Getjson(json0);

					intent.putExtra("json", json0.toString());

					startActivity(intent);

				}
			});
		}

	}

}
