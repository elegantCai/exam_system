package com.example.demo.entity;

// import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
import javax.persistence.Table;
//import javax.persistence.GenerationType;

@Entity
@Table(name="choiceansweredinfo") 
public class Choiceanswered {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Integer qid;
	private Integer type;
	private Integer examid;
	private Integer examsubid;
	private String answermail;
	private String answer;
	private Float score;
	private String comment;
	
	public Choiceanswered() {}
 	
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}	
	public int getExamid() {
		return examid;
	}
	public void setExamid(int examid) {
		this.examid = examid;
	}
	public int getExamsubid() {
		return examsubid;
	}
	public void setExamsubid(int examsubid) {
		this.examsubid = examsubid;
	}
	public String getAnswermail() {
		return answermail;
	}
	public void setAnswermail(String answermail) {
		this.answermail = answermail;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "User: [ userid=" +id +score;
		}
	
}


