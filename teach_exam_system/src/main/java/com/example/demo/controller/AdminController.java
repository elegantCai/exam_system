package com.example.demo.controller;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Adminrecord;
import com.example.demo.entity.Teacher;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class AdminController {
    private AdminService adminservice;
    @Autowired
    public void setadminservice(AdminService adminservice){
          this.adminservice=adminservice;
    }

    @RequestMapping("/admin.html")
    public ModelAndView toadmin(HttpServletResponse response, HttpServletRequest request,Model model) {



        String usermail = (String)request.getSession().getAttribute("usermail");
        Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);

        System.out.println(usermail);
        Admin admininfo = adminservice.getadmin(usermail);

        System.out.println(admininfo);
        ModelAndView modelAndView = new ModelAndView("admin");

        modelAndView.addObject("admininfo",admininfo);
        ArrayList<Adminrecord> class_list=adminservice.getmyClass(usermail);
        ArrayList<Adminrecord> exam=adminservice.getmyExam(usermail);
        model.addAttribute("class_list", class_list);
        model.addAttribute("exam", exam);
        return modelAndView;
    }


    @RequestMapping("/admininfoupdate")
    public ModelAndView admininfoupdate(HttpServletRequest request,Model model){
        String usermail = (String)request.getSession().getAttribute("usermail");
        Admin admininfo = adminservice.getadmin(usermail);
        ModelAndView modelAndView = new ModelAndView("adminedit");
        modelAndView.addObject("admininfo",admininfo);
       
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
        return modelAndView;

    }
    @PostMapping("/adminfoupdatesub")
    public @ResponseBody
    Object adminfoupdatesub(HttpServletRequest request,Model model){
        String usermail = (String)request.getSession().getAttribute("usermail");
        String username = request.getParameter("username");
        String password = request.getParameter("userpassword");
        String usergender = request.getParameter("usergender");
        Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
        int gender = Integer.parseInt(usergender);
        HashMap<String, String> res = new HashMap<>();
        boolean flag= adminservice.updateadmin(username,password,gender,usermail);
        if (flag){
            res.put("alert","suc");
        }else {
            res.put("alert","err");
        }
        return res;
    }
}
