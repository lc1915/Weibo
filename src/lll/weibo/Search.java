package lll.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Search extends Activity {

	EditText editText;
	Button searchButton;
	static String theusername;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editText = (EditText) findViewById(R.id.editText1);
				theusername = editText.getText().toString();
				Intent intent = new Intent();
				intent.setClass(Search.this, Dierge0.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
