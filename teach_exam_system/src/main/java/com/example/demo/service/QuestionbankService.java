package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.example.demo.dao.AdminrecordDao;
import com.example.demo.dao.AnsweredbigqDao;
import com.example.demo.dao.BigquestionDao;
import com.example.demo.dao.ChoiceDao;
import com.example.demo.dao.ChoiceansweredDao;
import com.example.demo.dao.FillinansDao;
import com.example.demo.dao.FillinquestionDao;
import com.example.demo.dao.JugementansDao;
import com.example.demo.dao.JugementinfoDao;
import com.example.demo.entity.Answeredbigq;
import com.example.demo.entity.Bigquestion;
import com.example.demo.entity.Choice;
import com.example.demo.entity.Choiceanswered;
import com.example.demo.entity.Fillinans;
import com.example.demo.entity.Fillinquestion;
import com.example.demo.entity.Jugementans;
import com.example.demo.entity.Jugementinfo;

import antlr.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service("questionbankService")

public class QuestionbankService {
	@Autowired 
	private BigquestionDao bigquestionDao;
	@Autowired 
	private JugementinfoDao jugementinfoDao;
	@Autowired 
	private FillinquestionDao fillinquestionDao;
	@Autowired 
	private ChoiceDao choiceDao;
	@Autowired 
	private ChoiceansweredDao choiceansweredDao;
	@Autowired 
	private JugementansDao jugementansDao;
	@Autowired 
	private FillinansDao fillinansDao;
	@Autowired 
	private AnsweredbigqDao answeredbigqDao;
	@Autowired 
	private AdminrecordDao adminrecordDao;
	
	//插入题目基本信息
		//type是题目的题型
		//exam是是否是考试
    public int insertNewQuestionC(int type,int exam,String answer,String content,float difficulty,int point,
    		String subject,String upmail,String optiona,String optionb,String optionc,String optiond) {
    	//是从题库中插入的
    	if(exam==0) {
    		//选择题
    		if (type==0) {
    			choiceDao.insertNewjquestionC(answer, content, difficulty, 1, 1, point, subject, upmail, optiona, optionb, optionc, optiond);//isok=1,isshow=1
    			return choiceDao.getnewinsertIDC(upmail);
    		}
    		//判断题
    		if (type==1) {
    			jugementinfoDao.insertNewjquestionC(answer, content, difficulty, 1, 1, point, subject, upmail);  //isok=1,isshow=1
    			return jugementinfoDao.getnewinsertIDC(upmail);
    		}
    		//填空题
    		if (type==2) {
    			fillinquestionDao.insertNewQuestionC(answer, content, difficulty, 1, 1, point, subject, upmail);  //isok=1,isshow=1
    			return fillinquestionDao.getnewinsertIDC(upmail);
    		}
    		//大题
        	if (type==3) {
        		bigquestionDao.insertNewbigquestionC(answer, content, difficulty, 1, 1, point, subject, upmail);  //isok=1,isshow=1
        		return bigquestionDao.getnewinsertIDC(upmail);
        	}
    	}
    	//是从考试中插入的
    	else if(exam==1) {
    		//选择题
    		if (type==0) {
    			choiceDao.insertNewjquestionC(answer, content, difficulty, 1, 0, point, subject, upmail, optiona, optionb, optionc, optiond);//isok=1,isshow=1
    			return choiceDao.getnewinsertIDC(upmail);
    		}
    		//判断题
    		if (type==1) {
    			jugementinfoDao.insertNewjquestionC(answer, content, difficulty, 1, 0, point, subject, upmail);  //isok=1,isshow=1
    			return jugementinfoDao.getnewinsertIDC(upmail);
    		}
    		//填空题
    		if (type==2) {
    			fillinquestionDao.insertNewQuestionC(answer, content, difficulty, 1, 0, point, subject, upmail);  //isok=1,isshow=1
    			return fillinquestionDao.getnewinsertIDC(upmail);
    		}
    		//大题
        	if (type==3) {
        		bigquestionDao.insertNewbigquestionC(answer, content, difficulty, 1, 0, point, subject, upmail);
        		return bigquestionDao.getnewinsertIDC(upmail);
        	}
    	}
    	return 0;
    }
    
