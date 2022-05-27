package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="answeredbigq") //指定关联的数据库表名
public class Answeredbigq {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Integer qid;
	private String answermail;
	@Column(length = 2048)
	private String answer;
	private String answerpic;
	private Float score;
	private String comment;
	private Integer type;
	private Integer examid;
	private Integer examsubid;
	private String correctmail;
	
	public Answeredbigq() {}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQid() {
		return qid;
	}
	public void setQid(Integer qid) {
		this.qid = qid;
	}
	public String getAnswermail() {
		return answermail;
	}
	public void setAnswermail(String answermail) {
		this.answermail = answermail;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswerpic() {
		return answerpic;
	}
	public void setAnswerpic(String answerpic) {
		this.answerpic = answerpic;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getExamid() {
		return examid;
	}
	public void setExamid(Integer examid) {
		this.examid = examid;
	}
	public Integer getExamsubid() {
		return examsubid;
	}
	public void setExamsubid(Integer examsubid) {
		this.examsubid = examsubid;
	}
	public String getCorrectmail() {
		return correctmail;
	}
	public void setCorrectmail(String correctmail) {
		this.correctmail = correctmail;
	}
	
}
