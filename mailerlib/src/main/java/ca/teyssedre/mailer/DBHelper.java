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
package ca.teyssedre.mailer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    protected static final String MAIN_ACCOUNT_TABLE = "accounts";
    protected static final String MAIL_ACCOUNT_SQL_ID = "id";
    protected static final String MAIL_ACCOUNT_ID = "accountId";
    protected static final String MAIL_ACCOUNT_EMAIl = "address";
    protected static final String MAIL_ACCOUNT_USERNAME = "username";
    protected static final String MAIL_ACCOUNT_PASSWORD = "password";
    protected static final String MAIL_ACCOUNT_CONFIGURATION = "configuration";

    protected static final String MAIN_MAIL_TABLE = "mails";
    protected static final String MAIL_SQL_ID = "id";
    protected static final String MAIL_ID = "mailId";
    protected static final String MAIL_SUBJECT = "subject";
    protected static final String MAIL_TYPE = "type";
    protected static final String MAIL_CONTENT = "content";
    protected static final String MAIL_CONTENT_PLAIN = "contentPlain";
    protected static final String MAIL_IS_NEW = "isNew";
    protected static final String MAIL_RECIPIENTS = "recipients";
    protected static final String MAIL_TO = "mailTo";
    protected static final String MAIL_CC = "mailCC";
    protected static final String MAIL_CCI = "mailCCI";
    protected static final String MAIL_ORIGINS = "origins";

    private static final String CREATE_MAIL_ACCOUNT_TABLE = "CREATE TABLE " + MAIN_ACCOUNT_TABLE + "(" +
            MAIL_ACCOUNT_SQL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MAIL_ACCOUNT_ID + " TEXT NOT NULL," +
            MAIL_ACCOUNT_EMAIl + " TEXT NOT NULL," +
            MAIL_ACCOUNT_USERNAME + " TEXT NOT NULL," +
            MAIL_ACCOUNT_PASSWORD + " TEXT NOT NULL," +
            MAIL_ACCOUNT_CONFIGURATION + " TEXT NOT NUll" +
            ")";

    private static final String CREATE_MAIL_TABLE = "CREATE TABLE " + MAIN_MAIL_TABLE + "(" +
            MAIL_SQL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MAIL_ID + " TEXT NOT NULL," +
            MAIL_ACCOUNT_ID + " TEXT NOT NULL," +
            MAIL_SUBJECT + " TEXT NOT NULL," +
            MAIL_TYPE + " INTEGER," +
            MAIL_CONTENT + " TEXT NOT NULL," +
            MAIL_RECIPIENTS + " TEXT NOT NULL," +
            MAIL_TO + " TEXT NOT NULL," +
            MAIL_CC + " TEXT NOT NULL," +
            MAIL_CCI + " TEXT NOT NULL," +
            MAIL_ORIGINS + " TEXT NOT NULL," +
            MAIL_CONTENT_PLAIN + " TEXT NOT NULL" +
            ")";

    public DBHelper(Context context) {
        super(context, "mailer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MAIL_ACCOUNT_TABLE);
        db.execSQL(CREATE_MAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