    	//转化老师输入的难度评级，difficulty是1-得分率
    public float transdifficulty(String strdifficultlevel) {
    	float difficulty;
    	if (strdifficultlevel!=null){
    		int difficultylevel=Integer.parseInt(strdifficultlevel);
        	//难
        	if (difficultylevel==0){
        		difficulty=(float) 0.8;
        		return difficulty;    		
        		}
        	//中
        	else if (difficultylevel==1){
        		difficulty=(float) 0.5;
        		return difficulty;    		
        		}
        	//易
        	else{
        		difficulty=(float) 0.2;
        		return difficulty;    		
        		}
    	}
    	else {
    		difficulty=(float) 0.5;
    		return difficulty;
    	}
    }
    	//插入图片相关
    		//大题
    public void updatebigqnewpic(String answerpic,String picture,int id){
    	bigquestionDao.insertPic(answerpic,picture,id);
    }
    		//其他题
    public void updatenewpic(int qtype,String picture,int id){
    	if (qtype==0) {
    		choiceDao.insertPic(picture, id);
    	}
    	else if (qtype==1) {
    		jugementinfoDao.insertPic(picture, id);
    	}
    	else {
    		fillinquestionDao.insertPic(picture, id);
    	}
    }
    //插入完了
    
    //显示刚刚插入的题目的信息
 		//选择题
    public Choice getNewchoiceq(int id) {
    	return choiceDao.getById(id);
    }
 		//判断题
    public Jugementinfo getjq(int id) {
    	return jugementinfoDao.getById(id);
    }
 		//填空题
    public Fillinquestion getfq(int id) {
    	return fillinquestionDao.getById(id);
    }
    	//大题
    public Bigquestion getNewbigq(int id) {
    	return bigquestionDao.getById(id);
    }
    
    
    //搜索页面
    	//一个转字符串的小函数
    public int transString(String thetype) {
    	if (thetype!=null) {
    		return 1;
    	}
    	else {
    		return 0;
    	}
    }
    	//选择题搜索
    public ArrayList<Choice> getSearchCq(String subject,String content,String strstype,String strsortype,int usertype) {
    	int stype;
    	int sortype;
    	
    	if (strstype!=null) {
    		stype=Integer.parseInt(strstype);}
    	else {stype=10;}
    	
    	if (strsortype!=null) {
    		sortype=Integer.parseInt(strsortype);}
    	else {sortype=10;}
    	
    	//管理员权限大，什么题都能看
    	if (usertype==2) {
    	//  如果是从难到易排列  	
        	if (sortype==1) {
        		//只查科目
        		if(stype==0) {
        			return choiceDao.getMQbySubDC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return choiceDao.getMQbyConDC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return choiceDao.getMQbyConandSubDC(content,subject);
        		}
        		//啥也不查
        		else {
        			return choiceDao.getMQbyDiffiDC();
        		}
        	}
        	//  如果是从易到难排列
        	else if(sortype==2) {
        		//只查科目
        		if(stype==0) {
        			return choiceDao.getMQbySubAC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return choiceDao.getMQbyConAC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return choiceDao.getMQbyConandSubAC(content,subject);
        		}
        		//啥也不查
        		else {
        			return choiceDao.getMQbyDiffiAC();
        		}
        	}
        	//不排序，直接按id排就行
        	else {
        		//只查科目
        		if(stype==0) {
        			return choiceDao.getMQbySubjectC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return choiceDao.getMQbyContentC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return choiceDao.getMQbyConandSubC(content,subject);
        		}
        		//啥也不查
        		else {
        			return choiceDao.getMallShowQC();
        		}
        	}
    	}
    	//但是老师和学生只能看审核通过的、可以公开的
    	else {
    	//  如果是从难到易排列  	
        	if (sortype==1) {
        		//只查科目
        		if(stype==0) {
        			return choiceDao.getQbySubDC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return choiceDao.getQbyConDC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return choiceDao.getQbyConandSubDC(content,subject);
        		}
        		//啥也不查
        		else {
        			return choiceDao.getQbyDiffiDC();
        		}
        	}
        	//  如果是从易到难排列
        	else if(sortype==2) {
        		//只查科目
        		if(stype==0) {
        			return choiceDao.getQbySubAC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return choiceDao.getQbyConAC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return choiceDao.getQbyConandSubAC(content,subject);
        		}
        		//啥也不查
        		else {
        			return choiceDao.getQbyDiffiAC();
        		}
        	}
        	//不排序，直接按id排就行
        	else {
        		//只查科目
        		if(stype==0) {
        			return choiceDao.getQbySubjectC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return choiceDao.getQbyContentC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return choiceDao.getQbyConandSubC(content,subject);
        		}
        		//啥也不查
        		else {
        			return choiceDao.getallShowQC();
        		}
        	}
    	}
    }

