package com.example.retea.licentapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.models.GeologicalPosition;
import com.example.retea.licentapp.models.Provider;

import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class ProviderListAdapter extends RecyclerView.Adapter<ProviderListAdapter.MyViewHolder> {

    private List<Provider> mList;
    private final OnItemClickListener listener;

    public ProviderListAdapter(List<Provider> mList, OnItemClickListener listener) {
        this.mList = mList;
        this.listener = listener;
    }

    @NonNull
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
        holder.providerItemIcon.setImageResource(provider.getIcon());
        holder.providerItemDistanceDifference.setText((distanceTo(provider.getProviderGeologicalPosition()))+" km");
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
        ImageView providerItemIcon;
        TextView providerItemName;
        TextView providerItemCategory;
        TextView providerItemDistanceDifference;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            providerItemLinearLaoyout = itemView.findViewById(R.id.ProviderItemLinearLayout);
            providerItemIcon = itemView.findViewById(R.id.ProviderItemIconId);
            providerItemName = itemView.findViewById(R.id.ProviderItemNameId);
            providerItemCategory = itemView.findViewById(R.id.ProviderItemCategoryId);
            providerItemDistanceDifference = itemView.findViewById(R.id.ProviderItemDistanceDifferenceId);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Provider provider);
    }
    private String distanceTo(GeologicalPosition geoPos){
        Location deviceLocation = new Location("");
        deviceLocation.setLatitude(LicentApplication.getDeviceGeologicalPosition().getLatitude());
        deviceLocation.setLongitude(LicentApplication.getDeviceGeologicalPosition().getLongitude());

        Location desiredLocation = new Location("");
        desiredLocation.setLatitude(geoPos.getLatitude());
        desiredLocation.setLongitude(geoPos.getLongitude());

        Log.d(TAG, "distanceTo: " + deviceLocation.distanceTo(desiredLocation)/1000);
        float distance = deviceLocation.distanceTo(desiredLocation)/1000;
        return String.format(Locale.getDefault(),"%.2f",distance);



    }
}
