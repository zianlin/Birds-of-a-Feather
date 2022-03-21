package com.example.birdsofafeather;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.birdsofafeather.db.Account;
import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.Course;
import com.example.birdsofafeather.db.CourseDao;
import com.example.birdsofafeather.db.JoinDao;
import com.example.birdsofafeather.db.filter.CurrentOnlyFilter;
import com.example.birdsofafeather.db.filter.DefaultFilter;
import com.example.birdsofafeather.db.filter.Filter;
import com.example.birdsofafeather.db.filter.MostRecentFilter;
import com.example.birdsofafeather.db.filter.SmallClassSizeFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class FilterTest {
    private ArrayList<Course> myCourses;
    private ArrayList<Course> otherCourses0;
    private ArrayList<Course> otherCourses1;
    private ArrayList<Course> otherCourses2;

    private CurrentOnlyFilter currentOnlyFilter;
    private DefaultFilter defaultFilter;
    private MostRecentFilter mostRecentFilter;
    private SmallClassSizeFilter smallClassSizeFilter;

    @Before
    public void createCourses()
    {
        myCourses = new ArrayList<Course>();
        myCourses.add(new Course("0", "FA", 2022, "CSE", "101", "Gigantic"));
        myCourses.add(new Course("0", "WI", 2022, "CSE", "100", "Large"));
        myCourses.add(new Course("0", "WI", 2021, "ECE", "101", "Medium"));
        myCourses.add(new Course("0", "SP", 2020, "ECE", "45", "Small"));
        myCourses.add(new Course("0", "SP", 2020, "ECE", "65", "Tiny"));

        otherCourses0 = new ArrayList<Course>();
        otherCourses0.add(new Course("1", "FA", 2022, "CSE", "101", "Gigantic"));
        otherCourses0.add(new Course("1", "WI", 2022, "CSE", "100", "Large"));

        otherCourses1 = new ArrayList<Course>();
        otherCourses1.add(new Course("2", "SP", 2020, "ECE", "45", "Small"));
        otherCourses1.add(new Course("2", "SP", 2020, "ECE", "65", "Tiny"));

        otherCourses2 = new ArrayList<Course>();
        otherCourses2.add(new Course("3", "WI", 2021, "ECE", "101", "Medium"));
    }

    @Before
    public void createFilter()
    {
        currentOnlyFilter = new CurrentOnlyFilter("WI", 2022);
        defaultFilter = new DefaultFilter();
        mostRecentFilter = new MostRecentFilter();
        smallClassSizeFilter = new SmallClassSizeFilter();
    }

    @Test
    public void testCurrentOnlyFilter()
    {
        assertTrue(currentOnlyFilter.generatePriority(myCourses, otherCourses0) != 0);
        assertTrue(currentOnlyFilter.generatePriority(myCourses, otherCourses1) == 0);
    }

    @Test
    public void defaultFilter()
    {
        assertTrue(defaultFilter.generatePriority(myCourses, otherCourses0) > defaultFilter.generatePriority(myCourses, otherCourses2));
    }

    @Test
    public void mostRecentFilter()
    {
        assertTrue(mostRecentFilter.generatePriority(myCourses, otherCourses0) > mostRecentFilter.generatePriority(myCourses, otherCourses1));
    }

    @Test
    public void smallClassSizeFilter()
    {
        assertTrue(smallClassSizeFilter.generatePriority(myCourses, otherCourses1) > smallClassSizeFilter.generatePriority(myCourses, otherCourses0));
    }
}
