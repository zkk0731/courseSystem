package com.example.demo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo2.entity.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, String>{

}
