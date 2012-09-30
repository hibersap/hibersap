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

package org.hibersap.configuration;

import org.hibersap.ConfigurationException;
import org.hibersap.MappingException;
import org.hibersap.bapi.BapiTransactionCommit;
import org.hibersap.interceptor.ExecutionInterceptor;
import org.hibersap.interceptor.impl.SapErrorInterceptor;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;
import org.hibersap.session.SessionManagerImpl;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class AnnotationConfigurationTest
{
    private static final Class<BapiTransactionCommit> BAPI_CLASS = BapiTransactionCommit.class;

    private AnnotationConfiguration configuration = new AnnotationConfiguration();

    @Test
    public void addsSapErrorInterceptorToSessionManagerPerDefault()
    {
        SessionManagerImpl sessionManager = configureAndBuildSessionManager();

        final Set<ExecutionInterceptor> interceptors = sessionManager.getExecutionInterceptors();

        assertThat( interceptors.toArray() ).hasAtLeastOneElementOfType( SapErrorInterceptor.class );
    }

    @Test
    public void addsAnnotatedClassToSessionManager()
    {
        configuration.getSessionManagerConfig().addAnnotatedClass( BAPI_CLASS );
        SessionManagerImpl sessionManager = configureAndBuildSessionManager();

        Map<Class<?>, BapiMapping> bapiMappings = sessionManager.getBapiMappings();
        assertThat( bapiMappings ).hasSize( 1 );
        assertThat( bapiMappings.get( BAPI_CLASS ) ).isNotNull();
    }

    @Test( expected = MappingException.class )
    public void throwsMappingExceptionWhenClassWasAddedThatIsNotAnnotated()
    {
        configuration.getSessionManagerConfig().addAnnotatedClass( Object.class );
        configureAndBuildSessionManager();
    }

    @Test( expected = ConfigurationException.class )
    public void throwsConfigurationExceptionWhenBapiClassWasAddedThatIsNotAnnotated()
    {
        configuration.getSessionManagerConfig().getAnnotatedClasses().add( "does.not.Exist" );
        configureAndBuildSessionManager();
    }

    private SessionManagerImpl configureAndBuildSessionManager()
    {
        configuration.getSessionManagerConfig().setContext( DummyContext.class.getName() );
        SessionManager sessionManager = configuration.buildSessionManager();
        return ( SessionManagerImpl ) sessionManager;
    }
}
