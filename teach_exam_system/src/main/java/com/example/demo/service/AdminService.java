package com.example.demo.service;

import com.example.demo.dao.AdminDao;
import com.example.demo.dao.AdminrecordDao;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Adminrecord;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private AdminrecordDao adminrecordDao;
    public Admin getadmin(String usermail){
        return adminDao.getAdminByemail(usermail);
    }
    public boolean updateadmin(String username,String password,int gender,String usermail){
        return adminDao.updateadmin(username,password,gender,usermail)>0;
    }
    
    public ArrayList<Adminrecord> getmyClass(String usermail){
    	return adminrecordDao.getmyrecordC(usermail, 5);
    }
    public ArrayList<Adminrecord> getmyExam(String usermail){
    	return adminrecordDao.getmyrecordC(usermail, 0);
    }

}
