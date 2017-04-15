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

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import ca.teyssedre.mailer.command.MailType;
import ca.teyssedre.mailer.model.MailMessage;

public class MailHelper {

    private static final String TAG = "MailHelper";

    public static void InitMap() {
        // There is something wrong with MailCap,
        // JavaMail® can not find a handler for the multipart/mixed part,
        // so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public static MailMessage ConvertMessage(Message message) {
        MailMessage mail = new MailMessage();
        mail.setRAW(message);
        return mail;
    }

    public static void ParseContent(MailMessage mail) {
        try {
            Message raw = mail.getRAW();
            mail.setNew(!raw.getFlags().contains(Flags.Flag.SEEN));
            // From
            Address[] from = raw.getFrom();
            List<String> origin = new ArrayList<>();
            for (Address fro : from) {
                origin.add(fro.toString());
            }
            mail.setOrigin(origin);

            // To
            Address[] allRecipients = raw.getAllRecipients();
            List<String> reception = new ArrayList<>();
            for (Address address : allRecipients) {
                reception.add(address.toString());
            }
            mail.setRecipients(reception);

            // Subject
            mail.setSubject(raw.getSubject());

            // ContentType
            String contentType = raw.getContentType();
            if (contentType.toLowerCase().contains("multipart")) {
                mail.setType(MailType.MULTIPART);
            } else if (contentType.toLowerCase().contains("text/plain") || contentType.toLowerCase().contains("text/html")) {
                boolean plain = contentType.toLowerCase().contains("text/plain");
                boolean html = contentType.toLowerCase().contains("text/html");
                MailType d = html ? MailType.HTML_TEXT : MailType.OTHER;
                mail.setType(plain ? MailType.PLAIN_TEXT : d);
            } else {
                mail.setType(MailType.OTHER);
            }
            mail.setReceivedDate(raw.getReceivedDate());
            mail.setSentDate(raw.getSentDate());
            // Content
            switch (mail.getType()) {
                case HTML_TEXT:
                case PLAIN_TEXT:
                    mail.setContent(raw.getContent().toString());
                    mail.setContentPlain(raw.getContent().toString());
                    break;
                case MULTIPART:
                    ArrayList<String> attachments = new ArrayList<>();
                    Multipart content = null;
                    try {
                        content = (Multipart) raw.getContent();
                    } catch (Exception e) {
                        Object d = raw.getContent();
                        Log.e(TAG, "ClassCastException for " + d.toString());
                    }
                    if (content == null) {
                        return;
                    }
                    for (int j = 0; j < content.getCount(); j++) {
                        BodyPart bodyPart = content.getBodyPart(j);
                        String disposition = bodyPart.getDisposition();
                        if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { // BodyPart.ATTACHMENT doesn't work for gmail
                            Log.d(TAG, "Mail have some attachment");
//                            DataHandler handler = bodyPart.getDataHandler();
//                            Log.d(TAG, "file name : " + handler.getName());
//                            attachments.add(handler.getName());
                        } else {
                            if (bodyPart.getContentType().contains("text/plain")) {
                                mail.setContentPlain(bodyPart.getContent().toString());
                            } else if (bodyPart.getContentType().contains("text/html")) {
                                Log.d(TAG, "HTML " + bodyPart.getContentType());
                                mail.setContentHTML(bodyPart.getContent().toString());
                            }
                        }
                    }
                    mail.setAttachments(attachments);
                    break;
                case OTHER:
                    Log.d(TAG, " OTHER TYPE ... " + raw.getSubject());
                    break;
            }
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
