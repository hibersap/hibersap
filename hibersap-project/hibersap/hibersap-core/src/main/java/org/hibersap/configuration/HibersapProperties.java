package org.hibersap.configuration;

/*
 * Defines the keys of all Hibersap properties. The properties may be directly defined in the
 * hibersap.properties file, set programmatically using the Configuration.setProperty() oder
 * setProperties() methods, or the can be defined indirectly using the hibersap.xml file.
 * 
 * @author Carsten Erker
 */
public interface HibersapProperties
{

    /*
     * The (unique) SessionFactory name. For each SAP system which is accessed by the application,
     * there must be a SessionFactory instance.
     */
    public static final String SESSION_FACTORY_NAME = "hibersap.session_factory_name";

    /*
     * The prefix for the BAPI classes. Numbers from 0..n are appended. For each SAP function module
     * which is used by the application there must be on BAPI class defined in Hibersap. The BAPI
     * classes must be annotated using the @Bapi annotation.
     */
    public static final String BAPI_CLASSES_PREFIX = "hibersap.bapi_class.";

    /*
     * The Hibersap context class. This property defines which interface technology to use, e.g.
     * org.hibersap.execution.jco.JCoContext for the SAP Java Connector (JCo) or
     * org.hibersap.execution.jca.JCAContext for a JCA compatible resource adapter. Users may
     * provide their own implementations by implementing the org.hibersap.session.Context interface.
     */
    public static final String CONTEXT_CLASS = "hibersap.context_class";

    /*
     * The JNDI name for the JCA ConnectionFactory. When using a JCA compatible resource adapter,
     * the JNDI name of the resource adapter's ConnectionFactory must be defined using this
     * property.
     */
    public static final String JCA_CONNECTION_FACTORY = "hibersap.connection_factory";

}
