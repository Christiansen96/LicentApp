package com.example.retea.licentapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retea.licentapp.R;
import com.example.retea.licentapp.models.Service;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceListViewHolder> {

    private static final String TAG = "ServiceListAdapter";

    private List<Service> mServiceList;
    private final OnItemClickListener listener;

    public ServiceListAdapter(List<Service> mServiceList, OnItemClickListener listener) {
        this.mServiceList = mServiceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.cardview_service_list_item, parent, false);
        return new ServiceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListViewHolder holder, int position) {

        Service service = mServiceList.get(position);

        if(service.getImageUri() == null){
            holder.serviceImage.setImageResource(R.drawable.ic_home_black_24dp);
        } else Picasso.get().load(service.getImageUri()).into(holder.serviceImage);
        holder.serviceName.setText(service.getName());
        holder.serviceShortDescription.setText(service.getShortDescription());
        holder.servicePrice.setText(String.valueOf(service.getPrice()));
        holder.serviceDuration.setText(service.getDuration());


    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public class ServiceListViewHolder extends RecyclerView.ViewHolder {

        ImageView serviceImage;
        TextView serviceName;
        TextView serviceShortDescription;
        TextView servicePrice;
        TextView serviceDuration;

        public ServiceListViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceImage = itemView.findViewById(R.id.ServiceItemImageId);
            serviceName = itemView.findViewById(R.id.ServiceItemNameId);
            serviceShortDescription = itemView.findViewById(R.id.ServiceItemShortDescriptionId);
            servicePrice = itemView.findViewById(R.id.ServiceItemPriceId);
            serviceDuration = itemView.findViewById(R.id.ServiceItemDurationId);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Service service);
    }
}
