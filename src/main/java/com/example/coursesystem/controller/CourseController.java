package com.example.coursesystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.coursesystem.constants.CourseRtnCode;
import com.example.coursesystem.entity.Course;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.service.ifs.CourseService;
import com.example.coursesystem.vo.CourseReq;
import com.example.coursesystem.vo.CourseRes;

@CrossOrigin
@RestController
public class CourseController {

	@Autowired
	private CourseService courseService;

	// 確認輸入該輸入的值是否合規定
	private CourseRes courseParamCheck(CourseReq req) {
		CourseRes res = new CourseRes();
		if (!StringUtils.hasText(req.getId())) {
			res.setMessage(CourseRtnCode.ID_REQUIRED.getMessage());
			return res;
		}
		if (!StringUtils.hasText(req.getName())) {
			res.setMessage(CourseRtnCode.NAME_REQUIRED.getMessage());
			return res;
		}
		if (req.getDay() == null) {
			res.setMessage(CourseRtnCode.DAY_REQUIRED.getMessage());
			return res;
		}
		if (req.getStart() == null) {
			res.setMessage(CourseRtnCode.START_REQUIRED.getMessage());
			return res;
		}
		if (req.getEnd() == null) {
			res.setMessage(CourseRtnCode.END_REQUIRED.getMessage());
			return res;
		}
		//星期天數須符合邏輯
		if (req.getDay() < 1 || req.getDay() > 7) {
			res.setMessage(CourseRtnCode.DAY_FAIL.getMessage());
			return res;
		}

		//限制開始與結束時間
		if (req.getStart() < 8 || req.getStart() > 16 || req.getStart() > req.getEnd() || req.getEnd() < 9
				|| req.getEnd() > 17) {
			res.setMessage(CourseRtnCode.TIME_PARAM_ERROR.getMessage());
			return res;
		}
		if (req.getCredit() == null) {
			res.setMessage(CourseRtnCode.CREDIT_REQUIRED.getMessage());
			return res;
		}
		//單一課程不能超過3學分
		if (req.getCredit() < 1 || req.getCredit() > 3) {
			res.setMessage(CourseRtnCode.CREDIT_PARAM_ERROR.getMessage());
			return res;
		}
		return null;
	}

	// 確認輸入該輸入的值是否合規定
	private CourseRes studentParamCheck(CourseReq req) {
		
		if (!StringUtils.hasText(req.getStudentId())) {
			CourseRes res = new CourseRes();
			res.setMessage(CourseRtnCode.STUDENT_ID_REQUIRED.getMessage());
			return res;
		}
		if (!StringUtils.hasText(req.getStudentName())) {
			CourseRes res = new CourseRes();
			res.setMessage(CourseRtnCode.STUDENT_NAME_REQUIRED.getMessage());
			return res;
		}
		return null;
	}

	@PostMapping(value = "/api/create_course")
	public CourseRes createCourse(@RequestBody CourseReq req) {
		//確認輸入該輸入的值是否合規定,都有則回傳null
		CourseRes check = courseParamCheck(req);
		if (check != null) {
			return check;
		}
		
		CourseRes res = new CourseRes();
		Course course = courseService.createCourse(req.getId(), req.getName(), req.getDay(), req.getStart(),
				req.getEnd(), req.getCredit());
		//上面方法若ID不存在會回傳null
		if (course == null) {
			res.setMessage(CourseRtnCode.ID_EXIST.getMessage());
			return res;
		}
		
		res = new CourseRes(course);
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		return res;
	}

	@PostMapping(value = "/api/update_course")
	public CourseRes updateCourse(@RequestBody CourseReq req) {
		//確認輸入該輸入的值是否合規定
		CourseRes check = courseParamCheck(req);
		if (check != null) {
			return check;
		}

		CourseRes res = new CourseRes();
		Course course = courseService.updateCourse(req.getId(), req.getName(), req.getDay(), req.getStart(),
				req.getEnd(), req.getCredit());
		//上面方法若ID不存在會回傳null
		if (course == null) {
			res.setMessage(CourseRtnCode.ID_NOT_EXIST.getMessage());
			return res;
		}
		
		res = new CourseRes(course);
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		return res;
	}

