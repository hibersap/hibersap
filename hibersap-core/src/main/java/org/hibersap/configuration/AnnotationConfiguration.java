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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.ConfigurationException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;

import java.util.HashMap;

/**
 * Configures Hibersap using annotated BAPI classes.
 * <p/>
 * There are two possibilities to add annotated classes:
 * <ol>
 * <li>In hibersap.xml:
 * &lt;annotated-class&gt;org.hibersap.examples.flightlist.FlightListBapi&lt;/anotated-class&gt;</li>
 * <li>programmatically via addAnnotatedClass().</li>
 * </ol>
 * <p/>
 * After calling buildSessionManager() this instance can be discarded. The SessionManager will be
 * used to interact with the back-end system. Properties may be overwritten using the methods in
 * this class' superclass, e.g. to specify different SAP systems in a test environment. For each SAP
 * system which will be accessed by the client application, one SessionManager has to be built.
 *
 * @author Carsten Erker
 */
public class AnnotationConfiguration extends Configuration
{
    private static final Log LOG = LogFactory.getLog( AnnotationConfiguration.class );

    private AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();

    public AnnotationConfiguration()
    {
        super();
    }

    public AnnotationConfiguration( SessionManagerConfig config )
    {
        super( config );
    }

    public AnnotationConfiguration( String name )
    {
        super( name );
    }

    /**
     * Builds a SessionManager object.
     *
     * @return The SessionManager
     */
    @Override
    public SessionManager buildSessionManager()
    {
        final HashMap<Class<?>, BapiMapping> bapiMappings = new HashMap<Class<?>, BapiMapping>();

        for ( final String className : getSessionManagerConfig().getAnnotatedClasses() )
        {
            try
            {
                LOG.info( "Mapping BAPI class " + className );
                Class<?> clazz = Class.forName( className );
                final BapiMapping bapiMapping = bapiMapper.mapBapi( clazz );
                bapiMappings.put( clazz, bapiMapping );
            }
            catch ( ClassNotFoundException e )
            {
                String message = "Cannot find class " + className + " in classpath";
                LOG.error( message );
                throw new ConfigurationException( message, e );
            }
        }
        setBapiMappings( bapiMappings );
        return super.buildSessionManager();
    }

    public String getSessionManagerName()
    {
        return getSessionManagerConfig().getName();
    }
}
