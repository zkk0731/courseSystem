package com.example.coursesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.coursesystem.entity.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, String>{

}
