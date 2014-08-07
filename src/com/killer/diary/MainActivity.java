package com.killer.diary;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.killer.entity.Diary;
import com.killer.pup.Ppupwin;
import com.killer.service.DiaryService;

public class MainActivity extends Activity {
	private ImageButton add_diary;
	private ListView diary_list;
	private TextView tv_user;
	List<Diary> diaryList = new ArrayList<Diary>();
	private MyAdapter Myadapter;
	private String user;
	// 自定义的弹出框类
	Ppupwin menuWindow;
	OnClickListener itemsOnClick;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_user = (TextView) findViewById(R.id.tv_user);
		add_diary = (ImageButton) findViewById(R.id.add_diary);
		diary_list = (ListView) findViewById(R.id.diary_list);
		user = DiaryService.currUserName;
		tv_user.setText(user + "！欢迎你使用云笔记");

		loadData();

		add_diary.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				startActivity(intent);
			}
		});
	}

	private void loadData() {
		new Thread(new Runnable() {

			public void run() {
				diaryList = DiaryService.queryDairysByUserName();
				if (diaryList != null) {
					h.sendEmptyMessage(1);

				} else {
					h.sendEmptyMessage(2);
				}

			}
		}).start();
	}

	public class MyAdapter extends BaseAdapter {

		public int getCount() {
			return diaryList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = getLayoutInflater().inflate(R.layout.diary_item, null);
			TextView diary_name = (TextView) view.findViewById(R.id.diary_name);
			TextView diary_content = (TextView) view
					.findViewById(R.id.diary_content);
			Diary diary = diaryList.get(position);
			view.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					// 实例化SelectPicPopupWindow
					menuWindow = new Ppupwin(MainActivity.this,
							itemsOnClick = new OnClickListener() {

								@Override
								public void onClick(View v) {
									menuWindow.dismiss();
									switch (v.getId()) {
									case R.id.btn_add:
										Intent intent = new Intent();
										intent.setClass(
												getApplicationContext(),
												AddActivity.class);
										// startActivity(intent);
										startActivityForResult(intent, 100);
										break;
									case R.id.btn_update:
										Intent intent0 = new Intent(
												MainActivity.this,
												UpdateActivity.class);
										int diary_id = diaryList.get(position)
												.getDiary_id();
										Log.i("+++++++++++++++++++++++++",
												diary_id + "");
										String diary_name = diaryList.get(
												position).getDiary_name();
										String diary_content = diaryList.get(
												position).getDiary_content();
										intent0.putExtra("diary_id", diary_id);
										intent0.putExtra("diary_name",
												diary_name);
										intent0.putExtra("diary_content",
												diary_content);
										// startActivity(intent0);
										startActivityForResult(intent0, 200);
										break;
									case R.id.btn_delete:
										new Thread(new Runnable() {
											int diary_id = diaryList.get(position).getDiary_id();
											public void run() {
												int flag=DiaryService.deldiary(diary_id);	
												Message message = new Message();
												Bundle b = new Bundle();
												b.putInt("flag", flag);
												message.setData(b);
												h.sendMessage(message);
											}
										}).start();
										break;
										
									default:
										break;
									}
								}
							});
					// 显示窗口
					menuWindow.showAtLocation(
							MainActivity.this.findViewById(R.id.diary_list),
							Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
					return false;
				}
			});

			diary_name.setText(diary.getDiary_name());
			diary_content.setText(diary.getDiary_content());

			return view;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			if (resultCode == 1) {
				loadData();
			}
		} else if (requestCode == 200) {
			// 重新加载数据
			loadData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int flag = msg.getData().getInt("flag");
			if(flag==1){
				loadData();
				Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
			}else if(flag==-1){
				Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_LONG).show();
			}
			if (msg.what == 1) {
				if (Myadapter == null) {
					Myadapter = new MyAdapter();
					diary_list.setAdapter(Myadapter);
				} else {
					Myadapter.notifyDataSetChanged();
				}
				Toast.makeText(MainActivity.this, "查询成功", Toast.LENGTH_LONG)
						.show();

			} else if (msg.what == 2) {
				Toast.makeText(MainActivity.this, "查询失败", Toast.LENGTH_LONG)
						.show();

			}
		};
	};
	


}
