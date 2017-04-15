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

package ca.teyssedre.mailmap.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import ca.teyssedre.mailer.model.MailMessage;
import ca.teyssedre.mailmap.R;
import ca.teyssedre.mailmap.mail.MailMessageAdapter;

/**
 * This fragment will be use to manage the {@link ca.teyssedre.mailer.model.MailMessage} items.
 * All logic regarding the list will be place here. Loading action interaction tracking etc.
 */
public class MailListFragment extends Fragment {

    private static final String MAIL_ITEMS = "mail_items";
    private RecyclerView recycler;
    private ProgressBar circleLoad;
    private ProgressBar flatLoad;
    private List<MailMessage> mails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.mail_list, container, false);
            recycler = (RecyclerView) v.findViewById(R.id.mail_list);
            circleLoad = (ProgressBar) v.findViewById(R.id.list_progress_circle);
            flatLoad = (ProgressBar) v.findViewById(R.id.list_progress_flat);
        }
        RestoreView(savedInstanceState);
        return v;
    }

    private void RestoreView(Bundle bundle) {
        if (bundle != null) {
            mails = (List<MailMessage>) bundle.getSerializable(MAIL_ITEMS);
            publishData();
        }else{
            displayLoading();
            // TODO: go fetch emails of current account
        }
    }

    private void publishData() {
        if (recycler != null) {
            recycler.post(new Runnable() {
                @Override
                public void run() {
                    circleLoad.setVisibility(View.GONE);
                    flatLoad.setVisibility(View.GONE);
                    recycler.setAdapter(new MailMessageAdapter(mails));
                }
            });
        }
    }

    private void displayLoading() {
        if (circleLoad != null) {
            circleLoad.post(new Runnable() {
                @Override
                public void run() {
                    circleLoad.setVisibility(View.VISIBLE);
                    flatLoad.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                }
            });
        }
    }
}
