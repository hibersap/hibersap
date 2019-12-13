/*
 * Copyright (c) 2008-2019 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.session;

import javax.annotation.Generated;

/**
 * Defines logon properties that may be used at runtime, when a new Session is created, to overwrite
 * configured properties. Use this for logon properties than can not be statically defined, i.e.
 * when a user has to enter his SAP user / password in a dialog or when the application supports
 * single sign-on.
 * <p/>
 * When creating a new Session, only those fields that are set using the setter methods of this
 * class (i.e. which are not null) are used to overwrite the configured properties. All other
 * properties remain as configured.
 * <p/>
 * The fields reflect the fields of JCoCustomDestination.UserData.
 */
public class Credentials {

    private String user;

    private String aliasUser;

    private String client;

    private String language;

    private String password;

    private String ssoTicket;

    private String x509Certificate;

    public String getUser() {
        return user;
    }

    public Credentials setUser(final String user) {
        this.user = user;
        return this;
    }

    public String getAliasUser() {
        return aliasUser;
    }

    public Credentials setAliasUser(final String aliasUser) {
        this.aliasUser = aliasUser;
        return this;
    }

    public String getClient() {
        return client;
    }

    public Credentials setClient(final String client) {
        this.client = client;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Credentials setLanguage(final String language) {
        this.language = language;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Credentials setPassword(final String password) {
        this.password = password;
        return this;
    }

    public String getSsoTicket() {
        return ssoTicket;
    }

    public Credentials setSsoTicket(final String ssoTicket) {
        this.ssoTicket = ssoTicket;
        return this;
    }

    public String getX509Certificate() {
        return x509Certificate;
    }

    public Credentials setX509Certificate(final String certificate) {
        x509Certificate = certificate;
        return this;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "aliasUser='" + aliasUser + '\'' +
                ", user='" + user + '\'' +
                ", client='" + client + '\'' +
                ", language='" + language + '\'' +
                ", password='***'" +
                ", ssoTicket='" + ssoTicket + '\'' +
                ", x509Certificate='" + x509Certificate + '\'' +
                '}';
    }

    @Override
    @Generated("IntelliJ IDEA")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Credentials that = (Credentials) o;

        if (aliasUser != null ? !aliasUser.equals(that.aliasUser) : that.aliasUser != null) {
            return false;
        }
        if (client != null ? !client.equals(that.client) : that.client != null) {
            return false;
        }
        if (language != null ? !language.equals(that.language) : that.language != null) {
            return false;
        }
        if (password != null ? !password.equals(that.password) : that.password != null) {
            return false;
        }
        if (ssoTicket != null ? !ssoTicket.equals(that.ssoTicket) : that.ssoTicket != null) {
            return false;
        }
        if (user != null ? !user.equals(that.user) : that.user != null) {
            return false;
        }
        if (x509Certificate != null ? !x509Certificate.equals(that.x509Certificate) : that.x509Certificate != null) {
            return false;
        }

        return true;
    }

    @Override
    @Generated("IntelliJ IDEA")
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (aliasUser != null ? aliasUser.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (ssoTicket != null ? ssoTicket.hashCode() : 0);
        result = 31 * result + (x509Certificate != null ? x509Certificate.hashCode() : 0);
        return result;
    }
}
