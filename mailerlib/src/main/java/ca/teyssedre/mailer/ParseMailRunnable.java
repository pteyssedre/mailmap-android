/**
 * ********************************************************************************************************
 * <p/>
 * All rights reserved © 2015  -  Innovative Imaging Technologies  -  www.iitreacts.com
 * This computer program may not be used, copied, distributed, corrected, modified, translated,
 * transmitted or assigned without Innovative Imaging Technologies's prior written authorization.
 * <p/>
 * Created by  :  pteyssedre
 * Date        :  15-12-07
 * Application :  mailmap-android .
 * Package     :  ca.teyssedre.mailer .
 * <p/>
 * ********************************************************************************************************
 */
package ca.teyssedre.mailer;

import java.util.Random;

import javax.mail.Message;

import ca.teyssedre.mailer.model.MailMessage;

class ParseMailRunnable implements Runnable {

    private final int index;

    protected interface TaskRunnableParseMethods {

        void handleParserStatus(RunnableStatus status);

        void setParserThread(Thread thread);

        void newMailMessageParsed(MailMessage mailMessage);
    }

    private final Message message;
    final TaskRunnableParseMethods task;

    ParseMailRunnable(TaskRunnableParseMethods task, Message message, int i ) {
        this.task = task;
        this.message = message;
        this.index = i;
    }


    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        try {

            task.handleParserStatus(RunnableStatus.Run);

            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
//            System.out.println(index);

            task.setParserThread(Thread.currentThread());

            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            MailMessage mailMessage = MailHelper.ConvertMessage(message);

            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

//            Thread.sleep(Random());
            MailHelper.ParseContent(mailMessage);


            task.newMailMessageParsed(mailMessage);

            task.handleParserStatus(RunnableStatus.Stop);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            task.handleParserStatus(RunnableStatus.Interrupt);

            // Clear thread ref in up task
            task.setParserThread(null);

            // Clears the Thread's interrupt flag
            Thread.interrupted();
        }
    }

    private int Random(){
        Random r = new Random();
        int Low = 10;
        int High = 100;
        return r.nextInt(High-Low) + Low;
    }
}
