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

package org.hibersap.it.jco.monitoring;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.execution.jco.util.JCoMonitoringUtil;
import org.hibersap.execution.jco.util.MonitoringData;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class JCoMonitoringUtilTest
{
    private final JCoMonitoringUtil util = new JCoMonitoringUtil();

    @Before
    public void setUp() throws Exception
    {
        // Hibersap is just used here to register the JCo destination, we don't really need the SessionManager
        new AnnotationConfiguration( "A12" ).buildSessionManager();
    }

    @Test
    public void showMonitoringData() throws Exception
    {
        MonitoringData data = util.getMonitoringData( "A12" );

        assertThat( data.getSystemDescription() ).startsWith( "DEST:                  A12" );
    }
}
