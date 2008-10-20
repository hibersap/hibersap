package org.hibersap.execution;

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

import java.util.Map;

import org.hibersap.session.Session;
import org.hibersap.session.Transaction;

/**
 * Implementations of this interface define the functionality how to communicate
 * with SAP, using for example the SAP Java Connector or a JCA resource adapter.
 * The implementation to be used by a session factory is specified by the
 * property <code>hibersap.executor_class</code>. The default implementation
 * is org.hibersap.execution.jco.JCoConnection. Implementations must provide a
 * default constructor.
 *
 * @author Carsten Erker
 */
public interface Connection
{
    Transaction beginTransaction( Session session );

    Transaction getTransaction();

    void execute( String bapiName, Map<String, Object> functionMap );

    void close();
}
