package com.example.demo.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Student;

public interface StudentDao extends JpaRepository<Student, Integer> {
	//插入新学生
		@Modifying
		@Transactional
		@Query(value = "insert into studentinfo(usermail) values(?1)",nativeQuery=true)
			public void insertNewstudentC(String usermail);
		//插入新学生
	    @Modifying
	    @Transactional
	    @Query(value = "insert into studentinfo(usermail) values(?1)",nativeQuery = true)
		public void insertStudentX(String usermail);
	 
	    //查找特定ID的学生邮箱
		@Query(value = "select usermail from studentinfo where userid=?1",nativeQuery=true)
	    public String getMailbyIdX(int userid);

		//查找特定邮箱的学生ID
		@Query(value = "select userid from studentinfo where usermail=?1",nativeQuery=true)
		 public Integer getIDbyMX(String mail);

		//查找特定ID的学生信息
		@Query(value = "select * from studentinfo where userid=?1",nativeQuery=true)
		public Student getInfobyIdX(int userid);

		//查找特定ID的学生信息
		@Query(value = "select * from studentinfo where usermail=?1",nativeQuery=true)
		public ArrayList<Student> getInfobyMX(String usermail);	
		
		//根据用户邮箱
	    @Query(value = "select * from studentinfo where usermail = ?1",nativeQuery=true)
	    public Student getStudentByemail(String usermail);

	    @Query(value = "select count(*) from studentinfo where usermail = ?1 and userpassword = ?2",nativeQuery=true)
	    public int getMatchCount(String usermail, String userpassword);


	    @Query(value = "select userpassword from studentinfo where usermail=?1",nativeQuery = true)
	    public String getPassword(String usermail);

	    //插入用户
	    @Modifying
	    @Transactional
	    @Query(value = "insert into studentinfo(useracademy,usergender,usergrade," +
	            "userheadshot,usermail,usermajor,username," +
	            "userpassword,usertype) values(?1,?2,?3,?4,?5,?6,?7,?8,?9)",
	            nativeQuery = true)
	    public int AddStudent(String useracademy,int usergender,String usergrade,
	                           String userheadshot,String usermail,String usermajor,
	                           String username,String userpassword,String usertype);
	    
	    /*************************************************yxr****************************************/
		//查找特定邮箱的学生ID
		@Query(value = "select userid from studentinfo where usermail=?1",nativeQuery=true)
		 public Integer getIDbyemailY(String mail);
		
		//查找特定id学生的密码
		@Query(value = "select userpassword from studentinfo where userid=?1",nativeQuery=true)
		 public String getpasswordbyIDY(int userid);

		//更新这位同学的学院状态
		@Modifying
		@Transactional
		@Query(value = "update studentinfo set useracademy=?1 where userid=?2",nativeQuery=true)
			public void updatestudentAcademyY(String useracademy,int id);
		@Modifying
		@Transactional
		@Query(value = "update studentinfo set usergrade=?1 where userid=?2",nativeQuery=true)
			public void updatestudentGradeY(String usergrade,int id);
		@Modifying
		@Transactional
		@Query(value = "update studentinfo set usermajor=?1 where userid=?2",nativeQuery=true)
			public void updatestudentMajorY(String usermajor,int id);
		@Modifying
		@Transactional
		@Query(value = "update studentinfo set userpassword=?1 where userid=?2",nativeQuery=true)
			public void updatestudentPasswordY(String userpassword,int id);
		
		//根据用户邮箱
	    @Query(value = "select * from studentinfo where usermail = ?1",nativeQuery=true)
	    public ArrayList<Student> getStudentnameByemail(String usermail);

}
