package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import javax.transaction.Transactional;

import com.example.demo.entity.Classes;

public interface ClassDao extends JpaRepository<Classes, Integer> {
	//插入新班级 0：未审核 f非文字
    @Modifying
    @Transactional
    @Query(value = "insert into classes(classname,teachermail,classtype,information,state) values(?1,?2,?3,?4,?5)",nativeQuery = true)
	public void insertNewclassX(String classname,String teachermail,String classtype,String information,Integer state);
	
    //更新班级信息 图片
    @Modifying
    @Transactional
    @Query(value = "update classes set picture=?2 where classid=?1",nativeQuery = true)
 	public void updateClassPX(Integer classid,String picture);    
    
  //查找最新插入的班级id
    @Query(value = "select max(classid) from classes where teachermail=?1",nativeQuery=true)
    public Integer getClassIDX(String teachermail);
     
    
    
    
    //删除班级
    @Modifying
    @Transactional
    @Query(value = "delete from classes where classid=?1",nativeQuery = true)
    public void deleteClassX(Integer classid);   
    
	//更新班级信息
    @Modifying
    @Transactional
    @Query(value = "update classes set classname?2,classtype?3,information?4,picture?5 where classid=?1",nativeQuery = true)
 	public void updateClassinfoX(Integer classid,String classname,String classtype,String information,String picture);    
    
   //查找特定班级ID的班级
    @Query(value = "select * from classes",nativeQuery=true)
    public ArrayList<Classes> getClassX();
      
    
    //修改状态
    @Modifying
    @Transactional
    @Query(value = "update classes set state=?2 where classid=?1",nativeQuery = true)
 	public void updateClassstateX(Integer classid,Integer state);    
    
    
    //查找特定班级ID的班级
	@Query(value = "select * from classes where classid=?1",nativeQuery=true)
    public ArrayList<Classes> getClassbyIdX(Integer classid);
    
	//查找特定班级ID的班级
	@Query(value = "select * from classes where classid=?1",nativeQuery=true)
	 public Classes getClassbyUIdX(Integer classid);
	    
	
	//查找特定班级名称的班级
	@Query(value = "select * from classes where classname=?1",nativeQuery=true)
	public ArrayList<Classes> getClassbyclassnameX(String classname);
	    
	//查找特定班级类型的班级
	@Query(value = "select * from classes where classtype=?1",nativeQuery=true)
	public ArrayList<Classes> getClassbyclasstypeX(String classtype);
	
	//查找特定班级状态的班级
	@Query(value = "select * from classes where state=?1",nativeQuery=true)
	public ArrayList<Classes> getClassbystateX(Integer state);
	
	//查找特定老师邮箱的班级，按班级id升序排列，也可称为班级创建时间
	@Query(value = "select * from classes where teachermail=?1 order by classid asc",nativeQuery=true)
	public ArrayList<Classes> ClassidByTeacheridX(String teachermail);
	
	//查找特定信息的班级
	@Query(value = "select * from classes where classid=?1 and classname=?2 and classtype=?3 and state=?4 and teachermail=?5",nativeQuery=true)
	public ArrayList<Classes> getClassbyAllX(Integer classid,String classname,String classtype,Integer state,String teachermail);
		
	
	
	
	
	
	//查找特定老师邮箱的班级，按班级类型升序排列
	@Query(value = "select * from classes where teachermail=?1 order by classtype asc",nativeQuery=true)
	public ArrayList<Classes> ClasstypeByTeacheridX(String teachermail);
	
	//查找特定老师邮箱的班级，按班级状态降序排列
	@Query(value = "select * from classes where teachermail=?1 order by state desc",nativeQuery=true)
	public ArrayList<Classes> ClassstateByTeacheridX(String teachermail);
	
	
	
	//查找特定老师邮箱的特定状态的班级，按从新到老排序
	// 0：未审核，1：审核未通过，2：审核通过进行中，3：结课申请审核中，4：结课
	@Query(value = "select * from classes where teachermail=?1 and state=?2 order by classid desc",nativeQuery=true)
		public ArrayList<Classes> getTeacherClassC(String teachermail,int state);
	
	/****************************************yxr开始**********************************************/
    //查找特定学生加入的班级，根据不同状态
	@Query(value = "select id from classandstudent where studentid=?1 and state=?2 order by classid desc",nativeQuery=true)
	public ArrayList<Classes> getStudentClassY(int studentid,int state);
	
    //查找班级信息
    @Query(value = "select * from classes where classid=?1",nativeQuery=true)
    public Classes getClassInformationY(int classid);
		
}

