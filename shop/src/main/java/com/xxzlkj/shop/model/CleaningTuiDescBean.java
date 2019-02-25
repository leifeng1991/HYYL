package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 *
 * @author zhangrq
 * 
 */
public class CleaningTuiDescBean extends BaseBean {


	/**
	 * data : {"desc":"1.服务前2小时（及以上）申请退款，可全额退还/n 2.服务前2小时申请退款，须支付20%违约金"}
	 */

	private DataBean data=new DataBean();

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * desc : 1.服务前2小时（及以上）申请退款，可全额退还/n 2.服务前2小时申请退款，须支付20%违约金
		 */

		private String desc;

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
}
