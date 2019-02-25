package com.xxzlkj.licallife.model;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class CleanerDetailBean extends BaseBean implements Serializable {


    /**
     * data : {"id":"3","uid":"111412","simg":"","name":"黄冈","sale":"0","label":"比克,比克123456789","star":"1","price":"0.00","ads":"","shopid":"1","satisfaction":"0","saturation":"0","service_starttime":"6:30","service_endtime":"22:30","grouptitle":[{"title":"日常保洁4","price":"0.01","unit_desc":""},{"title":"日常保洁3","price":"1.00","unit_desc":""},{"title":"日常保洁2","price":"1.00","unit_desc":""},{"title":"日常保洁1","price":"1.00","unit_desc":""},{"title":"日常保洁2小时","price":"100.00","unit_desc":"小时"}],"group":[{"id":"115","title":"家电清洗","addtime":"1500374096","uid":"111412","ord":"99","is_show":"1","pid":"0","type":"1","price":"0.00","peopleid":"3","groupid":"87","unit_desc":"","group":[{"id":"121","title":"家电清洗1","addtime":"1500953013","uid":"111412","ord":"99","is_show":"1","pid":"115","type":"1","price":"1.00","peopleid":"3","groupid":"98","unit_desc":""},{"id":"122","title":"家电清洗","addtime":"1500953013","uid":"111412","ord":"99","is_show":"1","pid":"115","type":"1","price":"1.00","peopleid":"3","groupid":"97","unit_desc":""}]},{"id":"116","title":"日常保洁","addtime":"1500374096","uid":"111412","ord":"99","is_show":"1","pid":"0","type":"1","price":"0.00","peopleid":"3","groupid":"86","unit_desc":"","group":[{"id":"117","title":"日常保洁4","addtime":"1500374105","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"0.01","peopleid":"3","groupid":"96","unit_desc":""},{"id":"118","title":"日常保洁3","addtime":"1500374105","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"1.00","peopleid":"3","groupid":"95","unit_desc":""},{"id":"119","title":"日常保洁2","addtime":"1500374105","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"1.00","peopleid":"3","groupid":"94","unit_desc":""},{"id":"120","title":"日常保洁1","addtime":"1500374105","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"1.00","peopleid":"3","groupid":"88","unit_desc":""},{"id":"125","title":"日常保洁2小时","addtime":"1501224186","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"100.00","peopleid":"3","groupid":"99","unit_desc":"小时"}]}],"img":[],"shop":[{"id":"1","title":"本地生活店铺1","phone":"1555555555","simg":"","desc":""}],"cleaning_shop_state":"1","schedule":[{"start":"1501430400","week":"周一","day":"07-31","time":[{"time":"06:30","state":"2","timestamp":"1501453800"},{"time":"07:00","state":"2","timestamp":"1501455600"},{"time":"07:30","state":"2","timestamp":"1501457400"},{"time":"08:00","state":"1","timestamp":"1501459200"},{"time":"08:30","state":"1","timestamp":"1501461000"},{"time":"09:00","state":"1","timestamp":"1501462800"},{"time":"09:30","state":"1","timestamp":"1501464600"},{"time":"10:00","state":"1","timestamp":"1501466400"},{"time":"10:30","state":"1","timestamp":"1501468200"},{"time":"11:00","state":"1","timestamp":"1501470000"},{"time":"11:30","state":"1","timestamp":"1501471800"},{"time":"12:00","state":"1","timestamp":"1501473600"},{"time":"12:30","state":"1","timestamp":"1501475400"},{"time":"13:00","state":"1","timestamp":"1501477200"},{"time":"13:30","state":"1","timestamp":"1501479000"},{"time":"14:00","state":"1","timestamp":"1501480800"},{"time":"14:30","state":"1","timestamp":"1501482600"},{"time":"15:00","state":"1","timestamp":"1501484400"},{"time":"15:30","state":"1","timestamp":"1501486200"},{"time":"16:00","state":"1","timestamp":"1501488000"},{"time":"16:30","state":"1","timestamp":"1501489800"},{"time":"17:00","state":"1","timestamp":"1501491600"},{"time":"17:30","state":"1","timestamp":"1501493400"},{"time":"18:00","state":"1","timestamp":"1501495200"},{"time":"18:30","state":"1","timestamp":"1501497000"},{"time":"19:00","state":"1","timestamp":"1501498800"},{"time":"19:30","state":"1","timestamp":"1501500600"},{"time":"20:00","state":"1","timestamp":"1501502400"},{"time":"20:30","state":"1","timestamp":"1501504200"},{"time":"21:00","state":"1","timestamp":"1501506000"},{"time":"21:30","state":"1","timestamp":"1501507800"},{"time":"22:00","state":"1","timestamp":"1501509600"}]},{"start":"1501516800","week":"周二","day":"08-01","time":[{"time":"06:30","state":"1","timestamp":"1501540200"},{"time":"07:00","state":"1","timestamp":"1501542000"},{"time":"07:30","state":"1","timestamp":"1501543800"},{"time":"08:00","state":"1","timestamp":"1501545600"},{"time":"08:30","state":"1","timestamp":"1501547400"},{"time":"09:00","state":"1","timestamp":"1501549200"},{"time":"09:30","state":"1","timestamp":"1501551000"},{"time":"10:00","state":"1","timestamp":"1501552800"},{"time":"10:30","state":"1","timestamp":"1501554600"},{"time":"11:00","state":"1","timestamp":"1501556400"},{"time":"11:30","state":"1","timestamp":"1501558200"},{"time":"12:00","state":"1","timestamp":"1501560000"},{"time":"12:30","state":"1","timestamp":"1501561800"},{"time":"13:00","state":"1","timestamp":"1501563600"},{"time":"13:30","state":"1","timestamp":"1501565400"},{"time":"14:00","state":"1","timestamp":"1501567200"},{"time":"14:30","state":"1","timestamp":"1501569000"},{"time":"15:00","state":"1","timestamp":"1501570800"},{"time":"15:30","state":"1","timestamp":"1501572600"},{"time":"16:00","state":"1","timestamp":"1501574400"},{"time":"16:30","state":"1","timestamp":"1501576200"},{"time":"17:00","state":"1","timestamp":"1501578000"},{"time":"17:30","state":"1","timestamp":"1501579800"},{"time":"18:00","state":"1","timestamp":"1501581600"},{"time":"18:30","state":"1","timestamp":"1501583400"},{"time":"19:00","state":"1","timestamp":"1501585200"},{"time":"19:30","state":"1","timestamp":"1501587000"},{"time":"20:00","state":"1","timestamp":"1501588800"},{"time":"20:30","state":"1","timestamp":"1501590600"},{"time":"21:00","state":"1","timestamp":"1501592400"},{"time":"21:30","state":"1","timestamp":"1501594200"},{"time":"22:00","state":"1","timestamp":"1501596000"}]},{"start":"1501603200","week":"周三","day":"08-02","time":[{"time":"06:30","state":"1","timestamp":"1501626600"},{"time":"07:00","state":"1","timestamp":"1501628400"},{"time":"07:30","state":"1","timestamp":"1501630200"},{"time":"08:00","state":"1","timestamp":"1501632000"},{"time":"08:30","state":"1","timestamp":"1501633800"},{"time":"09:00","state":"1","timestamp":"1501635600"},{"time":"09:30","state":"1","timestamp":"1501637400"},{"time":"10:00","state":"1","timestamp":"1501639200"},{"time":"10:30","state":"1","timestamp":"1501641000"},{"time":"11:00","state":"1","timestamp":"1501642800"},{"time":"11:30","state":"1","timestamp":"1501644600"},{"time":"12:00","state":"1","timestamp":"1501646400"},{"time":"12:30","state":"1","timestamp":"1501648200"},{"time":"13:00","state":"1","timestamp":"1501650000"},{"time":"13:30","state":"1","timestamp":"1501651800"},{"time":"14:00","state":"1","timestamp":"1501653600"},{"time":"14:30","state":"1","timestamp":"1501655400"},{"time":"15:00","state":"1","timestamp":"1501657200"},{"time":"15:30","state":"1","timestamp":"1501659000"},{"time":"16:00","state":"1","timestamp":"1501660800"},{"time":"16:30","state":"1","timestamp":"1501662600"},{"time":"17:00","state":"1","timestamp":"1501664400"},{"time":"17:30","state":"1","timestamp":"1501666200"},{"time":"18:00","state":"1","timestamp":"1501668000"},{"time":"18:30","state":"1","timestamp":"1501669800"},{"time":"19:00","state":"1","timestamp":"1501671600"},{"time":"19:30","state":"1","timestamp":"1501673400"},{"time":"20:00","state":"1","timestamp":"1501675200"},{"time":"20:30","state":"1","timestamp":"1501677000"},{"time":"21:00","state":"1","timestamp":"1501678800"},{"time":"21:30","state":"1","timestamp":"1501680600"},{"time":"22:00","state":"1","timestamp":"1501682400"}]},{"start":"1501689600","week":"周四","day":"08-03","time":[{"time":"06:30","state":"1","timestamp":"1501713000"},{"time":"07:00","state":"1","timestamp":"1501714800"},{"time":"07:30","state":"1","timestamp":"1501716600"},{"time":"08:00","state":"1","timestamp":"1501718400"},{"time":"08:30","state":"1","timestamp":"1501720200"},{"time":"09:00","state":"1","timestamp":"1501722000"},{"time":"09:30","state":"1","timestamp":"1501723800"},{"time":"10:00","state":"1","timestamp":"1501725600"},{"time":"10:30","state":"1","timestamp":"1501727400"},{"time":"11:00","state":"1","timestamp":"1501729200"},{"time":"11:30","state":"1","timestamp":"1501731000"},{"time":"12:00","state":"1","timestamp":"1501732800"},{"time":"12:30","state":"1","timestamp":"1501734600"},{"time":"13:00","state":"1","timestamp":"1501736400"},{"time":"13:30","state":"1","timestamp":"1501738200"},{"time":"14:00","state":"1","timestamp":"1501740000"},{"time":"14:30","state":"1","timestamp":"1501741800"},{"time":"15:00","state":"1","timestamp":"1501743600"},{"time":"15:30","state":"1","timestamp":"1501745400"},{"time":"16:00","state":"1","timestamp":"1501747200"},{"time":"16:30","state":"1","timestamp":"1501749000"},{"time":"17:00","state":"1","timestamp":"1501750800"},{"time":"17:30","state":"1","timestamp":"1501752600"},{"time":"18:00","state":"1","timestamp":"1501754400"},{"time":"18:30","state":"1","timestamp":"1501756200"},{"time":"19:00","state":"1","timestamp":"1501758000"},{"time":"19:30","state":"1","timestamp":"1501759800"},{"time":"20:00","state":"1","timestamp":"1501761600"},{"time":"20:30","state":"1","timestamp":"1501763400"},{"time":"21:00","state":"1","timestamp":"1501765200"},{"time":"21:30","state":"1","timestamp":"1501767000"},{"time":"22:00","state":"1","timestamp":"1501768800"}]},{"start":"1501776000","week":"周五","day":"08-04","time":[{"time":"06:30","state":"1","timestamp":"1501799400"},{"time":"07:00","state":"1","timestamp":"1501801200"},{"time":"07:30","state":"1","timestamp":"1501803000"},{"time":"08:00","state":"1","timestamp":"1501804800"},{"time":"08:30","state":"1","timestamp":"1501806600"},{"time":"09:00","state":"1","timestamp":"1501808400"},{"time":"09:30","state":"1","timestamp":"1501810200"},{"time":"10:00","state":"1","timestamp":"1501812000"},{"time":"10:30","state":"1","timestamp":"1501813800"},{"time":"11:00","state":"1","timestamp":"1501815600"},{"time":"11:30","state":"1","timestamp":"1501817400"},{"time":"12:00","state":"1","timestamp":"1501819200"},{"time":"12:30","state":"1","timestamp":"1501821000"},{"time":"13:00","state":"1","timestamp":"1501822800"},{"time":"13:30","state":"1","timestamp":"1501824600"},{"time":"14:00","state":"1","timestamp":"1501826400"},{"time":"14:30","state":"1","timestamp":"1501828200"},{"time":"15:00","state":"1","timestamp":"1501830000"},{"time":"15:30","state":"1","timestamp":"1501831800"},{"time":"16:00","state":"1","timestamp":"1501833600"},{"time":"16:30","state":"1","timestamp":"1501835400"},{"time":"17:00","state":"1","timestamp":"1501837200"},{"time":"17:30","state":"1","timestamp":"1501839000"},{"time":"18:00","state":"1","timestamp":"1501840800"},{"time":"18:30","state":"1","timestamp":"1501842600"},{"time":"19:00","state":"1","timestamp":"1501844400"},{"time":"19:30","state":"1","timestamp":"1501846200"},{"time":"20:00","state":"1","timestamp":"1501848000"},{"time":"20:30","state":"1","timestamp":"1501849800"},{"time":"21:00","state":"1","timestamp":"1501851600"},{"time":"21:30","state":"1","timestamp":"1501853400"},{"time":"22:00","state":"1","timestamp":"1501855200"}]},{"start":"1501862400","week":"周六","day":"08-05","time":[{"time":"06:30","state":"1","timestamp":"1501885800"},{"time":"07:00","state":"1","timestamp":"1501887600"},{"time":"07:30","state":"1","timestamp":"1501889400"},{"time":"08:00","state":"1","timestamp":"1501891200"},{"time":"08:30","state":"1","timestamp":"1501893000"},{"time":"09:00","state":"1","timestamp":"1501894800"},{"time":"09:30","state":"1","timestamp":"1501896600"},{"time":"10:00","state":"1","timestamp":"1501898400"},{"time":"10:30","state":"1","timestamp":"1501900200"},{"time":"11:00","state":"1","timestamp":"1501902000"},{"time":"11:30","state":"1","timestamp":"1501903800"},{"time":"12:00","state":"1","timestamp":"1501905600"},{"time":"12:30","state":"1","timestamp":"1501907400"},{"time":"13:00","state":"1","timestamp":"1501909200"},{"time":"13:30","state":"1","timestamp":"1501911000"},{"time":"14:00","state":"1","timestamp":"1501912800"},{"time":"14:30","state":"1","timestamp":"1501914600"},{"time":"15:00","state":"1","timestamp":"1501916400"},{"time":"15:30","state":"1","timestamp":"1501918200"},{"time":"16:00","state":"1","timestamp":"1501920000"},{"time":"16:30","state":"1","timestamp":"1501921800"},{"time":"17:00","state":"1","timestamp":"1501923600"},{"time":"17:30","state":"1","timestamp":"1501925400"},{"time":"18:00","state":"1","timestamp":"1501927200"},{"time":"18:30","state":"1","timestamp":"1501929000"},{"time":"19:00","state":"1","timestamp":"1501930800"},{"time":"19:30","state":"1","timestamp":"1501932600"},{"time":"20:00","state":"1","timestamp":"1501934400"},{"time":"20:30","state":"1","timestamp":"1501936200"},{"time":"21:00","state":"1","timestamp":"1501938000"},{"time":"21:30","state":"1","timestamp":"1501939800"},{"time":"22:00","state":"1","timestamp":"1501941600"}]},{"start":"1501948800","week":"周日","day":"08-06","time":[{"time":"06:30","state":"1","timestamp":"1501972200"},{"time":"07:00","state":"1","timestamp":"1501974000"},{"time":"07:30","state":"1","timestamp":"1501975800"},{"time":"08:00","state":"1","timestamp":"1501977600"},{"time":"08:30","state":"1","timestamp":"1501979400"},{"time":"09:00","state":"1","timestamp":"1501981200"},{"time":"09:30","state":"1","timestamp":"1501983000"},{"time":"10:00","state":"1","timestamp":"1501984800"},{"time":"10:30","state":"1","timestamp":"1501986600"},{"time":"11:00","state":"1","timestamp":"1501988400"},{"time":"11:30","state":"1","timestamp":"1501990200"},{"time":"12:00","state":"1","timestamp":"1501992000"},{"time":"12:30","state":"1","timestamp":"1501993800"},{"time":"13:00","state":"1","timestamp":"1501995600"},{"time":"13:30","state":"1","timestamp":"1501997400"},{"time":"14:00","state":"1","timestamp":"1501999200"},{"time":"14:30","state":"1","timestamp":"1502001000"},{"time":"15:00","state":"1","timestamp":"1502002800"},{"time":"15:30","state":"1","timestamp":"1502004600"},{"time":"16:00","state":"1","timestamp":"1502006400"},{"time":"16:30","state":"1","timestamp":"1502008200"},{"time":"17:00","state":"1","timestamp":"1502010000"},{"time":"17:30","state":"1","timestamp":"1502011800"},{"time":"18:00","state":"1","timestamp":"1502013600"},{"time":"18:30","state":"1","timestamp":"1502015400"},{"time":"19:00","state":"1","timestamp":"1502017200"},{"time":"19:30","state":"1","timestamp":"1502019000"},{"time":"20:00","state":"1","timestamp":"1502020800"},{"time":"20:30","state":"1","timestamp":"1502022600"},{"time":"21:00","state":"1","timestamp":"1502024400"},{"time":"21:30","state":"1","timestamp":"1502026200"},{"time":"22:00","state":"1","timestamp":"1502028000"}]}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 3
         * uid : 111412
         * simg :
         * name : 黄冈
         * sale : 0
         * label : 比克,比克123456789
         * star : 1
         * price : 0.00
         * ads :
         * shopid : 1
         * satisfaction : 0
         * saturation : 0
         * service_starttime : 6:30
         * service_endtime : 22:30
         * grouptitle : [{"title":"日常保洁4","price":"0.01","unit_desc":""},{"title":"日常保洁3","price":"1.00","unit_desc":""},{"title":"日常保洁2","price":"1.00","unit_desc":""},{"title":"日常保洁1","price":"1.00","unit_desc":""},{"title":"日常保洁2小时","price":"100.00","unit_desc":"小时"}]
         * group : [{"id":"115","title":"家电清洗","addtime":"1500374096","uid":"111412","ord":"99","is_show":"1","pid":"0","type":"1","price":"0.00","peopleid":"3","groupid":"87","unit_desc":"","group":[{"id":"121","title":"家电清洗1","addtime":"1500953013","uid":"111412","ord":"99","is_show":"1","pid":"115","type":"1","price":"1.00","peopleid":"3","groupid":"98","unit_desc":""},{"id":"122","title":"家电清洗","addtime":"1500953013","uid":"111412","ord":"99","is_show":"1","pid":"115","type":"1","price":"1.00","peopleid":"3","groupid":"97","unit_desc":""}]},{"id":"116","title":"日常保洁","addtime":"1500374096","uid":"111412","ord":"99","is_show":"1","pid":"0","type":"1","price":"0.00","peopleid":"3","groupid":"86","unit_desc":"","group":[{"id":"117","title":"日常保洁4","addtime":"1500374105","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"0.01","peopleid":"3","groupid":"96","unit_desc":""},{"id":"118","title":"日常保洁3","addtime":"1500374105","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"1.00","peopleid":"3","groupid":"95","unit_desc":""},{"id":"119","title":"日常保洁2","addtime":"1500374105","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"1.00","peopleid":"3","groupid":"94","unit_desc":""},{"id":"120","title":"日常保洁1","addtime":"1500374105","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"1.00","peopleid":"3","groupid":"88","unit_desc":""},{"id":"125","title":"日常保洁2小时","addtime":"1501224186","uid":"111412","ord":"99","is_show":"1","pid":"116","type":"1","price":"100.00","peopleid":"3","groupid":"99","unit_desc":"小时"}]}]
         * img : []
         * shop : [{"id":"1","title":"本地生活店铺1","phone":"1555555555","simg":"","desc":""}]
         * cleaning_shop_state : 1
         * schedule : [{"start":"1501430400","week":"周一","day":"07-31","time":[{"time":"06:30","state":"2","timestamp":"1501453800"},{"time":"07:00","state":"2","timestamp":"1501455600"},{"time":"07:30","state":"2","timestamp":"1501457400"},{"time":"08:00","state":"1","timestamp":"1501459200"},{"time":"08:30","state":"1","timestamp":"1501461000"},{"time":"09:00","state":"1","timestamp":"1501462800"},{"time":"09:30","state":"1","timestamp":"1501464600"},{"time":"10:00","state":"1","timestamp":"1501466400"},{"time":"10:30","state":"1","timestamp":"1501468200"},{"time":"11:00","state":"1","timestamp":"1501470000"},{"time":"11:30","state":"1","timestamp":"1501471800"},{"time":"12:00","state":"1","timestamp":"1501473600"},{"time":"12:30","state":"1","timestamp":"1501475400"},{"time":"13:00","state":"1","timestamp":"1501477200"},{"time":"13:30","state":"1","timestamp":"1501479000"},{"time":"14:00","state":"1","timestamp":"1501480800"},{"time":"14:30","state":"1","timestamp":"1501482600"},{"time":"15:00","state":"1","timestamp":"1501484400"},{"time":"15:30","state":"1","timestamp":"1501486200"},{"time":"16:00","state":"1","timestamp":"1501488000"},{"time":"16:30","state":"1","timestamp":"1501489800"},{"time":"17:00","state":"1","timestamp":"1501491600"},{"time":"17:30","state":"1","timestamp":"1501493400"},{"time":"18:00","state":"1","timestamp":"1501495200"},{"time":"18:30","state":"1","timestamp":"1501497000"},{"time":"19:00","state":"1","timestamp":"1501498800"},{"time":"19:30","state":"1","timestamp":"1501500600"},{"time":"20:00","state":"1","timestamp":"1501502400"},{"time":"20:30","state":"1","timestamp":"1501504200"},{"time":"21:00","state":"1","timestamp":"1501506000"},{"time":"21:30","state":"1","timestamp":"1501507800"},{"time":"22:00","state":"1","timestamp":"1501509600"}]},{"start":"1501516800","week":"周二","day":"08-01","time":[{"time":"06:30","state":"1","timestamp":"1501540200"},{"time":"07:00","state":"1","timestamp":"1501542000"},{"time":"07:30","state":"1","timestamp":"1501543800"},{"time":"08:00","state":"1","timestamp":"1501545600"},{"time":"08:30","state":"1","timestamp":"1501547400"},{"time":"09:00","state":"1","timestamp":"1501549200"},{"time":"09:30","state":"1","timestamp":"1501551000"},{"time":"10:00","state":"1","timestamp":"1501552800"},{"time":"10:30","state":"1","timestamp":"1501554600"},{"time":"11:00","state":"1","timestamp":"1501556400"},{"time":"11:30","state":"1","timestamp":"1501558200"},{"time":"12:00","state":"1","timestamp":"1501560000"},{"time":"12:30","state":"1","timestamp":"1501561800"},{"time":"13:00","state":"1","timestamp":"1501563600"},{"time":"13:30","state":"1","timestamp":"1501565400"},{"time":"14:00","state":"1","timestamp":"1501567200"},{"time":"14:30","state":"1","timestamp":"1501569000"},{"time":"15:00","state":"1","timestamp":"1501570800"},{"time":"15:30","state":"1","timestamp":"1501572600"},{"time":"16:00","state":"1","timestamp":"1501574400"},{"time":"16:30","state":"1","timestamp":"1501576200"},{"time":"17:00","state":"1","timestamp":"1501578000"},{"time":"17:30","state":"1","timestamp":"1501579800"},{"time":"18:00","state":"1","timestamp":"1501581600"},{"time":"18:30","state":"1","timestamp":"1501583400"},{"time":"19:00","state":"1","timestamp":"1501585200"},{"time":"19:30","state":"1","timestamp":"1501587000"},{"time":"20:00","state":"1","timestamp":"1501588800"},{"time":"20:30","state":"1","timestamp":"1501590600"},{"time":"21:00","state":"1","timestamp":"1501592400"},{"time":"21:30","state":"1","timestamp":"1501594200"},{"time":"22:00","state":"1","timestamp":"1501596000"}]},{"start":"1501603200","week":"周三","day":"08-02","time":[{"time":"06:30","state":"1","timestamp":"1501626600"},{"time":"07:00","state":"1","timestamp":"1501628400"},{"time":"07:30","state":"1","timestamp":"1501630200"},{"time":"08:00","state":"1","timestamp":"1501632000"},{"time":"08:30","state":"1","timestamp":"1501633800"},{"time":"09:00","state":"1","timestamp":"1501635600"},{"time":"09:30","state":"1","timestamp":"1501637400"},{"time":"10:00","state":"1","timestamp":"1501639200"},{"time":"10:30","state":"1","timestamp":"1501641000"},{"time":"11:00","state":"1","timestamp":"1501642800"},{"time":"11:30","state":"1","timestamp":"1501644600"},{"time":"12:00","state":"1","timestamp":"1501646400"},{"time":"12:30","state":"1","timestamp":"1501648200"},{"time":"13:00","state":"1","timestamp":"1501650000"},{"time":"13:30","state":"1","timestamp":"1501651800"},{"time":"14:00","state":"1","timestamp":"1501653600"},{"time":"14:30","state":"1","timestamp":"1501655400"},{"time":"15:00","state":"1","timestamp":"1501657200"},{"time":"15:30","state":"1","timestamp":"1501659000"},{"time":"16:00","state":"1","timestamp":"1501660800"},{"time":"16:30","state":"1","timestamp":"1501662600"},{"time":"17:00","state":"1","timestamp":"1501664400"},{"time":"17:30","state":"1","timestamp":"1501666200"},{"time":"18:00","state":"1","timestamp":"1501668000"},{"time":"18:30","state":"1","timestamp":"1501669800"},{"time":"19:00","state":"1","timestamp":"1501671600"},{"time":"19:30","state":"1","timestamp":"1501673400"},{"time":"20:00","state":"1","timestamp":"1501675200"},{"time":"20:30","state":"1","timestamp":"1501677000"},{"time":"21:00","state":"1","timestamp":"1501678800"},{"time":"21:30","state":"1","timestamp":"1501680600"},{"time":"22:00","state":"1","timestamp":"1501682400"}]},{"start":"1501689600","week":"周四","day":"08-03","time":[{"time":"06:30","state":"1","timestamp":"1501713000"},{"time":"07:00","state":"1","timestamp":"1501714800"},{"time":"07:30","state":"1","timestamp":"1501716600"},{"time":"08:00","state":"1","timestamp":"1501718400"},{"time":"08:30","state":"1","timestamp":"1501720200"},{"time":"09:00","state":"1","timestamp":"1501722000"},{"time":"09:30","state":"1","timestamp":"1501723800"},{"time":"10:00","state":"1","timestamp":"1501725600"},{"time":"10:30","state":"1","timestamp":"1501727400"},{"time":"11:00","state":"1","timestamp":"1501729200"},{"time":"11:30","state":"1","timestamp":"1501731000"},{"time":"12:00","state":"1","timestamp":"1501732800"},{"time":"12:30","state":"1","timestamp":"1501734600"},{"time":"13:00","state":"1","timestamp":"1501736400"},{"time":"13:30","state":"1","timestamp":"1501738200"},{"time":"14:00","state":"1","timestamp":"1501740000"},{"time":"14:30","state":"1","timestamp":"1501741800"},{"time":"15:00","state":"1","timestamp":"1501743600"},{"time":"15:30","state":"1","timestamp":"1501745400"},{"time":"16:00","state":"1","timestamp":"1501747200"},{"time":"16:30","state":"1","timestamp":"1501749000"},{"time":"17:00","state":"1","timestamp":"1501750800"},{"time":"17:30","state":"1","timestamp":"1501752600"},{"time":"18:00","state":"1","timestamp":"1501754400"},{"time":"18:30","state":"1","timestamp":"1501756200"},{"time":"19:00","state":"1","timestamp":"1501758000"},{"time":"19:30","state":"1","timestamp":"1501759800"},{"time":"20:00","state":"1","timestamp":"1501761600"},{"time":"20:30","state":"1","timestamp":"1501763400"},{"time":"21:00","state":"1","timestamp":"1501765200"},{"time":"21:30","state":"1","timestamp":"1501767000"},{"time":"22:00","state":"1","timestamp":"1501768800"}]},{"start":"1501776000","week":"周五","day":"08-04","time":[{"time":"06:30","state":"1","timestamp":"1501799400"},{"time":"07:00","state":"1","timestamp":"1501801200"},{"time":"07:30","state":"1","timestamp":"1501803000"},{"time":"08:00","state":"1","timestamp":"1501804800"},{"time":"08:30","state":"1","timestamp":"1501806600"},{"time":"09:00","state":"1","timestamp":"1501808400"},{"time":"09:30","state":"1","timestamp":"1501810200"},{"time":"10:00","state":"1","timestamp":"1501812000"},{"time":"10:30","state":"1","timestamp":"1501813800"},{"time":"11:00","state":"1","timestamp":"1501815600"},{"time":"11:30","state":"1","timestamp":"1501817400"},{"time":"12:00","state":"1","timestamp":"1501819200"},{"time":"12:30","state":"1","timestamp":"1501821000"},{"time":"13:00","state":"1","timestamp":"1501822800"},{"time":"13:30","state":"1","timestamp":"1501824600"},{"time":"14:00","state":"1","timestamp":"1501826400"},{"time":"14:30","state":"1","timestamp":"1501828200"},{"time":"15:00","state":"1","timestamp":"1501830000"},{"time":"15:30","state":"1","timestamp":"1501831800"},{"time":"16:00","state":"1","timestamp":"1501833600"},{"time":"16:30","state":"1","timestamp":"1501835400"},{"time":"17:00","state":"1","timestamp":"1501837200"},{"time":"17:30","state":"1","timestamp":"1501839000"},{"time":"18:00","state":"1","timestamp":"1501840800"},{"time":"18:30","state":"1","timestamp":"1501842600"},{"time":"19:00","state":"1","timestamp":"1501844400"},{"time":"19:30","state":"1","timestamp":"1501846200"},{"time":"20:00","state":"1","timestamp":"1501848000"},{"time":"20:30","state":"1","timestamp":"1501849800"},{"time":"21:00","state":"1","timestamp":"1501851600"},{"time":"21:30","state":"1","timestamp":"1501853400"},{"time":"22:00","state":"1","timestamp":"1501855200"}]},{"start":"1501862400","week":"周六","day":"08-05","time":[{"time":"06:30","state":"1","timestamp":"1501885800"},{"time":"07:00","state":"1","timestamp":"1501887600"},{"time":"07:30","state":"1","timestamp":"1501889400"},{"time":"08:00","state":"1","timestamp":"1501891200"},{"time":"08:30","state":"1","timestamp":"1501893000"},{"time":"09:00","state":"1","timestamp":"1501894800"},{"time":"09:30","state":"1","timestamp":"1501896600"},{"time":"10:00","state":"1","timestamp":"1501898400"},{"time":"10:30","state":"1","timestamp":"1501900200"},{"time":"11:00","state":"1","timestamp":"1501902000"},{"time":"11:30","state":"1","timestamp":"1501903800"},{"time":"12:00","state":"1","timestamp":"1501905600"},{"time":"12:30","state":"1","timestamp":"1501907400"},{"time":"13:00","state":"1","timestamp":"1501909200"},{"time":"13:30","state":"1","timestamp":"1501911000"},{"time":"14:00","state":"1","timestamp":"1501912800"},{"time":"14:30","state":"1","timestamp":"1501914600"},{"time":"15:00","state":"1","timestamp":"1501916400"},{"time":"15:30","state":"1","timestamp":"1501918200"},{"time":"16:00","state":"1","timestamp":"1501920000"},{"time":"16:30","state":"1","timestamp":"1501921800"},{"time":"17:00","state":"1","timestamp":"1501923600"},{"time":"17:30","state":"1","timestamp":"1501925400"},{"time":"18:00","state":"1","timestamp":"1501927200"},{"time":"18:30","state":"1","timestamp":"1501929000"},{"time":"19:00","state":"1","timestamp":"1501930800"},{"time":"19:30","state":"1","timestamp":"1501932600"},{"time":"20:00","state":"1","timestamp":"1501934400"},{"time":"20:30","state":"1","timestamp":"1501936200"},{"time":"21:00","state":"1","timestamp":"1501938000"},{"time":"21:30","state":"1","timestamp":"1501939800"},{"time":"22:00","state":"1","timestamp":"1501941600"}]},{"start":"1501948800","week":"周日","day":"08-06","time":[{"time":"06:30","state":"1","timestamp":"1501972200"},{"time":"07:00","state":"1","timestamp":"1501974000"},{"time":"07:30","state":"1","timestamp":"1501975800"},{"time":"08:00","state":"1","timestamp":"1501977600"},{"time":"08:30","state":"1","timestamp":"1501979400"},{"time":"09:00","state":"1","timestamp":"1501981200"},{"time":"09:30","state":"1","timestamp":"1501983000"},{"time":"10:00","state":"1","timestamp":"1501984800"},{"time":"10:30","state":"1","timestamp":"1501986600"},{"time":"11:00","state":"1","timestamp":"1501988400"},{"time":"11:30","state":"1","timestamp":"1501990200"},{"time":"12:00","state":"1","timestamp":"1501992000"},{"time":"12:30","state":"1","timestamp":"1501993800"},{"time":"13:00","state":"1","timestamp":"1501995600"},{"time":"13:30","state":"1","timestamp":"1501997400"},{"time":"14:00","state":"1","timestamp":"1501999200"},{"time":"14:30","state":"1","timestamp":"1502001000"},{"time":"15:00","state":"1","timestamp":"1502002800"},{"time":"15:30","state":"1","timestamp":"1502004600"},{"time":"16:00","state":"1","timestamp":"1502006400"},{"time":"16:30","state":"1","timestamp":"1502008200"},{"time":"17:00","state":"1","timestamp":"1502010000"},{"time":"17:30","state":"1","timestamp":"1502011800"},{"time":"18:00","state":"1","timestamp":"1502013600"},{"time":"18:30","state":"1","timestamp":"1502015400"},{"time":"19:00","state":"1","timestamp":"1502017200"},{"time":"19:30","state":"1","timestamp":"1502019000"},{"time":"20:00","state":"1","timestamp":"1502020800"},{"time":"20:30","state":"1","timestamp":"1502022600"},{"time":"21:00","state":"1","timestamp":"1502024400"},{"time":"21:30","state":"1","timestamp":"1502026200"},{"time":"22:00","state":"1","timestamp":"1502028000"}]}]
         */

        private String id;
        private String uid;
        private String simg;
        private String name;
        private String sale;
        private String label;
        private String star;
        private String price;
        private String ads;
        private String shopid;
        private String satisfaction;
        private String saturation;
        private String service_starttime;
        private String service_endtime;
        private String cleaning_shop_state;
        private String base;// 起售数量
        private String specialty;// 个人技能
        private List<GrouptitleBean> grouptitle = new ArrayList<>();
        private List<GroupBeanX> group = new ArrayList<>();
        private List<ProjectDetail.DataBean.ImgBean> img;
        private ShopBean shop = new ShopBean();
        private List<ScheduleBean> schedule = new ArrayList<>();

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSale() {
            return sale;
        }

        public void setSale(String sale) {
            this.sale = sale;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAds() {
            return ads;
        }

        public void setAds(String ads) {
            this.ads = ads;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getSatisfaction() {
            return satisfaction;
        }

        public void setSatisfaction(String satisfaction) {
            this.satisfaction = satisfaction;
        }

        public String getSaturation() {
            return saturation;
        }

        public void setSaturation(String saturation) {
            this.saturation = saturation;
        }

        public String getService_starttime() {
            return service_starttime;
        }

        public void setService_starttime(String service_starttime) {
            this.service_starttime = service_starttime;
        }

        public String getService_endtime() {
            return service_endtime;
        }

        public void setService_endtime(String service_endtime) {
            this.service_endtime = service_endtime;
        }

        public String getCleaning_shop_state() {
            return cleaning_shop_state;
        }

        public void setCleaning_shop_state(String cleaning_shop_state) {
            this.cleaning_shop_state = cleaning_shop_state;
        }

        public List<GrouptitleBean> getGrouptitle() {
            return grouptitle;
        }

        public void setGrouptitle(List<GrouptitleBean> grouptitle) {
            this.grouptitle = grouptitle;
        }

        public List<GroupBeanX> getGroup() {
            return group;
        }

        public void setGroup(List<GroupBeanX> group) {
            this.group = group;
        }

        public List<ProjectDetail.DataBean.ImgBean> getImg() {
            return img;
        }

        public void setImg(List<ProjectDetail.DataBean.ImgBean> img) {
            this.img = img;
        }

        public ShopBean getShop() {
            return shop;
        }

        public void setShop(ShopBean shop) {
            this.shop = shop;
        }

        public List<ScheduleBean> getSchedule() {
            return schedule;
        }

        public void setSchedule(List<ScheduleBean> schedule) {
            this.schedule = schedule;
        }

        public static class GrouptitleBean implements Serializable {
            /**
             * title : 日常保洁4
             * price : 0.01
             * unit_desc :
             */

            private String title;
            private String price;
            private String unit_desc;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getUnit_desc() {
                return unit_desc;
            }

            public void setUnit_desc(String unit_desc) {
                this.unit_desc = unit_desc;
            }
        }

        public static class GroupBeanX implements Serializable ,IPickerViewData{
            /**
             * id : 115
             * title : 家电清洗
             * addtime : 1500374096
             * uid : 111412
             * ord : 99
             * is_show : 1
             * pid : 0
             * type : 1
             * price : 0.00
             * peopleid : 3
             * groupid : 87
             * unit_desc :
             * group : [{"id":"121","title":"家电清洗1","addtime":"1500953013","uid":"111412","ord":"99","is_show":"1","pid":"115","type":"1","price":"1.00","peopleid":"3","groupid":"98","unit_desc":""},{"id":"122","title":"家电清洗","addtime":"1500953013","uid":"111412","ord":"99","is_show":"1","pid":"115","type":"1","price":"1.00","peopleid":"3","groupid":"97","unit_desc":""}]
             */

            private String id;
            private String title;
            private String addtime;
            private String uid;
            private String ord;
            private String is_show;
            private String pid;
            private String type;
            private String price;
            private String peopleid;
            private String groupid;
            private String unit_desc;
            private List<GroupBean> group = new ArrayList<>();

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

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getOrd() {
                return ord;
            }

            public void setOrd(String ord) {
                this.ord = ord;
            }

            public String getIs_show() {
                return is_show;
            }

            public void setIs_show(String is_show) {
                this.is_show = is_show;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPeopleid() {
                return peopleid;
            }

            public void setPeopleid(String peopleid) {
                this.peopleid = peopleid;
            }

            public String getGroupid() {
                return groupid;
            }

            public void setGroupid(String groupid) {
                this.groupid = groupid;
            }

            public String getUnit_desc() {
                return unit_desc;
            }

            public void setUnit_desc(String unit_desc) {
                this.unit_desc = unit_desc;
            }

            public List<GroupBean> getGroup() {
                return group;
            }

            public void setGroup(List<GroupBean> group) {
                this.group = group;
            }

            @Override
            public String getPickerViewText() {
                return title;
            }

            public static class GroupBean implements Serializable ,IPickerViewData{
                /**
                 * id : 121
                 * title : 家电清洗1
                 * addtime : 1500953013
                 * uid : 111412
                 * ord : 99
                 * is_show : 1
                 * pid : 115
                 * type : 1
                 * price : 1.00
                 * peopleid : 3
                 * groupid : 98
                 * unit_desc :
                 */

                private String id;
                private String title;
                private String addtime;
                private String uid;
                private String ord;
                private String is_show;
                private String pid;
                private String type;
                private String price;
                private String peopleid;
                private String groupid;
                private String unit_desc;
                private String time;

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
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

                public String getAddtime() {
                    return addtime;
                }

                public void setAddtime(String addtime) {
                    this.addtime = addtime;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public String getOrd() {
                    return ord;
                }

                public void setOrd(String ord) {
                    this.ord = ord;
                }

                public String getIs_show() {
                    return is_show;
                }

                public void setIs_show(String is_show) {
                    this.is_show = is_show;
                }

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getPeopleid() {
                    return peopleid;
                }

                public void setPeopleid(String peopleid) {
                    this.peopleid = peopleid;
                }

                public String getGroupid() {
                    return groupid;
                }

                public void setGroupid(String groupid) {
                    this.groupid = groupid;
                }

                public String getUnit_desc() {
                    return unit_desc;
                }

                public void setUnit_desc(String unit_desc) {
                    this.unit_desc = unit_desc;
                }

                @Override
                public String getPickerViewText() {
                    return title;
                }
            }
        }

        public static class ShopBean implements Serializable {
            /**
             * id : 1
             * title : 本地生活店铺1
             * phone : 1555555555
             * simg :
             * desc :
             */

            private String id;
            private String title;
            private String phone;
            private String simg;
            private String desc;

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

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }

    }
}
