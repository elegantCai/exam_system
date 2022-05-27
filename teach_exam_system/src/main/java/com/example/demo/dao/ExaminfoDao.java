package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Calendar;

import javax.transaction.Transactional;

import com.example.demo.entity.Examinfo;

public interface ExaminfoDao extends JpaRepository<Examinfo, Integer> {
	//插入考试信息
    @Modifying
    @Transactional
    @Query(value = "insert into examinfo(email,starttime,endtime,type,subject,state) "
    		+ "values(?1,?2,?3,?4,?5,?6)",nativeQuery = true)
	public void insertNewexamX(String email,Calendar starttime,Calendar endtime,
			Integer type,String subject,Integer state);
    
    //删除考试信息
    @Modifying
    @Transactional
    @Query(value = "delete from examinfo where id=?1",nativeQuery = true)
    public void deleteExamX(Integer id);   
    
	//查找特定ID的考试信息
	@Query(value = "select * from examinfo where id=?1",nativeQuery=true)
	public Examinfo getExaminfoByIdX(Integer id);

	//查找特定老师邮箱的考试信息
	@Query(value = "select * from examinfo where email=?1",nativeQuery=true)
	public ArrayList<Examinfo> getExaminfoByTeachermailX(String email);

	//查找特定ID的选择题题目信息
	@Query(value = "select choiceques from examinfo where id=?1",nativeQuery=true)
	public String getChoiceByIdX(Integer id);
	
	//查找特定ID的填空题题目信息
	@Query(value = "select fillinques from examinfo where id=?1",nativeQuery=true)
	public String getFillByIdX(Integer id);
	
	//查找特定ID的判断题题目信息
	@Query(value = "select judgmentques from examinfo where id=?1",nativeQuery=true)
	public String getJudgeByIdX(Integer id);

	//查找特定ID的大题题目信息
	@Query(value = "select bigques from examinfo where id=?1",nativeQuery=true)
	public String getBigByIdX(Integer id);

	//查找最新插入的考试信息
	@Query(value = "select max(id) from examinfo where email=?1",nativeQuery=true)
	public Integer getNewByEmailX(String email);
	
	
	//更新考试总分
    @Modifying
    @Transactional
    @Query(value="update examinfo set totalpoint=?1 where id=?2",nativeQuery = true)
    public void inserttotalpointC(int totalpoint,int id);
    
    //查到自己发布的各种状态的考试
    @Query(value="select * from examinfo where email=?1 and state=?2",nativeQuery = true)
    public ArrayList<Examinfo> searchmyexamC(String email,int state);
    
    //更新考试的状态
    @Modifying
    @Transactional
    @Query(value="update examinfo set state=?1 where id=?2",nativeQuery = true)
    public void updateStateC(int state,int id);
  //查找特定老师邮箱的考试信息
  	@Query(value = "select * from examinfo where email=?1 order by id desc",nativeQuery=true)
  	public ArrayList<Examinfo> getExaminfoByTeachermailC(String email);
  //查找特定状态的考试信息
  	@Query(value = "select * from examinfo where state=?1 order by id desc",nativeQuery=true)
  	public ArrayList<Examinfo> getNotcheckC(int state);
  	/*****************************************************yxr***********************************************/
	//查找特定ID的考试信息
	@Query(value = "select * from examinfo where id=?1",nativeQuery=true)
	public Examinfo getExamByIdY(Integer id);

}

