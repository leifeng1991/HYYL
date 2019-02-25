package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.List;

/**
 *  基础的bean
 * 
 * @author zhangrq
 * 
 */
public class CancelOrderGroupBean extends BaseBean {

	/**
	 * data : {"group":["买错了","其他原因"]}
	 */

	private DataBean data=new DataBean();

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean {
		private List<String> group;

		public List<String> getGroup() {
			return group;
		}

		public void setGroup(List<String> group) {
			this.group = group;
		}
	}
}
