package com.xxzlkj.licallife.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/7/31 18:46
 */
public class ScheduleBean implements Serializable {
    /**
     * start : 1501430400
     * week : 周一
     * day : 07-31
     * time : [{"time":"06:30","state":"2","timestamp":"1501453800"},{"time":"07:00","state":"2","timestamp":"1501455600"},{"time":"07:30","state":"2","timestamp":"1501457400"},{"time":"08:00","state":"1","timestamp":"1501459200"},{"time":"08:30","state":"1","timestamp":"1501461000"},{"time":"09:00","state":"1","timestamp":"1501462800"},{"time":"09:30","state":"1","timestamp":"1501464600"},{"time":"10:00","state":"1","timestamp":"1501466400"},{"time":"10:30","state":"1","timestamp":"1501468200"},{"time":"11:00","state":"1","timestamp":"1501470000"},{"time":"11:30","state":"1","timestamp":"1501471800"},{"time":"12:00","state":"1","timestamp":"1501473600"},{"time":"12:30","state":"1","timestamp":"1501475400"},{"time":"13:00","state":"1","timestamp":"1501477200"},{"time":"13:30","state":"1","timestamp":"1501479000"},{"time":"14:00","state":"1","timestamp":"1501480800"},{"time":"14:30","state":"1","timestamp":"1501482600"},{"time":"15:00","state":"1","timestamp":"1501484400"},{"time":"15:30","state":"1","timestamp":"1501486200"},{"time":"16:00","state":"1","timestamp":"1501488000"},{"time":"16:30","state":"1","timestamp":"1501489800"},{"time":"17:00","state":"1","timestamp":"1501491600"},{"time":"17:30","state":"1","timestamp":"1501493400"},{"time":"18:00","state":"1","timestamp":"1501495200"},{"time":"18:30","state":"1","timestamp":"1501497000"},{"time":"19:00","state":"1","timestamp":"1501498800"},{"time":"19:30","state":"1","timestamp":"1501500600"},{"time":"20:00","state":"1","timestamp":"1501502400"},{"time":"20:30","state":"1","timestamp":"1501504200"},{"time":"21:00","state":"1","timestamp":"1501506000"},{"time":"21:30","state":"1","timestamp":"1501507800"},{"time":"22:00","state":"1","timestamp":"1501509600"}]
     */

    private String start;
    private String week;// 周
    private String day;// 日期
    private String month;
    private String monthNum;
    private List<TimeBean> time = new ArrayList<>();

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(String monthNum) {
        this.monthNum = monthNum;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    public static class TimeBean implements Serializable {
        /**
         * time : 06:30
         * state : 2
         * timestamp : 1501453800
         */

        private String time;// 开始时间
        private String endtime;// 结束时间
        private String state;// 1 未预定 ；2 已预订
        private String timestamp;// 时间戳
        private String timePeriodState;// 自己需要的，// 1 未预定 ；2 已预订

        public String getTimePeriodState() {
            return timePeriodState;
        }

        public void setTimePeriodState(String timePeriodState) {
            this.timePeriodState = timePeriodState;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}