    	//判断题搜索
    public ArrayList<Jugementinfo> getSearchJq(String subject,String content,String strstype,String strsortype,int usertype) {
    	int stype;
    	int sortype;
    	
    	if (strstype!=null) {
    		stype=Integer.parseInt(strstype);}
    	else {stype=10;}
    	
    	if (strsortype!=null) {
    		sortype=Integer.parseInt(strsortype);}
    	else {sortype=10;}
    	
    	//管理员权限大，什么题都能看
    	if (usertype==2) {
    	//  如果是从难到易排列  	
        	if (sortype==1) {
        		//只查科目
        		if(stype==0) {
        			return jugementinfoDao.getMQbySubDC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return jugementinfoDao.getMQbyConDC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return jugementinfoDao.getMQbyConandSubDC(content,subject);
        		}
        		//啥也不查
        		else {
        			return jugementinfoDao.getMQbyDiffiDC();
        		}
        	}
        	//  如果是从易到难排列
        	else if(sortype==2) {
        		//只查科目
        		if(stype==0) {
        			System.out.println("你好");
        			return jugementinfoDao.getMQbySubAC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return jugementinfoDao.getMQbyConAC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return jugementinfoDao.getMQbyConandSubAC(content,subject);
        		}
        		//啥也不查
        		else {
        			return jugementinfoDao.getMQbyDiffiAC();
        		}
        	}
        	//不排序，直接按id排就行
        	else {
        		//只查科目
        		if(stype==0) {
        			return jugementinfoDao.getMQbySubjectC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return jugementinfoDao.getMQbyContentC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return jugementinfoDao.getMQbyConandSubC(content,subject);
        		}
        		//啥也不查
        		else {
        			return jugementinfoDao.getMallShowQC();
        		}
        	}
    	}
    	//但是老师和学生只能看审核通过的、可以公开的
    	else {
    	//  如果是从难到易排列  	
        	if (sortype==1) {
        		//只查科目
        		if(stype==0) {
        			return jugementinfoDao.getQbySubDC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return jugementinfoDao.getQbyConDC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return jugementinfoDao.getQbyConandSubDC(content,subject);
        		}
        		//啥也不查
        		else {
        			return jugementinfoDao.getQbyDiffiDC();
        		}
        	}
        	//  如果是从易到难排列
        	else if(sortype==2) {
        		//只查科目
        		if(stype==0) {
        			return jugementinfoDao.getQbySubAC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return jugementinfoDao.getQbyConAC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return jugementinfoDao.getQbyConandSubAC(content,subject);
        		}
        		//啥也不查
        		else {
        			return jugementinfoDao.getQbyDiffiAC();
        		}
        	}
        	//不排序，直接按id排就行
        	else {
        		//只查科目
        		if(stype==0) {
        			return jugementinfoDao.getQbySubjectC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return jugementinfoDao.getQbyContentC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return jugementinfoDao.getQbyConandSubC(content,subject);
        		}
        		//啥也不查
        		else {
        			return jugementinfoDao.getallShowQC();
        		}
        	}
    	}
    }

    	//填空题搜索
    public ArrayList<Fillinquestion> getSearchFq(String subject,String content,String strstype,String strsortype,int usertype) {
    	int stype;
    	int sortype;
    	
    	if (strstype!=null) {
    		stype=Integer.parseInt(strstype);}
    	else {stype=10;}
    	
    	if (strsortype!=null) {
    		sortype=Integer.parseInt(strsortype);}
    	else {sortype=10;}
    	
    	//管理员权限大，什么题都能看
    	if (usertype==2) {
    	//  如果是从难到易排列  	
        	if (sortype==1) {
        		//只查科目
        		if(stype==0) {
        			return fillinquestionDao.getMQbySubDC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return fillinquestionDao.getMQbyConDC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return fillinquestionDao.getMQbyConandSubDC(content,subject);
        		}
        		//啥也不查
        		else {
        			return fillinquestionDao.getMQbyDiffiDC();
        		}
        	}
        	//  如果是从易到难排列
        	else if(sortype==2) {
        		//只查科目
        		if(stype==0) {
        			System.out.println("你好");
        			return fillinquestionDao.getMQbySubAC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return fillinquestionDao.getMQbyConAC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return fillinquestionDao.getMQbyConandSubAC(content,subject);
        		}
        		//啥也不查
        		else {
        			return fillinquestionDao.getMQbyDiffiAC();
        		}
        	}
        	//不排序，直接按id排就行
        	else {
        		//只查科目
        		if(stype==0) {
        			return fillinquestionDao.getMQbySubjectC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return fillinquestionDao.getMQbyContentC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return fillinquestionDao.getMQbyConandSubC(content,subject);
        		}
        		//啥也不查
        		else {
        			return fillinquestionDao.getMallShowQC();
        		}
        	}
    	}
    	//但是老师和学生只能看审核通过的、可以公开的
    	else {
    	//  如果是从难到易排列  	
        	if (sortype==1) {
        		//只查科目
        		if(stype==0) {
        			return fillinquestionDao.getQbySubDC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return fillinquestionDao.getQbyConDC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return fillinquestionDao.getQbyConandSubDC(content,subject);
        		}
        		//啥也不查
        		else {
        			return fillinquestionDao.getQbyDiffiDC();
        		}
        	}
        	//  如果是从易到难排列
        	else if(sortype==2) {
        		//只查科目
        		if(stype==0) {
        			return fillinquestionDao.getQbySubAC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return fillinquestionDao.getQbyConAC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return fillinquestionDao.getQbyConandSubAC(content,subject);
        		}
        		//啥也不查
        		else {
        			return fillinquestionDao.getQbyDiffiAC();
        		}
        	}
        	//不排序，直接按id排就行
        	else {
        		//只查科目
        		if(stype==0) {
        			return fillinquestionDao.getQbySubjectC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return fillinquestionDao.getQbyContentC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return fillinquestionDao.getQbyConandSubC(content,subject);
        		}
        		//啥也不查
        		else {
        			return fillinquestionDao.getallShowQC();
        		}
        	}
    	}
    }

