package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class ReceptionInBoundPort extends AbstractInboundPort {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReceptionInBoundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	public ReceptionInBoundPort(String uri, Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(uri, implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

}
