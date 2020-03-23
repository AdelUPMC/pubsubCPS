package gestMessages.plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gestMessages.interfaces.PublicationCI;
import gestMessages.ports.PublicationInboundPortForPlugin;
import messages.MessageI;

public class BrokerPublicationPlugin extends AbstractPlugin implements PublicationCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected PublicationInboundPortForPlugin pibp ;

    @Override
    public void			installOn(ComponentI owner) throws Exception
    {
        super.installOn(owner) ;
        this.addOfferedInterface(PublicationCI.class) ;
        this.pibp = new PublicationInboundPortForPlugin(this.getPluginURI(), this.owner) ;
        this.pibp.publishPort();
    }

  
    @Override
    public void			uninstall() throws Exception
    {
        this.pibp.unpublishPort();
        this.pibp.destroyPort() ;
        this.removeOfferedInterface(PublicationCI.class) ;
    }
  
    @Override
    public void publish(MessageI m, String topic) throws Exception {
    	((PublicationCI)this.owner).publish(m, topic);
    }

    @Override
    public void publish(MessageI m, String[] topics) throws Exception {
    	((PublicationCI)this.owner).publish(m, topics);
    }

    @Override
    public void publish(MessageI[] ms, String topics) throws Exception {
    	((PublicationCI)this.owner).publish(ms, topics);
    }

    @Override
    public void publish(MessageI[] ms, String[] topics) throws Exception {
    	((PublicationCI)this.owner).publish(ms, topics);
    }

    public PublicationInboundPortForPlugin getPublicationInboundPort() {
        return pibp;
    }

}
