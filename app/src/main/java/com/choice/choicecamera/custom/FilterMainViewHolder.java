package com.choice.choicecamera.custom;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class FilterMainViewHolder extends RecyclerView.ViewHolder {

    public FilterMainViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract void bindType(int currentlySelected, FilterAdapter.OnItemSelectedListener itemSelectedListener);

}
