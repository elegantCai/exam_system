package com.example.demo.controller;

import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class StudentController {
    private StudentService studentservice;

    @Autowired
    public void setstudentservice(StudentService studentservice){
        this.studentservice = studentservice;
    }

//    private Student getstudent(int userid, String useracademy,int usergender, String usergrade,
//                               String userheadshot,String usermail, String usermajor,
//                               String username, String userpassword,String usertype) {
//        Student student = new Student();
//        student.setUserid(userid);
//
//        student.setUseracademy(useracademy);
//        student.setUsergender(usergender);
//        student.setUsergrade(usergrade);
//        student.setUserheadshot(userheadshot);
//        student.setUsermail(usermail);
//        student.setUsermajor(usermajor);
//        student.setUsername(username);
//        student.setUserpassword(userpassword);
//        student.setUsertype(usertype);
//
//        return student;
//    }



    //注册
    @RequestMapping(value = "studentadd",method = RequestMethod.POST)
    public ModelAndView studentadd(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String usermail = request.getParameter("usermail");
        String userpassword = request.getParameter("userpassword");
        String useracademy = request.getParameter("useracademy");
        String gender = request.getParameter("usergender");
        String usergrade = request.getParameter("usergrade");
        String usermajor = request.getParameter("usermajor");
        String username = request.getParameter("username");

        int usergender = Integer.parseInt(gender);

        String userheadshot = "img/wolf.jpg";
        boolean flag= studentservice.addstuent(useracademy,usergender,usergrade,
                userheadshot, usermail,usermajor,username,userpassword,"1");
        if (flag){
            redirectAttributes.addFlashAttribute("succ", "注册成功！");
        }else {
            redirectAttributes.addFlashAttribute("succ", "注册失败！");

        }

        return new ModelAndView("index");
    }

    @RequestMapping(value = "stureg/studentmail",method = RequestMethod.GET)
    public @ResponseBody
    String studentcode(@RequestParam String studentmail){
        String usermail;
        usermail = studentmail;
        String str="";
        if(("").equals(usermail) )
        {
            str="null";

        }else {
            str=studentservice.findmail(usermail);
        }
        return "{\"result\":\"" + str + "\"}";
    }


}
