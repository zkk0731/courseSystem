package com.example.coursesystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.coursesystem.entity.Course;

@Repository
public interface CourseDao extends JpaRepository<Course, String>{

//	由課程名稱搜尋對應的課程資訊
	public List<Course>findByName(String name);
//	由課程名稱(多筆)搜尋對應的課程資訊
	public List<Course>findByNameIn(List<String> names);
//	由課程名稱和課程ID搜尋對應課程資訊
	public List<Course>findByIdInOrNameIn(List<String>ids,List<String>names);
	
}
