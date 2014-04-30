/*
 * Copyright (c) 2008-2014 akquinet tech@spree GmbH
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

package org.hibersap.generation;

import org.hibersap.HibersapException;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.generation.bapi.BapiClassFormatter;
import org.hibersap.generation.bapi.ReverseBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class HibersapGenerator {

    public void generate( final String outputDir, final String packagePath, final String bapiName ) {
        String packageDir = packagePath.replace( '.', File.separatorChar );
        File outputDirFile = new File( outputDir + File.separator + packageDir );
        outputDirFile.mkdirs();

        AnnotationConfiguration cfg = new AnnotationConfiguration();
        SessionManager sessionManager = cfg.buildSessionManager();
        ReverseBapiMapper mapper = new ReverseBapiMapper();
        BapiMapping bapiMapping = mapper.map( bapiName, sessionManager );
        BapiClassFormatter formatter = new BapiClassFormatter();
        Map<String, String> classForName = formatter.createClasses( bapiMapping, packagePath );

        for ( String className : classForName.keySet() ) {
            String fileName = className + ".java";
            String content = classForName.get( className );
            writeToDisk( outputDirFile, fileName, content );
        }
    }

    private void writeToDisk( final File outputDir, final String fileName, final String content ) {
        try {
            File file = new File( outputDir, fileName );
            FileWriter writer = new FileWriter( file );
            writer.append( content );
            writer.close();
        } catch ( IOException e ) {
            throw new HibersapException( "File " + fileName + " could not be written to file system.", e );
        }
    }
}
