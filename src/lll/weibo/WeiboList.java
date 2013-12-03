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

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class WeiboList extends Activity {
	ListView mTweetList = null;
	HashMap<String, Object> item = null;
	private DBManager mgr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_list);
		mgr = new DBManager(this);
		mTweetList = (ListView) findViewById(R.id.listView1);
		//Display(getIntent().getStringExtra("json"));
		List<Tweet> tweets = mgr.query1();
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Tweet tweet0 : tweets) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", tweet0.name);
			map.put("tweet", tweet0.tweet);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, new String[] { "name",
						"tweet" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		mTweetList.setAdapter(adapter);
	}

	void Display(String jsonString) {

		String newTweets = null;
		String newTweets1 = null;

		try {
			JSONObject json = new JSONObject(jsonString);

			JSONArray tweets = json.getJSONArray("tweets");

			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < tweets.length(); i++) {
				item = new HashMap<String, Object>();
				newTweets = tweets.getString(i);

				// 正则表达式替换字符
				Pattern pattern = Pattern.compile("我操");
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
}