    	//大题搜索
    public ArrayList<Bigquestion> getSearchBq(String subject,String content,String strstype,String strsortype,int usertype) {	
    	int stype;
    	int sortype;
    	
    	if (strstype!=null) {
    		stype=Integer.parseInt(strstype);}
    	else {stype=10;}
    	
    	if (strsortype!=null) {
    		sortype=Integer.parseInt(strsortype);}
    	else {sortype=10;}
    	
    	//管理员权限大，什么题都能看
    	if (usertype==2) {
    	//  如果是从难到易排列  	
        	if (sortype==1) {
        		//只查科目
        		if(stype==0) {
        			return bigquestionDao.getMQbySubDC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return bigquestionDao.getMQbyConDC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return bigquestionDao.getMQbyConandSubDC(content,subject);
        		}
        		//啥也不查
        		else {
        			return bigquestionDao.getMQbyDiffiDC();
        		}
        	}
        	//  如果是从易到难排列
        	else if(sortype==2) {
        		//只查科目
        		if(stype==0) {
        			System.out.println("你好");
        			return bigquestionDao.getMQbySubAC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return bigquestionDao.getMQbyConAC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return bigquestionDao.getMQbyConandSubAC(content,subject);
        		}
        		//啥也不查
        		else {
        			return bigquestionDao.getMQbyDiffiAC();
        		}
        	}
        	//不排序，直接按id排就行
        	else {
        		//只查科目
        		if(stype==0) {
        			return bigquestionDao.getMQbySubjectC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return bigquestionDao.getMQbyContentC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return bigquestionDao.getMQbyConandSubC(content,subject);
        		}
        		//啥也不查
        		else {
        			return bigquestionDao.getMallShowQC();
        		}
        	}
    	}
    	//但是老师和学生只能看审核通过的、可以公开的
    	else {
    	//  如果是从难到易排列  	
        	if (sortype==1) {
        		//只查科目
        		if(stype==0) {
        			return bigquestionDao.getQbySubDC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return bigquestionDao.getQbyConDC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return bigquestionDao.getQbyConandSubDC(content,subject);
        		}
        		//啥也不查
        		else {
        			return bigquestionDao.getQbyDiffiDC();
        		}
        	}
        	//  如果是从易到难排列
        	else if(sortype==2) {
        		//只查科目
        		if(stype==0) {
        			System.out.println("你好");
        			return bigquestionDao.getQbySubAC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return bigquestionDao.getQbyConAC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return bigquestionDao.getQbyConandSubAC(content,subject);
        		}
        		//啥也不查
        		else {
        			return bigquestionDao.getQbyDiffiAC();
        		}
        	}
        	//不排序，直接按id排就行
        	else {
        		//只查科目
        		if(stype==0) {
        			return bigquestionDao.getQbySubjectC(subject);
        		}
        		//只查题干
        		else if(stype==1) {
        			return bigquestionDao.getQbyContentC(content);
        		}
        		//查科目和题干
        		else if(stype==2) {
        			return bigquestionDao.getQbyConandSubC(content,subject);
        		}
        		//啥也不查
        		else {
        			return bigquestionDao.getallShowQC();
        		}
        	}
    	}
    }

    
    //做题
    	//获取得分
    public float doexcercisescore(String stuanswer,String realanswer,int point,int qtype) {
    	float score;
    	if (qtype==0 || qtype==1 || qtype==2) {
    		if (stuanswer.equals(realanswer)) {
    			score=point;
    			return score;}
    		else {
    			score=(float) 0.0;
    			return score;}
    	}
    	else {
    		return (float) -1.0;    //随便返回一个数
    	}
    }
    
