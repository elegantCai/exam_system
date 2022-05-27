package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.entity.Fillinans;
import com.example.demo.entity.Fillinquestion;
import com.example.demo.entity.Jugementans;
import com.example.demo.entity.Jugementinfo;
import com.example.demo.service.QuestionbankService;
import com.example.demo.service.UploadfileService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionbankController {
	@Autowired
    private QuestionbankService questionbankService;
	@Autowired
    private UploadfileService uploadfileService;
	
	//去往题库的页面        
		@RequestMapping(value = { "/questionbankpage" })
		public String gotoqb(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			return "questionbank";
		}
	
	//去往上传题目的页面        
	@RequestMapping(value = { "/Addquestion" })
	public String addquestion(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		return "upquestion";
	}
	
	//选择上传什么题型
	@RequestMapping(value = { "/Addingquestion1" })
	public String addquestion_choose(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		String strqtype= request.getParameter("qtype");  //题型
		int qtype=Integer.parseInt(strqtype);
		model.addAttribute("qtype", qtype);
		
		//把上传的题型存到session里
		request.getSession().setAttribute("qtype", qtype);	
		return "upquestion";
	}
	
	//真正开始上传题目
	@RequestMapping(value = { "/Addingquestion2" },method = RequestMethod.POST)
	public String addquestion_content(HttpServletRequest request,Model model,
			@RequestParam("answerpic")MultipartFile answerpic,@RequestParam("picture")MultipartFile picture) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		//获取上传的题型
		int qtype = (int)request.getSession().getAttribute("qtype");
		model.addAttribute("qtype", qtype);
		//存在哪种题的文件夹
		String filedic=uploadfileService.getfiledic(qtype);
		
		//获取题目信息
		String subject= request.getParameter("subject");
		String strpoint= request.getParameter("point");
		String content= request.getParameter("content");
		String difficultylevel=request.getParameter("difficulty");
		String answer=request.getParameter("answer");
		int point=Integer.parseInt(strpoint);
		//如果是选择题还得获取选项
		String optiona=request.getParameter("optiona");
		String optionb=request.getParameter("optionb");
		String optionc=request.getParameter("optionc");
		String optiond=request.getParameter("optiond");
		float difficulty=questionbankService.transdifficulty(difficultylevel);    //这是转化的difficulty
		model.addAttribute("difficulty", difficulty);
		
		//插入文字信息并返回ID
		int newid=questionbankService.insertNewQuestionC(qtype, 0, answer, content, difficulty, point, subject, usermail,optiona,optionb,optionc,optiond);  //这是题库，所以不是考试
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
		
		return "upquestionmid";
	}
	
	@RequestMapping(value = { "/backqbank" },method = RequestMethod.POST)
	public String bankquestionbank(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		//把题型信息给删了		
		request.getSession().getAttribute("qtype");
		return "questionbank"; 
		}
	
	@RequestMapping(value = { "/qb_searchq" })
	public String searchq(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		String subject= request.getParameter("subject");
		String content= request.getParameter("content");
		model.addAttribute("subject", subject);
		model.addAttribute("content", content);
		String stype= request.getParameter("stype");
		String sortype= request.getParameter("sortype");
		
		//要什么题型
		String choiceq= request.getParameter("choiceq");
		String judq= request.getParameter("judq");
		String fillq= request.getParameter("fillq");
		String bigq= request.getParameter("bigq");
		
		//需不需要选择题
		int wchoiceq=questionbankService.transString(choiceq);
		model.addAttribute("choiceq", wchoiceq);
		if(wchoiceq==1) {
			model.addAttribute("choiceqlist", questionbankService.getSearchCq(subject, content, stype, sortype,usertype));
			System.out.println(questionbankService.getSearchCq(subject, content, stype, sortype,usertype));
		}
		//需不需要判断题
		int wjudq=questionbankService.transString(judq);
		model.addAttribute("judq", wjudq);
		if(wjudq==1) {
			model.addAttribute("judgeqlist", questionbankService.getSearchJq(subject, content, stype, sortype,usertype));
		}
		//需不需要填空题
		int wfillq=questionbankService.transString(fillq);
		model.addAttribute("fillq", wfillq);
		if(wfillq==1) {
			model.addAttribute("fillqlist", questionbankService.getSearchFq(subject, content, stype, sortype,usertype));
		}
		//需不需要大题
		int wbigq=questionbankService.transString(bigq);
		model.addAttribute("bigq", wbigq);
		if(wbigq==1) {
			model.addAttribute("bigqlist", questionbankService.getSearchBq(subject, content, stype, sortype,usertype));
		}
		//如果啥都没点,啥都要
		if(wchoiceq==0 && wjudq==0 && wfillq==0 && wbigq==0) {
			model.addAttribute("choiceqlist", questionbankService.getSearchCq(subject, content, stype, sortype,usertype));
			model.addAttribute("judgeqlist", questionbankService.getSearchJq(subject, content, stype, sortype,usertype));
			model.addAttribute("fillqlist", questionbankService.getSearchFq(subject, content, stype, sortype,usertype));
			model.addAttribute("bigqlist", questionbankService.getSearchBq(subject, content, stype, sortype,usertype));
		}
		
		return "qb_search";
	}
	
	@RequestMapping(value = { "/qdetail" })
	public String qdetail(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		String strqtype=request.getParameter("qtype");
		String strqid=request.getParameter("qid");
		
		int qtype;int qid;
		String strrec=request.getParameter("recommend");
		if (strrec!=null) {
			ArrayList<Integer> qtypeid=new ArrayList<Integer>();
			qtypeid=questionbankService.forrec();
			if (qtypeid.get(0)==-1) {
				model.addAttribute("qwarning",0);
				return "questionbank";
				}
			qtype=qtypeid.get(0);
			qid=qtypeid.get(1);
		}
		else {
			qtype=Integer.parseInt(strqtype); 
			qid=Integer.parseInt(strqid); 
		} 
		
		model.addAttribute("qtype", qtype);
		model.addAttribute("qid", qid);
		
		//存在session里
		request.getSession().setAttribute("qtype", qtype);	
		request.getSession().setAttribute("qid", qid);
		
		//根据题型找到题目信息
			//是选择题
		if (qtype==0) {
			model.addAttribute("qinfo", questionbankService.getNewchoiceq(qid));
			}
			//是判断题
		if (qtype==1) {
			model.addAttribute("qinfo", questionbankService.getjq(qid));
			}
			//是填空题
		if (qtype==2) {
			model.addAttribute("qinfo", questionbankService.getfq(qid));
			}
			//是大题
		if (qtype==3) {
			model.addAttribute("qinfo", questionbankService.getNewbigq(qid));
			}		
		return "qb_detail";
	}
	
	
	//查看题目全部信息(作答后）
	@RequestMapping(value = { "/qdetail_doq" },method = RequestMethod.POST)
	public String qdetail_alldo(HttpServletRequest request,Model model,@RequestParam("stuanswerpic") MultipartFile stuanswerpic) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		//从session里取出来qid和qtype
		Integer qtype = (Integer)request.getSession().getAttribute("qtype");
		Integer qid = (Integer)request.getSession().getAttribute("qid");
		model.addAttribute("qtype", qtype);
		model.addAttribute("qid", qid);
		
		//显示分数等
		model.addAttribute("wshowalldo", 1);
		//显示答案等
		model.addAttribute("wshowall", 1);
		
		String stuanswer=request.getParameter("stuanswer");
		String realanswer;
		int point;
		//根据题型找到题目信息
			//是选择题
		if (qtype==0) {
			Choice thiscq=questionbankService.getNewchoiceq(qid);
			realanswer=thiscq.getAnswer();
			point=(int) thiscq.getPoint();
			model.addAttribute("qinfo", thiscq);
			}
			//是判断题
		else if (qtype==1) {
			Jugementinfo thisjq=questionbankService.getjq(qid);
			realanswer=thisjq.getAnswer();
			point=(int) thisjq.getPoint();
			model.addAttribute("qinfo", thisjq);
			}
			//是填空题
		else if (qtype==2) {
			Fillinquestion thisfq=questionbankService.getfq(qid);
			realanswer=thisfq.getAnswer();
			point=(int) thisfq.getPoint();
			model.addAttribute("qinfo", thisfq);
			}
			//是大题
		else  {
			Bigquestion thisbq = questionbankService.getNewbigq(qid);
			realanswer=thisbq.getAnswer();
			point=(int) thisbq.getPoint();
			model.addAttribute("qinfo", thisbq);
			}	
		
		//开始插做过的题的数据库
		float score=questionbankService.doexcercisescore(stuanswer, realanswer, point, qtype);
		int aid=questionbankService.doexercise(qtype, qid, stuanswer, usermail, realanswer, score);
		if (qtype==3) {
			String picpath=uploadfileService.getfilepath("bigq","answered",stuanswerpic,aid);
			uploadfileService.uploadfile(picpath,stuanswerpic);
			String relpicpath=uploadfileService.getrelpath("bigq", "answered",picpath);
			questionbankService.doexerciseinsertpic(aid, relpicpath);
			model.addAttribute("stuanswerpic", relpicpath);
		}
			//更新一下难度
		if(qtype!=3) {
			questionbankService.updateDiff(qid, qtype);
		}
		
		//把刚才的作答信息传递到前端
		model.addAttribute("stuanswer", stuanswer);
		model.addAttribute("myscore", score);
		
		//把刚才做的题的aid，qid存在session里，防止他要记笔记
		request.getSession().setAttribute("aid",aid);
		request.getSession().setAttribute("qid",qid);
		
		return "qb_detail";
	}
	
	//做完题之后记录笔记
	@RequestMapping(value = { "/qdetail_doq_doc" })
	public String qdetail_doandrrecordc(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		//获取刚刚作答的qid,aid
		Integer qid = (Integer)request.getSession().getAttribute("qid");
		Integer aid = (Integer)request.getSession().getAttribute("aid");
		Integer qtype = (Integer)request.getSession().getAttribute("qtype");   //之前存的
		
		model.addAttribute("qtype", qtype);
		
		//显示分数等
		model.addAttribute("wshowalldo", 1);
		//显示答案等
		model.addAttribute("wshowall", 1);
		//显示笔记
		model.addAttribute("wshowcomment", 1);
		
		//根据题型找到题目信息
			//是选择题
		if (qtype==0) {
				model.addAttribute("qinfo", questionbankService.getNewchoiceq(qid));
				Choiceanswered thisaq=questionbankService.getACqbyaid(aid);
				String stuanswer=thisaq.getAnswer();
				float score=thisaq.getScore();
				model.addAttribute("stuanswer", stuanswer);
				model.addAttribute("myscore", score);
			}
			//是判断题
		if (qtype==1) {
				model.addAttribute("qinfo", questionbankService.getjq(qid));
				Jugementans thisaq=questionbankService.getAJqbyaid(aid);
				String stuanswer=thisaq.getAnswer();
				float score=thisaq.getScore();
				model.addAttribute("stuanswer", stuanswer);
				model.addAttribute("myscore", score);
			}
			//是填空题
		if (qtype==2) {
				model.addAttribute("qinfo", questionbankService.getfq(qid));
				Fillinans thisaq=questionbankService.getAFqbyaid(aid);
				String stuanswer=thisaq.getAnswer();
				float score=thisaq.getScore();
				model.addAttribute("stuanswer", stuanswer);
				model.addAttribute("myscore", score);
			}
			//是大题
		if (qtype==3) {
				model.addAttribute("qinfo", questionbankService.getNewbigq(qid));
				Answeredbigq thisaq=questionbankService.getABqbyaid(aid);
				String stuanswer=thisaq.getAnswer();
				String stuanswerpic=thisaq.getAnswerpic();
				model.addAttribute("stuanswer", stuanswer);
				model.addAttribute("stuanswerpic", stuanswerpic);
				}	
		
		//记录笔记并回显
		String comment=request.getParameter("comment");
		questionbankService.insertcomment(qtype, comment, aid);
		model.addAttribute("comment", comment);
		return "qb_detail";
	}
	//查看题目全部信息(含解析、不作答）
	@RequestMapping(value = { "/qdetail_showall" })
	public String qdetail_all(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		//从session里取出来qid和qtype
		Integer qtype = (Integer)request.getSession().getAttribute("qtype");
		Integer qid = (Integer)request.getSession().getAttribute("qid");
		model.addAttribute("qtype", qtype);
		model.addAttribute("qid", qid);
		
		model.addAttribute("wshowall", 1);
		
		//根据题型找到题目信息并且做题(更新数据库）
		//是选择题
		if (qtype==0) {
			model.addAttribute("qinfo", questionbankService.getNewchoiceq(qid));
			}
		//是判断题
		if (qtype==1) {
			model.addAttribute("qinfo", questionbankService.getjq(qid));
			}
		//是填空题
		if (qtype==2) {
			model.addAttribute("qinfo", questionbankService.getfq(qid));
			}
		//是大题
		if (qtype==3) {
			model.addAttribute("qinfo", questionbankService.getNewbigq(qid));
			}	
		return "qb_detail";
	}
	
	//查看我做过的题目信息
	@RequestMapping(value = { "/myquestion_search" })
	public String mydidqs(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		String choiceq= request.getParameter("choiceq");
		String judq= request.getParameter("judq");
		String fillq= request.getParameter("fillq");
		String bigq= request.getParameter("bigq");
		int wcq=questionbankService.transString(choiceq);
		int wjq=questionbankService.transString(judq);
		int wfq=questionbankService.transString(fillq);
		int wbq=questionbankService.transString(bigq);
		System.out.println(wcq);
		System.out.println(wjq);
		System.out.println(wfq);
		System.out.println(wbq);
		if (wcq==0 && wcq==0 && wcq==0 && wcq==0) {
			wcq=1;wjq=1;wfq=1;wbq=1;
			}   //如果没选就默认全要
		
		String subject=request.getParameter("subject");
		ArrayList<ArrayList<Integer>> allmydidaid=questionbankService.getallmydidaid(usermail);
		ArrayList<ArrayList<Integer>> allmydidqid=questionbankService.getallmydid(usermail);
		
		ArrayList<Integer> cqlist=new ArrayList<Integer>();
		ArrayList<Integer> jqlist=new ArrayList<Integer>();
		ArrayList<Integer> fqlist=new ArrayList<Integer>();
		ArrayList<Integer> bqlist=new ArrayList<Integer>();
		
		ArrayList<Integer> calist=new ArrayList<Integer>();
		ArrayList<Integer> jalist=new ArrayList<Integer>();
		ArrayList<Integer> falist=new ArrayList<Integer>();
		ArrayList<Integer> balist=new ArrayList<Integer>();
		//前两个是没输入科目
		if (subject==null) {
			cqlist=allmydidqid.get(0);
			jqlist=allmydidqid.get(1);
			fqlist=allmydidqid.get(2);
			bqlist=allmydidqid.get(3);	
			calist=allmydidaid.get(0);
			jalist=allmydidaid.get(1);
			falist=allmydidaid.get(2);
			balist=allmydidaid.get(3);	
		}
		else if (subject.trim().length()<=0){
			cqlist=allmydidqid.get(0);
			jqlist=allmydidqid.get(1);
			fqlist=allmydidqid.get(2);
			bqlist=allmydidqid.get(3);	
			calist=allmydidaid.get(0);
			jalist=allmydidaid.get(1);
			falist=allmydidaid.get(2);
			balist=allmydidaid.get(3);	
		}
		else {
			ArrayList<ArrayList<Integer>> showqidandaid=questionbankService.getallmydidsubject(allmydidqid, usermail, subject, wcq, wjq, wfq, wbq, allmydidaid);
			calist=showqidandaid.get(4);
			jalist=showqidandaid.get(5);
			falist=showqidandaid.get(6);
			balist=showqidandaid.get(7);
			cqlist=showqidandaid.get(0);
			jqlist=showqidandaid.get(1);
			fqlist=showqidandaid.get(2);
			bqlist=showqidandaid.get(3);
		}
		model.addAttribute("calist", calist);
		model.addAttribute("jalist", jalist);
		model.addAttribute("falist", falist);
		model.addAttribute("balist", balist);
		//选择题
		ArrayList<Choice> cqinfolist=questionbankService.getneedmyCq(cqlist);
		//判断题
		ArrayList<Jugementinfo> jqinfolist=questionbankService.getneedmyJq(jqlist);
		//填空题
		ArrayList<Fillinquestion> fqinfolist=questionbankService.getneedmyFq(fqlist);
		//简答题	
		ArrayList<Bigquestion> bqinfolist=questionbankService.getneedmyBq(bqlist);
		
		model.addAttribute("cqinfolist",cqinfolist);
		model.addAttribute("jqinfolist",jqinfolist);
		model.addAttribute("fqinfolist",fqinfolist);
		model.addAttribute("bqinfolist",bqinfolist);
		return "mydidq";
	}
	
	//查看我做过的题的详细信息
	@RequestMapping(value = { "/myquestion_diddetail" })
	public String mydidqdetail(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		//获取刚刚作答的aid，qtype
		String straid=request.getParameter("aid");
		String strqtype=request.getParameter("qtype");
		int aid=Integer.parseInt(straid); 
		int qtype=Integer.parseInt(strqtype);
		model.addAttribute("qtype", qtype);
		//显示分数等
		model.addAttribute("wshowalldo", 1);
		//显示答案等
		model.addAttribute("wshowall", 1);
		//显示笔记
		model.addAttribute("wshowcomment", 1);
		//这是做过的题
		model.addAttribute("wshowdid", 1);
		
		//根据题型找到题目信息
			//是选择题
		if (qtype==0) {
				Choiceanswered thisaq=questionbankService.getACqbyaid(aid);
				int qid=thisaq.getQid();
				model.addAttribute("qinfo", questionbankService.getNewchoiceq(qid));
				String stuanswer=thisaq.getAnswer();
				float score=thisaq.getScore();
				int type=thisaq.getType();
				String comment=thisaq.getComment();
				model.addAttribute("type", type);
				model.addAttribute("stuanswer", stuanswer);
				model.addAttribute("myscore", score);
				model.addAttribute("comment", comment);
			}
			//是判断题
		if (qtype==1) {
				Jugementans thisaq=questionbankService.getAJqbyaid(aid);
				int qid=thisaq.getQid();
				model.addAttribute("qinfo", questionbankService.getjq(qid));
				String stuanswer=thisaq.getAnswer();
				float score=thisaq.getScore();
				String comment=thisaq.getComment();
				int type=thisaq.getType();
				model.addAttribute("type", type);
				model.addAttribute("stuanswer", stuanswer);
				model.addAttribute("myscore", score);
				model.addAttribute("comment", comment);
			}
			//是填空题
		if (qtype==2) {
				Fillinans thisaq=questionbankService.getAFqbyaid(aid);
				int qid=thisaq.getQid();
				model.addAttribute("qinfo", questionbankService.getfq(qid));
				String stuanswer=thisaq.getAnswer();
				float score=thisaq.getScore();
				String comment=thisaq.getComment();
				int type=thisaq.getType();
				model.addAttribute("type", type);
				model.addAttribute("stuanswer", stuanswer);
				model.addAttribute("myscore", score);
				model.addAttribute("comment", comment);
			}
			//是大题
		if (qtype==3) {
				Answeredbigq thisaq=questionbankService.getABqbyaid(aid);
				int qid=thisaq.getQid();
				model.addAttribute("qinfo", questionbankService.getNewbigq(qid));
				String stuanswer=thisaq.getAnswer();
				String stuanswerpic=thisaq.getAnswerpic();
				String comment=thisaq.getComment();
				int type=thisaq.getType();
				model.addAttribute("type", type);
				model.addAttribute("stuanswer", stuanswer);
				model.addAttribute("stuanswerpic", stuanswerpic);
				model.addAttribute("comment", comment);
				}	
		
		return "qb_detail";
	}
	
	//查看我做过的题的详细信息
		@RequestMapping(value = { "/check_q" })
		public String checkq(HttpServletRequest request,Model model) {
			String usermail = (String)request.getSession().getAttribute("usermail");
			Integer usertype = (Integer)request.getSession().getAttribute("usertype");
			model.addAttribute("usermail", usermail);
			model.addAttribute("usertype", usertype);
			
			//从session里取出来qid和qtype
			Integer qtype = (Integer)request.getSession().getAttribute("qtype");
			Integer qid = (Integer)request.getSession().getAttribute("qid");
			model.addAttribute("qtype", qtype);
			model.addAttribute("qid", qid);
			
			model.addAttribute("wshowall", 1);
			
			String strpass=request.getParameter("pass");
			int pass=Integer.parseInt(strpass);
			questionbankService.admincheck(qtype,qid,pass,usermail);
			model.addAttribute("newpass", pass);
			
			//根据题型找到题目信息并且做题(更新数据库）
			//是选择题
			if (qtype==0) {
				model.addAttribute("qinfo", questionbankService.getNewchoiceq(qid));
				}
			//是判断题
			if (qtype==1) {
				model.addAttribute("qinfo", questionbankService.getjq(qid));
				}
			//是填空题
			if (qtype==2) {
				model.addAttribute("qinfo", questionbankService.getfq(qid));
				}
			//是大题
			if (qtype==3) {
				model.addAttribute("qinfo", questionbankService.getNewbigq(qid));
				}	
			return "qb_detail";
		}
	//查看我出过的题的详细信息
	public String myupquestion(HttpServletRequest request,Model model) {
		
		return "pretendbegin";
	}
	
	@RequestMapping(value = { "/qanalyze" })
	public String qanalyze(HttpServletRequest request,Model model) {
		return "qanalyze";
	}
	
	
}
