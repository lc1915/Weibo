package lll.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	TextView jj = null;
	Button weiboButton = null;
	Button guanzhushuButton = null;
	Button fensishuButton = null;
	Button guanzhu = null;
	Button search = null;
	ArrayList<HashMap<String, Object>> mTweets = null;
	SimpleAdapter mAdapter = null;
	HashMap<String, Object> item = null;

	String a = null;
	TextView t = null;
	private DBManager mgr;

	JSONObject json0;
	String mSession = null;
	int mUserId = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		// Display(getIntent().getStringExtra("json"));
		mgr = new DBManager(this);
		
		//基本信息
		Person person = mgr.query0();
		Integer c = person.age;
		String a=person.nation;
		if(a.equals("   中国")){
			c=c+1;
		}
		selectedImage.setImageURI(Uri.parse(person.background));
		selectedImage1.setImageURI(Uri.parse(person.icon));
		nicheng.setText(person.name);
		gender.setText(" " + person.gender);
		nianling.setText("   " + c.toString() + "岁");
		jianjie.setText("简介：" + person.info);

		//微博相关
		List<Tweet> tweets = mgr.query1();
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int i = 0;
		for (Tweet tweet0 : tweets) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", tweet0.name);
			map.put("tweet", tweet0.tweet);
			list.add(map);
			i++;
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, new String[] { "name",
						"tweet" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		mTweetList.setAdapter(adapter);

		mTweets1.setText("微博(" + i + ")");
		wbs.setText(" " + i + " ");

		// 查询登陆用户的关注数
		List<Fan> fans = mgr.query4();
		ArrayList<Map<String, String>> list0 = new ArrayList<Map<String, String>>();
		int j = 0;
		for (Fan fan0 : fans) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", fan0.name);
			map.put("ilike", fan0.ilike);
			list0.add(map);
			j++;
		}
		guanzhushu.setText("  " + j + "　");

		// 查询登陆用户的粉丝数
		List<Fan> fans0 = mgr.query5();
		ArrayList<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
		int k = 0;
		for (Fan fan0 : fans0) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", fan0.name);
			map.put("ilike", fan0.ilike);
			list1.add(map);
			k++;
		}
		fensishu.setText("  " + k + "　");

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
				// json0 = MainActivity.Getjson(json0);
				// intent.putExtra("json", json0.toString());
				startActivity(intent);
			}
		});

		//点击Button“关注数”
		guanzhushuButton = (Button) findViewById(R.id.buttonGzs);
		guanzhushuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Dierge.this, Followings.class);
				startActivity(intent);
			}
		});

		//点击Button“粉丝数”
		fensishuButton = (Button) findViewById(R.id.buttonFss);
		fensishuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(Dierge.this, Followers.class);
				startActivity(intent);
			}
		});

		//点击Button“搜索”
		search = (Button) findViewById(R.id.button1);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(Dierge.this, Search.class);
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

		if (2 == requestCode && resultCode == 4) {
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

			// Display(getIntent().getStringExtra("json"));

			mgr = new DBManager(this);
			Person person = mgr.query2();
			Integer a = person.followings;
			Integer b = person.followers;
			Integer c = person.age;
			selectedImage.setImageURI(Uri.parse(person.background));
			selectedImage1.setImageURI(Uri.parse(person.icon));
			nicheng.setText(person.name);
			gender.setText(" " + person.gender);
			guanzhushu.setText("  " + a.toString());
			fensishu.setText("  " + b.toString());
			nianling.setText("   " + c.toString() + "岁");
			jianjie.setText("简介：" + person.info);

			List<Tweet> tweets = mgr.query3();
			ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
			int i = 0;
			for (Tweet tweet0 : tweets) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", tweet0.name);
				map.put("tweet", tweet0.tweet);
				list.add(map);
				i++;
			}
			SimpleAdapter adapter = new SimpleAdapter(this, list,
					android.R.layout.simple_list_item_2, new String[] { "name",
							"tweet" }, new int[] { android.R.id.text1,
							android.R.id.text2 });
			mTweetList.setAdapter(adapter);

			mTweets1.setText("微博(" + i + ")");
			wbs.setText(" " + i + " ");

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
					// json0 = MainActivity.Getjson(json0);
					// intent.putExtra("json", json0.toString());
					startActivity(intent);
				}
			});

			guanzhu = (Button) findViewById(R.id.guanzhu);
			guanzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<Fan> fans = new ArrayList<Fan>();
					Fan fan1 = new Fan(SignIn.username, Search.theusername);
					fans.add(fan1);
					mgr.add2(fans);
					Toast.makeText(getApplicationContext(), "关注成功！",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		Toast.makeText(getApplicationContext(), "进去了啊啊啊", Toast.LENGTH_LONG)
				.show();
	}

}
