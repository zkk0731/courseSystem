package com.example.coursesystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.coursesystem.entity.Course;

@Repository
public interface CourseDao extends JpaRepository<Course, String>{

	public List<Course>findByName(String name);
	
	public List<Course>findByNameIn(List<String> names);
	
	public List<Course>findByIdInOrNameIn(List<String>ids,List<String>names);
	
}
