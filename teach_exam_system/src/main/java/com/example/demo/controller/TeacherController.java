package com.example.demo.controller;

import com.example.demo.entity.Classes;
import com.example.demo.entity.Examinfo;
import com.example.demo.entity.Teacher;
import com.example.demo.service.ClassService;
import com.example.demo.service.ExamService;
import com.example.demo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Controller
public class TeacherController {
	@Autowired
	private ClassService classservice;
	@Autowired
    private ExamService examService;
    private TeacherService teacherservice;
    /**
     * 获取随机名称
     *
     * @param realName 真实名称
     * @return uuid 随机名称
     */
     private String getUUIDName(String realName) {
        //realname  可能是  1sfasdf.jpg   也可能是 1sfasdf 1
        //获取后缀名
        int index = realName.lastIndexOf(".");
        if (index == -1) {
            return UUID.randomUUID().toString().replace("-", "").toUpperCase();
        } else {
            return UUID.randomUUID().toString().replace("-", "").toUpperCase() + realName.substring(index);
        }
    }

    private String getjobtitle(int jobtitle){
        String res = "";
        switch (jobtitle){

            case 0:
                res = "讲师";
            case 1:
                res = "副教授";
            case 2:
                res = "教授";
            case 3:
                res ="副研究员";
            case 4:
                res = "研究员";
            case 5:
                res = "助理教师";
        }
        return res;
    }
    @Autowired
    public void setteacherservice(TeacherService teacherservice){
        this.teacherservice=teacherservice;
    }




    //注册
    @RequestMapping("teacheradd")
    public ModelAndView teacheradd(RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {
        String usermail = request.getParameter("usermail");
        String password = request.getParameter("userpassword");
        String academy = request.getParameter("useracademy");
        String usergender = request.getParameter("usergender");
        int gender = Integer.parseInt(usergender);
        String department = request.getParameter("department");
        String username = request.getParameter("username");
        String userjobtitle = request.getParameter("jobtitle");
        int jobtitle = Integer.parseInt(userjobtitle);
        String headshot = "img/wolf.jpg";
        boolean flag= teacherservice.addteacher(academy,department,gender,headshot,
                jobtitle,password,usermail,username);

        if (flag){
            redirectAttributes.addFlashAttribute("succ", "注册成功！");
        }else {
            redirectAttributes.addFlashAttribute("succ", "注册失败！");

        }

        return new ModelAndView("index");
    }

    @RequestMapping(value = "teareg/teachermail",method = RequestMethod.GET)
    public @ResponseBody
    String studentcode(@RequestParam String teachermail,HttpServletRequest request,Model model){
        String usermail;
        usermail = teachermail;
        String str="";
        if(("").equals(usermail) )
        {
            str="null";

        }else {
            str=teacherservice.findmail(usermail);
        }
		Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
        return "{\"result\":\"" + str + "\"}";
    }

    @RequestMapping("/teacher.html")
    public ModelAndView toteacher(HttpServletRequest request,Model model) {
        String usermail = (String)request.getSession().getAttribute("usermail");
        System.out.println(usermail);
        Teacher teacherinfo = teacherservice.getteacher(usermail);

        System.out.println(teacherinfo);
        ModelAndView modelAndView = new ModelAndView("teacher");

        modelAndView.addObject("teacherinfo",teacherinfo);
        int title = teacherinfo.getJobtitle();
        String res = getjobtitle(title);
        modelAndView.addObject("jobtitle",res);
        Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
		
		ArrayList<Classes> class_list=classservice.ClassidByTeacheridX(usermail);
		model.addAttribute("class_list", class_list);
		ArrayList<ArrayList<Examinfo>> exam=examService.getmyupexam(usermail);
		model.addAttribute("exam", exam.get(1));
        return modelAndView;
    }
    @RequestMapping("/teacherinfoupdate")
    public ModelAndView teacherinfoupdate(HttpServletRequest request,Model model){
        String usermail = (String)request.getSession().getAttribute("usermail");
        Teacher teacherinfo = teacherservice.getteacher(usermail);
        ModelAndView modelAndView = new ModelAndView("teacheredit");
        modelAndView.addObject("teacherinfo",teacherinfo);
        Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);
        return modelAndView;

    }

    @PostMapping ("/teainfoupdatesub")
    public @ResponseBody
    Object teainfoupdatesub(HttpServletRequest request, MultipartFile file,Model model)throws IOException {
        String usermail = (String)request.getSession().getAttribute("usermail");

        String password = request.getParameter("userpassword");
        String academy = request.getParameter("useracademy");
        String usergender = request.getParameter("usergender");
        int gender = Integer.parseInt(usergender);
        String department = request.getParameter("department");
        String username = request.getParameter("username");
        String userjobtitle = request.getParameter("jobtitle");
        int jobtitle = Integer.parseInt(userjobtitle);
        Integer usertype = (Integer)request.getSession().getAttribute("usertype");
		model.addAttribute("usermail", usermail);
		model.addAttribute("usertype", usertype);



        //获取文件的内容
        InputStream in = file.getInputStream();
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);

        //生成一个uuid名称出来
        String uuidFilename = getUUIDName(originalFilename);
        System.out.println(uuidFilename);

        //产生一个随机目录
        //String dir0 = "showpic/userheadshot/";
      String dir0 = "G://exam//";
       // String dir0 = "D:/fuchuang/";
        File dir = new File(dir0);
        //若文件夹不存在,则创建出文件夹
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File newFile = new File(dir, uuidFilename);
        //将文件输出到目标的文件中
        System.out.print(newFile);
        file.transferTo(newFile);

        //将保存的文件路径更新到用户信息headshot中
        String headshot = "images" + "/" + "userheadshot"+"/"+uuidFilename;

        HashMap<String, String> res = new HashMap<>();
        boolean flag= teacherservice.updateteacher(academy,department,gender,headshot,
                jobtitle,password,username,usermail);
        if (flag){

            res.put("alert","suc");
        }else {

            res.put("alert","err");
        }
        return res;

    }


}
