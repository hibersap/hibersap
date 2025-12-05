/*
 * Copyright (c) 2008-2025 tech@spree GmbH
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

package org.hibersap.execution.jca;

import java.util.HashMap;
import javax.resource.cci.MappedRecord;

// MappedRecord extends the Map interface in a non-generic way
@SuppressWarnings("rawtypes")
public class MyMappedRecord extends HashMap implements MappedRecord {

    private String recordName;

    private String recordShortDescription;

    public MyMappedRecord(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordShortDescription() {
        return recordShortDescription;
    }

    public void setRecordShortDescription(String recordShortDescription) {
        this.recordShortDescription = recordShortDescription;
    }
}
