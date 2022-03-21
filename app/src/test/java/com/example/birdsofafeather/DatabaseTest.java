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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest
{
    private AppDatabase db;
    private AccountDao accountDao;
    private CourseDao courseDao;
    private JoinDao joinDao;

    @Before
    public void createDb()
    {
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase.useTestSingleton(context);
        db = AppDatabase.singleton(context);
        accountDao = db.accountDao();
        courseDao = db.courseDao();
        joinDao = db.joinDao();
    }

    @After
    public void closeDb() throws IOException
    {
        db.close();
    }

    @Test
    public void testDatabaseCount()
    {
        Account a1 = new Account("Slim Jim", "slimjim@gmail.com", "");
        Account a2 = new Account("Slender James", "slenderjames@gmail.com", "");

        accountDao.insert(a1);
        accountDao.insert(a2);

        Course c1 = new Course(a1.getId(), "Winter", 2000, "CSE", "110", "Tiny(1-40)");
        Course c2 = new Course(a2.getId(), "Winter", 2000, "CSE", "110", "Tiny(1-40)");

        courseDao.insert(c1);
        courseDao.insert(c2);

        boolean correctAccountCount = accountDao.count() == 2;
        boolean correctCourseCount = courseDao.count() == 2;

        assertTrue("Account count: " + accountDao.count() + " Course count: " + courseDao.count(),
                correctAccountCount && correctCourseCount);
    }

    @Test
    public void testDatabaseRetrieval()
    {
        Account a1 = new Account("Slim Jim", "slimjim@gmail.com", "");

        accountDao.insert(a1);

        Course c1 = new Course(a1.getId(), "Winter", 2000, "CSE", "110", "Tiny(1-40)");

        courseDao.insert(c1);

        boolean correctAccount = a1.equals(accountDao.get(a1.getId()));
        boolean correctCourse = c1.equals(courseDao.get(c1.getCourseId()));

        assertTrue((correctAccount? "Correct account, " : "Incorrect account, ") +
                        (correctCourse? "Correct course" : "Incorrect course"),
                correctAccount && correctCourse);
    }

    @Test
    public void correctCoursesForAccount()
    {
        Account a1 = new Account("Slim Jim", "slimjim@gmail.com", "");

        accountDao.insert(a1);

        Course c1 = new Course(a1.getId(), "Winter", 2000, "CSE", "140", "Tiny(1-40)");
        Course c2 = new Course(a1.getId(), "Winter", 2000, "CSE", "140L", "Tiny(1-40)");

        courseDao.insert(c1);
        courseDao.insert(c2);

        ArrayList<Course> courses = (ArrayList<Course>)courseDao.getForAccount(a1.getId());

        boolean correctCourses = (c1.equals(courses.get(0)) ||
                c1.equals(courses.get(1))) &&
                (c2.equals(courses.get(0)) ||
                        c2.equals(courses.get(1)));
        boolean coursesAreDifferent = !courses.get(0).equals(courses.get(1));

        assertTrue("Incorrect courses", correctCourses && coursesAreDifferent);
    }

    @Test
    public void successfulUpdate()
    {
        Account a1 = new Account("Bob The Builder", "btb@gmail.com","");
        Course c1 = new Course(a1.getId(), "Winter", 2000, "CSE", "140", "Tiny(1-40)");

        accountDao.insert(a1);
        courseDao.insert(c1);

        a1.setName("Robert The Foreman");
        c1.setYear(1999);

        accountDao.update(a1);
        courseDao.update(c1);

        assertEquals("Robert The Foreman", accountDao.get(a1.getId()).getName());
        assertEquals(1999, courseDao.get(c1.getCourseId()).getYear());
    }


    @Test
    public void successfulDelete()
    {
        Account a1 = new Account("Bob The Builder", "btb@gmail.com","");
        Course c1 = new Course(a1.getId(), "Winter", 2000, "CSE", "140", "Tiny(1-40)");

        accountDao.insert(a1);
        courseDao.insert(c1);
        accountDao.delete(a1);
        courseDao.delete(c1);

        assertNull(accountDao.get(a1.getId()));
        assertNull(courseDao.get(c1.getCourseId()));
    }

    @Test
    public void successfulUpdateFromId()
    {
        Account a1 = new Account("Bob The Builder", "btb@gmail.com","DEFAULT_PROFILE_PICTURE");
        Course c1 = new Course(a1.getId(), "Winter", 2000, "CSE", "140", "Tiny(1-40)");

        accountDao.insert(a1);
        courseDao.insert(c1);

        accountDao.updateNameFromId(a1.getId(), "Robert The Foreman");
        accountDao.updateProfilePictureURLFromId(a1.getId(), "URL");

        assertEquals("Robert The Foreman", accountDao.get(a1.getId()).getName());
        assertEquals("URL", accountDao.get(a1.getId()).getProfilePictureURL());
    }

    @Test
    public void correctBofList()
    {
        Account myAccount = new Account("Itsumi Mario", "mario@gmail.com", "P0");
        Course c0 = new Course(myAccount.getId(), "Winter", 2000, "MATH", "18", "Tiny(1-40)");
        Course c1 = new Course(myAccount.getId(), "Winter", 2000, "PHYS", "2A", "Tiny(1-40)");
        Course c2 = new Course(myAccount.getId(), "Winter", 2000, "CSE", "11", "Tiny(1-40)");
        Course c3 = new Course(myAccount.getId(), "Winter", 2000, "CAT", "125", "Tiny(1-40)");

        //They're the "Mario Bros" so their last name must be Mario.
        Account mostShared = new Account("Luigi Mario", "luigi@gmail.com", "P1");
        Course c4 = new Course(mostShared.getId(), "Winter", 2000, "MATH", "18", "Tiny(1-40)");
        Course c5 = new Course(mostShared.getId(), "Winter", 2000, "PHYS", "2A", "Tiny(1-40)");
        Course c6 = new Course(mostShared.getId(), "Winter", 2000, "CSE", "11", "Tiny(1-40)");
        Course c7 = new Course(mostShared.getId(), "Winter", 2000, "CAT", "125", "Tiny(1-40)");

        Account otherAccount = new Account("Rob Boss", "theboss@gmail.com", "P2");
        Course c8 = new Course(otherAccount.getId(), "Spring", 1999, "PHYS", "2A", "Tiny(1-40)");
        Course c9 = new Course(otherAccount.getId(), "Fall", 2000, "PHYS", "2B", "Tiny(1-40)");
        Course c10 = new Course(otherAccount.getId(), "Winter", 2000, "CSE", "11", "Tiny(1-40)");
        Course c11 = new Course(otherAccount.getId(), "Winter", 2000, "CAT", "125", "Tiny(1-40)");

        Account leastShared = new Account("laughing_emoji", "crying_emoji", "P3");
        Course c12 = new Course(leastShared.getId(), "Winter", 1999, "CSE", "11", "Tiny(1-40)");
        Course c13 = new Course(leastShared.getId(), "Spring", 1999, "PHYS", "2A", "Tiny(1-40)");
        Course c14 = new Course(leastShared.getId(), "Fall", 2000, "PHYS", "2B", "Tiny(1-40)");
        Course c15 = new Course(leastShared.getId(), "Winter", 2000, "CAT", "125", "Tiny(1-40)");

        Account noShared = new Account("Depressed Student", "ds@gmail.com", "P4");
        Course c16 = new Course(noShared.getId(), "Winter", 2022, "CSE", "110", "Tiny(1-40)");
        Course c17 = new Course(noShared.getId(), "Winter", 2022, "CSE", "176E", "Tiny(1-40)");
        Course c18 = new Course(noShared.getId(), "Winter", 2022, "ECE", "101", "Tiny(1-40)");
        Course c19 = new Course(noShared.getId(), "Winter", 2022, "ECE", "111", "Tiny(1-40)");

        ArrayList<Account> accounts = new ArrayList<Account>();
        accounts.add(myAccount);
        accounts.add(mostShared);
        accounts.add(otherAccount);
        accounts.add(leastShared);
        accounts.add(noShared);
        insertAccountsRandomOrder(accounts);

        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(c0);
        courses.add(c1);
        courses.add(c2);
        courses.add(c3);
        courses.add(c4);
        courses.add(c5);
        courses.add(c6);
        courses.add(c7);
        courses.add(c8);
        courses.add(c9);
        courses.add(c10);
        courses.add(c11);
        courses.add(c12);
        courses.add(c13);
        courses.add(c14);
        courses.add(c15);
        courses.add(c16);
        courses.add(c17);
        courses.add(c18);
        courses.add(c19);
        insertCoursesRandomOrder(courses);

        ArrayList<String> ids = (ArrayList<String>)joinDao.getBofIds(myAccount.getId());
        assertEquals(3, ids.size());
        assertEquals(mostShared.getId(), ids.get(0));
        assertEquals(otherAccount.getId(), ids.get(1));
        assertEquals(leastShared.getId(), ids.get(2));
    }

    @Test
    public void correctSharedCourses()
    {
        Account a0 = new Account("Itsumi Mario", "mario@gmail.com", "P0");
        Course c0 = new Course(a0.getId(), "Winter", 2000, "MATH", "18", "Tiny(1-40)");
        Course c1 = new Course(a0.getId(), "Winter", 2000, "PHYS", "2A", "Tiny(1-40)");
        Course c2 = new Course(a0.getId(), "Winter", 2000, "CSE", "11", "Tiny(1-40)");
        Course c3 = new Course(a0.getId(), "Winter", 2000, "CAT", "125", "Tiny(1-40)");

        //They're the "Mario Bros" so their last name must be Mario.
        Account a1 = new Account("Luigi Mario", "luigi@gmail.com", "P1");
        Course c4 = new Course(a1.getId(), "Winter", 2000, "MATH", "18", "Tiny(1-40)");
        Course c5 = new Course(a1.getId(), "Winter", 2000, "PHYS", "2A", "Tiny(1-40)");
        Course c6 = new Course(a1.getId(), "Winter", 2000, "CSE", "11", "Tiny(1-40)");
        Course c7 = new Course(a1.getId(), "Winter", 2000, "CAT", "125", "Tiny(1-40)");

        Account a2 = new Account("Rob Boss", "theboss@gmail.com", "P2");
        Course c8 = new Course(a2.getId(), "Spring", 1999, "PHYS", "2A", "Tiny(1-40)");
        Course c9 = new Course(a2.getId(), "Fall", 2000, "PHYS", "2B", "Tiny(1-40)");
        Course c10 = new Course(a2.getId(), "Winter", 2000, "CSE", "11", "Tiny(1-40)");
        Course c11 = new Course(a2.getId(), "Winter", 2000, "CAT", "125", "Tiny(1-40)");

        Account a3 = new Account("laughing_emoji", "crying_emoji", "P3");
        Course c12 = new Course(a3.getId(), "Winter", 1999, "CSE", "11", "Tiny(1-40)");
        Course c13 = new Course(a3.getId(), "Spring", 1999, "PHYS", "2A", "Tiny(1-40)");
        Course c14 = new Course(a3.getId(), "Fall", 2000, "PHYS", "2B", "Tiny(1-40)");
        Course c15 = new Course(a3.getId(), "Winter", 2000, "CAT", "125", "Tiny(1-40)");

        Account a4 = new Account("Depressed Student", "ds@gmail.com", "P4");
        Course c16 = new Course(a4.getId(), "Winter", 2022, "CSE", "110", "Tiny(1-40)");
        Course c17 = new Course(a4.getId(), "Winter", 2022, "CSE", "176E", "Tiny(1-40)");
        Course c18 = new Course(a4.getId(), "Winter", 2022, "ECE", "101", "Tiny(1-40)");
        Course c19 = new Course(a4.getId(), "Winter", 2022, "ECE", "111", "Tiny(1-40)");

        ArrayList<Account> accounts = new ArrayList<Account>();
        accounts.add(a0);
        accounts.add(a1);
        accounts.add(a2);
        accounts.add(a3);
        accounts.add(a4);
        insertAccountsRandomOrder(accounts);

        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(c0);
        courses.add(c1);
        courses.add(c2);
        courses.add(c3);
        courses.add(c4);
        courses.add(c5);
        courses.add(c6);
        courses.add(c7);
        courses.add(c8);
        courses.add(c9);
        courses.add(c10);
        courses.add(c11);
        courses.add(c12);
        courses.add(c13);
        courses.add(c14);
        courses.add(c15);
        courses.add(c16);
        courses.add(c17);
        courses.add(c18);
        courses.add(c19);
        insertCoursesRandomOrder(courses);

        ArrayList<Integer> a0_a1_ids = (ArrayList<Integer>)joinDao.getSharedCourseIds(a0.getId(), a1.getId());
        Integer[] correct_a0_a1_ids = {c4.getCourseId(), c5.getCourseId(), c6.getCourseId(), c7.getCourseId()};
        ArrayList<Integer> a0_a2_ids = (ArrayList<Integer>)joinDao.getSharedCourseIds(a0.getId(), a2.getId());
        Integer[] correct_a0_a2_ids = {c10.getCourseId(), c11.getCourseId()};
        ArrayList<Integer> a0_a4_ids = (ArrayList<Integer>)joinDao.getSharedCourseIds(a0.getId(), a4.getId());
        assertArrayEquals(correct_a0_a1_ids, a0_a1_ids.toArray());
        assertArrayEquals(correct_a0_a2_ids, a0_a2_ids.toArray());
        assertEquals(0, a0_a4_ids.size());
    }

    private void insertAccountsRandomOrder(ArrayList<Account> toInsert)
    {
        Random r = new Random();
        while(toInsert.size() > 0)
        {
            int index = r.nextInt(toInsert.size());
            accountDao.insert(toInsert.remove(index));
        }
    }

    private void insertCoursesRandomOrder(ArrayList<Course> toInsert)
    {
        Random r = new Random();
        while(toInsert.size() > 0)
        {
            int index = r.nextInt(toInsert.size());
            courseDao.insert(toInsert.remove(index));
        }
    }
}
