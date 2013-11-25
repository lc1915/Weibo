package lll.weibo;

import java.util.ArrayList;
import java.util.HashMap;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_list);
		mTweetList = (ListView) findViewById(R.id.listView1);
		Display(getIntent().getStringExtra("json"));
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

				// �������ʽ�滻�ַ�
				Pattern pattern = Pattern.compile("�Ҳ�");
				Matcher matcher = pattern.matcher(newTweets);
				StringBuffer sbr = new StringBuffer();
				while (matcher.find()) {
					matcher.appendReplacement(sbr, "ôô��");
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