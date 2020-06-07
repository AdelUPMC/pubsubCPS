package replicateur.components;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// basic component programming model to program with components
// distributed applications in the Java programming language.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.InboundPortI;
import fr.sorbonne_u.components.ports.OutboundPortI;
import replicateur.interfaces.CombinatorI;
import replicateur.interfaces.PortFactoryI;
import replicateur.interfaces.ReplicaI;
import replicateur.interfaces.ReplicableCI;
import replicateur.interfaces.SelectorI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// -----------------------------------------------------------------------------
/**
 * The class <code>ReplicationManager</code> implements a generic component
 * to represent several replication patterns in a distributed application:
 * dispatchers, fault-tolerant or error-tolerant, etc. replication.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-02-27</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
// -----------------------------------------------------------------------------
@OfferedInterfaces(offered = {ReplicableCI.class})
@RequiredInterfaces(required = {ReplicableCI.class})
// -----------------------------------------------------------------------------
public class			ReplicationManager<T>
extends		AbstractComponent
implements	ReplicaI<T>
{
	/** inbound port through which clients call this component.				*/
	protected InboundPortI		inboundPort ;
	/** outbound ports through which this component calls servers.			*/
	protected OutboundPortI[]	outboundPorts ;
	/** a function mapping outbound ports to unique numbers.				*/
	protected Map<OutboundPortI,Integer>	numbers ;
	/** a function that selects outbound ports among the available ones.	*/
	protected SelectorI			selector ;
	/** a function that combines results from servers to give one result
	 *  returned to the caller.												*/
	protected CombinatorI<T>	combinator ;
	/** a port factory to create inbound and outbound ports.				*/
	protected PortFactoryI		portCreator ;
	/** URIs of the inbound ports of the server to connect this component.	*/
	protected String[]			serverInboundPortURIs ;

	/**
	 * create a replication manager.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code nbThreads > 0}
	 * pre	{@code ownInboundPortURI != null}
	 * pre	{@code selector != null && ThreadSafe(selector)}
	 * pre	{@code combinator != null && ThreadSafe(combinator)}
	 * pre	{@code portCreator != null}
	 * pre	{@code serverInboundPortURIs != null && serverInboundPortURIs.length > 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param nbThreads				number of threads used to execute the client calls.
	 * @param ownInboundPortURI		URI of the inbound port of this component.
	 * @param selector				a function that selects outbound ports among the available ones.
	 * @param combinator			a function that combines results from servers to give one result returned to the caller.
	 * @param portCreator			a port factory to create inbound and outbound ports.
	 * @param serverInboundPortURIs	URIs of the inbound ports of the server to connect this component.
	 * @throws Exception			<i>to do</i>.
	 */
	protected			ReplicationManager(
		int nbThreads,
		String ownInboundPortURI,
		SelectorI selector,
		CombinatorI<T> combinator,
		PortFactoryI portCreator,
		String[] serverInboundPortURIs
		) throws Exception
	{
		super(nbThreads, 0) ;

		assert	nbThreads > 0 ;
		assert	ownInboundPortURI != null ;
		assert	selector != null ;
		assert	combinator != null ;
		assert	portCreator != null ;
		assert	serverInboundPortURIs != null &&
											serverInboundPortURIs.length > 0 ;

		this.initialise(ownInboundPortURI, selector, combinator,
						portCreator, serverInboundPortURIs) ;
	}

	/**
	 * create a replication manager.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code nbThreads > 0}
	 * pre	{@code ownInboundPortURI != null}
	 * pre	{@code selector != null && ThreadSafe(selector)}
	 * pre	{@code combinator != null && ThreadSafe(combinator)}
	 * pre	{@code portCreator != null}
	 * pre	{@code serverInboundPortURIs != null && serverInboundPortURIs.length > 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param reflectionInboundPortURI	URI of the reflection inbound port of this component.
	 * @param nbThreads					number of threads used to execute the client calls.
	 * @param ownInboundPortURI			URI of the inbound port of this component.
	 * @param selector					a function that selects outbound ports among the available ones.
	 * @param combinator				a function that combines results from servers to give one result returned to the caller.
	 * @param portCreator				a port factory to create inbound and outbound ports.
	 * @param serverInboundPortURIs		URIs of the inbound ports of the server to connect this component.
	 * @throws Exception				<i>to do</i>.
	 */
	protected			ReplicationManager(
		String reflectionInboundPortURI,
		int nbThreads,
		String ownInboundPortURI,
		SelectorI selector,
		CombinatorI<T> combinator,
		PortFactoryI portCreator,
		String[] serverInboundPortURIs
		) throws Exception
	{
		super(reflectionInboundPortURI, nbThreads, 0);

		assert	nbThreads > 0 ;
		assert	ownInboundPortURI != null ;
		assert	selector != null ;
		assert	combinator != null ;
		assert	portCreator != null ;
		assert	serverInboundPortURIs != null &&
											serverInboundPortURIs.length > 0 ;

		this.initialise(ownInboundPortURI, selector, combinator,
						portCreator, serverInboundPortURIs) ;
	}

	/**
	 * initialise the replication manager.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code ownInboundPortURI != null}
	 * pre	{@code selector != null}
	 * pre	{@code combinator != null}
	 * pre	{@code portCreator != null}
	 * pre	{@code serverInboundPortURIs != null && serverInboundPortURIs.length > 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param ownInboundPortURI			URI of the inbound port of this component.
	 * @param selector					a function that selects outbound ports among the available ones.
	 * @param combinator				a function that combines results from servers to give one result returned to the caller.
	 * @param portCreator				a port factory to create inbound and outbound ports.
	 * @param serverInboundPortURIs		URIs of the inbound ports of the server to connect this component.
	 * @throws Exception				<i>to do</i>.
	 */
	protected void		initialise(
		String ownInboundPortURI,
		SelectorI selector,
		CombinatorI<T> combinator,
		PortFactoryI portCreator,
		String[] serverInboundPortURIs
		) throws Exception
	{
		this.portCreator = portCreator ;
		this.selector = selector ;
		this.combinator = combinator ;

		this.inboundPort =
					portCreator.createInboundPort(ownInboundPortURI, this) ;
		assert	this.inboundPort instanceof ReplicableCI ;
		this.inboundPort.publishPort() ;

		assert	this.inboundPort instanceof ReplicableCI ;
		this.outboundPorts = new OutboundPortI[serverInboundPortURIs.length] ;
		this.numbers = new HashMap<OutboundPortI,Integer>() ;
		for (int i = 0 ; i < serverInboundPortURIs.length ; i++) {
			this.outboundPorts[i] = portCreator.createOutboundPort(this) ;
			this.numbers.put(this.outboundPorts[i], i) ;
			assert	this.outboundPorts[i] instanceof ReplicableCI ;
			this.outboundPorts[i].publishPort() ;
		}
		this.serverInboundPortURIs = serverInboundPortURIs ;
		this.tracer.setTitle("ReplicationManager") ;
		this.tracer.setRelativePosition(1, 0) ;
		this.toggleTracing() ;
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		for (int i = 0 ; i < this.serverInboundPortURIs.length ; i++) {
			try {
				this.doPortConnection(
						this.outboundPorts[i].getPortURI(),
						serverInboundPortURIs[i],
						this.portCreator.getConnectorClassName());
			} catch (Exception e) {
				throw new ComponentStartException(e) ;
			}
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		for (int i = 0 ; i < this.outboundPorts.length ; i++) {
			this.doPortDisconnection(this.outboundPorts[i].getPortURI()) ;
		}
		super.finalise() ;
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		try {
			for (int i = 0 ; i < this.outboundPorts.length ; i++) {
				this.outboundPorts[i].unpublishPort() ;
			}
			this.inboundPort.unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown() ;
	}

	/**
	 * @see fr.sorbonne_u.alasca.replication.interfaces.ReplicaI#call(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T			call(Object... parameters) throws Exception
	{
		OutboundPortI[]	selected = this.selector.select(this.outboundPorts) ;
		String mes = "" ;
		for (int i = 0 ; i < selected.length ; i++) {
			mes += this.numbers.get(selected[i]) + " ," ;
		}
		this.traceMessage(mes + "\n") ;

		ArrayList<T> results = new ArrayList<T>() ;
		Exception raised = null ;
		for (int i = 0 ; i <  selected.length ; i++) {
			try {
				results.add(
					(T)((ReplicableCI<T>)selected[i]).call(parameters)) ;
			} catch(Exception e) {
				raised = e ;
			}
		}
		if (results.size() == 0 && raised != null) {
			throw new Exception(raised) ;
		}
		return this.combinator.combine((T[]) results.toArray()) ;
	}
}
// -----------------------------------------------------------------------------
