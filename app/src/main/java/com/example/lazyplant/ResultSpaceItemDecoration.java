package com.example.lazyplant;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class ResultSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpaceHeight;

    public ResultSpaceItemDecoration(int mSpaceHeight) {
        this.mSpaceHeight = mSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mSpaceHeight;
        outRect.top = mSpaceHeight;
    }


}
