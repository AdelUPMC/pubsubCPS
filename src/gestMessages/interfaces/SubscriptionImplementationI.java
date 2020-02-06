package gestMessages.interfaces;

import messages.MessageFilterI;
import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;


public interface SubscriptionImplementationI extends OfferedI,RequiredI{
	void subscribe(String topic, String inboundPortUri);
	void subscribe(String[] topics, String inboundPortUri);
	void subscribe(String topic,MessageFilterI newFilter, String inboundPortUri);
	void modifyFilter(String topic,MessageFilterI newFilter, String inboundPortUri);
	void unsubscribe(String topic,String inboundPortUri);


}
