package com.example.coursesystem.service.ifs;

import java.util.List;

import com.example.coursesystem.entity.Course;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.vo.CourseRes;

public interface CourseService {
	
	//�s�W�ҵ{
	public Course createCourse(String id,String name,int day,int start,int end,int credit);
	//�ק�ҵ{
	public Course alterCourse(String id,String name,int day,int start,int end,int credit);
	//�R���ҵ{
	public CourseRes deleteCourse(String id);
	//�ǥ�ID�ΦW�ٷj�M�ҵ{
	public CourseRes findCourseByIdOrName(List<String> ids,List<String> names);
	//�s�W�ǥ�
	public Student createStudent(String id,String name);
	//�ק�ǥ�
	public Student alterStudent(String id,String name);
	//�R���ǥ�
	public CourseRes deleteStudent(String id);
	//���
	public CourseRes courseSelection(String studentId,List<String> courseSelList);
	//�h��
	public CourseRes courseCancel(String studentId,List<String> courseDelList);
	//�ǥѾǥ�ID���o�ǥ͸�T
	public CourseRes findStudentInfo(String studentId);
}
