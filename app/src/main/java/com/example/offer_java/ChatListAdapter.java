package com.example.offer_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<User> users; // Assuming you have a User model
    private LayoutInflater inflater;

    public ChatListAdapter(Context context, List<User> users) {
        this.inflater = LayoutInflater.from(context);
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.userNameTextView.setText(user.getName());
        holder.lastMessageTextView.setText(user.getLastMessage());
        // Here you could also set the user image
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, lastMessageTextView;
        ImageView userImageView;

        ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            userImageView = itemView.findViewById(R.id.userImageView);
        }
    }
}