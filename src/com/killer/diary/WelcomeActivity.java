package com.killer.diary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.killer.service.DiaryService;

public class WelcomeActivity extends Activity {
	private SharedPreferences sp;
	private String ip;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		sp = getSharedPreferences("diary", 0);
		ip = sp.getString("et_ip", "");
		port = sp.getInt("et_port", 0);
		if (ip.equals("") || port == 0) {
			Builder b = new Builder(WelcomeActivity.this);
			b.setTitle("网络配置提示");
			b.setMessage("请重新配置网络连接");
			b.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			b.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(WelcomeActivity.this,ConfigActivity.class);
					startActivity(intent);
				}
			});
			b.create().show();
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Socket socket = new Socket();
					SocketAddress socketAddress = new InetSocketAddress(ip,port);

					try {
						socket.connect(socketAddress, 5000);
						if (socket.isConnected()) {
							DiaryService.init(socket);
							Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
		}

		
	}

	

}
