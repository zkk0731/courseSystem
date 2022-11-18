package com.example.coursesystem.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.coursesystem.constants.CourseRtnCode;
import com.example.coursesystem.entity.Course;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.repository.CourseDao;
import com.example.coursesystem.repository.StudentDao;
import com.example.coursesystem.service.ifs.CourseService;
import com.example.coursesystem.vo.CourseRes;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDao courseDao;
	@Autowired
	private StudentDao studentDao;

	// 新增課程及資料
	@Override
	public Course createCourse(String id, String name, int day, int start, int end, int credit) {
		// 判斷ID是否存在
		if (courseDao.existsById(id)) {
			return null;
		}
		// 將輸入值帶入
		Course course = new Course(id, name, day, start, end, credit);
		// save回DB
		return courseDao.save(course);
	}

	// 修改課程資料
	@Override
	public Course updateCourse(String id, String name, int day, int start, int end, int credit) {
		// 判斷ID是否存在
		Optional<Course> courseOp = courseDao.findById(id);

		if (!courseOp.isPresent()) {
			return null;
		}
		// 存在就將DB裡的資料get出來,再將修改值存回
		Course course = courseOp.get();
		course = new Course(id, name, day, start, end, credit);
		return courseDao.save(course);

	}

	// 刪除課程
	@Override
	public CourseRes deleteCourse(String id) {
		CourseRes res = new CourseRes();
		// 判斷ID是否存在
		Optional<Course> courseOp = courseDao.findById(id);
		if (courseOp.isPresent()) {
			// 存在就將其資料刪除
			courseDao.deleteById(id);
			res.setMessage(CourseRtnCode.SUCCESS.getMessage());
			return res;
		}

		res.setMessage(CourseRtnCode.ID_NOT_EXIST.getMessage());
		return res;
	}

	// 用Id或名稱搜尋課程
	@Override
	public CourseRes findCourseByIdOrName(List<String> ids, List<String> names) {
		CourseRes res = new CourseRes();
		// 利用在Dao新增的方法尋找
		List<Course> serchResult = courseDao.findByIdInOrNameIn(ids, names);
		if (serchResult.isEmpty()) {
			res.setMessage(CourseRtnCode.NO_RESULT.getMessage());
			return res;
		}

		res.setCourseList(serchResult);
		return res;
	}

	// 新增學生
	@Override
	public Student createStudent(String id, String name) {
		// 判斷ID是否存在
		if (studentDao.existsById(id)) {
			return null;
		}
		// 將輸入值帶入
		Student student = new Student(id, name);
		return studentDao.save(student);
	}

	// 修改學生名子
	@Override
	public Student updateStudent(String id, String name) {
		// 判斷ID是否存在
		Optional<Student> studentOp = studentDao.findById(id);
		if (!studentOp.isPresent()) {
			return null;
		}
		Student student = studentOp.get();
		// 將輸入值帶入
		student = new Student(id, name);
		return studentDao.save(student);

	}

	// 刪除學生
	@Override
	public CourseRes deleteStudent(String id) {
		// 將輸入值帶入
		Optional<Student> studentOp = studentDao.findById(id);
		CourseRes res = new CourseRes();
		if (studentOp.isPresent()) {
			Student student = studentOp.get();
			// 學生須退完課才能夠刪除
			if (!StringUtils.hasText(student.getCourseId())) {
				studentDao.delete(student);
				res.setMessage(CourseRtnCode.SUCCESS.getMessage());
				return res;
			} else {
				res.setMessage(CourseRtnCode.NEED_CANCEL_ALL_COURSE.getMessage());
				return res;
			}
		}
		res.setMessage(CourseRtnCode.ID_NOT_EXIST.getMessage());
		return res;
	}

	// 選課
	@Override
	public CourseRes courseSelection(String studentId, List<String> courseSelList) {
		int totalCredit = 0;
		CourseRes res = new CourseRes();
		List<String> messageList = new ArrayList<>();
		// 判斷學生ID是否存在
		Optional<Student> studentOp = studentDao.findById(studentId);
		if (!studentOp.isPresent()) {
			res.setMessage(CourseRtnCode.STUDENT_ID_NOT_EXIST.getMessage());
			return res;
		}
		Student student = studentOp.get();
		// 將學生DB裡存的課程ID以courseIdStr接收
		String courseIdStr = student.getCourseId();
		Set<String> courseSet = new HashSet<>();

		// 選課ID檢查,並將符合資格的課程ID都放進Set
		courseSelectIdCheck(courseSelList, messageList, courseSet, courseIdStr);

		// 檢查是否相同名稱及衝堂,並移除
		courseTimeAndNameCheck(messageList, courseSet, courseIdStr);

		// 判斷選的課是否被超過五人選過了
		courseSelectByStudentCount(courseSet, messageList, courseIdStr);

		// 若courseSet為空則代表沒有選任何課程
		if (courseSet.isEmpty()) {
			res.setStudentId(student.getId());
			res.setStudentName(student.getName());
			res.setMessageList(messageList);
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		// 取得學生選課詳細課程資料
		List<Course> allStudentCourse = courseDao.findAllById(courseSet);
		// 計算總學分,超過10終止此加選
		for (Course course : allStudentCourse) {
			totalCredit += course.getCredit();
		}
		if (totalCredit > 10) {
			res.setMessage(CourseRtnCode.CREDIT_TOTAL_OVER.getMessage());
			return res;
		}
		// 若messageList為空 表示沒有任何警告資訊
		if (messageList.isEmpty()) {
			res.setMessage(CourseRtnCode.SUCCESS.getMessage());
			// 沒設成null在postman上仍會顯示中括號
			messageList = null;
		} else {
			res.setMessage(CourseRtnCode.WARNING.getMessage());
		}
		res.setMessageList(messageList);
		// 將List轉成字串存回DB
		student.setCourseId(courseSet.toString().substring(1, courseSet.toString().length() - 1));
		studentDao.save(student);

		res.setCourseList(allStudentCourse);
		res.setCourseId(student.getCourseId());
		res.setStudentName(student.getName());
		res.setStudentId(student.getId());
		res.setTotlaCredit(totalCredit);

		return res;
	}

	// 選課ID檢查,並將符合資格的課程ID都放進Set
	private void courseSelectIdCheck(List<String> courseSelList, List<String> messageList, Set<String> courseSet,
			String courseIdStr) {

		List<Course> selCourseInfo = courseDao.findAllById(courseSelList);

		// 找出不在DB的課程,將有存在的課程加進Set裡,並remove courseSelList裡有存在DB的課程
		for (Course course : selCourseInfo) {
			if (courseSelList.contains(course.getId())) {
				courseSelList.remove(course.getId());
				courseSet.add(course.getId());
			}
		}

		// courseSelList 會剩下不在DB裡的課程
		if (!courseSelList.isEmpty()) {
			messageList.add(courseSelList.toString().substring(1, courseSelList.toString().length() - 1) + " "
					+ CourseRtnCode.COURSE_NOT_EXIST.getMessage());
		}
		String[] courseIdArray;
		// 找出重複ID的課程
		if (StringUtils.hasText(courseIdStr)) {
			for (String str : courseSet) {
				if (courseIdStr.contains(str.trim())) {
					messageList.add(str.trim() + " " + CourseRtnCode.SAME_ID_COURSE_SELECTED.getMessage());
				}
			}
			// 將學生原有課程的字串轉成陣列 再加進Set
			courseIdArray = courseIdStr.split(",");
			for (String item : courseIdArray) {
				courseSet.add(item.trim());
			}
		} else {
			// 如果學生原本沒選任何課,給一個空字串避免後面使用時因null而出錯
			courseIdStr = "";
		}
	}

	// 檢查衝堂和名稱相同的課程
	private void courseTimeAndNameCheck(List<String> messageList, Set<String> courseSet, String courseIdStr) {
		// Set轉成List 方便使用index位置取值
		List<String> courseSetToList = new ArrayList<>(courseSet);
		List<Course> myAllCourseList = courseDao.findAllById(courseSetToList);
		// 對同一List的資料互相比較
		for (int i = 0; i < myAllCourseList.size() - 1; i++) {

			Course courseA = myAllCourseList.get(i);
			for (int j = i + 1; j < myAllCourseList.size(); j++) {

				Course courseB = myAllCourseList.get(j);

				// 課程名稱重複排除
				if (courseA.getName().equalsIgnoreCase(courseB.getName())) {
					// 排除掉新增的重複名稱課程
					messageList.add(courseContainCheck(courseA, courseB, courseSet, courseIdStr)
							+ CourseRtnCode.SAME_NAME_COURSE_SELECTED.getMessage());
				}

				// 衝堂排除
				if (courseA.getDay() == courseB.getDay()) {
					// 排除掉新增的衝堂課程
					if (!(courseA.getStart() >= courseB.getEnd() || courseA.getEnd() <= courseB.getStart())) {
						messageList.add(courseContainCheck(courseA, courseB, courseSet, courseIdStr)
								+ CourseRtnCode.CLASS_TIME_CONFLICT.getMessage());
					}
				}
			}
		}
	}

	// 判斷兩課程為原有的或新選的,只有在課程名稱重複或衝堂時才會呼叫此方法
	private String courseContainCheck(Course courseA, Course courseB, Set<String> courseSet, String courseIdStr) {
		String failMsg;
		// 只排除新選的
		// 若課程A和B衝堂,如果A是原有的的那B就是新選的,所以在Set中移除掉B
		if (courseIdStr.contains(courseA.getId())) {
			courseSet.remove(courseB.getId());
			failMsg = courseB.getId() + " ";
		}
		// 若A跟B都不是原有的,則都是新選的,因此皆排除
		else if (!courseIdStr.contains(courseA.getId()) && !courseIdStr.contains(courseB.getId())) {
			courseSet.remove(courseA.getId());
			courseSet.remove(courseB.getId());
			failMsg = courseA.getId() + " " + courseB.getId() + " ";

		}
		// 若不是以上條件,則A是新選的,將他排除
		else {
			courseSet.remove(courseA.getId());
			failMsg = courseA.getId() + " ";
		}
		return failMsg;
	}

	// 判斷選的課是否被超過五人選過了
	private void courseSelectByStudentCount(Set<String> courseSet, List<String> messageList, String courseIdStr) {
		// 找出所有學生資料
		List<Student> studentList = studentDao.findAll();

		// new一個map Key=課程ID Value=選課人數
		Map<String, Integer> courseCountMap = new HashMap<>();

		// 將所有學生選的所有課存成List
		List<String> allStudentCourse = new ArrayList<>();
		for (Student stu : studentList) {
			String allStudentCourseIdStr = stu.getCourseId();
			if (StringUtils.hasText(allStudentCourseIdStr)) {
				allStudentCourse.addAll(courseStrToList(allStudentCourseIdStr));
			}
		}

		// 將新選的課也放入List
		allStudentCourse.addAll(courseSet);

		// 判斷課程ID在List中有幾個=被選了幾次
		// 超過五就擋掉此筆加選
		for (String str : allStudentCourse) {
			Integer selCount = courseCountMap.get(str);
			if (selCount == null) {
				selCount = 0;
			}

			switch (selCount) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				courseCountMap.put(str, selCount + 1);
				break;
			case 5:
				if (!courseIdStr.contains(str)) {
					courseSet.remove(str);
					messageList.add(str + " " + CourseRtnCode.COURSE_CANNOT_BE_SELECTED_MORE_THAN_5.getMessage());
					break;
				}
			}
		}

	}

	@Override
	public CourseRes courseCancel(String studentId, List<String> courseDelList) {
		CourseRes res = new CourseRes();
		List<String> messageList = new ArrayList<>();
		// 判斷學生資料是否存在
		Optional<Student> studentOp = studentDao.findById(studentId);
		if (!studentOp.isPresent()) {
			res.setMessage(CourseRtnCode.STUDENT_ID_NOT_EXIST.getMessage());
			return res;
		}
		// 學生選課ID
		Student student = studentOp.get();
		String courseIdStr = student.getCourseId();
		// 學生本來沒任何課,因此無法退選
		if (courseIdStr.isEmpty()) {
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		// 學生課課程ID字串轉成陣列,再放入List
		List<String> courseList = courseStrToList(courseIdStr);
		// 找出退選清單中學生原本就沒有的課
		for (String str : courseDelList) {
			if (!courseIdStr.contains(str)) {
				messageList.add(str + " " + CourseRtnCode.STUDENT_DONT_HAVE_THIS_COURSE.getMessage());
			}
		}
		// 移除courseList裡有含courseDelList的課程
		courseList.removeAll(courseDelList);
		// 取得學生選課詳細課程資料
		List<Course> studentCourseList = courseDao.findAllById(courseList);
		// 將List轉成字串存回DB
		student.setCourseId(courseList.toString().substring(1, courseList.toString().length() - 1));
		studentDao.save(student);
		// 退完後沒任何課,但在postman上顯示會有中括號,所以設成null
		if (studentCourseList.isEmpty()) {
			studentCourseList = null;
		}

		// 若messageList為空 表示沒有任何警告資訊
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
		// 判斷學生資料是否存在
		CourseRes res = new CourseRes();
		Optional<Student> studentOp = studentDao.findById(studentId);
		if (!studentOp.isPresent()) {
			res.setMessage(CourseRtnCode.ID_NOT_EXIST.getMessage());
			return res;
		}
		// 取出學生選課資料
		Student student = studentOp.get();
		String courseIdStr = student.getCourseId();
		// 沒選任何課 直接回傳
		if (courseIdStr.isEmpty()) {
			res.setStudentId(student.getId());
			res.setStudentName(student.getName());
			res.setMessage(CourseRtnCode.NO_COURSE_SELECTED.getMessage());
			return res;
		}
		// 學生課課程ID字串轉成陣列,再放入List
		List<String> courseList = courseStrToList(courseIdStr);
		// 取得學生選課詳細課程資料
		List<Course> stuCourseList = courseDao.findAllById(courseList);
		// 計算總學分
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

	// 學生課課程ID字串轉成陣列,再放入List
	private List<String> courseStrToList(String courseIdStr) {
		// 用逗號將字串內不同課程切開裝入陣列
		String[] courseIdArray = courseIdStr.split(",");
		List<String> courseList = new ArrayList<>();

		// 將陣列內資料存入List
		for (String item : courseIdArray) {
			courseList.add(item.trim());
		}
		return courseList;
	}

}
