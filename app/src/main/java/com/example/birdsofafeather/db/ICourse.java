package com.example.birdsofafeather.db;

public interface ICourse
{
    int getCourseId();
    void setCourseId(int courseId);
    String getAccountId();
    void setAccountId(String accountId);
    String getQuarter();
    void setQuarter(String quarter);
    int getYear();
    void setYear(int year);
    String getDepartment();
    void setDepartment(String department);
    String getCourseCode();
    void setCourseCode(String courseCode);
    String getClassSize();
    void setClassSize(String classSize);
    boolean equals(ICourse other);
    String getCourseString();
}
