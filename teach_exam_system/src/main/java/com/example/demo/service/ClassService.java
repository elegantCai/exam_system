package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;


import com.example.demo.entity.Admin;
//entity_lever
import com.example.demo.entity.Classes;
//import com.example.demo.entity.Classandstudent;
import com.example.demo.entity.Examinfo;
//import com.example.demo.entity.Choiceanswered;
import com.example.demo.entity.Student;
import com.example.demo.entity.Teacher;
//dao_lever
import com.example.demo.dao.ClassDao;
import com.example.demo.dao.ClassandstudentDao;
import com.example.demo.dao.ExaminfoDao;
import com.example.demo.dao.ChoiceansweredDao;
import com.example.demo.dao.FillinansDao;
import com.example.demo.dao.JugementansDao;
import com.example.demo.dao.AnsweredbigqDao;
import com.example.demo.dao.ExamclassDao;
import com.example.demo.dao.StudentDao;
import com.example.demo.dao.ExamquestionDao;
import com.example.demo.dao.TeacherDao;
import com.example.demo.dao.AdminDao;
import com.example.demo.dao.AdminrecordDao;

import com.example.demo.dao.BigquestionDao;
import com.example.demo.dao.ChoiceDao;
import com.example.demo.dao.FillinquestionDao;
import com.example.demo.dao.JugementinfoDao;


@Service("ClassService")
public class ClassService {
	@Autowired
    private ClassDao classdao;
	@Autowired
	private ClassandstudentDao classandstudentdao;
	@Autowired
	private ExaminfoDao examinfodao;
	@Autowired
	private ChoiceansweredDao choiceanswereddao;
	@Autowired
	private FillinansDao fillinansdao;
	@Autowired
	private JugementansDao jugementansdao;
	@Autowired
	private AnsweredbigqDao answeredbigqdao;
	@Autowired
	private ExamclassDao examclassdao;
	@Autowired
	private StudentDao studentdao;
	@Autowired
	private ExamquestionDao examquestiondao;
	@Autowired
	private TeacherDao teacherdao;
	@Autowired
	private AdminDao admindao;
	@Autowired
	private AdminrecordDao adminrecorddao;
	
	@Autowired
	private BigquestionDao bigquestiondao;
	@Autowired
	private ChoiceDao choicedao;
	@Autowired
	private FillinquestionDao fillinquestiondao;
	@Autowired
	private JugementinfoDao jugementinfodao;
	
	public Integer getBPbyIdX(Integer id) {
		return bigquestiondao.getPbyIdX(id);
	}
	public Integer getCPbyIdX(Integer id) {
		return choicedao.getPbyIdX(id);
	}
	public Integer getFPbyIdX(Integer id) {
		return fillinquestiondao.getPbyIdX(id);
	}
	public Integer getJPbyIdX(Integer id) {
		return jugementinfodao.getPbyIdX(id);
	}
	
	
	
	
	//id数组转化为ArrayList<Integer>
	public ArrayList<Integer> strtointarray(String id) {
		char idstrArray[] = id.toCharArray(); //利用toCharArray方法转换
	    ArrayList<Integer> idintarray = new ArrayList<Integer>();
	    for (int i = 0; i < idstrArray.length; i++) {
	        int num = Character.getNumericValue(idstrArray[i]);
	        idintarray.add(num);
	    }
	    return idintarray;
	}
	
	
	//int[]转化为ArrayList<Integer>
	public ArrayList<Integer> toArray(int[] eqmail) {
		ArrayList<Integer> intarray = new ArrayList<Integer>();
		for (int i = 0; i < eqmail.length; i++) {
			int num = eqmail[i];
			intarray.add(num);
		}
	    return intarray;
	}
	
	
	
	public Classes[] transclasses(ArrayList<Classes> classes) {
		Classes [] newclasses = new Classes[classes.size()];
		for (int i = 0;i<classes.size();i++) {
			newclasses[i] = classes.get(i);
		}
		return newclasses;
	}	
	
	
	public int[] transclassid(ArrayList<Classes> classes) {
		int [] classid = new int[classes.size()];
		for (int i = 0;i<classes.size();i++) {
			classid[i] = classes.get(i).getClassid();
		}
		return classid;
	}	
	public ArrayList<Classes> transclass(int [] classid) {
		ArrayList<Classes> newclasses = new ArrayList<Classes>();
		if(classid!=null) {
			for (int i = 0;i<classid.length;i++) {
				newclasses.add(getClassbyUIdX(classid[i])); 
			}
		}
		return newclasses;
	}
	
	
	public int transString(String thetype) {
    	if (thetype!=null) {
    		return 1;
    	}
    	else {
    		return 0;
    	}
    }	
    
