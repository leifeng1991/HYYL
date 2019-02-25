package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

public class HomeBean extends BaseBean {

    /**
     * data : {"height":"0","weight":"0","sbp":"0","dbp":"0","pulse":"0","glu":"0","chol":"0","weight_low":"0","weight_high":"0","weight_ratio":"0.00","weight_state":"2","sbp_low":"45","sbp_high":"195","sbp_ratio":"0","sbp_state":"2","dbp_low":"37.5","dbp_high":"112.5","dbp_ratio":"0","dbp_state":"2","chol_low":"1.55","chol_high":"8.05","chol_ratio":"0","chol_state":"2","glu_low":"2.25","glu_high":"7.75","glu_ratio":"0","glu_state":"2","pulse_low":"5","pulse_high":"155","pulse_ratio":"0","pulse_state":"2"}
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
         * height : 0
         * weight : 0
         * sbp : 0
         * dbp : 0
         * pulse : 0
         * glu : 0
         * chol : 0
         * weight_low : 0
         * weight_high : 0
         * weight_ratio : 0.00
         * weight_state : 2
         * sbp_low : 45
         * sbp_high : 195
         * sbp_ratio : 0
         * sbp_state : 2
         * dbp_low : 37.5
         * dbp_high : 112.5
         * dbp_ratio : 0
         * dbp_state : 2
         * chol_low : 1.55
         * chol_high : 8.05
         * chol_ratio : 0
         * chol_state : 2
         * glu_low : 2.25
         * glu_high : 7.75
         * glu_ratio : 0
         * glu_state : 2
         * pulse_low : 5
         * pulse_high : 155
         * pulse_ratio : 0
         * pulse_state : 2
         */

        private String height;
        private String weight;
        private String sbp;
        private String dbp;
        private String pulse;
        private String glu;
        private String chol;
        private String weight_low;
        private String weight_high;
        private String weight_ratio;
        private String weight_state;
        private String sbp_low;
        private String sbp_high;
        private String sbp_ratio;
        private String sbp_state;
        private String dbp_low;
        private String dbp_high;
        private String dbp_ratio;
        private String dbp_state;
        private String chol_low;
        private String chol_high;
        private String chol_ratio;
        private String chol_state;
        private String glu_low;
        private String glu_high;
        private String glu_ratio;
        private String glu_state;
        private String pulse_low;
        private String pulse_high;
        private String pulse_ratio;
        private String pulse_state;

        public DataBean() {
        }

