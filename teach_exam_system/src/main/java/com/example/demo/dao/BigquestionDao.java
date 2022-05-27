package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Bigquestion;
import com.example.demo.entity.Choiceanswered;

public interface BigquestionDao extends JpaRepository<Bigquestion, Integer> {
	//插入新题目
		//我需要先上传文字内容，上传之后拿到id，再存图片路径
	@Modifying
	@Transactional
	@Query(value = "insert into bigquestion(answer, content, difficulty, isok, isshow, point, subject, upmail) values(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery=true)
		public void insertNewbigquestionC(String answer,String content,float difficulty,int isok,int isshow,int point,String subject,String upmail);
		//获取刚刚插入的ID(为了怕多用户同时插入题目，我还限制了一下upmail，保证多用户可行）
	@Query(value = "select max(id) from bigquestion where upmail=?1",nativeQuery = true)
		public int getnewinsertIDC(String upmail);
		//这个时候开始存图片
	@Modifying
	@Transactional
	@Query(value="update bigquestion set answerpic=?1,picture=?2 where id=?3",nativeQuery = true)
		public void insertPic(String answerpic,String picture,int id);
		//到这里存完了
	
	//根据题目ID获取题目信息
	@Query(value = "select * from bigquestion where id=?1",nativeQuery=true)
		public Bigquestion getQbyIdC(Integer id);
	//更新大题难度
	@Modifying
	@Transactional
	@Query(value = "update bigquestion set difficulty=?1 where id=?2",nativeQuery = true)
		public void UpadateQDiffC(float difficulty,int id);
	//更新题目公开状态
	@Modifying
	@Transactional
	@Query(value = "update bigquestion set isshow=?1 where id=?2",nativeQuery = true)
		public void UpadateQShowC(int isshow,int id);
	//更新题目审核状态
	@Modifying
	@Transactional
	@Query(value = "update bigquestion set isok=?1 where id=?2",nativeQuery = true)
		public void UpadateQOkayC(int isok,int id);
	//根据题目ID查询题目信息
	@Query(value = "select * from bigquestion where id=?1 and isok=1 and isshow=1",nativeQuery=true)
		public Bigquestion getSQbyIdC(Integer id);
	
	//这里是给老师同学正常搜索的
	//获取所有
	@Query(value = "select * from bigquestion where isok=1 and isshow=1",nativeQuery=true)
		public ArrayList<Bigquestion> getallShowQC();
	//根据题目科目查询题目信息
	@Query(value = "select * from bigquestion where subject like %?1% and isok=1 and isshow=1",nativeQuery=true)
		public ArrayList<Bigquestion> getQbySubjectC(String subject);
	//根据题目题干查询题目信息
	@Query(value = "select * from bigquestion where content like %?1% and isok=1 and isshow=1",nativeQuery=true)
		public ArrayList<Bigquestion> getQbyContentC(String content);
	//根据题目难度从难到易查询题目信息
	@Query(value = "select * from bigquestion where isok=1 and isshow=1 order by difficulty desc",nativeQuery=true)
		public ArrayList<Bigquestion> getQbyDiffiDC();
	//根据题目难度从易到难查询题目信息
	@Query(value = "select * from bigquestion where isok=1 and isshow=1 order by difficulty asc",nativeQuery=true)
		public ArrayList<Bigquestion> getQbyDiffiAC();
	//根据题目题干和科目查询题目信息
	@Query(value = "select * from bigquestion where content like %?1% and subject like %?2% and isok=1 and isshow=1",nativeQuery=true)
		public ArrayList<Bigquestion> getQbyConandSubC(String content,String subject);
	//根据题目题干按从难倒易查询题目信息
	@Query(value = "select * from bigquestion where content like %?1% and isok=1 and isshow=1 order by difficulty desc",nativeQuery=true)
		public ArrayList<Bigquestion> getQbyConDC(String content);
	//根据题目题干按从难倒易查询题目信息
	@Query(value = "select * from bigquestion where content like %?1% and isok=1 and isshow=1 order by difficulty asc",nativeQuery=true)
		public ArrayList<Bigquestion> getQbyConAC(String content);
	//根据题目科目按从难倒易查询题目信息
	@Query(value = "select * from bigquestion where subject like %?1% and isok=1 and isshow=1 order by difficulty desc",nativeQuery=true)
		public ArrayList<Bigquestion> getQbySubDC(String subject);
	//根据题目科目按从易到难查询题目信息
	@Query(value = "select * from bigquestion where subject like %?1% and isok=1 and isshow=1 order by difficulty asc",nativeQuery=true)
		public ArrayList<Bigquestion> getQbySubAC(String subject);
	//根据题目题干和科目按从难倒易查询题目信息
	@Query(value = "select * from bigquestion where content like %?1% and subject like %?2% and isok=1 and isshow=1 order by difficulty desc",nativeQuery=true)
		public ArrayList<Bigquestion> getQbyConandSubDC(String content,String subject);
	//根据题目题干和科目从易到难查询题目信息
	@Query(value = "select * from bigquestion where content like %?1% and subject like %?2% and isok=1 and isshow=1 order by difficulty asc",nativeQuery=true)
		public ArrayList<Bigquestion> getQbyConandSubAC(String content,String subject);
	
	
	
	//这里是给管理员搜索的
	@Query(value = "select * from bigquestion",nativeQuery=true)
	public ArrayList<Bigquestion> getMallShowQC();
//根据题目科目查询题目信息
@Query(value = "select * from bigquestion where subject like %?1%",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbySubjectC(String subject);
//根据题目题干查询题目信息
@Query(value = "select * from bigquestion where content like %?1%",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbyContentC(String content);
//根据题目难度从难到易查询题目信息
@Query(value = "select * from bigquestion order by difficulty desc",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbyDiffiDC();
//根据题目难度从易到难查询题目信息
@Query(value = "select * from bigquestion order by difficulty asc",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbyDiffiAC();
//根据题目题干和科目查询题目信息
@Query(value = "select * from bigquestion where content like %?1% and subject like %?2%",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbyConandSubC(String content,String subject);
//根据题目题干按从难倒易查询题目信息
@Query(value = "select * from bigquestion where content like %?1%  order by difficulty desc",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbyConDC(String content);
//根据题目题干按从难倒易查询题目信息
@Query(value = "select * from bigquestion where content like %?1%  order by difficulty asc",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbyConAC(String content);
//根据题目科目按从难倒易查询题目信息
@Query(value = "select * from bigquestion where subject like %?1%  order by difficulty desc",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbySubDC(String subject);
//根据题目科目按从易到难查询题目信息
@Query(value = "select * from bigquestion where subject like %?1%  order by difficulty asc",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbySubAC(String subject);
//根据题目题干和科目按从难倒易查询题目信息
@Query(value = "select * from bigquestion where content like %?1% and subject like %?2% order by difficulty desc",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbyConandSubDC(String content,String subject);
//根据题目题干和科目从易到难查询题目信息
@Query(value = "select * from bigquestion where content like %?1% and subject like %?2% order by difficulty asc",nativeQuery=true)
	public ArrayList<Bigquestion> getMQbyConandSubAC(String content,String subject);

//更新题目审核情况
@Modifying
@Transactional
@Query(value = "update bigquestion set isok=?1 where id=?2",nativeQuery = true)
	public void updateqQKC(int isok,int qid);

//根据题目ID获取题目满分
		@Query(value = "select point from bigquestion where id=?1",nativeQuery=true)
			public Integer getPbyIdX(Integer id);
		
	
		
		
	




}