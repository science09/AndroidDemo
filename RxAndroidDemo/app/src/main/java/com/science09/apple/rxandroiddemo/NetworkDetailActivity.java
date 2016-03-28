package com.science09.apple.rxandroiddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.science09.apple.rxandroiddemo.networks.NetworkWrapper;
import com.science09.apple.rxandroiddemo.networks.RepoListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NetworkDetailActivity extends AppCompatActivity {
    private static final String USER_KEY = "network_detail_activity.user";

    @Bind(R.id.network_rv_list)
    RecyclerView mRvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        // 设置布局
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvList.setLayoutManager(manager);

        // 设置Adapter
        RepoListAdapter adapter = new RepoListAdapter();
        NetworkWrapper.getReposInfo(getIntent().getStringExtra(USER_KEY), adapter);
        mRvList.setAdapter(adapter);
    }

    public static Intent from(Context context, String username) {
        Intent intent = new Intent(context, NetworkDetailActivity.class);
        intent.putExtra(USER_KEY, username);
        return intent;
    }

}
