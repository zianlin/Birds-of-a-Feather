package com.example.birdsofafeather.db.filter;

import com.example.birdsofafeather.db.Course;

import java.util.List;

public class DefaultFilter implements Filter {
    @Override
    public int generatePriority(List<Course> myCourses, List<Course> otherCourses) {
        int priority = 0;
        for(Course c1 : myCourses)
        {
            for(Course c2 : otherCourses) {
                if (c1.equals(c2)) {
                    priority++;
                }
            }
        }
        return priority;
    }
}