    public int doexercise(int qtype,int qid,String stuanswer,String answermail,String realanswer,float score) {
    		//选择题
    	if (qtype==0) {
    		choiceansweredDao.insertNewQuestionC(stuanswer, answermail, qid,score);
    		return choiceansweredDao.getnewinsertIDC(answermail);
    	}
    		//判断题
    	else if (qtype==1) {
    		jugementansDao.insertNewQuestionC(stuanswer, answermail, qid,score);
    		return jugementansDao.getnewinsertIDC(answermail);
    	}
    		//填空题
    	else if (qtype==2) {
    		fillinansDao.insertNewQuestionC(stuanswer, answermail, qid,score);
    		return fillinansDao.getnewinsertIDC(answermail);
    	}
    		//大题(一会还得继续插图片)
    	else {
    		answeredbigqDao.insertNewQuestionC(stuanswer, answermail, qid);
    		return answeredbigqDao.getnewinsertIDC(answermail);
    	}	
    }
    
    	//大题再插一下答案图片
    public void doexerciseinsertpic(int aid,String stuanswerpic) {
    	answeredbigqDao.insertPic(stuanswerpic, aid);
    }
    	
    	//根据分数更新difficulty（做题人数达到10人以上才会更新难度，否则保持老师给出的难度or默认难度）
    public void updateDiff(int qid,int qtype) {
    	//选择题
    	if (qtype==0) {
    		Choice thisq=choiceDao.getById(qid);
    		float difficulty = thisq.getDifficulty();
    		int point=(int) thisq.getPoint();
    		ArrayList<Choiceanswered> didthisqs=choiceansweredDao.getbyqIDC(qid);
    		float allscore=(float) 0.0;
			int allpoint=0;
			//判断该不该更新，如果difficulty为0.2 or 0.5 or 0.8的默认值，那需要遍历看一下这是不是满10道题了
    		if (difficulty==0.2 || difficulty==0.5 || difficulty==0.8) {
    			int didnumber=didthisqs.size();
    			if (didnumber>10) {
    				for(Choiceanswered didthisq:didthisqs){
        	            float score=didthisq.getScore();
        	            allscore=allscore+score;
        	            allpoint=allpoint+point;
        	        }
    				float newdiff=1-allscore/allpoint;
    				//更新难度
    				choiceDao.UpadateDiffC(newdiff, qid);
    			}
    		}
    		//已经不是默认值了，那肯定过10道题了，直接更新
    		else {
				for(Choiceanswered didthisq:didthisqs){
    	            float score=didthisq.getScore();
    	            allscore=allscore+score;
    	            allpoint=allpoint+point;
    	        }
				float newdiff=1-allscore/allpoint;
				//更新难度
				choiceDao.UpadateDiffC(newdiff, qid);
			}		
    	}
    	//判断题
    	if (qtype==1) {
    		Jugementinfo thisq=jugementinfoDao.getById(qid);
    		float difficulty = thisq.getDifficulty();
    		int point=(int) thisq.getPoint();
    		ArrayList<Jugementans> didthisqs=jugementansDao.getbyqIDC(qid);
    		float allscore=(float) 0.0;
			int allpoint=0;
			//判断该不该更新，如果difficulty为0.2 or 0.5 or 0.8的默认值，那需要遍历看一下这是不是满10道题了
    		if (difficulty==0.2 || difficulty==0.5 || difficulty==0.8) {
    			int didnumber=didthisqs.size();
    			if (didnumber>10) {
    				for(Jugementans didthisq:didthisqs){
        	            float score=didthisq.getScore();
        	            allscore=allscore+score;
        	            allpoint=allpoint+point;
        	        }
    				float newdiff=1-allscore/allpoint;
    				//更新难度
    				jugementinfoDao.UpadateDiffC(newdiff, qid);
    			}
    		}
    		//已经不是默认值了，那肯定过10道题了，直接更新
    		else {
				for(Jugementans didthisq:didthisqs){
    	            float score=didthisq.getScore();
    	            allscore=allscore+score;
    	            allpoint=allpoint+point;
    	        }
				float newdiff=1-allscore/allpoint;
				//更新难度
				jugementinfoDao.UpadateDiffC(newdiff, qid);
			}		
    	}
    	//填空题
    	if (qtype==2) {
    		Fillinquestion thisq=fillinquestionDao.getById(qid);
    		float difficulty = thisq.getDifficulty();
    		int point=(int) thisq.getPoint();
    		ArrayList<Fillinans> didthisqs=fillinansDao.getbyqIDC(qid);
    		float allscore=(float) 0.0;
			int allpoint=0;
			//判断该不该更新，如果difficulty为0.2 or 0.5 or 0.8的默认值，那需要遍历看一下这是不是满10道题了
    		if (difficulty==0.2 || difficulty==0.5 || difficulty==0.8) {
    			int didnumber=didthisqs.size();
    			if (didnumber>10) {
    				for(Fillinans didthisq:didthisqs){
        	            float score=didthisq.getScore();
        	            allscore=allscore+score;
        	            allpoint=allpoint+point;
        	        }
    				float newdiff=1-allscore/allpoint;
    				//更新难度
    				fillinquestionDao.UpadateDiffC(newdiff, qid);
    			}
    		}
    		//已经不是默认值了，那肯定过10道题了，直接更新
    		else {
				for(Fillinans didthisq:didthisqs){
    	            float score=didthisq.getScore();
    	            allscore=allscore+score;
    	            allpoint=allpoint+point;
    	        }
				float newdiff=1-allscore/allpoint;
				//更新难度
				fillinquestionDao.UpadateDiffC(newdiff, qid);
			}		
    	}
    	//大题
    	if (qtype==3) {
    		Bigquestion thisq=bigquestionDao.getById(qid);
    		float difficulty = thisq.getDifficulty();
    		int point=(int) thisq.getPoint();
    		ArrayList<Answeredbigq> didthisqs=answeredbigqDao.getbyqIDC(qid);
    		float allscore=(float) 0.0;
			int allpoint=0;
			//判断该不该更新，如果difficulty为0.2 or 0.5 or 0.8的默认值，那需要遍历看一下这是不是满10道题了
    		if (difficulty==0.2 || difficulty==0.5 || difficulty==0.8) {
    			int didnumber=didthisqs.size();
    			System.out.println(didnumber);
    			if (didnumber>10) {
    				System.out.println("aaaaa");
    				for(Answeredbigq didthisq:didthisqs){
        	            float score=didthisq.getScore();
        	            allscore=allscore+score;
        	            allpoint=allpoint+point;
        	        }
    				float newdiff=1-allscore/allpoint;
    				//更新难度
    				bigquestionDao.UpadateQDiffC(newdiff, qid);
    			}
    		}
    		//已经不是默认值了，那肯定过10道题了，直接更新
    		else {
				for(Answeredbigq didthisq:didthisqs){
    	            float score=didthisq.getScore();
    	            allscore=allscore+score;
    	            allpoint=allpoint+point;
    	        }
				float newdiff=1-allscore/allpoint;
				//更新难度
				bigquestionDao.UpadateQDiffC(newdiff, qid);
			}		
    	}
    	
    }
    
