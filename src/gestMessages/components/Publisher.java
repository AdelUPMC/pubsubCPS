package gestMessages.components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gestMessages.interfaces.PublicationCI;
import gestMessages.ports.PublicationOutboundPort;
import messages.MessageI;


@RequiredInterfaces(required = {PublicationCI.class})
public class Publisher extends AbstractComponent {
	protected PublicationOutboundPort	portO;

	protected Publisher(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}

	protected Publisher(String uri, String reflectionOutboundPortURI, int nbThreads, int nbSchedulableThreads) throws Exception {
		super(uri, nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
		portO =	new PublicationOutboundPort(reflectionOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.portO.localPublishPort();;
		this.tracer.setTitle("Publisher") ;
		this.tracer.setRelativePosition(1, 1) ;
	
	}

	
	public void testConnection()
	{
		try {	
			this.portO.publish((MessageI)null, "Toute mes felicitations");
		} catch (Exception e) {
			System.out.println("Presque !!");
			e.printStackTrace();
				}
	}
	
	public void			start() throws ComponentStartException
	{
		System.out.println("pub Start ");
		super.start() ;
		this.logMessage("starting publisher component.") ;
		// Schedule the first service method invocation in one second.
		this.scheduleTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						//System.out.println("PREEEEEESQUE");
						//((Publisher)this.getTaskOwner()).testConnection();
					} catch (Exception e) {
					//	System.out.println("pp PREEEEEESQUE");

						throw new RuntimeException(e) ;
					}
				}
			},
			1000, TimeUnit.MILLISECONDS);

		System.out.println("Pub  Start End ");
	}
	
}
