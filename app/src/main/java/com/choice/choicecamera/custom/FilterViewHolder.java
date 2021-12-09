package com.choice.choicecamera.custom;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.choice.choicecamera.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class FilterViewHolder extends FilterMainViewHolder {

    ImageView selectedIndicator,filterImage;
    public FilterViewHolder(@NonNull View itemView) {
        super(itemView);
        selectedIndicator=itemView.findViewById(R.id.selectedIndicator);
        filterImage=itemView.findViewById(R.id.filterImage);
    }

    @Override
    public void bindType(int currentlySelected, FilterAdapter.OnItemSelectedListener itemSelectedListener) {

        if(currentlySelected==getAdapterPosition()){
            selectedIndicator.setVisibility(View.VISIBLE);
        }else {
            selectedIndicator.setVisibility(View.GONE);
        }
        itemView.setOnClickListener(v->{
            itemSelectedListener.itemClicked(getAdapterPosition(),selectedIndicator);
            YoYo.with(Techniques.Pulse).duration(200).playOn(selectedIndicator);
        });

    }
}
