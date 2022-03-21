package com.example.birdsofafeather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.db.Account;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.Course;
import com.example.birdsofafeather.db.IAccount;
import com.example.birdsofafeather.db.filter.CurrentOnlyFilter;
import com.example.birdsofafeather.db.filter.DefaultFilter;
import com.example.birdsofafeather.db.filter.Filter;
import com.example.birdsofafeather.db.filter.FilterApplier;
import com.example.birdsofafeather.db.filter.MostRecentFilter;
import com.example.birdsofafeather.db.filter.SmallClassSizeFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBirdsOfAFeatherActivity extends AppCompatActivity {
    protected static AppDatabase db;

    protected RecyclerView accountsRecyclerView;
    protected RecyclerView.LayoutManager accountsLayoutManager;
    protected AccountsViewAdapter accountsViewAdapter;

    Spinner spinner_filter;
    Filter filter;
    FilterApplier filterApplier;

    List<Account> unfilteredList;
    List<Account> filteredList;

    protected List<IAccount> data = Arrays.asList(
            new Account("John Smith", "john.smith@email.com", ""),
            new Account("William Griswold", "alice.johnson@email.com", "https://isr.uci.edu/sites/isr.uci.edu/files/styles/speaker-lg/public/speakers/pictures/billg_0.jpg?itok=9gOqEkei"),
            new Account("Bob Roberts", "bob.roberts@email.com", ""),
            new Account("Bill Williams", "bill.williams@email.com", "")
    );

    protected List<Course> courses0 = Arrays.asList(
            new Course(data.get(0).getId(), "SP", 2022, "CSE", "110", "Large"),
            new Course(data.get(0).getId(), "WI", 2019, "MAE", "100A", "Tiny")
    );

    protected List<Course> courses1 = Arrays.asList(
            new Course(data.get(1).getId(), "FA", 2021, "ECE", "35", "Medium"),
            new Course(data.get(1).getId(), "WI", 2020, "CSE", "12", "Huge"),
            new Course(data.get(1).getId(), "WI", 2022, "CSE", "15L", "Gigantic"),
            new Course(data.get(1).getId(), "SP", 2022, "CSE", "110", "Large")
    );

    protected List<Course> courses2 = Arrays.asList(
            new Course(data.get(2).getId(), "SS1", 2020,"CSE","140", "Large"),
            new Course(data.get(2).getId(), "WI", 2020, "CSE", "12", "Huge"),
            new Course(data.get(2).getId(), "WI", 2019, "MAE", "100A", "Tiny")
    );

    protected List<Course> courses3 = Arrays.asList(
            new Course(data.get(3).getId(), "FA", 1999, "PHIL", "27", "Small"),
            new Course(data.get(3).getId(), "WI", 2000, "PHIL", "28", "Medium")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bof);
        setTitle("List Birds of a Feather");

        // Temporary- add accounts and courses to database
        db = AppDatabase.singleton(this);
        if(db.accountDao().count() <= 1) {
            for (IAccount acc : data) db.accountDao().insert((Account) acc);
            for (Course course : courses0) db.courseDao().insert(course);
            for (Course course : courses1) db.courseDao().insert(course);
            for (Course course : courses2) db.courseDao().insert(course);
            for (Course course : courses3) db.courseDao().insert(course);
        }

        //Instantiate Spinner
        spinner_filter = findViewById(R.id.spinner_filter);
        ArrayAdapter<CharSequence> adapter_filter = ArrayAdapter.createFromResource(this,
                R.array.filters, android.R.layout.simple_spinner_item);
        adapter_filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_filter.setAdapter(adapter_filter);

        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);


        /*
        // Temporary- assign the user to shared preferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_account_id", 2);
        editor.apply();
         */

        String userAccountId = preferences.getString("user_account_id", "");
        Account userAccount = db.accountDao().get(userAccountId);

        // TODO use bluetooth to get nearbyAccounts
        List<IAccount> nearbyAccounts = new ArrayList();
        // Temporary- get nearbyAccounts that aren't the user's account
        for (IAccount acct : data) {
            if (acct.getId() != userAccount.getId()) nearbyAccounts.add(acct);
        }

        // Only get accounts that have shared courses with the user
        List<String> nearbySharedIds = db.joinDao().getBofIds(userAccountId);
        unfilteredList = new ArrayList<Account>();
        for(String id : nearbySharedIds)
        {
            unfilteredList.add(db.accountDao().get(id));
        }

        accountsRecyclerView = findViewById(R.id.accounts_view);

        accountsLayoutManager = new LinearLayoutManager(this);
        accountsRecyclerView.setLayoutManager(accountsLayoutManager);

        filter = new DefaultFilter();
        filterApplier = new FilterApplier(filter, userAccount, db.courseDao());
        updateFilter();
    }

    public void onClickedStopSearch(View view) {
        finish();
    }

    public void onClickedApplyFilter(View view) {
        updateFilter();
    }

    private void updateFilter(){
        String filterString = spinner_filter.getSelectedItem().toString();

        switch(filterString){

            case "prioritize small class size":
                filter = new SmallClassSizeFilter();
                Toast.makeText(this, "Applied Filter: prioritize small class size" , Toast.LENGTH_SHORT).show();
                break;

            case "prioritize most recent":
                filter = new MostRecentFilter();
                Toast.makeText(this, "Applied Filter: most recent" , Toast.LENGTH_SHORT).show();
                break;

            case "current qtr only":
                filter = new CurrentOnlyFilter("WI", 2022);
                Toast.makeText(this, "Applied Filter: current quarter only" , Toast.LENGTH_SHORT).show();
                break;

            default:
                filter = new DefaultFilter();
                break;
        }

        filterApplier.setFilter(filter);
        filteredList = filterApplier.applyFilter(unfilteredList);

        accountsViewAdapter = new AccountsViewAdapter(filteredList);
        accountsRecyclerView.setAdapter(accountsViewAdapter);
    }
}
