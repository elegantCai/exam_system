package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.example.demo.entity.Jugementinfo;

public interface JugementinfoDao extends JpaRepository<Jugementinfo, Integer> {
	//插入新题目
		//我需要先上传文字内容，上传之后拿到id，再存图片路径
	@Modifying
	@Transactional
	@Query(value = "insert into jugementinfo(answer, content, difficulty, isok, isshow, point, subject, upmail) values(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery=true)
		public void insertNewjquestionC(String answer,String content,float difficulty,int isok,int isshow,int point,String subject,String upmail);
		//获取刚刚插入的ID(为了怕多用户同时插入题目，我还限制了一下upmail，保证多用户可行）
	@Query(value = "select max(id) from jugementinfo where upmail=?1",nativeQuery = true)
		public int getnewinsertIDC(String upmail);
		//这个时候开始存图片
	@Modifying
	@Transactional
	@Query(value="update jugementinfo set picture=?1 where id=?2",nativeQuery = true)
		public void insertPic(String picture,int id);
		//到这里存完了
	
	//根据题目ID获取题目信息
		@Query(value = "select * from jugementinfo where id=?1",nativeQuery=true)
			public Jugementinfo getQbyIdC(Integer id);
		//更新题目难度
		@Modifying
		@Transactional
		@Query(value = "update jugementinfo set difficulty=?1 where id=?2",nativeQuery = true)
			public void UpadateDiffC(float difficulty,int id);
		//更新题目公开状态
		@Modifying
		@Transactional
		@Query(value = "update jugementinfo set isshow=?1 where id=?2",nativeQuery = true)
			public void UpadateShowC(int isshow,int id);
		//更新题目审核状态
		@Modifying
		@Transactional
		@Query(value = "update jugementinfo set isok=?1 where id=?2",nativeQuery = true)
			public void UpadateOkayC(int isok,int id);
		//根据题目ID查询题目信息
		@Query(value = "select * from jugementinfo where id=?1 and isok=1 and isshow=1",nativeQuery=true)
			public Jugementinfo getSQbyIdC(Integer id);
		//获取所有
		@Query(value = "select * from jugementinfo where isok=1 and isshow=1",nativeQuery=true)
			public ArrayList<Jugementinfo> getallShowQC();
		//根据题目科目查询题目信息
		@Query(value = "select * from jugementinfo where subject like %?1% and isok=1 and isshow=1",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbySubjectC(String subject);
		//根据题目题干查询题目信息
		@Query(value = "select * from jugementinfo where content like %?1% and isok=1 and isshow=1",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbyContentC(String content);
		//根据题目难度从难到易查询题目信息
		@Query(value = "select * from jugementinfo where isok=1 and isshow=1 order by difficulty desc",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbyDiffiDC();
		//根据题目难度从易到难查询题目信息
		@Query(value = "select * from jugementinfo where isok=1 and isshow=1 order by difficulty asc",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbyDiffiAC();
		//根据题目题干和科目查询题目信息
		@Query(value = "select * from jugementinfo where content like %?1% and subject like %?2% and isok=1 and isshow=1",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbyConandSubC(String content,String subject);
		//根据题目题干按从难倒易查询题目信息
		@Query(value = "select * from jugementinfo where content like %?1% and isok=1 and isshow=1 order by difficulty desc",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbyConDC(String content);
		//根据题目题干按从难倒易查询题目信息
		@Query(value = "select * from jugementinfo where content like %?1% and isok=1 and isshow=1 order by difficulty asc",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbyConAC(String content);
		//根据题目科目按从难倒易查询题目信息
		@Query(value = "select * from jugementinfo where subject like %?1% and isok=1 and isshow=1 order by difficulty desc",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbySubDC(String subject);
		//根据题目科目按从易到难查询题目信息
		@Query(value = "select * from jugementinfo where subject like %?1% and isok=1 and isshow=1 order by difficulty asc",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbySubAC(String subject);
		//根据题目题干和科目按从难倒易查询题目信息
		@Query(value = "select * from jugementinfo where content like %?1% and subject like %?2% and isok=1 and isshow=1 order by difficulty desc",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbyConandSubDC(String content,String subject);
		//根据题目题干和科目从易到难查询题目信息
		@Query(value = "select * from jugementinfo where content like %?1% and subject like %?2% and isok=1 and isshow=1 order by difficulty asc",nativeQuery=true)
			public ArrayList<Jugementinfo> getQbyConandSubAC(String content,String subject);
		
		
		//这里是给管理员搜索的
		@Query(value = "select * from jugementinfo",nativeQuery=true)
		public ArrayList<Jugementinfo> getMallShowQC();
	//根据题目科目查询题目信息
	@Query(value = "select * from jugementinfo where subject like %?1%",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbySubjectC(String subject);
	//根据题目题干查询题目信息
	@Query(value = "select * from jugementinfo where content like %?1%",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbyContentC(String content);
	//根据题目难度从难到易查询题目信息
	@Query(value = "select * from jugementinfo order by difficulty desc",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbyDiffiDC();
	//根据题目难度从易到难查询题目信息
	@Query(value = "select * from jugementinfo order by difficulty asc",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbyDiffiAC();
	//根据题目题干和科目查询题目信息
	@Query(value = "select * from jugementinfo where content like %?1% and subject like %?2%",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbyConandSubC(String content,String subject);
	//根据题目题干按从难倒易查询题目信息
	@Query(value = "select * from jugementinfo where content like %?1%  order by difficulty desc",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbyConDC(String content);
	//根据题目题干按从难倒易查询题目信息
	@Query(value = "select * from jugementinfo where content like %?1%  order by difficulty asc",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbyConAC(String content);
	//根据题目科目按从难倒易查询题目信息
	@Query(value = "select * from jugementinfo where subject like %?1%  order by difficulty desc",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbySubDC(String subject);
	//根据题目科目按从易到难查询题目信息
	@Query(value = "select * from jugementinfo where subject like %?1%  order by difficulty asc",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbySubAC(String subject);
	//根据题目题干和科目按从难倒易查询题目信息
	@Query(value = "select * from jugementinfo where content like %?1% and subject like %?2% order by difficulty desc",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbyConandSubDC(String content,String subject);
	//根据题目题干和科目从易到难查询题目信息
	@Query(value = "select * from jugementinfo where content like %?1% and subject like %?2% order by difficulty asc",nativeQuery=true)
		public ArrayList<Jugementinfo> getMQbyConandSubAC(String content,String subject);
	
	//更新题目审核情况
		@Modifying
		@Transactional
		@Query(value = "update jugementinfo set isok=?1 where id=?2",nativeQuery = true)
			public void updateqQKC(int isok,int qid);
		
		//根据题目ID获取题目满分
		@Query(value = "select point from jugementinfo where id=?1",nativeQuery=true)
			public Integer getPbyIdX(Integer id);
	
}
