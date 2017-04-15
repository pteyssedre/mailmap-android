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

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MailAccount implements Serializable {

    private long sqlId;
    private String id;
    private String email;
    private String username;
    private String password;
    private MailConfig configuration;

    public MailAccount() {

    }

    public MailAccount(long sqlId, String id, String email, String username, String password, String config) {
        this.sqlId = sqlId;
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        try {
            this.configuration = MailConfig.parse(new JSONObject(config));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected MailAccount(Parcel in) {
        sqlId = in.readLong();
        id = in.readString();
        email = in.readString();
        username = in.readString();
        password = in.readString();
        try {
            this.configuration = MailConfig.parse(new JSONObject(in.readString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getSqlId() {
        return sqlId;
    }

    public void setSqlId(long sqlId) {
        this.sqlId = sqlId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfig() {
        return configuration.Serialize();
    }

    public void setConfig(String config) {
        try {
            this.configuration = MailConfig.parse(new JSONObject(config));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MailConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(MailConfig configuration) {
        this.configuration = configuration;
    }
}