    	//如果要记笔记
   public void insertcomment(int qtype,String comment,int aid) {
	   if (qtype==0) {choiceansweredDao.insertComment(comment, aid);}
	   else if (qtype==1) {jugementansDao.insertComment(comment, aid);}
	   else if (qtype==2) {fillinansDao.insertComment(comment, aid);}
	   else {answeredbigqDao.insertComment(comment, aid);}
   } 
    
    //根据aid，qtype获取我做的题的信息
    	//选择题
    public Choiceanswered getACqbyaid(int aid) {
    	return choiceansweredDao.getById(aid);
    }
    	//判断题
    public Jugementans getAJqbyaid(int aid) {
    	return jugementansDao.getById(aid);
    }
    	//填空题
    public Fillinans getAFqbyaid(int aid) {
    	return fillinansDao.getById(aid);
    }
    	//大题
    public Answeredbigq getABqbyaid(int aid) {
    	return answeredbigqDao.getById(aid);
    }

    //获取自己做过的题们的qid
    public ArrayList<ArrayList<Integer>> getallmydid(String usermail){
    	ArrayList<ArrayList<Integer>> allmydid=new ArrayList<ArrayList<Integer>>();
    	
		ArrayList<Integer> cqid=choiceansweredDao.getMydidqidC(usermail);
		ArrayList<Integer> jqid=jugementansDao.getMydidqidC(usermail);
		ArrayList<Integer> fqid=fillinansDao.getMydidqidC(usermail);
		ArrayList<Integer> bqid=answeredbigqDao.getMydidqidC(usermail);
		
		allmydid.add(cqid);
		allmydid.add(jqid);
		allmydid.add(fqid);
		allmydid.add(bqid);
		
		return allmydid;
    }

