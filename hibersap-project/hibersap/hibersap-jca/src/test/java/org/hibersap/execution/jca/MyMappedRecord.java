package org.hibersap.execution.jca;

import java.util.HashMap;

import javax.resource.cci.MappedRecord;

// MappedRecord extends the Map interface in a non-generic way
@SuppressWarnings("unchecked")
public class MyMappedRecord
    extends HashMap
    implements MappedRecord
{
    private String recordName;

    private String recordShortDescription;

    public MyMappedRecord( String recordName )
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
