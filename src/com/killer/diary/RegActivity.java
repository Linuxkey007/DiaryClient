package com.killer.diary;

import com.killer.service.DiaryService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegActivity extends Activity implements OnClickListener{
	private EditText reg_user,reg_pwd;
	private Button btn_reg,btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		reg_user=(EditText)findViewById(R.id.reg_user);
		reg_pwd=(EditText)findViewById(R.id.reg_pwd);
		btn_reg=(Button)findViewById(R.id.btn_reg);
		btn_cancel=(Button)findViewById(R.id.reg_quit);
		btn_reg.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String u=reg_user.getText().toString();
		String p=reg_pwd.getText().toString();
		if(v==btn_reg){
			int flag=DiaryService.reg(u, p);
			if(flag==1){
				Toast.makeText(RegActivity.this, "注册成功，正在跳转到登录页面", Toast.LENGTH_LONG).show();
				Intent intent=new Intent(RegActivity.this,LoginActivity.class);
				startActivity(intent);
			}else if(flag==-1){
				Toast.makeText(RegActivity.this, "用户名已经存在，请重试", Toast.LENGTH_LONG).show();
				reg_user.setText("");
				reg_pwd.setText("");
			}else if(flag==-2){
				Toast.makeText(RegActivity.this, "服务器错误，请重试", Toast.LENGTH_LONG).show();
			}
		}
		
	}




}
