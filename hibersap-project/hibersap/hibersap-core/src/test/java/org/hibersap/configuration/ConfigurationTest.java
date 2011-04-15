package org.hibersap.configuration;

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

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.BeanValidationInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManagerImplementor;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

/**
 * @author Carsten Erker
 */
public class ConfigurationTest
{
    private Configuration configuration;
    private SessionManagerConfig sessionManagerConfig;
    private SessionManagerImplementor sessionManager;

    @Before
    public void createConfiguration()
    {
        configuration = new Configuration( "TEST" )
        {
            // create instance of abstract class
        };
        sessionManagerConfig = configuration.getSessionManagerConfig();
        sessionManagerConfig.setContext( DummyContext.class.getName() );
        sessionManager = ( SessionManagerImplementor ) configuration.buildSessionManager();
    }

    @Test
    public void executionInterceptorCanBeManuallyAdded()
    {
        final ExecutionInterceptor dummyInterceptor = new ExecutionInterceptorDummy();
        configuration.addExecutionInterceptor( dummyInterceptor );

        assertThat( sessionManager.getExecutionInterceptors(), hasItem( dummyInterceptor ) );
    }

    @Test
    public void overwritesContextClass() throws Exception
    {
        sessionManagerConfig.setContext( "test" );

        assertThat( sessionManagerConfig.getContext(), is( "test" ) );
    }

    @Test
    public void overwritesProperties()
            throws Exception
    {
        sessionManagerConfig.setProperty( "jco.client.user", "test" );

        assertThat( sessionManagerConfig.getProperty( "jco.client.user" ), is( "test" ) );
    }

    @Test
    public void overwritesCompleteSessionManagerConfiguration()
            throws Exception
    {
        final SessionManagerConfig config = new SessionManagerConfig().setProperty( "testkey", "testvalue" );

        configuration.setSessionManagerConfig( config );

        final SessionManagerConfig sessionManagerConfig = configuration.getSessionManagerConfig();
        assertThat( sessionManagerConfig.getProperties().size(), is( 1 ) );
        assertThat( sessionManagerConfig.getProperty( "testkey" ), is( "testvalue" ) );
    }

    @Test
    public void createsBapiInterceptorsFromXmlConfig()
    {
        final Set<BapiInterceptor> interceptors = sessionManager.getBapiInterceptors();
        assertThat( interceptors.size(), is( 1 ) );
        assertThat( interceptors.iterator().next(), is( CoreMatchers.instanceOf( BeanValidationInterceptor.class ) ) );
    }

    @Test
    public void initializesStandardInterceptorsAutomatically()
    {
        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();

        assertThat( interceptors, hasItem( instanceOf( SapErrorInterceptor.class ) ) );
    }

    @Test
    public void createsExecutionInterceptorsFromXmlConfig()
    {
        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();
        assertThat( interceptors.size(), is( 2 ) );
        assertThat( interceptors, hasItem( instanceOf( ExecutionInterceptorDummy.class ) ) );
    }

    private static Matcher<ExecutionInterceptor> instanceOf( Class<? extends ExecutionInterceptor> clazz )
    {
        return CoreMatchers.instanceOf( clazz );
    }

    public static class ExecutionInterceptorDummy implements ExecutionInterceptor
    {
        public void afterExecution( BapiMapping bapiMapping, Map<String, Object> functionMap )
        {
            // dummy
        }

        public void beforeExecution( BapiMapping bapiMapping, Map<String, Object> functionMap )
        {
            // dummy
        }
    }
}