    //获取自己做过的题们的aid
    public ArrayList<ArrayList<Integer>> getallmydidaid(String usermail){
    	ArrayList<ArrayList<Integer>> allmydid=new ArrayList<ArrayList<Integer>>();
    	
		ArrayList<Integer> cqid=choiceansweredDao.getMydidaidC(usermail);
		ArrayList<Integer> jqid=jugementansDao.getMydidaidC(usermail);
		ArrayList<Integer> fqid=fillinansDao.getMydidaidC(usermail);
		ArrayList<Integer> bqid=answeredbigqDao.getMydidaidC(usermail);
		
		allmydid.add(cqid);
		allmydid.add(jqid);
		allmydid.add(fqid);
		allmydid.add(bqid);
		
		return allmydid;
    }
    
    //获取特定科目的我做过的题们的qid和aid
    public ArrayList<ArrayList<Integer>> getallmydidsubject(ArrayList<ArrayList<Integer>> allmydid,String usermail,String subject,int wcq,int wjq,int wfq,int wbq,
    		ArrayList<ArrayList<Integer>> allmydidaid){
    	
    	ArrayList<ArrayList<Integer>> allmydidsubject=new ArrayList<ArrayList<Integer>>();
    	
    	ArrayList<Integer> cqid=new ArrayList<Integer>();
		ArrayList<Integer> jqid=new ArrayList<Integer>();
		ArrayList<Integer> fqid=new ArrayList<Integer>();
		ArrayList<Integer> bqid=new ArrayList<Integer>();
		
		ArrayList<Integer> cqaid=new ArrayList<Integer>();
		ArrayList<Integer> jqaid=new ArrayList<Integer>();
		ArrayList<Integer> fqaid=new ArrayList<Integer>();
		ArrayList<Integer> bqaid=new ArrayList<Integer>();
		
		//如果要选择题
		if (wcq==1) {
			ArrayList<Integer> cqallid=allmydid.get(0);
			ArrayList<Integer> cqallaid=allmydidaid.get(0);
			int listsize=cqallid.size();
			for (int i=0;i<listsize;i++) {
				String thissubject=choiceDao.getById(cqallid.get(i)).getSubject();
				if (thissubject==null) {
					thissubject="";
				}
				if(thissubject.equals(subject)) {
					cqid.add(cqallid.get(i));
					cqaid.add(cqallaid.get(i));
				} 
			}
		}
		if (wjq==1) {
			ArrayList<Integer> jqallid=allmydid.get(1);
			ArrayList<Integer> jqallaid=allmydidaid.get(1);
			int listsize=jqallid.size();
			for (int i=0;i<listsize;i++) {
				String thissubject=jugementinfoDao.getById(jqallid.get(i)).getSubject();
				if (thissubject==null) {
					thissubject="";
				}
				System.out.println(thissubject);
				if(thissubject.equals(subject)) {
					jqid.add(jqallid.get(i));
					jqaid.add(jqallaid.get(i));
				} 
			}
		}
		if (wfq==1) {
			ArrayList<Integer> fqallid=allmydid.get(2);
			ArrayList<Integer> fqallaid=allmydidaid.get(2);
			int listsize=fqallid.size();
			for (int i=0;i<listsize;i++) {
				String thissubject=fillinquestionDao.getById(fqallid.get(i)).getSubject();
				if (thissubject==null) {
					thissubject="";
				}
				if(thissubject.equals(subject)) {
					fqid.add(fqallid.get(i));
					fqaid.add(fqallaid.get(i));
				} 
			}
		}
		if (wbq==1) {
			ArrayList<Integer> bqallid=allmydid.get(3);
			ArrayList<Integer> bqallaid=allmydidaid.get(3);
			int listsize=bqallid.size();
			for (int i=0;i<listsize;i++) {
				String thissubject=bigquestionDao.getById(bqallid.get(i)).getSubject();
				if (thissubject==null) {
					thissubject="";
				}
				if(thissubject.equals(subject)) {
					bqid.add(bqallid.get(i));
					fqaid.add(bqallaid.get(i));
				} 
			}
		}
		allmydidsubject.add(cqid);
		allmydidsubject.add(jqid);
		allmydidsubject.add(fqid);
		allmydidsubject.add(bqid);
		allmydidsubject.add(cqaid);
		allmydidsubject.add(jqaid);
		allmydidsubject.add(fqaid);
		allmydidsubject.add(bqaid);
		
		return allmydidsubject;    	
    }

