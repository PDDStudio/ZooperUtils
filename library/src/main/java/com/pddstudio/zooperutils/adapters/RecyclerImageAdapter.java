package com.pddstudio.zooperutils.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pddstudio.zooperutils.R;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class was created by Patrick J
 * on 25.12.15. For more Details and Licensing
 * have a look at the README.md
 */
public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolder> {

    private List<Bitmap> itemData;

    public RecyclerImageAdapter(@NonNull List<Bitmap> dataSet) {
        this.itemData = dataSet;
    }

    public void addBitmap(Bitmap item) {
        this.itemData.add(item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_widget_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap widgetPreview = itemData.get(position);
        holder.widgetImage.setImageBitmap(widgetPreview);
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView widgetImage;
        TextView widgetName;

        public ViewHolder(View itemView) {
            super(itemView);
            widgetImage = (ImageView) itemView.findViewById(R.id.widget_image);
            widgetName = (TextView) itemView.findViewById(R.id.widget_name);
        }

    }

}