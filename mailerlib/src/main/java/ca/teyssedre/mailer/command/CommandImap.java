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
package ca.teyssedre.mailer.command;

import java.util.Date;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;

import ca.teyssedre.mailer.Common;
import ca.teyssedre.mailer.model.MailConfig;

public class CommandImap {

    static final long DAY_IN_MS = 1000 * 60 * 60 * 24;
    public static final FlagTerm UNREAD = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

    public static Message[] getMessages(MailConfig config, String path, SearchTerm... flag) {
        Message[] messages = null;
        try {
            Session session = Session.getDefaultInstance(config.getProperties());
            Store store = session.getStore(Common.IMAPS_PROTOCOL);
            store.connect(config.getImapServer(), config.getImapUsername(), config.getImapPassword());

            Folder folder = store.getFolder(path);
            folder.open(Folder.READ_ONLY);
            if (flag == null || flag.length == 0) {
                messages = folder.getMessages();
            } else {
                messages = folder.search(new AndTerm(flag));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return messages;
    }

    public static Message[] getUnreadMessages(MailConfig config) {
        return getMessages(config, "INBOX", new FlagTerm(new Flags(Flags.Flag.SEEN), false));
    }

    public static SearchTerm LastDays(int i) {
        Date date = new Date(System.currentTimeMillis() - (i * DAY_IN_MS));
        SentDateTerm sentDateTerm = new SentDateTerm(ComparisonTerm.GE, date);
        ReceivedDateTerm receivedDateTerm = new ReceivedDateTerm(ComparisonTerm.GE, date);
        return new AndTerm(sentDateTerm, receivedDateTerm);
    }

}
