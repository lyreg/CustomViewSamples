package com.uestc.lyreg.customview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uestc.lyreg.customview.R;
import com.uestc.lyreg.customview.models.GankFuli;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 *
 * @Author lyreg
 */
public class SamplesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<GankFuli> mDatas;

    public SamplesAdapter() {
        mDatas = new ArrayList<>();
    }

    public void setDatas(List<GankFuli> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<GankFuli> datas) {
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
//        notifyItemRemoved(getItemCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recyclerview, parent, false);
            ItemViewHolder vh = new ItemViewHolder(view);
            return vh;
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_recylerview, parent, false);
            FooterViewHolder fvh = new FooterViewHolder(view);
            return fvh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder) {
            GankFuli fuli = mDatas.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Picasso.with(holder.itemView.getContext()).load(fuli.getUrl()).into(itemViewHolder.mImage);
            itemViewHolder.mDate.setText(fuli.getPublishedAt());
            itemViewHolder.mName.setText(fuli.getWho());
        } else if (holder instanceof FooterViewHolder) {

        }
    }


    @Override
    public int getItemCount() {
//        return mDatas == null ? 0 : mDatas.size()+1;
        if(mDatas == null || mDatas.size() == 0) {
            return 0;
        }
        return mDatas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if(position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImage;
        public TextView mName;
        public TextView mDate;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.image);
            mName = (TextView) itemView.findViewById(R.id.name);
            mDate = (TextView) itemView.findViewById(R.id.date);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
