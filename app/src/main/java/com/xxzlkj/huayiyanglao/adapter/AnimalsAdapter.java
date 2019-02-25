package com.xxzlkj.huayiyanglao.adapter;

import android.support.v7.widget.RecyclerView;

import com.xxzlkj.huayiyanglao.model.HomeIndexBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class AnimalsAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  private ArrayList<HomeIndexBean.DataBean.IndexBean> items = new ArrayList<>();

  public AnimalsAdapter() {
    setHasStableIds(true);
  }

  public void add(HomeIndexBean.DataBean.IndexBean object) {
    items.add(object);
    notifyDataSetChanged();
  }

  public void add(int index, HomeIndexBean.DataBean.IndexBean object) {
    items.add(index, object);
    notifyDataSetChanged();
  }

  public void addAll(Collection<? extends HomeIndexBean.DataBean.IndexBean> collection) {
    if (collection != null) {
      items.addAll(collection);
      notifyDataSetChanged();
    }
  }

  public void addAll(HomeIndexBean.DataBean.IndexBean... items) {
    addAll(Arrays.asList(items));
  }

  public void clear() {
    items.clear();
    notifyDataSetChanged();
  }

  public void remove(String object) {
    items.remove(object);
    notifyDataSetChanged();
  }

  public HomeIndexBean.DataBean.IndexBean getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
