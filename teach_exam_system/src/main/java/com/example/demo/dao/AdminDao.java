package com.example.demo.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Admin;

public interface AdminDao extends JpaRepository<Admin, Integer> {
	//插入新管理员
		@Modifying
		@Transactional
		@Query(value = "insert into admin(usermail) values(?1)",nativeQuery=true)
			public void insertNewadminC(String usermail);
		
		//插入新学生
	    @Modifying
	    @Transactional
	    @Query(value = "insert into admin(usermail) values(?1)",nativeQuery = true)
		public void insertAdminX(String usermail);
	 
	    //查找特定ID的学生邮箱
		@Query(value = "select usermail from admin where userid=?1",nativeQuery=true)
	    public String getMailbyIdX(int userid);

		//查找特定邮箱的学生ID
		@Query(value = "select userid from admin where usermail=?1",nativeQuery=true)
		 public Integer getIDbyMX(String mail);

		//查找特定ID的学生信息
		@Query(value = "select * from admin where userid=?1",nativeQuery=true)
		public Admin getInfobyIdX(int userid);

		//查找特定ID的学生信息
		@Query(value = "select * from admin where usermail=?1",nativeQuery=true)
		public ArrayList<Admin> getInfobyMX(String usermail);
		
		 //根据用户邮箱
	    @Query(value = "select * from admin where usermail=?1",nativeQuery=true)
	    public Admin getAdminByemail(String usermail);

	    @Query(value = "select count(*) from admin where usermail = ?1 and password = ?2",nativeQuery=true)
	    public int getMatchCount(String usermail, String password);


	    @Query(value = "select password from admin where usermail=?1",nativeQuery = true)
	    public String getPassword(String usermail);

	    @Modifying
	    @Transactional
	    @Query(value = "update admin set username=?1,password=?2,gender=?3 where usermail=?4",nativeQuery = true)
	    public int updateadmin(String username,String password,int gender, String usermail);


	    //插入用户
	    @Modifying
	    @Transactional
	    @Query(value = "insert into admin(gender,password,usermail,username) values(?1,?2,?3,?4)",
	            nativeQuery = true)
	    public void AddUser(int gender,String password,String usermail,String username);

}