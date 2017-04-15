
package ca.teyssedre.mailer;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

import ca.teyssedre.mailer.model.MailAccount;
import ca.teyssedre.mailer.model.MailMessage;

class SyncAccountTask implements ParseMailRunnable.TaskRunnableParseMethods, FetchAllMailRunnable.AllMessageRunnableMethods {

    private final Mailer instance;
    private final List<MailMessage> allMessages;
    private final MailAccount account;
    private Thread mCurrentThread;
    private Message[] messages;
    private Runnable fetchRunnable;
    protected TaskStatus status = TaskStatus.None;
    private boolean fetcherDone;
    private int ParserDone;

    public SyncAccountTask(Mailer mailer, MailAccount account) {
        this.instance = mailer;
        this.account = account;
        this.allMessages = new ArrayList<>();
        this.fetchRunnable = new FetchAllMailRunnable(this, account);
    }

    protected List<MailMessage> getMailMessagesList() {
        return allMessages;
    }

    protected Message[] getMessages() {
        return messages;
    }

    @Override
    public void handleParserStatus(RunnableStatus status) {
        switch (status) {
            case Run:
                setStatus(TaskStatus.Started);
                break;
            case Stop:
                ParserDone++;
                if (ParserDone == messages.length) {
                    setStatus(TaskStatus.Terminated);
                } else {
                    setStatus(TaskStatus.Running);
                }
                break;
            case Interrupt:
                setStatus(TaskStatus.Cancel);
                break;
        }
    }

    @Override
    public void setParserThread(Thread thread) {
        setCurrentThread(thread);
    }

    @Override
    public void newMailMessageParsed(MailMessage mailMessage) {
//        synchronized (this) {
            allMessages.add(mailMessage);
//        }
    }

    public void setCurrentThread(Thread thread) {
//        synchronized (instance) {
//            mCurrentThread = thread;
//        }
    }

    public Thread getCurrentThread() {
        synchronized (instance) {
            return mCurrentThread;
        }
    }

    @Override
    public void handleFetcherStatus(RunnableStatus status) {
        switch (status) {
            case Run:
                setStatus(TaskStatus.Started);
                break;
            case Stop:
                if (!fetcherDone) {
                    setStatus(TaskStatus.Pending);
                    fetcherDone = true;
                }
                break;
            case Interrupt:
                setStatus(TaskStatus.Cancel);
                break;
        }
    }

    @Override
    public void setFetcherThread(Thread thread) {
        setCurrentThread(thread);
    }

    @Override
    public void setAllMessages(Message[] messages) {
        this.messages = messages;
    }

    public Runnable getFetchRunnable() {
        return fetchRunnable;
    }

    public Runnable getNewParserRunnable(Message message, int i) {
        return new ParseMailRunnable(this, message,i);
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        this.instance.handleSyncUpdate(this);
    }

    public int getNbParsed() {
        return ParserDone;
    }
}
