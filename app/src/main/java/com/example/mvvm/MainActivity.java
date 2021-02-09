package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mvvm.adapter.DiscussionListAdapter;
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.viewmodel.DiscussionListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DiscussionListAdapter.ItemListener {

    private List<DiscussionModel> discussionModelList;
    private DiscussionListAdapter adapter;
    private DiscussionListViewModel viewModel;
    EditText titleEditText;
    CountDownTimer countDownTimer;
    Toolbar toolbar;
    String selectedPage="trendingDiscussions";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.mainToolbar);
        RecyclerView discussionRecyclerView = (RecyclerView) findViewById(R.id.discussionlistview);
        final TextView noDiscussionFound = (TextView) findViewById(R.id.noDiscussionFoundTextView);
        noDiscussionFound.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        discussionRecyclerView.setLayoutManager(layoutManager);
        adapter = new DiscussionListAdapter(this, discussionModelList, this);
        discussionRecyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(DiscussionListViewModel.class);
        viewModel.getDiscussionListObserver().observe(this, new Observer<List<DiscussionModel>>() {
            @Override
            public void onChanged(List<DiscussionModel> discussionModels) {
                if (discussionModels != null) {
                    discussionModelList = discussionModels;
                    adapter.setDiscussionList(discussionModels);
                    noDiscussionFound.setVisibility(View.INVISIBLE);
                } else {
                    noDiscussionFound.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.makeApiCall(selectedPage);
        countDownTimer = new CountDownTimer(5000,5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
    viewModel.makeApiCall(selectedPage);
    countDownTimer.start();
            }
        };
        countDownTimer.start();
    }
    private Context getContext() {
        return this;
    }

    @Override
    public void onDiscussionClick(DiscussionModel discussionModel) {
        Intent intent =new Intent(this,ChatActivity.class);
        intent.putExtra("EXTRA_DISCUSSION_ID", discussionModel.getDiscussion_id());
        startActivity(intent);
//                Toast.makeText(this,"Clicked Discussion : "+discussionModel.getDiscussion_id(),Toast.LENGTH_LONG).show();
//        viewModel.makeApiCall();
    }

    @Override
    public void onDiscussionHold(DiscussionModel discussionModel) {
        Toast.makeText(this,"Clicked Discussion : "+discussionModel.getDiscussion_id(),Toast.LENGTH_LONG).show();

    }


    public void changePage(View view) {
        ImageView temp=(ImageView)view;

        String tag=temp.getTag().toString();
        if (selectedPage.equals(tag))
            return;
        Log.d("clicked tag is :",tag);
        Drawable background = getResources().getDrawable( R.drawable.bg_edittext);
        findViewById(R.id.followedDiscussionsImageView).setBackground(background);
        findViewById(R.id.categorizedDiscussionsImageView).setBackground(background);
        findViewById(R.id.trendingDiscussionsImageView).setBackground(background);
        findViewById(R.id.personalDiscussionsImageView).setBackground(background);
        background = getResources().getDrawable( R.drawable.login_signup_button);
        temp.setBackground(background);
        selectedPage=tag;
        viewModel.makeApiCall(selectedPage);
//        Intent intent=new Intent(this,CreateDiscussionActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        viewModel.makeApiCall(selectedPage);
    }
}



