package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Classes;
import com.example.demo.entity.Examinfo;
import com.example.demo.entity.Student;
import com.example.demo.entity.Teacher;


import com.example.demo.entity.Admin;
import com.example.demo.service.ClassService;
import com.example.demo.service.ClassService.ScoreList;
import com.example.demo.service.UploadfileService;


@Controller
public class ClassController {
	@Autowired
	private ClassService classservice;
	@Autowired
	private UploadfileService uploadfileService;
	
	//登录判断用户类型进入不同的班级界面
	@RequestMapping(value = { "/class" })
	public String startC(HttpServletRequest request,Model model) {
		
		//邮箱+类型
		String usermail = (String)request.getSession().getAttribute("usermail");
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		model.addAttribute("usermail", usermail);
		//设置班级界面显示内容，0：默认显示相关班级，1：显示搜索结果的班级
		request.getSession().setAttribute("searchtype",0);
		model.addAttribute("searchtype", 0);
		if(usertype==0){
			//个人信息
			ArrayList<Teacher> teacherinfo = classservice.getInfobyTMX(usermail);
			model.addAttribute("teacherinfo", teacherinfo);
			//班级信息
			ArrayList<Classes> classinfo = classservice.ClassidByTeacheridX(usermail);
			model.addAttribute("classinfo", classinfo);
			return "class";
		}
		else if(usertype==1) {
			//个人信息
			ArrayList<Student> studentinfo = classservice.getInfobySMX(usermail);
			model.addAttribute("studentinfo", studentinfo);
			//班级信息
			ArrayList<Integer> classid =classservice.getClassidByStudentmailX(usermail);
			ArrayList<Classes> classinfo =new ArrayList<Classes>();
			for(Integer i=0; i < classid.size(); i++) {
				classinfo.add(classservice.getClassbyUIdX(classid.get(i)));
	    	}
			model.addAttribute("classinfo", classinfo);
			return "class";
		}
		else{
			//个人信息
			ArrayList<Admin> admininfo = classservice.getInfobyAMX(usermail);
			model.addAttribute("admininfo", admininfo);
			//班级信息
			ArrayList<Classes> classinfo = classservice.getClassX();
			model.addAttribute("classinfo", classinfo);
			return "class";
		}		
	}
	
	//进入添加考试页面
	@RequestMapping(value = { "/Examinfo" })
	public String startE(HttpServletRequest request,Model model) {
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		return "Examinfo";
	}
	
	//老师插入班级
	@RequestMapping(value = { "/upclass" })
	public String creatClass(HttpServletRequest request,Model model,
					@RequestParam("picture")MultipartFile picture) {
		//session 取老师的类型，个人信息，班级信息
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		String usermail = (String)request.getSession().getAttribute("usermail");
		ArrayList<Teacher> teacherinfo = classservice.getInfobyTMX(usermail);
		model.addAttribute("teacherinfo", teacherinfo);
		//插入班级需要将班级信息后移
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		//session 取班级信息展示内容为0默认或者1搜索的
		int searchtype = (int)request.getSession().getAttribute("searchtype");
		
		System.out.println(searchtype);
		
		model.addAttribute("searchtype", searchtype);
		int[] classinfo11 = (int [])request.getSession().getAttribute("classinfo1");
		ArrayList<Classes> classinfo1 =classservice.transclass(classinfo11);			
		model.addAttribute("classinfo1", classinfo1);
		int[] eqmail = (int [])request.getSession().getAttribute("eqmail");
		model.addAttribute("eqmail", eqmail);
		//前端获取到的老师创建班级的信息输入
		String classname = request.getParameter("classname");
		String classtype = request.getParameter("classtype");
		String information = request.getParameter("information");
		Integer state = 0;

		//先插入非图片的信息
		System.out.println("先插入非图片的信息");
		if((classname!=null && classname.trim().length() > 0)&& 
				(usermail!=null&& usermail.trim().length() > 0)&& 
				(classtype!=null && classtype.trim().length() > 0)) {
			classservice.insertNewclassX(classname,usermail,classtype,information,state); 
		}
		//获取班级id后插入图片信息
		Integer classid=classservice.getClassIDX(usermail);
		//存班级头像
		String picpath=uploadfileService.getfilepath("class","classheadshot",picture,classid);
		uploadfileService.uploadfile(picpath, picture);
		//插入图片的相对路径到数据库   images/class/classheadshot/1.png				
		String relpicpath=uploadfileService.getrelpath("class", "classheadshot",picpath);
		classservice.updateClassPX(classid,relpicpath);
		//搜索相关班级信息，包括新插入的班级
		ArrayList<Classes> classinfo = classservice.ClassidByTeacheridX(usermail);
		model.addAttribute("classinfo", classinfo);	
		model.addAttribute("usermail", usermail);
		return "class";
	}
	
