package com.herokuapp.tmess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.herokuapp.tmess.entity.Msg;
import com.herokuapp.tmess.svc.HttpMsgAfterRead;
import com.herokuapp.tmess.svc.HttpMsgSvc;
import com.herokuapp.tmess.svc.MsgSvc;
import com.herokuapp.tmess.view.MsgView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MsgSvc msgSvc = new MsgSvc();
    private MsgView msgView = new MsgView(this);
    private HttpMsgSvc httpMsgSvc = new HttpMsgSvc();

    private void postScroll() {
        final View scrollView = findViewById(R.id.scroll_view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, scrollView.getHeight());
            }
        });
    }

    private void fillInMessages() {
        LinearLayout layout = findViewById(R.id.scroll_linear);

        List<Msg> msgs = msgSvc.fetchMsgs();
        for (int i = 0; i < msgs.size(); i++) {
            layout.addView(msgView.makeView(msgs.get(i)));
        }

        postScroll();
    }

    private View.OnClickListener makeSendListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgSvc.sendMsg(new Msg(0,
                        ((TextView) findViewById(R.id.message)).getText().toString().
                                replaceAll("\\Q\n\\E", "<br/>"),
                        MsgView.MAIL,
                        "axel@tmess.d54637.com",
                        1582664242002L
                        ));

                LinearLayout layout = findViewById(R.id.scroll_linear);
                if (((LinearLayout) layout).getChildCount() > 0) {
                    ((LinearLayout) layout).removeAllViews();
                }

                ((EditText) findViewById(R.id.message)).setText("");

                fillInMessages();
                //testHttpGet();
                testHttpPost();
            }
        };
    }

    private void testHttpGet() {
        HttpMsgAfterRead afterRead = new HttpMsgAfterRead() {
            @Override
            public void onRight(String answer) {
                System.out.println("afterRead.onRight()");
                httpMsgSvc.makeList(answer);
            }
            @Override
            public void onWrong(String answer) {
                System.out.println("afterRead.onWrong()");
            }
        };

        String query = "?" + httpMsgSvc.paramsByTwoChattersAndTimeAfter(
                "unga@tmess.d54637.com",
                "axel@tmess.d54637.com",
                19
        );
        if (httpMsgSvc.open(HttpMsgSvc.URL_START + query,
                "GET", "", afterRead)) {
            System.out.println("Opened alright.");
            httpMsgSvc.sendAndRead();
        }
    }

    private void testHttpPost() {
        HttpMsgAfterRead afterRead = new HttpMsgAfterRead() {
            @Override
            public void onRight(String answer) {
                System.out.println("afterRead.onRight()");
                httpMsgSvc.makeMsg(answer);
            }
            @Override
            public void onWrong(String answer) {
                System.out.println("afterRead.onWrong()");
            }
        };

        Msg msg = new Msg(0, "testHttpPost",
                "axel@tmess.d54637.com", "unga@tmess.d54637.com",
                1582764242002L);
        if (httpMsgSvc.open(HttpMsgSvc.URL_START,
                "POST", msg.toJsonString(), afterRead)) {
            System.out.println("Opened alright.");
            httpMsgSvc.sendAndRead();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(MsgView.TO);
        setContentView(R.layout.activity_main);

        LinearLayout layout = findViewById(R.id.scroll_linear);

        ConstraintLayout constraintLayout  = new ConstraintLayout(this);
        TextView textView2 = new TextView(this);
        textView2.setId(View.generateViewId());
        textView2.setText("textView2");
        constraintLayout.addView(textView2);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(textView2.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraintSet.setHorizontalWeight(textView2.getId(), 0.9f);
        constraintSet.applyTo(constraintLayout);

        layout.addView(constraintLayout);

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        Space space3 = new Space(this);
        LinearLayout.LayoutParams space3LayoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.2f
        );
        space3.setLayoutParams(space3LayoutParams);
        Drawable background = this.getResources().getDrawable(R.drawable.msg_text_style);
        TextView textView3 = new TextView(this);
        textView3.setId(View.generateViewId());
        textView3.setText("textView3");
        LinearLayout.LayoutParams textView3LayoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.8f
        );
        textView3.setLayoutParams(textView3LayoutParams);
        textView3.setBackground(background);
        innerLayout.addView(space3);
        innerLayout.addView(textView3);
        layout.addView(innerLayout);

        fillInMessages();

        ((Button) findViewById(R.id.send)).setOnClickListener(makeSendListener());
    }
}
