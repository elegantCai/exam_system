package com.example.demo.service;
import com.example.demo.entity.Admin;
import com.example.demo.dao.AdminDao;
import com.example.demo.dao.StudentDao;
import com.example.demo.dao.TeacherDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
@Service
public class LoginService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private TeacherDao teacherDao;
    public boolean hasmatchadmin(String  usermail,String password){
        return  adminDao.getMatchCount(usermail,password)>0;
    }
    public boolean hasmatchstudent(String  usermail,String password){
        return  studentDao.getMatchCount(usermail, password)>0;
    }
    public boolean hasmatchteacher(String  usermail,String password){
        return  teacherDao.getMatchCount(usermail, password)>0;
    }
    public String getadminpassword(String usermail){
        return adminDao.getPassword(usermail);
    }
    public String getstudentpassword(String usermail){
        return studentDao.getPassword(usermail);
    }
    public String getteacherpassword(String usermail){

        return teacherDao.getPassword(usermail);

    }


}
