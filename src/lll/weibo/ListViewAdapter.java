package lll.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListViewAdapter extends SimpleAdapter {
	private Context context;
	private ArrayList<Map<String, Object>> arraylist;
	private int layout;
	String aString;

	public ListViewAdapter(Context context,
			ArrayList<Map<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = (Activity) context;
		this.arraylist = data;
		this.layout = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater factory = LayoutInflater.from(context);
		HashMap<String, Object> item = (HashMap<String, Object>) arraylist
				.get(position);
		View itemView = factory.inflate(layout, parent, false);
		TextView tv = (TextView) itemView.findViewById(android.R.id.text1);
		tv.setText(item.get("tweet").toString());
		tv.setTag(item);
		aString=item.get("tweet").toString();

		

		Pattern pattern = Pattern.compile("你好");
		Matcher matcher = pattern.matcher(item.get("tweet").toString());

		while (matcher.find()) {
			Log.e("aaaaaa", "有@");
			int i = 0;
			char a = item.get("tweet").toString().charAt(0);
			for (i = 0; a != '@'; i++) {
				a = aString.charAt(i);
				Log.e("a", "a");
			}
			int j = 0;
			char b = item.get("tweet").toString().charAt(0);
			for (j = 0; b != ' '; j++) {
				b = item.get("tweet").toString().charAt(j);
				Log.e("b", "b");
			}
			
			// 将文本转换成SpannableString对象
			SpannableString spannableString1 = new SpannableString(item.get("tweet").toString());

			// 将text1中的所有文本设置成ClickableSpan对象，并实现了onClick方法
			spannableString1.setSpan(new ClickableSpan() {
				// 在onClick方法中可以编写单击链接时要执行的动作
				@Override
				public void onClick(View widget) {
					
				}
			}, i - 1, j - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// 使用SpannableString对象设置两个TextView控件的内容
			tv.setText(spannableString1);

			// 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
			tv.setMovementMethod(LinkMovementMethod.getInstance());
		}

		if (position == 1) {
			// 需要做特殊处理的item（此处假设第三个item需要显示超链接）
			tv.setText(Html
					.fromHtml("<a href=\"http://www.google.com\">这是一个超链接</a> "));
			tv.setMovementMethod(LinkMovementMethod.getInstance());
		}
		return itemView;

	}
}