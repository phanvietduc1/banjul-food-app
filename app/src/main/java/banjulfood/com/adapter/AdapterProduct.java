package banjulfood.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.ArrayList;
import java.util.List;

import banjulfood.com.R;
import banjulfood.com.model.ProductModel;

public class AdapterProduct extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel> items = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;
    Context context;
    String layout_style = "0";


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ProductModel obj, int pos);
    }

    public AdapterProduct(List<ProductModel> items, Context context, String layout_style) {
        this.items = items;
        this.context = context;
        this.layout_style = layout_style;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView price;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            price = (TextView) v.findViewById(R.id.price);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        if (layout_style.equals("0")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        } else if (layout_style.equals("1")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_1, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        }


        vh = new OriginalViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final ProductModel p = items.get(position);
            view.title.setText(p.title + "");
            view.price.setText(p.price + "");


            Glide.with(context)
                    .load(p.photo)
                    .animate(R.anim.abc_fade_in)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .centerCrop()
                    .into(view.image)
            ;


            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}
