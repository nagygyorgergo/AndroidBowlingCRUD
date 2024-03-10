package com.example.firestorecrud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder>{

    Context context;
    ArrayList<Reservation> arrayList;
    ReservationAdapter.OnItemClickListener onItemClickListener;

    public ReservationAdapter(Context context, ArrayList<Reservation> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ReservationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        return new ReservationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.ViewHolder holder, int position) {
        holder.name.setText(MessageFormat.format("{0} {1}", arrayList.get(position).getFirstName(), arrayList.get(position).getLastName()));
        holder.phone.setText(arrayList.get(position).getPhone());
        holder.bio.setText(arrayList.get(position).getBio());
        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, bio, phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_item_name);
            bio = itemView.findViewById(R.id.list_item_bio);
            phone = itemView.findViewById(R.id.list_item_phone);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Reservation reservation);

    }
}
