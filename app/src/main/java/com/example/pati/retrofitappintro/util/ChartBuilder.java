package com.example.pati.retrofitappintro.util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartBuilder {


    public static void buildBArChart (BarChart barChart, List<BarEntry> expensesList, List<BarEntry> incomeList){

        BarDataSet set = new BarDataSet(expensesList, "Incomes");
        BarDataSet negSet = new BarDataSet(incomeList, "Expenses");
        negSet.setColor(Color.RED);

        set.setColor(Color.GREEN);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        dataSets.add(negSet);
        BarData data = new BarData(dataSets);
        barChart.setData(data);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        XAxis xAxis = barChart.getXAxis();
        String[] days = new String[]{"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(false);
        barChart.setBackgroundColor(ColorTemplate.getHoloBlue());
        barChart.setAlpha(0.8f);
    }

}
