package com.gg.sellers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserproductAdapter extends RecyclerView.Adapter<UserproductAdapter.ViewHolder> {
    private Context context;
    private List<Post> mPosts;

    public UserproductAdapter(Context context, List<Post> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userproduct_item, parent, false);
        return new UserproductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = mPosts.get(position);

        holder.name.setText(post.getProductName());
        holder.quantity.setText(post.getProductQuantity() + "kg");
        holder.quality.setText(post.getProductQuality());
        holder.harvestTime.setText(post.getProductHarvestTime());
        holder.dispatchTime.setText(post.getProductDispatchTime());
        holder.productPrice.setText("₹" + post.getProductPrice() + "/kg");
        holder.sellerContact.setText(post.getSellerPhoneNumber());
        holder.sellerAddress.setText(post.getSellerAddress());
        holder.uploadDate.setText(post.getUploadDate());
        holder.sellerName.setText(post.getSellerName());

    }

    @Override
    public int getItemCount() {
        if (mPosts.size() == 0) {
            HomeFragment.emptyAnimation.setVisibility(View.VISIBLE);
            HomeFragment.emptyTV.setVisibility(View.VISIBLE);
        } else {
            HomeFragment.emptyAnimation.setVisibility(View.INVISIBLE);
            HomeFragment.emptyTV.setVisibility(View.INVISIBLE);
        }
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, quantity, quality, harvestTime, dispatchTime, productPrice, sellerContact, sellerAddress, uploadDate, sellerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.productName);
            quantity = itemView.findViewById(R.id.productQuantity);
            quality = itemView.findViewById(R.id.productQuality);
            harvestTime = itemView.findViewById(R.id.productHarvestTime);
            dispatchTime = itemView.findViewById(R.id.productDispatchTime);
            productPrice = itemView.findViewById(R.id.productPrice);
            sellerContact = itemView.findViewById(R.id.sellerContact);
            sellerAddress = itemView.findViewById(R.id.sellerAddress);
            uploadDate = itemView.findViewById(R.id.uploadDate);
            sellerName = itemView.findViewById(R.id.sellerName);

        }
    }
}
