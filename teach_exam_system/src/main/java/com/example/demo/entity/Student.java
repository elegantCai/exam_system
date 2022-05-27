package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;


@Entity
@Table(name="studentinfo") 
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userid;
	@Column(unique =true)
	private String usermail;
	private String userpassword;
	private String username;
	private Integer usergender;
	private String usertype;
	private String usergrade;
	private String useracademy;
	private String usermajor;
	private String userheadshot;
	
	
	public Student() {}
 	
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
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
	public int getUsergender() {
		return usergender;
	}
	public void setUsergender(int usergender) {
		this.usergender = usergender;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getUsergrade() {
		return usergrade;
	}
	public void setUsergrade(String usergrade) {
		this.usergrade = usergrade;
	}
	public String getUseracademy() {
		return useracademy;
	}
	public void setUseracademy(String useracademy) {
		this.useracademy = useracademy;
	}
	public String getUserheadshot() {
		return userheadshot;
	}
	public void setUserheadshot(String userheadshot) {
		this.userheadshot = userheadshot;
	}
	public String getUsermajor() {
		return usermajor;
	}
	public void setUsermajor(String usermajor) {
		this.usermajor = usermajor;
	}
	public String getUserpassword() {
		return userpassword;
	}
	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}
	
	
}

