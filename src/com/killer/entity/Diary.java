package com.killer.entity;

import java.io.Serializable;

public class Diary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4169409325850195194L;
	private int diary_id;
	private String diary_name;
	private String diary_content;
	

	public int getDiary_id() {
		return diary_id;
	}
	public void setDiary_id(int diary_id) {
		this.diary_id = diary_id;
	}
	public String getDiary_name() {
		return diary_name;
	}
	public void setDiary_name(String diary_name) {
		this.diary_name = diary_name;
	}
	public String getDiary_content() {
		return diary_content;
	}
	public void setDiary_content(String diary_content) {
		this.diary_content = diary_content;
	}
	
	

}
