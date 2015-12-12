package com.mercury.gallery;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class GalleryAdapter extends BaseAdapter {

    private Uri[] imagesUri;
    private long[] imagesIds;
    private int screenWidth, screenHeight;
    private float imagesNumHorizontal;
    private int imagesNumVertical;
    private WeakReference<Context> mContext;

    public static class ViewHolder {
        public ImageView thumb;
        public int position;
        public Uri image;
    }

    public GalleryAdapter(Context c, int screenWidth, int screenHeight) {
        mContext=new WeakReference<>(c);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        if (screenWidth > screenHeight) {
            imagesNumHorizontal = 3.5f;
            imagesNumVertical = 2;
        } else {
            imagesNumHorizontal = 2.5f;
            imagesNumVertical = 4;
        }
        String[] projection= {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        Cursor imagesCursor=c.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection , null, null, null);
        if(imagesCursor!=null){
            imagesUri=new Uri[imagesCursor.getCount()];
            imagesIds=new long[imagesCursor.getCount()];
            imagesCursor.moveToFirst();
            for(int i=0; i<imagesCursor.getCount(); i++) {
                imagesCursor.moveToPosition(i);
                imagesIds[i] = imagesCursor.getLong(0);
                imagesUri[i] = Uri.parse(imagesCursor.getString(1));
            }
            imagesCursor.close();
        }
    }

    @Override
    public int getCount() {
        return imagesIds.length;
    }

    @Override
    public Object getItem(int position) {
        return imagesUri[position].getPath();
    }

    @Override
    public long getItemId(int position) {
        return imagesIds[position];
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            ImageView imageView = new ImageView(parent.getContext());
            int thumbWidthAndHeight =
                    Math.min(
                            Math.round(screenWidth / imagesNumHorizontal),
                            Math.round(screenHeight / imagesNumVertical)
                    );
            imageView.setLayoutParams(new GridView.LayoutParams(thumbWidthAndHeight, thumbWidthAndHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewHolder dataHolder = new ViewHolder();
            dataHolder.thumb = imageView;
            imageView.setTag(dataHolder);
            convertView = imageView;
            convertView.setRotation(90);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.thumb.setImageBitmap(null);
        holder.position = position;

        new AsyncTask<ViewHolder, Void, Bitmap>() {
            private ViewHolder hld;

            @Override
            protected Bitmap doInBackground(ViewHolder... params) {
                hld = params[0];
                hld.image=imagesUri[hld.position];
                return getBitmap(imagesIds[hld.position]);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (position == hld.position)
                    hld.thumb.setImageBitmap(result);
            }
        }.execute(holder);
        return convertView;
    }

    private Bitmap getBitmap(long id) {
        return MediaStore.Images.Thumbnails.getThumbnail(
                mContext.get().getContentResolver(),
                id,
                MediaStore.Images.Thumbnails.MINI_KIND, null
        );
    }

}
