package ca.teyssedre.mailer;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;

import ca.teyssedre.mailer.callbacks.IActionCallback;
import ca.teyssedre.mailer.exception.AccountException;
import ca.teyssedre.mailer.model.MailAccount;
import ca.teyssedre.mailer.model.MailConfig;
import ca.teyssedre.mailer.model.MailMessage;

public class Mailer {

    public static final String ADD_ACCOUNT_ACTION = "AddAction";

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    public static Mailer instance;

    private final MailerStorage storage;
    private final ThreadPoolExecutor executor;

    private SyncAccountTask syncAccountTask;
    private IActionCallback<List<MailMessage>> syncCallback;

    private Mailer(Context context) {
        int NUMBER_OF_CORE = Runtime.getRuntime().availableProcessors();
        this.storage = new MailerStorage(context);
        LinkedBlockingQueue<Runnable> mImapWorkQueue = new LinkedBlockingQueue<>();
        executor = new ThreadPoolExecutor(NUMBER_OF_CORE, NUMBER_OF_CORE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mImapWorkQueue);
    }

    public static Mailer getInstance(Context context) {
        if (instance == null) {
            instance = new Mailer(context);
        }
        return instance;
    }

    public static Mailer getInstance() {
        if (instance == null) {
            throw new RuntimeException("No Context has been provide, no storage available");
        }
        return instance;
    }

    public boolean hasProfile() {
        List<MailAccount> accounts = this.storage.getAllAccounts();
        return accounts != null && accounts.size() > 0;
    }

    public void SyncAccountAsync(String accountId, IActionCallback<List<MailMessage>> syncDone) throws AccountException {
        if (syncAccountTask == null) {
            MailAccount account = this.storage.getAccount(accountId);
            if (account != null) {
                syncAccountTask = new SyncAccountTask(this, account);
                syncCallback = syncDone;
                executor.execute(syncAccountTask.getFetchRunnable());
            } else {
                throw new AccountException("No account found");
            }
        }
    }

    public void TestSync(MailConfig config, IActionCallback<List<MailMessage>> syncDone) {
        MailAccount account = new MailAccount();
        account.setConfiguration(config);
        syncAccountTask = new SyncAccountTask(this, account);
        syncCallback = syncDone;
        executor.execute(syncAccountTask.getFetchRunnable());
    }

    protected void handleSyncUpdate(SyncAccountTask task) {
        Message[] messages = task.getMessages();
        switch (task.status) {
            case Running:
                syncCallback.onActionUpdate(messages.length, task.getNbParsed());
                break;
            case Pending:
                Log.d("Mailer", "launching all thread for parsing");
                for (int i = messages.length - 1; i >= 0; i--) {
                    Message message = messages[i];
                    executor.execute(task.getNewParserRunnable(message, i));
                }
                Log.d("Mailer", "all pushed on executor " + messages.length);
                break;
            case Terminated:
                syncCallback.onActionTerminated(null, task.getMailMessagesList());
                syncAccountTask = null;
                break;
        }
    }

    public enum Id {
        ADD_ACCOUNT_ID
    }
}
