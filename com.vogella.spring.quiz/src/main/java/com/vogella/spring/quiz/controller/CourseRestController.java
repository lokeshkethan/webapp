package com.vogella.spring.quiz.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vogella.spring.quiz.entities.Course;
import com.vogella.spring.quiz.entities.Question;
import com.vogella.spring.quiz.entities.User;
import com.vogella.spring.quiz.repositories.CourseRepository;

@RestController
@RequestMapping("/api/course")   
public class CourseRestController {
	private CourseRepository courseRepo;
	
	public CourseRestController(CourseRepository courseRepo) {
		this.courseRepo = courseRepo;
	}
	
	@EventListener
    public void appReady(ApplicationReadyEvent event) {
		
		String courseName = "Java";
		int[] quiz = {1, 2, 3};
		//list for indicating the accomplished quizs, 1 for accomplished, 0 for not
		int[] quizAccomplished = {0, 0, 0};
		double progress = 0;
		String info = "Beginner Guide to Java";
		Course course = new Course(courseName, quiz, progress, info, quizAccomplished);
		this.courseRepo.save(course);
		
		course = new Course("Flutter", quiz, progress, "Short Flutter Intro", quizAccomplished);
		this.courseRepo.save(course);
	}
	
	
	@GetMapping("/{courseId}")
	public ResponseEntity<Course> getCourse(@PathVariable("courseId") Optional<Course> courseOptional) {
	/*	String courseName = "Java";
		int[] quiz = {1, 2, 3};
		double progress = 20;
		String info = "Beginner Guide to Java";
		long courseId = 1;
		Course course = new Course(courseName, quiz, progress, info, courseId);
		course = this.courseRepo.save(course);
	*/	
		//TODO this part skipped for id generation and example data
		/*
		//TODO use the id from example data
		if (!quizOptional.isPresent() ) {
   		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   	} */
		
		return new ResponseEntity<>(courseOptional.get(), HttpStatus.OK);
		//return new ResponseEntity<>(courseRepo.findByCourseId(course.getCourseId()), HttpStatus.OK); 
    }
	
	
	
	@PostMapping("/course")
	public Course addCourse(@Valid @RequestBody Course course) {
		return this.courseRepo.save(course);
	}
	
	@GetMapping
	public List<Course> getAll(){
		return this.courseRepo.findAll();
	}
	
	@PutMapping("/update/{id}")
	  Course replaceCourse(@RequestBody Course newCourse, @PathVariable Long id) {

	    return courseRepo.findById(id)
	      .map(course -> {
	        course.setCourseName(newCourse.getCourseName());
	        course.setQuiz(newCourse.getQuiz());
	        course.setQuizAccomplished(newCourse.getQuizAccomplished());
	        course.setProgress(newCourse.getProgress());
	        course.setInfo(newCourse.getInfo());
	        return courseRepo.save(course);
	      })
	      .orElseGet(() -> {
	        newCourse.setCourseId(id);
	        return courseRepo.save(newCourse);
	      });
	  }
}
