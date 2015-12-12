package com.mercury.gallery;

import android.content.Context;
import android.widget.GridView;

public class GalleryView extends GridView {

    public GalleryView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(heightMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setX(-(r - b) / 2);
        setY((r - b) / 2);
    }
}
