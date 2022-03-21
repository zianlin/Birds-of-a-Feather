package com.example.birdsofafeather.db.filter;

import com.example.birdsofafeather.db.Course;

import java.util.List;

public interface Filter {
    public int generatePriority(List<Course> myCourses, List<Course> otherCourses);
}
