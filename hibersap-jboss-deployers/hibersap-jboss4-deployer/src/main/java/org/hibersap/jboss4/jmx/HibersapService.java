package org.hibersap.jboss4.jmx;

import org.apache.log4j.Logger;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.xml.HibersapConfig;
import org.hibersap.configuration.xml.SessionManagerConfig;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HibersapService
    implements HibersapServiceMBean
{

    private static final Logger LOGGER = Logger.getLogger( HibersapService.class );

    private final HibersapConfig config;

    private final AtomicBoolean status = new AtomicBoolean();

    private final List<AnnotationConfiguration> configs = new ArrayList<AnnotationConfiguration>();

    private ClassLoader cl;

    public HibersapService( HibersapConfig config, ClassLoader classLoader )
    {
        this.config = config;
        this.cl = classLoader;
    }

    public void start()
    {
        if ( status.compareAndSet( false, true ) )
        {
            LOGGER.info( "Start" );

            for ( SessionManagerConfig smConfig : config.getSessionManagers() )
            {
                AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration( smConfig );

                configs.add( annotationConfiguration );

                register( configs );
            }
        }
    }

    private void register( List<AnnotationConfiguration> annotationConfigurations )
    {
        try
        {
            InitialContext ic = new InitialContext();

            try
            {
                ic.lookup( "java:hibersap" );
            }
            catch ( NameNotFoundException e )
            {
                ic.createSubcontext( "java:hibersap" );
            }

            Context context = (Context) ic.lookup( "java:hibersap" );

            ClassLoader old = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader( cl );
            try
            {

                for ( AnnotationConfiguration annotationConfig : annotationConfigurations )
                {

                    context.bind( annotationConfig.getSessionManagerConfig().getName(), annotationConfig
                        .buildSessionManager() );
                }
            }
            finally
            {
                Thread.currentThread().setContextClassLoader( old );
                ic.close();
            }
        }
        catch ( NamingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public void stop()
    {
        if ( status.compareAndSet( true, false ) )
        {
            LOGGER.info( "Stop" );

            unregister( configs );

            configs.clear();
        }
    }

    private void unregister( List<AnnotationConfiguration> annotationConfigurations )
    {
        try
        {
            InitialContext ic = new InitialContext();

            try
            {

                for ( AnnotationConfiguration annotationConfiguration : annotationConfigurations )
                {

                    ic.unbind( "java:hibersap/" + annotationConfiguration.getSessionManagerConfig().getName() );
                }
            }
            finally
            {
                ic.close();
            }
        }
        catch ( NamingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public String getStatus()
    {
        return status.get() ? "STARTED" : "STOPPED";
    }

    public String viewConfiguration()
    {
        StringBuilder builder = new StringBuilder();

        for ( SessionManagerConfig smConfig : config.getSessionManagers() )
        {
            builder.append( smConfig.toString() );
        }

        return builder.toString();
    }
}
