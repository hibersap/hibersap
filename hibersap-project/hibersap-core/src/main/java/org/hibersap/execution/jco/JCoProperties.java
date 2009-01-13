package org.hibersap.execution.jco;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
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
 */

/**
 * Defines constants for JCo property names as defined in JCo interface com.sap.conn.jco.ext.DestinationDataProvider.
 *
 * @author Carsten Erker
 */
public interface JCoProperties
{
    // SAP client
    static final String JCO_CLIENT_CLIENT = "hibersap.client.client";

    // Logon user
    static final String JCO_CLIENT_USER = "hibersap.client.user";

    // Logon user alias
    static final String JCO_CLIENT_ALIAS_USER = "hibersap.client.alias_user";

    // Logon password
    static final String JCO_CLIENT_PASSWD = "hibersap.client.passwd";

    // Logon language
    static final String JCO_CLIENT_LANG = "hibersap.client.lang";

    // SAP router string to use for a system protected by a firewall
    static final String JCO_CLIENT_SAPROUTER = "hibersap.client.saprouter";

    // SAP system number
    static final String JCO_CLIENT_SYSNR = "hibersap.client.sysnr";

    // SAP application server
    static final String JCO_CLIENT_ASHOST = "hibersap.client.ashost";

    // SAP message server
    static final String JCO_CLIENT_MSHOST = "hibersap.client.mshost";

    // optional: SAP message server port to use instead of the default sapms<sysid>
    static final String JCO_CLIENT_MSSERV = "hibersap.client.msserv";

    // Gateway host
    static final String JCO_CLIENT_GWHOST = "hibersap.client.gwhost";

    // Gateway service
    static final String JCO_CLIENT_GWSERV = "hibersap.client.gwserv";

    // System ID of the SAP system
    static final String JCO_CLIENT_R3NAME_ = "hibersap.client.r3name";

    // Group of SAP application servers
    static final String JCO_CLIENT_GROUP = "hibersap.client.group";

    // Program ID of external server program
    static final String JCO_CLIENT_TPNAME = "hibersap.client.tpname";

    // Host of external server program
    static final String JCO_CLIENT_TPHOST = "hibersap.client.tphost";

    // Type of remote host 2 = R/2, 3 = R/3, E = External
    static final String JCO_CLIENT_TYPE = "hibersap.client.type";

    // Enable/disable RFC trace (0 or 1)
    static final String JCO_CLIENT_TRACE = "hibersap.client.trace";

    // Enable/disable CPIC trace (-1 [take over environment value], 0 no trace, 1,2,3 different
    // amount of trace)
    static final String JCO_CLIENT_CPIC_TRACE = "hibersap.client.cpic_trace";

    // Initial codepage in SAP notation
    static final String JCO_CLIENT_CODEPAGE = "hibersap.client.codepage";

    // Get/Don't get a SSO ticket after logon (1 or 0)
    static final String JCO_CLIENT_GETSSO2 = "hibersap.client.getsso2";

    // Use the specified SAP Cookie Version 2 as logon ticket
    static final String JCO_CLIENT_MYSAPSSO2 = "hibersap.client.mysapsso2";

    // Use the specified X509 certificate as logon ticket
    static final String JCO_CLIENT_X509CERT = "hibersap.client.x509cert";

    // Enable/Disable logon check at open time, 1 (enable) or 0 (disable)
    static final String JCO_CLIENT_LCHECK = "hibersap.client.lcheck";

    // Secure network connection (SNC) mode, 0 (off) or 1 (on)
    static final String JCO_CLIENT_SNC_MODE = "hibersap.client.snc_mode";

    // SNC partner, e.g. p:CN=R3, O=XYZ-INC, C=EN
    static final String JCO_CLIENT_SNC_PARTNERNAME = "hibersap.client.snc_partnername";

    // SNC level of security, 1 to 9
    static final String JCO_CLIENT_SNC_QOP = "hibersap.client.snc_qop";

    // SNC name. Overrides default SNC partner
    static final String JCO_CLIENT_SNC_MYNAME = "hibersap.client.snc_myname";

    // Path to library which provides SNC service
    static final String JCO_CLIENT_SNC_LIB = "hibersap.client.snc_lib";

    // Enable/Disable dsr support (0 or 1)
    static final String JCO_CLIENT_DSR = "hibersap.client.dsr";

    // Maximum number of active connections that can be created for a destination simultaneously
    static final String JCO_DESTINATION_PEAK_LIMIT = "hibersap.destination.peak_limit";

    // Maximum number of idle connections kept open by the destination. A value of 0 has the effect
    // that there is no connection pooling.
    static final String JCO_DESTINATION_POOL_CAPACITY = "hibersap.destination.pool_capacity";

    // Time in ms after that the connections hold by the destination can be closed
    static final String JCO_DESTINATION_EXPIRATION_TIME = "hibersap.destination.expiration_time";

    // Period in ms after that the destination checks the released connections for expiration
    static final String JCO_DESTINATION_EXPIRATION_CHECK_PERIOD = "hibersap.destination.expiration_check_period";

    // Max time in ms to wait for a connection, if the max allowed number of connections is
    // allocated by the application
    static final String JCO_DESTINATION_MAX_GET_CLIENT_TIME = "hibersap.destination.max_get_client_time";

    // Specifies which destination should be used as repository, i.e. use this destination's
    // repository
    static final String JCO_DESTINATION_REPOSITORY_DESTINATION = "hibersap.destination.repository_destination";

    // Optional: If repository destination is not set, and this property is set, it will be used as
    // user for repository calls. This allows using a different user for repository lookups
    static final String JCO_DESTINATION_REPOSITORY_USER = "hibersap.destination.repository.user";

    // The password for a repository user. Mandatory, if a repository user should be used.
    static final String JCO_DESTINATION_REPOSITORY_PASSWD_ = "hibersap.destination.repository.passwd";

    // Optional: If SNC is used for this destination, it is possible to turn it off for repository
    // connections, if this property is set to 0. Defaults to the value of static final String
    // JCO_CLIENT_ = "hibersap.client.snc_mode
    static final String JCO_DESTINATION_REPOSITORY_SNC_MODE = "hibersap.destination.repository.snc_mode";
}
