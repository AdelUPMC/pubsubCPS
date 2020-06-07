package replicateur.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import replicateur.interfaces.ReplicaI;
import replicateur.interfaces.ReplicableCI;

// -----------------------------------------------------------------------------
/**
 * The class <code>ReplicableInboundPort</code> implements an inbound port for
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
public class			ReplicableInboundPort<T>
extends		AbstractInboundPort
implements	ReplicableCI<T>
{
	private static final long serialVersionUID = 1L;

	public				ReplicableInboundPort(ComponentI owner)
	throws Exception
	{
		super(ReplicableCI.class, owner) ;
		assert	owner instanceof ReplicaI ;
	}

	public				ReplicableInboundPort(String uri, ComponentI owner)
	throws Exception
	{
		super(uri, ReplicableCI.class, owner) ;
		assert	owner instanceof ReplicaI ;
	}

	public				ReplicableInboundPort(
		Class<?> implementedInterface,
		ComponentI owner
		) throws Exception
	{
		super(implementedInterface, owner) ;
		assert	owner instanceof ReplicaI ;
		assert	ReplicableCI.class.isAssignableFrom(implementedInterface) ;
	}

	public				ReplicableInboundPort(
		String uri,
		Class<?> implementedInterface,
		ComponentI owner
		) throws Exception
	{
		super(uri, implementedInterface, owner) ;
		assert	owner instanceof ReplicaI ;
		assert	ReplicableCI.class.isAssignableFrom(implementedInterface) ;
	}

	/**
	 * @see fr.sorbonne_u.alasca.replication.interfaces.ReplicaI#call(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T			call(Object... parameters) throws Exception
	{
		return this.getOwner().handleRequestSync(
									o -> ((ReplicaI<T>)o)).call(parameters) ;
	}
}
// -----------------------------------------------------------------------------
