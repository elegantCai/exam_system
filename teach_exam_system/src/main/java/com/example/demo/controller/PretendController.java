package com.example.demo.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Classes;
import com.example.demo.entity.Distribution;
import com.example.demo.entity.Examinfo;
import com.example.demo.service.ExamService;
import com.example.demo.service.PretendService;
import com.example.demo.service.StudentService;

import org.springframework.ui.Model;

@Controller
public class PretendController {
	@Autowired
    private PretendService pretendService;
	@Autowired
    private StudentService studentService;
	@Autowired
    private ExamService examService;
	
	@RequestMapping(value = { "/啊啊啊" })
	public String pretendbegin(HttpServletRequest request,Model model) {
		return "pretendbegin";
	}
	
	@RequestMapping(value = { "/main" })
	public String main(HttpServletRequest request,Model model) {
		return "main";
	}
	
	@RequestMapping(value = { "/personpage1" })
	public String pretendsign(HttpServletRequest request,Model model) {
		String usermail= request.getParameter("usermail");
		String strusertype= request.getParameter("usertype");
		int usertype=Integer.parseInt(strusertype);
		
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		pretendService.insertNewUserC(usertype, usermail);
		
		return "personpage";
	}
	
	@RequestMapping(value = { "/personpage2" })
	public String pretend(HttpServletRequest request,Model model) {
		String usermail= request.getParameter("usermail");
		String strusertype= request.getParameter("usertype");
		int usertype=Integer.parseInt(strusertype);
		
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
				
		return "personpage";
	}
	//////////////////////////////////////////******在此基础修改学生跳转界面*****//////////////////////////////////////////
	@RequestMapping(value = { "/personpage3" })
	public String pretendlogin(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		String username = studentService.getStudentnameByemail(usermail).get(0).getUsername();
		int gender = studentService.getStudentnameByemail(usermail).get(0).getUsergender();
		String useracademy = studentService.getStudentnameByemail(usermail).get(0).getUseracademy();
		String usergrade = studentService.getStudentnameByemail(usermail).get(0).getUsergrade();
		String usermajor = studentService.getStudentnameByemail(usermail).get(0).getUsermajor();
		String userheadshot = studentService.getStudentnameByemail(usermail).get(0).getUserheadshot();
		String usergender = "";
		if(gender==0) {
			usergender = "男";
		}
		else {
			usergender = "女";
		}
		
		int userid = studentService.getIDbyemailY(usermail);
		int num = studentService.classCount(userid);
		if(num>4) {
			num = 4;
		}

		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		request.getSession().setAttribute("username", username);	
		request.getSession().setAttribute("userid", userid);
		request.getSession().setAttribute("usergender", usergender);	
		request.getSession().setAttribute("useracademy", useracademy);
		request.getSession().setAttribute("usergrade", usergrade);	
		request.getSession().setAttribute("usermajor", usermajor);
		request.getSession().setAttribute("userheadshot", userheadshot);
		model.addAttribute("usermail", usermail);
		model.addAttribute("username", username);
		model.addAttribute("usertype", usertype);
		model.addAttribute("userid", userid);
		model.addAttribute("usergender", usergender);
		model.addAttribute("useracademy", useracademy);
		model.addAttribute("usergrade", usergrade);
		model.addAttribute("userheadshot", userheadshot);
		model.addAttribute("usermajor", usermajor);	

		//班级信息
		ArrayList<Integer> classid =studentService.getClassidByStudentidY(userid);
		ArrayList<Classes> classinfo =new ArrayList<Classes>();
		for(Integer i=0; i < num; i++) {
			classinfo.add(studentService.getClassInformationY(classid.get(i)));
    	}
		model.addAttribute("class_list", classinfo);

		//未开始的考试
		ArrayList<Integer> examid=studentService.getmyexamsidY(usermail, 2);
		int numexam = examid.size();
		if(numexam>4) {
			numexam = 4;
		}
		
		ArrayList<Examinfo> exam =new ArrayList<Examinfo>();
		for(Integer i=0; i < numexam; i++) {
			exam.add(studentService.getExamByIdY(examid.get(i)));
    	}
		model.addAttribute("exam", exam);
						
		//return "personpage";
		//return "studentPersonal";
		return "student_profile";
	}
	
