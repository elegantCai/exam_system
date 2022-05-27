package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.example.demo.dao.TeacherDao;
import com.example.demo.dao.StudentDao;
import com.example.demo.dao.AdminDao;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service("pretendService")
public class PretendService {
	@Autowired 
	private TeacherDao teacherDao;
	@Autowired 
	private StudentDao studentDao;
	@Autowired 
	private AdminDao adminDao;
	
	public void insertNewUserC(int usertype,String usermail) {
		//如果是老师
		if (usertype==0) {
			teacherDao.insertNewteacherC(usermail);
		}
		//如果是学生
		else if (usertype==1) {
			studentDao.insertNewstudentC(usermail);
		}
		//如果是管理员
		else if (usertype==2) {
			adminDao.insertNewadminC(usermail);
		}
	}
	
}