	public ArrayList<Teacher> getInfobyTMX(String usermail){
		return teacherdao.getInfobyMX(usermail);
		
	}
	public ArrayList<Admin> getInfobyAMX(String usermail){
		return admindao.getInfobyMX(usermail);
		
	}
	public ArrayList<Student> getInfobySMX(String usermail){
		return studentdao.getInfobyMX(usermail);	
	}
	
	
	//插入新班级
    public void insertNewclassX(String classname,String teachermail,
    		String classtype,String information,Integer state) {
    	classdao.insertNewclassX(classname,teachermail,classtype,information,state);    
    }
    
  //更新班级信息 图片
   public void updateClassPX(Integer classid ,String picture) {
	   classdao.updateClassPX(classid,picture);    
	    
   } 
 //查找最新插入的班级id
  public Integer getClassIDX(String teachermail) {
	  return classdao.getClassIDX(teachermail); 
  }
 
    
    
    //删除班级
    public void deleteClassX(Integer classid) {
    	classdao.deleteClassX(classid);    
    } 
	//更新班级信息
	public void updateClassinfoX(Integer classid,String classname,String classtype,String information,String picture) {
		classdao.updateClassinfoX(classid,classname,classtype,information,picture);
	}
	
	//查找所有的班级
	public ArrayList<Classes> getClassX(){
			return classdao.getClassX();    
	}
	
	//查找特定班级ID的班级
	public ArrayList<Classes> getClassbyIdX(Integer classid){
		return classdao.getClassbyIdX(classid);    
	}
	//查找特定班级ID的班级
	 public Classes getClassbyUIdX(Integer classid) {
		 return classdao.getClassbyUIdX(classid);    
	 }
		
	 //查找特定班级ID的班级,学生，需要已审核通过的
	 public Classes getClassbySIdX(Integer classid) {
		 return classdao.getClassbyUIdX(classid);    
	 }
		
	 
	 
	//查找特定班级名称的班级
	public ArrayList<Classes> getClassbyclassnameX(String classname){
		return classdao.getClassbyclassnameX(classname);
	}
		    
	//查找特定班级类型的班级
	public ArrayList<Classes> getClassbyclasstypeX(String classtype){
		return classdao.getClassbyclasstypeX(classtype);
	}
		
	//查找特定班级状态的班级
	public ArrayList<Classes> getClassbystateX(Integer state){
		return classdao.getClassbystateX(state);
	}
		
	//查找特定信息的班级
	public ArrayList<Classes> getClassbyAllX(Integer classid,String classname,String classtype,Integer state,String teachermail){
		return classdao.getClassbyAllX(classid,classname,classtype,state,teachermail);
			
	}	
	
	//查找特定老师ID的班级，按班级id升序排列，也可称为班级创建时间
	public ArrayList<Classes> ClassidByTeacheridX(String teachermail){
		return classdao.ClassidByTeacheridX(teachermail); 
	}
	//查找特定老师ID的班级，按班级类型升序排列
	public ArrayList<Classes> ClasstypeByTeacheridX(String teachermail){
		return classdao.ClasstypeByTeacheridX(teachermail); 
	}
	//查找特定老师ID的班级，按班级状态降序排列
	public ArrayList<Classes> ClassstateByTeacheridX(String teachermail){
		return classdao.ClassstateByTeacheridX(teachermail); 
	}
	//修改状态
    public void updateClassstateX(Integer classid,Integer state) {
    	classdao.updateClassstateX(classid,state); 
    }    
    
    		

	
	
	
	
	
	
	
	//插入新学生
    public void insertNewstudentX(Integer classid,Integer studentid){
    	classandstudentdao.insertNewstudentX(classid,studentid);
    }
    //删除学生
    public void deleteStudentX(Integer classid,Integer studentid){
    	classandstudentdao.deleteStudentX(classid,studentid);
    }
    //查找特定班级ID的学生人数
	public int getClassnumbyIdX(Integer classid){
		return classandstudentdao.getClassnumbyIdX(classid);
	}
	//查找特定班级ID的学生ID
	public ArrayList<Integer> getStudentidByClassidX(Integer classid){
		return classandstudentdao.getStudentidByClassidX(classid);
	}
	//查找特定学生ID的所有班级
	public ArrayList<Integer> getClassidByStudentidX(Integer studentid){
		return classandstudentdao.getStudentidByClassidX(studentid);
	}
	//查找特定学生邮箱的所有班级
	public ArrayList<Integer> getClassidByStudentmailX(String mail){
		Integer studentid =studentdao.getIDbyMX(mail);
		return classandstudentdao.getClassidByStudentidX(studentid);
	}
	
