package com.xxzlkj.huayiyanglao.adapter;

import android.support.v7.widget.RecyclerView;

import com.xxzlkj.huayiyanglao.model.FoodsSaleListBean;
import com.xxzlkj.huayiyanglao.model.HomeIndexBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class TakeOutAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  private ArrayList<FoodsSaleListBean.DataBean.ListBeanX> items = new ArrayList<>();

  public TakeOutAdapter() {
    setHasStableIds(true);
  }

  public void add(FoodsSaleListBean.DataBean.ListBeanX object) {
    items.add(object);
    notifyDataSetChanged();
  }

  public void add(int index, FoodsSaleListBean.DataBean.ListBeanX object) {
    items.add(index, object);
    notifyDataSetChanged();
  }

  public void addAll(Collection<? extends FoodsSaleListBean.DataBean.ListBeanX> collection) {
    if (collection != null) {
      items.addAll(collection);
      notifyDataSetChanged();
    }
  }

  public void addAll(FoodsSaleListBean.DataBean.ListBeanX... items) {
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

  public FoodsSaleListBean.DataBean.ListBeanX getItem(int position) {
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
