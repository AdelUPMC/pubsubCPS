package gestMessages.plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gestMessages.interfaces.ManagementCI;
import gestMessages.ports.ManagementInboundPortForPlugin;
import messages.MessageFilterI;

public class BrokerManagementPlugin extends AbstractPlugin implements ManagementCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ManagementInboundPortForPlugin mibp ;

	 @Override
	    public void			installOn(ComponentI owner) throws Exception
	    {
	        super.installOn(owner) ;
	        this.addOfferedInterface(ManagementCI.class) ;
	        this.mibp = new ManagementInboundPortForPlugin(
	                this.getPluginURI(), this.owner) ;
	        this.mibp.publishPort() ;
	    }

	    @Override
	    public void			uninstall() throws Exception
	    {
	        this.mibp.unpublishPort() ;
	        this.mibp.destroyPort() ;
	        this.removeOfferedInterface(ManagementCI.class) ;

	    }

	    @Override
	    public void subscribe(String topic, String inboundPortURI) throws Exception {
	    	((ManagementCI)this.owner).subscribe(topic, inboundPortURI);
	    }

	    @Override
	    public void subscribe(String[] topics, String inboundPortURI) throws Exception {
	    	((ManagementCI)this.owner).subscribe(topics, inboundPortURI);
	    }

	    @Override
	    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
	    	((ManagementCI)this.owner).subscribe(topic, filter, inboundPortURI);
	    }

	    @Override
	    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
	    	((ManagementCI)this.owner).modifyFilter(topic, newFilter, inboundPortURI);
	    }

	    @Override
	    public void unsubscribe(String topic, String inboundPortUri) throws Exception {
	    	((ManagementCI)this.owner).unsubscribe(topic ,inboundPortUri);
	    }
	    
	    @Override
	    public void createTopic(String topic) throws Exception {
	    	((ManagementCI)this.owner).createTopic(topic);
	    }

	    @Override
	    public void createTopics(String[] topics) throws Exception {
	    	((ManagementCI)this.owner).createTopics(topics);
	    }

	    @Override
	    public void destroyTopic(String topic) throws Exception {
	    	((ManagementCI)this.owner).destroyTopic(topic);
	    }

	    @Override
	    public boolean isTopic(String topic) throws Exception {
	        return   ((ManagementCI)this.owner).isTopic(topic);
	    }

	    @Override
	    public String[] getTopics() throws Exception {
	        return   ((ManagementCI)this.owner).getTopics();
	    }

	    @Override
	    public String getPublicationPortURI() throws Exception {
	        return   ((ManagementCI)this.owner).getPublicationPortURI();
	    }

	    

}