	//删除班级
    public void deleteClassSX(Integer classid) {
    	classandstudentdao.deleteClassX(classid);
    }
    
    //查找学生是否已经在某个班级中
    public Integer getidX(Integer classid, Integer studentid) {
    	return classandstudentdao.getidX(classid, studentid);
    }

    
    
    
    
    
    
	//插入新学生
	public void insertStudentX(String usermail) {
		studentdao.insertStudentX(usermail);
	}
    //查找特定ID的学生邮箱
	public String getMailbyIdX(int userid) {
		return studentdao.getMailbyIdX(userid);
	}
	//查找特定邮箱的学生ID
	@Query(value = "select userid from studentinfo where usermail=?1",nativeQuery=true)
	public Integer getIDbyMX(String mail) {
		return studentdao.getIDbyMX(mail);
	}

	//查找特定ID的学生信息
	public Student getInfobyIdX(int userid) {
		return studentdao.getInfobyIdX(userid);
	}

	
	
	//插入考试信息
    public void insertNewexamX(String email,Calendar starttime,Calendar endtime,
    		Integer type,String subject,Integer state){
    	examinfodao.insertNewexamX(email,starttime,endtime,type,subject,state);
    }
    
    //删除考试信息
    public void deleteExamX(Integer id) {
    	examinfodao.deleteExamX(id);
    } 
	//查找特定ID的考试信息
	public Examinfo getExaminfoByIdX(Integer id){
		return examinfodao.getExaminfoByIdX(id);
	}
	//查找特定老师邮箱的考试信息
	public ArrayList<Examinfo> getExaminfoByTeachermailX(String email){
		return examinfodao.getExaminfoByTeachermailX(email);
	}
	//查找特定ID的选择题题目信息
	public String getChoiceByIdX(Integer id) {
		return examinfodao.getChoiceByIdX(id);
	}
	//查找特定ID的填空题题目信息
	public String getFillByIdX(Integer id) {
		return examinfodao.getFillByIdX(id);
	}	
	//查找特定ID的判断题题目信息
	public String getJudgeByIdX(Integer id) {
		return examinfodao.getJudgeByIdX(id);
	}
	//查找特定ID的大题题目信息
	public String getBigByIdX(Integer id) {
		return examinfodao.getBigByIdX(id);
	}
	//查找最新插入的考试ID
	public Integer getNewByEmailX(String email) {
		return examinfodao.getNewByEmailX(email);
	}

	//插入考试-班级对信息
   public void insertNewexamclassX(Integer examid,Integer classid) {
	   examclassdao.insertNewexamclassX(examid,classid);
   }
    //查找特定考试的所有班级
	public ArrayList<Integer> getClassidByIdX(Integer examid){
		return examclassdao.getClassidByIdX(examid);
	}	
	//查找特定班级的所有考试
	public ArrayList<Integer> getExamidByIdX(Integer classid){
		return examclassdao.getExamidByIdX(classid);
	}
	
	//插入考试-题目对信息
   public void insertNewexamQX(Integer examid,Integer qid,Integer qtype) {
	   examquestiondao.insertNewexamQX(examid,qid,qtype);
   }
   
