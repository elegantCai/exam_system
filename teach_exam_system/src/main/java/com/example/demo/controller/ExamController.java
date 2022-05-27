package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

import com.example.demo.entity.Answeredbigq;
import com.example.demo.entity.Bigquestion;
import com.example.demo.entity.Choice;
import com.example.demo.entity.Choiceanswered;
import com.example.demo.entity.Classes;
import com.example.demo.entity.Distribution;
import com.example.demo.entity.Examinfo;
import com.example.demo.entity.Fillinans;
import com.example.demo.entity.Fillinquestion;
import com.example.demo.entity.Jugementans;
import com.example.demo.entity.Jugementinfo;
import com.example.demo.service.ExamService;
import com.example.demo.service.ExamService.Forbig;
import com.example.demo.service.ExamService.Forfill;
import com.example.demo.service.QuestionbankService;
import com.example.demo.service.UploadfileService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Controller
public class ExamController {
	@Autowired
    private QuestionbankService questionbankService;
	@Autowired
    private UploadfileService uploadfileService;
	@Autowired
    private ExamService examService;
	
	//去往考试主页
	@RequestMapping(value = { "/mainexampage" })
	public String gotoqb(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		return "mainexampage";
	}
		//去往上传题目的页面        
		@RequestMapping(value = { "/AddExaminfo" })
		public String addexaminfo(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			ArrayList<Classes> teacherclasses=examService.getteacherpassedclass(usermail);	//这个老师通过审核的班级，供选择
			model.addAttribute("teacherclasses", teacherclasses);
			
			return "upexaminfo";
		}
		
		//填考试信息  
		@RequestMapping(value = { "/AddExaminfo1" })
		public String addexamquestion(HttpServletRequest request,Model model) throws ParseException {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			String orgstarttime =request.getParameter("starttime");
			String orgendtime =request.getParameter("endtime");
			String strstarttime=examService.transtimestr(orgstarttime);
			String strendtime=examService.transtimestr(orgendtime);
			Calendar starttime= examService.transtime(strstarttime);
			Calendar endtime= examService.transtime(strendtime);
			String subject=request.getParameter("subject");    
			String strtype=request.getParameter("type");    //是否贡献为题库
			
			int type;
			if (strtype!=null) {type=Integer.parseInt(strtype);}
			else {type=0;}    //不选就默认作为题库
			
			String[] classidlist=request.getParameterValues("classid");
			for (String i:classidlist) {System.out.println(i);}
			int newexamid=examService.insertnewexam(usermail, starttime, endtime, type, subject,classidlist);
			
			//给老师看班级名，而不是班级id
			String[] classnames=examService.getclassnames(classidlist);
									
			model.addAttribute("newexamid", newexamid);
			model.addAttribute("subject", subject);
			model.addAttribute("starttime", strstarttime);
			model.addAttribute("endtime", strendtime);
			model.addAttribute("type", type);
			model.addAttribute("classidlist", classidlist);
			model.addAttribute("classnames", classnames);
					
			//顺便直接存在session里，因为上传考试的时候要一直显示
			request.getSession().setAttribute("newexamid", newexamid);	
			request.getSession().setAttribute("subject", subject);
			request.getSession().setAttribute("starttime", strstarttime);
			request.getSession().setAttribute("endtime", strendtime);
			request.getSession().setAttribute("type", type);
			request.getSession().setAttribute("classidlist", classidlist);
			request.getSession().setAttribute("classnames", classnames);
			
			//存四个arraylist在session里，为了侧边显示上传过的题
			int[] upcqlist = null;
			request.getSession().setAttribute("upcqlist", upcqlist);	
			int[] upjqlist = null;
			request.getSession().setAttribute("upjqlist", upjqlist);	
			int[] upfqlist = null;
			request.getSession().setAttribute("upfqlist", upfqlist);	
			int[] upbqlist = null;
			request.getSession().setAttribute("upbqlist", upbqlist);	
			//顺便再存一个总分
			int totalpoint=0;
			request.getSession().setAttribute("totalpoint", totalpoint);	
			
			return "upexamquestion";
		}
		
		//选择上传什么题型
		@RequestMapping(value = { "/AddingExamquestion1" })
		public String addquestion_choose(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			//取考试信息
			int newexamid = (int)request.getSession().getAttribute("newexamid");
			String subject = (String)request.getSession().getAttribute("subject");
			String starttime = (String)request.getSession().getAttribute("starttime");
			String endtime = (String)request.getSession().getAttribute("endtime");
			int type = (int)request.getSession().getAttribute("type");
			String[] classidlist = (String[])request.getSession().getAttribute("classidlist");
			String[] classnames = (String[])request.getSession().getAttribute("classnames");
			
			model.addAttribute("newexamid", newexamid);
			model.addAttribute("subject", subject);
			model.addAttribute("starttime", starttime);
			model.addAttribute("endtime", endtime);
			model.addAttribute("type", type);
			model.addAttribute("classidlist", classidlist);
			model.addAttribute("classnames", classnames);
			
			String strqtype= request.getParameter("qtype");  //题型
			int qtype=Integer.parseInt(strqtype);
			model.addAttribute("qtype", qtype);
			//取侧边栏
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
			model.addAttribute("upcqlist", upcqlist);
			int[]  upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
			model.addAttribute("upjqlist", upjqlist);
			int[]  upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
			model.addAttribute("upfqlist", upfqlist);
			int[]  upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
			model.addAttribute("upbqlist", upbqlist);
			int totalpoint=(int) request.getSession().getAttribute("totalpoint");
			model.addAttribute("totalpoint", totalpoint);
			
			//把上传的题型存到session里
			request.getSession().setAttribute("qtype", qtype);	
			return "upexamquestion";
		}
		
		//真正开始上传题目
		@RequestMapping(value = { "/AddingExamquestion2" },method = RequestMethod.POST)
		public String addexamquestion_content(HttpServletRequest request,Model model,
				@RequestParam("answerpic")MultipartFile answerpic,@RequestParam("picture")MultipartFile picture) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			//取考试信息
			int newexamid = (int)request.getSession().getAttribute("newexamid");
			String subject = (String)request.getSession().getAttribute("subject");
			String starttime = (String)request.getSession().getAttribute("starttime");
			String endtime = (String)request.getSession().getAttribute("endtime");
			int type = (int)request.getSession().getAttribute("type");
			String[] classidlist = (String[])request.getSession().getAttribute("classidlist");
			String[] classnames = (String[])request.getSession().getAttribute("classnames");
			
			model.addAttribute("newexamid", newexamid);
			model.addAttribute("subject", subject);
			model.addAttribute("starttime", starttime);
			model.addAttribute("endtime", endtime);
			model.addAttribute("type", type);
			model.addAttribute("classidlist", classidlist);
			model.addAttribute("classnames", classnames);
			
			//获取上传的题型
			int qtype = (int)request.getSession().getAttribute("qtype");
			model.addAttribute("qtype", qtype);
			//存在哪种题的文件夹
			String filedic=uploadfileService.getfiledic(qtype);
			
			//获取题目信息
			String strpoint= request.getParameter("point");
			String content= request.getParameter("content");
			String difficultylevel=request.getParameter("difficulty");
			String answer=request.getParameter("answer");
			System.out.println(answer);
			int point=Integer.parseInt(strpoint);
			//如果是选择题还得获取选项
			String optiona=request.getParameter("optiona");
			String optionb=request.getParameter("optionb");
			String optionc=request.getParameter("optionc");
			String optiond=request.getParameter("optiond");
			float difficulty=questionbankService.transdifficulty(difficultylevel);    //这是转化的difficulty
			
			//插入文字信息并返回ID
			int newid=questionbankService.insertNewQuestionC(qtype, 1, answer, content, difficulty, point, subject, usermail,optiona,optionb,optionc,optiond);  //这是题库，所以不是考试
			//存图片
				//存题干图片
			String picpath=uploadfileService.getfilepath(filedic,"picture",picture,newid);
			uploadfileService.uploadfile(picpath, picture);
			if(qtype==3) {
				//存标答图片
				String answerpicpath=uploadfileService.getfilepath("bigq","answerpic",answerpic,newid);
				uploadfileService.uploadfile(answerpicpath, answerpic);
				//插入图片的相对路径到数据库
				String relpicpath=uploadfileService.getrelpath("bigq", "picture",picpath);
				String relanswerpicpath=uploadfileService.getrelpath("bigq", "answerpic",answerpicpath);
				questionbankService.updatebigqnewpic(relanswerpicpath,relpicpath,newid);
			}
			else {
				String relpicpath=uploadfileService.getrelpath(filedic, "picture",picpath);
				questionbankService.updatenewpic(qtype, relpicpath, newid);
			}
			
