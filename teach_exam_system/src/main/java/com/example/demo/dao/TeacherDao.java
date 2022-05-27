package com.example.demo.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Teacher;

public interface TeacherDao extends JpaRepository<Teacher, Integer> {
	//插入新管理员
		@Modifying
		@Transactional
		@Query(value = "insert into teacher(usermail) values(?1)",nativeQuery=true)
			public void insertNewteacherC(String usermail);
		
		//插入新学生
	    @Modifying
	    @Transactional
	    @Query(value = "insert into teacher(usermail) values(?1)",nativeQuery = true)
		public void insertAdminX(String usermail);
	 
	    //查找特定ID的学生邮箱
		@Query(value = "select usermail from teacher where userid=?1",nativeQuery=true)
	    public String getMailbyIdX(int userid);

		//查找特定邮箱的学生ID
		@Query(value = "select userid from teacher where usermail=?1",nativeQuery=true)
		 public Integer getIDbyMX(String mail);

		//查找特定ID的学生信息
		@Query(value = "select * from teacher where userid=?1",nativeQuery=true)
		public Teacher getInfobyIdX(int userid);

		//查找特定ID的学生信息
		@Query(value = "select * from teacher where usermail=?1",nativeQuery=true)
		public ArrayList<Teacher> getInfobyMX(String usermail);
		
		 //根据用户邮箱
	    @Query(value = "select * from teacher where usermail=?1",nativeQuery=true)
	    public Teacher getTeacherByemail(String usermail);

	    @Query(value = "select count(*) from teacher where usermail = ?1 and password = ?2",nativeQuery=true)
	    public int getMatchCount(String usermail, String password);


	    @Query(value = "select password from teacher where usermail=?1",nativeQuery = true)
	    public String getPassword(String usermail);

	    @Modifying
	    @Transactional
	    @Query(value = "update teacher set academy=?1,department=?2,gender=?3," +
	            "headshot=?4,jobtitle=?5,password=?6,username=?7 where usermail=?8",nativeQuery = true)
	    public int updateteacher(String academy,String department,int gender,
	                             String headshot,int jobtitle,String password,
	                             String username,String usermail);

	    //插入用户
	    @Modifying
	    @Transactional
	    @Query(value = "insert into teacher(academy,department,gender," +
	            "headshot,jobtitle,password,usermail,username) values(?1,?2,?3,?4,?5,?6,?7,?8)",
	            nativeQuery = true)
	    public int AddTeacher(String academy,String department,int gender,
	                           String headshot,int jobtitle,String password,
	                           String usermail,String username);


}