    //查找特定考试的特定类型的题目0：选择题1：判断题2：填空题3：简答题
	public ArrayList<Integer> getQidByIdX(Integer examid,Integer qtype){
		return examquestiondao.getQidByIdX(examid,qtype);
	}

	
	
	
	//插入选择题答题信息
	public void insertNewansweredX(int qid,int type,int examid,int examsubid,String answermail,String answer,float score,String comment) {
		choiceanswereddao.insertNewansweredX(qid,type,examid,examsubid,answermail,answer,score,comment);
	};
	//获取选择题学生答案
	public String getAnsweredX(int examid,int examsubid,String answermail) {
		return choiceanswereddao.getAnsweredX(examid,examsubid,answermail);
	}
    //修改选择题学生得分
	public void updateScoreX(float score,int examid,int examsubid,String answermail) {
		choiceanswereddao.updateScoreX(score,examid,examsubid,answermail);
	}	
	//获取选择题学生得分
    public Float getScoreX(int examid,int examsubid,String answermail) {
    	return choiceanswereddao.getScoreX(examid,examsubid,answermail);
    }
    //获取选择题学生得分
    public Float getScoreQX(int examid,int qid,String answermail) {
    	return choiceanswereddao.getScoreQX(examid,qid,answermail);
    }

    
	//插入填空题答题信息
	public void insertFNewansweredX(int qid,int type,int examid,int examsubid,String answermail,String answer,float score,String comment) {
		fillinansdao.insertNewansweredX(qid,type,examid,examsubid,answermail,answer,score,comment);
	}    
   //获取填空题学生答案
    public String getFAnsweredX(int examid,int examsubid,String answermail) {
    	return fillinansdao.getAnsweredX(examid,examsubid,answermail);
    }
    //修改填空题学生得分
    public void updateFScoreX(float score,int examid,int examsubid,String answermail) {
    	fillinansdao.updateScoreX(score,examid,examsubid,answermail);
    }
    //获取填空题学生得分
    public Float getFScoreX(int examid,int examsubid,String answermail) {
    	return fillinansdao.getScoreX(examid,examsubid,answermail);   
    }
    //获取填空题学生得分
    public Float getFScoreQX(int examid,int qid,String answermail) {
	   return fillinansdao.getScoreQX(examid,qid,answermail);   
    }

    
    
    //插入判断题答题信息
    public void insertJNewansweredX(int qid,int type,int examid,int examsubid,String answermail,String answer,float score,String comment) {
    	jugementansdao.insertNewansweredX(qid,type,examid,examsubid,answermail,answer,score,comment);
    }    
    //获取判断题学生答案
    public String getJAnsweredX(int examid,int examsubid,String answermail) {
    	return jugementansdao.getAnsweredX(examid,examsubid,answermail);
    }
    //修改判断题学生得分
    public void updateJScoreX(float score,int examid,int examsubid,String answermail) {
    	jugementansdao.updateScoreX(score,examid,examsubid,answermail);
    }
  //获取判断题学生得分
   public Float getJScoreX(int examid,int examsubid,String answermail) {
	   return jugementansdao.getScoreX(examid,examsubid,answermail);
   }
   //获取判断题学生得分
   public Float getJScoreQX(int examid,int qid,String answermail) {
	   return jugementansdao.getScoreQX(examid,qid,answermail);
   }

    
    //插入大题答题信息
    public void insertBNewansweredX(int qid,int type,int examid,int examsubid,String answermail,String answer,
			float score,String comment,String answerpic) {
    	answeredbigqdao.insertNewansweredX(qid,type,examid,examsubid,answermail,answer,score,comment,answerpic);
    }   
   	//获取大题学生文字答案
    public String getBAnsweredWX(int examid,int examsubid,String answermail) {
    	return answeredbigqdao.getAnsweredWX(examid,examsubid,answermail);
    }
    //获取大题学生图片答案
    public String getBAnsweredPX(int examid,int examsubid,String answermail) {
    	return answeredbigqdao.getAnsweredPX(examid,examsubid,answermail);
    }
    //修改大题学生得分
    public void updateBScoreX(float score,int examid,int examsubid,String answermail,String correctmail) {
    	answeredbigqdao.updateScoreX(score,examid,examsubid,answermail,correctmail);
    }
    //获取大题学生得分
    public Float getBScoreX(int examid,int examsubid,String answermail) {
	   return answeredbigqdao.getScoreX(examid,examsubid,answermail); 
    }
    //获取大题学生得分
    public Float getBScoreQX(int examid,int qid,String answermail) {
    	return answeredbigqdao.getScoreQX(examid,qid,answermail); 
    }
    
    
    
