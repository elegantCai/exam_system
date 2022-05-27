package com.example.demo.service;
import com.example.demo.dao.TeacherDao;
import com.example.demo.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
@Service
public class TeacherService {
    @Autowired
    private TeacherDao teacherDao;




    public boolean addteacher(String academy,String department,int gender,
                              String headshot,int jobtitle,String password,
                              String usermail,String username){
        return teacherDao.AddTeacher(academy,department,gender,headshot,
                jobtitle,password,usermail,username)>0;

    }

    public String findmail(String usermail) {
        // TODO Auto-generated method stub
        if(teacherDao.getTeacherByemail(usermail)!=null){
            return "exist";
        }
        return "noexist";
    }

    public Teacher getteacher(String usermail){
        return teacherDao.getTeacherByemail(usermail);
    }


    public boolean updateteacher(String academy,String department,int gender,
                                 String headshot,int jobtitle,String password,
                                 String username,String usermail){
        return teacherDao.updateteacher(academy,department,gender,headshot,
                jobtitle,password,username,usermail)>0;
    }

}
