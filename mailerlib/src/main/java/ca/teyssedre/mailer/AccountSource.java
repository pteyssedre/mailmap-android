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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ca.teyssedre.mailer.model.MailAccount;

class AccountSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] AllColumns = {
            DBHelper.MAIL_ACCOUNT_SQL_ID, DBHelper.MAIL_ACCOUNT_ID,
            DBHelper.MAIL_ACCOUNT_EMAIl, DBHelper.MAIL_ACCOUNT_USERNAME,
            DBHelper.MAIL_ACCOUNT_PASSWORD, DBHelper.MAIL_ACCOUNT_CONFIGURATION
    };

    protected AccountSource(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    protected void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    protected void close() {
        database.close();
        dbHelper.close();
    }

    public MailAccount AddMailAccount(MailAccount account) {
        try {
            ContentValues values = new ContentValues();
            values.put(DBHelper.MAIL_ACCOUNT_ID, account.getId());
            values.put(DBHelper.MAIL_ACCOUNT_EMAIl, account.getEmail());
            values.put(DBHelper.MAIL_ACCOUNT_USERNAME, account.getUsername());
            values.put(DBHelper.MAIL_ACCOUNT_PASSWORD, account.getPassword());
            values.put(DBHelper.MAIL_ACCOUNT_CONFIGURATION, account.getConfig());

            open();
            long insert = database.insert(DBHelper.MAIN_ACCOUNT_TABLE, null, values);
            close();

            account.setSqlId(insert);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return account;
    }

    public List<MailAccount> RetrieveAllAccounts() {
        List<MailAccount> accounts = new ArrayList<>();
        try {
            open();
            Cursor cursor = database.query(DBHelper.MAIN_ACCOUNT_TABLE,
                    AllColumns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                accounts.add(cursorToMailAccount(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    private MailAccount cursorToMailAccount(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return new MailAccount(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
    }

    public MailAccount getAccount(String accountId) {
        List<MailAccount> accounts = new ArrayList<>();
        try {
            open();
            Cursor cursor = database.query(DBHelper.MAIN_ACCOUNT_TABLE,
                    AllColumns, DBHelper.MAIL_ACCOUNT_ID + " = " + accountId,
                    null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                accounts.add(cursorToMailAccount(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts.size() > 0 ? accounts.get(0) : null;
    }
}
