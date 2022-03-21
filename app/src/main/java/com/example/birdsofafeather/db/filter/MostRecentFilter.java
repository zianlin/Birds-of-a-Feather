package com.example.birdsofafeather.db.filter;

import com.example.birdsofafeather.db.Course;

import java.util.List;

public class MostRecentFilter implements Filter {
    @Override
    public int generatePriority(List<Course> myCourses, List<Course> otherCourses){
        int priority = 0;
        int sharedClasses = 0;
        for(Course c1 : myCourses)
        {
            for(Course c2 : otherCourses) {
                if (c1.equals(c2)) {
                    int dateValue = c1.year * 6;
                    switch (c1.getQuarter()) {
                        case "WI":
                            dateValue += 1;
                            break;
                        case "SP":
                            dateValue += 2;
                            break;
                        case "SS1":
                            dateValue += 3;
                            break;
                        case "SS2":
                            dateValue += 4;
                            break;
                        case "SSS":
                            dateValue += 5;
                    }
                    if (priority == 1000 * dateValue) {
                        sharedClasses++;
                    } else if (priority < 1000 * dateValue) {
                        priority = 1000 * dateValue;
                        sharedClasses = 0;
                    }
                }
            }
        }
        priority += sharedClasses;
        return priority;
    }
}
