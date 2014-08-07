package com.killer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import com.killer.entity.Diary;

public class DiaryService {
	public static Socket clientSocket;
	public static BufferedReader reader;
	public static PrintWriter pw;
	public static String currUserName;

	public static void init(Socket socket) {
		clientSocket = socket;// 赋值给socket
		try {
			// 从socket中获取输入/输出流并转化为方便我们使用的字符流
			reader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream(), "utf-8"));

			pw = new PrintWriter(new OutputStreamWriter(
					clientSocket.getOutputStream(), "utf-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param u
	 *            用户名
	 * @param p
	 *            密码
	 * @return 登录标示 登录方法
	 */
	public static int login(String u, String p) {
		StringBuffer sub = new StringBuffer("#<LOGIN>#");
		sub.append(u + "|" + p);
		pw.println(sub.toString());
		pw.flush();
		String msg;
		try {
			msg = reader.readLine();
			if (msg.startsWith("#<LOGYES>#")) {
				return 1;
			} else if (msg.startsWith("#<NOUSER>#")) {
				return -1;
			} else if (msg.startsWith("#<WPWD>#")) {
				return -2;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;

	}

	/**
	 * 注册新用户
	 * 
	 * @param u
	 * @param p
	 * @return
	 */
	public static int reg(String u, String p) {
		StringBuffer sub = new StringBuffer("#<REG>#");
		sub.append(u + "|" + p);
		pw.println(sub.toString());
		pw.flush();
		String msg;
		try {
			msg = reader.readLine();
			if (msg.startsWith("#<REGYES>#")) {
				return 1;
			} else if (msg.startsWith("#<REPUID>#")) {
				return -1;
			} else if (msg.startsWith("#<SEREE>#")) {
				return -2;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;

	}

	// 查询日记信息的方法
	public static List<Diary> queryDairysByUserName() {

		// 组合发送给服务器的内容
		String str = "#<GETDAIRY>#" + currUserName;

		// 发送
		pw.println(str);
		pw.flush();

		// 接收服务器返回数据
		try {
			String msg = reader.readLine();
			// 服务器返回的是一个json格式的字符串
			if (msg.startsWith("#<GETERR>#")) {
				// 获取失败
				return null;
			} else if (msg.startsWith("#<GETOK>#")) {

				List<Diary> diaryList = new ArrayList<Diary>();
				Log.i("我需要的值是", msg);

				String msgInfo = msg.substring(9);
				Log.i("我需要的值是", msgInfo);

				// 解析json格式的字符串
				JSONObject json = new JSONObject(msgInfo);
				// int count = json.getInt("dairyCount");
				JSONArray arr = json.getJSONArray("diaryInfos");

				for (int i = 0; i < arr.length(); i++) {
					Diary d = new Diary();
					JSONObject o = arr.getJSONObject(i);
					d.setDiary_id(o.getInt("dairyId"));
					Log.i("++++++++++++++++++++++++++++++++++++",
							o.getInt("dairyId") + "");
					d.setDiary_name(o.getString("dairyname"));
					Log.i("++++++++++++++++++++++++++++++++++++++++++",
							o.getString("dairyname"));
					d.setDiary_content(o.getString("dairycontent"));
					Log.i("++++++++++++++++++++++++++++++++++++++++++",
							o.getString("dairycontent"));

					diaryList.add(d);
				}
				return diaryList;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @添加日记
	 */
	public static int add(String diary_name, String diary_content) {
		StringBuffer sub = new StringBuffer("#<NEWDAIRY>#");
		sub.append(currUserName + "|" + diary_name + "|" + diary_content);
		sub.append("#<end>#");
		pw.println(sub.toString());
		pw.flush();
		String msg;
		try {
			msg = reader.readLine();
			if (msg.endsWith("#<SAVEYES>#")) {
				return 1;
			} else if (msg.endsWith("#<SEERR>#")) {
				return -1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;

	}

	/**
	 * 
	 * @param diary_name
	 * @param diary_content
	 * @return修改笔记
	 */

	public static int update(int diary_id, String diary_name,
			String diary_content) {
		StringBuffer sub = new StringBuffer("#<CHADAIRY>#");
		sub.append(diary_id + "|" + diary_name + "|" + diary_content);
		pw.println(sub.toString());
		pw.flush();
		String msg;
		try {
			msg = reader.readLine();
			if (msg.startsWith("#<CHOK>#")) {
				return 1;
			} else if (msg.startsWith("#<SEREE>#")) {
				return -1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;

	}
	
	public static int deldiary(int diary_id) {
		StringBuffer sub = new StringBuffer("#<DELETE>#");
		sub.append(diary_id);
		pw.println(sub.toString());
		pw.flush();
		String msg;
		try {
			msg = reader.readLine();
			if (msg.startsWith("#<DELETEOK>#")) {
				return 1;
			} else if (msg.startsWith("#<DELETEERR>#")) {
				return -1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;

	}

	// 发送消息
	public void sendMsg(String msg) {
		pw.println(msg);
		pw.flush();
	}

	// 接收消息
	public String receiveMsg() {
		String strs = "";
		try {
			strs = new String(reader.readLine().getBytes("utf-8"), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strs;
	}

}
