package cellPhoneX.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.ArrayList;
import java.util.List;

import cellPhoneX.com.R;
import cellPhoneX.com.model.CartModel;
import cellPhoneX.com.model.ProductModel;

public class AdapterProduct extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel> items = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;
    private OnRemoveItemClickListener mOnRemoveItemClickListener;
    Context context;
    String layout_style = "0";


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnRemoveItemClickListener(final OnRemoveItemClickListener mRemoveItemClickListener) {
        this.mOnRemoveItemClickListener = mRemoveItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ProductModel obj, int pos);
    }

    public interface OnRemoveItemClickListener {
        void onRemoveItemClick();
    }

    public AdapterProduct(List<ProductModel> items, Context context, String layout_style) {
        this.items = items;
        this.context = context;
        this.layout_style = layout_style;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageView remove;
        public TextView title;
        public TextView price;
        public TextView des;
        public TextView soluong;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            soluong = (TextView) v.findViewById(R.id.soluong);
            image = (ImageView) v.findViewById(R.id.image);
            remove = (ImageView) v.findViewById(R.id.remove);
            title = (TextView) v.findViewById(R.id.title);
            price = (TextView) v.findViewById(R.id.price);
            des = (TextView) v.findViewById(R.id.des);
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
            view.price.setText(p.price + " đ");
            view.des.setText(p.details);
            view.soluong.setText(String.valueOf(p.quantity));
            view.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove_product_to_cart(p.product_id);
                    mOnRemoveItemClickListener.onRemoveItemClick();
                }
            });


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

    private void remove_product_to_cart(String product_id) {
        List<CartModel> list_cart_item = CartModel.find(CartModel.class, "productId = ?", product_id);

        if (list_cart_item.get(0).quantity == 0) {
            return;
        }
        list_cart_item.get(0).quantity = list_cart_item.get(0).quantity-1;

        try {
            //cart_item.save();
            CartModel.save(list_cart_item.get(0));
            Toast.makeText(context, "Product remove to cart", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed yo save because " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}
