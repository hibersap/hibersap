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

package org.hibersap.execution.jca;

import java.util.ArrayList;

import javax.resource.cci.IndexedRecord;

// IndexedRecord extends the List interface in a non-generic way
@SuppressWarnings("unchecked")
public class MyIndexedRecord
    extends ArrayList
    implements IndexedRecord
{

    private String recordShortDescription;

    private String recordName;

    public MyIndexedRecord( String recordName )
    {
        this.recordName = recordName;
    }

    public String getRecordName()
    {
        return recordName;
    }

    public String getRecordShortDescription()
    {
        return recordShortDescription;
    }

    public void setRecordName( String recordName )
    {
        this.recordName = recordName;
    }

    public void setRecordShortDescription( String recordShortDescription )
    {
        this.recordShortDescription = recordShortDescription;
    }
}
