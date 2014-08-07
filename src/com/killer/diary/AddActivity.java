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
import android.widget.Toast;

import com.killer.service.DiaryService;

public class AddActivity extends Activity implements OnClickListener {
	private EditText add_diary_name, add_diary_content;
	private Button add_yes, add_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		add_diary_name = (EditText) findViewById(R.id.add_diary_name);
		add_diary_content = (EditText) findViewById(R.id.add_diary_content);
		add_yes = (Button) findViewById(R.id.add_yes);
		add_cancel = (Button) findViewById(R.id.add_cancel);
		add_yes.setOnClickListener(this);
		add_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final String diary_name = add_diary_name.getText().toString().trim();
		final String diary_content = add_diary_content.getText().toString()
				.trim();
		if (v == add_yes) {
			if (diary_name.equals("") || diary_content.equals("")) {
				Toast.makeText(AddActivity.this, "请将日记信息填写完整",
						Toast.LENGTH_LONG).show();
			} else {
				new Thread(new Runnable() {

					@Override
					public void run() {
						int flag = DiaryService.add(diary_name, diary_content);
						Message message = new Message();
						Bundle b = new Bundle();
						b.putInt("flag", flag);
						message.setData(b);
						message.what = 1;
						handler.sendMessage(message);
					}
				}).start();
			}
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			int flag = msg.getData().getInt("flag");
			if (flag == 1) {
				Intent intent = new Intent(AddActivity.this,
						MainActivity.class);
				startActivity(intent);
				Toast.makeText(AddActivity.this, "笔记创建成功", Toast.LENGTH_LONG)
						.show();
			} else if (flag == -1) {
				Toast.makeText(AddActivity.this, "笔记创建失败", Toast.LENGTH_LONG)
						.show();
			}
			AddActivity.this.finish();
		};
	};
}
