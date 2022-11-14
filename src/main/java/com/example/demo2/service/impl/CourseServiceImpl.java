package com.example.demo2.service.impl;

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

	//�s�W�ҵ{�θ��
	@Override
	public Course createCourse(String id, String name, int day, int start, int end, int credit) {
		if (courseDao.existsById(id)) {
			return null;
		}
		Course course = new Course(id, name, day, start, end, credit);
		return courseDao.save(course);
	}

	//�ק�ҵ{���
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

	//�R���ҵ{
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

	//��Id�ΦW�ٷj�M�ҵ{
	@Override
	public CourseRes findCourseByIdOrName(List<String> ids, List<String> names) {
		CourseRes res = new CourseRes();
		
		List<Course> serchResult = courseDao.findByIdInOrNameIn(ids, names);
		if(serchResult.isEmpty()) {
			res.setMessage(CourseRtnCode.NO_RESULT.getMessage());
			return res;
		}
		res.setCourseList(serchResult);

		return res;
	}

	//�s�W�ǥ�
	@Override
	public Student createStudent(String id, String name) {
		if (studentDao.existsById(id)) {
			return null;
		}
		Student student = new Student(id, name);
		return studentDao.save(student);
	}

	//�ק�ǥͦW�l
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

	//�R���ǥ�
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

	//���
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
		//���ID�ˬd,�ñN�ŦX��檺�ҵ{ID����iSet
		courseSelectIdCheck(courseSelList, messageList, courseSet, courseIdStr);
		//�ˬd�O�_�ۦP�W�٤νİ�,�ò���
		courseTimeAndNameCheck(messageList, courseSet, courseIdStr);

		if (courseSet.isEmpty()) {
			res.setStudentId(student.getId());
			res.setStudentName(student.getName());
			res.setMessageList(messageList);
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}

		List<Course> allStudentCourse = courseDao.findAllById(courseSet);
		//�p���`�Ǥ�,�W�L10�פ�[��
		for (Course course : allStudentCourse) {
			totalCredit += course.getCredit();
		}
		if (totalCredit > 10) {
			res.setMessage(CourseRtnCode.CREDIT_TOTAL_OVER.getMessage());
			return res;
		}
		//�YmessageList���� ��ܨS������ĵ�i��T
		if (messageList.isEmpty()) {
			res.setMessage(CourseRtnCode.SUCCESS.getMessage());
			//�S�]��null�bpostman�W���|��ܤ��A��
			messageList = null;
		} else {
			res.setMessage(CourseRtnCode.WARNING.getMessage());
		}
		res.setMessageList(messageList);
		//�NList�ন�r��s�^DB
		student.setCourseId(courseSet.toString().substring(1, courseSet.toString().length() - 1));
		studentDao.save(student);
		res.setCourseList(allStudentCourse);
		res.setCourseId(student.getCourseId());
		res.setStudentName(student.getName());
		res.setStudentId(student.getId());
		res.setTotlaCredit(totalCredit);
		return res;
	}

	
	//���ID�ˬd,�ñN�ŦX��檺�ҵ{ID����iSet
	private void courseSelectIdCheck(List<String> courseSelList, List<String> messageList, Set<String> courseSet,
			String courseIdStr) {
		List<Course> allCourse = courseDao.findAllById(courseSelList);
		//��X���bDB���ҵ{,�N���s�b���ҵ{�[�iSet��,��remove courseSelList�̦��s�bDB���ҵ{
		for (Course course : allCourse) {
			if (courseSelList.contains(course.getId())) {				
				courseSelList.remove(course.getId());
				courseSet.add(course.getId());
			}
		}
		//courseSelList �|�ѤU���bDB�̪��ҵ{
		if (!courseSelList.isEmpty()) {
			messageList.add(courseSelList.toString().substring(1, courseSelList.toString().length() - 1) + " "
					+ CourseRtnCode.COURSE_NOT_EXIST.getMessage());
		}
		String[] courseIdArray = null;
		//��X����ID���ҵ{
		if (StringUtils.hasText(courseIdStr)) {
			for (String str : courseSet) {
				if (courseIdStr.contains(str.trim())) {
					messageList.add(str.trim() + " " + CourseRtnCode.SAME_ID_COURSE_SELECTED.getMessage());
				}
			}
			//�N�ǥͭ즳�ҵ{���r���ন�}�C �A�[�iSet
			courseIdArray = courseIdStr.split(",");
			for (String item : courseIdArray) {
				courseSet.add(item.trim());
			}
		} else {
			//�p�G�ǥͭ쥻�S������,���@�ӪŦr���קK�᭱�ϥήɦ]null�ӥX��
			courseIdStr = "";
		}
	}

	//�ˬd�İ�M�W�٬ۦP���ҵ{
	private void courseTimeAndNameCheck(List<String> messageList, Set<String> courseSet, String courseIdStr) {
		//Set�নList ��K�ϥ�index��m����
		List<String> CourseSetToList = new ArrayList<>(courseSet);
		List<Course> myAllCourseList = courseDao.findAllById(CourseSetToList);
		for (int i = 0; i < myAllCourseList.size() - 1; i++) {
			Course courseA = myAllCourseList.get(i);
			for (int j = i + 1; j < myAllCourseList.size(); j++) {
				Course courseB = myAllCourseList.get(j);
				//�ҵ{�W�٭��Ʊư�
				if (courseA.getName().equalsIgnoreCase(courseB.getName())) {
					messageList.add(courseContainCheck(courseA, courseB, courseSet, courseIdStr)
							+ CourseRtnCode.SAME_NAME_COURSE_SELECTED.getMessage());
				}
				//�İ�ư�
				if (courseA.getDay() == courseB.getDay()) {
					if (!(courseA.getStart() >= courseB.getEnd() || courseA.getEnd() <= courseB.getStart())) {
						messageList.add(courseContainCheck(courseA, courseB, courseSet, courseIdStr)
								+ CourseRtnCode.CLASS_TIME_CONFLICT.getMessage());
					}
				}
			}
		}
	}

	//�P�_��ҵ{���즳���ηs�諸,�u���b�ҵ{�W�٭��Ʃνİ�ɤ~�|�I�s����k
	private String courseContainCheck(Course courseA, Course courseB, Set<String> courseSet, String courseIdStr) {
		String failList;
		//�u�ư��s�諸
		//�Y�ҵ{A�MB�İ�,�p�GA�O�즳������B�N�O�s�諸,�ҥH�bSet��������B
		if (courseIdStr.contains(courseA.getId())) {
			courseSet.remove(courseB.getId());
			failList = courseB.getId() + " ";
		}
		//�YA��B�����O�즳��,�h���O�s�諸,�]���ұư�
		else if (!courseIdStr.contains(courseA.getId()) && !courseIdStr.contains(courseB.getId())) {
			courseSet.remove(courseA.getId());
			courseSet.remove(courseB.getId());
			failList = courseA.getId() + " " + courseB.getId() + " ";
		
		} 
		//�Y���O�H�W����,�hA�O�s�諸,�N�L�ư�
		else {
			courseSet.remove(courseA.getId());
			failList = courseA.getId() + " ";
		}
		return failList;
	}
	
	
	
	@Override
	public CourseRes courseCancel(String studentId, List<String> courseDelList) {
		CourseRes res = new CourseRes();
		List<String> messageList = new ArrayList<>();
		Optional<Student> studentOp = studentDao.findById(studentId);
		if (!studentOp.isPresent()) {
			res.setMessage(CourseRtnCode.STUDENT_ID_NOT_EXIST.getMessage());
			return res;
		}
		Student student = studentOp.get();
		String courseIdStr = student.getCourseId();
		//�ǥͥ��ӨS�����,�]���L�k�h��
		if (courseIdStr.isEmpty()) {
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		//�ǥͽҽҵ{ID�r���ন�}�C,�A��JList
		List<String> courseList = courseStrToList(courseIdStr);
		//��X�h��M�椤�ǥͭ쥻�N�S������
		for(String str:courseDelList) {
			if(!courseIdStr.contains(str)) {
				messageList.add(str + " "+ CourseRtnCode.STUDENT_DONT_HAVE_THIS_COURSE.getMessage());
			}
		}
		courseList.removeAll(courseDelList);

		List<Course> studentCourseList = courseDao.findAllById(courseList);
		//�NList�ন�r��s�^DB
		student.setCourseId(courseList.toString().substring(1, courseList.toString().length() - 1));
		studentDao.save(student);
		if (studentCourseList.isEmpty()) {
			//�h����S�����,���bpostman�W��ܷ|�����A��,�ҥH�]��null
			studentCourseList = null;
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
		//�S������ �����^��
		if (courseIdStr.isEmpty()) {
			res.setStudentId(student.getId());
			res.setStudentName(student.getName());
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		//�ǥͽҽҵ{ID�r���ন�}�C,�A��JList
		List<String> courseList = courseStrToList(courseIdStr);
		
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
	
	//�ǥͽҽҵ{ID�r���ন�}�C,�A��JList
		private List<String> courseStrToList(String courseIdStr){
			String[] courseIdArray = courseIdStr.split(",");
			List<String> courseList = new ArrayList<>();
			for (String item : courseIdArray) {
				courseList.add(item.trim());
			}
			return courseList;
		}

}
