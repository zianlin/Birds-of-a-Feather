package com.example.birdsofafeather.db;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JoinDao
{
    @Query("SELECT c2.account_id FROM courses AS c1 " +
            "INNER JOIN courses AS c2 " +
            "ON c1.quarter = c2.quarter " +
            "AND c1.year = c2.year " +
            "AND c1.department = c2.department " +
            "AND c1.course_code = c2.course_code " +
            "WHERE c1.account_id = :myId " +
            "AND c2.account_id != :myId " +
            "GROUP BY c2.account_id " +
            "ORDER BY COUNT(*) DESC")
    List<String> getBofIds(String myId);

    @Query("SELECT c2.course_id " +
            "FROM courses AS c1 " +
            "INNER JOIN courses AS c2 " +
            "ON c1.quarter = c2.quarter " +
            "AND c1.year = c2.year " +
            "AND c1.department = c2.department " +
            "AND c1.course_code = c2.course_code " +
            "WHERE c1.account_id = :myId " +
            "AND c2.account_id = :otherId")
    List<Integer> getSharedCourseIds(String myId, String otherId);

    @Query("SELECT COUNT(c2.course_id) " +
            "FROM courses AS c1 " +
            "INNER JOIN courses AS c2 " +
            "ON c1.quarter = c2.quarter " +
            "AND c1.year = c2.year " +
            "AND c1.department = c2.department " +
            "AND c1.course_code = c2.course_code " +
            "WHERE c1.account_id = :myId " +
            "AND c2.account_id = :otherId")
    int getSharedCourseCount(String myId, String otherId);
}
