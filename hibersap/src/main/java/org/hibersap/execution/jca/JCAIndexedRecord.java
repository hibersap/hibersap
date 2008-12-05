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
import java.util.ArrayList;

import javax.resource.cci.IndexedRecord;

/**
 * Thin wrapper around List to implement IndexedRecord.
 * 
 * @author dahm
 */
@SuppressWarnings("unchecked")
public class JCAIndexedRecord
    extends ArrayList
    implements IndexedRecord
{
    private static final long serialVersionUID = 1L;

    private String recordName;

    private String recordShortDescription;

    public JCAIndexedRecord( final String recordName )
    {
        this.recordName = recordName;
    }

    public String getRecordName()
    {
        return recordName;
    }

    public void setRecordName( final String recordName )
    {
        this.recordName = recordName;
    }

    public String getRecordShortDescription()
    {
        return recordShortDescription;
    }

    public void setRecordShortDescription( final String recordShortDescription )
    {
        this.recordShortDescription = recordShortDescription;
    }

    @Override
    public String toString()
    {
        return "IndexedRecord(" + recordName + ") = " + super.toString();
    }
}
