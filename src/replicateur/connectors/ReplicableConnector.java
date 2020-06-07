package replicateur.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import replicateur.interfaces.ReplicableCI;

// -----------------------------------------------------------------------------
/**
 * The class <code>ReplicableConnector</code> implements a connector for the
 * <code>ReplicableCI</code> component interface.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-02-28</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			ReplicableConnector<T>
extends		AbstractConnector
implements	ReplicableCI<T>
{
	/**
	 * @see fr.sorbonne_u.alasca.replication.interfaces.ReplicaI#call(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T			call(Object... parameters) throws Exception
	{
		return ((ReplicableCI<T>)this.offering).call(parameters) ;
	}
}
// -----------------------------------------------------------------------------
