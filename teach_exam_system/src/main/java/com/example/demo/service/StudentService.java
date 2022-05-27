package com.example.demo.service;

import com.example.demo.entity.Choiceanswered;
import com.example.demo.entity.Classes;
import com.example.demo.entity.Distribution;
import com.example.demo.entity.Examinfo;
import com.example.demo.entity.Student;
import com.example.demo.dao.TeacherDao;
import com.example.demo.dao.BigquestionDao;
import com.example.demo.dao.ClassDao;
import com.example.demo.dao.ClassandstudentDao;
import com.example.demo.dao.DistributionDao;
import com.example.demo.dao.ExaminfoDao;
import com.example.demo.dao.StudentDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@Service("studentService")
public class StudentService {
	@Autowired 
	private ClassDao classDao;

	@Autowired 
	private StudentDao studentDao;

	@Autowired 
	private ClassandstudentDao classandstudentDao;
	@Autowired 
	private DistributionDao distributionDao;
	@Autowired 
	private ExaminfoDao examinfoDao;
	
	public ArrayList<Classes> getStudentClassY(int studentid,int state){
		return getStudentClassY(studentid, state);
	}
	public Integer getIDbyMX(String mail) {
		return getIDbyMX(mail);
	}
	
	public int classCount(int classid) {
		return classandstudentDao.classCount(classid);
	}
	
	public ArrayList<Integer> getClassidByStudentidY(Integer studentid){
		return classandstudentDao.getClassidByStudentidY(studentid);
	}
	
	public Classes getClassInformationY(int classid){
		return classDao.getClassInformationY(classid);
	}
	
	public ArrayList<Integer> getmyexamsidY(String usermail,int state){
		return distributionDao.getmyexamsidY(usermail,state);
	}
	public Examinfo getExamByIdY(Integer examid){
		return examinfoDao.getExamByIdY(examid);
	}

	public void updatestudentAcademyY(String useracademy,int id) {
		studentDao.updatestudentAcademyY(useracademy, id);
	}

	public void updatestudentGradeY(String usergrade,int id) {
		studentDao.updatestudentGradeY(usergrade, id);
	}

	public void updatestudentMajorY(String usermajor,int id) {
		studentDao.updatestudentMajorY(usermajor, id);
	}

	public void updatestudentPasswordY(String userpassord,int id) {
		studentDao.updatestudentPasswordY(userpassord, id);
	}
	
	public String getpasswordbyIDY(int userid) {
		return studentDao.getpasswordbyIDY(userid);
	}
	
	/*************************************************以上yxr****************************************/

    public boolean addstuent(String useracademy,int usergender, String usergrade,
                             String userheadshot,String usermail, String usermajor,
                             String username, String userpassword,String usertype){

        return studentDao.AddStudent(useracademy,usergender,usergrade,userheadshot,
                usermail,usermajor,username,userpassword,usertype)>0;

    }

    public String findmail(String usermail) {
        // TODO Auto-generated method stub
        if(studentDao.getStudentByemail(usermail)!=null){
            return "exist";
        }
        return "noexist";
    }
    
    public ArrayList<Student> getStudentnameByemail(String usermail) {
    	return studentDao.getStudentnameByemail(usermail);
    }

    public Integer getIDbyemailY(String mail) {
    	return studentDao.getIDbyemailY(mail);
    }
}
