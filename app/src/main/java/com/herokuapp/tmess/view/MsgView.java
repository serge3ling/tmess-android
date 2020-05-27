package com.herokuapp.tmess.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.herokuapp.tmess.R;
import com.herokuapp.tmess.entity.Msg;

public class MsgView {
    public static final String MAIL = "axel@tmess.d54637.com";
    public static final String TO = "unga@tmess.d54637.com";
    private static final float TEXT_WEIGHT = 0.8f;

    private final Context context;

    private LinearLayout.LayoutParams spaceLayoutParams = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            (1 - TEXT_WEIGHT)
    );
    private LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            TEXT_WEIGHT
    );

    public MsgView(Context context) {
        this.context = context;
        textLayoutParams.setMargins(4, 4, 4, 24);
    }

    public View makeView(Msg msg) {
        boolean ownMsg = msg.getFrom().toLowerCase().equals(MAIL.toLowerCase());

        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.HORIZONTAL);

        Space space = new Space(context);
        space.setLayoutParams(spaceLayoutParams);

        Drawable background = context.getResources().getDrawable((ownMsg) ?
                R.drawable.own_msg_text_style :
                R.drawable.other_s_msg_text_style);

        TextView textView = new TextView(context);
        textView.setId(View.generateViewId());
        textView.setText(Html.fromHtml(msg.getText()));
        textView.setLayoutParams(textLayoutParams);
        textView.setBackground(background);
        textView.setTextColor(ContextCompat.getColor(context, R.color.msgTextColor));

        if (ownMsg) {
            view.addView(space);
            view.addView(textView);
        } else {
            view.addView(textView);
            view.addView(space);
        }

        return view;
    }
}
