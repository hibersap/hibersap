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

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManagerImplementor;
import org.hibersap.validation.BeanValidationInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
    public void createsBapiInterceptorsFromXmlConfig()
    {
        final Set<BapiInterceptor> interceptors = sessionManager.getBapiInterceptors();

        assertThat( interceptors.size(), is( 2 ) );
        assertThat( interceptors, hasItemInstanceOf( BapiInterceptorDummy.class ) );
        assertThat( interceptors, hasItemInstanceOf( BeanValidationInterceptor.class ) );
    }

    @Test
    public void initializesStandardExecutionInterceptorsAutomatically()
    {
        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();

        assertThat( interceptors, hasItemInstanceOf( SapErrorInterceptor.class ) );
    }

    @Test
    public void createsExecutionInterceptorsFromXmlConfig()
    {
        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();
        assertThat( interceptors.size(), is( 2 ) );
        final Class<ExecutionInterceptorDummy> clazz = ExecutionInterceptorDummy.class;
        assertThat( interceptors, hasItemInstanceOf( clazz ) );
    }

    private static <T> Matcher<Iterable<? super T>> hasItemInstanceOf( Class<T> clazz )
    {
        //noinspection RedundantTypeArguments
        return Matchers.<T>hasItem( Matchers.<T>instanceOf( clazz ) );
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

    public static class BapiInterceptorDummy implements BapiInterceptor
    {
        public void beforeExecution( Object bapiObject )
        {
            // dummy
        }

        public void afterExecution( Object bapiObject )
        {
            // dummy
        }
    }
}