	//老师，管理员结课课程的状态修改
	@RequestMapping(value = { "/endclass" })
	public String endClass(HttpServletRequest request,Model model) {	
		//session 里取用户类型+用户邮箱
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		String usermail = (String)request.getSession().getAttribute("usermail");
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		//session 取班级信息展示内容为0默认或者1搜索的
		int searchtype = Integer.parseInt(request.getParameter("searchtype"));
		model.addAttribute("searchtype", searchtype);
		int[] classinfo11 = (int [])request.getSession().getAttribute("classinfo1");
		//搜索的班级信息获取后移，因为要等修改后再获取才有效
		int[] eqmail = (int [])request.getSession().getAttribute("eqmail");
		model.addAttribute("eqmail", eqmail);
		//前端获取的要结课的班级id
		String classid1 = request.getParameter("classid");
		Integer classid = Integer.parseInt(classid1);
			
		//0：未审核，1：审核未通过，2：审核通过进行中，3：结课申请审核中，4：结课
		//老师
		if(usertype==0) {
			classservice.updateClassstateX(classid,3);
			//个人信息
			ArrayList<Teacher> teacherinfo = classservice.getInfobyTMX(usermail);
			model.addAttribute("teacherinfo", teacherinfo);
			//默认班级信息
			ArrayList<Classes> classinfo = classservice.ClassidByTeacheridX(usermail);
			model.addAttribute("classinfo", classinfo);
			ArrayList<Classes> classinfo1 =classservice.transclass(classinfo11);			
			model.addAttribute("classinfo1", classinfo1);
			model.addAttribute("usermail", usermail);
			return "class";
		}
		//管理员
		else{
			classservice.updateClassstateX(classid,4);	
			//插入新审核记录
		  	//treattype：   0：审核考试  1：审核选择题 2：审核填空题 3：审核判断题 4：审核大题 5：审核班级
		  	//treatresult：    0：审核通过  1：审核不通过  2：审核结课通过
			classservice.insertNewrecordC(usermail,5,classid,"审核结课通过",2);
			//个人信息
			ArrayList<Admin> admininfo = classservice.getInfobyAMX(usermail);
			model.addAttribute("admininfo", admininfo);				
			//默认班级信息
			ArrayList<Classes> classinfo = classservice.getClassX();
			model.addAttribute("classinfo", classinfo);
			ArrayList<Classes> classinfo1 =classservice.transclass(classinfo11);			
			model.addAttribute("classinfo1", classinfo1);
			model.addAttribute("usermail", usermail);
			return "class";
		}
			
	}
		
