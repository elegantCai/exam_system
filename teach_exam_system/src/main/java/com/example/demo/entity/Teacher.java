package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name="teacher") //指定关联的数据库表名
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userid;
	@Column(unique =true)
	private String usermail;
	private String username;
	private String password;
	private String academy;
	private String department;
	private Integer jobtitle;
	private Integer gender;
	private String headshot;
	
	public Teacher() {}
	
	public Integer getUserid() {
		return userid;
	}
	public void setId(Integer userid) {
		this.userid = userid;
	}
	public String getUsermail() {
		return usermail;
	}
	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String userpassword) {
		this.password = userpassword;
	}
	public String getAcademy() {
		return academy;
	}
	public void setAcademy(String academy) {
		this.academy = academy;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Integer getJobtitle() {
		return jobtitle;
	}
	public void setJobtitle(int jobtitle) {
		this.jobtitle = jobtitle;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getHeadshot() {
		return headshot;
	}
	public void setHeadshot(String headshot) {
		this.headshot = headshot;
	}
	@Override
	public String toString() {
		return "User: [ userid=" + userid + ","
				+ " username=" + username + ",usermail="+usermail+","
				+  "password=" + password +"]";
		}
	

}
