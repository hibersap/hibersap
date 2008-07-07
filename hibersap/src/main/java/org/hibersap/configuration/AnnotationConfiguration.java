package org.hibersap.configuration;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.mapping.AnnotationBapiMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.SessionFactory;
import org.hibersap.session.SessionFactoryImpl;

/**
 * Configures Hibersap using annotated BAPI classes. Usually a client creates an
 * instance of this class once and adds BAPI classes using addAnnotatedClass().
 * After calling buildSessionFactory() this instance can be discarded. The
 * SessionFactory will be used to interact with the back-end system. Properties
 * may be overwritten using the methods in this class' superclass, e.g. to
 * specify different SAP systems in a test environment. For each SAP system
 * which will be accessed by the client application, one SessionFactory has to
 * be built.
 * 
 * @author cerker
 */
public class AnnotationConfiguration extends Configuration
{
  private static final Log LOG = LogFactory.getLog(AnnotationConfiguration.class);

  protected AnnotationBapiMapper bapiMapper = new AnnotationBapiMapper();

  private final Set<Class<? extends Object>> annotatedClasses = new HashSet<Class<? extends Object>>();

  /**
   * Adds an annotated BAPI class to the Configuration.
   * 
   * @param bapiClass
   */
  public void addAnnotatedClass(Class<?> bapiClass)
  {
    annotatedClasses.add(bapiClass);
  }

  /**
   * Builds a SessionFactory object. Provide properties and add BAPI classes
   * before calling this method.
   * 
   * @return The SessionFactory
   */
  public SessionFactory buildSessionFactory()
  {
    bapiMappingForClass.clear();
    for (Class<? extends Object> clazz : annotatedClasses)
    {
      LOG.info("Mapping class " + clazz.getName());
      BapiMapping bapiMapping = bapiMapper.mapBapi(clazz);
      bapiMappingForClass.put(clazz, bapiMapping);
    }
    return new SessionFactoryImpl(this, buildSettings(properties));
  }
}
