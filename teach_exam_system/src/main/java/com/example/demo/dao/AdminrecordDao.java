package com.example.demo.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Adminrecord;

public interface AdminrecordDao extends JpaRepository<Adminrecord, Integer>  {
	//插入新审核记录
		//treattype：   0：审核考试  1：审核选择题 2：审核填空题 3：审核判断题 4：审核大题 5：审核班级
		//treatresult：    0：审核通过  1：审核不通过  2：审核结课通过
	@Modifying
	@Transactional
	@Query(value = "insert into adminrecord(usermail,treattype,treatid,comment,treatresult) values(?1,?2,?3,?4,?5)",nativeQuery=true)
		public void insertNewrecordC(String usermail,int treattype,int treatid,String comment,int treatresult);
	
	//获取某人的啥样的审核记录
	@Query(value = "select * from adminrecord where usermail=?1 and treattype=?2",nativeQuery=true)
	public ArrayList<Adminrecord> getmyrecordC(String usermail,int treattype);
}
