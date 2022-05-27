package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import javax.transaction.Transactional;

import com.example.demo.entity.Classandstudent;

public interface ClassandstudentDao extends JpaRepository<Classandstudent, Integer> {
	//插入新学生
    @Modifying
    @Transactional
    @Query(value = "insert into classandstudent(classid,studentid) values(?1,?2)",nativeQuery = true)
	public void insertNewstudentX(Integer classid,Integer studentid);
    
    //删除学生
    @Modifying
    @Transactional
    @Query(value = "delete from classandstudent where classid=?1 and studentid=?2",nativeQuery = true)
    public void deleteStudentX(Integer classid,Integer studentid);   
    
    //查找特定班级ID的学生人数
	@Query(value = "select count(*) from classandstudent where classid=?1",nativeQuery=true)
    public int getClassnumbyIdX(Integer classid);
	
	//查找特定班级ID的学生ID
	@Query(value = "select studentid from classandstudent where classid=?1",nativeQuery=true)
	public ArrayList<Integer> getStudentidByClassidX(Integer classid);

	//查找特定学生ID的所有班级
	@Query(value = "select classid from classandstudent where studentid=?1",nativeQuery=true)
	public ArrayList<Integer> getClassidByStudentidX(Integer studentid);

	//删除班级
    @Modifying
    @Transactional
    @Query(value = "delete from classandstudent where classid=?1 ",nativeQuery = true)
    public void deleteClassX(Integer classid);   
    
   //查找学生是否已经在某个班级中
  	@Query(value = "select id from classandstudent where classid=?1 and studentid=?2",nativeQuery=true)
  	public Integer getidX(Integer classid, Integer studentid);
  	
  	/****************************************yxr开始**********************************************/
    //判断全部班级的数量
    @Query(value = "select count(*) from classandstudent where studentid=?1",nativeQuery=true)
    public int classCount(int studentid);
	//查找特定学生ID的所有班级
	@Query(value = "select classid from classandstudent where studentid=?1",nativeQuery=true)
	public ArrayList<Integer> getClassidByStudentidY(Integer studentid);

    
    
    
}

