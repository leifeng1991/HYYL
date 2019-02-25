package com.xxzlkj.licallife.utils;

import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;


public class LocalLifeUnitUtils {

    public static String getUnitContent(String num, String unit, String unitDesc) {
        return getUnitLeft(unit) + getUnitRight(num, unit, unitDesc);
    }

    public static String getUnitLeft(String unit) {
        String unitLeft = "";
        if ("1".equals(unit)) {// 数量
            unitLeft = "数　　量：";
        } else if ("2".equals(unit)) {// 时间
            unitLeft = "服务时长：";
        }
        return unitLeft;
    }

    public static String getUnitRight(String num, String unit, String unitDesc) {
        String numStr = "";
        if ("1".equals(unit)) {// 数量
            numStr = num;
        } else if ("2".equals(unit)) {// 时间
            numStr = String.valueOf(NumberFormatUtils.toInt(num) * 0.5f);
        }
        return StringUtil.subZeroAndDot(numStr) + unitDesc;
    }

}
