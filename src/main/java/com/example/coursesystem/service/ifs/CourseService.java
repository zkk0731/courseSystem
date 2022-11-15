package com.example.coursesystem.service.ifs;

import java.util.List;

import com.example.coursesystem.entity.Course;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.vo.CourseRes;

public interface CourseService {
	
	//新增課程
	public Course createCourse(String id,String name,int day,int start,int end,int credit);
	//修改課程
	public Course alterCourse(String id,String name,int day,int start,int end,int credit);
	//刪除課程
	public CourseRes deleteCourse(String id);
	//藉由ID或名稱搜尋課程
	public CourseRes findCourseByIdOrName(List<String> ids,List<String> names);
	//新增學生
	public Student createStudent(String id,String name);
	//修改學生
	public Student alterStudent(String id,String name);
	//刪除學生
	public CourseRes deleteStudent(String id);
	//選課
	public CourseRes courseSelection(String studentId,List<String> courseSelList);
	//退選
	public CourseRes courseCancel(String studentId,List<String> courseDelList);
	//藉由學生ID取得學生資訊
	public CourseRes findStudentInfo(String studentId);
}
