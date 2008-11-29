package org.hibersap.jmx;

public interface HiberSapMBean {

	void start();

	void stop();

	String getSessionFactoryName();

}