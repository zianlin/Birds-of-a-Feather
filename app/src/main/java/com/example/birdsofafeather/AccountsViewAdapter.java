package com.example.birdsofafeather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.db.AccountDao;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.IAccount;

import java.util.List;

public class AccountsViewAdapter extends RecyclerView.Adapter<AccountsViewAdapter.ViewHolder> {
    private final List<? extends IAccount> accounts;

    public AccountsViewAdapter(List<? extends IAccount> accounts) {
        super();
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public AccountsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.account_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewAdapter.ViewHolder holder, int position) {
        holder.setAccount(accounts.get(position));
    }

    @Override
    public int getItemCount() { return this.accounts.size(); }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final TextView accountNameView;
        private final TextView sharedCourseNumberView;
        private final ImageView profilePictureView;
        private IAccount account;

        ViewHolder(View itemView) {
            super(itemView);
            this.accountNameView = itemView.findViewById(R.id.account_row_name);
            this.profilePictureView = itemView.findViewById(R.id.profile_picture_view);
            this.sharedCourseNumberView = itemView.findViewById(R.id.shared_course_number);
            itemView.setOnClickListener(this);
        }

        public void setAccount(IAccount account) {
            this.account = account;
            this.accountNameView.setText(account.getName());
            this.sharedCourseNumberView.setText("");
            ProfilePicture profilePicture = new ProfilePicture((Activity) itemView.getContext(),
                    profilePictureView,
                    account.getId(),
                    AppDatabase.singleton(itemView.getContext()).accountDao());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, AccountDetailActivity.class);
            intent.putExtra("account_id", account.getId());
            context.startActivity(intent);
        }
    }
}
