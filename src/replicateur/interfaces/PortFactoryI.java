package replicateur.interfaces;

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

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.InboundPortI;
import fr.sorbonne_u.components.ports.OutboundPortI;

// -----------------------------------------------------------------------------
/**
 * The interface <code>PortFactoryI</code> declares a set of signatures to
 * be implemented by a port factory.
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
public interface		PortFactoryI
{
	/**
	 * create an inbound port for the given component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	c != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param c				object representing the component that will hold the port.
	 * @return				the inbound port.
	 * @throws Exception	<i>to do</i>.
	 */
	public InboundPortI		createInboundPort(ComponentI c) throws Exception ;

	/**
	 * create an inbound port for the given component with the given URI.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code uri != null && c != null}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param uri			URI imposed to the port.
	 * @param c				object representing the component that will hold the port.
	 * @return				the inbound port.
	 * @throws Exception	<i>to do</i>.
	 */
	public InboundPortI		createInboundPort(String uri, ComponentI c)
	throws Exception ;

	/**
	 * create an outbound port for the given component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	c != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param c				object representing the component that will hold the port.
	 * @return				the outbound port.
	 * @throws Exception	<i>to do</i>.
	 */
	public OutboundPortI	createOutboundPort(ComponentI c) throws Exception ;

	/**
	 * create an outbound port for the given component with the given URI.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code uri != null && c != null}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param uri			URI imposed to the port.
	 * @param c				object representing the component that will hold the port.
	 * @return				the ioutbound port.
	 * @throws Exception	<i>to do</i>.
	 */
	public OutboundPortI	createOutboundPort(String uri, ComponentI c)
	throws Exception ;

	/**
	 * return the name of the connector class to be used to connect such ports.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return	the name of the connector class to be used to connect such ports.
	 */
	public String			getConnectorClassName() ;
}
// -----------------------------------------------------------------------------
