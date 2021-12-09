package com.choice.choicecamera.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.choice.choicecamera.R;
import com.otaliastudios.cameraview.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterMainViewHolder> {
    List<Filter>filters=new ArrayList<>();

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public interface OnItemSelectedListener{

        void itemVisible(int position, View view);
        void itemClicked(int position,View view);
        int currentSelect();
    }

    private OnItemSelectedListener itemSelectedListener;

    public int selectedPosition=RecyclerView.NO_POSITION;
    public void setItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    @NonNull
    @Override
    public FilterMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_design,parent,false);
        return new FilterViewHolder(view);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public void onBindViewHolder(@NonNull FilterMainViewHolder holder, int position) {


        holder.bindType(itemSelectedListener.currentSelect(),itemSelectedListener);


    }

    @Override
    public int getItemCount() {
        return filters.size();
    }


}
