package com.example.birdsofafeather.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course implements ICourse
{
    private static int nextId;

    @PrimaryKey
    @ColumnInfo(name = "course_id")
    private int courseId;

    @ColumnInfo(name = "account_id")
    private String accountId;

    @ColumnInfo(name = "quarter")
    public String quarter;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "department")
    public String department;

    @ColumnInfo(name = "course_code")
    public String courseCode;

    @ColumnInfo(name = "class_size")
    private String classSize;

    public Course(String accountId, String quarter, int year, String department, String courseCode, String classSize)
    {
        this.courseId = nextId;
        nextId++;
        this.accountId = accountId;
        this.quarter = quarter;
        this.year = year;
        this.department = department;
        this.courseCode = courseCode;
        this.classSize = classSize;
    }
    @Override
    public int getCourseId() { return this.courseId; }

    @Override
    public void setCourseId(int courseId) { this.courseId = courseId; }

    @Override
    public String getAccountId() { return this.accountId; }

    @Override
    public void setAccountId(String accountId) { this.accountId = accountId; }

    @Override
    public String getQuarter()
    {
        return this.quarter;
    }

    @Override
    public void setQuarter(String quarter) { this.quarter = quarter; }

    @Override
    public int getYear()
    {
        return this.year;
    }

    @Override
    public void setYear(int year) { this.year = year; }

    @Override
    public String getDepartment()
    {
        return this.department;
    }

    @Override
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String getCourseCode()
    {
        return this.courseCode;
    }

    @Override
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    @Override
    public String getClassSize()
    {
        return this.classSize;
    }

    @Override
    public void setClassSize(String classSize)
    {
        this.classSize = classSize;
    }

    @Override
    public boolean equals(ICourse other)
    {
        return this.quarter.equals(other.getQuarter()) &&
                this.year == other.getYear() &&
                this.department.equals(other.getDepartment()) &&
                this.courseCode.equals(other.getCourseCode());
    }

    @Override
    public String getCourseString() {
        String courseString = year+" "+quarter+" "+department+" "+courseCode+" "+classSize;
        return courseString;
    }
}