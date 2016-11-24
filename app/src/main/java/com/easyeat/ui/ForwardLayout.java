package com.easyeat.ui;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easyeat.R;

/**
 * Created by chenglongwei on 11/24/16.
 */

public class ForwardLayout {
    private View topLine;
    private View bottomLine;
    private TextView text;
    private TextView value;
    private ImageView image;
    private static int marginSmall;

    public ForwardLayout(View root, CharSequence content, boolean showTopLine, boolean showBottomLine) {
        topLine = root.findViewById(R.id.line_top);
        bottomLine = root.findViewById(R.id.line_bottom);
        topLine.setVisibility(showTopLine ? View.VISIBLE : View.GONE);
        bottomLine.setVisibility(showBottomLine ? View.VISIBLE : View.GONE);
        text = (TextView) root.findViewById(R.id.tv_text);
        text.setText(content);
        value = (TextView) root.findViewById(R.id.tv_value);
        image = (ImageView) root.findViewById(R.id.iv_forward);
        if (marginSmall == 0) {
            marginSmall = root.getContext().getResources().getDimensionPixelOffset(R.dimen.margin_small);
        }
    }

    public void setForwardImageVisibility(boolean visibility) {
        image.setVisibility(visibility ? View.VISIBLE : View.GONE);
        if (!visibility) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.value.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.rightMargin = marginSmall;
            this.value.setLayoutParams(params);
        }
    }

    public void setValue(CharSequence value) {
        this.value.setText(value);
    }

    public void setColorValue(CharSequence value, int color) {
        Spannable spannable = new SpannableString(value);
        spannable.setSpan(new ForegroundColorSpan(color), 0, value.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        this.value.setText(spannable);
    }

    /**
     * 单条的，上下两条线都显示
     *
     * @param root
     * @param content
     * @return
     */
    public static ForwardLayout single(View root, CharSequence content) {
        return initForwardLayout(root, content, true, true, 0, 0);
    }

    /**
     * 放在上边的条，上边的线显示，下边的线不显示
     *
     * @param root
     * @param content
     * @return
     */
    public static ForwardLayout top(View root, CharSequence content) {
        return initForwardLayout(root, content, true, false, 0, 0);
    }

    /**
     * 放在中间的条，上边的线显示（并且左边缩进），下边不显示
     *
     * @param root
     * @param content
     * @return
     */
    public static ForwardLayout mid(View root, CharSequence content) {
        return initForwardLayout(root, content, true, false, marginSmall, 0);
    }

    /**
     * 放在下边的条，上边的线显示，并且左边缩进，下班的线也显示
     *
     * @param root
     * @param content
     * @return
     */
    public static ForwardLayout bottom(View root, CharSequence content) {
        return initForwardLayout(root, content, true, true, marginSmall, 0);
    }


    public static ForwardLayout initForwardLayout(View root, CharSequence content, boolean showTopLine, boolean showBottomLine, int topLeftMargin, int bottomLeftMargin) {
        ForwardLayout layout = new ForwardLayout(root, content, showTopLine, showBottomLine);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.topLine.getLayoutParams();
        params.leftMargin = topLeftMargin;
        layout.topLine.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) layout.bottomLine.getLayoutParams();
        params.leftMargin = bottomLeftMargin;
        layout.bottomLine.setLayoutParams(params);
        return layout;
    }
}