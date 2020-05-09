package org.hibersap.execution.neo;

import java.util.Map;

import com.sap.conn.jco.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.execution.Connection;
import org.hibersap.execution.jco.JCoMapper;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.Credentials;
import org.hibersap.session.SessionImplementor;
import org.hibersap.session.Transaction;


/**
 *  Connection for SAP Cloud Runtime Neo.
 *  Since connection details are configured within SCP only use a Destination Name
 *
 *  @author Ludger Pottmeier
 */
public class NeoConnection implements Connection {
	private static final Log LOG = LogFactory.getLog(NeoConnection.class);

	private final String destinationName;
	private final NeoJCoMapper jcoMapper;

	public NeoConnection(String destinationName) {
		this.destinationName = destinationName;
		this.jcoMapper = new NeoJCoMapper();
	}

	/**
	 * @deprecated : Not needed for Neo
	 * @param credentials The Credentials
	 */
	@Override
	public void setCredentials(Credentials credentials) {
		// not needed
	}

	@Override
	public Transaction beginTransaction(SessionImplementor session) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Transaction getTransaction() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void execute(final BapiMapping bapiMapping, final Map<String, Object> functionMap){
		this.execute(bapiMapping.getBapiName(),functionMap);
    }



	public void execute(String bapiName, Map<String, Object> functionMap) {
		JCoDestination destination;
		JCoRepository repo;
		JCoFunction function;
		
		try {
			destination = JCoDestinationManager.getDestination(this.destinationName);
			repo = destination.getRepository();
			function = repo.getFunction(bapiName);
			jcoMapper.putFunctionMapValuesToFunction(function, functionMap);
			function.execute(destination);
			jcoMapper.putFunctionValuesToFunctionMap(function, functionMap);
		} 
		catch (JCoException e) {

			throw new HibersapException("Error executing function module " + bapiName, e);
		}
	}

	@Override
	public void close() {
	}
}
