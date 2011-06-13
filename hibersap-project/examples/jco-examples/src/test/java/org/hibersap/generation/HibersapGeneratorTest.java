package org.hibersap.generation;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class HibersapGeneratorTest
{

    private static final String TMP_DIR = System.getProperty( "java.io.tmpdir" );

    @Test
    public void testGenerate()
    {
        final HibersapGenerator generator = new HibersapGenerator();

        final String baseDir = TMP_DIR + File.separatorChar + "HibersapGeneratorTest";
        final String packagePath = "org.hibersap.generated";
        final File outdir = new File( baseDir + File.separatorChar + packagePath.replace( '.', File.separatorChar ) );

        generator.generate( baseDir, packagePath, "BAPI_MONITOR_GETLIST" );

        assertTrue( outdir.exists() );
        assertTrue( new File( outdir, "BapiMonitorGetlist.java" ).exists() );
        assertTrue( new File( outdir, "Components2select.java" ).exists() );
        assertTrue( new File( outdir, "Bapilist.java" ).exists() );
        assertTrue( new File( outdir, "Return.java" ).exists() );
        assertTrue( new File( outdir, "Systems2select.java" ).exists() );

        deleteFilesRecursively( baseDir );
    }

    private void deleteFilesRecursively( String pathName )
    {
        File file = new File( pathName );

        if ( file.isFile() )
        {
            file.delete();
        }
        else
        {
            String[] children = file.list();
            for ( String child : children )
            {
                deleteFilesRecursively( file.getPath() + File.separatorChar + child );
            }

            file.delete();
        }
    }
}
