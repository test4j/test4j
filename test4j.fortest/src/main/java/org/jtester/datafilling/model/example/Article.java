package org.jtester.datafilling.model.example;

import java.io.Serializable;

import org.jtester.datafilling.annotations.FillConstructor;
import org.jtester.datafilling.annotations.FillDouble;
import org.jtester.datafilling.annotations.FillInteger;

public class Article implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int id;

	private final String description;

	private final Double itemCost;

	@FillConstructor(comment = "Immutable-like POJOs must be annotated with @PodamConstructor")
	public Article(@FillInteger(max = 100000) int id, String description, @FillDouble(min = 50.0) Double itemCost) {
		super();
		this.id = id;
		this.description = description;
		this.itemCost = itemCost;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public Double getItemCost() {
		return itemCost;
	}

}