	@RequestMapping(value = { "/personpage4" })
	public String pretendloginstu(HttpServletRequest request,Model model) {
		String usermail = (String)request.getSession().getAttribute("usermail");
		Integer usertype = 1;
		request.getSession().setAttribute("usertype",1);
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		int gender;
		if (studentService.getStudentnameByemail(usermail).size()==0) {
			
		}
		else {
		String username = studentService.getStudentnameByemail(usermail).get(0).getUsername();
		gender = studentService.getStudentnameByemail(usermail).get(0).getUsergender();
		String useracademy = studentService.getStudentnameByemail(usermail).get(0).getUseracademy();
		String usergrade = studentService.getStudentnameByemail(usermail).get(0).getUsergrade();
		String usermajor = studentService.getStudentnameByemail(usermail).get(0).getUsermajor();
		String userheadshot = studentService.getStudentnameByemail(usermail).get(0).getUserheadshot();
		String usergender = "";
		if(gender==0) {
			usergender = "男";
		}
		else {
			usergender = "女";
		}
		
		int userid = studentService.getIDbyemailY(usermail);
		int num = studentService.classCount(userid);
		if(num>4) {
			num = 4;
		}
		
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		request.getSession().setAttribute("username", username);	
		request.getSession().setAttribute("userid", userid);
		request.getSession().setAttribute("usergender", usergender);	
		request.getSession().setAttribute("useracademy", useracademy);
		request.getSession().setAttribute("usergrade", usergrade);	
		request.getSession().setAttribute("usermajor", usermajor);
		request.getSession().setAttribute("userheadshot", userheadshot);
		model.addAttribute("usermail", usermail);
		model.addAttribute("username", username);
		model.addAttribute("usertype", usertype);
		model.addAttribute("userid", userid);
		model.addAttribute("usergender", usergender);
		model.addAttribute("useracademy", useracademy);
		model.addAttribute("usergrade", usergrade);
		model.addAttribute("userheadshot", userheadshot);
		model.addAttribute("usermajor", usermajor);	

		//班级信息
		ArrayList<Integer> classid =studentService.getClassidByStudentidY(userid);
		ArrayList<Classes> classinfo =new ArrayList<Classes>();
		for(Integer i=0; i < num; i++) {
			classinfo.add(studentService.getClassInformationY(classid.get(i)));
    	}
		model.addAttribute("class_list", classinfo);

		//未开始的考试
		ArrayList<Integer> examid=studentService.getmyexamsidY(usermail, 2);
		int numexam = examid.size();
		if(numexam>4) {
			numexam = 4;
		}
		
		ArrayList<Examinfo> exam =new ArrayList<Examinfo>();
		for(Integer i=0; i < numexam; i++) {
			exam.add(studentService.getExamByIdY(examid.get(i)));
    	}
		model.addAttribute("exam", exam);}
						
		//return "personpage";
		//return "studentPersonal";
		return "student_profile";
	}
	
	@RequestMapping(value = { "/studentInformation" })
	public String studentInformation(HttpServletRequest request,Model model) {
		String usermail= (String) request.getSession().getAttribute("usermail");
		String username= (String) request.getSession().getAttribute("username");
		String id= String.valueOf(request.getSession().getAttribute("userid"));
		int userid=Integer.parseInt(id);
		String usergender= String.valueOf(request.getSession().getAttribute("usergender"));
		String useracademy= (String) request.getSession().getAttribute("useracademy");
		String usergrade= String.valueOf(request.getSession().getAttribute("usergrade"));
		String userheadshot= (String) request.getSession().getAttribute("userheadshot");
		String usermajor= String.valueOf(request.getSession().getAttribute("usermajor"));

		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usertype", usertype);
		
		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("username", username);	
		request.getSession().setAttribute("userid", userid);
		request.getSession().setAttribute("usergender", usergender);	
		request.getSession().setAttribute("useracademy", useracademy);
		request.getSession().setAttribute("usergrade", usergrade);	
		request.getSession().setAttribute("usermajor", usermajor);
		request.getSession().setAttribute("userheadshot", userheadshot);
		model.addAttribute("usermail", usermail);
		model.addAttribute("username", username);
		model.addAttribute("userid", userid);
		model.addAttribute("usergender", usergender);
		model.addAttribute("useracademy", useracademy);
		model.addAttribute("usergrade", usergrade);
		model.addAttribute("userheadshot", userheadshot);
		model.addAttribute("usermajor", usermajor);	
		return "student_information";
	}
	
