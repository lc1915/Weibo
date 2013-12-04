package lll.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class WeiboList0 extends Activity {
	ListView mTweetList = null;
	HashMap<String, Object> item = null;
	private DBManager mgr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_list);
		
		mgr = new DBManager(this);
		mTweetList = (ListView) findViewById(R.id.listView1);
		
		List<Tweet> tweets = mgr.query10();
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
}