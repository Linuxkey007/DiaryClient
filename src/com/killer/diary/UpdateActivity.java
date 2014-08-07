package com.killer.diary;

import com.killer.service.DiaryService;

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

public class UpdateActivity extends Activity {
	private EditText update_diary_name, update_diary_content;
	private Button update_yes, update_cancel;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		update_diary_name = (EditText) findViewById(R.id.update_diary_name);
		update_diary_content = (EditText) findViewById(R.id.update_diary_content);
		update_yes = (Button) findViewById(R.id.update_yes);
		update_cancel = (Button) findViewById(R.id.update_cancel);
		Intent intent = getIntent();
		final int diary_id = intent.getIntExtra("diary_id", 1);
		String diary_name = intent.getStringExtra("diary_name");
		String diary_content = intent.getStringExtra("diary_content");
		update_diary_name.setText(diary_name);
		update_diary_content.setText(diary_content);
		update_yes.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final int id = diary_id;
				final String name = update_diary_name.getText().toString()
						.trim();
				final String content = update_diary_content.getText()
						.toString().trim();
				new Thread(new Runnable() {

					public void run() {
						int flag = DiaryService.update(id, name, content);
						Message message = new Message();
						Bundle b = new Bundle();
						b.putInt("flag", flag);
						message.setData(b);
						message.what = 1;
						handler.sendMessage(message);

					}
				}).start();

			}

		});
		update_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(UpdateActivity.this,
						MainActivity.class);
				startActivity(intent);

			}
		});

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int flag = msg.getData().getInt("flag");
			if (flag == 1) {
				Toast.makeText(UpdateActivity.this, "修改成功", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent(UpdateActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else if (flag == -1) {
				Toast.makeText(UpdateActivity.this, "修改失败", Toast.LENGTH_LONG)
						.show();
			}

		};
	};
}
