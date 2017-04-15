package ca.teyssedre.mailer;

import javax.mail.Message;

import ca.teyssedre.mailer.command.CommandImap;
import ca.teyssedre.mailer.model.MailAccount;

class FetchAllMailRunnable implements Runnable {

    private final MailAccount account;
    private final AllMessageRunnableMethods task;

    protected interface AllMessageRunnableMethods {

        void handleFetcherStatus(RunnableStatus status);

        void setFetcherThread(Thread thread);

        void setAllMessages(Message[] messages);
    }

    public FetchAllMailRunnable(AllMessageRunnableMethods task, MailAccount account) {
        this.task = task;
        this.account = account;
    }

    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        try {

            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            task.handleFetcherStatus(RunnableStatus.Run);

            task.setFetcherThread(Thread.currentThread());

            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            MailHelper.InitMap();
            Message[] inbox = CommandImap.getMessages(account.getConfiguration(), "INBOX", null);

            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            task.setAllMessages(inbox);

            task.handleFetcherStatus(RunnableStatus.Stop);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            task.handleFetcherStatus(RunnableStatus.Interrupt);

            task.setFetcherThread(null);

            Thread.interrupted();
        }
    }
}
