package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvm.adapter.DiscussionListAdapter;
import com.example.mvvm.model.DiscussionModel;
import com.example.mvvm.viewmodel.DiscussionListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DiscussionListAdapter.ItemClickListener {

    private List<DiscussionModel> discussionModelList;
    private DiscussionListAdapter adapter;
    private DiscussionListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView discussionRecyclerView =(RecyclerView)findViewById(R.id.discussionlistview);
        final TextView noDiscussionFound =(TextView) findViewById(R.id.noDiscussionFoundTextView);
        noDiscussionFound.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager=new GridLayoutManager(this,2);
        discussionRecyclerView.setLayoutManager(layoutManager);
        adapter =new DiscussionListAdapter(this,discussionModelList,this);
        discussionRecyclerView.setAdapter(adapter);
        viewModel= ViewModelProviders.of(this).get(DiscussionListViewModel.class);
        viewModel.getDiscussionListObserver().observe(this, new Observer<List<DiscussionModel>>() {
            @Override
            public void onChanged(List<DiscussionModel> discussionModels) {
                if (discussionModels != null){
                    discussionModelList =discussionModels;
                    adapter.setDiscussionList(discussionModels);
                }
                else {
                    noDiscussionFound.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.makeApiCall();
    }

    @Override
    public void onDiscussionClick(DiscussionModel discussionModel) {
        Toast.makeText(this,"Clicked Discussion : "+discussionModel.getTitle(),Toast.LENGTH_LONG).show();
        viewModel.makeApiCall();
    }
}