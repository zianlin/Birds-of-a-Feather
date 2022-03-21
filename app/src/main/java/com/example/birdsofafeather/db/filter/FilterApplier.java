package com.example.birdsofafeather.db.filter;

import com.example.birdsofafeather.db.Account;
import com.example.birdsofafeather.db.CourseDao;

import java.util.ArrayList;
import java.util.List;

public class FilterApplier {
    private Filter filter;

    private Account userAccount;
    private CourseDao courseDao;

    public FilterApplier(Filter filter, Account userAccount, CourseDao courseDao) {
        this.filter = filter;

        this.userAccount = userAccount;
        this.courseDao = courseDao;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<Account> applyFilter(List<Account> accounts) {
        List<Account> sorted = new ArrayList<Account>();

        // Filter out any accounts that have 0 priority
        for (Account acc : accounts) {
            if (filter.generatePriority(courseDao.getForAccount(userAccount.getId()), courseDao.getForAccount(acc.getId())) > 0) {
                sorted.add(acc);
            }
        }

        // Sort the courses according to their priority
        sorted.sort((a1, a2) -> {
            int p1 = filter.generatePriority(courseDao.getForAccount(userAccount.getId()), courseDao.getForAccount(a1.getId()));
            int p2 = filter.generatePriority(courseDao.getForAccount(userAccount.getId()), courseDao.getForAccount(a2.getId()));
            return p2 - p1;
        });

        return sorted;
    }
}