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

package ca.teyssedre.mailmap.mail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.teyssedre.mailer.model.MailMessage;
import ca.teyssedre.mailmap.R;
import ca.teyssedre.mailmap.mailDetailActivity;

public class MailViewModel extends RecyclerView.ViewHolder {

    public final View mView;
    public final ImageView icon;
    public final TextView subjectView;
    public final TextView mContentView;

    public MailViewModel(View itemView) {
        super(itemView);
        mView = itemView;
        icon = (ImageView) itemView.findViewById(R.id.mail_icon_sender);
        subjectView = (TextView) itemView.findViewById(R.id.mail_subject);
        mContentView = (TextView) itemView.findViewById(R.id.mail_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            itemView.setElevation(5);
            itemView.setTranslationZ(5);
        }
    }

    public void updateView(final MailMessage message) {
        if (message.isNew()) {
            subjectView.setTypeface(null, Typeface.BOLD);
            mContentView.setTypeface(null, Typeface.BOLD);
        } else {
            subjectView.setTypeface(null, Typeface.NORMAL);
            mContentView.setTypeface(null, Typeface.NORMAL);
        }
        subjectView.setText(message.getSubject());
        mContentView.setText(message.getContentUpTo(27));
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mTwoPane) {
//                Bundle arguments = new Bundle();
//                arguments.putParcelable("data", message);
//                mailDetailFragment fragment = new mailDetailFragment();
//                fragment.setArguments(arguments);
//                AppCompatActivity activity = (AppCompatActivity) mView.getContext();
//                activity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.mail_detail_container, fragment)
//                        .commit();
//                } else {
//                    Context context = v.getContext();
                Context context = mView.getContext();
                Intent intent = new Intent(context, mailDetailActivity.class);
                intent.putExtra("data", message);

                context.startActivity(intent);
//                }
            }
        });
    }
}
