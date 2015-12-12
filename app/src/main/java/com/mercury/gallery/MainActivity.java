package com.mercury.gallery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends Activity {

    GalleryView gView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gView = new GalleryView(this);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        gView.setRotation(-90);
        int numColumns = 4;
        if (displaymetrics.widthPixels > displaymetrics.heightPixels)
            numColumns = 2;
        gView.setColumnWidth(displaymetrics.heightPixels / numColumns);
        gView.setNumColumns(numColumns);
        gView.setAdapter(new GalleryAdapter(this, displaymetrics.widthPixels, displaymetrics.heightPixels));
        gView.requestLayout();
        gView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.fromFile(new File(((GalleryAdapter.ViewHolder) view.getTag()).image.getPath()))
                        , "image/*");
                startActivity(intent);
            }
        });
        setContentView(gView);
    }


}
