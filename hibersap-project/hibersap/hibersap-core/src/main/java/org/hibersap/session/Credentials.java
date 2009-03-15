package org.hibersap.session;

/*
 * Copyright (C) 2008-2009 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 * 
 * Hibersap is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Hibersap is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Hibersap. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Carsten Erker
 */

/**
 * Defines logon properties that may be used at runtime, when a new Session is created, to overwrite
 * configured properties. Use this for logon properties than can not be statically defined, i.e.
 * when a user has to enter his SAP user / password in a dialog or when the application supports
 * single sign-on.
 * 
 * When creating a new Session, only those fields that are set using the setter methods of this
 * class (i.e. which are not null) are used to overwrite the configured properties. All other
 * properties remain as configured.
 * 
 * The fields reflect the fields of JCoCustomDestination.UserData.
 */
public class Credentials
{
    private String user;

    private String aliasUser;

    private String client;

    private String language;

    private String password;

    private String ssoTicket;

    private String x509Certificate;

    public String getUser()
    {
        return user;
    }

    public Credentials setUser( String user )
    {
        this.user = user;
        return this;
    }

    public String getAliasUser()
    {
        return aliasUser;
    }

    public Credentials setAliasUser( String aliasUser )
    {
        this.aliasUser = aliasUser;
        return this;
    }

    public String getClient()
    {
        return client;
    }

    public Credentials setClient( String client )
    {
        this.client = client;
        return this;
    }

    public String getLanguage()
    {
        return language;
    }

    public Credentials setLanguage( String language )
    {
        this.language = language;
        return this;
    }

    public String getPassword()
    {
        return password;
    }

    public Credentials setPassword( String password )
    {
        this.password = password;
        return this;
    }

    public String getSsoTicket()
    {
        return ssoTicket;
    }

    public Credentials setSsoTicket( String ssoTicket )
    {
        this.ssoTicket = ssoTicket;
        return this;
    }

    public String getX509Certificate()
    {
        return x509Certificate;
    }

    public Credentials setX509Certificate( String certificate )
    {
        x509Certificate = certificate;
        return this;
    }

}
