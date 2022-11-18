package com.example.coursesystem.constants;

public enum CourseRtnCode {

	SUCCESS("200","Success"),
	ID_REQUIRED("400","Id required"),
	NAME_REQUIRED("400","Name required"),
	NAME_NOT_EXIST("400","Name does not exist"),
	DAY_FAIL("400","Day is wrong"),
	DAY_REQUIRED("400","Day required"),
	START_REQUIRED("400","Start time required"),
	END_REQUIRED("400","End time required"),
	TIME_PARAM_ERROR("403","Course start or end time error"),
	CREDIT_REQUIRED("400","Credit required"),
	ID_EXIST("400","Id already exist"),
	ID_NOT_EXIST("400","Id does not exist"),
	CREDIT_PARAM_ERROR("400","Credit param error"),
	STUDENT_ID_REQUIRED("400","Student id required"),
	STUDENT_NAME_REQUIRED("400","Student name required"),
	STUDENT_ID_NOT_EXIST("400","Student id does not exist"),
	CLASS_TIME_CONFLICT("403","Course time conflict"),
	CREDIT_TOTAL_OVER("403","Total credit is more than 10"),
	COURSE_NOT_EXIST("400","Select course not exist"),
	NO_COURSE_SELECTED("403","Student don't have any course"),
	SAME_NAME_COURSE_SELECTED("403","Same name course is selected"),
	SAME_ID_COURSE_SELECTED("403","Same id course is selected"),
	STUDENT_DONT_HAVE_THIS_COURSE("403","Student not have this courses"),
	ID_OR_NAME_REQUIRED("400","Id or name required"),
	COURSE_LIST_REQUIRED("400","Courses required"),
	NO_RESULT("403","No result"),
	NEED_CANCEL_ALL_COURSE("403","Student must cancel all course"),
	COURSE_CANNOT_BE_SELECTED_MORE_THAN_5("403","Course can't be selected by more than 5 students"),
	WARNING("403","Warning");
	
	
	private String code;
	
	private String message;

	private CourseRtnCode(String code,String message) {
		this.code = code;
		this.message = message;
	}
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	
}
