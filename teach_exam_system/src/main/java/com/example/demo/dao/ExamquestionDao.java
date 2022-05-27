package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.ExamQuestion;

public interface ExamquestionDao extends JpaRepository<ExamQuestion, Integer> {
	//插入考试-题目对信息
    @Modifying
    @Transactional
    @Query(value = "insert into examquestion(examid,qid,qtype,finishedcor) values(?1,?2,?3,0)",nativeQuery = true)
	public void insertNewexamQX(Integer examid,Integer qid,Integer qtype);
   
    //查找特定考试的特定类型的题目
	@Query(value = "select qid from examquestion where examid=?1 and qtype=?2",nativeQuery=true)
	public ArrayList<Integer> getQidByIdX(Integer examid,Integer qtype);
	
	//查找特定考试的题目
	@Query(value = "select qid from examquestion where examid=?1",nativeQuery=true)
	public ArrayList<Integer> getAllQidByIdC(Integer examid);
	
	//查找特定考试的题目的题型
	@Query(value = "select qtype from examquestion where examid=?1",nativeQuery=true)
	public ArrayList<Integer> getallQtypeByIdC(Integer examid);
	
	//查找特定考试的题目
	@Query(value = "select * from examquestion where examid=?1",nativeQuery=true)
	public ArrayList<ExamQuestion> getallQByIdC(Integer examid);
	
	 //查找特定考试的特定类型的题目全部
		@Query(value = "select * from examquestion where examid=?1 and qtype=?2",nativeQuery=true)
		public ArrayList<ExamQuestion> getQidByIdC(Integer examid,Integer qtype);
		
		//更新这道大题批完没有
		 @Modifying
		 @Transactional
		 @Query(value = "update examquestion set finishedcor=1 where examid=?1 and qid=?2 and qtype=3",nativeQuery = true)
		 public void updatefinishC(Integer examid,Integer qid);
		 
		//查找特定考试的大题
			@Query(value = "select * from examquestion where examid=?1 and qtype=3",nativeQuery=true)
			public ArrayList<ExamQuestion> getallQBByIdC(Integer examid);

}

