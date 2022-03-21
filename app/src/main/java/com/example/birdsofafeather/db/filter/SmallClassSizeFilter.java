package com.example.birdsofafeather.db.filter;

import com.example.birdsofafeather.db.Course;

import java.util.List;

public class SmallClassSizeFilter implements Filter {
    @Override
    public int generatePriority(List<Course> myCourses, List<Course> otherCourses) {
        int priority = 0;
        for(Course c1 : myCourses)
        {
            for(Course c2 : otherCourses)
            {
                if(c1.equals(c2))
                {
                    switch(c1.getClassSize())
                    {
                        case "Tiny":
                            priority += 100;
                            break;

                        case "Small":
                            priority += 33;
                            break;

                        case "Medium":
                            priority += 18;
                            break;

                        case "Large":
                            priority += 10;
                            break;

                        case "Huge":
                            priority += 6;
                            break;

                        case "Gigantic":
                            priority += 3;
                    }
                }
            }
        }
        return priority;
    }
}
