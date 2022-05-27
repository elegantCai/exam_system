package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name="fillinans")
public class Fillinans {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer qid;
	private Integer examid;
	private Integer examsubid;
	private String answermail;
	private String answer;
	private Integer score;
	private Integer type;
	@Column(length=2048)
	private String comment;
	
	public Fillinans() {}
	
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

	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "fillinans: [id=" + id + ", examid=" + examid + ","
				+ " examsubid=" + examsubid + ", answermail=" + answermail + ","
				+ "answer=" + answer + "," +   "score=" + score + "," 
				+ "type=" +type+ "," +   "information=" +comment+ "," + "]";
	}
}
