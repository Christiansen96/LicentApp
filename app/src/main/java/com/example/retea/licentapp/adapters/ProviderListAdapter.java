package com.example.retea.licentapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.models.Provider;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProviderListAdapter extends RecyclerView.Adapter<ProviderListAdapter.MyViewHolder> {

    private List<Provider> mList;
    private final OnItemClickListener listener;

    public ProviderListAdapter(List<Provider> mList, OnItemClickListener listener) {
        this.mList = mList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.cardview_provider_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Provider provider = mList.get(position);

        holder.providerItemName.setText(provider.getName());
        holder.providerItemCategory.setText(provider.getCategory());
        Picasso.get().load(provider.getImage()).into(holder.providerItemImage);
        holder.providerItemLinearLaoyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(provider);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout providerItemLinearLaoyout;
        ImageView providerItemImage;
        TextView providerItemName;
        TextView providerItemCategory;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            providerItemLinearLaoyout = itemView.findViewById(R.id.ProviderItemLinearLayout);
            providerItemImage = itemView.findViewById(R.id.ProviderItemImageId);
            providerItemName = itemView.findViewById(R.id.ProviderItemNameId);
            providerItemCategory = itemView.findViewById(R.id.ProviderItemCategoryId);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Provider provider);
    }
}