    public class ScoreList{  
    	private Integer studentid ;  
    	private String studentmail ; 
    	private Integer examid ;
    	private Float  sumscore ; 
    	private ArrayList<ArrayList<Float>> subscore ;
    	  
    	public Integer getStudentid() {  
    	   return studentid;  
    	}  
    	public void setStudentid(Integer studentid) {  
    	   this.studentid = studentid;  
    	}  
    	
    	public String getStudentmail() {  
    	   return studentmail;  
    	}  
    	public void setStudentmail(String studentmail) {  
    	   this.studentmail = studentmail;  
    	}  
    	
    	public Integer getExamid() {  
     	   return examid;  
     	}  
     	public void setExamid(Integer examid) {  
     	   this.examid = examid;  
     	} 
     	
    	public Float getSumscore() {  
    	   return sumscore;  
    	}  
    	public void setSumscore(Float sumscore) {  
    	   this.sumscore = sumscore;  
    	}  
    	
    	public ArrayList<ArrayList<Float>> getSubscore() {  
     	   return subscore;  
     	}  
     	public void setSubscore(ArrayList<ArrayList<Float>> subscore) {  
     	   this.subscore =subscore;  
     	}
    	  
    	public ScoreList(Integer studentid,String studentmail,Integer examid,ArrayList<ArrayList<Float>> subscore){  
    	   this.studentid = studentid ;  
    	   this.studentmail = studentmail ;  
    	   this.examid = examid; 
    	   this.subscore = subscore; 
    	   Float scorei =(float) 0;
    	   //[[1，2，3], [5，5，5], [8，9], [20]]
    	   for(Integer i=0;i<subscore.size();i++) {
    		   for(Integer j=0;j<subscore.get(i).size();j++) {
    			   if(subscore.get(i).get(j)!=null) {
    				   scorei= subscore.get(i).get(j)+ scorei;
    			   }
    		   }
    	   }
    	   this.sumscore = scorei; 
    	}   
    }  
    //插入新审核记录
  	//treattype：   0：审核考试  1：审核选择题 2：审核填空题 3：审核判断题 4：审核大题 5：审核班级
  	//treatresult：    0：审核通过  1：审核不通过  2：审核结课通过
    public void insertNewrecordC(String usermail,int treattype,int treatid,String comment,int treatresult) {
    	adminrecorddao.insertNewrecordC(usermail, treattype, treatid, comment, treatresult);
    }
    
    
    //获取某个班的某场考试中所有学生的题目成绩[[学生1的题目score],[学生2的题目score]]
    public ArrayList<ScoreList> ScoreArrayinClass(Integer classid,Integer examid) {
    	ArrayList<ScoreList> result= new ArrayList<ScoreList>();
    	//获取这个班的所有学生的学生邮箱ArrayList<String> studentmail
    	System.out.println("--------进入循环11-------");
    	ArrayList<Integer> studentidarray=getStudentidByClassidX(classid);
    	ArrayList<String> studentmail= new ArrayList<String>();
    	for(Integer i=0; i < studentidarray.size(); i++) {
    		String mail = getMailbyIdX(studentidarray.get(i));
    		studentmail.add(mail);
    	}
    	System.out.println("--------进入循环22-------");
    	//ArrayList<String> studentmail邮箱
    	//ArrayList<Integer> studentidarray学号
    	//题目id
    	//查找特定考试的特定类型的题目0：选择题1：判断题2：填空题3：简答题
   			
    	ArrayList<Integer> choice= getQidByIdX(examid,0);
    	ArrayList<Integer> judge= getQidByIdX(examid,1);
    	ArrayList<Integer> fill= getQidByIdX(examid,2);
    	ArrayList<Integer> big= getQidByIdX(examid,3);  
    	System.out.println(studentmail.size());
    	if(studentmail.size()==0) {
    		System.out.println("--------这个班级没有学生-------");
    		return result;
    	}
    	else {
    		//学生i
    		for(Integer i=0; i < studentmail.size(); i++) {  
    			System.out.println("--------进入循环33-------");
    			//这个学生的成绩表，[[5,5,5],[4,4,4],[0,9,3],[10,40]]
    			System.out.println(studentmail.get(i));
    			
    			ArrayList<ArrayList<Float>> scoretemp= new ArrayList<ArrayList<Float>>();
    			//选择题i---[5,5,5]
    			ArrayList<Float> scoretemp0= new ArrayList<Float>();
    			for(Integer j=0; j < choice.size(); j++) {
    				System.out.println("--------进入循环选择题-------");
    				scoretemp0.add(getScoreQX(examid,choice.get(j),studentmail.get(i)));
    			}
    			//判断题i---[4,4,4]
    			ArrayList<Float> scoretemp1= new ArrayList<Float>();
    			for(Integer j=0; j < judge.size(); j++) {
    				System.out.println("--------进入循环判断题-------");
    			
    				scoretemp1.add(getJScoreQX(examid,judge.get(j),studentmail.get(i)));
    			}
    			//填空题i---[0,9,3]
    			ArrayList<Float> scoretemp2= new ArrayList<Float>();
    			for(Integer j=0; j < fill.size(); j++) {
    				System.out.println("--------进入循环填空题-------");
    				
    				scoretemp2.add(getFScoreQX(examid,fill.get(j),studentmail.get(i)));
    			}
    			//选择题i---[10,40]
    			ArrayList<Float> scoretemp3= new ArrayList<Float>();
    			for(Integer j=0; j < big.size(); j++) {
    				System.out.println("--------进入循环大题-------");
    			
    				scoretemp3.add(getBScoreQX(examid,big.get(j),studentmail.get(i)));
    			}
    			System.out.println("--------结束循环计算-------");
    			scoretemp.add(scoretemp0);
    			scoretemp.add(scoretemp1);
    			scoretemp.add(scoretemp2);
    			scoretemp.add(scoretemp3);
    			System.out.println("--------将学生各类题型的得分list加入学生列表-------");
    			//ArrayList<ArrayList<Float>> scoretemp即subscore
    			//studentmail.get(i)学生邮箱
    			//studentidarray.get(i)学生学号
    			//Integer examid考试ID
    			//总分不用算，直接进入class就可以算出来
    			System.out.println("--------学生邮箱-------");
    			System.out.println(studentmail.get(i));
    			System.out.println("--------学生id-------");
    			System.out.println(studentidarray.get(i));
    			System.out.println(scoretemp);
    			ScoreList scorei = new ScoreList(studentidarray.get(i),studentmail.get(i),examid,scoretemp);  
    			System.out.println("--------将学生成绩加入结果-------");
    			result.add(scorei);
    		}
    		//按总分排个序
    		for (int i = 0; i < result.size(); i++) { 
    			for (int j = i+1; j < result.size(); j++) { 
    				if (result.get(i).getSumscore()< result.get(j).getSumscore()) { 
    					ScoreList scoretemp = result.get(i);
    					result.set(i, result.get(j));
    					result.set(j, scoretemp);
    				} 
    			} 
    		}
    		return result;
    	}
	}

