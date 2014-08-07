package com.killer.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.killer.service.DiaryService;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText et_user, et_pwd;
	private Button btn_login, btn_quit;
	private String u, p;
	private TextView tv_reg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		et_user = (EditText) findViewById(R.id.et_user);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_quit = (Button) findViewById(R.id.btn_quit);
		tv_reg = (TextView) findViewById(R.id.tv_reg);
		btn_login.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
		tv_reg.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == btn_login) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					u = et_user.getText().toString();
					p = et_pwd.getText().toString();
					DiaryService.currUserName=u;
					int flag = DiaryService.login(u, p);
					Message message = new Message();
					Bundle b = new Bundle();
					b.putInt("flag", flag);
					message.setData(b);
					handler.sendMessage(message);
				}
			}).start();
		} else if (v == btn_quit) {
			finish();

		} else if (v == tv_reg) {
			Intent intent = new Intent(LoginActivity.this, RegActivity.class);
			startActivity(intent);
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int flag = msg.getData().getInt("flag");
			if (flag == 1) {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
			}

		};
	};

}
