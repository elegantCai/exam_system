package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name="fillinquestion")
public class Fillinquestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String upmail;
	@Column(length=2048)
	private String content;
	private String picture;
	private String answer;
	private Integer point;
	private Integer isshow;
	private Float difficulty;
	private Integer isok;
	private String subject;
	
	public Fillinquestion() {}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUpmail() {
		return upmail;
	}
	public void setUpmail(String upmail) {
		this.upmail = upmail;
	}

	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getIsshow() {
		return isshow;
	}
	public void setIsshow(Integer isshow) {
		this.isshow = isshow;
	}

	public Float getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(Float difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getIsok() {
		return isok;
	}
	public void setIsok(Integer isok) {
		this.isok = isok;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Override
	public String toString() {
		return "fillinans: [id=" + id + ", upmail=" + upmail + ","
				+ " content=" + content + ", picture=" + picture + ","
				+ "answer=" + answer + "," +   "point=" + point + "," 
				+ "isshow=" + isshow + "," +   "difficulty=" + difficulty + "," 
				+ "isok=" +isok+ "," +   "subject=" + subject + "," + "]";
	}
}
