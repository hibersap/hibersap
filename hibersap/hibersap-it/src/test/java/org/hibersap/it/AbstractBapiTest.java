package org.hibersap.it;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.After;
import org.junit.Before;

public class AbstractBapiTest
{
    protected Session session;
    protected SessionManager sessionManager;

    @Before
    public void openSession() throws Exception
    {
        sessionManager = new AnnotationConfiguration( "A12" ).buildSessionManager();
        session = sessionManager.openSession();
    }

    @After
    public void closeSession() throws Exception
    {
        if ( session != null )
        {
            session.close();
        }
        if ( sessionManager != null )
        {
            sessionManager.close();
        }
    }
}
