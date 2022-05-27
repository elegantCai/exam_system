package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.SetUtils;

import com.example.demo.dao.AdminrecordDao;
import com.example.demo.dao.AnsweredbigqDao;
import com.example.demo.dao.BigquestionDao;
import com.example.demo.dao.ChoiceDao;
import com.example.demo.dao.ChoiceansweredDao;
import com.example.demo.dao.ClassDao;
import com.example.demo.dao.ClassandstudentDao;
import com.example.demo.dao.DistributionDao;
import com.example.demo.dao.ExamclassDao;
import com.example.demo.dao.ExaminfoDao;
import com.example.demo.dao.ExamquestionDao;
import com.example.demo.dao.FillinansDao;
import com.example.demo.dao.FillinquestionDao;
import com.example.demo.dao.JugementansDao;
import com.example.demo.dao.JugementinfoDao;
import com.example.demo.dao.StudentDao;
import com.example.demo.entity.Answeredbigq;
import com.example.demo.entity.Bigquestion;
import com.example.demo.entity.Choice;
import com.example.demo.entity.Choiceanswered;
import com.example.demo.entity.Classes;
import com.example.demo.entity.Distribution;
import com.example.demo.entity.ExamQuestion;
import com.example.demo.entity.Examinfo;
import com.example.demo.entity.Fillinans;
import com.example.demo.entity.Fillinquestion;
import com.example.demo.entity.Jugementans;
import com.example.demo.entity.Jugementinfo;
import com.example.demo.entity.Student;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

@Service("examService")
public class ExamService {
	@Autowired 
	private ExaminfoDao examinfoDao;
	@Autowired 
	private ExamclassDao examclassDao;
	@Autowired 
	private ClassDao classDao;
	@Autowired 
	private ExamquestionDao examquestionDao;
	@Autowired 
	private BigquestionDao bigquestionDao;
	@Autowired 
	private JugementinfoDao jugementinfoDao;
	@Autowired 
	private FillinquestionDao fillinquestionDao;
	@Autowired 
	private ChoiceDao choiceDao;
	@Autowired 
	private ClassandstudentDao classandstudentDao;
	@Autowired 
	private StudentDao studentDao;
	@Autowired 
	private DistributionDao distributionDao;
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
	
