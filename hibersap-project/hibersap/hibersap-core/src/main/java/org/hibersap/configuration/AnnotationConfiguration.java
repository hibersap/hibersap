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
 * 
 * @author Carsten Erker
 */

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionFactory;

/**
 * Configures Hibersap using annotated BAPI classes.
 * 
 * There are three possibilities to add annotated classes:
 * <ol>
 * <li>using hibersap.properties:
 * hibersap.bapi_class.0=org.hibersap.examples.flightlist.FlightListBapi</li>
 * <li>similarily in hibersap.xml:
 * &lt;class&gt;org.hibersap.examples.flightlist.FlightListBapi&lt;/class&gt;</li>
 * <li>programmatically via addAnnotatedClass().</li>
 * </ol>
 * 
 * After calling buildSessionFactory() this instance can be discarded. The SessionFactory will be
 * used to interact with the back-end system. Properties may be overwritten using the methods in
 * this class' superclass, e.g. to specify different SAP systems in a test environment. For each SAP
 * system which will be accessed by the client application, one SessionFactory has to be built.
 * 
 * @author Carsten Erker
 */
public class AnnotationConfiguration
    extends Configuration
{
    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog( AnnotationConfiguration.class );

    protected AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();

    private final Set<Class<? extends Object>> annotatedClasses = new HashSet<Class<? extends Object>>();

    public AnnotationConfiguration()
    {
        addAnnotatedClassesFromProperties();
    }

    private void addAnnotatedClassesFromProperties()
    {
        for ( final Object key : properties.keySet() )
        {
            final String keyString = key.toString();

            if ( keyString.startsWith( HibersapProperties.BAPI_CLASSES_PREFIX ) )
            {
                final String valueClass = properties.getProperty( keyString );
                LOG.info( "Found BAPI class " + keyString + " = " + valueClass );
                addAnnotatedClass( SettingsFactory.getClassForName( valueClass, keyString ) );
            }
        }
    }

    /**
     * Adds an annotated BAPI class to the Configuration.
     * 
     * @param bapiClass
     */
    public void addAnnotatedClass( final Class<?> bapiClass )
    {
        annotatedClasses.add( bapiClass );
    }

    /**
     * Builds a SessionFactory object. Provide properties and add BAPI classes before calling this
     * method.
     * 
     * @return The SessionFactory
     */
    @Override
    public SessionFactory buildSessionFactory()
    {
        bapiMappingForClass.clear();
        for ( final Class<? extends Object> clazz : annotatedClasses )
        {
            LOG.info( "Mapping class " + clazz.getName() );
            final BapiMapping bapiMapping = bapiMapper.mapBapi( clazz );
            bapiMappingForClass.put( clazz, bapiMapping );
        }
        return super.buildSessionFactory();
    }
}
