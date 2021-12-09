package com.choice.choicecamera.galleryfolder;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.choice.choicecamera.GalleryItem;
import com.choice.choicecamera.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.innerViewHolder>{

    List<GalleryItem> itemList=new ArrayList<>();

    public void setItemList(List<GalleryItem> itemList) {
        this.itemList = itemList;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public innerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.makepostlayout,parent,false);
        return new innerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull innerViewHolder holder, int position) {
        File file = new File(itemList.get(position).getFileURL());

        Uri uri = Uri.fromFile(file);

        Picasso.get().load(uri).fit().into(holder.imageView);
        holder.imageView.setOnClickListener(v->{
            onItemClickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class innerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public innerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.makepostImage);
        }
    }
}
