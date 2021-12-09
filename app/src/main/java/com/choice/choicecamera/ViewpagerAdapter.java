package com.choice.choicecamera;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapter extends PagerAdapter {

    List<String> text=new ArrayList<>();
    @Override
    public int getCount() {
        return text.size();
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.93f;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.text_page,container,false);
        TextView pageText=view.findViewById(R.id.pageText);
        pageText.setText(text.get(position));
        container.addView(pageText);
       // destroyItem(container,position,pageText);
        return pageText;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {


        return view==object;
    }
}
