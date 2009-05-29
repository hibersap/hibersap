package org.hibersap.jboss4.jmx;

public interface HibersapServiceMBean {

	void start();

	void stop();

	String viewConfiguration();

	String getStatus();

}
