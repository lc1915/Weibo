package lll.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

	private DBManager mgr;

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

		// 基本信息
		Person person = mgr.query0();
		Integer c = person.age;
		String a = person.nation;
		if (a.equals("   中国")) {
			c = c + 1;
		}
		selectedImage.setImageURI(Uri.parse(person.background));
		selectedImage1.setImageURI(Uri.parse(person.icon));
		nicheng.setText(person.name);
		gender.setText(" " + person.gender);
		nianling.setText("   " + c.toString() + "岁");
		jianjie.setText("简介：" + person.info);

		// 微博相关
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

		// 点击Button“关注数”
		guanzhushuButton = (Button) findViewById(R.id.buttonGzs);
		guanzhushuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Dierge.this, Followings.class);
				startActivity(intent);
			}
		});

		// 点击Button“粉丝数”
		fensishuButton = (Button) findViewById(R.id.buttonFss);
		fensishuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(Dierge.this, Followers.class);
				startActivity(intent);
			}
		});

		// 点击Button“搜索”
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
}
