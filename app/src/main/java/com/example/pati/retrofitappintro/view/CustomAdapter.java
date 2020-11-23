package com.example.pati.retrofitappintro.view;

/**
 * Created by Pati on 12.12.2018.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pati.retrofitappintro.R;
import com.example.pati.retrofitappintro.model.TransactionDto;
import com.example.pati.retrofitappintro.util.TimeHelper;

import java.util.Calendar;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private List<TransactionDto> transactionList;

    public CustomAdapter(final Context context, final List<TransactionDto> transactionList) {
        this.transactionList = transactionList;
        this.context = context;
    }

    public void updateEvents(final List<TransactionDto> transactions) {
        transactionList = transactions;
        notifyDataSetChanged();
    }

    public void setData(List<TransactionDto> newData) {
        this.transactionList = newData;
        notifyDataSetChanged();
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View transView = layoutInflater.inflate(R.layout.row, parent, false);
        ViewHolder viewHolder = new ViewHolder(transView);
        return viewHolder;
    }

    public void clear() {
        final int size = transactionList.size();
        transactionList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TransactionDto transaction = transactionList.get(position);

        Calendar calendar = TimeHelper.getNow();
//        calendar.setTimeInMillis(transaction.getDateOfTransaction());
//        String dateOfTransaction = calendar.get(Calendar.DAY_OF_MONTH) + "-" + String.format("%2d", calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
        holder.textViewValue.setText(String.valueOf(transaction.getValue()));
        holder.textViewDate.setText(transaction.getDateOfTransaction().toString());
         holder.textViewCategory.setText(transaction.getCategoryName());
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewValue;
        public TextView textViewDate;
        public TextView textViewCategory;

        public ViewHolder(final View view) {
            super(view);

            textViewValue = (TextView) view.findViewById(R.id.valueField);
            textViewCategory = (TextView) view.findViewById(R.id.categoryField);
            textViewDate = (TextView) view.findViewById(R.id.dateField);
        }
    }
}
