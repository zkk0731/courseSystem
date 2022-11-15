package com.example.demo2;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.coursesystem.constants.CourseRtnCode;
import com.example.coursesystem.entity.Course;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.repository.CourseDao;
import com.example.coursesystem.repository.StudentDao;
import com.example.coursesystem.service.ifs.CourseService;
import com.example.coursesystem.vo.CourseRes;

@SpringBootTest
public class CourseImplTest {

//	@Autowired
//	private CourseDao courseDao;
//	@Autowired
//	private StudentDao studentDao;
	@Autowired
	private CourseService coursevice;
	
	@Test
	public void createCourseTset() {
		Course courseTest =  coursevice.createCourse("A99", "test", 1, 9, 10, 1);
		Assert.isTrue(courseTest.getId() != null, "id error");
		Assert.isTrue(courseTest.getName() != null, "id error");
		Assert.isTrue(courseTest.getDay() != 0, "id error");
		Assert.isTrue(courseTest.getStart() != 0, "id error");
		Assert.isTrue(courseTest.getEnd() != 0, "id error");
		Assert.isTrue(courseTest.getCredit() != 0, "id error");
	}
	
	@Test
	public void alterCourseTest() {
		Course courseTest =  coursevice.alterCourse("A99", "test", 2, 10, 11, 2);
		Assert.isTrue(courseTest.getId() == null, "id error");
		Assert.isTrue(courseTest.getName() == null, "id error");
		Assert.isTrue(courseTest.getDay() == 0, "id error");
		Assert.isTrue(courseTest.getStart() == 0, "id error");
		Assert.isTrue(courseTest.getEnd() == 0, "id error");
		Assert.isTrue(courseTest.getCredit() == 0, "id error");
	}
	
	@Test
	public void deleteCourseTest() {
		CourseRes res = coursevice.deleteCourse("A99");
		Assert.isTrue(res.getMessage().equals("Success"), "fail");
//		Assert.isTrue(res.getMessage().equals(CourseRtnCode.ID_NOT_EXIST.getMessage()), "success");
		
	}
	
	@Test
	public void createStudentTest() {
		Student student = coursevice.createStudent("999", "test1");
		Assert.isTrue(student.getId().equals("999"),"id fail");
		Assert.isTrue(student.getName().equals("test1"),"name fail");		
	}
	
	@Test
	public void alterStudentTest() {
		Student student = coursevice.alterStudent("998", "test2");
		Assert.isTrue(student.getId().equals("998"),"id fail");
		Assert.isTrue(student.getName().equals("test2"),"name fail");		
	}
	
	@Test
	public void deleteStudentTest() {
		CourseRes res = coursevice.deleteStudent("998");
		Assert.isTrue(!res.getMessage().equals("Success"),"seccess");
					
	}
	
	@Test
	public void courseSelectTest() {
		List<String> courseSel = new ArrayList<>();
		courseSel.add("A01");
		courseSel.add("A02");
		coursevice.createStudent("999", "test1");
		CourseRes res = coursevice.courseSelection("999", courseSel);
		System.out.println(res.getMessage());
		System.out.println(res.getMessageList());		
	}
	@Test
	public void courseCancelTest() {
		List<String> courseDel = new ArrayList<>();
		courseDel.add("A01");
		courseDel.add("A02");
		CourseRes res = coursevice.courseCancel("999", courseDel);
		System.out.println(res.getMessage());
		System.out.println(res.getMessageList());
	}
	
	@Test
	public void studentInfoTest() {
		CourseRes res = coursevice.findStudentInfo("999");
		System.out.println(res.getMessage());
//		System.out.println(res.getMessageList());
	}
	
}
