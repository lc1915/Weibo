package lll.weibo;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class MainActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 0;
	private static int RESULT_LOAD_IMAGE1 = 1;

	private DBManager mgr;

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
	EditText xingbiexx = null;
	Button jia = null;
	ArrayList<Tweet> tweets = null;
	String tweet;

	private String nicheng = null;
	private String gender = null;
	private String guanzhushu = null;
	private String fensishu = null;
	private String jianjie = null;
	private String nianling = null;
	private String nation;
	int age;
	int followings;
	int followers;

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

		if (requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK
				&& null != data) {
			selectedImage1 = data.getData();
			Log.e("uri", selectedImage1.toString());
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(selectedImage1));
				imageView2 = (ImageView) findViewById(R.id.imageView2);
				/* ��Bitmap�趨��ImageView */
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
		tweets = new ArrayList<Tweet>();

		// ���ѡ�񱳾���ť������sd����
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

		// ���ѡ��ͷ��ť������sd����
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

		// ������΢����ť
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

		// �����ȷ�ϲ����桱��ť
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
				gender = xingbie.isChecked() ? "   ��" : "   Ů";
				nation = guoji.isChecked() ? "   �й�" : "   ����";

				ArrayList<Person> persons = new ArrayList<Person>();
				Person person1 = new Person(selectedImage.toString(),
						selectedImage1.toString(), nicheng, gender, nation,
						age, followings, followers, jianjie, null, null);
				persons.add(person1);
				mgr.add(persons);

				ListAdapter listAdapter = mTweetList.getAdapter();

				for (int i = 0; i < listAdapter.getCount(); i++) {
					View listItem = mTweetList.getChildAt(i);
					tweet = ((EditText) listItem
							.findViewById(R.id.tweet_list_item_tweet))
							.getText().toString();
					Tweet tweet1 = new Tweet(nicheng, tweet);
					tweets.add(tweet1);
					Log.w("SetupActivity", "Current tweet is \"" + tweet
							+ "\".");
				}
				mgr.add0(tweets);
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, Dierge.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Ӧ�õ����һ��Activity�ر�ʱӦ�ͷ�DB
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
