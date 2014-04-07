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

package org.hibersap.it.jco;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;

import java.util.HashSet;
import java.util.Set;

@Bapi( "BAPI_MONITOR_GETLIST" )
@ThrowExceptionOnError
public class BapiFinder
{
    @Table
    @Parameter( "COMPONENTS2SELECT" )
    private Set<SapComponent> componentsToSelect = new HashSet<SapComponent>();

    @Table
    @Parameter( "BAPILIST" )
    private Set<BapiDescription> bapiDescriptions;

    public BapiFinder( String componentToSelect )
    {
        this.componentsToSelect.add( new SapComponent( componentToSelect ) );
    }

    public Set<BapiDescription> getBapiDescriptions()
    {
        return bapiDescriptions;
    }

    @BapiStructure
    private static class SapComponent
    {
        @Parameter( "COMPONENT" )
        private String component;

        @SuppressWarnings( {"UnusedDeclaration"} ) // called by Hibersap
        public SapComponent()
        {
        }

        private SapComponent( String component )
        {
            this.component = component;
        }
    }
}
