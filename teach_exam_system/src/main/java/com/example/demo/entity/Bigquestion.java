package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name="bigquestion") //指定关联的数据库表名
public class Bigquestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String upmail;
	@Column(length = 2048)
	private String content;
	private String picture;
	@Column(length = 2048)
	private String answer;
	private String answerpic;
	private String subject;
	private Integer point;
	private Integer isshow;
	private Float difficulty;
	private Integer isok;
	
	public Bigquestion() {}
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswerpic() {
		return answerpic;
	}
	public void setAnswerpic(String answerpic) {
		this.answerpic = answerpic;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public int getIsshow() {
		return isshow;
	}
	public void setIsshow(Integer isshow) {
		this.isshow = isshow;
	}
	public float getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(float difficulty) {
		this.difficulty = difficulty;
	}
	public int getIsok() {
		return isok;
	}
	public void setIsok(Integer isok) {
		this.isok = isok;
	}
	@Override
	public String toString() {
		return "User: [ userid=" +id;
		}
	

}
