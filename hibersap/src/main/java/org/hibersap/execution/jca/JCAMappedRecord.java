package org.hibersap.execution.jca;

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

import java.util.TreeMap;

import javax.resource.cci.MappedRecord;

/**
 * Thin wrapper around Map to implement MappedRecord
 * 
 * @author dahm
 */
@SuppressWarnings("unchecked")
public class JCAMappedRecord
    extends TreeMap
    implements MappedRecord
{
    private static final long serialVersionUID = 1L;

    private String recordName = "";

    private String shortDescr = "";

    public JCAMappedRecord( final String name )
    {
        this( name, "" );
    }

    public JCAMappedRecord( final String name, final String shortDescr )
    {
        this.recordName = name;
        this.shortDescr = shortDescr;
    }

    public String getRecordName()
    {
        return recordName;
    }

    public String getRecordShortDescription()
    {
        return shortDescr;
    }

    public void setRecordName( final String name )
    {
        this.recordName = name;
    }

    public void setRecordShortDescription( final String shortDescr )
    {
        this.shortDescr = shortDescr;
    }

    @Override
    public String toString()
    {
        return "MappedRecord(" + recordName + ") = " + super.toString();
    }
}
