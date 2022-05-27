package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Fillinans;
import com.example.demo.entity.Jugementans;


public interface JugementansDao extends JpaRepository<Jugementans, Integer>{
		//练习时插入做题记录
			//先插入文字相关信息(客观题可以直接判分插入分数)
		@Modifying
		@Transactional
		@Query(value = "insert into jugementans(answer, answermail,qid,score,type) values(?1,?2,?3,?4,1)",nativeQuery=true)
			public void insertNewQuestionC(String answer,String answermail,int qid,float score);
			//再获取刚刚做题的id
		@Query(value = "select max(id) from jugementans where answermail=?1",nativeQuery = true)
		public int getnewinsertIDC(String answermail);
			//如果这人做完题还想再写个笔记
		@Modifying
		@Transactional
		@Query(value="update jugementans set comment=?1 where id=?2",nativeQuery = true)
			public void insertComment(String comment,int id);
		//到这里插完了(更新难度在题目信息Dao)
		
		//根据题目id获取作答信息
		@Query(value = "select * from jugementans where qid=?1",nativeQuery = true)
			public ArrayList<Jugementans> getbyqIDC(int qid);
		
		//考试中如果要改答案
		@Modifying
		@Transactional
		@Query(value = "update jugementans set answer=?1 where id=?2 ",nativeQuery=true)
			public void updateQAC(String answer,int id);
		//考完试要答案
		@Query(value = "select * from jugementans where qid=?1",nativeQuery=true)
			public String getExamanswerC(String answer,int id);
		//考完试更新分数
		@Modifying
		@Transactional
		@Query(value = "update jugementans set score=?1 where id=?2 ",nativeQuery=true)
			public void updateQAC(float score,int id);
		
		//根据用户查找做过的题号
		@Query(value = "select qid from jugementans where answermail=?1",nativeQuery=true)
			public ArrayList<Integer> getMydidqidC(String answermail);
		//根据用户查找做过的aid
		@Query(value = "select id from jugementans where answermail=?1",nativeQuery=true)
			public ArrayList<Integer> getMydidaidC(String answermail);
		
		
		//谢彩云的
		//插入判断题答题信息
	    @Modifying
	    @Transactional
	    @Query(value = "insert into jugementans(qid,type,examid,examsubid,answermail,answer,score,comment)"
	    		+ "values(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery = true)
		public void insertNewansweredX(int qid,int type,int examid,int examsubid,String answermail,String answer,float score,String comment);
	    
	   //获取判断题学生答案
	    @Query(value = "select answer from jugementans where examid=?1 and examsubid=?2 and answermail=?3",nativeQuery = true)
	 	public String getAnsweredX(int examid,int examsubid,String answermail);

	    //修改判断题学生得分
	    @Modifying
	    @Transactional
	    @Query(value = "update jugementans set score=?1 where examid=?2 and examsubid=?3 and  answermail=?4",nativeQuery = true)
	 	public void updateScoreX(float score,int examid,int examsubid,String answermail);

	  //获取判断题学生得分
	    @Query(value = "select score from jugementans where examid=?1 and examsubid=?2 and answermail=?3",nativeQuery = true)
	 	public Float getScoreX(int examid,int examsubid,String answermail);

	  //获取判断题学生得分
	    @Query(value = "select score from jugementans where examid=?1 and qid=?2 and answermail=?3",nativeQuery = true)
	 	public Float getScoreQX(int examid,int qid,String answermail);
	    
	  //考试的时候插入学生作答
	    @Modifying
		@Transactional
		@Query(value = "insert into jugementans(qid, examid,answermail,score,type) values(?1,?2,?3,-1,0)",nativeQuery=true)
			public void insertNewQuestionExamC(int qid,int examid,String answermail);
	  //根据考试id和学生email拿到作答情况
	    @Query(value = "select * from jugementans where examid=?1 and answermail=?2 and qid=?3",nativeQuery=true)
		public Jugementans getExamCQC(int examid,String answermail,int qid);
	  //考试的时候更新分数和答案
	    @Modifying
	    @Transactional
	    @Query(value = "update jugementans set answer=?1 where examid=?2 and answermail=?3 and qid=?4",nativeQuery = true)
	 	public void updateExamC(String answer,int examid,String answermail,int qid);
	  //考完了直接批分数
	    @Modifying
	    @Transactional
	    @Query(value = "update jugementans set score=?1 where id=?2",nativeQuery = true)
	 	public void updateExamSC(float score,int aid);
	  //拿某个学生某场考试所有的成绩
	    @Query(value = "select score from jugementans where examid=?1 and answermail=?2",nativeQuery = true)
	 	public ArrayList<Float> getSPC(int examid,String answermail);
	    
	  //查找这场考试的这道题
	    @Query(value = "select * from jugementans where examid=?1 and qid=?2 and score>-1",nativeQuery = true)
	 	public ArrayList<Jugementans> getEQC(int examid,int qid);

		
}

