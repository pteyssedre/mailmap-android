package ca.teyssedre.mailer.model;

import android.os.Parcel;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.mail.Message;

import ca.teyssedre.mailer.command.MailType;

public class MailMessage implements Serializable {

    public static final Comparator<MailMessage> DATE_SORT = new Comparator<MailMessage>() {
        @Override
        public int compare(MailMessage lhs, MailMessage rhs) {
            return lhs.receivedDate.compareTo(rhs.receivedDate) * -1;
        }
    };

    private MailType type;
    private String subject;
    private List<String> recipients;
    private String content;
    private List<String> attachments;
    private Message RAW;
    private Date receivedDate;
    private Date sentDate;
    private String contentPlain;
    private String contentHTML;
    private List<String> origin;
    private boolean isNew;

    public MailMessage() {
    }

    protected MailMessage(Parcel in) {
        subject = in.readString();
        recipients = in.createStringArrayList();
        content = in.readString();
        attachments = in.createStringArrayList();
        contentPlain = in.readString();
        contentHTML = in.readString();
        origin = in.createStringArrayList();
        type = MailType.parse(in.readInt());
    }

    public MailType getType() {
        return type;
    }

    public void setType(MailType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getContentUpTo(int maxLength) {
        if (contentPlain == null) {
            return "";
        }
        if (contentPlain.length() <= maxLength) {
            return contentPlain.trim();
        } else {
            String copy = contentPlain.replace("\n", " ").replace("\t", " ").replace("\r", " ");
            while (copy.contains("  ")) {
                copy = copy.replace("  ", " ").trim();
            }
            return copy.substring(0, maxLength - 4) + " ...";
        }
    }

    public Message getRAW() {
        return RAW;
    }

    public void setRAW(Message RAW) {
        this.RAW = RAW;
    }

    public String getContentPlain() {
        return contentPlain;
    }

    public void setContentPlain(String contentPlain) {
        this.contentPlain = contentPlain;
    }

    public String getContentHTML() {
        return contentHTML;
    }

    public void setContentHTML(String contentHTML) {
        this.contentHTML = contentHTML;
    }

    public List<String> getOrigin() {
        return origin;
    }

    public void setOrigin(List<String> origin) {
        this.origin = origin;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        this.isNew = aNew;
    }
}
