package com.killer.diary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends Activity implements OnClickListener {
	private EditText et_ip, et_port;
	private Button btn_setting, btn_cancel;
	private SharedPreferences sp;
	private String ip;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		et_ip = (EditText) findViewById(R.id.et_ip);
		et_port = (EditText) findViewById(R.id.et_port);
		btn_setting = (Button) findViewById(R.id.btn_setting);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_setting.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		sp = this.getSharedPreferences("diary", 0);


	}

	@Override
	public void onClick(View v) {

		if (v == btn_setting) {
			ip = et_ip.getText().toString();
			try {
				port = Integer.parseInt(et_port.getText().toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			Editor editor = sp.edit();
			editor.putString("et_ip", ip);
			editor.putInt("et_port", port);
			editor.commit();
			Toast.makeText(ConfigActivity.this, "设置成功，跳转中......",
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent(ConfigActivity.this,
					WelcomeActivity.class);
			startActivity(intent);

		} else if (v == btn_cancel) {
			Editor editor = sp.edit();
			editor.remove("et_ip");
			editor.remove("et_port");
			editor.commit();
			finish();

		}

	}

}
