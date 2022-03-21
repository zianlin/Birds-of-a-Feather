package com.example.birdsofafeather;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.Course;

import java.util.List;

public class CourseInsertActivity extends AppCompatActivity {

    AppDatabase db;
    AccountDao accountDao;

    TextView textView_year;
    Spinner spinner_quarter;
    Spinner spinner_courseSize;
    TextView textView_department;
    TextView textView_courseCode;
    TextView textView_courseList;
    Button button_continue;

    private String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_insert);

        //AppDatabase.useTestSingleton(this);
        db = AppDatabase.singleton(this);
        accountDao = db.accountDao();

        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
        accountId = preferences.getString("user_account_id", "");

        textView_year = findViewById(R.id.textview_year);
        textView_department = findViewById(R.id.textview_department);
        textView_courseCode = findViewById(R.id.textview_courseCode);
        textView_courseList = findViewById(R.id.textview_courseList);
        button_continue = findViewById(R.id.button_continue);


        //Quarter Dropdown Menu
        spinner_quarter = findViewById(R.id.spinner_quarter);
        ArrayAdapter<CharSequence> adapter_qtr = ArrayAdapter.createFromResource(this,
                R.array.course_list, android.R.layout.simple_spinner_item);
        adapter_qtr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_quarter.setAdapter(adapter_qtr);

        //Course Size Dropdown Menu
        spinner_courseSize = findViewById(R.id.spinner_courseSize);
        ArrayAdapter<CharSequence> adapter_cs = ArrayAdapter.createFromResource(this,
                R.array.course_sizes, android.R.layout.simple_spinner_item);
        adapter_cs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_courseSize.setAdapter(adapter_cs);

        //List of course objects from database
        List<Course> courseList = db.courseDao().getForAccount(accountId);

        updateCourseList(courseList);

        if(textView_courseList.getText().toString().isEmpty()){
            button_continue.setEnabled(false);
        }
    }

    public void onClickedAddCourse(View view) {
        if(textView_year.getText().toString().isEmpty()             ||
                spinner_quarter.getSelectedItem().toString().isEmpty()   ||
                textView_department.getText().toString().isEmpty()       ||
                textView_courseCode.getText().toString().isEmpty()       ||
                spinner_courseSize.getSelectedItem().toString().isEmpty())
        {
            Utilities.showAlert(this,"NOT ALL FIELDS ENTERED", "You must enter something for each field in order to add a course!");
        }
        else if(!textView_year.getText().toString().isEmpty() && textView_year.getText().toString().length() != 4){
            Utilities.showAlert(this,"INVALID YEAR", "Year must be length 4");
        }
        else{

            int year = parseInt(textView_year.getText().toString());
            String quarter = spinner_quarter.getSelectedItem().toString();
            String department = textView_department.getText().toString().toUpperCase().trim();
            String courseCode = textView_courseCode.getText().toString().toUpperCase().trim();
            String courseSize = spinner_courseSize.getSelectedItem().toString();

            Course course = new Course(accountId, quarter, year, department, courseCode, courseSize);

            db.courseDao().insert(course);

            Toast.makeText(CourseInsertActivity.this, "Added course: "+year+" "+quarter+" "+department+" "+courseCode+" "+courseSize, Toast.LENGTH_SHORT).show();

            List<Course> courseList = db.courseDao().getForAccount(accountId);

            updateCourseList(courseList);
            // clearFields();
        }
    }

    public void updateCourseList(List<Course> courseList){
        String courseListText = "";

        for(Course course:courseList){

            courseListText = courseListText+course.getCourseString()+"\n";

        }

        textView_courseList.setText(courseListText);

        if (textView_courseList.getText().toString().isEmpty()) {
            button_continue.setEnabled(false);
        } else {
            button_continue.setEnabled(true);
        }
    }

    public void onClickedContinue(View view) {
        Intent continueIntent = new Intent(this, LaunchStudentSearchActivity.class);
        startActivity(continueIntent);
    }
}