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

package org.hibersap.generation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.hibersap.HibersapException;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.generation.bapi.BapiClassFormatter;
import org.hibersap.generation.bapi.ReverseBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionManager;

public class HibersapGenerator
{
    public void generate( String outputDir, String packagePath, String bapiName )
    {
        String packageDir = packagePath.replace( '.', File.separatorChar );
        File outputDirFile = new File( outputDir + File.separator + packageDir );
        outputDirFile.mkdirs();

        AnnotationConfiguration cfg = new AnnotationConfiguration();
        SessionManager sessionManager = cfg.buildSessionManager();
        ReverseBapiMapper mapper = new ReverseBapiMapper();
        BapiMapping bapiMapping = mapper.map( bapiName, sessionManager );
        BapiClassFormatter formatter = new BapiClassFormatter();
        Map<String, String> classForName = formatter.createClasses( bapiMapping, packagePath );

        for ( String className : classForName.keySet() )
        {
            String fileName = className + ".java";
            String content = classForName.get( className );
            writeToDisk( outputDirFile, fileName, content );
        }
    }

    private void writeToDisk( File outputDir, String fileName, String content )
    {
        try
        {
            File file = new File( outputDir, fileName );
            FileWriter writer = new FileWriter( file );
            writer.append( content );
            writer.close();
        }
        catch ( IOException e )
        {
            throw new HibersapException( "File " + fileName + " could not be written to file system.", e );
        }
    }
}
