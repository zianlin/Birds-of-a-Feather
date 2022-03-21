package com.example.birdsofafeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.db.ICourse;

import java.util.List;

public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.ViewHolder> {
    private final List<? extends ICourse> courses;

    public CoursesViewAdapter(List<? extends ICourse> courses) {
        super();
        this.courses = courses;
    }

    @NonNull
    @Override
    public CoursesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewAdapter.ViewHolder holder, int position) {
        holder.setCourse(courses.get(position));
    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseDeptCodeView;
        private final TextView courseQuarterYearView;

        private ICourse course;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseDeptCodeView = itemView.findViewById(R.id.course_row_dept_code);
            this.courseQuarterYearView = itemView.findViewById(R.id.course_row_quarter_year);
        }

        public void setCourse(ICourse course) {
            this.course = course;
            this.courseDeptCodeView.setText(course.getDepartment() + " " + course.getCourseCode());
            this.courseQuarterYearView.setText(course.getQuarter() + " " + course.getYear());
        }
    }
}
