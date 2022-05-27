package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Distribution;


public interface DistributionDao extends JpaRepository<Distribution, Integer> {
	//0：已考 1：缺考 2：未开始 3:批改完成 4:签到中 5：考试中
	//考试发布了就直接往分发表里插入东西
	@Modifying
	@Transactional
	@Query(value = "insert into distribution(examid,studentemail,absent) values(?1,?2,2)",nativeQuery=true)
		public void insertNewQuestionC(int examid,String answermail);
	//获取这位同学在这场考试的分发表id
	@Query(value = "select id from distribution where studentemail=?1 and examid=?2",nativeQuery=true)
		public int getmythisexamC(String answermail,int examid);
	//更新这位同学的签到状态
	@Modifying
	@Transactional
	@Query(value = "update distribution set absent=?1 where id=?2",nativeQuery=true)
		public void updatemyDstateC(int state,int did);
	
	//获取某位同学的某种状态的分发表
	@Query(value = "select * from distribution where studentemail=?1 and absent=?2 order by id desc",nativeQuery=true)
		public ArrayList<Distribution> getmyexamsC(String answermail,int absent);
	//根据考试ID获取分发表
	@Query(value = "select id from distribution where examid=?1",nativeQuery=true)
		public ArrayList<Integer> getDofexamsC(int examid);
	//根据考试ID和状态获取分发表
	@Query(value = "select id from distribution where examid=?1 and absent=?2",nativeQuery=true)
	public ArrayList<Integer> getDSofexamsC(int examid,int absent);
	//考试开始签到了根据考试ID更新分发表
	@Modifying
	@Transactional
	@Query(value = "update distribution set absent=4 where examid=?1",nativeQuery=true)
		public void updateCkinC(int examid);
	//根据考试ID和状态作答邮箱更新分发表
	@Modifying
	@Transactional
	@Query(value = "update distribution set absent=?1,totalpoint=0 where examid=?2 and studentemail=?3",nativeQuery=true)
		public void updatemyCK(int state,int examid,String studentemail);
	
	//更新总分
	@Modifying
	@Transactional
	@Query(value = "update distribution set absent=0,totalpoint=?1 where examid=?2 and studentemail=?3",nativeQuery=true)
		public void updateTPC(float totalpoint,int examid,String studentemail);
	//获取这位同学在这场考试的分发表
		@Query(value = "select * from distribution where studentemail=?1 and examid=?2",nativeQuery=true)
			public Distribution getmythisexamall(String answermail,int examid);
		
		//获取这场考试的所有分发表已经考的分发表
		@Query(value = "select * from distribution where examid=?1 and absent=?2",nativeQuery=true)
		public ArrayList<Distribution> getallDSofexamsC(int examid,int absent);
		
		//更新总分
		@Modifying
		@Transactional
		@Query(value = "update distribution set totalpoint=?1,absent=3 where id=?2",nativeQuery=true)
			public void updateTFPC(float totalpoint,int did);
		
		//获取这场考试的所有分发表已经考的分发表
				@Query(value = "select * from distribution where examid=?1 order by totalpoint desc",nativeQuery=true)
				public ArrayList<Distribution> getallDofexamsC(int examid);
				
		//处理缺考人员的
				@Modifying
				@Transactional
				@Query(value = "update distribution set absent=?1,totalpoint=0 where id=?2",nativeQuery=true)
					public void updatemyDstate1C(int state,int did);
				

				/***********************************yxr**********************************/
				//获取某位同学的考试的分发表
				@Query(value = "select examid from distribution where studentemail=?1 and absent=?2 order by id desc",nativeQuery=true)
					public ArrayList<Integer> getmyexamsidY(String answermail,int absent);
	
}