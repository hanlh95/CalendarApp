package com.example.lehoanghan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lehoanghan.appcalendar.R;

import java.util.ArrayList;

/**
 * Created by lehoanghan on 5/22/2016.
 */
public class ImageAdapter extends BaseAdapter {

    private int galleryItem;

    private Context adapterContext;

    private String listFriendInvite;

    private LayoutInflater layoutInflater = null;

    private String[] strFriend;

    private ArrayList<Bitmap> imageArray = new ArrayList<>();

    public ImageAdapter(Context C, String Friend, ArrayList<Bitmap> imageArray) {
        //layoutInflater=LayoutInflater.from(C);
        adapterContext = C;
        layoutInflater = (LayoutInflater) adapterContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        listFriendInvite = Friend;
        strFriend = listFriendInvite.split(",");
        for (Bitmap b : imageArray) {
            this.imageArray.add(b);
        }
        TypedArray attr = adapterContext.obtainStyledAttributes(R.styleable.GalleryActivity);
        galleryItem = attr.getResourceId(
                R.styleable.GalleryActivity_android_galleryItemBackground, 0);
        attr.recycle();
    }

    @Override
    public int getCount() {
        return strFriend.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView tvGallery;

        private ImageView ivGallery;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder HOLDER;
        View vi = convertView;
//            if (parent != null)
//                parent.removeView(convertView);

        if (vi == null) {
            vi = layoutInflater.inflate(R.layout.custom_gallery_friendinvite, parent, false);
            vi.setLayoutParams(new Gallery.LayoutParams(500, 700));
            HOLDER = new ViewHolder();
            HOLDER.ivGallery = (ImageView) vi.findViewById(R.id.imgGallery);
            HOLDER.tvGallery = (TextView) vi.findViewById(R.id.txtGallery);
            vi.setTag(HOLDER);
        } else {
            HOLDER = (ViewHolder) vi.getTag();
        }
        HOLDER.ivGallery.setImageBitmap(imageArray.get(position));
        HOLDER.ivGallery.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
        HOLDER.ivGallery.setBackgroundResource(galleryItem);
        HOLDER.tvGallery.setText(strFriend[position].trim().replace("&", "."));
        HOLDER.tvGallery.setTag(strFriend[position].trim().replace("&", "."));
        HOLDER.tvGallery.setGravity(Gravity.CENTER);
        return vi;
    }
}
