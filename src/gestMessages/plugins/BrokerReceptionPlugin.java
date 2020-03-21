package gestMessages.plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import gestMessages.connectors.PublicationConnector;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.ports.ReceptionOutboundPort;
import messages.MessageI;

public class BrokerReceptionPlugin extends AbstractPlugin {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ReceptionOutboundPort robp;

    public void installOn(ComponentI owner) throws Exception
    {
        super.installOn(owner);
        this.addRequiredInterface(ReceptionCI.class);
        this.robp = new ReceptionOutboundPort(this.owner);
        this.robp.publishPort();
    }

    public void initialise() throws Exception
    {
  
        this.addRequiredInterface(ReflectionI.class) ;
        ReflectionOutboundPort reflection_obp = new ReflectionOutboundPort(this.owner) ;
        reflection_obp.publishPort() ;

        String[] uris = reflection_obp.findPortURIsFromInterface(ReceptionCI.class) ;
        assert	uris != null && uris.length == 1 ;

        this.owner.doPortDisconnection(reflection_obp.getPortURI()) ;
        reflection_obp.unpublishPort() ;
        reflection_obp.destroyPort() ;
        this.removeRequiredInterface(ReflectionI.class) ;
        
        //connect the outbound port
        this.owner.doPortConnection(
                this.robp.getPortURI(),
                uris[0],
                PublicationConnector.class.getCanonicalName()) ;

        super.initialise();

    }
    public void finalise() throws Exception
    {
        this.owner.doPortDisconnection(this.robp.getPortURI()) ;
    }
    public void unistall() throws Exception
    {
        this.robp.unpublishPort() ;
        this.robp.destroyPort() ;
        this.removeRequiredInterface(ReceptionCI.class) ;
    }

    public void acceptMessage(MessageI m) throws Exception {
        this.robp.acceptMessage(m);
    }

    public void acceptMessages(MessageI[] ms) throws Exception {
        this.robp.acceptMessages(ms);
    }
	

}
