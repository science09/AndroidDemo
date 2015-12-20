package com.science09.apple.materialdesigendemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.science09.apple.materialdesigendemo.R;

/**
 * Created by apple on 15/12/20.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    public TextView mTextView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.id_textview);
    }
}
