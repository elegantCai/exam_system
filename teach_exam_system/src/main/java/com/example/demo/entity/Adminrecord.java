package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name="adminrecord") //指定关联的数据库表名

public class Adminrecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String usermail;
	private Integer treattype;
	private Integer treatid;
	private String comment;
	private Integer treatresult;
	
	public Adminrecord() {}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsermail() {
		return usermail;
	}
	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}
	public Integer getTreattype() {
		return treattype;
	}
	public void setTreattype(Integer treattype) {
		this.treattype = treattype;
	}
	public Integer getTreatid() {
		return treatid;
	}
	public void setTreatid(Integer treatid) {
		this.treatid = treatid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getTreatresult() {
		return treatresult;
	}
	public void setTreatresult(Integer treatresult) {
		this.treatresult = treatresult;
	}

}
