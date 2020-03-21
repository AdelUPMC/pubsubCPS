package gestMessages.plugins;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import gestMessages.CVM;
import gestMessages.connectors.ManagementConnector;
import gestMessages.interfaces.ManagementCI;
import gestMessages.ports.ManagementOutboundPort;
import messages.MessageFilterI;

public class PubSubManagementPlugin extends AbstractPlugin {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** the inbound port which calls will be on this plug-in.*/
    protected ManagementOutboundPort mobp;

    public void installOn(ComponentI owner) throws Exception
    {
        super.installOn(owner);
        this.addRequiredInterface(ManagementCI.class);
        this.mobp = new ManagementOutboundPort(this.owner);
        this.mobp.publishPort();
    }

    @Override
    public void			uninstall() throws Exception
    {
        this.mobp.unpublishPort() ;
        this.mobp.destroyPort() ;
        this.removeRequiredInterface(ManagementCI.class); ;
    }


    public void initialise() throws Exception
    {
        this.addRequiredInterface(ReflectionI.class) ;
        ReflectionOutboundPort robp = new ReflectionOutboundPort(this.owner) ;
        robp.publishPort() ;

        this.owner.doPortConnection(
                robp.getPortURI(),
                CVM.BROKER_COMPONENT_URI,
                ReflectionConnector.class.getCanonicalName()) ;

        String[] uris = robp.findPortURIsFromInterface(ManagementCI.class) ;
        assert	uris != null && uris.length == 1 ;

        this.owner.doPortDisconnection(robp.getPortURI()) ;
        robp.unpublishPort() ;
        robp.destroyPort() ;
        this.removeRequiredInterface(ReflectionI.class) ;

        // connect the outbound port.
        this.owner.doPortConnection(
                this.mobp.getPortURI(),
                uris[0],
                ManagementConnector.class.getCanonicalName()) ;

        super.initialise();

    }

    public void finalise() throws Exception
    {
        this.owner.doPortDisconnection(this.mobp.getPortURI()) ;
    }

    
    public void subscribe(String topic, String inboundPortURI)throws Exception {
        this.mobp.subscribe(topic, inboundPortURI);

    }

    
    public void subscribe(String[] topics, String inboundPortURI)throws Exception {
        this.mobp.subscribe(topics, inboundPortURI);

    }

    
    public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
        this.mobp.subscribe(topic,filter, inboundPortURI);

    }

    
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception{
        this.mobp.modifyFilter(topic, newFilter, inboundPortURI);

    }

   
    public void unsubscribe(String topic, String inboundPortUri) throws Exception {
        this.mobp.unsubscribe(topic, inboundPortUri);
    }

    
    public void createTopic(String topic)throws Exception {
        this.mobp.createTopic(topic);
    }

    
    public void createTopics(String[] topics) throws Exception{
        this.mobp.createTopics(topics);
    }

    
    public void destroyTopic(String topic)throws Exception {
        this.mobp.destroyTopic(topic);
    }

    public boolean isTopic(String topic) throws Exception{
        return this.mobp.isTopic(topic);
    }

    
    public String[] getTopics() throws Exception{
        return this.mobp.getTopics();
    }

   
    public String getPublicationPortURI() throws Exception{
        return this.mobp.getPublicationPortURI();
    }

}
