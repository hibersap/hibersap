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

package org.hibersap.conversion;

import java.util.HashMap;
import java.util.Map;
import org.hibersap.mapping.ReflectionHelper;

/**
 * Holds instances of implementations of org.hibersap.conversion.Converter. The instances are
 * created lazily.
 *
 * @author Carsten Erker
 */
public class ConverterCache {

    private final Map<Class<? extends Converter>, Converter> converterForClass = new HashMap<Class<? extends Converter>, Converter>();

    /**
     * Called by the framework to get a Converter instance. If not yet in the cache, the instance
     * will be created lazily.
     *
     * @param clazz The Coverter implementation class
     * @return The Converter instance
     */
    public Converter getConverter(Class<? extends Converter> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("null");
        }
        Converter converter = converterForClass.get(clazz);
        if (converter == null) {
            converter = ReflectionHelper.newInstance(clazz);
            converterForClass.put(clazz, converter);
        }
        return converter;
    }

    /**
     * Returns the current number of Converter instances in the cache.
     *
     * @return The number of Converter instances
     */
    int getSize() {
        return converterForClass.size();
    }

    public void clear() {
        converterForClass.clear();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (converterForClass.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConverterCache other = (ConverterCache) obj;
        //noinspection RedundantIfStatement
        if (!converterForClass.keySet().equals(other.converterForClass.keySet())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + converterForClass;
    }
}
