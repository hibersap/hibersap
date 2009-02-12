package org.hibersap.jbossdeployer;

import java.lang.annotation.Annotation;

import org.hibersap.jbossdeployer.metadata.HiberSapMetaData;
import org.hibersap.jbossdeployer.metadata.SessionManagerMetaData;
import org.hibersap.jmx.HiberSap;
import org.hibersap.jmx.HiberSapMBean;
import org.jboss.aop.microcontainer.aspects.jmx.JMX;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.helpers.AbstractSimpleRealDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.logging.Logger;
import org.jboss.util.id.GUID;
import org.jboss.virtual.VirtualFile;

public class HiberSapRealDeployer
    extends AbstractSimpleRealDeployer<HiberSapMetaData>
{

    private static final Logger LOG = Logger.getLogger( HiberSapRealDeployer.class );

    private boolean scanFromTop;

    public HiberSapRealDeployer()
    {
        super( HiberSapMetaData.class );
        setOutput( BeanMetaData.class );
        LOG.trace( "Created HiberSap deployer." );
    }

    @Override
    public void deploy( final DeploymentUnit unit, final HiberSapMetaData metaData )
        throws DeploymentException
    {
        LOG.trace( "deploy(" + unit + " , " + metaData + ")" );
        if ( ( unit instanceof VFSDeploymentUnit ) == false )
        {
            LOG.error( "I can only deploy VFSDeploymentUnits. I abort deployment." );
            return;
        }

        final SessionManagerMetaData sessionManagerMetaData = metaData.getSessionManager();
        if ( sessionManagerMetaData == null )
        {
            LOG.error( "I found no metadata. This means the parsing phase did not succedd. I abort deployment." );
            return;
        }

        VFSDeploymentUnit vfsUnit = (VFSDeploymentUnit) unit;
        if ( scanFromTop )
        {
            vfsUnit = vfsUnit.getTopLevel();
        }
        final VirtualFile root = vfsUnit.getRoot();

        final BeanMetaData beanMetaData = createBuilderForHiberSapJmxBean( root, sessionManagerMetaData );

        vfsUnit.addAttachment( BeanMetaData.class + "$HiberSap", beanMetaData );

        log.debug( "Created HiberSap bean: " + beanMetaData );
    }

    private BeanMetaData createBuilderForHiberSapJmxBean( final VirtualFile root,
                                                          final SessionManagerMetaData sessionManagerMetaData )
    {
        final BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder( ( GUID.asString() + "$HiberSap" ),
                                                                               HiberSap.class.getName() );

        final JMX jmxAnnotation = new JMX()
        {
            public Class<? extends Annotation> annotationType()
            {
                return JMX.class;
            }

            public Class<?> exposedInterface()
            {
                return HiberSapMBean.class;
            }

            public String name()
            {
                // TODO: create a unique name
                return "hibersap:name=myHiberSapChangeMePLEASE";
            }

            public boolean registerDirectly()
            {
                return true;
            }
        };
        builder.addAnnotation( jmxAnnotation );

        if ( root != null )
        {
            builder.addConstructorParameter( VirtualFile.class.getName(), root );
        }
        builder.addPropertyMetaData( "sessionManagerName", sessionManagerMetaData.getName() );

        final BeanMetaData result = builder.getBeanMetaData();
        LOG.trace( "created builder " + result );

        final BeanMetaData beanMetaData = result;
        return beanMetaData;
    }

    /**
     * Do we scan for mapping from the top, or from this deployment unit.
     * 
     * @param scanFromTop true if we're scanning from the top
     */
    public void setScanFromTop( final boolean scanFromTop )
    {
        this.scanFromTop = scanFromTop;
    }
}