	@PostMapping(value = "/api/delete_course")
	public CourseRes deleteCourse(@RequestBody CourseReq req) {
		CourseRes res = new CourseRes();
		//沒輸入所需值時,給予警示
		if (!StringUtils.hasText(req.getId())) {
			res.setMessage(CourseRtnCode.ID_REQUIRED.getMessage());
			return res;
		}
		
		return courseService.deleteCourse(req.getId());
	}

	@PostMapping(value = "/api/find_course_by_id_or_name")
	public CourseRes findCourseByIdOrName(@RequestBody CourseReq req) {
		CourseRes res = new CourseRes();
		//沒輸入所需值時,給予警示
		if (req.getIds() == null && req.getNames() == null) {
			res.setMessage(CourseRtnCode.ID_OR_NAME_REQUIRED.getMessage());
			return res;
		}
		
		return courseService.findCourseByIdOrName(req.getIds(), req.getNames());
	}

	@PostMapping(value = "/api/create_student")
	public CourseRes createStudent(@RequestBody CourseReq req) {
		//確認輸入該輸入的值是否合規定
		CourseRes check = studentParamCheck(req);
		if (check != null) {
			return check;
		}
		CourseRes res = new CourseRes();
		Student student = courseService.createStudent(req.getStudentId(), req.getStudentName());
		if (student == null) {
			res.setMessage(CourseRtnCode.ID_EXIST.getMessage());
			return res;
		}
		
		res = new CourseRes(student.getId(), student.getName());
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		return res;
	}

	@PostMapping(value = "/api/update_student")
	public CourseRes updateStudent(@RequestBody CourseReq req) {
		//確認輸入該輸入的值是否合規定
		CourseRes check = studentParamCheck(req);
		if (check != null) {
			return check;
		}
		CourseRes res = new CourseRes();
		Student student = courseService.updateStudent(req.getStudentId(), req.getStudentName());
		if (student == null) {
			res.setMessage(CourseRtnCode.STUDENT_ID_NOT_EXIST.getMessage());
			return res;
		}
		
		res = new CourseRes(student.getId(), student.getName());
		res.setMessage(CourseRtnCode.SUCCESS.getMessage());
		return res;
	}

	@PostMapping(value = "/api/delete_student")
	public CourseRes deleteStudent(@RequestBody CourseReq req) {
		CourseRes res = new CourseRes();
		//沒輸入所需值時,給予警示
		if (!StringUtils.hasText(req.getStudentId())) {
			res.setMessage(CourseRtnCode.STUDENT_ID_REQUIRED.getMessage());
			return res;
		}
		
		return courseService.deleteStudent(req.getStudentId());
	}

	@PostMapping(value = "/api/select_course")
	public CourseRes selectCourse(@RequestBody CourseReq req) {
		CourseRes res = new CourseRes();
		//沒輸入所需值時,給予警示
		if (!StringUtils.hasText(req.getStudentId())) {
			res.setMessage(CourseRtnCode.STUDENT_ID_REQUIRED.getMessage());
			return res;
		}
		if (req.getCourseList() == null) {
			res.setMessage(CourseRtnCode.COURSE_LIST_REQUIRED.getMessage());
			return res;
		}
		
		return courseService.courseSelection(req.getStudentId(), req.getCourseList());
	}

	@PostMapping(value = "/api/cancel_course")
	public CourseRes cancelCourse(@RequestBody CourseReq req) {
		CourseRes res = new CourseRes();
		//沒輸入所需值時,給予警示
		if (!StringUtils.hasText(req.getStudentId())) {
			res.setMessage(CourseRtnCode.STUDENT_ID_REQUIRED.getMessage());
			return res;
		}
		if (req.getCourseList() == null) {
			res.setMessage(CourseRtnCode.COURSE_LIST_REQUIRED.getMessage());
			return res;
		}
		
		return courseService.courseCancel(req.getStudentId(), req.getCourseList());
	}

	@PostMapping(value = "/api/find_student_info")
	public CourseRes findStudentInfo(@RequestBody CourseReq req) {
		CourseRes res = new CourseRes();
		//沒輸入所需值時,給予警示
		if (!StringUtils.hasText(req.getStudentId())) {
			res.setMessage(CourseRtnCode.STUDENT_ID_REQUIRED.getMessage());
			return res;
		}
		
		return courseService.findStudentInfo(req.getStudentId());
	}

}
