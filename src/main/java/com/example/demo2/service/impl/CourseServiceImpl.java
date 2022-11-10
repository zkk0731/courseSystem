package com.example.demo2.service.impl;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
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
		Set<String> courseSet = new HashSet<>();

		courseSelectIdCheck(courseSelList, messageList, courseSet, courseIdStr);
		courseTimeAndNameCheck(messageList, courseSet, courseIdStr);

		if (courseSet.isEmpty()) {
			res.setStudentId(student.getId());
			res.setStudentName(student.getName());
			res.setMessageList(messageList);
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}

		List<Course> allStudentCourse = courseDao.findAllById(courseSet);
		for (Course course : allStudentCourse) {
			totalCredit += course.getCredit();
		}
		if (totalCredit > 10) {
			res.setMessage(CourseRtnCode.CREDIT_TOTAL_OVER.getMessage());
			return res;
		}
		if (messageList.isEmpty()) {
			res.setMessage(CourseRtnCode.SUCCESS.getMessage());
			messageList = null;
		} else {
			res.setMessage(CourseRtnCode.WARNING.getMessage());
		}
		res.setMessageList(messageList);
		student.setCourseId(courseSet.toString().substring(1, courseSet.toString().length() - 1));
		studentDao.save(student);
		res.setCourseList(allStudentCourse);
		res.setCourseId(student.getCourseId());
		res.setStudentName(student.getName());
		res.setStudentId(student.getId());
		res.setTotlaCredit(totalCredit);
		return res;
	}

	private void courseSelectIdCheck(List<String> courseSelList, List<String> messageList, Set<String> courseSet,
			String courseIdStr) {
		List<Course> allCourse = courseDao.findAllByIdIn(courseSelList);
		for (Course course : allCourse) {
			if (courseSelList.contains(course.getId())) {
				courseSelList.remove(course.getId());
				courseSet.add(course.getId());
			}
		}
		if (!courseSelList.isEmpty()) {
			messageList.add(courseSelList.toString().substring(1, courseSelList.toString().length() - 1) + " "
					+ CourseRtnCode.COURSE_NOT_EXIST.getMessage());
		}
		String[] courseIdArray = null;
		if (StringUtils.hasText(courseIdStr)) {
			for (String str : courseSet) {
				if (courseIdStr.contains(str.trim())) {
					messageList.add(str.trim() + " " + CourseRtnCode.SAME_ID_COURSE_SELECTED.getMessage());
				}
			}
			courseIdArray = courseIdStr.split(",");
			for (String origCourse : courseIdArray) {
				courseSet.add(origCourse.trim());
			}
		} else {
			courseIdStr = "";
		}
	}

	private void courseTimeAndNameCheck(List<String> messageList, Set<String> courseSet, String courseIdStr) {
		List<String> CourseSetToList = new ArrayList<>(courseSet);
		List<Course> myAllCourseList = courseDao.findAllById(CourseSetToList);
		for (int i = 0; i < myAllCourseList.size() - 1; i++) {
			Course courseA = myAllCourseList.get(i);
			for (int j = i + 1; j < myAllCourseList.size(); j++) {
				Course courseB = myAllCourseList.get(j);
				if (courseA.getName().equalsIgnoreCase(courseB.getName())) {
					messageList.add(courseContainCheck(courseA, courseB, courseSet, courseIdStr)
							+ CourseRtnCode.SAME_NAME_COURSE_SELECTED.getMessage());
				}
				if (courseA.getDay() == courseB.getDay()) {
					if (!(courseA.getStart() >= courseB.getEnd() || courseA.getEnd() <= courseB.getStart())) {
						messageList.add(courseContainCheck(courseA, courseB, courseSet, courseIdStr)
								+ CourseRtnCode.CLASS_TIME_CONFLICT.getMessage());
					}
				}
			}
		}
	}

	private String courseContainCheck(Course courseA, Course courseB, Set<String> courseSet, String courseIdStr) {
		String failList;
		if (courseIdStr.contains(courseA.getId())) {
			courseSet.remove(courseB.getId());
			failList = courseB.getId() + " ";
		} else if (!courseIdStr.contains(courseA.getId()) && !courseIdStr.contains(courseB.getId())) {
			courseSet.remove(courseA.getId());
			courseSet.remove(courseB.getId());
			failList = courseA.getId() + " " + courseB.getId() + " ";
		} else {
			courseSet.remove(courseA.getId());
			failList = courseA.getId() + " ";
		}
		return failList;
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
		if (courseIdStr.isEmpty()) {
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		String[] courseIdArray = courseIdStr.split(",");
		List<String> courseList = new ArrayList<>();
		for (String item : courseIdArray) {
			courseList.add(item.trim());
		}
		deleteStudentCourse(courseList, courseDelList);

		List<Course> studentCourseList = courseDao.findAllById(courseList);

		List<String> messageList = new ArrayList<>();
		student.setCourseId(courseList.toString().substring(1, courseList.toString().length() - 1));
		studentDao.save(student);
		if (studentCourseList.isEmpty()) {
			studentCourseList = null;
		}

		if (!courseDelList.isEmpty()) {
			messageList.add(courseDelList.toString().substring(1, courseDelList.toString().length() - 1) + " "
					+ CourseRtnCode.STUDENT_DONT_HAVE_THIS_COURSE.getMessage());
		}

		if (messageList.isEmpty()) {
			res.setMessage(CourseRtnCode.SUCCESS.getMessage());
			messageList = null;
		} else {
			res.setMessage(CourseRtnCode.WARNING.getMessage());
		}

		res.setMessageList(messageList);
		res.setCourseList(studentCourseList);
		res.setCourseId(student.getCourseId());
		res.setStudentName(student.getName());
		return res;
	}

	private void deleteStudentCourse(List<String> courseList, List<String> courseDelList) {
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
		if (courseIdStr.isEmpty()) {
			res.setStudentId(student.getId());
			res.setStudentName(student.getName());
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		String[] courseIdArray = courseIdStr.split(",");
		List<String> courseList = new ArrayList<>();

		for (String str : courseIdArray) {
			courseList.add(str.trim());
		}
		List<Course> stuCourseList = courseDao.findAllById(courseList);
		for (Course course : stuCourseList) {
			studentCredit += course.getCredit();
		}
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		res.setStudentId(student.getId());
		res.setStudentName(student.getName());
		res.setCourseList(stuCourseList);
		res.setTotlaCredit(studentCredit);
		return res;
	}

}
