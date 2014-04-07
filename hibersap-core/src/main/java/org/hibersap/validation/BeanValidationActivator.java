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

package org.hibersap.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.configuration.xml.ValidationMode;
import org.hibersap.interceptor.BapiInterceptor;
import org.hibersap.mapping.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Acts as a middle-man between Hibersap and the Bean Validation framework.
 * Hibersap applications should not be forced to have the Bean Validation API library on the classpath
 * if they don't use Bean Validation at all. Therefore, this class has no direct dependency to the
 * javax.validation package.
 */
public class BeanValidationActivator
{
    private static final Log LOGGER = LogFactory.getLog( BeanValidationActivator.class );

    private static final String TYPE_SAFE_ACTIVATOR_CLASS_NAME = "org.hibersap.validation.TypeSafeActivator";
    private static final String TYPE_SAFE_ACTIVATOR_METHOD_NAME = "activateBeanValidation";
    private static final String VALIDATION_CLASS_NAME = "javax.validation.Validation";

    /**
     * If Bean Validation should be used, as specified in the Hibersap SessionManagerConfig,
     * the TypeSafeActivator class is used to create the ValidationFactory.
     * This class has no direct dependency on javax.validation, so Hibersap applications are not forced to
     * have any Bean Validation API or implementation libraries on their classpath.
     *
     * @param bapiInterceptors     The BeanValidationInterceptor will be added here if Bean Validation will be used.
     * @param sessionManagerConfig Provides the configuration information to decide if Bean Validation shall be used.
     */
   public static void activateBeanValidation( Set<BapiInterceptor> bapiInterceptors,
                                                SessionManagerConfig sessionManagerConfig )
    {
        ValidationMode validationMode = sessionManagerConfig.getValidationMode();

        if ( validationMode != ValidationMode.NONE )
        {
            if ( shouldActivateBeanValidation( validationMode ) )
            {
                activateBeanValidationWithTypeSafeActivator( bapiInterceptors, sessionManagerConfig );
            }
        }
    }

    private static boolean shouldActivateBeanValidation( ValidationMode validationMode )
    {
        try
        {
            ReflectionHelper.getClassForName( VALIDATION_CLASS_NAME );
            return true;
        }
        catch ( ClassNotFoundException e )
        {
            if ( validationMode == ValidationMode.CALLBACK )
            {
                throw new HibersapException(
                        "Bean Validation is not available in the classpath but required " +
                                "when ValidationMode is CALLBACK", e );
            }
            else if ( validationMode == ValidationMode.AUTO )
            {
                LOGGER.info( "Bean Validation will not be used because " +
                        "class javax.validation.Validation was not found in classpath while ValidationMode is AUTO" );
                return false;
            }
            else
            {
                throw new HibersapException( "This should not ever happen", e );
            }
        }
    }

    private static void activateBeanValidationWithTypeSafeActivator( Set<BapiInterceptor> bapiInterceptors,
                                                                     SessionManagerConfig sessionManagerConfig )
    {
        try
        {
            Class<?> activator = ReflectionHelper.getClassForName( TYPE_SAFE_ACTIVATOR_CLASS_NAME );

            Method activateBeanValidation = activator.getDeclaredMethod( TYPE_SAFE_ACTIVATOR_METHOD_NAME,
                    Set.class, SessionManagerConfig.class );
            activateBeanValidation.invoke( null, bapiInterceptors, sessionManagerConfig );
        }
        catch ( ClassNotFoundException e )
        {
            throw new HibersapException( "Cannot find class " + TYPE_SAFE_ACTIVATOR_CLASS_NAME, e );
        }
        catch ( InvocationTargetException e )
        {
            throw new HibersapException( "Cannot invoke method " + TYPE_SAFE_ACTIVATOR_METHOD_NAME + " in class " +
                    TYPE_SAFE_ACTIVATOR_CLASS_NAME, e );
        }
        catch ( NoSuchMethodException e )
        {
            throw new HibersapException( "Cannot find method " + TYPE_SAFE_ACTIVATOR_METHOD_NAME + " in class " +
                    TYPE_SAFE_ACTIVATOR_CLASS_NAME, e );
        }
        catch ( IllegalAccessException e )
        {
            throw new HibersapException( "Cannot invoke method " + TYPE_SAFE_ACTIVATOR_METHOD_NAME + " in class " +
                    TYPE_SAFE_ACTIVATOR_CLASS_NAME, e );
        }
    }
}
