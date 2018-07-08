package com.progteamf.test.imagedownloader.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.progteamf.test.imagedownloader.R;
import com.progteamf.test.imagedownloader.model.Image;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<Image> imageList;

    public HistoryAdapter(Context context,List<Image> imageList) {
        this.imageList = imageList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView dateText;
        private TextView linkText;
        private LinearLayout parent;

        public ViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.date_text);
            linkText = view.findViewById(R.id.link_text);
            parent = view.findViewById(R.id.historyItemView);

        }

        @Override
        public void onClick(View view) { }

    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_history, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        final Image current = imageList.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        holder.dateText.setText(dateFormat.format(current.getTime().getTime()));
        holder.linkText.setText(current.getLink());
        switch (current.getStatus()){
            case DOWNLOADED:
                holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGreen));
                break;
            case ERROR:
                holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.lightRed));
                break;
            case UNKNOWN:
                holder.parent.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGray));
                break;
        }
    }



    @Override
    public int getItemCount() {
        return imageList.size();
    }


}
