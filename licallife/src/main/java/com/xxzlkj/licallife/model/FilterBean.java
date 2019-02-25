package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class FilterBean extends BaseBean {


    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * experience : [{"title":"不限"},{"big":"1","small":"0","title":"一年以下"},{"big":"5","small":"1","title":"1-5年"},{"big":"30","small":"5","title":"5年以上"}]
         * title : 经验
         * age : [{"title":"不限"},{"big":"20","small":"0","title":"20以下"},{"big":"29","small":"20","title":"20-29"},{"big":"39","small":"30","title":"30-39"},{"big":"49","small":"40","title":"40-49"},{"big":"100","small":"50","title":"50以上"}]
         * duties : [{"id":"100","title":"做家务"},{"id":"101","title":"辅助孩子"}]
         * ability : [{"id":"100","title":"保姆证"},{"id":"101","title":"月嫂证"}]
         * education : [{"id":"1","title":"小学"},{"id":"2","title":"初中"},{"id":"3","title":"高中"},{"id":"4","title":"中专"},{"id":"5","title":"大专"},{"id":"6","title":"本科"}]
         * onduty : [{"title":"不限"},{"big":"1","small":"0","title":"一年以下"},{"big":"3","small":"1","title":"1-3年"},{"big":"10","small":"5","title":"5-10年"},{"big":"50","small":"10","title":"10年以上"}]
         * training : [{"title":"不限"},{"is":"1","title":"有"}]
         * paperwork : [{"title":"不限"},{"is":"1","title":"有"}]
         * insurance : [{"title":"不限"},{"is":"1","title":"有"}]
         */

        private String title;
        private String type;
        private int selectPosition;
        private List<DataItemBean> experience = new ArrayList<>();
        private List<DataItemBean> age = new ArrayList<>();
        private List<DataItemBean> duties = new ArrayList<>();
        private List<DataItemBean> ability = new ArrayList<>();
        private List<DataItemBean> education = new ArrayList<>();
        private List<DataItemBean> onduty = new ArrayList<>();
        private List<DataItemBean> training = new ArrayList<>();
        private List<DataItemBean> paperwork = new ArrayList<>();
        private List<DataItemBean> insurance = new ArrayList<>();
        private List<DataItemBean> salary = new ArrayList<>();
        private List<DataItemBean> hugh = new ArrayList<>();

        public List<DataItemBean> getSalary() {
            return salary;
        }

        public void setSalary(List<DataItemBean> salary) {
            this.salary = salary;
        }

        public List<DataItemBean> getHugh() {
            return hugh;
        }

        public void setHugh(List<DataItemBean> hugh) {
            this.hugh = hugh;
        }

        public int getSelectPosition() {
            return selectPosition;
        }

        public void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<DataItemBean> getExperience() {
            return experience;
        }

        public void setExperience(List<DataItemBean> experience) {
            this.experience = experience;
        }

        public List<DataItemBean> getAge() {
            return age;
        }

        public void setAge(List<DataItemBean> age) {
            this.age = age;
        }

        public List<DataItemBean> getDuties() {
            return duties;
        }

        public void setDuties(List<DataItemBean> duties) {
            this.duties = duties;
        }

        public List<DataItemBean> getAbility() {
            return ability;
        }

        public void setAbility(List<DataItemBean> ability) {
            this.ability = ability;
        }

        public List<DataItemBean> getEducation() {
            return education;
        }

        public void setEducation(List<DataItemBean> education) {
            this.education = education;
        }

        public List<DataItemBean> getOnduty() {
            return onduty;
        }

        public void setOnduty(List<DataItemBean> onduty) {
            this.onduty = onduty;
        }

        public List<DataItemBean> getTraining() {
            return training;
        }

        public void setTraining(List<DataItemBean> training) {
            this.training = training;
        }

        public List<DataItemBean> getPaperwork() {
            return paperwork;
        }

        public void setPaperwork(List<DataItemBean> paperwork) {
            this.paperwork = paperwork;
        }

        public List<DataItemBean> getInsurance() {
            return insurance;
        }

        public void setInsurance(List<DataItemBean> insurance) {
            this.insurance = insurance;
        }

        public static class DataItemBean {
            /**
             * title : 不限
             * big : 1
             * small : 0
             */
            private String is;
            private String id;
            private String title;
            private String big;
            private String small;

            public String getIs() {
                return is;
            }

            public void setIs(String is) {
                this.is = is;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getBig() {
                return big;
            }

            public void setBig(String big) {
                this.big = big;
            }

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }
        }

        public static class DutiesBean {
            /**
             * id : 100
             * title : 做家务
             */

            private String id;
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class AbilityBean {
            /**
             * id : 100
             * title : 保姆证
             */

            private String id;
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
