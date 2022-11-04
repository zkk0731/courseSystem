package com.example.demo2.constants;

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
	NO_COURSE_SELECTED("403","No course is selected"),
	SAME_COURSE_SELECTED("403","Same name course is selected");
	
	
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