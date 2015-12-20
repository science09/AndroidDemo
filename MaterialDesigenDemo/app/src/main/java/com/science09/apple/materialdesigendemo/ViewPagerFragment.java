package com.science09.apple.materialdesigendemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.science09.apple.materialdesigendemo.adapter.RecyclerViewAdapter;
import com.science09.apple.materialdesigendemo.adapter.StaggeredViewAdapter;
import com.science09.apple.materialdesigendemo.utils.SnackbarUtil;


public class ViewPagerFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        RecyclerViewAdapter.OnItemClickListener,
        StaggeredViewAdapter.OnItemClickListener {
    @Override
    public void onItemClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView, getString(R.string.item_clicked), 0);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView, getString(R.string.item_longclicked), 0);
    }

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private StaggeredViewAdapter mStaggeredViewAdapter;

    private static final int VERTICAL_LIST = 0;
    private static final int HORIZONTAL_LIST = 1;
    private static final int VERTICAL_GRID = 2;
    private static final int HORIZONTAL_GRID = 3;
    private static final int STAGGERED_GRID = 4;
    private static final int SPAN_COUNT = 2;
    private int flag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_main, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_swiperefreshlayout);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_recyclerview);
        flag = (int)getArguments().get("flag");
        configRecyclerView();
    }



    private void configRecyclerView() {
        switch (flag) {
            case VERTICAL_LIST:
                mLayoutManager = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_LIST:
                mLayoutManager = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false);
                break;
            case VERTICAL_GRID:
                mLayoutManager = new GridLayoutManager(getActivity(),
                        SPAN_COUNT, GridLayoutManager.VERTICAL, false);
                break;
            case HORIZONTAL_GRID:
                mLayoutManager = new GridLayoutManager(getActivity(),
                        SPAN_COUNT, GridLayoutManager.HORIZONTAL, false);
                break;
            case STAGGERED_GRID:
                mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT,
                        StaggeredGridLayoutManager.VERTICAL);
                break;
        }
        if(flag != STAGGERED_GRID) {
            mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity());
            mRecyclerViewAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        } else {
            mStaggeredViewAdapter = new StaggeredViewAdapter(getActivity());
            mStaggeredViewAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mStaggeredViewAdapter);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                int temp = (int)(Math.random()*10);
                if(flag != STAGGERED_GRID) {
                    mRecyclerViewAdapter.mDatas.add(0, "New" + temp);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }else {
                    mStaggeredViewAdapter.mDatas.add(0, "New" + temp);
                    mStaggeredViewAdapter.mHeights.add(0, (int)(Math.random()*300) + 200);
                    mStaggeredViewAdapter.notifyDataSetChanged();
                }
            }
        }, 1000);
    }
}
