package org.hibersap.jboss4.deployer;

import org.jboss.deployment.SubDeployerExtMBean;
import org.jboss.deployment.SubDeployerMBean;
import org.jboss.system.ServiceMBean;

public interface HibersapDeployerMBean extends ServiceMBean, SubDeployerExtMBean, SubDeployerMBean{

}
