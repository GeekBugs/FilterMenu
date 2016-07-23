package me.f1reking.filtermenu_sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.Collection;
import java.util.List;

/**
 * Created by HuangYH on 2016/2/29.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> mDatas;
    private LayoutInflater mInflater;
    private int mLayoutId;

    public CommonAdapter(Context context, List<T> mDatas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.get(mContext,convertView,parent,mLayoutId,position);
        convert(holder,getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder,T t);

    public void addAll(Collection<? extends T> collections){
        mDatas.addAll(collections);
    }

    public void add(T t){
        mDatas.add(t);
    }

    public void add(int position,T t){
        mDatas.add(position,t);
    }

    public void clear(){
        mDatas.clear();
    }
}
