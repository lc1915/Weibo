package lll.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Followers0 extends Activity {
	ListView FollowerList = null;
	HashMap<String, Object> item = null;
	private DBManager mgr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_list);
		mgr = new DBManager(this);
		FollowerList = (ListView) findViewById(R.id.listView1);

		List<Fan> fans = mgr.query50();
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Fan fan0 : fans) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", fan0.name);
			map.put("ilike", fan0.ilike);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, new String[] { "name",
						null }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		FollowerList.setAdapter(adapter);
	}
}
