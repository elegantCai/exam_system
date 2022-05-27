package com.example.demo.controller;

import com.example.demo.entity.Teacher;
import com.example.demo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.example.demo.service.LoginService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
@Controller
public class LoginController {
    private LoginService loginservice;
    @Autowired
    public void setloginservice(LoginService loginservice) {
        this.loginservice = loginservice;
    }

    @RequestMapping(value = {"/"})
    public String tomain(HttpServletRequest request) {
        request.getSession().invalidate();
        return "main";
    }
    
    @RequestMapping(value = {"/login", "/login.html"})
    public String tologin(HttpServletRequest request) {
        request.getSession().invalidate();
        return "index";
    }

    
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    //负责处理loginCheck.html请求
    //请求参数会根据参数名称默认契约自动绑定到相应方法的入参中
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    public @ResponseBody
    Object logincheck(HttpServletRequest request) {
        String usermail = request.getParameter("usermail");
        String password = request.getParameter("password");
        boolean isStudent = loginservice.hasmatchstudent(usermail, password);
        boolean isAdmin = loginservice.hasmatchadmin(usermail, password);
        boolean isTeacher = loginservice.hasmatchteacher(usermail, password);

        HashMap<String, String> res = new HashMap<>();
        if (isAdmin) {
            int type = 2;
            request.getSession().setAttribute("usermail", usermail);
            request.getSession().setAttribute("usertype", type);
            res.put("type", "2");
            res.put("msg", "管理员登陆成功！");
        } else if (isTeacher) {
            int type = 0;
            request.getSession().setAttribute("usermail", usermail);
            request.getSession().setAttribute("usertype", type);
            res.put("type", "0");
            res.put("msg", "教师登陆成功！");
        } else if (isStudent) {
            int type = 1;
            request.getSession().setAttribute("usermail", usermail);
            request.getSession().setAttribute("usertype", type);
            res.put("type", "1");
            res.put("msg", "学生登陆成功！");
        } else {
            res.put("type", "404");
            res.put("msg", "账号或密码错误！");
        }
        return res;
    }








    @RequestMapping("register")
    public ModelAndView register() {
        return new ModelAndView("registermain");
    }
    @RequestMapping("studentregister")
    public ModelAndView studentregister() {
        return new ModelAndView("studentregister");
    }

    @RequestMapping("teacherregister")
    public ModelAndView teacherregister() {
        return new ModelAndView("teacherregister");
    }



}