    //根据qid调题干、科目、分值、examid
    	//调选择题
    public ArrayList<Choice> getneedmyCq (ArrayList<Integer> qidlist){
    	ArrayList<Choice> myqlist=new ArrayList<Choice>();
    	for (int qid:qidlist) {
    		myqlist.add(choiceDao.getById(qid));
    	}
    	return myqlist;
    }
    	//调判断题
    public ArrayList<Jugementinfo> getneedmyJq (ArrayList<Integer> qidlist){
    	ArrayList<Jugementinfo> myqlist=new ArrayList<Jugementinfo>();
    	for (int qid:qidlist) {
    		myqlist.add(jugementinfoDao.getById(qid));
    	}
    	return myqlist;
    }
    	//调填空题
    public ArrayList<Fillinquestion> getneedmyFq (ArrayList<Integer> qidlist){
    	ArrayList<Fillinquestion> myqlist=new ArrayList<Fillinquestion>();
    	for (int qid:qidlist) {
    		myqlist.add(fillinquestionDao.getById(qid));
    	}
    	return myqlist;
    }
    	//调大题
    public ArrayList<Bigquestion> getneedmyBq (ArrayList<Integer> qidlist){
    	ArrayList<Bigquestion> myqlist=new ArrayList<Bigquestion>();
    	for (int qid:qidlist) {
    		myqlist.add(bigquestionDao.getById(qid));
    	}
    	return myqlist;
    }
    
    // 通过审核
    public void admincheck(int qtype,int qid,int pass,String usermail) {
    	if(pass==0) {
    		if (qtype==0) {choiceDao.updateqQKC(0, qid);adminrecordDao.insertNewrecordC(usermail,1,qid,null,1);}
    		else if (qtype==1) {jugementinfoDao.updateqQKC(0, qid);adminrecordDao.insertNewrecordC(usermail,2,qid,null,1);}
    		else if (qtype==2) {fillinquestionDao.updateqQKC(0, qid);adminrecordDao.insertNewrecordC(usermail,3,qid,null,1);}
    		else {bigquestionDao.updateqQKC(0, qid);adminrecordDao.insertNewrecordC(usermail,4,qid,null,1);}
    	}
    	else {
    		if (qtype==0) {choiceDao.updateqQKC(1, qid);adminrecordDao.insertNewrecordC(usermail,1,qid,null,0);}
    		else if (qtype==1) {jugementinfoDao.updateqQKC(1, qid);adminrecordDao.insertNewrecordC(usermail,2,qid,null,0);}
    		else if (qtype==2) {fillinquestionDao.updateqQKC(1, qid);adminrecordDao.insertNewrecordC(usermail,3,qid,null,0);}
    		else {bigquestionDao.updateqQKC(1, qid);adminrecordDao.insertNewrecordC(usermail,4,qid,null,0);}
    	}
    }
    
   
    
    public ArrayList<Integer> forrec() {
    	ArrayList<Integer> qtypeid=new ArrayList<Integer>();
    	Random rand = new Random();
    	int qtype=rand.nextInt(4);
    	int number;
    	if (qtype==0) {
    		ArrayList<Choice> qlist=choiceDao.getallShowQC();
    		number=qlist.size();
    		if (number==0) {qtypeid.add(-1);return qtypeid;}
    	}
    	if (qtype==1) {
    		ArrayList<Jugementinfo> qlist=jugementinfoDao.getallShowQC();
    		number=qlist.size();
    		if (number==0) {qtypeid.add(-1);return qtypeid;}
    	}
    	if (qtype==2) {
    		ArrayList<Fillinquestion> qlist=fillinquestionDao.getallShowQC();
    		number=qlist.size();
    		if (number==0) {qtypeid.add(-1);return qtypeid;}
    	}
    	else {
    		ArrayList<Bigquestion> qlist=bigquestionDao.getallShowQC();
    		number=qlist.size();
    		if (number==0) {qtypeid.add(-1);return qtypeid;}
    	}
    	int a=rand.nextInt(number)+1;
    	qtypeid.add(qtype);
    	qtypeid.add(a);
    	return qtypeid;
    }

}
