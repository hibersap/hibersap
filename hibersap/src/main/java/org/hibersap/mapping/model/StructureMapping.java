package org.hibersap.mapping.model;

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

import java.util.HashSet;
import java.util.Set;

/**
 * @author Carsten Erker
 */
public class StructureMapping
    extends ObjectMapping
{
    private final Set<FieldMapping> parameters;

    public StructureMapping( Class<?> associatedClass, String sapName, String javaName )
    {
        super( associatedClass, sapName, javaName );
        parameters = new HashSet<FieldMapping>();
    }

    public StructureMapping( Class<?> associatedClass, String sapName, String javaName, Set<FieldMapping> parameters )
    {
        super( associatedClass, sapName, javaName );
        this.parameters = parameters;
    }

    public void addParameter( FieldMapping fieldParam )
    {
        parameters.add( fieldParam );
    }

    public Set<FieldMapping> getParameters()
    {
        return parameters;
    }

    @Override
    public ParamType getParamType()
    {
        return ParamType.STRUCTURE;
    }

    public void setParameters( Set<FieldMapping> parameters )
    {
        this.parameters.clear();
        this.parameters.addAll( parameters );
    }
}
