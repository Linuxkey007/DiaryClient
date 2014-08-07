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
		clientSocket = socket;// ��ֵ��socket
		try {
			// ��socket�л�ȡ����/�������ת��Ϊ��������ʹ�õ��ַ���
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
	 *            �û���
	 * @param p
	 *            ����
	 * @return ��¼��ʾ ��¼����
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
	 * ע�����û�
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

	// ��ѯ�ռ���Ϣ�ķ���
	public static List<Diary> queryDairysByUserName() {

		// ��Ϸ��͸�������������
		String str = "#<GETDAIRY>#" + currUserName;

		// ����
		pw.println(str);
		pw.flush();

		// ���շ�������������
		try {
			String msg = reader.readLine();
			// ���������ص���һ��json��ʽ���ַ���
			if (msg.startsWith("#<GETERR>#")) {
				// ��ȡʧ��
				return null;
			} else if (msg.startsWith("#<GETOK>#")) {

				List<Diary> diaryList = new ArrayList<Diary>();
				Log.i("����Ҫ��ֵ��", msg);

				String msgInfo = msg.substring(9);
				Log.i("����Ҫ��ֵ��", msgInfo);

				// ����json��ʽ���ַ���
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
	 * @����ռ�
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
	 * @return�޸ıʼ�
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

	// ������Ϣ
	public void sendMsg(String msg) {
		pw.println(msg);
		pw.flush();
	}

	// ������Ϣ
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
