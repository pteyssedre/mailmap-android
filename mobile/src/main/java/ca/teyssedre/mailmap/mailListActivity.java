/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Pierre Teyssedre
 * Created by  :  pteyssedre
 * Date        :  15-12-04
 * Application :  MailMap .
 * Package     :  ca.teyssedre.mailer .
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ca.teyssedre.mailmap;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;

import ca.teyssedre.mailer.MailHelper;
import ca.teyssedre.mailer.Mailer;
import ca.teyssedre.mailer.ThreadUtils;
import ca.teyssedre.mailer.callbacks.IActionCallback;
import ca.teyssedre.mailer.command.CommandImap;
import ca.teyssedre.mailer.exception.AccountException;
import ca.teyssedre.mailer.model.MailConfig;
import ca.teyssedre.mailer.model.MailMessage;
import ca.teyssedre.mailmap.dummy.DummyContent;
import ca.teyssedre.mailmap.mail.MailMessageAdapter;

/**
 * An activity representing a list of mails. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link mailDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class mailListActivity extends AppCompatActivity {

    public static final String ACCOUNT_ID = "AccountId";
    public static final int ADD_ACCOUNT_ID = 101;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Each time a sync of an account has been completed, the recyclerView must be updated
     */
    private IActionCallback<List<MailMessage>> SyncDone = new IActionCallback<List<MailMessage>>() {
        @Override
        public void onActionTerminated(Exception error, List<MailMessage> result) {
            if (error == null) {
                pushResult(result);
            }
        }

        @Override
        public void onActionUpdate(int max, int current) {
            setMax(max);
            setCurrent(current);
        }
    };
    private ProgressBar progressBar;
    private ProgressBar animBar;
    private RecyclerView recyclerView;
    private ArrayList<MailMessage> data;
    private long before;
    private final Object _lock = new Object();
    private List<MailMessage> allMessages = new ArrayList<>();
    private int done = 0;
    private int max = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MailApplication mailApp = (MailApplication) getApplicationContext();
        //mailApp.AddAccountIfNeeded();
        setContentView(R.layout.activity_mail_list);

        // TODO: add MailListFragment to container

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: start NewMailActivity
                Snackbar.make(view, "New mail action not supported yet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.mail_list);
        animBar = (ProgressBar) findViewById(R.id.list_progress_circle);
        progressBar = (ProgressBar) findViewById(R.id.list_progress_flat);
        assert recyclerView != null;

        if (findViewById(R.id.mail_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        if (savedInstanceState == null) {
            //TODO: change for load from local then fetch and merge from remote
            before = System.currentTimeMillis();
            test();
//            new AllTestMail(this).execute();
//            Mailer.getInstance(this).TestSync(DummyContent.TestMailConfig(), this.SyncDone);
        }
    }

    private void test() {
        ThreadUtils.runOnBackground(new Runnable() {
            @Override
            public void run() {
                MailConfig config = DummyContent.TestMailConfig();

                final Message[] inbox = CommandImap.getMessages(config, "INBOX", CommandImap.LastDays(2));
                max = inbox.length;
                setMax(max);
                for (int i = max - 1; i >= 0; i--) {
                    final Message message = inbox[i];
                    ThreadUtils.runOnBackground(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MailMessage newMail = MailHelper.ConvertMessage(message);
                                MailHelper.ParseContent(newMail);
                                addingMessage(newMail);
                                updateDone();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateDone() {
        synchronized (_lock) {
            done++;
            setCurrent(done);
            if (done == max) {
                pushResult(allMessages);
            }
        }
    }

    private void addingMessage(MailMessage newMail) {
        synchronized (_lock) {
            allMessages.add(newMail);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_ACCOUNT_ID:
                switch (resultCode) {
                    case RESULT_OK:
                        String accountId = data.getExtras().getString(ACCOUNT_ID);
                        try {
                            Mailer.getInstance(this).SyncAccountAsync(accountId, this.SyncDone);
                        } catch (AccountException e) {
                            e.printStackTrace();
                        }
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data", data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            data = (ArrayList<MailMessage>) savedInstanceState.getSerializable("data");
            pushResult(data);
        }
    }

    public boolean hasTwoPanels() {
        return mTwoPane;
    }

    public void setMax(final int max) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setMax(max * 1000);
            }
        });
    }

    public void setCurrent(final int current) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", current * 1000);
                animation.setDuration(500);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
            }
        });
    }

    public void pushResult(List<MailMessage> allMessages) {
        if (data != allMessages) {
            data = new ArrayList<>();
            data.addAll(allMessages);
            long time = System.currentTimeMillis() - before;
            String format = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(time),
                    TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
            );
            Log.d("MAIL_LIST", format);
        }
        Collections.sort(data, MailMessage.DATE_SORT);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new MailMessageAdapter(data));
            }
        });
    }
}
