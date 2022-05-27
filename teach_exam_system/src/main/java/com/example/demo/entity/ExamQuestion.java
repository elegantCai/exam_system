package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name="examquestion") 
public class ExamQuestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Integer examid;
	private Integer qid;
	private Integer qtype;
	//是否批改完成   0：没批完，1：已批完
	private Integer finishedcor;
	
	public ExamQuestion() {}
 	
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getExamid() {
		return examid;
	}
	public void setExamid(int examid) {
		this.examid = examid;
	}
	public Integer getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public Integer getQtype() {
		return qtype;
	}
	public void setQtype(int qtype) {
		this.qtype = qtype;
	}
	public Integer getFinishedcor() {
		return finishedcor;
	}
	public void setFinishedcor(int finishedcor) {
		this.finishedcor = finishedcor;
	}

}



