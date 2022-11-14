package com.example.demo2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "course")
public class Course {

	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "name")
	private String name;

	@JsonProperty("day_of_the_week")
	@Column(name = "day")
	private int day;

	@JsonProperty("start_time")
	@Column(name = "start")
	private int start;

	@JsonProperty("end_time")
	@Column(name = "end")
	private int end;
	@Column(name = "credit")
	private int credit;

	public Course() {

	}

	public Course(String id, String name, int day, int start, int end, int credit) {
		this.id = id;
		this.name = name;
		this.day = day;
		this.start = start;
		this.end = end;
		this.credit = credit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

}
