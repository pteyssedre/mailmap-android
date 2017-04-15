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

package ca.teyssedre.mailer.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import ca.teyssedre.mailer.Common;

public class MailConfig {

    //region Properties
    private Properties properties;

    private String protocol;

    private boolean imapSsl;
    private boolean imapSslIgnore;
    private int imapPort;
    private String imapServer;
    private String imapUsername;
    private String imapPassword;

    private boolean smtpSsl;
    private boolean smtpSslIgnore;
    private int smtpPort;
    private String smtpServer;
    private String smtpUsername;
    private String smtpPassword;
    //endregion

    //region Constructors
    public MailConfig() {
        properties = new Properties();
    }

    public MailConfig(JSONObject config) {
        parseJSON(config);
    }
    //endregion

    //region parsers

    /**
     * Parser to reader and reset the {@link MailConfig} instance.
     *
     * @param object {@link JSONObject} instance to parse.
     */
    private void parseJSON(JSONObject object) {
        if (object == null) {
            throw new RuntimeException("Invalid object to parse");
        }
        //Properties need to be reset to assure proper configuration.
        properties = new Properties();
        try {
            if (object.has(Common.MAIL_PROTOCOL)) {
                setProtocol(object.getString(Common.MAIL_PROTOCOL));
                if (protocol.equals(Common.IMAPS_PROTOCOL)) {
                    parseForIMAPS(object);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Parser to retrieve the IMAP SSL config.
     *
     * @param object {@link JSONObject} instance to parse.
     */
    private void parseForIMAPS(JSONObject object) {
        try {
            setImapSsl(true);
            if (object.has(Common.IMAPS_HOST)) {
                setImapServer(object.getString(Common.IMAPS_HOST));
            }
            if (object.has(Common.IMAPS_PORT)) {
                setImapPort(object.getInt(Common.IMAPS_PORT));
            }
            if (object.has(Common.IMAPS_SOCKET_SSL_CHECK_IDENTITY)) {
                setImapSslIgnore(object.getBoolean(Common.IMAPS_SOCKET_SSL_CHECK_IDENTITY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Getters
    public Properties getProperties() {
        return properties;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isImapSsl() {
        return imapSsl;
    }

    public boolean isImapSslIgnore() {
        return imapSslIgnore;
    }

    public int getImapPort() {
        return imapPort;
    }

    public String getImapServer() {
        return imapServer;
    }

    public String getImapUsername() {
        return imapUsername;
    }

    public String getImapPassword() {
        return imapPassword;
    }

    public boolean isSmtpSsl() {
        return smtpSsl;
    }

    public boolean isSmtpSslIgnore() {
        return smtpSslIgnore;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }
    //endregion

    //region Setters
    public void setProtocol(String protocol) {
        this.protocol = protocol;
        properties.setProperty(Common.MAIL_PROTOCOL, protocol);
    }

    public void setImapSsl(boolean imapSsl) {
        this.imapSsl = imapSsl;
        if (this.imapSsl) {
            properties.setProperty(Common.MAIL_PROTOCOL, Common.IMAPS_PROTOCOL);
            properties.setProperty(Common.IMAPS_SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory");
            properties.setProperty(Common.IMAPS_SOCKET_FACTORY_FALLBACK, "false");
        }
    }

    public void setImapSslIgnore(boolean imapSslIgnore) {
        this.imapSslIgnore = imapSslIgnore;
        if (this.imapSslIgnore) {
            properties.setProperty(Common.IMAPS_SOCKET_SSL_CHECK_IDENTITY, "false");
            properties.setProperty(Common.IMAPS_SOCKET_SSL_TRUST, "*");
        }
    }

    public void setImapPort(int imapPort) {
        this.imapPort = imapPort;
        properties.setProperty(Common.IMAPS_PORT, String.valueOf(imapPort));
    }

    public void setImapServer(String imapServer) {
        this.imapServer = imapServer;
        properties.setProperty(Common.IMAPS_HOST, imapServer);
    }

    public void setImapUsername(String imapUsername) {
        this.imapUsername = imapUsername;
    }

    public void setImapPassword(String imapPassword) {
        this.imapPassword = imapPassword;
    }

    public void setSmtpSsl(boolean smtpSsl) {
        this.smtpSsl = smtpSsl;
    }

    public void setSmtpSslIgnore(boolean smtpSslIgnore) {
        this.smtpSslIgnore = smtpSslIgnore;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }
    //endregion

    public static MailConfig parse(JSONObject config) {
        MailConfig configuration = new MailConfig();
        configuration.parseJSON(config);
        return configuration;
    }

    public String Serialize() {
        return null;
    }
}
