package com.example.demo2.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo2.constants.CourseRtnCode;
import com.example.demo2.entity.Course;
import com.example.demo2.entity.Student;
import com.example.demo2.repository.CourseDao;
import com.example.demo2.repository.StudentDao;
import com.example.demo2.service.ifs.CourseService;
import com.example.demo2.vo.CourseRes;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDao courseDao;
	@Autowired
	private StudentDao studentDao;

	@Override
	public Course createCourse(String id, String name, int day, int start, int end, int credit) {
		if (courseDao.existsById(id)) {
			return null;
		}
		Course course = new Course(id, name, day, start, end, credit);
		return courseDao.save(course);
	}

	@Override
	public Course alterCourse(String id, String name, int day, int start, int end, int credit) {
		Optional<Course> courseOp = courseDao.findById(id);
		if (courseOp.isPresent()) {
			Course course = courseOp.get();
			course = new Course(id, name, day, start, end, credit);
			return courseDao.save(course);
		}
		return null;
	}

	@Override
	public CourseRes deleteCourse(String id) {
		CourseRes res = new CourseRes();
		Optional<Course> courseOp = courseDao.findById(id);
		if (courseOp.isPresent()) {
			courseDao.deleteById(id);
			res.setMessage(CourseRtnCode.SUCCESS.getMessage());
			return res;
		}
		res.setMessage(CourseRtnCode.ID_NOT_EXIST.getMessage());
		return res;
	}

	@Override
	public CourseRes findCourseById(String id) {
		CourseRes res = new CourseRes();
		Optional<Course> courseOp = courseDao.findById(id);
		if (courseOp.isPresent()) {
			Course course = courseOp.get();
			res.setCourse(course);
			res.setMessage(CourseRtnCode.SUCCESS.getMessage());
			return res;
		}
		res.setMessage(CourseRtnCode.ID_NOT_EXIST.getMessage());
		return res;
	}

	@Override
	public CourseRes findCourseByName(String name) {
		CourseRes res = new CourseRes();
		List<Course> courseList = courseDao.findByName(name);
		if (courseList.isEmpty()) {
			res.setMessage(CourseRtnCode.NAME_NOT_EXIST.getMessage());
			return res;
		}
		res.setCourseList(courseList);
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		return res;

	}

	@Override
	public Student createStudent(String id, String name) {
		if (studentDao.existsById(id)) {
			return null;
		}
		Student student = new Student(id, name);
		return studentDao.save(student);
	}

	@Override
	public Student alterStudent(String id, String name) {
		Optional<Student> studentOp = studentDao.findById(id);
		if (studentOp.isPresent()) {
			Student student = studentOp.get();
			student = new Student(id, name);
			return studentDao.save(student);
		}
		return null;
	}

	@Override
	public CourseRes deleteStudent(String id) {
		Optional<Student> studentOp = studentDao.findById(id);
		CourseRes res = new CourseRes();
		if (studentOp.isPresent()) {
			Student student = studentOp.get();
			studentDao.delete(student);
			res.setMessage(CourseRtnCode.SUCCESS.getMessage());
			return res;
		}
		res.setMessage(CourseRtnCode.ID_NOT_EXIST.getMessage());
		return res;
	}

	@Override
	public CourseRes courseSelection(String studentId, List<String> courseSelList) {
		int totalCredit = 0;
		CourseRes res = new CourseRes();
		List<String> messageList = new ArrayList<>();
		Optional<Student> studentOp = studentDao.findById(studentId);
		if (!studentOp.isPresent()) {
			res.setMessage(CourseRtnCode.STUDENT_ID_NOT_EXIST.getMessage());
			return res;
		}
		Student student = studentOp.get();
		String courseIdStr = student.getCourseId();
		String[] courseIdArray = null;
		Set<String> courseSet = new HashSet<>();
		for (String strr : courseSelList) {
			courseSet.add(strr);
		}

		if (StringUtils.hasText(courseIdStr)) {
			courseIdArray = courseIdStr.split(",");
			for (String origCourse : courseIdArray) {
				courseSet.add(origCourse.trim());
			}
		}
		List<String> nonReCourseList = new ArrayList<>(courseSet);
		List<Course> allCourseList = new ArrayList<>();
		for(String str:nonReCourseList) {
			Optional<Course> courseOp = courseDao.findById(str);
			if(courseOp.isPresent()) {
				allCourseList.add(courseOp.get());
			}
			else {
				courseSet.remove(str);
				messageList.add(str + " " + CourseRtnCode.COURSE_NOT_EXIST.getMessage());
			}
		}
		
		for(int i = 0;i<allCourseList.size();i++) {
			Course courseA = allCourseList.get(i);
			for(int j=i+1;j<allCourseList.size();j++) {
				Course courseB = allCourseList.get(j);
				if (courseA.getName().equalsIgnoreCase(courseB.getName())) {
					courseSet.remove(nonReCourseList.get(j));
					messageList.add(
							nonReCourseList.get(j) + " " + CourseRtnCode.SAME_COURSE_SELECTED.getMessage());
				}
				
				if (courseA.getDay() == courseB.getDay()) {
					if (courseA.getStart() <= courseB.getStart() && courseA.getEnd() >= courseB.getStart()) {
						courseSet.remove(nonReCourseList.get(j));
						messageList.add(
								nonReCourseList.get(j) + " " + CourseRtnCode.CLASS_TIME_CONFLICT.getMessage());
					} else if (courseA.getStart() <= courseB.getEnd() && courseA.getEnd() >= courseB.getEnd()) {
						courseSet.remove(nonReCourseList.get(j));
						messageList.add(
								nonReCourseList.get(j) + " " + CourseRtnCode.CLASS_TIME_CONFLICT.getMessage());
					}
				}
			}
		}
		
//		for (int i = 0; i < nonReCourseList.size(); i++) {
//			if (courseDao.findById(nonReCourseList.get(i)).isPresent()) {
//				Course courseA = courseDao.findById(nonReCourseList.get(i)).get();
//				for (int j = i + 1; j < nonReCourseList.size(); j++) {
//					if (courseDao.findById(nonReCourseList.get(j)).isPresent()) {
//						Course courseB = courseDao.findById(nonReCourseList.get(j)).get();
//						if (courseA.getName().equalsIgnoreCase(courseB.getName())) {
//							courseSet.remove(nonReCourseList.get(j));
//							messageList.add(
//									nonReCourseList.get(j) + " " + CourseRtnCode.SAME_COURSE_SELECTED.getMessage());
//						}
//
//						if (courseA.getDay() == courseB.getDay()) {
//							if (courseA.getStart() <= courseB.getStart() && courseA.getEnd() >= courseB.getStart()) {
//								courseSet.remove(nonReCourseList.get(j));
//								messageList.add(
//										nonReCourseList.get(j) + " " + CourseRtnCode.CLASS_TIME_CONFLICT.getMessage());
//							} else if (courseA.getStart() <= courseB.getEnd() && courseA.getEnd() >= courseB.getEnd()) {
//								courseSet.remove(nonReCourseList.get(j));
//								messageList.add(
//										nonReCourseList.get(j) + " " + CourseRtnCode.CLASS_TIME_CONFLICT.getMessage());
//							}
//						}
//					}
//				}
//			} else {
//				courseSet.remove(nonReCourseList.get(i));
//				messageList.add(nonReCourseList.get(i) + " " + CourseRtnCode.COURSE_NOT_EXIST.getMessage());
//			}
//		}
		if (messageList.isEmpty()) {
			messageList = null;
		}
		res.setMessageList(messageList);
		if (courseSet.isEmpty()) {
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		List<Course> allStudentCourse = new ArrayList<>();
		for (String str : courseSet) {
			Course course = courseDao.findById(str).get();
			allStudentCourse.add(course);
			totalCredit += course.getCredit();
		}
		if (totalCredit > 10) {
			res.setMessage(CourseRtnCode.CREDIT_TOTAL_OVER.getMessage());
			return res;
		}
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		student.setCourseId(courseSet.toString().substring(1, courseSet.toString().length() - 1));
		studentDao.save(student);
		res.setCourseList(allStudentCourse);
		res.setCourseId(student.getCourseId());
		res.setStudentName(student.getName());
		res.setStudentId(student.getId());
		res.setTotlaCredit(totalCredit);
		return res;
	}

	@Override
	public CourseRes courseCancel(String studentId, List<String> courseDelList) {
		CourseRes res = new CourseRes();
		Optional<Student> studentOp = studentDao.findById(studentId);
		if (!studentOp.isPresent()) {
			res.setMessage(CourseRtnCode.STUDENT_ID_NOT_EXIST.getMessage());
			return res;
		}
		Student student = studentOp.get();
		String courseIdStr = student.getCourseId();
		if (courseIdStr == null) {
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		String[] courseIdArray = courseIdStr.split(",");
		List<String> courseList = new ArrayList<>();
		for (String item : courseIdArray) {
			courseList.add(item.trim());
		}
		List<String> courseListForEach = new ArrayList<>(courseList);
		List<String> courseDelListForEach = new ArrayList<>(courseDelList);
		for (String str1 : courseListForEach) {
			for (String str2 : courseDelListForEach) {
				if (str1.equalsIgnoreCase(str2)) {
					courseList.remove(str1);
					courseDelList.remove(str1);
				}
			}
		}
		List<Course> studentCourseList = new ArrayList<>();
		for (String item : courseList) {
			studentCourseList.add(courseDao.findById(item).get());
		}
		List<String> messageList = new ArrayList<>();
		if (!courseDelList.isEmpty()) {
			messageList.add(courseDelList.toString().substring(1, courseDelList.toString().length() - 1) + " " + CourseRtnCode.ID_NOT_EXIST.getMessage());
		}	
			student.setCourseId(courseList.toString().substring(1, courseList.toString().length() - 1));
		
		studentDao.save(student);
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		if (messageList.isEmpty()) {
			messageList = null;
		}
		if(studentCourseList.isEmpty()) {
			studentCourseList = null;
		}
		res.setMessageList(messageList);
		res.setCourseList(studentCourseList);
		res.setCourseId(student.getCourseId());
		res.setStudentName(student.getName());
		return res;
	}

	@Override
	public CourseRes findStudentInfo(String studentId) {
		int studentCredit = 0;
		CourseRes res = new CourseRes();
		Optional<Student> studentOp = studentDao.findById(studentId);
		if (!studentOp.isPresent()) {
			res.setMessage(CourseRtnCode.ID_NOT_EXIST.getMessage());
			return res;
		}
		Student student = studentOp.get();
		String courseIdStr = student.getCourseId();
		if (courseIdStr == null) {
			res.setStudentId(student.getId());
			res.setStudentName(student.getName());
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		String[] courseIdArray = courseIdStr.split(",");
		// List<String> courseList = new ArrayList<>();
		List<Course> stuCourseList = new ArrayList<>();
		for (String str : courseIdArray) {
			stuCourseList.add(courseDao.findById(str.trim()).get());
			studentCredit += courseDao.findById(str.trim()).get().getCredit();
		}
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		res.setStudentId(student.getId());
		res.setStudentName(student.getName());
		res.setCourseList(stuCourseList);
		res.setTotlaCredit(studentCredit);
		return res;
	}

}
