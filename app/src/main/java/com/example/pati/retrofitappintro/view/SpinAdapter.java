package com.example.pati.retrofitappintro.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pati.retrofitappintro.model.Category;

import java.util.List;

public class SpinAdapter extends ArrayAdapter<Category> {

    private Context context;
    private List<Category> values;

    public SpinAdapter(Context context, int textViewResourceId, List<Category> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        Category item = getItem(position);
        label.setText(item.getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        Category item = getItem(position);
        label.setText(item.getName());

        return label;
    }
}
