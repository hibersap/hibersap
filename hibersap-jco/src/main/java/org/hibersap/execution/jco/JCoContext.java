/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.execution.jco;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.Property;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.Connection;
import org.hibersap.session.Context;

import java.util.List;
import java.util.Properties;

/*
 * Uses the SAP Java Connector to connect to SAP.
 * 
 * @author Carsten Erker
 */
public class JCoContext implements Context {

    private static final Log LOG = LogFactory.getLog( JCoContext.class );

    private static final String JCO_PROPERTIES_PREFIX = "jco.";

    private String destinationName;

    /*
     * {@inheritDoc}
     */
    public void configure( final SessionManagerConfig config )
            throws HibersapException {
        LOG.trace( "configure JCo context" );

        final Properties jcoProperties = new Properties();
        List<Property> properties = config.getProperties();

        for ( Property property : properties ) {
            String name = property.getName();
            if ( name.startsWith( JCO_PROPERTIES_PREFIX ) ) {
                jcoProperties.put( name, property.getValue() );
            }
        }

        destinationName = config.getName();
        if ( StringUtils.isEmpty( destinationName ) ) {
            throw new HibersapException( "A session manager name must be specified in Hibersap configuration" );
        }

        JCoEnvironment.registerDestination( destinationName, jcoProperties );
    }

    /*
     * {@inheritDoc}
     */
    public void close() {
        JCoEnvironment.unregisterDestination( destinationName );
        destinationName = null;
    }

    /*
     * {@inheritDoc}
     */
    public Connection getConnection() {
        return new JCoConnection( destinationName );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( destinationName == null ) ? 0 : destinationName.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        JCoContext other = (JCoContext) obj;
        if ( destinationName == null ) {
            if ( other.destinationName != null ) {
                return false;
            }
        } else if ( !destinationName.equals( other.destinationName ) ) {
            return false;
        }
        return true;
    }
}
