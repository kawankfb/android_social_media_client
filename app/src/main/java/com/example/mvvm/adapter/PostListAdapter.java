package com.example.mvvm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mvvm.R;
import com.example.mvvm.model.PostModel;

import java.util.List;
import java.util.Random;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder> {
    private Context context;
    private ItemListener itemListener;
    private int user_id;
    public void setPostList(List<PostModel> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    private List<PostModel> postList;

    public PostListAdapter(Context context, List<PostModel> postList, ItemListener itemListener) {
        this.context = context;
        this.postList = postList;
        this.itemListener = itemListener;
        this.user_id=-1;
    }
    public PostListAdapter(Context context, List<PostModel> postList, ItemListener itemListener,int user_id) {
        this.context = context;
        this.postList = postList;
        this.itemListener = itemListener;
        this.user_id=user_id;
    }

    @NonNull
    @Override
    public PostListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.post_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.MyViewHolder holder, int position) {

        if (this.postList.get(position).getUser_id()==user_id) {

            holder.others.setVisibility(View.INVISIBLE);
            holder.personal.setVisibility(View.VISIBLE);


            holder.personal_text.setText(this.postList.get(position).getText().toString());
            holder.personal_post_last_edit_time.setText(this.postList.get(position).getUpdated_at());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onPostClick(postList.get(position));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemListener.onPostHold(postList.get(position));
                    return true;
                }
            });
            Log.d("image_URL", this.postList.get(position).getUrl());
            Glide.with(context).load(this.postList.get(position).getUrl()).circleCrop().into(holder.personal_postProfilePicture);
            if (holder.personal_postProfilePicture.getDrawable() == null) {
                String title = this.postList.get(position).getUserName().toString();
                String[] temp = title.split(" ");
                holder.personal_postNoProfileTextView.setVisibility(View.VISIBLE);
                holder.personal_postProfilePicture.setVisibility(View.INVISIBLE);
                if (temp.length == 1) {
                    StringBuilder tempBuilder = new StringBuilder();
                    tempBuilder.append(temp[0].toUpperCase().charAt(0));
                    holder.personal_postNoProfileTextView.setText(tempBuilder.toString());
                } else if (temp.length <= 1) {
                    holder.personal_postNoProfileTextView.setText("");
                } else {
                    StringBuilder tempBuilder = new StringBuilder();
                    tempBuilder.append(temp[0].toUpperCase().charAt(0));
                    tempBuilder.append(' ');
                    tempBuilder.append(temp[1].toUpperCase().charAt(0));
                    holder.personal_postNoProfileTextView.setText(tempBuilder.toString());

                }
                Drawable myIcon = context.getResources().getDrawable(R.drawable.profile_default);
                int toGenerateColor = (this.postList.get(position).getUser_id() % (Integer.MAX_VALUE / 16)) * 4;
                int red = toGenerateColor % 128 + 64;
                int blue = (toGenerateColor * 2) % 128 + 64;
                int green = (toGenerateColor * 3) % 128 + 64;
                ColorFilter filter = new LightingColorFilter(Color.BLACK, Color.rgb(red, blue, green));
                myIcon.setColorFilter(filter);
                holder.personal_postNoProfileTextView.setBackground(myIcon);
            } else {
                holder.personal_postNoProfileTextView.setVisibility(View.INVISIBLE);
                holder.personal_postProfilePicture.setVisibility(View.VISIBLE);
            }

        }

        else {

            holder.others.setVisibility(View.VISIBLE);
            holder.personal.setVisibility(View.INVISIBLE);


            holder.text.setText(this.postList.get(position).getText().toString());
            holder.post_last_edit_time.setText(this.postList.get(position).getUpdated_at());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onPostClick(postList.get(position));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemListener.onPostHold(postList.get(position));
                    return true;
                }
            });
            Log.d("image_URL", this.postList.get(position).getUrl());
            Glide.with(context).load(this.postList.get(position).getUrl()).circleCrop().into(holder.postProfilePicture);
            if (holder.postProfilePicture.getDrawable() == null) {
                String title = this.postList.get(position).getUserName().toString();
                String[] temp = title.split(" ");
                holder.postNoProfileTextView.setVisibility(View.VISIBLE);
                holder.postProfilePicture.setVisibility(View.INVISIBLE);
                if (temp.length == 1) {
                    StringBuilder tempBuilder = new StringBuilder();
                    tempBuilder.append(temp[0].toUpperCase().charAt(0));
                    holder.postNoProfileTextView.setText(tempBuilder.toString());
                } else if (temp.length <= 1) {
                    holder.postNoProfileTextView.setText("");
                } else {
                    StringBuilder tempBuilder = new StringBuilder();
                    tempBuilder.append(temp[0].toUpperCase().charAt(0));
                    tempBuilder.append(' ');
                    tempBuilder.append(temp[1].toUpperCase().charAt(0));
                    holder.postNoProfileTextView.setText(tempBuilder.toString());

                }
                Drawable myIcon = context.getResources().getDrawable(R.drawable.profile_default);
                int toGenerateColor = (this.postList.get(position).getUser_id() % (Integer.MAX_VALUE / 16)) * 4;
                int red = toGenerateColor % 128 + 64;
                int blue = (toGenerateColor * 2) % 128 + 64;
                int green = (toGenerateColor * 3) % 128 + 64;
                ColorFilter filter = new LightingColorFilter(Color.BLACK, Color.rgb(red, blue, green));
                myIcon.setColorFilter(filter);
                holder.postNoProfileTextView.setBackground(myIcon);
            } else {
                holder.postNoProfileTextView.setVisibility(View.INVISIBLE);
                holder.postProfilePicture.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (this.postList != null)
            return this.postList.size();
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        TextView post_last_edit_time;
        TextView postNoProfileTextView;
        ImageView postProfilePicture;
        RelativeLayout others;
        TextView personal_text;
        TextView personal_post_last_edit_time;
        TextView personal_postNoProfileTextView;
        ImageView personal_postProfilePicture;
        RelativeLayout personal;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.post_text);
            postProfilePicture =(ImageView)itemView.findViewById(R.id.chat_user_profile_picture);
            postNoProfileTextView =(TextView)itemView.findViewById(R.id.noUserProfileTextView);
            post_last_edit_time =(TextView)itemView.findViewById(R.id.post_last_edit_time);
            others=(RelativeLayout)itemView.findViewById(R.id.chatRelativeLayout);

            personal_text=(TextView)itemView.findViewById(R.id.personal_post_text);
            personal_postProfilePicture =(ImageView)itemView.findViewById(R.id.personal_chat_user_profile_picture);
            personal_postNoProfileTextView =(TextView)itemView.findViewById(R.id.personal_noUserProfileTextView);
            personal_post_last_edit_time =(TextView)itemView.findViewById(R.id.personal_post_last_edit_time);
            personal=(RelativeLayout)itemView.findViewById(R.id.personal_chatRelativeLayout);
        }
    }
    public interface ItemListener {
        public void onPostClick(PostModel PostModel);
        public void onPostHold(PostModel PostModel);
    }
}
