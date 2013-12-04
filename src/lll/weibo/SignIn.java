package lll.weibo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignIn extends Activity {

	EditText mUsername = null;
	EditText mPassword = null;
	Button login = null;
	Button regist = null;
	SharedPreferences preferences;
	static String username;
	static String password;
	
	private DBManager mgr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		
		mgr = new DBManager(this);

		mUsername = (EditText) findViewById(R.id.editText1);
		mPassword = (EditText) findViewById(R.id.editText2);

		regist = (Button) findViewById(R.id.regist_button);
		regist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				username = mUsername.getText().toString();
				password = mPassword.getText().toString();
				
				
				API api = API.GetInstance(SignIn.this);
				Response res = api.Register(username, password);

				switch (res.GetStatusCode()) {
				case Response.STATUS_CODE_SUCCESS:

					SharedPreferences sharedPreferences = getSharedPreferences(
							"login", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("username", username);
					editor.putString("password", password);
					editor.commit();
					
					ArrayList<Sign> signs = new ArrayList<Sign>();
					Sign sign1 = new Sign(username,password);
					signs.add(sign1);
					mgr.add1(signs);

					Intent intent = new Intent(SignIn.this, MainActivity.class);
					intent.putExtra("session", res.GetSession());
					intent.putExtra("user", res.GetExtra());
					startActivity(intent);
					break;
				default:
					Toast.makeText(SignIn.this, res.GetExtra(),
							Toast.LENGTH_SHORT).show();
					break;
				}

			}
		});

		login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				username = mUsername.getText().toString();
				password = mPassword.getText().toString();

				API api = API.GetInstance(SignIn.this);
				Response res = api.Login(username, password);

				switch (res.GetStatusCode()) {
				case Response.STATUS_CODE_SUCCESS:
					SharedPreferences sharedPreferences = getSharedPreferences(
							"login", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("username", username);
					editor.putString("password", password);
					editor.commit();
					
					Intent intent = new Intent(SignIn.this, Dierge.class);
					intent.putExtra("session", res.GetSession());
					intent.putExtra("user", res.GetExtra());
					startActivity(intent);
					break;
				default:
					Toast.makeText(SignIn.this, res.GetExtra(),
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});

		SharedPreferences sharedPreferences = getSharedPreferences("login",
				Context.MODE_PRIVATE);
		mUsername.setText(sharedPreferences.getString("username", ""));
		mPassword.setText(sharedPreferences.getString("password", ""));
	}
}
