package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name="admin") //指定关联的数据库表名

public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userid;
	@Column(unique =true)
	private String usermail;
	private String username;
	private String password;
	private Integer gender;
	
	public Admin() {}
	
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
	public Integer getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return "User: [ userid=" + userid + ","
				+ " username=" + username + ",usermail="+usermail+","
				+  "password=" + password +"]";
		}

}