	//趣味小环节，随机签到照片
	public String checkinpicpath() {
		String[] piclist= {"images/checkinginpic/1.jpg","images/checkinginpic/2.jpg","images/checkinginpic/3.jpg","images/checkinginpic/4.jpg",
				"images/checkinginpic/5.jpg","images/checkinginpic/6.jpg","images/checkinginpic/7.jpg","images/checkinginpic/8.jpg","images/checkinginpic/9.jpg",
				"images/checkinginpic/10.jpg","images/checkinginpic/11.jpg","images/checkinginpic/12.jpg","images/checkinginpic/13.jpg"};
		int num=(int)(Math.random()*(13));
		return piclist[num];
	}
	//转化时间的小函数
		//先转化成回显的字符串
 	public String transtimestr(String stringtime) throws ParseException {
		String[] split=stringtime.split("T");
		String formtime=split[0]+" "+split[1]+":00";
		return formtime;
	}
	public Calendar transtime(String formtime) throws ParseException {
		Calendar ctime =Calendar.getInstance();
		ctime.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formtime));
		return ctime;
	}
	
	//int[] 转arraylist并且插入新的再转回int[]
	public int[] transarrayandinsert(int[] examqlist,int newqid) {
		ArrayList<Integer> arrqlist = new ArrayList<Integer>();
		if (examqlist!=null) {
			for (int i:examqlist) {
				arrqlist.add(i);
			}
			arrqlist.add(newqid);
			int listsize=arrqlist.size();
			int[] newexamqlist = new int[listsize];
			for(int i = 0;i<listsize;i++){
				newexamqlist[i] = arrqlist.get(i);
				}
			return newexamqlist;
		}
		else {
			int[] newexamqlist = {newqid};
			return newexamqlist;
		}
	}	
	//插入考试信息
	public int insertnewexam(String teachermail,Calendar starttime,Calendar endtime,Integer type,String subject,String[] classidlist) {
		examinfoDao.insertNewexamX(teachermail,starttime,endtime,type,subject,0);
		int newexamid=examinfoDao.getNewByEmailX(teachermail);
		//插入班级考试表
		for(String strclassid:classidlist) {
			int classid=Integer.parseInt(strclassid);
			examclassDao.insertNewexamclassX(newexamid, classid);	
		}
		return newexamid;
		
	}
	
	//拿到这个老师的班级，供老师选择
	public ArrayList<Classes> getteacherpassedclass(String teachermail){
		return classDao.getTeacherClassC(teachermail, 2);
	}
		//拿到这个班级的名字
	public String[] getclassnames(String[] classidlist) {
		ArrayList<String> classnames = new ArrayList<String>();
		String classname;
		for(String strclassid:classidlist) {
			int classid=Integer.parseInt(strclassid);
			classname=classDao.getById(classid).getClassname();
			classnames.add(classname);
		}
		String[] arrString = classnames.toArray(new String[classnames.size()]) ;
		return arrString;
	}
	
	public ArrayList<String> getclassnames2(ArrayList<Integer> classofexam) {
		ArrayList<String> classnames=new ArrayList<String>();
		for(int classid:classofexam) {
			String classname=classDao.getById(classid).getClassname();
			classnames.add(classname);
		}
		return classnames;
	}
	
	//插入考试题的信息
	public void insertexamq(int examid,int qtype,int qid) {
		examquestionDao.insertNewexamQX(examid, qid, qtype);
	}
	//插入考试的总分
	public void updatetotalpoint(int examid,int totalpoint) {
		examinfoDao.inserttotalpointC(totalpoint, examid);
	}
	
	//返回考试题的所有信息(根据考试id看试卷)
		//拿考试题的信息
	public Examinfo getExambyid(int examid) {
		return examinfoDao.getById(examid);
	}
		//拿考试题的试题号
	public ArrayList<Integer> getExamqid(int examid){
		return examquestionDao.getAllQidByIdC(examid);
	} 
		//拿考试题的题型号
	public ArrayList<Integer> getExamqtype(int examid){
		return examquestionDao.getallQtypeByIdC(examid);
	} 
		//拿试题号和题型号的对应
	public ArrayList<ArrayList<Integer>> getcorrQidandQtype(ArrayList<Integer> qidlist,ArrayList<Integer> qtypelist){
		int qnumber=qidlist.size();
		ArrayList<ArrayList<Integer>> corrQidandtype=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> cqid=new ArrayList<Integer>();
		ArrayList<Integer> jqid=new ArrayList<Integer>();
		ArrayList<Integer> fqid=new ArrayList<Integer>();
		ArrayList<Integer> bqid=new ArrayList<Integer>();
		corrQidandtype.add(cqid);
		corrQidandtype.add(jqid);
		corrQidandtype.add(fqid);
		corrQidandtype.add(bqid);
		for(int i = 0;i<qnumber;i++){
			//是选择题
			if (qtypelist.get(i)==0) {
				cqid.add(qidlist.get(i));
			}
			//判断题
			if (qtypelist.get(i)==1) {
				jqid.add(qidlist.get(i));
			}
			//填空题
			if (qtypelist.get(i)==2) {
				fqid.add(qidlist.get(i));
			}
			//大题
			if (qtypelist.get(i)==3) {
				bqid.add(qidlist.get(i));
			}
		}
		corrQidandtype.add(cqid);
		corrQidandtype.add(jqid);
		corrQidandtype.add(fqid);
		corrQidandtype.add(bqid);
		return corrQidandtype;
	}
		//用对应列表取对应的题的信息
	public ArrayList<Choice> getCqofexam(ArrayList<Integer> qlist){
		ArrayList<Choice> myqlist=new ArrayList<Choice>();
		for (int i:qlist) {
			Choice thisq=choiceDao.getById(i);
			myqlist.add(thisq);
		}
		return myqlist;
	}
	public ArrayList<Jugementinfo> getJqofexam(ArrayList<Integer> qlist){
		ArrayList<Jugementinfo> myqlist=new ArrayList<Jugementinfo>();
		for (int i:qlist) {
			Jugementinfo thisq=jugementinfoDao.getById(i);
			myqlist.add(thisq);
		}
		return myqlist;
	}
	public ArrayList<Fillinquestion> getFqofexam(ArrayList<Integer> qlist){
		ArrayList<Fillinquestion> myqlist=new ArrayList<Fillinquestion>();
		for (int i:qlist) {
			Fillinquestion thisq=fillinquestionDao.getById(i);
			myqlist.add(thisq);
		}
		return myqlist;
	}
	public ArrayList<Bigquestion> getBqofexam(ArrayList<Integer> qlist){
		ArrayList<Bigquestion> myqlist=new ArrayList<Bigquestion>();
		for (int i:qlist) {
			Bigquestion thisq=bigquestionDao.getById(i);
			myqlist.add(thisq);
		}
		return myqlist;

	}
		//拿这场考试的班级
	public ArrayList<Integer> getclassofexam(int examid){
		return examclassDao.getClassidByIdX(examid);
	}
	
	//分发考试
	public void distributeexam2(String[] classidlist,int examid) {
		ArrayList<String> stumaillist=new ArrayList<String>();
		for (String i:classidlist) {
			int classid=Integer.parseInt(i);
			ArrayList<Integer> stuidlist=classandstudentDao.getStudentidByClassidX(classid);
			for (int j:stuidlist) {
				String stumail=studentDao.getById(j).getUsermail();
				stumaillist.add(stumail);
			}
		}
		for (String stumail:stumaillist) {
			distributionDao.insertNewQuestionC(examid, stumail);
		}
	}
	
	public void distributeexam(ArrayList<Integer> classidlist,int examid) {
		ArrayList<String> stumaillist=new ArrayList<String>();
		for(int classid:classidlist) {
			ArrayList<Integer> stuidlist=classandstudentDao.getStudentidByClassidX(classid);
			for (int j:stuidlist) {
				String stumail=studentDao.getById(j).getUsermail();
				stumaillist.add(stumail);
			}
		}
		for (String stumail:stumaillist) {
			distributionDao.insertNewQuestionC(examid, stumail);
		}
	}
	//考试页面(看到自己相关的考试，不能瞎搜)
		//老师看见发布的考试们
	public ArrayList<ArrayList<Examinfo>> getmyupexam(String usermail){
		ArrayList<Examinfo> allmyup=examinfoDao.getExaminfoByTeachermailX(usermail);
		 //0：未审核 1：未开始 2：考试中 3：结束未批改完成 4：结束并批改完成 5：签到中 6：审核未通过
		ArrayList<Examinfo> checking=new ArrayList<Examinfo>();	
		ArrayList<Examinfo> notbegin=new ArrayList<Examinfo>();	
		ArrayList<Examinfo> examing=new ArrayList<Examinfo>();	
		ArrayList<Examinfo> finish=new ArrayList<Examinfo>();
		ArrayList<Examinfo> corrected=new ArrayList<Examinfo>();
		ArrayList<Examinfo> checkin=new ArrayList<Examinfo>();
		ArrayList<Examinfo> fail=new ArrayList<Examinfo>();
		for(Examinfo i:allmyup) {
			int state=i.getState();
			if (state==0) {checking.add(i);}
			else if (state==1) {notbegin.add(i);}
			else if (state==2) {examing.add(i);}
			else if (state==3) {finish.add(i);}
			else if (state==4) {corrected.add(i);}
			else if (state==5) {checkin.add(i);}
			else {fail.add(i);}
		}
		ArrayList<ArrayList<Examinfo>> bystate=new ArrayList<ArrayList<Examinfo>>();
		bystate.add(checking);bystate.add(notbegin);bystate.add(examing);bystate.add(finish);
		bystate.add(corrected);bystate.add(checkin);bystate.add(fail);
		return bystate;
	}
		//管理员看见所有的特定状态的考试
	public ArrayList<Examinfo> getSpecState(int state){
		return examinfoDao.getNotcheckC(state);
	}
		//老师查看自己特定状态的考试们
	public ArrayList<Examinfo> getmyexamwithtype(String usermail,int state){
		return examinfoDao.searchmyexamC(usermail, state);
	}
		//学生看见分发给自己的考试们(根据状态)
	public ArrayList<Distribution> getmyexam(String usermail,int state){
		return distributionDao.getmyexamsC(usermail,state);}
	public ArrayList<Examinfo> getcorexams(ArrayList<Distribution> myexams){
		ArrayList<Examinfo> corexams=new ArrayList<Examinfo>();
		for (Distribution i:myexams) {
			int examid=i.getExamid();
			corexams.add(examinfoDao.getById(examid));
		}
		return corexams;
	}
	
	//判断是不是签到的
	public Boolean judgetime(Calendar calendartime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, +10);
        String nowbefore10 = sdf.format(now.getTime());
        String nowbefore101 = nowbefore10 +":00";
        Calendar ctime =Calendar.getInstance();
		ctime.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowbefore101));
		return calendartime.before(ctime);
	}
	
	//判断是不是结束、开始的
	public Boolean judgetime2(Calendar calendartime) {
		Calendar now = Calendar.getInstance();
		return calendartime.before(now);
	}
	//更新考试状态
	public void updateexamState(int state,int examid) {
		examinfoDao.updateStateC(state, examid);
	}
	
	public Examinfo getexambyid(int examid) {
		return examinfoDao.getById(examid);
	}
	
	//查找考试的分发表并且改变状态
		//签到的时候
	public void updateDstate1(int examid) {
		distributionDao.updateCkinC(examid);
	}
		//考试结束的时候
	public void updateDstate2(int examid) {
		ArrayList<Integer> Didlist=distributionDao.getDSofexamsC(examid,4);
		if (Didlist.size()==0) {
			Didlist=distributionDao.getDSofexamsC(examid,2);
		}
		for (int did:Didlist) {
			distributionDao.updatemyDstate1C(1, did);
		}
	}
		//把题目的状态改变
	public void updateDstate3(int examid,int toqb) {
		if (toqb==0) {
			ArrayList<Integer> cqidlist=examquestionDao.getQidByIdX(examid, 0);
			ArrayList<Integer> jqidlist=examquestionDao.getQidByIdX(examid, 1);
			ArrayList<Integer> fqidlist=examquestionDao.getQidByIdX(examid, 2);
			ArrayList<Integer> bqidlist=examquestionDao.getQidByIdX(examid, 3);
			for (int i:cqidlist) {
				choiceDao.UpadateShowC(1, i);
			}
			for (int i:jqidlist) {
				jugementinfoDao.UpadateShowC(1, i);
			}
			for (int i:fqidlist) {
				fillinquestionDao.UpadateShowC(1, i);
			}
			for (int i:bqidlist) {
				bigquestionDao.UpadateQShowC(1, i);
			}
		}
	}
		//更改这个学生的签到状态顺便把考试的所有题都下发
	public ArrayList<ArrayList<Integer>> updateThisstu(int examid,String stumail) {
		ArrayList<ArrayList<Integer>> examqidlist=new ArrayList<ArrayList<Integer>>();
		distributionDao.updatemyCK(5, examid,stumail);
			//选择题
		ArrayList<Integer> cqidlist=examquestionDao.getQidByIdX(examid, 0);
		for (int i:cqidlist) {
			choiceansweredDao.insertNewQuestionExamC(i, examid, stumail);
		}
		ArrayList<Integer> jqidlist=examquestionDao.getQidByIdX(examid, 1);
		for (int i:jqidlist) {
			jugementansDao.insertNewQuestionExamC(i, examid, stumail);
		}
		ArrayList<Integer> fqidlist=examquestionDao.getQidByIdX(examid, 2);
		for (int i:fqidlist) {
			fillinansDao.insertNewQuestionExamC(i, examid, stumail);
		}
		ArrayList<Integer> bqidlist=examquestionDao.getQidByIdX(examid, 3);
		for (int i:bqidlist) {
			answeredbigqDao.insertNewQuestionExamC(i, examid, stumail);
		}
		examqidlist.add(cqidlist);
		examqidlist.add(jqidlist);
		examqidlist.add(fqidlist);
		examqidlist.add(bqidlist);
		return examqidlist;
	}
	
	//ArrayList转int[]
	public int[]transAtoL(ArrayList<Integer> arrlist)	{
		int listsize=arrlist.size();
		int[]newexamqlist = new int[listsize];
		for(int i = 0;i<listsize;i++){
			newexamqlist[i] = arrlist.get(i);
			}
		return newexamqlist;
	}
	
	//int[]转ArrayList
	public ArrayList<Integer> transLtoA(int [] mylist){
		ArrayList<Integer> AList=new ArrayList<Integer>();
		for (int i :mylist) {
			AList.add(i);
		}
		return AList;
	}
	
	//考试中看自己某道题的作答情况
		//选择题
	public Choiceanswered getAcqduringExam(int examid,String usermail,int qid) {
		return choiceansweredDao.getExamCQC(examid, usermail, qid);
	}
	public Jugementans getAjqduringExam(int examid,String usermail,int qid) {
		return jugementansDao.getExamCQC(examid, usermail, qid);
	}
	public Fillinans getAfqduringExam(int examid,String usermail,int qid) {
		return fillinansDao.getExamCQC(examid, usermail, qid);
	}
	public Answeredbigq getAbqduringExam(int examid,String usermail,int qid) {
		return answeredbigqDao.getExamCQC(examid, usermail, qid);
	}
	//考试中更新做过表
		//选择题
	public void updateECq(int examid,int qid,String answer,String answermail) {
		choiceansweredDao.updateExamC(answer, examid, answermail, qid);	
	}
		//判断题
	public void updateEJq(int examid,int qid,String answer,String answermail) {
		jugementansDao.updateExamC(answer, examid, answermail, qid);	
	}
		//填空题
	public void updateEFq(int examid,int qid,String answer,String answermail) {
		fillinansDao.updateExamC(answer, examid, answermail, qid);	
	}
		//大题
	public void updateEBq(int examid,int qid,String answer,String answermail) {
		answeredbigqDao.updateExamC(answer, examid, answermail, qid);	
	}
		//大题还得再拿aid存图片
	public int getEBQaid(int examid,int qid,String answermail) {
		return answeredbigqDao.getExamCQC(examid, answermail, qid).getId();
	}
		//存图片
	public void UpdateEBp(int aid,String picpath) {
		answeredbigqDao.updateExamPC(picpath, aid);
	}
	
		//交卷之后自动批客观题
			//选择
	public float  Getacqlistinfo(ArrayList<Choice> cqlist,int examid,String usermail){
		float totalcq=0;
		for(Choice i:cqlist) {
			float score;
			String realanswer=i.getAnswer();
			int qid=i.getId();
			int point =(int) i.getPoint();
			Choiceanswered thisaq=choiceansweredDao.getExamCQC(examid, usermail, qid);
			int aid=thisaq.getId();
			String stuanswer=thisaq.getAnswer();
			if (stuanswer==null) {score=0;}
			else {
				if(stuanswer.equals(realanswer)) {
					score=point;
					System.out.println(score);
				}
				else{score=0;}
			}
			choiceansweredDao.updateExamSC(score, aid);
			totalcq=totalcq+score;
		}
		return totalcq;
	}
	
			//判断题
			public float Getajqlistinfo(ArrayList<Jugementinfo> cqlist,int examid,String usermail){
				float totaljq=0;
				for(Jugementinfo i:cqlist) {
					float score;
					String realanswer=i.getAnswer();
					int qid=i.getId();
					int point =(int) i.getPoint();
					Jugementans thisaq=jugementansDao.getExamCQC(examid, usermail, qid);
					int aid=thisaq.getId();
					String stuanswer=thisaq.getAnswer();
					if (stuanswer==null) {score=0;}
					else {
						if(stuanswer.equals(realanswer)) {
							score=point;
						}
						else{score=0;}
					}
					jugementansDao.updateExamSC(score, aid);
					totaljq=totaljq+score;
				}
				return totaljq;
			}
			//填空
			public float Getafqlistinfo(ArrayList<Fillinquestion> cqlist,int examid,String usermail){
				float totalfq=0;
				for(Fillinquestion i:cqlist) {
					float score;
					String realanswer=i.getAnswer();
					int qid=i.getId();
					int point =(int) i.getPoint();
					Fillinans thisaq=fillinansDao.getExamCQC(examid, usermail, qid);
					int aid=thisaq.getId();
					String stuanswer=thisaq.getAnswer();
					if (stuanswer==null) {score=0;}
					else {
						if(stuanswer.equals(realanswer)) {
							score=point;
						}
						else{score=0;}
					}
					fillinansDao.updateExamSC(score, aid);
					totalfq=totalfq+score;
				}
				return totalfq;
			}
			//给大题上一个假分（为了避免空）
			public void Getabqlistinfo(ArrayList<Integer> cqlist,int examid,String usermail) {
				for(int i:cqlist) {
					Answeredbigq thisaq=answeredbigqDao.getExamCQC(examid, usermail, i);
					answeredbigqDao.updateExamSC(-1,thisaq.getId());
				}
			}
		
			//拿到所有选择题的信息
			public ArrayList<Choiceanswered> Getacqinfo(ArrayList<Choice> cqlist,int examid,String usermail){
				ArrayList<Choiceanswered> acqlist=new ArrayList<Choiceanswered>();
				for (Choice i:cqlist) {
					int qid=i.getId();
					Choiceanswered thisaq=choiceansweredDao.getExamCQC(examid, usermail, qid);
					acqlist.add(thisaq);
				}
				return acqlist;
			}
			//拿到所有判断的信息
			public ArrayList<Jugementans> Getajqinfo(ArrayList<Jugementinfo> cqlist,int examid,String usermail){
				ArrayList<Jugementans> acqlist=new ArrayList<Jugementans>();
				for (Jugementinfo i:cqlist) {
					int qid=i.getId();
					Jugementans thisaq=jugementansDao.getExamCQC(examid, usermail, qid);
					acqlist.add(thisaq);
				}
				return acqlist;
			}
			//拿到所有填空题的信息
			public ArrayList<Fillinans> Getafqinfo(ArrayList<Fillinquestion> cqlist,int examid,String usermail){
				ArrayList<Fillinans> acqlist=new ArrayList<Fillinans>();
				for (Fillinquestion i:cqlist) {
					int qid=i.getId();
					Fillinans thisaq=fillinansDao.getExamCQC(examid, usermail, qid);
					acqlist.add(thisaq);
				}
				return acqlist;
			}
			//拿到所有大题的信息
			public ArrayList<Answeredbigq> Getabqinfo(ArrayList<Bigquestion> cqlist,int examid,String usermail){
				ArrayList<Answeredbigq> acqlist=new ArrayList<Answeredbigq>();
				for (Bigquestion i:cqlist) {
					int qid=i.getId();
					Answeredbigq thisaq=answeredbigqDao.getExamCQC(examid, usermail, qid);
					acqlist.add(thisaq);
				}
				return acqlist;
			}
			
			//更新总分
			public float partScore(ArrayList<Choiceanswered> acqinfo,ArrayList<Jugementans> ajqinfo,ArrayList<Fillinans> afqinfo,int examid,String usermail) {
				float partscore=0;
				for (Choiceanswered i:acqinfo) {
					partscore=partscore+i.getScore();
					
				}
				for (Jugementans i:ajqinfo) {
					partscore=partscore+i.getScore();
				}
				for (Fillinans i:afqinfo) {
					partscore=partscore+i.getScore();
				}
				distributionDao.updateTPC(partscore, examid, usermail);
				return partscore;
			}
			//更新真总分
			public void updateparttotal(float partscore,int examid,String usermail) {
				distributionDao.updateTPC(partscore, examid, usermail);
			}
			//根据邮箱答题人，获取distribution表
			public Distribution getDexaminfo(int examid,String usermail) {
				return distributionDao.getmythisexamall(usermail, examid);
			}
			
			//查找特定考试的题目，并且分状态
			public ArrayList<ArrayList<Integer>> getexamqlist(int examid){
				ArrayList<Integer> cqlist=new ArrayList<Integer>();
				ArrayList<Integer> jqlist=new ArrayList<Integer>();
				ArrayList<Integer> fqlist=new ArrayList<Integer>();
				ArrayList<Integer> bqlist=new ArrayList<Integer>();
				ArrayList<Integer> bqcorlist=new ArrayList<Integer>();
				
				ArrayList<ExamQuestion> allqlist=examquestionDao.getallQByIdC(examid);
				for (ExamQuestion i:allqlist) {
					if (i.getQtype()==0) {
						cqlist.add(i.getQid());
					}
					else if (i.getQtype()==1) {
						jqlist.add(i.getQid());
					}
					else if (i.getQtype()==2) {
						fqlist.add(i.getQid());
					}
					else {
						bqlist.add(i.getQid());
						bqcorlist.add(i.getFinishedcor());
					}
				}
				ArrayList<ArrayList<Integer>> allqidlist=new ArrayList<ArrayList<Integer>> ();
				allqidlist.add(cqlist);
				allqidlist.add(jqlist);
				allqidlist.add(fqlist);
				allqidlist.add(bqlist);
				allqidlist.add(bqcorlist);
				return allqidlist;
			}
			
			//拿到那场考试的批改简答题的aid，并且获取批没批
			public ArrayList<ArrayList<Integer>> getbqaid(int examid,int qid){
				 ArrayList<Answeredbigq> aqlist=answeredbigqDao.getEAQC(examid, qid);
				 ArrayList<Integer> aidlist=new  ArrayList<Integer>();
				 ArrayList<Integer> scorelist=new  ArrayList<Integer>();
				 for (Answeredbigq i:aqlist) {
					aidlist.add(i.getId());
					float score=i.getScore();
					scorelist.add((int) (score));
				 }
				 ArrayList<ArrayList<Integer>> alllist=new ArrayList<ArrayList<Integer>>();
				 alllist.add(aidlist);
				 alllist.add(scorelist);
				 return alllist;
			}
			
			public int showindex(ArrayList<Integer> scorelist) {
				int showindex=0;
				for (int i:scorelist) {
					if(i!=-1) {
						showindex=showindex+1;
					}
					else
					{
						return showindex;
					}
				}
				return -1;
			}
			
			public Answeredbigq getthisaqinfo(int aid) {
				return answeredbigqDao.getById(aid);
			}	
			public Bigquestion getthisbqinfo(int qid) {
				return bigquestionDao.getById(qid);
			}	
			public void Corbq(int aid,float getscore,String comment) {
				answeredbigqDao.updateExamSTC(getscore,comment, aid);
			}
			public ArrayList<int[]> updateColumn(int aid,float getscore,int [] scorelist,int [] aidlist) {
				int finish=1;
				int size=scorelist.length;
				for (int i=0;i<size;i++) {
					if (aid==aidlist[i]) {
						scorelist[i]=(int) getscore;
					}
					if(scorelist[i]==-1) {
						finish=0;
					}
				}
				ArrayList<int[]> hhh=new ArrayList<int[]>();
				hhh.add(scorelist);
				int [] forfinish= {finish};
				hhh.add(forfinish);
				return hhh;			
			}
			
			//(顺道看看这大题批没批完，如果批完了可以更新一下考试题目表)
			public void updateEQ(int examid,int qid) {
				examquestionDao.updatefinishC(examid, qid);
				ArrayList<ExamQuestion> bqlist=examquestionDao.getQidByIdC(examid, 3);
				boolean allfinished=true;
				for (ExamQuestion i:bqlist) {
					int thisqid=i.getQid();
					int cor=i.getFinishedcor();
					if (cor==0 && thisqid!=qid) {
						allfinished=false;
					}
				} 
				if (allfinished) {
					examinfoDao.updateStateC(4, examid);
					//顺便更新分发表的总分们(已经考试的人）
					ArrayList<Distribution> dlist=distributionDao.getallDSofexamsC(examid, 0);
					
					for (Distribution i:dlist) {
						String stumail=i.getStudentemail();
						float nowscore=i.getTotalpoint();
						int did=i.getId();
						ArrayList<Float> floatsa=answeredbigqDao.getSPC(examid, stumail);
						for (float j:floatsa) {
							nowscore=nowscore+j;
						}
						distributionDao.updateTFPC(nowscore,did);
						}	
				}
			}
			
			//看看考试的状态应该是什么
			public int getstate (int examid) {
				ArrayList<ExamQuestion> bqlist=examquestionDao.getallQBByIdC(examid);
				ArrayList<Integer> bqidlist=new ArrayList<Integer>();
				if (bqlist.size()==0) {return 4;}
				boolean finished=true;
				for (ExamQuestion i:bqlist) {
					if (i.getFinishedcor()==0) {
						finished=false;
					}
					bqidlist.add(i.getQid());
				}
				int state=-1;
				if (finished) {
					examinfoDao.updateStateC(4, examid);
					//顺便更新分发表的总分们(已经考试的人）
					ArrayList<Distribution> dlist=distributionDao.getallDSofexamsC(examid, 0);
					
					for (Distribution i:dlist) {
						String stumail=i.getStudentemail();
						float nowscore=i.getTotalpoint();
						int did=i.getId();
						ArrayList<Float> floatsa=answeredbigqDao.getSPC(examid, stumail);
						for (float j:floatsa) {
							nowscore=nowscore+j;
						}
						distributionDao.updateTFPC(nowscore,did);
						}	
					state=4;
				}
				return state;
			}
			
			
			//审核考试
			public void checkexam(String usermail,int examid,String comment,int examState) {
				if (examState==1) {
					adminrecordDao.insertNewrecordC(usermail,0,examid,comment, 0);
				}
				else {
					adminrecordDao.insertNewrecordC(usermail,0,examid,comment, 1);  //审核不通过
				}
			}
			
			//获取那个班级的全部学生名单
			public ArrayList<String> getstudentlist (ArrayList<Integer> classidlist){
				ArrayList<String> stumaillist=new ArrayList<String>();
				for(int classid:classidlist) {
					ArrayList<Integer> stuidlist=classandstudentDao.getStudentidByClassidX(classid);
					for (int j:stuidlist) {
						String stumail=studentDao.getById(j).getUsermail();
						stumaillist.add(stumail);
					}
				}
				return stumaillist;
			}
			
			//获取那个考试的所有did
			public ArrayList<ArrayList<Integer>> getdidlist(int examid){
				ArrayList<Distribution> dlist=distributionDao.getallDofexamsC(examid);
				ArrayList<Integer> didlist=new ArrayList<Integer>();
				ArrayList<Integer> absent=new ArrayList<Integer>();
				ArrayList<Integer> nowscore=new ArrayList<Integer>();
				for (Distribution i:dlist) {
					didlist.add(i.getId());
					absent.add(i.getAbsent());
					float fnowscore=i.getTotalpoint();
					nowscore.add((int) fnowscore);
				}
				ArrayList<ArrayList<Integer>> dinfos=new ArrayList<ArrayList<Integer>>();
				dinfos.add(didlist);
				dinfos.add(absent);
				dinfos.add(nowscore);
				return dinfos;		
			}
			
			//获取考试试题列表
			public ArrayList<ArrayList<Integer>> getqofexam(int examid) {
				ArrayList<ArrayList<Integer>> examqidlist=new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> cqidlist=examquestionDao.getQidByIdX(examid, 0);
				ArrayList<Integer> jqidlist=examquestionDao.getQidByIdX(examid, 1);
				ArrayList<Integer> fqidlist=examquestionDao.getQidByIdX(examid, 2);
				ArrayList<Integer> bqidlist=examquestionDao.getQidByIdX(examid, 3);
				examqidlist.add(cqidlist);
				examqidlist.add(jqidlist);
				examqidlist.add(fqidlist);
				examqidlist.add(bqidlist);
				return examqidlist;
			}
			
			//获取那个distribution
			public Distribution thisd(int did) {
				return distributionDao.getById(did);
			}
			
			public ArrayList<Integer> analycqinfo(int examid,int qid,int maxscore){
				ArrayList<Choiceanswered> aqinfo= choiceansweredDao.getEQC(examid, qid);
				ArrayList<Integer> someinfo=new ArrayList<Integer>();
				int totalscore=0;
				int totalmaxscore=0;
				int answerA=0;
				int answerB=0;
				int answerC=0;
				int answerD=0;
				int noanswer=0;
				int cornumber=0;
				int wrongnumber=0;
				for(Choiceanswered i:aqinfo) {
					float fscore=i.getScore();
					int score=(int) fscore;
					if (score>0) {cornumber=cornumber+1;}
					else {wrongnumber=wrongnumber+1;}
					totalscore=totalscore+score;
					totalmaxscore=totalmaxscore+maxscore;
					String answer=i.getAnswer();
					if (answer==null) {noanswer=noanswer+1;}
					else {
						if(answer.equals("A")) {
							answerA=answerA+1;
						}
						else if(answer.equals("B")) {
							answerB=answerB+1;
						}
						else if(answer.equals("C")) {
							answerC=answerC+1;
						}
						else{
							answerD=answerD+1;
						}
					}
				}
				someinfo.add(totalscore);
				someinfo.add(totalmaxscore);
				someinfo.add(answerA);
				someinfo.add(answerB);
				someinfo.add(answerC);
				someinfo.add(answerD);
				someinfo.add(noanswer);
				someinfo.add(cornumber);
				someinfo.add(wrongnumber);
				return someinfo;
			}
			
			public ArrayList<Integer> analyjqinfo(int examid,int qid,int maxscore){
				ArrayList<Jugementans> aqinfo= jugementansDao.getEQC(examid, qid);
				ArrayList<Integer> someinfo=new ArrayList<Integer>();
				int totalscore=0;
				int totalmaxscore=0;
				int answerT=0;
				int answerF=0;
				int noanswer=0;
				int cornumber=0;
				int wrongnumber=0;
				for(Jugementans i:aqinfo) {
					float fscore=i.getScore();
					int score=(int) fscore;
					if (score>0) {cornumber=cornumber+1;}
					else {wrongnumber=wrongnumber+1;}
					totalscore=totalscore+score;
					totalmaxscore=totalmaxscore+maxscore;
					String answer=i.getAnswer();
					if (answer==null) {noanswer=noanswer+1;}
					else {
						if(answer.equals("T")) {
							answerT=answerF+1;
						}
						else {
							answerF=answerF+1;
						}
					}
				}
				someinfo.add(totalscore);
				someinfo.add(totalmaxscore);
				someinfo.add(answerT);
				someinfo.add(answerF);
				someinfo.add(noanswer);
				someinfo.add(cornumber);
				someinfo.add(wrongnumber);
				return someinfo;
			}
			
			public class Forfill{
				private ArrayList<String> stuanswers;
				private ArrayList<Integer> someinfo;
				public ArrayList<String> getStuanswers() {  
			     	   return stuanswers;  
			     }  
			    public void setStuanswers(ArrayList<String> stuanswers) {  
			    		this.stuanswers =stuanswers;  
			     	}
			    public ArrayList<Integer> getSomeinfo(){
			    	return someinfo;
			    }
			    public void setSomeinfo(ArrayList<Integer> someinfo) {  
		    		this.someinfo =someinfo;  
		     	}
			}
			
			public Forfill analyfqinfo(int examid,int qid,int maxscore){
				ArrayList<Fillinans> aqinfo= fillinansDao.getEQC(examid, qid);
				System.out.println(aqinfo.size());
				ArrayList<Integer> someinfo=new ArrayList<Integer>();
				ArrayList<String> stuanswers=new ArrayList<String>();
				int totalscore=0;
				int totalmaxscore=0;
				int noanswer=0;
				int cornumber=0;
				int wrongnumber=0;
				for(Fillinans i:aqinfo) {
					float fscore=i.getScore();
					int score=(int) fscore;
					if (score>0) {cornumber=cornumber+1;}
					else {wrongnumber=wrongnumber+1;}
					totalscore=totalscore+score;
					totalmaxscore=totalmaxscore+maxscore;
					String answer=i.getAnswer();
					if (answer==null) {noanswer=noanswer+1;}
					else {
						stuanswers.add(answer);
					}
				}
				someinfo.add(totalscore);
				someinfo.add(totalmaxscore);
				someinfo.add(noanswer);
				//someinfo.add(cornumber);
				someinfo.add(cornumber);
				someinfo.add(wrongnumber);
				Forfill fillinfos=new Forfill();
				fillinfos.setSomeinfo(someinfo);
				fillinfos.setStuanswers(stuanswers);
				return fillinfos;
			}
			
			public class Forbig{
				private ArrayList<String> goodanswers;
				private ArrayList<String> answerpics;
				private ArrayList<String> goodmail;
				private ArrayList<Float> stugetscore;
				private Float scorerate;
				private ArrayList<Float> goodscore;
				
				public ArrayList<String> getGoodanswers() {  
			     	   return goodanswers;  
			     }  
			    public void setGoodanswers(ArrayList<String> goodanswers) {  
			    		this.goodanswers =goodanswers;  
			     	}
			    public ArrayList<String> getAnswerpics() {  
			     	   return answerpics;  
			     }  
			    public void setGoodmail(ArrayList<String> goodmail) {  
			    		this.goodmail =goodmail;  
			     	}
			    public ArrayList<String> getGoodmail() {  
			     	   return goodmail;  
			     }  
			    public void setAnswerpics(ArrayList<String> answerpics) {  
			    		this.answerpics =answerpics;  
			     	}
			    public ArrayList<Float> getStugetscore() {  
			     	   return stugetscore;  
			     }  
			    public void setStugetscore(ArrayList<Float> stugetscore) {  
			    		this.stugetscore=stugetscore;  
			     	}
			    public ArrayList<Float> getGoodgetscore() {  
			     	   return goodscore;  
			     }  
			    public void setGoodgetscore(ArrayList<Float> goodscore) {  
			    		this.goodscore=goodscore;  
			     	}
			    public Float getScorerate() {
			    	return scorerate;
			    }
			    public void setScorerate(Float scorerate) {
			    	this.scorerate=scorerate;
			    }
			}
			
			public Forbig analybqinfo(int examid,int qid,int maxscore) {
				ArrayList<Answeredbigq> aqinfo=answeredbigqDao.getEAQC(examid, qid);
				ArrayList<String> goodanswers=new ArrayList<String>();
				ArrayList<String> answerpics=new ArrayList<String>();
				ArrayList<String> goodmail=new ArrayList<String>();
				ArrayList<Float> stugetscore=new ArrayList<Float>();
				ArrayList<Float> goodscore=new ArrayList<Float>();
				float totalscore=0;
				int totalmaxscore=0; 
				int forcount=0;
				for(Answeredbigq i:aqinfo) {
					float score=i.getScore();
					totalscore=totalscore+score;
					totalmaxscore=totalmaxscore+maxscore;
					if (forcount<4) {
						goodanswers.add(i.getAnswer());
						answerpics.add(i.getAnswerpic());
						goodmail.add(i.getAnswermail());
						goodscore.add(score);
					}
					forcount=forcount+1;
					stugetscore.add(score);
				}
				float scorerate;
				if (totalmaxscore!=0)
						{scorerate=(float)totalscore/(float)totalmaxscore;}
				else {scorerate=0;}
				Forbig biginfo=new Forbig();
				biginfo.setGoodanswers(goodanswers);
				biginfo.setAnswerpics(answerpics);
				biginfo.setGoodgetscore(goodscore);
				biginfo.setStugetscore(stugetscore);
				biginfo.setScorerate(scorerate);
				biginfo.setGoodmail(goodmail);
				return biginfo;
			}
			
			//系统建议分数
			public float predictbqscore(int maxpoint,String a, String b) {
		        if (a == null && b == null) {
		            return 1f;
		        }
		        if (a == null || b == null) {
		            return 0F;
		        }
		        int editDistance = editDis(a, b);
		        float similarity= 1 - ((float) editDistance / Math.max(a.length(), b.length()));
		        return (float)maxpoint*similarity;
		    }
			
			//系统建议分数之计算编辑距离
		    private static int editDis(String a, String b) {

		        int aLen = a.length();
		        int bLen = b.length();

		        if (aLen == 0) return aLen;
		        if (bLen == 0) return bLen;

		        int[][] v = new int[aLen + 1][bLen + 1];
		        for (int i = 0; i <= aLen; ++i) {
		            for (int j = 0; j <= bLen; ++j) {
		                if (i == 0) {
		                    v[i][j] = j;
		                } else if (j == 0) {
		                    v[i][j] = i;
		                } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
		                    v[i][j] = v[i - 1][j - 1];
		                } else {
		                    v[i][j] = 1 + Math.min(v[i - 1][j - 1], Math.min(v[i][j - 1], v[i - 1][j]));
		                }
		            }
		        }
		        return v[aLen][bLen];
		    }
		  
		    //看哪些客观题是错题
		    public void getwrongbq(ArrayList<Choice> cqinfolist,ArrayList<Choiceanswered> acqinfolist,
		    		ArrayList<Jugementinfo> jqinfolist,ArrayList<Jugementans> ajqinfolist,
		    		ArrayList<Fillinquestion> fqinfolist,ArrayList<Fillinans> afqinfolist) {
		    	//删对的选择题
		    	ArrayList<Integer> deletecqlist=new ArrayList<Integer>();
		    	int j=0;
		    	for (Choiceanswered i:acqinfolist) {
		    		if (i.getScore()>0) {
		    			deletecqlist.add(j);
		    		}
		    		j=j+1;
		    	}
		    	for (int i:deletecqlist) {
		    		cqinfolist.remove(i);
		    		acqinfolist.remove(i);
		    	}
		    	//删对的判断题
		    	ArrayList<Integer> deletejqlist=new ArrayList<Integer>();
		    	j=0;
		    	for (Jugementans i:ajqinfolist) {
		    		if (i.getScore()>0) {
		    			deletejqlist.add(j);
		    		}
		    		j=j+1;
		    	}
		    	for (int i:deletejqlist) {
		    		jqinfolist.remove(i);
		    		ajqinfolist.remove(i);
		    	}
		    	//删对的填空
		    	ArrayList<Integer> deletefqlist=new ArrayList<Integer>();
		    	j=0;
		    	for (Fillinans i:afqinfolist) {
		    		if (i.getScore()>0) {
		    			deletefqlist.add(j);
		    		}
		    		j=j+1;
		    	}
		    	for (int i:deletefqlist) {
		    		fqinfolist.remove(i);
		    		afqinfolist.remove(i);
		    	}
		    }
		    
		    		    
			
}
	