        public DataBean(String weight, String sbp, String dbp, String pulse, String glu, String chol, String weight_ratio, String weight_state, String sbp_ratio, String sbp_state, String dbp_ratio, String dbp_state, String chol_ratio, String chol_state, String glu_ratio, String glu_state, String pulse_ratio, String pulse_state) {
            this.weight = weight;
            this.sbp = sbp;
            this.dbp = dbp;
            this.pulse = pulse;
            this.glu = glu;
            this.chol = chol;
            this.weight_ratio = weight_ratio;
            this.weight_state = weight_state;
            this.sbp_ratio = sbp_ratio;
            this.sbp_state = sbp_state;
            this.dbp_ratio = dbp_ratio;
            this.dbp_state = dbp_state;
            this.chol_ratio = chol_ratio;
            this.chol_state = chol_state;
            this.glu_ratio = glu_ratio;
            this.glu_state = glu_state;
            this.pulse_ratio = pulse_ratio;
            this.pulse_state = pulse_state;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getSbp() {
            return sbp;
        }

        public void setSbp(String sbp) {
            this.sbp = sbp;
        }

        public String getDbp() {
            return dbp;
        }

        public void setDbp(String dbp) {
            this.dbp = dbp;
        }

        public String getPulse() {
            return pulse;
        }

        public void setPulse(String pulse) {
            this.pulse = pulse;
        }

        public String getGlu() {
            return glu;
        }

        public void setGlu(String glu) {
            this.glu = glu;
        }

        public String getChol() {
            return chol;
        }

        public void setChol(String chol) {
            this.chol = chol;
        }

        public String getWeight_low() {
            return weight_low;
        }

        public void setWeight_low(String weight_low) {
            this.weight_low = weight_low;
        }

        public String getWeight_high() {
            return weight_high;
        }

        public void setWeight_high(String weight_high) {
            this.weight_high = weight_high;
        }

        public String getWeight_ratio() {
            return weight_ratio;
        }

        public void setWeight_ratio(String weight_ratio) {
            this.weight_ratio = weight_ratio;
        }

        public String getWeight_state() {
            return weight_state;
        }

        public void setWeight_state(String weight_state) {
            this.weight_state = weight_state;
        }

        public String getSbp_low() {
            return sbp_low;
        }

        public void setSbp_low(String sbp_low) {
            this.sbp_low = sbp_low;
        }

        public String getSbp_high() {
            return sbp_high;
        }

        public void setSbp_high(String sbp_high) {
            this.sbp_high = sbp_high;
        }

        public String getSbp_ratio() {
            return sbp_ratio;
        }

        public void setSbp_ratio(String sbp_ratio) {
            this.sbp_ratio = sbp_ratio;
        }

        public String getSbp_state() {
            return sbp_state;
        }

        public void setSbp_state(String sbp_state) {
            this.sbp_state = sbp_state;
        }

        public String getDbp_low() {
            return dbp_low;
        }

        public void setDbp_low(String dbp_low) {
            this.dbp_low = dbp_low;
        }

        public String getDbp_high() {
            return dbp_high;
        }

        public void setDbp_high(String dbp_high) {
            this.dbp_high = dbp_high;
        }

        public String getDbp_ratio() {
            return dbp_ratio;
        }

        public void setDbp_ratio(String dbp_ratio) {
            this.dbp_ratio = dbp_ratio;
        }

        public String getDbp_state() {
            return dbp_state;
        }

        public void setDbp_state(String dbp_state) {
            this.dbp_state = dbp_state;
        }

        public String getChol_low() {
            return chol_low;
        }

        public void setChol_low(String chol_low) {
            this.chol_low = chol_low;
        }

        public String getChol_high() {
            return chol_high;
        }

        public void setChol_high(String chol_high) {
            this.chol_high = chol_high;
        }

        public String getChol_ratio() {
            return chol_ratio;
        }

        public void setChol_ratio(String chol_ratio) {
            this.chol_ratio = chol_ratio;
        }

        public String getChol_state() {
            return chol_state;
        }

        public void setChol_state(String chol_state) {
            this.chol_state = chol_state;
        }

        public String getGlu_low() {
            return glu_low;
        }

        public void setGlu_low(String glu_low) {
            this.glu_low = glu_low;
        }

        public String getGlu_high() {
            return glu_high;
        }

        public void setGlu_high(String glu_high) {
            this.glu_high = glu_high;
        }

        public String getGlu_ratio() {
            return glu_ratio;
        }

        public void setGlu_ratio(String glu_ratio) {
            this.glu_ratio = glu_ratio;
        }

        public String getGlu_state() {
            return glu_state;
        }

        public void setGlu_state(String glu_state) {
            this.glu_state = glu_state;
        }

        public String getPulse_low() {
            return pulse_low;
        }

        public void setPulse_low(String pulse_low) {
            this.pulse_low = pulse_low;
        }

        public String getPulse_high() {
            return pulse_high;
        }

        public void setPulse_high(String pulse_high) {
            this.pulse_high = pulse_high;
        }

        public String getPulse_ratio() {
            return pulse_ratio;
        }

        public void setPulse_ratio(String pulse_ratio) {
            this.pulse_ratio = pulse_ratio;
        }

        public String getPulse_state() {
            return pulse_state;
        }

        public void setPulse_state(String pulse_state) {
            this.pulse_state = pulse_state;
        }
    }
}
