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

		

		Pattern pattern = Pattern.compile("���");
		Matcher matcher = pattern.matcher(item.get("tweet").toString());

		while (matcher.find()) {
			Log.e("aaaaaa", "��@");
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
			
			// ���ı�ת����SpannableString����
			SpannableString spannableString1 = new SpannableString(item.get("tweet").toString());

			// ��text1�е������ı����ó�ClickableSpan���󣬲�ʵ����onClick����
			spannableString1.setSpan(new ClickableSpan() {
				// ��onClick�����п��Ա�д��������ʱҪִ�еĶ���
				@Override
				public void onClick(View widget) {
					
				}
			}, i - 1, j - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// ʹ��SpannableString������������TextView�ؼ�������
			tv.setText(spannableString1);

			// �ڵ�������ʱ������Ҫִ�еĶ���������������MovementMethod����
			tv.setMovementMethod(LinkMovementMethod.getInstance());
		}

		if (position == 1) {
			// ��Ҫ�����⴦���item���˴����������item��Ҫ��ʾ�����ӣ�
			tv.setText(Html
					.fromHtml("<a href=\"http://www.google.com\">����һ��������</a> "));
			tv.setMovementMethod(LinkMovementMethod.getInstance());
		}
		return itemView;

	}
}