    public ArrayList<Integer> ScoreLever(ArrayList<Float> score){
		ArrayList<Integer> lever = new ArrayList<Integer>();
    	Integer c1=0;Integer c2=0;Integer c3=0;Integer c4=0;Integer c5=0;Integer c6=0;
		for(int i=0;i<score.size();i++) {
			if(score.get(i)>=90) {
				c1=c1+1;
			}
			else if(score.get(i)>=80&&score.get(i)<90){
				c2=c2+1;
			}
			else if(score.get(i)>=70&&score.get(i)<80){
				c3=c3+1;
			}
			else if(score.get(i)>=60&&score.get(i)<70){
				c4=c4+1;
			}
			else if(score.get(i)>=50&&score.get(i)<60){
				c5=c5+1;
			}
			else if(score.get(i)<50){
				c6=c6+1;
			}
		}
		lever.add(c1);
		lever.add(c2);
		lever.add(c3);
		lever.add(c4);
		lever.add(c5);
		lever.add(c6);
		return lever;
    }
    
    public float scorerank(ArrayList<Float> scoreall, Float yourscore){
		int yourrank=scoreall.size()+1;
		for(int i=0;i<scoreall.size();i++) {
			if(yourscore<scoreall.get(i)) {
				yourrank--;
			}
		}
		float yourrank1= yourrank/(scoreall.size()+1)*100;
		return yourrank1; 
    }
    
	
}
