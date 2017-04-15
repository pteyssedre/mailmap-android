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


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ca.teyssedre.mailer.model.MailAccount;
import ca.teyssedre.mailer.model.MailMessage;

class MailSource {

    private final DBHelper dbHelper;
    private SQLiteDatabase database;
    private final String[] allColumns = {
            DBHelper.MAIL_SQL_ID, DBHelper.MAIL_ID,
            DBHelper.MAIL_ACCOUNT_ID, DBHelper.MAIL_TYPE,
            DBHelper.MAIL_CONTENT, DBHelper.MAIL_CONTENT_PLAIN, DBHelper.MAIL_IS_NEW};

    public MailSource(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    protected void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    protected void close() {
        database.close();
        dbHelper.close();
    }

    public List<MailMessage> getAllMailFor(MailAccount account) {
        List<MailMessage> messages = new ArrayList<>();
        try {
            open();
            Cursor query = database.query(DBHelper.MAIN_MAIL_TABLE, allColumns,
                    DBHelper.MAIL_ACCOUNT_ID + " = " + account.getId(), null, null, null, null);
            query.moveToFirst();
            while (!query.isAfterLast()) {
                messages.add(cursorToMailMessage(query));
                query.moveToNext();
            }
            query.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private MailMessage cursorToMailMessage(Cursor cursor) {
        return new MailMessage();
    }
}