	//创建考试
	//发布考试
	@RequestMapping(value = { "/upExaminfo" })
	public String createExam(HttpServletRequest request,Model model) throws ParseException {

		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		
		//直接使用模块
		String email = request.getParameter("email");
		String subject = request.getParameter("subject");
		//审核状态为未审核
		Integer state = 0;
		//时间处理模块
		String starttime1 =request.getParameter("starttime");
		String endtime1 =request.getParameter("endtime");
		Calendar starttime =Calendar.getInstance();
		starttime.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(starttime1));
		Calendar endtime =Calendar.getInstance();
		starttime.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endtime1));
		
		//String-integer模块
		String type1 = request.getParameter("type");
		Integer type = Integer.parseInt(type1);
		
		//1,2,3转为[1,2,3]模块，包括classid,judgmentques,choiceques,fillinques,bigques
		String classid1 = request.getParameter("classid");
		String classid2[]=classid1.split(",");
		int[] classid = Arrays.asList(classid2).stream().mapToInt(Integer::parseInt).toArray();		
		String judgmentques1 = request.getParameter("judgmentques");
		String judgmentques2[]=judgmentques1.split(",");
		int[] judgmentques = Arrays.asList(judgmentques2).stream().mapToInt(Integer::parseInt).toArray();
		String choiceques1 = request.getParameter("choiceques");
		String choiceques2[]=choiceques1.split(",");
		int[] choiceques = Arrays.asList(choiceques2).stream().mapToInt(Integer::parseInt).toArray();
		String fillinques1 = request.getParameter("fillinques");
		String fillinques2[]=fillinques1.split(",");
		int[] fillinques = Arrays.asList(fillinques2).stream().mapToInt(Integer::parseInt).toArray();
		String bigques1 = request.getParameter("bigques");
		String bigques2[]=bigques1.split(",");
		int[] bigques = Arrays.asList(bigques2).stream().mapToInt(Integer::parseInt).toArray();
	
		//存session模块
		request.getSession().setAttribute("usermail", email);	
		request.getSession().setAttribute("usertype", 0);
		
		//检查错误模块
		System.out.println(email);
		System.out.println(starttime.getTime());
		System.out.println(endtime.getTime());	
		System.out.println(type);
		System.out.println(subject);	
		System.out.println(classid);
		System.out.println(judgmentques);
		System.out.println(choiceques);
		System.out.println(fillinques);
		System.out.println(bigques);
		
		//判断输入正确与否，并插入考试数据，包括examinfo，examclass，examquestion
		if((email!=null&& email.trim().length() > 0)&& (starttime1!=null&& starttime1.trim().length() > 0)&& 
			(endtime1!=null&& endtime1.trim().length() > 0)&& (type1!=null&& type1.trim().length() > 0)&& 
			(subject!=null&& subject.trim().length() > 0)&&(judgmentques1!=null&& judgmentques1.trim().length() > 0)&& 
			(choiceques1!=null&& choiceques1.trim().length() > 0)&&(fillinques1!=null&& fillinques1.trim().length() > 0)&& 
			(bigques1!=null&& bigques1.trim().length() > 0)) {
			
			classservice.insertNewexamX(email,starttime,endtime,type,subject,state);
			//获取插入的考试的id，根据id插入班级和题目数据
			Integer examid = classservice.getNewByEmailX(email);
			for(Integer i=0; i < classid.length; i++) {
    			classservice.insertNewexamclassX(examid,classid[i]); 
			}
			for(Integer i=0; i < judgmentques.length; i++) {
    			classservice.insertNewexamQX(examid,judgmentques[i],1); 
			}
			for(Integer i=0; i < choiceques.length; i++) {
    			classservice.insertNewexamQX(examid,choiceques[i],0); 
			}
			for(Integer i=0; i < fillinques.length; i++) {
    			classservice.insertNewexamQX(examid,fillinques[i],2); 
			}
			for(Integer i=0; i < bigques.length; i++) {
    			classservice.insertNewexamQX(examid,bigques[i],3); 
			}
			
		}
		//给前端传值模块，输出已发布的考试列表
		String usermail = (String)request.getSession().getAttribute("usermail");
		ArrayList<Examinfo> Examinfo = classservice.getExaminfoByTeachermailX(usermail);
		model.addAttribute("Examinfo", Examinfo);
		return "Examinfo";
	}

	//查看班级所有考试和学生的邮箱
	
	//老师，学生，管理员查看班级详情
	@RequestMapping(value = { "/checkclass" })
	public String checkClass(HttpServletRequest request,Model model) throws ParseException {
		//session 里取用户类型+用户邮箱
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		String usermail = (String)request.getSession().getAttribute("usermail");
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		//----------------信息处理模块------------------------------------------------------------
		//从前端获取需要查看的班级ID
		String classid1 = request.getParameter("classid");
		Integer classid = Integer.parseInt(classid1);
		//查找特定班级ID下的所有考试
		ArrayList<Integer> examidarray= classservice.getExamidByIdX(classid);
		//查找特定考试ID的所有考试信息
		ArrayList<Examinfo> examinfoarray =new ArrayList<Examinfo>();
		for(Integer i=0; i < examidarray.size(); i++) {
			Examinfo examinfotemp = classservice.getExaminfoByIdX(examidarray.get(i));	
			examinfoarray.add(examinfotemp);
    	}			
		//查找特定班级ID的学生ID
		ArrayList<Student> Studentinfoarray =new ArrayList<Student>();
		ArrayList<Integer> studentidarray = classservice.getStudentidByClassidX(classid);
		for(Integer i=0; i < studentidarray.size(); i++) {
			Student Stuinfotemp = classservice.getInfobyIdX(studentidarray.get(i));	
			Studentinfoarray.add(Stuinfotemp);
    	}		
		//查找特定班级ID的班级信息
		ArrayList<Classes> classinfo =classservice.getClassbyIdX(classid);    
		//----------------信息处理模块------------------------------------------------------------
		
		//给前端传值模块，输出所有名下的考试列表
		model.addAttribute("Examinfo", examinfoarray);
		//给前端传值模块，输出所有名下的学生
		model.addAttribute("Studentinfo", Studentinfoarray);
		//给前端传值模块，输出一些班级信息
		model.addAttribute("Classinfo", classinfo);
		
		if(usertype==0){
			ArrayList<Teacher> teacherinfo = classservice.getInfobyTMX(usermail);
			model.addAttribute("teacherinfo", teacherinfo);			
		}
		else if(usertype==1) {
			ArrayList<Student> studentinfo = classservice.getInfobySMX(usermail);
			model.addAttribute("studentinfo", studentinfo);			
		}
		else{
			ArrayList<Admin> admininfo = classservice.getInfobyAMX(usermail);
			model.addAttribute("admininfo", admininfo);
		}				
		model.addAttribute("usermail", usermail);
		return "checkclass";
	}

	//老师，学生，管理员从班级详情界面进入查看班级考试得分情况
	@RequestMapping(value = { "/checkscore" })
	public String checkscore(HttpServletRequest request,Model model) throws ParseException {
		//session 里取用户类型+用户邮箱
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		String usermail = (String)request.getSession().getAttribute("usermail");
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		Integer classid = Integer.parseInt(request.getParameter("classid"));
		//从前端获取要查看的考试的ID
		String examid1 = request.getParameter("examid");
		Integer examid = Integer.parseInt(examid1);
		
		//根据考试id获取四个类型题的总分
		//查找特定考试的特定类型的题目0：选择题1：判断题2：填空题3：简答题
		 ArrayList<Integer> cqid= classservice.getQidByIdX(examid,0);
		 ArrayList<Integer> jqid= classservice.getQidByIdX(examid,1);
		 ArrayList<Integer> fqid= classservice.getQidByIdX(examid,2);
		 ArrayList<Integer> bqid= classservice.getQidByIdX(examid,3);
		 int max0=0;int max1=0;int max2=0;int max3=0;
		 for(int i=0;i<cqid.size();i++) {
			 max0=max0+classservice.getCPbyIdX(cqid.get(i));
		 }
		 for(int i=0;i<jqid.size();i++) {
			 max1=max1+classservice.getJPbyIdX(jqid.get(i));
		 }
		 for(int i=0;i<fqid.size();i++) {
			 max2=max2+classservice.getFPbyIdX(fqid.get(i));
		 }
		 for(int i=0;i<bqid.size();i++) {
			 max3=max3+classservice.getBPbyIdX(bqid.get(i));
		 }
		 ArrayList<Integer> maxscore=new  ArrayList<Integer>();
		 maxscore.add(max0);
		 maxscore.add(max1);
		 maxscore.add(max2);
		 maxscore.add(max3);
		 maxscore.add(max0+max1+max2+max3);
		 //----------------信息处理模块------------------------------------------------------------
		//查找特定考试ID名下的所有班级
		ArrayList<Integer> allclassid= classservice.getClassidByIdX(examid);
		//获取所有班的某场考试中所有学生的题目成绩[[学生1的题目score],[学生2的题目score]]
		ArrayList<ArrayList<ScoreList>> result= new ArrayList<ArrayList<ScoreList>>();
		for(Integer i=0; i < allclassid.size(); i++) {
			//获取某个班的某场考试中所有学生的题目成绩[[学生1的题目score],[学生2的题目score]]
			ArrayList<ScoreList> scorelisti=classservice.ScoreArrayinClass(allclassid.get(i),examid);
			result.add(scorelisti);
		}
		//管理员+老师+学生，根据usertype来区分是否显示按钮或者学生姓名
		ArrayList<Float> allclassavg= new ArrayList<Float>();	
		ArrayList<Float> allclassmax= new ArrayList<Float>();
		//班级
		System.out.println("--------这个班级没有学生-------");
		System.out.println(result.size());
		for(Integer i=0; i < result.size(); i++) {	
			System.out.println(result.get(i).size());
			System.out.println("--------这个班级没有学生-------");
			if(result.get(i).size()!=0){
				//班级中的学生
				Float sumscorej=(float)0;//总分
				for(Integer j=0; j < result.get(i).size(); j++){				
					sumscorej = sumscorej+result.get(i).get(j).getSumscore();				
				}
				Float maxscorei = result.get(i).get(0).getSumscore();//最高分						
				sumscorej=sumscorej/result.get(i).size();//均分	
				allclassmax.add(maxscorei);
				allclassavg.add(sumscorej);
			}				
		}	
		//----------------信息处理模块------------------------------------------------------------
		//按班级平均分排序
		for(int i=0;i<allclassavg.size();i++) {
			for(int j =i+1;j<allclassavg.size();j++) {
				if(allclassavg.get(i)>allclassavg.get(j)) {
					Float temp1= allclassavg.get(i);
					Float temp2= allclassmax.get(i);
					Integer temp3 = allclassid.get(i);
					ArrayList<ScoreList> temp4= result.get(i);					
					allclassavg.set(i, allclassavg.get(j));
					allclassmax.set(i, allclassmax.get(j));
					allclassid.set(i, allclassid.get(j));
					result.set(i,result.get(j));					
					allclassavg.set(j,temp1 );
					allclassmax.set(j, temp2);
					allclassid.set(j, temp3);
					result.set(j,temp4);
				}
			}
		}
		//全部班级的全部学生的分数
		ArrayList<Float> score=new ArrayList<Float>();
		int count = 0;
		for(int i = 0; i < result.size(); i++){
			for(int j = 0; j < result.get(i).size(); j++){		
				System.out.println(result.get(i).get(j).getSumscore());
				score.add(result.get(i).get(j).getSumscore());
				System.out.println(score.get(count));
				count++;
			}			
		}
		System.out.println(score);
		//获取班级id 的索引
		int classidindex=0;
		for(int i=0;i<allclassid.size();i++) {
			if(allclassid.get(i)==classid) {
				classidindex=i;
			}
		}
		//指定（当前）班级的全部学生的分数
		ArrayList<Float> score1=new ArrayList<Float>();
		for(int j = 0; j < result.get(classidindex).size(); j++){
			System.out.println(result.get(classidindex).get(j).getSumscore());
			score1.add(result.get(classidindex).get(j).getSumscore());
			System.out.println(score1.get(j));		
		}
		System.out.println(score1);	
		////每个分数段的人数，全部班级+指定（当前）班级
		ArrayList<Integer> scorelever = classservice.ScoreLever(score);
		ArrayList<Integer> scorelever1 = classservice.ScoreLever(score1);
		//[选择题得分，判断题得分，填空题得分，大题得分,总成绩,班级排名，总排名100个人中超过多少人]
		ArrayList<Float> analyticyou = new ArrayList<Float>();
		//[选择题得分，判断题得分，填空题得分，大题得分,总成绩,上面的一半，50]
		ArrayList<Float> analyticall = new ArrayList<Float>();
		if(usertype==1) {			
			//该班级的所有学生的分数
			ArrayList<ScoreList> classscore=result.get(classidindex);
			//获取该学生的analyticyou
			for(int j = 0; j < classscore.size(); j++){
				if(usermail.equals(classscore.get(j).getStudentmail())) {
					System.out.println("有邮箱");
					ArrayList<ArrayList<Float>> subscore=classscore.get(j).getSubscore();
					for(int i=0;i<subscore.size();i++) {
						//4中题型
						Float temp =(float) 0;
						for(int m=0;m<subscore.get(i).size();m++) {
							System.out.println(usermail);
							System.out.println(i);
							temp=temp+subscore.get(i).get(m);
						}
						analyticyou.add(temp);
					}
					analyticyou.add(analyticyou.get(0)+analyticyou.get(1)+analyticyou.get(2)+analyticyou.get(3));
					analyticyou.add((float) (j+1));	
				}
						
			}		
			analyticyou.add(classservice.scorerank(score, analyticyou.get(4)));
			System.out.println("自己的自己的算完了算完了算完了算完了算完了算完了算完了");
			//--------------所有人的
			Float temp1 =(float) 0;
			Float temp2 =(float) 0;
			Float temp3 =(float) 0;
			Float temp4 =(float) 0;
			int stusum=0;
			for(int j = 0; j < result.size(); j++){
				//某班
				System.out.println("班");
				System.out.println(j);
				System.out.println(result.size());
				for(int i=0;i<result.get(j).size();i++) {
					System.out.println("人");
					System.out.println(i);
					System.out.println(result.get(j).size());
					stusum=stusum+result.get(j).size();
					//某人result.get(i).get(j)---ScoreList
					ScoreList stui = result.get(j).get(i);
					ArrayList<ArrayList<Float>> subscore=stui.getSubscore();
					for(int m=0;m<4;m++) {
						//4中题型
						for(int n=0;n<subscore.get(m).size();n++) {
							System.out.println("题型区分----");
							System.out.println(subscore.get(m).get(n));
							if(subscore.get(m).get(n)!=null) {
								if(m==0) {
									System.out.println("temp1");
									System.out.println(temp1);
									temp1=temp1+subscore.get(m).get(n);
									System.out.println(temp1);
								}
								else if(m==1) {
									System.out.println("temp2");
									System.out.println(temp2);
									temp2=temp2+subscore.get(m).get(n);
									System.out.println(temp2);
								}
								else if(m==2) {
									System.out.println("temp3");
									System.out.println(temp3);
									temp3=temp3+subscore.get(m).get(n);
									System.out.println(temp3);
								}
								else if(m==3) {
									System.out.println("temp4");
									System.out.println(temp4);								
									temp4=temp4+subscore.get(m).get(n);
									System.out.println(temp4);
							}
							}
							System.out.println(temp1);
						}
					}
				}
			}
			System.out.println("所有的所有的算完了算完了算完了算完了算完了算完了算完了");
			analyticall.add(temp1/stusum);
			analyticall.add(temp2/stusum);
			analyticall.add(temp3/stusum);
			analyticall.add(temp4/stusum);
			analyticall.add(analyticall.get(0)+analyticall.get(1)+analyticall.get(2)+analyticall.get(3));
			analyticall.add((float) (classscore.size()/2));
			analyticall.add((float) 50);		
			System.out.println("算完了算完了算完了算完了算完了算完了算完了");
		}		
		//------------------------向前端传值模块--------------------------------------
		model.addAttribute("usertype",usertype);
		model.addAttribute("allclassid", allclassid);
		model.addAttribute("allclassavg", allclassavg);
		model.addAttribute("allclassmax", allclassmax);	
		model.addAttribute("result", result);
		model.addAttribute("classid",classid);
		model.addAttribute("scorelever", scorelever);
		model.addAttribute("scorelevernow", scorelever1);	
		model.addAttribute("nowclassscore", result.get(classidindex));	
		//学生个人成绩分析
		System.out.println(analyticyou);
		System.out.println(analyticall);
		System.out.println(maxscore);
		model.addAttribute("analyticyou", analyticyou);	
		model.addAttribute("analyticall", analyticall);	
		model.addAttribute("maxscore", maxscore);	
		//------------------------向前端传值模块--------------------------------------
		System.out.println("传完了传完了传完了传完了传完了传完了传完了");
		if(usertype==0){
			ArrayList<Teacher> teacherinfo = classservice.getInfobyTMX(usermail);
			model.addAttribute("teacherinfo", teacherinfo);
		}
		else if(usertype==1) {
			ArrayList<Student> studentinfo = classservice.getInfobySMX(usermail);
			model.addAttribute("studentinfo", studentinfo);
		}
		else{
			ArrayList<Admin> admininfo = classservice.getInfobyAMX(usermail);
			model.addAttribute("admininfo", admininfo);
		}		
		model.addAttribute("usermail", usermail);
		return "checkscore";
	}

	//管理员审核班级，老师提交重新审核班级按钮
	@RequestMapping(value = { "/shenheclass" })
	public String shenheClass(HttpServletRequest request,Model model) throws ParseException {
		//session 里取用户类型+用户邮箱
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		String usermail = (String)request.getSession().getAttribute("usermail");
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		//session 取班级信息展示内容为0默认或者1搜索的
		int searchtype = Integer.parseInt(request.getParameter("searchtype"));
		model.addAttribute("searchtype", searchtype);
		int[] classinfo11 = (int [])request.getSession().getAttribute("classinfo1");
		//搜索的班级信息后移，因为需要等修改后重新获取的才有效
		int[] eqmail = (int [])request.getSession().getAttribute("eqmail");
		model.addAttribute("eqmail", eqmail);
		
		//从前端获取需要审核的班级ID
		String classid1 = request.getParameter("classid");
		Integer classid = Integer.parseInt(classid1); 
		
		//0：未审核，1：审核未通过，2：审核通过进行中，3：结课申请审核中，4：结课
		//管理员
		if(usertype==2) {
			int stype = Integer.parseInt(request.getParameter("stypey"));	
			System.out.println(stype);
			if(stype==1){//通过
				classservice.updateClassstateX(classid,2);
				//插入新审核记录
			  	//treattype：   0：审核考试  1：审核选择题 2：审核填空题 3：审核判断题 4：审核大题 5：审核班级
			  	//treatresult：    0：审核通过  1：审核不通过  2：审核结课通过
				classservice.insertNewrecordC(usermail,5,classid,"审核通过",0);
				
			}
			else {//不通过
				classservice.updateClassstateX(classid,1);
				classservice.insertNewrecordC(usermail,5,classid,"审核不通过",1);
			}
			ArrayList<Classes> classinfo = classservice.getClassX();
			model.addAttribute("classinfo", classinfo);
			ArrayList<Admin> admininfo = classservice.getInfobyAMX(usermail);
			model.addAttribute("admininfo", admininfo);
		}
		//老师
		else {
			//重新提交审核按钮
			classservice.updateClassstateX(classid,0);
			ArrayList<Classes> classinfo = classservice.ClassidByTeacheridX(usermail);
			model.addAttribute("classinfo", classinfo);
			ArrayList<Teacher> teacherinfo = classservice.getInfobyTMX(usermail);
			model.addAttribute("teacherinfo", teacherinfo);
		}
		ArrayList<Classes> classinfo1 =classservice.transclass(classinfo11);			
		model.addAttribute("classinfo1", classinfo1);
		model.addAttribute("usermail", usermail);
		return "class";			
	}
			
	//老师，学生，管理员搜索班级
	@RequestMapping(value = { "/searchclass" })
	public String seacrchClass(HttpServletRequest request,Model model) throws ParseException {	
		//session 里取用户类型+用户邮箱
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		String usermail = (String)request.getSession().getAttribute("usermail");
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		//搜索按钮和搜索结果后面的按钮中存放了searchtype的赋值为1的隐藏input信息
		//更新session，向目标前端页面传searchtype
		int searchtype = Integer.parseInt(request.getParameter("searchtype"));
		request.getSession().setAttribute("searchtype",searchtype);//后加的，不知道会不会有影响
		model.addAttribute("searchtype", searchtype);
		//获取前端的搜索输入
		int stype = Integer.parseInt(request.getParameter("stype"));
		System.out.println(stype);
		//判断搜索班级的输入				
		if(stype==5){//精准搜索
			String classid1 = request.getParameter("classid");
			Integer classid = Integer.parseInt(classid1);
			String state1 = request.getParameter("state");
			Integer state = Integer.parseInt(state1);		
			String classname = request.getParameter("classname");
			String teachermail = request.getParameter("teachermail");
			String classtype = request.getParameter("classtype");
			ArrayList<Classes> classinfo1= classservice.getClassbyAllX(classid,classname,classtype,state,teachermail);
			if(classinfo1.size()!=0) {
				int[] eqmail = new int[classinfo1.size()];
				for(Integer i=0;i<classinfo1.size();i++) {
					if(classinfo1.get(i).getTeachermail().equals(usermail)) {
						eqmail[i]=1;
					}
					else {
						eqmail[i]=0;
					}
					model.addAttribute("eqmail", eqmail);
					request.getSession().setAttribute("eqmail",eqmail);	
				}
				
			}
			model.addAttribute("classinfo1",classinfo1);						
			int[] classinfo2=classservice.transclassid(classinfo1);
			System.out.println(classinfo2);
			request.getSession().setAttribute("classinfo1",classinfo2);			
		}
		else if (stype==0){//班号搜索
			System.out.println("---1----");
			String classid1 = request.getParameter("classid");
			Integer classid = Integer.parseInt(classid1);
			System.out.println(classid1);
			System.out.println(classid);
			ArrayList<Classes> classinfo1= classservice.getClassbyIdX(classid);
			if(classinfo1.size()!=0) {
				int[] eqmail = new int[classinfo1.size()];
				for(Integer i=0;i<classinfo1.size();i++) {
					if(classinfo1.get(i).getTeachermail().equals(usermail)) {
						eqmail[i]=1;
					}
					else {
						eqmail[i]=0;
					}
					model.addAttribute("eqmail", eqmail);
					request.getSession().setAttribute("eqmail",eqmail);	
				}
				
			}
			model.addAttribute("classinfo1",classinfo1 );		
			int[] classinfo2=classservice.transclassid(classinfo1);
			request.getSession().setAttribute("classinfo1",classinfo2);			
		}
		else if (stype==4){//状态搜索
			String state1 = request.getParameter("state");
			Integer state = Integer.parseInt(state1);
			System.out.println("------------");
			System.out.println(state1);
			System.out.println(state);
			System.out.println("------------");
			ArrayList<Classes> classinfo1= classservice.getClassbystateX(state);
			if(classinfo1.size()!=0) {
				int[] eqmail = new int[classinfo1.size()];
				for(Integer i=0;i<classinfo1.size();i++) {
					if(classinfo1.get(i).getTeachermail().equals(usermail)) {
						eqmail[i]=1;
					}
					else {
						eqmail[i]=0;
					}
					model.addAttribute("eqmail", eqmail);
					request.getSession().setAttribute("eqmail",eqmail);	
				}
				
			}
			model.addAttribute("classinfo1",classinfo1 );	
			int[] classinfo2=classservice.transclassid(classinfo1);
			request.getSession().setAttribute("classinfo1",classinfo2);			
		}
		else if (stype==1){//班级名称搜索
			String classname = request.getParameter("classname");
			System.out.println(classname);
			ArrayList<Classes> classinfo1= classservice.getClassbyclassnameX(classname);
			model.addAttribute("classinfo1", classinfo1);	
			int[] classinfo2=classservice.transclassid(classinfo1);
			if(classinfo1.size()!=0) {
				int[] eqmail = new int[classinfo1.size()];
				for(Integer i=0;i<classinfo1.size();i++) {
					if(classinfo1.get(i).getTeachermail().equals(usermail)) {
						eqmail[i]=1;
					}
					else {
						eqmail[i]=0;
					}
					model.addAttribute("eqmail", eqmail);
					request.getSession().setAttribute("eqmail",eqmail);	
				}
				
			}
			System.out.println(classinfo2);
			request.getSession().setAttribute("classinfo1",classinfo2);			
		}
		else if (stype==3){//任课老师邮箱搜索
			String teachermail = request.getParameter("teachermail");
			System.out.println(teachermail);
			ArrayList<Classes> classinfo1= classservice.ClassidByTeacheridX(teachermail);
			model.addAttribute("classinfo1", classinfo1);
			int[] classinfo2=classservice.transclassid(classinfo1);
			if(classinfo1.size()!=0) {
				int[] eqmail = new int[classinfo1.size()];
				for(Integer i=0;i<classinfo1.size();i++) {
					if(classinfo1.get(i).getTeachermail().equals(usermail)) {
						eqmail[i]=1;
					}
					else {
						eqmail[i]=0;
					}
					model.addAttribute("eqmail", eqmail);
					request.getSession().setAttribute("eqmail",eqmail);	
				}
				
			}
			System.out.println(classinfo2);
			request.getSession().setAttribute("classinfo1",classinfo2);			
		}
		else if (stype==2){//班级类型搜索，即科目，英语，数学等
			String classtype = request.getParameter("classtype");
			System.out.println(classtype);
			ArrayList<Classes> classinfo1=  classservice.getClassbyclasstypeX(classtype);
			model.addAttribute("classinfo1",classinfo1);
			int[] classinfo2=classservice.transclassid(classinfo1);
			if(classinfo1.size()!=0) {
				int[] eqmail = new int[classinfo1.size()];
				for(Integer i=0;i<classinfo1.size();i++) {
					if(classinfo1.get(i).getTeachermail().equals(usermail)) {
						eqmail[i]=1;
					}
					else {
						eqmail[i]=0;
					}
					model.addAttribute("eqmail", eqmail);
					request.getSession().setAttribute("eqmail",eqmail);	
				}
				
			}
			System.out.println(classinfo2);
			request.getSession().setAttribute("classinfo1",classinfo2);			
		}
		else if (stype==6) {
			ArrayList<Classes> classinfo1= classservice.getClassX();
			model.addAttribute("classinfo1", classinfo1);
			int[] classinfo2=classservice.transclassid(classinfo1);
			if(classinfo1.size()!=0) {
				int[] eqmail = new int[classinfo1.size()];
				for(Integer i=0;i<classinfo1.size();i++) {
					if(classinfo1.get(i).getTeachermail().equals(usermail)) {
						eqmail[i]=1;
					}
					else {
						eqmail[i]=0;
					}
					model.addAttribute("eqmail", eqmail);
					request.getSession().setAttribute("eqmail",eqmail);	
				}
				
			}
			System.out.println(classinfo2);
			request.getSession().setAttribute("classinfo1",classinfo2);	
		}
		System.out.println("------------");
		//老师
		if(usertype==0) {	
			System.out.println("------------");
			//默认班级信息
			ArrayList<Classes> classinfo = classservice.ClassidByTeacheridX(usermail);
			model.addAttribute("classinfo", classinfo);
			//个人信息
			ArrayList<Teacher> teacherinfo = classservice.getInfobyTMX(usermail);
			model.addAttribute("teacherinfo", teacherinfo);			
		}
		//学生
		else if(usertype==1) {
			//默认班级信息
			ArrayList<Integer> classid =classservice.getClassidByStudentmailX(usermail);
			ArrayList<Classes> classinfo =new ArrayList<Classes>();
			for(Integer i=0; i < classid.size(); i++) {
				classinfo.add(classservice.getClassbyUIdX(classid.get(i)));
	    	}
			model.addAttribute("classinfo", classinfo);
			//个人信息
			ArrayList<Student> studentinfo = classservice.getInfobySMX(usermail);
			model.addAttribute("studentinfo", studentinfo);
			//判断是否已经加入
			int[] classinfo11 = (int [])request.getSession().getAttribute("classinfo1");
			if(classinfo11!=null) {
				int[] joinclass = new int[classinfo11.length];
				for(Integer i = 0; i < classinfo11.length; i++) {
					//如果还没有加入这个班级，则按钮为可以显示
					if(classservice.getidX(classinfo11[i],studentinfo.get(0).getUserid())==null) {
						joinclass[i] = 0;	
					}
					//已经加入了这个班级，则按钮为不显示
					else {
						joinclass[i] = 1;	
					}
				}
				model.addAttribute("joinclass", joinclass);
			}			
		}
		//管理员
		else{
			//默认班级信息
			ArrayList<Classes> classinfo = classservice.getClassX();
			model.addAttribute("classinfo", classinfo);
			//个人信息
			ArrayList<Admin> admininfo = classservice.getInfobyAMX(usermail);
			model.addAttribute("admininfo", admininfo);			
		}
		model.addAttribute("usermail", usermail);
		return "class";
	}
	
	//学生加入班级
	@RequestMapping(value = { "/joinclass" })
	public String joinClass(HttpServletRequest request,Model model) throws ParseException {
		System.out.println("------------");
		//session 里取用户类型+用户邮箱
		String usertype1 = String.valueOf(request.getSession().getAttribute("usertype"));
		Integer usertype = Integer.parseInt(usertype1);
		model.addAttribute("usertype", usertype);
		String usermail = (String)request.getSession().getAttribute("usermail");
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		System.out.println("------------");
		//session 取班级信息展示内容为0默认或者1搜索的
		int searchtype = Integer.parseInt(request.getParameter("searchtype"));
		model.addAttribute("searchtype", searchtype);
		request.getSession().setAttribute("searchtype",searchtype);//后加的，不知道会不会有影响
		int[] classinfo11 = (int [])request.getSession().getAttribute("classinfo1");
		//搜索的班级信息后移，因为需要等学生加入了之后搜出来的才有效
		int[] eqmail = (int [])request.getSession().getAttribute("eqmail");
		model.addAttribute("eqmail", eqmail);
		
		System.out.println("------------");
		
		
		//插入学生-班级对，若已经加入了，则不重复加入班级
		String classid1 = request.getParameter("classid");
		Integer classid = Integer.parseInt(classid1);
		Integer studentid = classservice.getIDbyMX(usermail);	
		if(classservice.getidX(classid,studentid)==null) {
			classservice.insertNewstudentX(classid,studentid);	
		}	
		System.out.println("------------");
		//默认班级信息
		System.out.println(usermail);
		ArrayList<Integer> classid2 =classservice.getClassidByStudentmailX(usermail);
		System.out.println(classid2);
		ArrayList<Classes> classinfo =new ArrayList<Classes>();
		System.out.println(classid2.size());
		for(Integer i = 0; i < classid2.size(); i++) {
			System.out.println(classid2.get(i));
			classinfo.add(classservice.getClassbyUIdX(classid2.get(i)));
	    }
		model.addAttribute("classinfo", classinfo);
		System.out.println("------------");
		//个人信息
		ArrayList<Student> studentinfo = classservice.getInfobySMX(usermail);
		model.addAttribute("studentinfo", studentinfo);
		ArrayList<Classes> classinfo1 =classservice.transclass(classinfo11);			
		model.addAttribute("classinfo1", classinfo1);
		System.out.println("------------");
		//判断是否已经加入
		if(classinfo11!=null) {
			int[] joinclass = new int[classinfo11.length];
			for(Integer i = 0; i < classinfo11.length; i++) {
				//如果还没有加入这个班级，则按钮为可以显示
				if(classservice.getidX(classinfo11[i],studentid)==null) {
					joinclass[i] = 0;	
				}
				//已经加入了这个班级，则按钮为不显示
				else {
					joinclass[i] = 1;	
				}
			}
			model.addAttribute("joinclass", joinclass);		
		}
		model.addAttribute("usermail", usermail);
		return "class";
	}
		
}
