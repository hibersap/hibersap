/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
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

package org.hibersap.it.jco;

import java.util.HashSet;
import java.util.Set;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;

@Bapi( "BAPI_MONITOR_GETLIST" )
@ThrowExceptionOnError
public class BapiFinder {

    @Table
    @Parameter( "COMPONENTS2SELECT" )
    private Set<SapComponent> componentsToSelect = new HashSet<SapComponent>();

    @Table
    @Parameter( "BAPILIST" )
    private Set<BapiDescription> bapiDescriptions;

    public BapiFinder( final String componentToSelect ) {
        this.componentsToSelect.add( new SapComponent( componentToSelect ) );
    }

    public Set<BapiDescription> getBapiDescriptions() {
        return bapiDescriptions;
    }

    @BapiStructure
    private static class SapComponent {

        @Parameter( "COMPONENT" )
        private String component;

        @SuppressWarnings( {"UnusedDeclaration"} ) // called by Hibersap
        public SapComponent() {
        }

        private SapComponent( final String component ) {
            this.component = component;
        }
    }
}
