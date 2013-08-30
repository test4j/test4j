package org.jtester.datafilling.model.example;

import java.io.Serializable;

import org.jtester.datafilling.annotations.FillExclude;

public class OrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	@FillExclude(comment = "We don't want notes to be automatically filled")
	private String note;

	private double lineAmount;

	private Article article;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public double getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(double lineAmount) {
		this.lineAmount = lineAmount;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

}