			//把插入的题插入到考试题目表里
			examService.insertexamq(newexamid, qtype, newid);
			//顺道更新一下session里的为了侧边栏的列表
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");
			int[] upjqlist=(int[]) request.getSession().getAttribute("upjqlist");
			int[] upfqlist=(int[]) request.getSession().getAttribute("upfqlist");
			int[] upbqlist=(int[]) request.getSession().getAttribute("upbqlist");
			if (qtype==0) {	
				upcqlist=examService.transarrayandinsert(upcqlist, newid);
				request.getSession().setAttribute("upcqlist", upcqlist);
			}
			else if (qtype==1) {
				upjqlist=examService.transarrayandinsert(upjqlist, newid);
				request.getSession().setAttribute("upjqlist", upjqlist);
			}
			else if (qtype==2) {
				upfqlist=examService.transarrayandinsert(upfqlist, newid);
				request.getSession().setAttribute("upfqlist", upfqlist);			
			}
			else {
				upbqlist=examService.transarrayandinsert(upbqlist, newid);
				request.getSession().setAttribute("upbqlist", upbqlist);
			}
			//侧栏更新完了，再传到前端 
			model.addAttribute("upcqlist", upcqlist);
			model.addAttribute("upjqlist", upjqlist);
			model.addAttribute("upfqlist", upfqlist);
			model.addAttribute("upbqlist", upbqlist);
			//顺道更新一下总分数
			int totalpoint=(int) request.getSession().getAttribute("totalpoint");
			totalpoint=totalpoint+point;
			model.addAttribute("totalpoint", totalpoint);
			request.getSession().setAttribute("totalpoint", totalpoint);
			//下面是获取要显示的东西
				//根据题型找到题目信息
					//是选择题
			if (qtype==0) {
				model.addAttribute("newqinfo", questionbankService.getNewchoiceq(newid));
				}
				//是判断题
			if (qtype==1) {
				model.addAttribute("newqinfo", questionbankService.getjq(newid));
				}
				//是填空题
			if (qtype==2) {
				model.addAttribute("newqinfo", questionbankService.getfq(newid));
				}
				//是大题
			if (qtype==3) {
				model.addAttribute("newqinfo", questionbankService.getNewbigq(newid));
				}		
			model.addAttribute("difficulty",difficulty);
			return "upexamquestionmid";
		}
		
		//考试中看考试的时候上传的题目
		@RequestMapping(value = { "/Addexam_qdetail" })
		public String duringuploadseedetail(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			//取考试信息
			int newexamid = (int)request.getSession().getAttribute("newexamid");
			String subject = (String)request.getSession().getAttribute("subject");
			String starttime = (String)request.getSession().getAttribute("starttime");
			String endtime = (String)request.getSession().getAttribute("endtime");
			int type = (int)request.getSession().getAttribute("type");
			String[] classidlist = (String[])request.getSession().getAttribute("classidlist");
			String[] classnames = (String[])request.getSession().getAttribute("classnames");
			model.addAttribute("newexamid", newexamid);
			model.addAttribute("subject", subject);
			model.addAttribute("starttime", starttime);
			model.addAttribute("endtime", endtime);
			model.addAttribute("type", type);
			model.addAttribute("classidlist", classidlist);
			model.addAttribute("classnames", classnames);
			
			String strqtype= request.getParameter("qtype");  //题型
			int qtype=Integer.parseInt(strqtype);
			model.addAttribute("qtype", qtype);
			//取侧边栏
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
			model.addAttribute("upcqlist", upcqlist);
			int[]  upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
			model.addAttribute("upjqlist", upjqlist);
			int[]  upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
			model.addAttribute("upfqlist", upfqlist);
			int[]  upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
			model.addAttribute("upbqlist", upbqlist);
			int totalpoint=(int) request.getSession().getAttribute("totalpoint");
			model.addAttribute("totalpoint", totalpoint);
			
			//看点的那一道题目是什么id
			String strqid= request.getParameter("qid");  
			int qid=Integer.parseInt(strqid);
			//根据题型找到题目信息并且做题(更新数据库）
			//是选择题
			if (qtype==0) {
				Choice newqinfo=questionbankService.getNewchoiceq(qid);
				model.addAttribute("newqinfo", newqinfo);
				float difficulty=newqinfo.getDifficulty();
				model.addAttribute("difficulty",difficulty);
				}
			//是判断题
			if (qtype==1) {
				Jugementinfo newqinfo=questionbankService.getjq(qid);
				model.addAttribute("newqinfo", newqinfo);
				float difficulty=newqinfo.getDifficulty();
				model.addAttribute("difficulty",difficulty);
				}
			//是填空题
			if (qtype==2) {
				Fillinquestion newqinfo=questionbankService.getfq(qid);
				model.addAttribute("newqinfo",newqinfo );
				float difficulty=newqinfo.getDifficulty();
				model.addAttribute("difficulty",difficulty);
				}
			//是大题
			if (qtype==3) {
				Bigquestion newqinfo=questionbankService.getNewbigq(qid);
				model.addAttribute("newqinfo", newqinfo);
				float difficulty=newqinfo.getDifficulty();
				model.addAttribute("difficulty",difficulty);
				}	
			return "upexamquestionmid";
		}
		//上传考试完成
		@RequestMapping(value = { "/Finishaddexam" })
		public String finishup_queston(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			int newexamid = (int)request.getSession().getAttribute("newexamid");
			model.addAttribute("newexamid", newexamid);
			//插入总分
			int totalpoint=(int) request.getSession().getAttribute("totalpoint");
			examService.updatetotalpoint(newexamid, totalpoint);
			String[] classidlist = (String[])request.getSession().getAttribute("classidlist");
			String[] classnames = (String[])request.getSession().getAttribute("classnames");
			model.addAttribute("classidlist", classidlist);
			model.addAttribute("classnames", classnames);
			
			Examinfo thisexaminfo=examService.getExambyid(newexamid);
			model.addAttribute("thisexaminfo",thisexaminfo);
			
			ArrayList<Integer> qidlist=examService.getExamqid(newexamid);
			ArrayList<Integer> qtypelist=examService.getExamqtype(newexamid);
			ArrayList<ArrayList<Integer>> corrQidandtype=examService.getcorrQidandQtype(qidlist, qtypelist);
			
			ArrayList<Integer> cqlist=corrQidandtype.get(0);
			ArrayList<Integer> jqlist=corrQidandtype.get(1);
			ArrayList<Integer> fqlist=corrQidandtype.get(2);
			ArrayList<Integer> bqlist=corrQidandtype.get(3);
			
			ArrayList<Choice> cqinfolist=examService.getCqofexam(cqlist);
			ArrayList<Jugementinfo> jqinfolist=examService.getJqofexam(jqlist);
			ArrayList<Fillinquestion> fqinfolist=examService.getFqofexam(fqlist);
			ArrayList<Bigquestion> bqinfolist=examService.getBqofexam(bqlist);
			
			model.addAttribute("cqinfolist", cqinfolist);
			model.addAttribute("jqinfolist", jqinfolist);
			model.addAttribute("fqinfolist", fqinfolist);
			model.addAttribute("bqinfolist", bqinfolist);
			
			//我考试上传完了之后，立即分发考试给班级里有的人(要改成审核完之后、但是等会再写）
			//examService.distributeexam(classidlist, newexamid);
			
			
			return "wholeupexam";
		}
		
		//搜索考试
		@RequestMapping(value = { "/myexam" })
		public String tosearchexam(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			//老师可以看见自己发布的考试
			if (usertype==0) {
				ArrayList<ArrayList<Examinfo>> bystate=examService.getmyupexam(usermail);
				model.addAttribute("notcheck", bystate.get(0));
				model.addAttribute("notbegin", bystate.get(1));
				model.addAttribute("examing", bystate.get(2));
				model.addAttribute("finish", bystate.get(3));
				model.addAttribute("corrected", bystate.get(4));
				model.addAttribute("checkin", bystate.get(5));
				model.addAttribute("fail", bystate.get(6));
			}
			//管理员可以所有的考试
			if (usertype==2) {
				String strstate= request.getParameter("state");  //题型
				int state;
				if (strstate!=null) {
					state=Integer.parseInt(strstate);
				}
				else {state=0;}//默认看未审核的
				ArrayList<Examinfo> thisstate=examService.getSpecState(state);
				model.addAttribute("thisstate", thisstate);
			}
			
			//学生可以看见自己待参加的考试
			if (usertype==1) {
				//缺考的考试
				ArrayList<Distribution> absentexam=examService.getmyexam(usermail, 1);
				ArrayList<Examinfo> absentexaminfo=examService.getcorexams(absentexam);
				//签到中的考试
				ArrayList<Distribution> checkinginexam=examService.getmyexam(usermail, 4);
				ArrayList<Examinfo> checkinginexaminfo=examService.getcorexams(checkinginexam);
				//未开始的考试
				ArrayList<Distribution> notbeginexam=examService.getmyexam(usermail, 2);
				ArrayList<Examinfo> notbeginexaminfo=examService.getcorexams(notbeginexam);
				//考完的考试(没批完）
				ArrayList<Distribution> finishedexam=examService.getmyexam(usermail, 0);
				ArrayList<Examinfo> finishedexaminfo=examService.getcorexams(finishedexam);
				//考完的考试(完）
				ArrayList<Distribution> correctedexam=examService.getmyexam(usermail, 3);
				ArrayList<Examinfo> correctedexaminfo=examService.getcorexams(correctedexam);
				
				model.addAttribute("absentexam", absentexam);
				model.addAttribute("notbeginexam", notbeginexam);
				model.addAttribute("checkinginexam", checkinginexam);
				model.addAttribute("checkinginexaminfo", checkinginexaminfo);
				model.addAttribute("finishedexam", finishedexam);
				model.addAttribute("correctededexam", correctedexam);
				model.addAttribute("absentexaminfo", absentexaminfo);
				model.addAttribute("notbeginexaminfo", notbeginexaminfo);
				model.addAttribute("finishedexaminfo", finishedexaminfo);
				model.addAttribute("correctededexaminfo", correctedexaminfo);	
			}
			return "exampage";
		}
		
		@RequestMapping(value = { "/Examdetail" })
		public String toexamdetail(HttpServletRequest request,Model model) throws ParseException {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			String strexamid= request.getParameter("examid"); 
			int examid=Integer.parseInt(strexamid);
			request.getSession().setAttribute("examid",examid);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			int examState=thisexaminfo.getState();
			int toqb=thisexaminfo.getType();
			//判断是否从未开始改为签到中，顺便把分发表里的记录改成签到中
			if (examState==1){
				Calendar starttime=thisexaminfo.getStarttime();
				if (examService.judgetime(starttime)) {
					//如果发现还比开始时间晚，那改成进行中
					if (examService.judgetime2(starttime)) {
						Calendar endtime=thisexaminfo.getEndtime();
						//如果发现比结束时间还晚，那改成结束了，顺便把分发表里没考的同学们改成缺考
						if (examService.judgetime2(endtime)) {
							examService.updateexamState(3, examid);
							examState=3;
							examService.updateDstate2(examid);
							examService.updateDstate3(examid, toqb);
						}
						else {
							examService.updateexamState(2, examid);
							examState=2;
							examService.updateDstate1(examid);
						}
					}
					else {
						examService.updateexamState(5, examid);
						examState=5;
						examService.updateDstate1(examid);
					}
				}
			}
			//判断是否从签到改为进行中
			else if (examState==5){
				Calendar starttime=thisexaminfo.getStarttime();
				if (examService.judgetime2(starttime)) {
					Calendar endtime=thisexaminfo.getEndtime();
					if (examService.judgetime2(endtime)) {
						//如果还比结束时间晚，直接改成已结束
						examService.updateexamState(3, examid);
						examState=3;
						examService.updateDstate2(examid);
						examService.updateDstate3(examid, toqb);
					}
					else {
						examService.updateexamState(2, examid);
						examState=2;
					}
				}
			}
			//判断是否从进行中改为结束未批改
			else if (examState==2){
				Calendar endtime=thisexaminfo.getEndtime();
				if (examService.judgetime2(endtime)) {
					examService.updateexamState(3, examid);
					examState=3;
					examService.updateDstate2(examid);
					examService.updateDstate3(examid, toqb);
				}
			}
			//判断是否从结束未批改改为已批改完毕(如果批完了顺便把分发表的总分给改了)
			else if(examState==3) {
				int forState=examService.getstate(examid);
				if (forState!=-1) {
					examState=forState;
				}
			}
			//这个考试考完了（或者查阅的人是管理员or老师)
			if (examState==3 || examState==4 || usertype==2 || usertype==0) {
				ArrayList<ArrayList<Integer>> allqidlist=examService.getexamqlist(examid);
				//选择题信息
				ArrayList<Choice> cqlist=examService.getCqofexam(allqidlist.get(0));
				//判断题信息
				ArrayList<Jugementinfo> jqlist=examService.getJqofexam(allqidlist.get(1));
				//填空题信息
				ArrayList<Fillinquestion> fqlist=examService.getFqofexam(allqidlist.get(2));
				//简答题信息
				ArrayList<Bigquestion> bqlist=examService.getBqofexam(allqidlist.get(3));
				model.addAttribute("cqlist", cqlist);
				model.addAttribute("jqlist", jqlist);
				model.addAttribute("fqlist", fqlist);
				model.addAttribute("bqlist", bqlist);
				model.addAttribute("bqcorlist", allqidlist.get(4));	
				request.getSession().setAttribute("bqidlist",examService.transAtoL(allqidlist.get(3)));
			}
			//这场考试的班级
			ArrayList<Integer> classofexam=examService.getclassofexam(examid);
			ArrayList<String> classnames=examService.getclassnames2(classofexam);
			model.addAttribute("thisexaminfo", thisexaminfo);
			model.addAttribute("examState", examState);
			model.addAttribute("classofexam", classofexam);
			model.addAttribute("classnames", classnames);
			
			//趣味小图片
			model.addAttribute("Checkpic",examService.checkinpicpath());
			return "examdetail";
		}
	
		@RequestMapping(value = { "/WaitingExam" })
		public String waitingExam(HttpServletRequest request,Model model) throws ParseException {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			String strexamid= request.getParameter("examid"); 
			int examid=Integer.parseInt(strexamid);
			request.getSession().setAttribute("examid", examid);	
			
			Examinfo thisexaminfo=examService.getexambyid(examid);
			Calendar starttime=thisexaminfo.getStarttime();
			Calendar endtime=thisexaminfo.getEndtime();
			int totalpoint=thisexaminfo.getTotalpoint();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String strendtime = sdf.format(endtime.getTime());
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strendtime2 = sdf2.format(endtime.getTime());
			//把结束时间存在session里，格式是yyyy-MM-dd（用来转Calendar）
			request.getSession().setAttribute("strendtime2", strendtime2);	
			//用来给js用。格式是yyyy/MM/dd
			request.getSession().setAttribute("strendtime1", strendtime);	
			
			//更改这个学生的分发表状态并且下发试题(并且把试题信息存住了)
			ArrayList<ArrayList<Integer>> examqidlist=examService.updateThisstu(examid, usermail);
			int[] upcqlist=examService.transAtoL(examqidlist.get(0));
			int[] upjqlist=examService.transAtoL(examqidlist.get(1));
			int[] upfqlist=examService.transAtoL(examqidlist.get(2));
			int[] upbqlist=examService.transAtoL(examqidlist.get(3));
			request.getSession().setAttribute("upcqlist", upcqlist);	
			request.getSession().setAttribute("upjqlist", upjqlist);	
			request.getSession().setAttribute("upfqlist", upfqlist);	
			request.getSession().setAttribute("upbqlist", upbqlist);
			request.getSession().setAttribute("totalpoint", totalpoint);	
			
			//看看这哥们做没做这道题
			int[] upcqdo;
			int[] upjqdo;
			int[] upfqdo;
			int[] upbqdo;
			int upcqlistlen;
			int upjqlistlen;
			int upfqlistlen;
			int upbqlistlen;
			if (upcqlist!=null) {upcqlistlen=upcqlist.length;upcqdo=new int[upcqlistlen];} else {upcqdo=null;upcqlistlen=0;}
			if (upjqlist!=null) {upjqlistlen=upjqlist.length;upjqdo=new int[upjqlistlen];} else {upjqdo=null;upjqlistlen=0;}
			if (upfqlist!=null) {upfqlistlen=upfqlist.length;upfqdo=new int[upfqlistlen];} else {upfqdo=null;upfqlistlen=0;}
			if (upbqlist!=null) {upbqlistlen=upbqlist.length;upbqdo=new int[upbqlistlen];} else {upbqdo=null;upbqlistlen=0;}
			
			model.addAttribute("upcqlist", upcqlist);
			model.addAttribute("upjqlist", upjqlist);
			model.addAttribute("upfqlist", upfqlist);
			model.addAttribute("upbqlist", upbqlist);
			request.getSession().setAttribute("upcqdo", upcqdo);	
			request.getSession().setAttribute("upjqdo", upjqdo);	
			request.getSession().setAttribute("upfqdo", upfqdo);
			request.getSession().setAttribute("upbqdo", upbqdo);	
			
			//看看剩多少时间
				//这一段是伪造的时间（为了方便）
				//改的时候把把前两句删了就行
			//String format="2022-04-10 17:57:00";
			//starttime=examService.transtime(format);
			String strstarttime = sdf.format(starttime.getTime());
			
			int min=(int) (endtime.getTimeInMillis() - starttime.getTimeInMillis())/(60*1000);  
			Calendar now = Calendar.getInstance();
			int leftms=(int) (starttime.getTimeInMillis() - now.getTimeInMillis()); 
			System.out.println(leftms);
			if(leftms<10) {leftms=10;}
			System.out.println(leftms);
			//剩多少秒跳转
			model.addAttribute("leftms",leftms);
			//考试总时长
			model.addAttribute("min",min);
			model.addAttribute("starttime",strstarttime);
			model.addAttribute("endtime",strendtime);
			
			return "waitingexam";
		}
		
		@RequestMapping(value = { "/DoingExam1" })
		public String DoingExam(HttpServletRequest request,Model model) throws ParseException {
			
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			//取侧边栏
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
			model.addAttribute("upcqlist", upcqlist);
			int[]  upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
			model.addAttribute("upjqlist", upjqlist);
			int[]  upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
			model.addAttribute("upfqlist", upfqlist);
			int[]  upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
			model.addAttribute("upbqlist", upbqlist);
				//做没做
			int[] upcqdo=(int[]) request.getSession().getAttribute("upcqdo");	
			model.addAttribute("upcqdo", upcqdo);
			int[] upjqdo=(int[]) request.getSession().getAttribute("upjqdo");	
			model.addAttribute("upjqdo", upjqdo);
			int[] upfqdo=(int[]) request.getSession().getAttribute("upfqdo");	
			model.addAttribute("upfqdo", upfqdo);
			int[] upbqdo=(int[]) request.getSession().getAttribute("upbqdo");	
			model.addAttribute("upbqdo", upbqdo);
				//总分
			int totalpoint=(int) request.getSession().getAttribute("totalpoint");
			model.addAttribute("totalpoint", totalpoint);
			//考试id
			int examid=(int) request.getSession().getAttribute("examid");
			model.addAttribute("examid", examid);
			
			//看本页显示哪一道题
			String strqid= request.getParameter("qid"); 
			int qid;
			int qtype;
			if(strqid!=null) {
				qid=Integer.parseInt(strqid);
				String strqtype= request.getParameter("qtype"); 
				qtype=Integer.parseInt(strqtype);
			}
			else {
				if (upcqlist.length!=0) {qid=upcqlist[0];qtype=0;}
				else if (upjqlist.length!=0) {qid=upjqlist[0];qtype=1;}
				else if (upfqlist.length!=0) {qid=upfqlist[0];qtype=2;}
				else {qid=upbqlist[0];qtype=3;}
			}
			
			model.addAttribute("qtype", qtype);
		//根据题型找到题目信息(这一页显示什么东西)，并且看这个题做没做
			//是选择题
		if (qtype==0) {
			model.addAttribute("qinfo", questionbankService.getNewchoiceq(qid));
			model.addAttribute("aqinfo",examService.getAcqduringExam(examid, usermail, qid));
			}
			//是判断题
		if (qtype==1) {
			model.addAttribute("qinfo", questionbankService.getjq(qid));
			model.addAttribute("aqinfo",examService.getAjqduringExam(examid, usermail, qid));
			}
			//是填空题
		if (qtype==2) {
			model.addAttribute("qinfo", questionbankService.getfq(qid));
			model.addAttribute("aqinfo",examService.getAfqduringExam(examid, usermail, qid));
			}
			//是大题
		if (qtype==3) {
			model.addAttribute("qinfo", questionbankService.getNewbigq(qid));
			model.addAttribute("aqinfo",examService.getAbqduringExam(examid, usermail, qid));
			}
		
		//自动提交
		String strendtime2 = (String)request.getSession().getAttribute("strendtime2");
		String strendtime1 = (String)request.getSession().getAttribute("strendtime1");
		Calendar endtime=examService.transtime(strendtime2);
		Calendar now=Calendar.getInstance();
		int leftms=(int) (endtime.getTimeInMillis() - now.getTimeInMillis());  
		model.addAttribute("leftms", leftms);
		model.addAttribute("endtime",strendtime1);
		
		Examinfo thisexaminfo=examService.getexambyid(examid);
		model.addAttribute("thisexaminfo",thisexaminfo);
		
			return "doingexam";
		}
		
		@RequestMapping(value = { "/DoingExam2" },method = RequestMethod.POST)
		public String DoingExam_up(HttpServletRequest request,Model model,@RequestParam("stuanswerpic")MultipartFile answerpic) throws ParseException {
			
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			//取侧边栏
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
			model.addAttribute("upcqlist", upcqlist);
			int[]  upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
			model.addAttribute("upjqlist", upjqlist);
			int[]  upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
			model.addAttribute("upfqlist", upfqlist);
			int[]  upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
			model.addAttribute("upbqlist", upbqlist);
				//做没做
			int[] upcqdo=(int[]) request.getSession().getAttribute("upcqdo");	
			int[] upjqdo=(int[]) request.getSession().getAttribute("upjqdo");			
			int[] upfqdo=(int[]) request.getSession().getAttribute("upfqdo");			
			int[] upbqdo=(int[]) request.getSession().getAttribute("upbqdo");	
			
				//总分
			int totalpoint=(int) request.getSession().getAttribute("totalpoint");
			model.addAttribute("totalpoint", totalpoint);
			//考试id
			int examid=(int) request.getSession().getAttribute("examid");
			model.addAttribute("examid", examid);
			
			//看本页显示哪一道题(应该显示下一道题了)  顺便更新一下做过表
			String strqid= request.getParameter("qid"); 
			int qid=Integer.parseInt(strqid);
			String strqtype= request.getParameter("qtype"); 
			int qtype=Integer.parseInt(strqtype);
			String answer= request.getParameter("answer"); 
			
			if (qtype==0) {
				int position=Arrays.binarySearch(upcqlist, qid);
				upcqdo[position]=1;
				int thesize=upcqlist.length;
				examService.updateECq(examid, qid, answer, usermail);
				if (position<thesize-1) {
					qid=upcqlist[position+1];
				}
				else if (upjqlist.length!=0)
				{
					qid=upjqlist[0];qtype=1;
				}
				else if (upfqlist.length!=0)
				{
					qid=upfqlist[0];qtype=2;
				}
				else if(upbqlist.length!=0)
				{
					qid=upbqlist[0];qtype=3;
				}
				else {
					model.addAttribute("warning", "这是最后一题了");
				}
			}
			else if (qtype==1) {
				int position=Arrays.binarySearch(upjqlist, qid);
				upjqdo[position]=1;
				examService.updateEJq(examid, qid, answer, usermail);
				int thesize=upjqlist.length;
				if (position<thesize-1) {
					qid=upjqlist[position+1];
				}
				else if (upfqlist.length!=0)
				{
					qid=upfqlist[0];qtype=2;
				}
				else if(upbqlist.length!=0)
				{
					qid=upbqlist[0];qtype=3;
				}
				else {
					model.addAttribute("warning", "这是最后一题了");
				}
			}
			else if (qtype==2) {
				int position=Arrays.binarySearch(upfqlist, qid);
				upfqdo[position]=1;
				int thesize=upfqlist.length;
				examService.updateEFq(examid, qid, answer, usermail);
				if (position<thesize-1) {
					qid=upfqlist[position+1];
				}
				else if(upbqlist.length!=0)
				{
					qid=upbqlist[0];qtype=3;
				}
				else {
					model.addAttribute("warning", "这是最后一题了");
				}
			}
			else {
				int position=Arrays.binarySearch(upbqlist, qid);
				upbqdo[position]=1;
				int thesize=upbqlist.length;
				examService.updateEBq(examid, qid, answer, usermail);
				int aid=examService.getEBQaid(examid, qid, usermail);
				String answerpicpath=uploadfileService.getfilepath("bigq","answered",answerpic,aid);
				String relanswerpicpath=uploadfileService.getrelpath("bigq", "answered",answerpicpath);
				uploadfileService.uploadfile(answerpicpath, answerpic);
				examService.UpdateEBp(aid, relanswerpicpath);
				if (position<thesize-1) {
					qid=upbqlist[position+1];
				}
				else {
					model.addAttribute("warning", "这是最后一题了");
				}
			}
			
			
		//根据题型找到题目信息(这一页显示什么东西)，并且看这个题做没做
			//是选择题
		if (qtype==0) {
			model.addAttribute("qinfo", questionbankService.getNewchoiceq(qid));
			model.addAttribute("aqinfo",examService.getAcqduringExam(examid, usermail, qid));
			}
			//是判断题
		if (qtype==1) {
			model.addAttribute("qinfo", questionbankService.getjq(qid));
			model.addAttribute("aqinfo",examService.getAjqduringExam(examid, usermail, qid));
			}
			//是填空题
		if (qtype==2) {
			model.addAttribute("qinfo", questionbankService.getfq(qid));
			model.addAttribute("aqinfo",examService.getAfqduringExam(examid, usermail, qid));
			}
			//是大题
		if (qtype==3) {
			model.addAttribute("qinfo", questionbankService.getNewbigq(qid));
			model.addAttribute("aqinfo",examService.getAbqduringExam(examid, usermail, qid));
			}
		model.addAttribute("qtype", qtype);
		
		model.addAttribute("upcqdo", upcqdo);
		model.addAttribute("upjqdo", upjqdo);
		model.addAttribute("upfqdo", upfqdo);
		model.addAttribute("upbqdo", upbqdo);
		
		//自动提交
				String strendtime2 = (String)request.getSession().getAttribute("strendtime2");
				String strendtime1 = (String)request.getSession().getAttribute("strendtime1");
				Calendar endtime=examService.transtime(strendtime2);
				Calendar now=Calendar.getInstance();
				int leftms=(int) (endtime.getTimeInMillis() - now.getTimeInMillis());  
				model.addAttribute("leftms", leftms);
				model.addAttribute("endtime",strendtime1);
				
				Examinfo thisexaminfo=examService.getexambyid(examid);
				model.addAttribute("thisexaminfo",thisexaminfo);
				
			return "doingexam";
		}
		
		//做完卷到过渡页面
		@RequestMapping(value = { "/FinishExam" })
		public String FinishExam(HttpServletRequest request,Model model) throws ParseException {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			int examid=(int) request.getSession().getAttribute("examid");
			model.addAttribute("examid", examid);
			
			//做没做
			int[] upcqdo=(int[]) request.getSession().getAttribute("upcqdo");	
			int[] upjqdo=(int[]) request.getSession().getAttribute("upjqdo");	
			int[] upfqdo=(int[]) request.getSession().getAttribute("upfqdo");	
			int[] upbqdo=(int[]) request.getSession().getAttribute("upbqdo");	
			boolean finished=true;
			for (int i:upcqdo) {
				if(i==0) {
					finished=false;
				}
			}
			for (int i:upjqdo) {
				if(i==0) {
					finished=false;
				}
			}
			for (int i:upfqdo) {
				if(i==0) {
					finished=false;
				}
			}
			for (int i:upbqdo) {
				if(i==0) {
					finished=false;
				}
			}
			if(finished==false) {
				//取侧边栏
				int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
				model.addAttribute("upcqlist", upcqlist);
				int[]  upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
				model.addAttribute("upjqlist", upjqlist);
				int[]  upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
				model.addAttribute("upfqlist", upfqlist);
				int[]  upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
				model.addAttribute("upbqlist", upbqlist);
					//做没做
				
				model.addAttribute("upcqdo", upcqdo);
				
				model.addAttribute("upjqdo", upjqdo);
					
				model.addAttribute("upfqdo", upfqdo);
					
				model.addAttribute("upbqdo", upbqdo);
					//总分
				int totalpoint=(int) request.getSession().getAttribute("totalpoint");
				model.addAttribute("totalpoint", totalpoint);
				//考试id
				
				model.addAttribute("examid", examid);
				
				//看本页显示哪一道题
				String strqid= request.getParameter("qid"); 
				int qid;
				int qtype;
				if(strqid!=null) {
					qid=Integer.parseInt(strqid);
					String strqtype= request.getParameter("qtype"); 
					qtype=Integer.parseInt(strqtype);
				}
				else {
					if (upcqlist.length!=0) {qid=upcqlist[0];qtype=0;}
					else if (upjqlist.length!=0) {qid=upjqlist[0];qtype=1;}
					else if (upfqlist.length!=0) {qid=upfqlist[0];qtype=2;}
					else {qid=upbqlist[0];qtype=3;}
				}
				
				model.addAttribute("qtype", qtype);
			//根据题型找到题目信息(这一页显示什么东西)，并且看这个题做没做
				//是选择题
			if (qtype==0) {
				model.addAttribute("qinfo", questionbankService.getNewchoiceq(qid));
				model.addAttribute("aqinfo",examService.getAcqduringExam(examid, usermail, qid));
				}
				//是判断题
			if (qtype==1) {
				model.addAttribute("qinfo", questionbankService.getjq(qid));
				model.addAttribute("aqinfo",examService.getAjqduringExam(examid, usermail, qid));
				}
				//是填空题
			if (qtype==2) {
				model.addAttribute("qinfo", questionbankService.getfq(qid));
				model.addAttribute("aqinfo",examService.getAfqduringExam(examid, usermail, qid));
				}
				//是大题
			if (qtype==3) {
				model.addAttribute("qinfo", questionbankService.getNewbigq(qid));
				model.addAttribute("aqinfo",examService.getAbqduringExam(examid, usermail, qid));
				}
			
			//自动提交
			String strendtime2 = (String)request.getSession().getAttribute("strendtime2");
			String strendtime1 = (String)request.getSession().getAttribute("strendtime1");
			Calendar endtime=examService.transtime(strendtime2);
			Calendar now=Calendar.getInstance();
			int leftms=(int) (endtime.getTimeInMillis() - now.getTimeInMillis());  
			model.addAttribute("leftms", leftms);
			model.addAttribute("endtime",strendtime1);
				model.addAttribute("warning", "还有试题未作答，请继续认真作答");
				Examinfo thisexaminfo=examService.getexambyid(examid);
				model.addAttribute("thisexaminfo",thisexaminfo);
				return "doingexam";
			}
			
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
			int[] upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
			int[] upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
			ArrayList<Integer> Acqlist=examService.transLtoA(upcqlist);
			ArrayList<Integer> Ajqlist=examService.transLtoA(upjqlist);
			ArrayList<Integer> Afqlist=examService.transLtoA(upfqlist);
			
			ArrayList<Choice> cqinfolist=examService.getCqofexam(Acqlist);
			ArrayList<Jugementinfo> jqinfolist=examService.getJqofexam(Ajqlist);
			ArrayList<Fillinquestion> fqinfolist=examService.getFqofexam(Afqlist);
			
			float totalcq=examService.Getacqlistinfo(cqinfolist, examid, usermail);
			float totaljq=examService.Getajqlistinfo(jqinfolist, examid, usermail);
			float totalfq=examService.Getafqlistinfo(fqinfolist, examid, usermail);
			float partscore=totalcq+totaljq+totalfq;
			//我得更新一下，大题的分数为-1，因为null不能用，会报错
			int[] upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
			ArrayList<Integer> Abqlist=examService.transLtoA(upbqlist);
			examService.Getabqlistinfo(Abqlist, examid, usermail);
			
			model.addAttribute("upcqlist", upcqlist);

			model.addAttribute("upjqlist", upjqlist);
	
			model.addAttribute("upfqlist", upfqlist);
	
			model.addAttribute("upbqlist", upbqlist);
			
			//更新客观题总分
			examService.updateparttotal(partscore,examid, usermail);
			model.addAttribute("partscore",partscore);
			return "transitionexam";
		}
		
		//做完卷到过渡页面（时间到了）
		@RequestMapping(value = { "/FinishExam1" })
		public String FinishExamtimeout(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			int examid=(int) request.getSession().getAttribute("examid");
			model.addAttribute("examid", examid);
			examService.updateDstate2(examid);    //时间到了需要把这个考试状态给更新了
			examService.updateexamState(3, examid);
			
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
			int[] upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
			int[] upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
			ArrayList<Integer> Acqlist=examService.transLtoA(upcqlist);
			ArrayList<Integer> Ajqlist=examService.transLtoA(upjqlist);
			ArrayList<Integer> Afqlist=examService.transLtoA(upfqlist);
			
			ArrayList<Choice> cqinfolist=examService.getCqofexam(Acqlist);
			ArrayList<Jugementinfo> jqinfolist=examService.getJqofexam(Ajqlist);
			ArrayList<Fillinquestion> fqinfolist=examService.getFqofexam(Afqlist);
			
			float totalcq=examService.Getacqlistinfo(cqinfolist, examid, usermail);
			float totaljq=examService.Getajqlistinfo(jqinfolist, examid, usermail);
			float totalfq=examService.Getafqlistinfo(fqinfolist, examid, usermail);
			float partscore=totalcq+totaljq+totalfq;
			//我得更新一下，大题的分数为-1，因为null不能用，会报错
			int[] upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
			ArrayList<Integer> Abqlist=examService.transLtoA(upbqlist);
			examService.Getabqlistinfo(Abqlist, examid, usermail);
			 //更新客观题总分
			examService.updateparttotal(partscore,examid, usermail);
			model.addAttribute("partscore",partscore);
			
			model.addAttribute("upcqlist", upcqlist);

			model.addAttribute("upjqlist", upjqlist);
	
			model.addAttribute("upfqlist", upfqlist);
	
			model.addAttribute("upbqlist", upbqlist);
			return "transitionexam";
		}
		
		//考完试立刻看试卷详情
		@RequestMapping(value = { "/didexamdetail1" })
		public String Seedidexam(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			int examid=(int) request.getSession().getAttribute("examid");
			model.addAttribute("examid", examid);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			model.addAttribute("thisexaminfo",thisexaminfo);
			
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
			int[] upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
			int[] upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
			int[] upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
			ArrayList<Integer> Acqlist=examService.transLtoA(upcqlist);
			ArrayList<Integer> Ajqlist=examService.transLtoA(upjqlist);
			ArrayList<Integer> Afqlist=examService.transLtoA(upfqlist);
			ArrayList<Integer> Abqlist=examService.transLtoA(upbqlist);
			
			ArrayList<Choice> cqinfolist=examService.getCqofexam(Acqlist);
			ArrayList<Jugementinfo> jqinfolist=examService.getJqofexam(Ajqlist);
			ArrayList<Fillinquestion> fqinfolist=examService.getFqofexam(Afqlist);
			ArrayList<Bigquestion> bqinfolist=examService.getBqofexam(Abqlist);
			
			ArrayList<Choiceanswered> acqinfolist=examService.Getacqinfo(cqinfolist, examid, usermail);
			ArrayList<Jugementans> ajqinfolist=examService.Getajqinfo(jqinfolist, examid, usermail);
			ArrayList<Fillinans> afqinfolist=examService.Getafqinfo(fqinfolist, examid, usermail);
			ArrayList<Answeredbigq> abqinfolist=examService.Getabqinfo(bqinfolist, examid, usermail);
			
			model.addAttribute("cqinfolist", cqinfolist);
			model.addAttribute("jqinfolist", jqinfolist);
			model.addAttribute("fqinfolist", fqinfolist);
			model.addAttribute("bqinfolist", bqinfolist);
			model.addAttribute("acqinfolist", acqinfolist);
			model.addAttribute("ajqinfolist", ajqinfolist);
			model.addAttribute("afqinfolist", afqinfolist);
			model.addAttribute("abqinfolist", abqinfolist);
			Distribution dinfo=examService.getDexaminfo(examid, usermail);
			model.addAttribute("absent",dinfo.getAbsent());
			model.addAttribute("partscore",dinfo.getTotalpoint());
			return "didexam";
		} 
		
		//老师批改试卷(从那个批改试卷那进去的）
		@RequestMapping(value = { "/corqexam1" })
		public String Corqidexam(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			request.getSession().setAttribute("finished",0);
			//考试id
			Integer examid = (Integer)request.getSession().getAttribute("examid");
			//大题题号列表
			int [] bqidlist=(int[]) request.getSession().getAttribute("bqidlist"); 
			String strbqindex= request.getParameter("qindex"); 
			int bqindex=Integer.parseInt(strbqindex);
			int bqid=bqidlist[bqindex];
			request.getSession().setAttribute("bqid",bqid); 
			//大题信息列表
			ArrayList<ArrayList<Integer>> alllist=examService.getbqaid(examid, bqid);
			ArrayList<Integer> aidlist=alllist.get(0);
			ArrayList<Integer> scorelist=alllist.get(1);
			model.addAttribute("scorelist",scorelist);
			model.addAttribute("aidlist",aidlist);
			//顺道存进session里
			request.getSession().setAttribute("aidlist",examService.transAtoL(aidlist));
			request.getSession().setAttribute("scorelist",examService.transAtoL(scorelist));
			
			//这一页该显示啥
			int indexa=examService.showindex(scorelist);
			if (indexa==-1) {
				indexa=0;
			}
			
			int aid=aidlist.get(indexa);
			Answeredbigq showqinfo=examService.getthisaqinfo(aid);
			model.addAttribute("stuanswer", showqinfo.getAnswer());
			model.addAttribute("stuanswerpic", showqinfo.getAnswerpic());
			model.addAttribute("getscore", showqinfo.getScore());
			model.addAttribute("comment", showqinfo.getComment());
			
			Bigquestion thisbq=examService.getthisbqinfo(bqid);
			model.addAttribute("qinfo", thisbq);
			int maxpoint=thisbq.getPoint();
			float predictscore=examService.predictbqscore(maxpoint,showqinfo.getAnswer(),thisbq.getAnswer());
			model.addAttribute("predictscore", predictscore);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			model.addAttribute("thisexaminfo", thisexaminfo);
			model.addAttribute("aid", aid);
			return "correctexam";	
		}
		
		//老师从批卷从侧栏点击
		@RequestMapping(value = { "/corqexam2" })
		public String Corqidexam2(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			//考试id
			Integer examid = (Integer)request.getSession().getAttribute("examid");
			//大题题号
			Integer bqid = (Integer)request.getSession().getAttribute("bqid");
			//显示的aid号
			String straid= request.getParameter("aid");
			int aid=Integer.parseInt(straid);
			Answeredbigq showqinfo=examService.getthisaqinfo(aid);
			model.addAttribute("stuanswer", showqinfo.getAnswer());
			model.addAttribute("stuanswerpic", showqinfo.getAnswerpic());
			model.addAttribute("getscore", showqinfo.getScore());
			model.addAttribute("comment", showqinfo.getComment());
			
			Bigquestion thisbq=examService.getthisbqinfo(bqid);
			model.addAttribute("qinfo", thisbq);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			model.addAttribute("thisexaminfo", thisexaminfo);
			int maxpoint=thisbq.getPoint();
			float predictscore=examService.predictbqscore(maxpoint,showqinfo.getAnswer(),thisbq.getAnswer());
			model.addAttribute("predictscore", predictscore);
			//侧栏
			int [] aidlist=(int[]) request.getSession().getAttribute("aidlist");
			int [] scorelist=(int[]) request.getSession().getAttribute("scorelist");
			model.addAttribute("scorelist",scorelist);
			model.addAttribute("aidlist",aidlist);
			
			//warning
			int finished=(int) request.getSession().getAttribute("finished");
			if (finished==1) {
				model.addAttribute("warning", "本题已批改完成，返回批改下一道？");
			}
			model.addAttribute("aid", aid);
			return "correctexam";
		}
		
		//老师提交一道题的分数
		@RequestMapping(value = { "/corqexam3" })
		public String Corqidexam3(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			//考试id
			Integer examid = (Integer)request.getSession().getAttribute("examid");
			//大题题号
			Integer bqid = (Integer)request.getSession().getAttribute("bqid");
			//侧栏
			int [] aidlist=(int[]) request.getSession().getAttribute("aidlist");
			int [] scorelist=(int[]) request.getSession().getAttribute("scorelist");
			String strgetscore=request.getParameter("getscore");
			System.out.println(strgetscore);
			float getscore=Float.parseFloat(strgetscore);
			String comment=request.getParameter("comment");
			//aid
			String straid= request.getParameter("aid");
			int aid=Integer.parseInt(straid);
			//更新数据库
			examService.Corbq(aid, getscore, comment);
			//更新侧栏
			ArrayList<int[]> needinfos=examService.updateColumn(aid, getscore, scorelist, aidlist);
			int [] newscorelist=needinfos.get(0);
			request.getSession().setAttribute("scorelist",newscorelist);
			model.addAttribute("scorelist",newscorelist);
			model.addAttribute("aidlist",aidlist);	
			int finished=needinfos.get(1)[0];
			//这一页该显示啥
			int indexa=examService.showindex(examService.transLtoA(newscorelist));
			if (indexa!=-1) {
				aid=aidlist[indexa];
			}
			model.addAttribute("aid",aid);
			
			Answeredbigq showqinfo=examService.getthisaqinfo(aid);
			model.addAttribute("stuanswer", showqinfo.getAnswer());
			model.addAttribute("stuanswerpic", showqinfo.getAnswerpic());
			model.addAttribute("getscore", showqinfo.getScore());
			model.addAttribute("comment", showqinfo.getComment());
			
			Bigquestion thisbq=examService.getthisbqinfo(bqid);
			model.addAttribute("qinfo", thisbq);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			model.addAttribute("thisexaminfo", thisexaminfo);
			int maxpoint=thisbq.getPoint();
			float predictscore=examService.predictbqscore(maxpoint,showqinfo.getAnswer(),thisbq.getAnswer());
			model.addAttribute("predictscore", predictscore);
			//warning
			request.getSession().setAttribute("finished",finished);
			if (finished==1) {
				model.addAttribute("warning", "本题已批改完成，返回批改下一道？");
				examService.updateEQ(examid, bqid);
			}
	
			return "correctexam";
		}
		
		@RequestMapping(value = { "/passexam" })
		public String AdminpassExam(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			String strexamid= request.getParameter("examid"); 
			int examid=Integer.parseInt(strexamid);
			request.getSession().setAttribute("examid",examid);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			String strexamState=request.getParameter("examState"); 
			int examState=Integer.parseInt(strexamState);
			if (examState<100)
			{examService.updateexamState(examState, examid);
			String comment=request.getParameter("comment"); 
			examService.checkexam(usermail, examid, comment, examState);}
			model.addAttribute("examState", examState);
			//这场考试的班级
			ArrayList<Integer> classofexam=examService.getclassofexam(examid);
			if (examState==1) {
				examService.distributeexam(classofexam, examid);
			}
			if (examState>=105) {
				Random rand = new Random();
				int a=rand.nextInt(10);
				if (a<2) {
					model.addAttribute("comment","此项审核不合格");
				}
				else {
					model.addAttribute("comment","此项审核合格");
				}
				model.addAttribute("examState", 0);
			}
			
			ArrayList<String> classnames=examService.getclassnames2(classofexam);
			model.addAttribute("thisexaminfo", thisexaminfo);
			
			model.addAttribute("classofexam", classofexam);
			model.addAttribute("classnames", classnames);
			
			ArrayList<ArrayList<Integer>> allqidlist=examService.getexamqlist(examid);
			//选择题信息
			ArrayList<Choice> cqlist=examService.getCqofexam(allqidlist.get(0));
			//判断题信息
			ArrayList<Jugementinfo> jqlist=examService.getJqofexam(allqidlist.get(1));
			//填空题信息
			ArrayList<Fillinquestion> fqlist=examService.getFqofexam(allqidlist.get(2));
			//简答题信息
			ArrayList<Bigquestion> bqlist=examService.getBqofexam(allqidlist.get(3));
			model.addAttribute("cqlist", cqlist);
			model.addAttribute("jqlist", jqlist);
			model.addAttribute("fqlist", fqlist);
			model.addAttribute("bqlist", bqlist);
			model.addAttribute("bqcorlist", allqidlist.get(4));	
			
			return "examdetail";
		}
		
		//查看本场考试每个人的试卷（直接从考试详情看）
		@RequestMapping(value = { "/seedidexams1" })
		public String Everyonedidexam(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			//如果是学生
			if (usertype==1) {
				String strexamid= request.getParameter("examid"); 
				int examid=Integer.parseInt(strexamid);
				Examinfo thisexaminfo=examService.getexambyid(examid);
				model.addAttribute("thisexaminfo",thisexaminfo);
				model.addAttribute("examid",examid);
				ArrayList<ArrayList<Integer>> examqidlist=examService.getqofexam(examid);
				
				ArrayList<Choice> cqinfolist=examService.getCqofexam(examqidlist.get(0));
				ArrayList<Jugementinfo> jqinfolist=examService.getJqofexam(examqidlist.get(1));
				ArrayList<Fillinquestion> fqinfolist=examService.getFqofexam(examqidlist.get(2));
				ArrayList<Bigquestion> bqinfolist=examService.getBqofexam(examqidlist.get(3));
				
				model.addAttribute("cqinfolist", cqinfolist);
				model.addAttribute("jqinfolist", jqinfolist);
				model.addAttribute("fqinfolist", fqinfolist);
				model.addAttribute("bqinfolist", bqinfolist);
				
				Distribution thisd=examService.getDexaminfo(examid, usermail);
				
				int absent=thisd.getAbsent();
				if (absent==1) {
					model.addAttribute("noinfo",1);
					model.addAttribute("warning","您缺席了这场考试，请联系教师");
					return "didexam";
				}
				if (absent==5) {
					model.addAttribute("noinfo",1);
					model.addAttribute("warning","您的这场考试异常，请联系教师");
					return "didexam";
				}
				float partscore=thisd.getTotalpoint();
				model.addAttribute("absent",absent);
				model.addAttribute("partscore", partscore);
				ArrayList<Choiceanswered> acqinfolist=examService.Getacqinfo(cqinfolist, examid, usermail);
				ArrayList<Jugementans> ajqinfolist=examService.Getajqinfo(jqinfolist, examid, usermail);
				ArrayList<Fillinans> afqinfolist=examService.Getafqinfo(fqinfolist, examid, usermail);
				ArrayList<Answeredbigq> abqinfolist=examService.Getabqinfo(bqinfolist, examid, usermail);
				
				model.addAttribute("acqinfolist", acqinfolist);
				model.addAttribute("ajqinfolist", ajqinfolist);
				model.addAttribute("afqinfolist", afqinfolist);
				model.addAttribute("abqinfolist", abqinfolist);
				
				return "didexam";
			}
			//是老师
			model.addAttribute("noinfo",1);
			String strexamid= request.getParameter("examid"); 
			int examid=Integer.parseInt(strexamid);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			model.addAttribute("thisexaminfo",thisexaminfo);
			model.addAttribute("examid",examid);
			ArrayList<ArrayList<Integer>> dinfos=examService.getdidlist(examid);
			//侧栏
			model.addAttribute("didlist", dinfos.get(0));
			model.addAttribute("absentlist", dinfos.get(1));
			model.addAttribute("nowscorelist", dinfos.get(2));
			
			int[] didlist=examService.transAtoL(dinfos.get(0));
			int[] absentlist=examService.transAtoL(dinfos.get(1));
			int[] nowscorelist=examService.transAtoL(dinfos.get(2));
			request.getSession().setAttribute("didlist", didlist);	
			request.getSession().setAttribute("absentlist",absentlist);	
			request.getSession().setAttribute("nowscorelist", nowscorelist);	
		
			ArrayList<ArrayList<Integer>> examqidlist=examService.getqofexam(examid);
			int[] upcqlist=examService.transAtoL(examqidlist.get(0));
			int[] upjqlist=examService.transAtoL(examqidlist.get(1));
			int[] upfqlist=examService.transAtoL(examqidlist.get(2));
			int[] upbqlist=examService.transAtoL(examqidlist.get(3));
			
			request.getSession().setAttribute("upcqlist", upcqlist);	
			request.getSession().setAttribute("upjqlist", upjqlist);	
			request.getSession().setAttribute("upfqlist", upfqlist);	
			request.getSession().setAttribute("upbqlist", upbqlist);
			
			ArrayList<Choice> cqinfolist=examService.getCqofexam(examqidlist.get(0));
			ArrayList<Jugementinfo> jqinfolist=examService.getJqofexam(examqidlist.get(1));
			ArrayList<Fillinquestion> fqinfolist=examService.getFqofexam(examqidlist.get(2));
			ArrayList<Bigquestion> bqinfolist=examService.getBqofexam(examqidlist.get(3));
			
			
			model.addAttribute("cqinfolist", cqinfolist);
			model.addAttribute("jqinfolist", jqinfolist);
			model.addAttribute("fqinfolist", fqinfolist);
			model.addAttribute("bqinfolist", bqinfolist);
			
			return "didexam";
		} 
		
		//查看本场考试每个人的试卷（从看所有人点着看）
		@RequestMapping(value = { "/seedidexams2" })
		public String Everyonedidexam2(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			String strexamid= request.getParameter("examid"); 
			int examid=Integer.parseInt(strexamid);
			model.addAttribute("examid",examid);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			model.addAttribute("thisexaminfo",thisexaminfo);
			
			//取侧边栏
			int[] upcqlist=(int[]) request.getSession().getAttribute("upcqlist");	
			int[]  upjqlist=(int[]) request.getSession().getAttribute("upjqlist");	
			int[]  upfqlist=(int[]) request.getSession().getAttribute("upfqlist");	
			int[]  upbqlist=(int[]) request.getSession().getAttribute("upbqlist");	
			int[] didlist=(int[]) request.getSession().getAttribute("didlist");	
			int[] absentlist=(int[]) request.getSession().getAttribute("absentlist");	
			int[] nowscorelist=(int[]) request.getSession().getAttribute("nowscorelist");	
			
			model.addAttribute("didlist", didlist);
			model.addAttribute("absentlist", absentlist);
			model.addAttribute("nowscorelist", nowscorelist);
			
			ArrayList<Integer> Acqlist=examService.transLtoA(upcqlist);
			ArrayList<Integer> Ajqlist=examService.transLtoA(upjqlist);
			ArrayList<Integer> Afqlist=examService.transLtoA(upfqlist);
			ArrayList<Integer> Abqlist=examService.transLtoA(upbqlist);
			
			ArrayList<Choice> cqinfolist=examService.getCqofexam(Acqlist);
			ArrayList<Jugementinfo> jqinfolist=examService.getJqofexam(Ajqlist);
			ArrayList<Fillinquestion> fqinfolist=examService.getFqofexam(Afqlist);
			ArrayList<Bigquestion> bqinfolist=examService.getBqofexam(Abqlist);
			
			model.addAttribute("cqinfolist", cqinfolist);
			model.addAttribute("jqinfolist", jqinfolist);
			model.addAttribute("fqinfolist", fqinfolist);
			model.addAttribute("bqinfolist", bqinfolist);
			
			String strdid=request.getParameter("did");
			if (strdid==null) {
				model.addAttribute("noinfo",1);
				return "didexam";
			}
			int did=Integer.parseInt(strdid);
			Distribution thisd=examService.thisd(did);
			String stumail=thisd.getStudentemail();
			int absent=thisd.getAbsent();
			model.addAttribute("absent",absent);
			ArrayList<Choiceanswered> acqinfolist=examService.Getacqinfo(cqinfolist, examid, stumail);
			ArrayList<Jugementans> ajqinfolist=examService.Getajqinfo(jqinfolist, examid, stumail);
			ArrayList<Fillinans> afqinfolist=examService.Getafqinfo(fqinfolist, examid, stumail);
			ArrayList<Answeredbigq> abqinfolist=examService.Getabqinfo(bqinfolist, examid, stumail);
			
			model.addAttribute("acqinfolist", acqinfolist);
			model.addAttribute("ajqinfolist", ajqinfolist);
			model.addAttribute("afqinfolist", afqinfolist);
			model.addAttribute("abqinfolist", abqinfolist);
			
			model.addAttribute("thisd",thisd);
			return "didexam";
		}
		
		//查看本场考试题目的情况
		@RequestMapping(value = { "/seeqexam" })
		public String seeqscore(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			String strqid=request.getParameter("qid");
			String strqtype=request.getParameter("qtype");
			int qid=Integer.parseInt(strqid);
			int qtype=Integer.parseInt(strqtype);
			
			int examid=(int) request.getSession().getAttribute("examid");
			model.addAttribute("qtype",qtype);
			
			//根据题型找到题目信息(这一页显示什么东西)，并且看这个题做没做
			//是选择题
		if (qtype==0) {
			Choice qinfo=questionbankService.getNewchoiceq(qid);
			model.addAttribute("qinfo", qinfo);
			int maxscore=qinfo.getPoint();
			ArrayList<Integer> someinfo=examService.analycqinfo(examid, qid, maxscore);
			float scorerate;
			if (someinfo.get(1)!=0) {scorerate=(float)someinfo.get(0)/(float)someinfo.get(1);}
			else {scorerate=0;}
			model.addAttribute("scorerate",scorerate);  //得分率
			int answerA=someinfo.get(2);
			float avescore=scorerate*(float)maxscore;
			model.addAttribute("avescore",avescore);  //均分
			System.out.println(someinfo.get(0));
			System.out.println(someinfo.get(1));
			System.out.println(avescore);
			System.out.println(scorerate);
			System.out.println(maxscore);
			model.addAttribute("answerA",answerA);    //选A的人数
			int answerB=someinfo.get(3);
			model.addAttribute("answerB",answerB);    //选B的人数
			int answerC=someinfo.get(4);
			model.addAttribute("answerC",answerC);    //选C的人数
			int answerD=someinfo.get(5);
			model.addAttribute("answerD",answerD);    //选D的人数
			int noanswer=someinfo.get(6);
			model.addAttribute("noanswer",noanswer);    //没答案的人数
			int cornumber=someinfo.get(7);
			model.addAttribute("cornumber",cornumber);    //作对的人数
			int wrongnumber=someinfo.get(8);
			model.addAttribute("wrongnumber",wrongnumber);    //做错的人数
			int allnumber=wrongnumber+cornumber;
			model.addAttribute("allnumber",allnumber);
			if (usertype==1) {
				Distribution thisd=examService.getDexaminfo(examid, usermail);
				int absent=thisd.getAbsent();
				if (absent==0 || absent==3) {
				model.addAttribute("aqinfo",examService.getAcqduringExam(examid, usermail, qid));
				}
				else {
					model.addAttribute("absent", 1);
				}
			}
			}
			//是判断题
		if (qtype==1) {
			Jugementinfo qinfo=questionbankService.getjq(qid);
			model.addAttribute("qinfo", qinfo);
			int maxscore=qinfo.getPoint();
			ArrayList<Integer> someinfo=examService.analyjqinfo(examid, qid, maxscore);
			float scorerate;
			if (someinfo.get(1)!=0) {scorerate=(float)someinfo.get(0)/(float)someinfo.get(1);}
			else {scorerate=0;}
			model.addAttribute("scorerate",scorerate);  //得分率
			float avescore=scorerate*(float)maxscore;
			model.addAttribute("avescore",avescore);  //均分
			int answerT=someinfo.get(2);
			model.addAttribute("answerT",answerT);    //选T的人数
			int answerF=someinfo.get(3);
			model.addAttribute("answerF",answerF);    //选F的人数
			int noanswer=someinfo.get(4);
			model.addAttribute("noanswer",noanswer);    //没答案的人数
			int cornumber=someinfo.get(5);
			model.addAttribute("cornumber",cornumber);    //作对的人数
			int wrongnumber=someinfo.get(6);
			model.addAttribute("wrongnumber",wrongnumber);    //做错的人数
			int allnumber=wrongnumber+cornumber;
			model.addAttribute("allnumber",allnumber);
			if (usertype==1) {
				Distribution thisd=examService.getDexaminfo(examid, usermail);
				int absent=thisd.getAbsent();
				if (absent==0 || absent==3) {
					model.addAttribute("aqinfo",examService.getAjqduringExam(examid, usermail, qid));
				}
				else {
					model.addAttribute("absent", 1);
				}
			}
			}
			//是填空题
		if (qtype==2) {
			System.out.println(qid);
			Fillinquestion qinfo=questionbankService.getfq(qid);
			model.addAttribute("qinfo", qinfo);
			int maxscore=qinfo.getPoint();
			Forfill fillinfos=examService.analyfqinfo(examid, qid, maxscore);
			ArrayList<Integer> someinfo=fillinfos.getSomeinfo();
			ArrayList<String> stuanswers=fillinfos.getStuanswers();
			float scorerate;
			if (someinfo.get(1)!=0) {scorerate=(float)someinfo.get(0)/(float)someinfo.get(1);}
			else {scorerate=0;}
			model.addAttribute("scorerate",scorerate);  //得分率
			float avescore=scorerate*(float)maxscore;
			model.addAttribute("avescore",avescore);  //均分
			int noanswer=someinfo.get(2);
			model.addAttribute("noanswer",noanswer);    //没答案的人数
			int cornumber=someinfo.get(3);
			model.addAttribute("cornumber",cornumber);    //作对的人数
			int wrongnumber=someinfo.get(4);
			model.addAttribute("wrongnumber",wrongnumber);    //做错的人数
			model.addAttribute("stuanswers", stuanswers);   //学生的答案们
			int allnumber=wrongnumber+cornumber;
			model.addAttribute("allnumber",allnumber);
			if (usertype==1) {
				Distribution thisd=examService.getDexaminfo(examid, usermail);
				int absent=thisd.getAbsent();
				if (absent==0 || absent==3) {
				model.addAttribute("aqinfo",examService.getAfqduringExam(examid, usermail, qid));
				System.out.println(examService.getAfqduringExam(examid, usermail, qid));
				System.out.println(qid);
				}
				else {
					model.addAttribute("absent", 1);
				}
			}
			
			}
			//是大题
		if (qtype==3) {
			Bigquestion qinfo=questionbankService.getNewbigq(qid);
			model.addAttribute("qinfo", qinfo);
			int maxscore=qinfo.getPoint();
			Forbig biginfos=examService.analybqinfo(examid, qid, maxscore);
			float scorerate=biginfos.getScorerate();
			model.addAttribute("scorerate",scorerate);  //得分率
			float avescore=scorerate*maxscore;
			model.addAttribute("avescore",avescore);  //均分
			ArrayList<Float> stugetscore=biginfos.getStugetscore();
			model.addAttribute("stugetscore", stugetscore);    //每个人的得分
			ArrayList<String> goodanswers=biginfos.getGoodanswers();
			model.addAttribute("goodanswers", goodanswers);   //优秀作答
			ArrayList<String> goodanswerpics=biginfos.getAnswerpics();
			model.addAttribute("goodanswerpics", goodanswerpics);  //优秀作答图片
			ArrayList<String> goodmails=biginfos.getGoodmail();
			model.addAttribute("goodmails", goodmails);  //优秀作答学生邮箱
			ArrayList<Float> goodscores=biginfos.getGoodgetscore();
			model.addAttribute("goodscores", goodscores);   //优秀作答分数
			model.addAttribute("cornumber",scorerate*100.0);
			model.addAttribute("allnumber",100);
			if (usertype==1) {
				Distribution thisd=examService.getDexaminfo(examid, usermail);
				int absent=thisd.getAbsent();
				if (absent==0 || absent==3) {
				model.addAttribute("aqinfo",examService.getAbqduringExam(examid, usermail, qid));
				}
				else {
					model.addAttribute("absent", 1);
				}
			}
			}
			
			return "seeqexam";
		}
		
		@RequestMapping(value = { "/seewrongqs" })
		public String seewrongqexam(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			String strexamid= request.getParameter("examid"); 
			int examid=Integer.parseInt(strexamid);
			Examinfo thisexaminfo=examService.getexambyid(examid);
			model.addAttribute("thisexaminfo",thisexaminfo);
			model.addAttribute("examid",examid);
			ArrayList<ArrayList<Integer>> examqidlist=examService.getqofexam(examid);
			
			Distribution thisd=examService.getDexaminfo(examid, usermail);
			
			ArrayList<Choice> cqinfolist=examService.getCqofexam(examqidlist.get(0));
			ArrayList<Jugementinfo> jqinfolist=examService.getJqofexam(examqidlist.get(1));
			ArrayList<Fillinquestion> fqinfolist=examService.getFqofexam(examqidlist.get(2));
			
			ArrayList<Choiceanswered> acqinfolist=examService.Getacqinfo(cqinfolist, examid, usermail);
			ArrayList<Jugementans> ajqinfolist=examService.Getajqinfo(jqinfolist, examid, usermail);
			ArrayList<Fillinans> afqinfolist=examService.Getafqinfo(fqinfolist, examid, usermail);
			
			 //筛选错题
			examService.getwrongbq(cqinfolist, acqinfolist, jqinfolist, ajqinfolist, fqinfolist, afqinfolist);
			
			model.addAttribute("cqinfolist", cqinfolist);
			model.addAttribute("jqinfolist", jqinfolist);
			model.addAttribute("fqinfolist", fqinfolist);
			
			model.addAttribute("acqinfolist", acqinfolist);
			model.addAttribute("ajqinfolist", ajqinfolist);
			model.addAttribute("afqinfolist", afqinfolist);
			
			int absent=thisd.getAbsent();
			if (absent==1) {
				model.addAttribute("noinfo",1);
				model.addAttribute("warning","您缺席了这场考试，请联系教师");
				return "didexam2";
			}
			if (absent==5) {
				model.addAttribute("noinfo",1);
				model.addAttribute("warning","您的这场考试异常，请联系教师");
				return "didexam2";
			}
			float partscore=thisd.getTotalpoint();
			model.addAttribute("absent",absent);
			model.addAttribute("partscore", partscore);

			return "didexam";
			
		} 
		
		
}
