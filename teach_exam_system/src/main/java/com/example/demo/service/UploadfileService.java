package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Service("uploadfileService")
public class UploadfileService {
	//返回文件上传到哪里
		//这个type是指是题目的图片，还是答案的图片
	public String getfilepath(String filedic,String type,MultipartFile file,int id) {
		if(!file.isEmpty()) {
			//String rootPath= "showpic/";   //基础文件夹（为了能显示图片）
			String rootPath= "G://exam//";
			//获取上传时的文件名
		    String fileName = file.getOriginalFilename();
		    //获取上传的文件的后缀
		    String[] strArray = fileName.split("\\.");
		    int suffixIndex = strArray.length -1;
		    String suffix=strArray[suffixIndex];
	        //重命名
	        String strid=String.valueOf(id);
	        String newfilename=filedic+"/"+type+"/"+strid+"."+suffix;
	        String filepath=rootPath+newfilename;
	        return filepath;			
		}
		else {
			return null;
		}
	}
	
	//filepath是文件存储路径
	public void uploadfile(String filepath,MultipartFile file) {
		if(!file.isEmpty()) {
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				inputStream = file.getInputStream();
			    File targetFile = new File(filepath);
			    //判断文件是否存在
			    if(targetFile.exists()){
			        //如果存在就把原来的删了
			    	targetFile.delete();
			    }
			    //获取文件的输出流
			    outputStream = new FileOutputStream(targetFile);
			    //最后使用资源访问器FileCopyUtils的copy方法拷贝文件
			    FileCopyUtils.copy(inputStream, outputStream);
			}catch (IOException e) {
			e.printStackTrace();}
			finally { 
				if (inputStream != null) {
			    try {
			        inputStream.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    	}
				}
			}
		}
	}
	
	//返回文件的相对路径
	public String getrelpath(String filedic,String type,String filepath) {
		if (filepath != null && filepath != "") {
			File file = new File(filepath);
			String fileName = file.getName();
			String relpath="images/"+filedic+"/"+type+"/"+fileName;
			return relpath;
		}
		else {
			return null;
		}
	}
	
	//返回要存的图片的文件夹
	public String getfiledic(int qtype) {
		if(qtype==0) {
			return "choiceq";
		}
		else if (qtype==1) {
			return "judq";
		}
		else if (qtype==2) {
			return "fillq";
		}
		else {
			return "bigq";
		}
	}
}

