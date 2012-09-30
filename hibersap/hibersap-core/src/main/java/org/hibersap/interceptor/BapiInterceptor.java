/*
 * Copyright (c) 2008-2012 akquinet tech@spree GmbH
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

package org.hibersap.interceptor;

/**
 * Implementations may be registered on the SessionManager and will then be called before and after
 * a function module in SAP is called. The Bapi object itself will be passed to the methods.
 */
public interface BapiInterceptor
{
    /**
     * Will be called before the function module is called in SAP.
     *
     * @param bapiObject The Bapi object as provided by the application code.
     */
    void beforeExecution( Object bapiObject );

    /**
     * Will be called after the function module is called in SAP.
     *
     * @param bapiObject The Bapi object as provided by the application code.
     */
    void afterExecution( Object bapiObject );
}
