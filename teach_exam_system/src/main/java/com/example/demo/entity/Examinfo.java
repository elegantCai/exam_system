//参考https://blog.csdn.net/seedshome/article/details/52316913
package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;


import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="examinfo")
public class Examinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private Calendar starttime;

    private Calendar endtime;

    private Integer type;

    private String subject;
    //0：未审核 1：未开始 2：考试中 3：结束未批改完成 4：结束并批改完成 5：签到中 6：审核未通过
    private Integer state;
    
    private Integer totalpoint;

    public Examinfo() {}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Calendar getStarttime() {
        return starttime;
    }
    public void setStarttime(Calendar starttime) {
        this.starttime = starttime;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Calendar getEndtime() {
        return endtime;
    }
    public void setEndtime(Calendar endtime) {
        this.endtime = endtime;
    }
    public Integer getType(){
        return type;
    }
    public void setType(Integer type){
        this.type=type;
    }
    public String getSubject(){
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    public Integer getTotalpoint() {
        return totalpoint;
    }

    public void setTotalpoint(Integer totalpoint) {
        this.totalpoint = totalpoint;
    }


}
