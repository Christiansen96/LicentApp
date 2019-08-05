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
import com.example.retea.licentapp.models.Appointment;
import com.example.retea.licentapp.models.GeologicalPosition;
import com.example.retea.licentapp.models.Provider;

import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.MyViewHolder> {

    private List<Appointment> mList;
    private final OnItemClickListener listener;

    public AppointmentListAdapter(List<Appointment> mList, OnItemClickListener listener) {
        this.mList = mList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.cardview_appointment_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Appointment appointment = mList.get(position);

        int minute = appointment.getMinute();
        int hour = appointment.getHour();
        String lMinute = "";
        String lHour = "";
        if (minute < 10) {
            lMinute = "0" + minute;
        }
        if (hour < 10) {
            lHour = "0" + hour;
        }
        String time = lHour + ":" + lMinute + " PM";

        String topText = "Appointment at '" + appointment.getProviderName() + "' for:";
        holder.appointmentItemTopText.setText(topText);
        String bottomText = "service: '" + appointment.getServiceName() + "'";
        holder.appointmentItemBottomText.setText(bottomText);
        String date = appointment.getDay() + "/" + appointment.getMonth() + "/" + appointment.getYear();
        holder.appointmentItemDate.setText(date);
        holder.appointmentItemHour.setText(time);
        if(appointment.isConfirmed()){
            holder.appointmentItemIcon.setImageResource(R.drawable.ic_done_black_24dp);
        }else holder.appointmentItemIcon.setImageResource(R.drawable.ic_timelapse_black_24dp);
        holder.appointmentItemLinearLaoyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(appointment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout appointmentItemLinearLaoyout;
        ImageView appointmentItemIcon;
        TextView appointmentItemTopText;
        TextView appointmentItemBottomText;
        TextView appointmentItemDate;
        TextView appointmentItemHour;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentItemLinearLaoyout = itemView.findViewById(R.id.AppointmentItemLayoutId);
            appointmentItemIcon = itemView.findViewById(R.id.AppointmentIconId);
            appointmentItemTopText = itemView.findViewById(R.id.AppointmentItemTopId);
            appointmentItemBottomText = itemView.findViewById(R.id.AppointmentItemBottomId);
            appointmentItemHour = itemView.findViewById(R.id.AppointmentItemHourId);
            appointmentItemDate = itemView.findViewById(R.id.AppointmentItemDateId);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

}
