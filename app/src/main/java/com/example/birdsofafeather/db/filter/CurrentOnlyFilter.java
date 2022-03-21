package com.example.birdsofafeather.db.filter;

import com.example.birdsofafeather.db.Course;

import java.util.Calendar;
import java.util.List;

public class CurrentOnlyFilter implements Filter {
    private String quarter;
    private int year;

    public CurrentOnlyFilter(String quarter, int year)
    {
        this.quarter = quarter;
        this.year = year;
    }

    @Override
    public int generatePriority(List<Course> myCourses, List<Course> otherCourses) {
        int priority = 0;
        for(Course c1 : myCourses)
        {
            for(Course c2 : otherCourses)
            if(c1.getYear() == year && c1.getQuarter().equals(quarter) && c1.equals(c2))
            {
                priority++;
            }
        }
        return priority;
    }
}
