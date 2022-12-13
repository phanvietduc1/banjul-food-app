package cellPhoneX.com.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import cellPhoneX.com.R;
import cellPhoneX.com.model.Photo;

public class PhotoAdapter extends PagerAdapter {
    Context mContext;
    List<Photo> mPhotos;

    public PhotoAdapter(Context mContext, List<Photo> mPhotos) {
        this.mContext = mContext;
        this.mPhotos = mPhotos;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo, container, false);
        ImageView imgPhoto = view.findViewById(R.id.image_photo);
        TextView textView = view.findViewById(R.id.text);

        Photo photo = mPhotos.get(position);
        if (photo!=null){
            Glide.with(mContext).load(photo.getResourceId()).into(imgPhoto);
            if (position==0){
                textView.setText("Khẳng định phong cách và đẳng cấp ");
            } if (position==1){
                textView.setText("Là nhà phân phối chính thức của nhiều thương hiệu lớn");
            }
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (mPhotos != null) {
            return mPhotos.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
