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

import javax.mail.Flags;
import javax.mail.search.FlagTerm;

public class Common {

    public static final String MAIL_PROTOCOL = "mail.store.protocol";

    public static final String IMAPS_PROTOCOL = "imaps";
    public static final String IMAPS_HOST = "mail.imaps.host";
    public static final String IMAPS_PORT = "mail.imaps.port";
    public static final String IMAPS_SOCKET_FACTORY_CLASS = "mail.imaps.socketFactory.class";
    public static final String IMAPS_SOCKET_FACTORY_FALLBACK = "mail.imaps.socketFactory.fallback";
    public static final String IMAPS_SOCKET_SSL_CHECK_IDENTITY = "mail.imaps.ssl.checkserveridentity";
    public static final String IMAPS_SOCKET_SSL_TRUST = "mail.imaps.ssl.trust";

    public static final String IMAP_PROTOCOL = "imap";
    public static final String IMAP_HOST = "mail.imap.host";
    public static final String IMAP_PORT = "mail.imap.port";


    public static final FlagTerm NEW_MAIL = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
    public static final FlagTerm ALL_READY_READ_MAIL = new FlagTerm(new Flags(Flags.Flag.SEEN), true);
    public static final FlagTerm DRAFT_MAIL = new FlagTerm(new Flags(Flags.Flag.DRAFT), true);
    public static final FlagTerm FLAGGED_MAIL = new FlagTerm(new Flags(Flags.Flag.FLAGGED), true);
    public static final FlagTerm DELETED_MAIL = new FlagTerm(new Flags(Flags.Flag.DELETED), true);
}
