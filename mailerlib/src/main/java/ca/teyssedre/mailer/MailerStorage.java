package ca.teyssedre.mailer;

import android.content.Context;

import java.util.List;

import ca.teyssedre.mailer.model.MailAccount;
import ca.teyssedre.mailer.model.MailMessage;

class MailerStorage {

    private final MailSource mailSource;
    private final AccountSource accountSource;

    protected MailerStorage(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.accountSource = new AccountSource(dbHelper);
        this.mailSource = new MailSource(dbHelper);
    }

    public List<MailAccount> getAllAccounts() {
        return accountSource.RetrieveAllAccounts();
    }

    public MailAccount getAccount(String accountId) {
        if (accountId == null || accountId.length() == 0)
            return null;
        return accountSource.getAccount(accountId);
    }

    public List<MailMessage> getMailForAccount(MailAccount account) {
        if(account != null){
            return mailSource.getAllMailFor(account);
        }
        return null;
    }
}
