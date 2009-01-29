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
import org.hibersap.session.SessionFactory;

public class HibersapGenerator
{
    public void generate(String outputDir, String packagePath, String bapiName)
    {
        String packageDir = packagePath.replace( '.', File.separatorChar );
        File outputDirFile = new File( outputDir + File.separator + packageDir );
        outputDirFile.mkdirs();

        AnnotationConfiguration cfg = new AnnotationConfiguration();
        SessionFactory sessionFactory = cfg.buildSessionFactory();
        ReverseBapiMapper mapper = new ReverseBapiMapper();
        BapiMapping bapiMapping = mapper.map( bapiName, sessionFactory );
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
