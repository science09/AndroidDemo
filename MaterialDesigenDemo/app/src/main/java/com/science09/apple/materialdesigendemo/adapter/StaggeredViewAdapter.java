package com.science09.apple.materialdesigendemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.science09.apple.materialdesigendemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 15/12/20.
 */
public class StaggeredViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public Context mContext;
    public List<String> mDatas;
    public List<Integer> mHeights;
    public LayoutInflater mLayoutInflater;

    public StaggeredViewAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = new ArrayList<>();
        mHeights = new ArrayList<>();
        for(int i = 'A'; i < 'z'; i++){
            mDatas.add((char) i + "");
        }
        for(int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int)(Math.random()*300) + 200);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // 创建ViewHolder
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
        RecyclerViewHolder mViewHolder = new RecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        if(mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }

        ViewGroup.LayoutParams mLayoutParams = holder.mTextView.getLayoutParams();
        mLayoutParams.height = mHeights.get(position);
        holder.mTextView.setLayoutParams(mLayoutParams);
        holder.mTextView.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}