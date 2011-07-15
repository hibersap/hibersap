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
