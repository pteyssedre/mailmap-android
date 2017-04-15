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

package ca.teyssedre.mailmap.dummy;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

import ca.teyssedre.mailer.MailHelper;
import ca.teyssedre.mailer.ThreadUtils;
import ca.teyssedre.mailer.command.CommandImap;
import ca.teyssedre.mailer.model.MailConfig;
import ca.teyssedre.mailer.model.MailMessage;
import ca.teyssedre.mailmap.mailListActivity;

public class AllTestMail extends AsyncTask<String, String, String> {

    private final mailListActivity activity;
    private List<MailMessage> allMessages;

    public AllTestMail(mailListActivity mailListActivity) {
        this.allMessages = new ArrayList<>();
        this.activity = mailListActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... params) {
        MailConfig config = DummyContent.TestMailConfig();

        final Message[] inbox = CommandImap.getMessages(config, "INBOX", null);
        final int nbMessages = inbox.length;
        publishProgress("max", String.valueOf(nbMessages));
        for (int i = nbMessages - 1; i >= 0; i--) {
            final Message message = inbox[i];
            final int finalI = i;
            ThreadUtils.runOnBackground(new Runnable() {
                @Override
                public void run() {
                    try {
                        MailMessage newMail = MailHelper.ConvertMessage(message);
                        MailHelper.ParseContent(newMail);
                        addingMessage(newMail);
                        int count = nbMessages - finalI;
                        publishProgress("current", String.valueOf(count));
                        if(count == 0){
                            activity.pushResult(allMessages);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return null;
    }

    private void addingMessage(MailMessage newMail) {
        synchronized (this) {
            allMessages.add(newMail);
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        String action = values[0];
        int value = Integer.parseInt(values[1]);
        if ("max".equals(action)) {
            activity.setMax(value);
        } else if ("current".equals(action)) {
            activity.setCurrent(value);

        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
