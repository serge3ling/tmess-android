package com.herokuapp.tmess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private MsgSvc msgSvc;
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
                String msgText = Html.escapeHtml(
                        ((TextView) findViewById(R.id.message)).getText().
                        toString().trim().
                        replaceAll("\\Q\n\\E", "<br/>"));
                System.out.println(msgText);
                if (msgText.length() > 0) {
                    msgSvc.sendMsg(new Msg(0, msgText, MsgView.MAIL, MsgView.TO, 0));
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(MsgView.TO);
        setContentView(R.layout.activity_main);

        final LinearLayout layout = findViewById(R.id.scroll_linear);
        msgSvc = new MsgSvc(httpMsgSvc,
                new HttpMsgAfterRead() {
                    @Override
                    public void onRight(final String answer) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Msg msg = Msg.makeMsg(new JSONObject(answer));
                                    layout.addView(msgView.makeView(msg));
                                    msgSvc.setAfter(msg.getTime());
                                    postScroll();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onWrong(String answer) {
                        System.out.println(answer);
                    }
                },

                new HttpMsgAfterRead() {
                    @Override
                    public void onRight(final String answer) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<Msg> msgs = httpMsgSvc.makeList(answer);
                                long after = 0;
                                for (int i = 0; i < msgs.size(); i++) {
                                    layout.addView(msgView.makeView(msgs.get(i)));
                                    long maybeAfter = msgs.get(i).getTime();
                                    after = Math.max(maybeAfter, after);
                                }
                                if (after > 0) {
                                    msgSvc.setAfter(after);
                                }
                                postScroll();
                            }
                        });
                    }

                    @Override
                    public void onWrong(String answer) {
                        System.out.println(answer);
                    }
                }
        );

        /*
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
        Drawable background = this.getResources().getDrawable(R.drawable.own_msg_text_style);
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
        */

        //msgSvc.fetchLastMsgs(MsgView.MAIL, MsgView.TO/*, 0*/);
        msgSvc.startRunAsk();

        ((Button) findViewById(R.id.send)).setOnClickListener(makeSendListener());
    }
}
