package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.ExamClass;

public interface ExamclassDao extends JpaRepository<ExamClass, Integer> {
	//插入考试-班级对信息
    @Modifying
    @Transactional
    @Query(value = "insert into examclass(examid,classid) values(?1,?2)",nativeQuery = true)
	public void insertNewexamclassX(Integer examid,Integer classid);
   
    //查找特定考试的所有班级
	@Query(value = "select classid from examclass where examid=?1",nativeQuery=true)
	public ArrayList<Integer> getClassidByIdX(Integer examid);

	//查找特定班级的所有考试
	@Query(value = "select examid from examclass where classid=?1",nativeQuery=true)
	public ArrayList<Integer> getExamidByIdX(Integer classid);
	
}

