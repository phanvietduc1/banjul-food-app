package banjulfood.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import banjulfood.com.R;
import banjulfood.com.model.CartModel;
import banjulfood.com.model.OrderModel;

public class AdapterOrders extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel> items = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;
    Context context;
    String layout_style = "0";


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, OrderModel obj, int pos);
    }

    public AdapterOrders(List<OrderModel> items, Context context, String layout_style) {
        this.items = items;
        this.context = context;
        this.layout_style = layout_style;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView customer_name;
        public TextView customer_address;
        public TextView total_price;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            customer_name = (TextView) v.findViewById(R.id.customer_name);
            customer_address = (TextView) v.findViewById(R.id.customer_address);
            total_price = (TextView) v.findViewById(R.id.total_price);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        if (layout_style.equals("0")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        } else if (layout_style.equals("1")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        }

        vh = new OriginalViewHolder(v);
        return vh;
    }


    int tot = 0;

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final OrderModel o = items.get(position);
            view.customer_name.setText(o.customer.first_name + " " + o.customer.last_name);
            view.customer_address.setText(o.customer.address);


            tot = 0;
            for (CartModel c : o.cart) {
                try {
                    tot += Integer.valueOf(c.product_price);
                } catch (Exception E) {

                }
            }

            view.total_price.setText("$ " + tot);


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
