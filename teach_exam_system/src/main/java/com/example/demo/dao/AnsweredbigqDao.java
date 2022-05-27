package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Answeredbigq;
import com.example.demo.entity.Choiceanswered;


public interface AnsweredbigqDao extends JpaRepository<Answeredbigq, Integer> {
	
	//练习时插入做题记录
		//先插入文字相关信息
	@Modifying
	@Transactional
	@Query(value = "insert into answeredbigq(answer, answermail,qid,type) values(?1,?2,?3,1)",nativeQuery=true)
		public void insertNewQuestionC(String answer,String answermail,int qid);
		//再获取刚刚做题的id
	@Query(value = "select max(id) from answeredbigq where answermail=?1",nativeQuery = true)
	public int getnewinsertIDC(String answermail);
		//这个时候开始存图片
	@Modifying
	@Transactional
	@Query(value="update answeredbigq set answerpic=?1 where id=?2",nativeQuery = true)
		public void insertPic(String answerpic,int id);
		//如果这人做完题还想再写个笔记
	@Modifying
	@Transactional
	@Query(value="update answeredbigq set comment=?1 where id=?2",nativeQuery = true)
		public void insertComment(String comment,int id);
	//到这里就插完了
	
		//根据题目id获取作答信息
	@Query(value = "select * from answeredbigq where qid=?1",nativeQuery = true)
		public ArrayList<Answeredbigq> getbyqIDC(int qid);
	
	//考试中如果要改答案
	@Modifying
	@Transactional
	@Query(value = "update answeredbigq set answer=?1 where id=?2 ",nativeQuery=true)
		public void updateQAC(String answer,int id);
	//考完试要答案
	@Query(value = "select * from answeredbigq where qid=?1",nativeQuery=true)
		public String getExamanswerC(String answer,int id);
	//考完试更新分数
	@Modifying
	@Transactional
	@Query(value = "update answeredbigq set score=?1 where id=?2 ",nativeQuery=true)
		public void updateQAC(float score,int id);
	
	//签到之后直接插入表
	@Modifying
	@Transactional
	@Query(value = "insert into answeredbigq(answermail,qid,examid,type) values(?1,?2,?3,0)",nativeQuery=true)
		public void insertNewQuestionC(String answermail,int qid,int examid);
		
	//根据用户查找做过的题号
	@Query(value = "select qid from answeredbigq where answermail=?1 order by id desc",nativeQuery=true)
		public ArrayList<Integer> getMydidqidC(String answermail);
	
	//根据用户查找做过的aid
	@Query(value = "select id from answeredbigq where answermail=?1 order by id desc",nativeQuery=true)
			public ArrayList<Integer> getMydidaidC(String answermail);
	//根据用户查找做过的题的分数
		@Query(value = "select score from answeredbigq where answermail=?1 order by id desc",nativeQuery=true)
				public  ArrayList<Float> getMydidaidFloat(String answermail);
		
		
	//从这开始是谢彩云的
		//插入大题答题信息
	    @Modifying
	    @Transactional
	    @Query(value = "insert into answeredbigq(qid,type,examid,examsubid,answermail,answer,score,comment,answerpic)"
	    		+ "values(?1,?2,?3,?4,?5,?6,?7,?8,?9)",nativeQuery = true)
		public void insertNewansweredX(int qid,int type,int examid,int examsubid,String answermail,String answer,
				float score,String comment,String answerpic);
	    
	   //获取大题学生文字答案
	    @Query(value = "select answer from answeredbigq where examid=?1 and examsubid=?2 and answermail=?3",nativeQuery = true)
	 	public String getAnsweredPX(int examid,int examsubid,String answermail);

	  //获取大题学生文字答案
	    @Query(value = "select answerpic from answeredbigq where examid=?1 and examsubid=?2 and answermail=?3",nativeQuery = true)
	 	public String getAnsweredWX(int examid,int examsubid,String answermail);
	    
	    //修改大题学生得分
	    @Modifying
	    @Transactional
	    @Query(value = "update answeredbigq set score=?1 and correctmail=?5 where examid=?2 and examsubid=?3 and  answermail=?4",nativeQuery = true)
	 	public void updateScoreX(float score,int examid,int examsubid,String answermail,String correctmail);

	  //获取大题学生得分
	     @Query(value = "select score from answeredbigq where examid=?1 and examsubid=?2 and answermail=?3",nativeQuery = true)
	 	public Float getScoreX(int examid,int examsubid,String answermail);
	    
	  //获取大题学生得分
	    @Query(value = "select score from answeredbigq where examid=?1 and qid=?2 and answermail=?3",nativeQuery = true)
	 	public Float getScoreQX(int examid,int qid,String answermail);
	    
	  //考试的时候插入学生作答
	    @Modifying
		@Transactional
		@Query(value = "insert into answeredbigq(qid, examid,answermail,score,type) values(?1,?2,?3,-1,0)",nativeQuery=true)
			public void insertNewQuestionExamC(int qid,int examid,String answermail);
	    
	    //根据考试id和学生email拿到作答情况
	    @Query(value = "select * from answeredbigq where examid=?1 and answermail=?2 and qid=?3",nativeQuery=true)
		public Answeredbigq getExamCQC(int examid,String answermail,int qid);
	    //考试的时候更新分数和答案
	    @Modifying
	    @Transactional
	    @Query(value = "update answeredbigq set answer=?1 where examid=?2 and answermail=?3 and qid=?4",nativeQuery = true)
	 	public void updateExamC(String answer,int examid,String answermail,int qid);
	    //根据aid更新图片路径
	    @Modifying
	    @Transactional
	    @Query(value = "update answeredbigq set answerpic=?1 where id=?2",nativeQuery = true)
	 	public void updateExamPC(String answer,int aid);
	    //考完了直接批分数(假的、只是为了不为空）
	    @Modifying
	    @Transactional
	    @Query(value = "update answeredbigq set score=?1 where id=?2",nativeQuery = true)
	 	public void updateExamSC(float score,int aid);
	    //根据考试和题号获取题的信息们
	    @Query(value = "select * from answeredbigq where examid=?1 and qid=?2",nativeQuery = true)
	 	public ArrayList<Answeredbigq> getEAQC(int examid,int qid);
	    //考完了直接批分数(假的、只是为了不为空）
	    @Modifying
	    @Transactional
	    @Query(value = "update answeredbigq set score=?1,comment=?2 where id=?3",nativeQuery = true)
	 	public void updateExamSTC(float score,String comment,int aid);
	    
	    //拿某个学生某场考试所有的成绩
	    @Query(value = "select score from answeredbigq where examid=?1 and answermail=?2",nativeQuery = true)
	 	public ArrayList<Float> getSPC(int examid,String answermail);
	    
	  //查找这场考试的这道题
	    @Query(value = "select * from answeredbigq where examid=?1 and qid=?2 and score>-1 order by score desc",nativeQuery = true)
	 	public ArrayList<Answeredbigq> getEQC(int examid,int qid);
	    
	    
	    
	
}