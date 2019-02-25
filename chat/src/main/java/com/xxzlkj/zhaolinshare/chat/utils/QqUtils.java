package com.xxzlkj.zhaolinshare.chat.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.xxzlkj.zhaolinshare.chat.EmoticonsEditText;


public class QqUtils {

    public static void initEmoticonsEditText(EmoticonsEditText etContent) {
        etContent.addEmoticonFilter(new QqFilter());
    }


    public static void spannableEmoticonFilter(TextView tv_content, String content) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        Spannable spannable = QqFilter.spannableFilter(tv_content.getContext(),
                spannableStringBuilder,
                content,
                EmoticonsKeyboardUtils.getFontHeight(tv_content));
        tv_content.setText(spannable);
    }
}