	@RequestMapping(value = { "/personalStudent" })
	public String personalStudent(HttpServletRequest request,Model model) {
		String usermail= (String) request.getSession().getAttribute("usermail");
		String usertype= String.valueOf(request.getSession().getAttribute("usertype"));
		String username = studentService.getStudentnameByemail(usermail).get(0).getUsername();
		int gender = studentService.getStudentnameByemail(usermail).get(0).getUsergender();
		String useracademy = studentService.getStudentnameByemail(usermail).get(0).getUseracademy();
		String usergrade = studentService.getStudentnameByemail(usermail).get(0).getUsergrade();
		String usermajor = studentService.getStudentnameByemail(usermail).get(0).getUsermajor();
		String userheadshot = studentService.getStudentnameByemail(usermail).get(0).getUserheadshot();
		String usergender = "";
		if(gender==0) {
			usergender = "男";
		}
		else {
			usergender = "女";
		}

		int userid = studentService.getIDbyemailY(usermail);
		int num = studentService.classCount(userid);
		if(num>4) {
			num = 4;
		}

		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		request.getSession().setAttribute("username", username);	
		request.getSession().setAttribute("userid", userid);
		request.getSession().setAttribute("usergender", usergender);	
		request.getSession().setAttribute("useracademy", useracademy);
		request.getSession().setAttribute("usergrade", usergrade);	
		request.getSession().setAttribute("usermajor", usermajor);
		request.getSession().setAttribute("userheadshot", userheadshot);
		model.addAttribute("usermail", usermail);
		model.addAttribute("username", username);
		model.addAttribute("usertype", usertype);
		model.addAttribute("userid", userid);
		model.addAttribute("usergender", usergender);
		model.addAttribute("useracademy", useracademy);
		model.addAttribute("usergrade", usergrade);
		model.addAttribute("userheadshot", userheadshot);
		model.addAttribute("usermajor", usermajor);	

		//班级信息
		ArrayList<Integer> classid =studentService.getClassidByStudentidY(userid);
		ArrayList<Classes> classinfo =new ArrayList<Classes>();
		for(Integer i=0; i < num; i++) {
			classinfo.add(studentService.getClassInformationY(classid.get(i)));
    	}
		model.addAttribute("class_list", classinfo);

		//未开始的考试
		ArrayList<Integer> examid=studentService.getmyexamsidY(usermail, 2);
		int numexam = examid.size();
		if(numexam>4) {
			numexam = 4;
		}
		
		ArrayList<Examinfo> exam =new ArrayList<Examinfo>();
		for(Integer i=0; i < numexam; i++) {
			exam.add(studentService.getExamByIdY(examid.get(i)));
    	}
		model.addAttribute("exam", exam);
						
		//return "personpage";
		//return "studentPersonal";
		return "student_profile";
	}
	
	
	@RequestMapping(value = { "/newstudentInformation" })
	public String newstudentInformation(HttpServletRequest request,Model model) {
		String usermail= (String) request.getSession().getAttribute("usermail");
		String usertype= String.valueOf(request.getSession().getAttribute("usertype"));
		String username = studentService.getStudentnameByemail(usermail).get(0).getUsername();
		int gender = studentService.getStudentnameByemail(usermail).get(0).getUsergender();
		String useracademy = studentService.getStudentnameByemail(usermail).get(0).getUseracademy();
		String usergrade = studentService.getStudentnameByemail(usermail).get(0).getUsergrade();
		String usermajor = studentService.getStudentnameByemail(usermail).get(0).getUsermajor();
		String userheadshot = studentService.getStudentnameByemail(usermail).get(0).getUserheadshot();
		String usergender = "";
		if(gender==0) {
			usergender = "男";
		}
		else {
			usergender = "女";
		}
		int userid = studentService.getIDbyemailY(usermail);
		String academy= request.getParameter("academy");
		String grade= request.getParameter("grade");
		String major= request.getParameter("major");
		String userpassword= request.getParameter("password");
		String newpassword1= request.getParameter("newpassword1");
		String newpassword2= request.getParameter("newpassword2");
		String pass = studentService.getpasswordbyIDY(userid);

		request.getSession().setAttribute("usermail", usermail);	
		request.getSession().setAttribute("usertype", usertype);
		request.getSession().setAttribute("username", username);	
		request.getSession().setAttribute("userid", userid);
		request.getSession().setAttribute("usergender", usergender);
		request.getSession().setAttribute("userheadshot", userheadshot);
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		model.addAttribute("usermail", usermail);
		model.addAttribute("username", username);
		model.addAttribute("userid", userid);
		model.addAttribute("usergender", usergender);
		model.addAttribute("userheadshot", userheadshot);
		// 
		if(pass.equals(userpassword) && newpassword1.equals(newpassword2)) {
			studentService.updatestudentPasswordY(newpassword1, userid);
			request.getSession().setAttribute("userpassword", newpassword1);
		}
		
		if(!"".equals(academy)) {
			studentService.updatestudentAcademyY(academy, userid);
			request.getSession().setAttribute("useracademy", academy);
			model.addAttribute("useracademy", academy);
		}
		else {
			request.getSession().setAttribute("useracademy", useracademy);
			model.addAttribute("useracademy", useracademy);
		}
		
		if(!"".equals(grade)) {
			studentService.updatestudentGradeY(grade, userid);
			request.getSession().setAttribute("usergrade", grade);	
			model.addAttribute("usergrade", grade);
		}
		else {
			request.getSession().setAttribute("usergrade", usergrade);
			model.addAttribute("usergrade", usergrade);
		}
		
		if(!"".equals(major)) {
			studentService.updatestudentMajorY(major, userid);
			request.getSession().setAttribute("usermajor", major);
			model.addAttribute("usermajor", major);	
		}
		else {
			request.getSession().setAttribute("usermajor", usermajor);
			model.addAttribute("usermajor", usermajor);
		}
		return "student_information";
	}
	
	/**************
	 * another
	 * 
	 * 
		
	 *
		
	 */
}
