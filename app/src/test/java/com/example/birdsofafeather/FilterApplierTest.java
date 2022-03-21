package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.db.Account;
import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.Course;
import com.example.birdsofafeather.db.CourseDao;
import com.example.birdsofafeather.db.filter.CurrentOnlyFilter;
import com.example.birdsofafeather.db.filter.DefaultFilter;
import com.example.birdsofafeather.db.filter.FilterApplier;
import com.example.birdsofafeather.db.filter.MostRecentFilter;
import com.example.birdsofafeather.db.filter.SmallClassSizeFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FilterApplierTest {
    private AppDatabase db;
    private AccountDao accountDao;
    private CourseDao courseDao;

    private Account myAccount;
    private ArrayList<Account> accounts;

    private CurrentOnlyFilter currentOnlyFilter;
    private DefaultFilter defaultFilter;
    private MostRecentFilter mostRecentFilter;
    private SmallClassSizeFilter smallClassSizeFilter;

    private FilterApplier filterApplier;

    @Before
    public void createFilter()
    {
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase.useTestSingleton(context);
        this.db = AppDatabase.singleton(context);
        this.accountDao = db.accountDao();
        this.courseDao = db.courseDao();

        this.myAccount = new Account("Bob Roberts", "bob.roberts@email.com", "pfp0.com");
        accountDao.insert(myAccount);

        this.accounts = new ArrayList<Account>();
        accounts.add(new Account("a", "a@gmail.com", ""));
        accounts.add(new Account("b", "b@gmail.com", ""));
        accounts.add(new Account("c", "c@gmail.com", ""));
        accounts.add(new Account("d", "d@gmail.com", ""));
        for (Account acc : accounts) accountDao.insert(acc);

        this.currentOnlyFilter = new CurrentOnlyFilter("WI", 2022);
        this.defaultFilter = new DefaultFilter();
        this.mostRecentFilter = new MostRecentFilter();
        this.smallClassSizeFilter = new SmallClassSizeFilter();

        this.filterApplier = new FilterApplier(defaultFilter, myAccount, courseDao);
    }

    @Before
    public void createCourses()
    {
        ArrayList<Course> myCourses = new ArrayList<Course>();
        myCourses.add(new Course("bob.roberts@email.com", "FA", 2020, "CSE", "101", "Large"));
        myCourses.add(new Course("bob.roberts@email.com", "WI", 2021, "CSE", "100", "Tiny"));
        myCourses.add(new Course("bob.roberts@email.com", "WI", 2021, "ECE", "101", "Medium"));
        myCourses.add(new Course("bob.roberts@email.com", "SP", 2022, "ECE", "45", "Small"));
        myCourses.add(new Course("bob.roberts@email.com", "WI", 2022, "CSE", "110", "Large"));
        myCourses.add(new Course("bob.roberts@email.com", "WI", 2022, "CSE", "105", "Huge"));
        for (Course c : myCourses) courseDao.insert(c);

        ArrayList<Course> otherCourses1 = new ArrayList<Course>();
        otherCourses1.add(new Course(accounts.get(0).getId(), "FA", 2020, "CSE", "101", "Large"));
        otherCourses1.add(new Course(accounts.get(0).getId(), "WI", 2021, "CSE", "100", "Tiny"));
        otherCourses1.add(new Course(accounts.get(0).getId(), "WI", 2021, "ECE", "101", "Medium"));
        otherCourses1.add(new Course(accounts.get(0).getId(), "SP", 2022, "ECE", "45", "Small"));
        otherCourses1.add(new Course(accounts.get(0).getId(), "WI", 2022, "CSE", "110", "Large"));
        otherCourses1.add(new Course(accounts.get(0).getId(), "WI", 2022, "CSE", "105", "Huge"));
        for (Course c : otherCourses1) courseDao.insert(c);

        ArrayList<Course> otherCourses2 = new ArrayList<Course>();
        otherCourses2.add(new Course(accounts.get(1).getId(), "FA", 2020, "CSE", "101", "Large"));
        otherCourses2.add(new Course(accounts.get(1).getId(), "WI", 2021, "CSE", "100", "Tiny"));
        otherCourses2.add(new Course(accounts.get(1).getId(), "WI", 2021, "ECE", "101", "Medium"));
        for (Course c : otherCourses2) courseDao.insert(c);

        ArrayList<Course> otherCourses3 = new ArrayList<Course>();
        otherCourses3.add(new Course(accounts.get(2).getId(), "FA", 2020, "CSE", "101", "Large"));
        otherCourses3.add(new Course(accounts.get(2).getId(), "WI", 2021, "CSE", "100", "Tiny"));
        for (Course c : otherCourses3) courseDao.insert(c);

        ArrayList<Course> otherCourses4 = new ArrayList<Course>();
        otherCourses4.add(new Course(accounts.get(3).getId(), "WI", 2022, "CSE", "110", "Large"));
        for (Course c : otherCourses4) courseDao.insert(c);
    }

    @Test
    public void testDefaultFilterApplication() {
        filterApplier = new FilterApplier(defaultFilter, myAccount, courseDao);
        List<Account> filtered = filterApplier.applyFilter(accounts);

        assertEquals(accounts.size(), filtered.size());
        assertEquals(accounts.get(0).getName(), filtered.get(0).getName());
        assertEquals(accounts.get(1).getName(), filtered.get(1).getName());
        assertEquals(accounts.get(2).getName(), filtered.get(2).getName());
        assertEquals(accounts.get(3).getName(), filtered.get(3).getName());
    }

    @Test
    public void testCurrentOnlyFilterApplication() {
        filterApplier = new FilterApplier(currentOnlyFilter, myAccount, courseDao);
        List<Account> filtered = filterApplier.applyFilter(accounts);

        assertEquals(2, filtered.size());
        assertEquals(accounts.get(0).getName(), filtered.get(0).getName());
        assertEquals(accounts.get(3).getName(), filtered.get(1).getName());
    }

    @Test
    public void testMostRecentFilterApplication() {
        filterApplier = new FilterApplier(mostRecentFilter, myAccount, courseDao);
        List<Account> filtered = filterApplier.applyFilter(accounts);

        assertEquals(accounts.size(), filtered.size());
        assertEquals(accounts.get(0).getName(), filtered.get(0).getName());
        assertEquals(accounts.get(3).getName(), filtered.get(1).getName());
        assertEquals(accounts.get(1).getName(), filtered.get(2).getName());
        assertEquals(accounts.get(2).getName(), filtered.get(3).getName());
    }

    @Test
    public void testSmallClassSizeFilterApplication() {
        filterApplier = new FilterApplier(smallClassSizeFilter, myAccount, courseDao);
        List<Account> filtered = filterApplier.applyFilter(accounts);

        assertEquals(accounts.size(), filtered.size());
        assertEquals(accounts.get(0).getName(), filtered.get(0).getName());
        assertEquals(accounts.get(1).getName(), filtered.get(1).getName());
        assertEquals(accounts.get(2).getName(), filtered.get(2).getName());
        assertEquals(accounts.get(3).getName(), filtered.get(3).getName());
    }

    @After
    public void teardown() {
        accounts.clear();
        db.close();
    }
}
