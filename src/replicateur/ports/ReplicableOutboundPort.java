package replicateur.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import replicateur.interfaces.ReplicableCI;

// -----------------------------------------------------------------------------
/**
 * The class <code>ReplicableOutboundPort</code> implements an outbound port for
 * the <code>ReplicableCI</code> component interface.
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
public class			ReplicableOutboundPort<T>
extends		AbstractOutboundPort
implements	ReplicableCI<T>
{
	private static final long serialVersionUID = 1L;

	public				ReplicableOutboundPort(ComponentI owner)
	throws Exception
	{
		super(ReplicableCI.class, owner) ;
	}

	public				ReplicableOutboundPort(String uri, ComponentI owner)
	throws Exception
	{
		super(uri, ReplicableCI.class, owner) ;
	}

	protected			ReplicableOutboundPort(
		Class<?> implementedInterface,
		ComponentI owner)
	throws Exception
	{
		super(implementedInterface, owner) ;
		assert	ReplicableCI.class.isAssignableFrom(implementedInterface) ;
	}

	public				ReplicableOutboundPort(
		String uri,
		Class<?> implementedInterface,
		ComponentI owner
		) throws Exception
	{
		super(uri, implementedInterface, owner) ;
		assert	ReplicableCI.class.isAssignableFrom(implementedInterface) ;
	}

	/**
	 * @see fr.sorbonne_u.alasca.replication.interfaces.ReplicaI#call(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T			call(Object... parameters) throws Exception
	{
		return ((ReplicableCI<T>)this.connector).call(parameters) ;
	}
}
// -----------------------------------------------------------------------------
