package gestMessages.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;
import messages.MessageFilterI;


public interface SubscriptionImplementationI extends OfferedI, RequiredI{
	void subscribe(String topic, String inboundPortUri) throws Exception;
	void subscribe(String[] topics, String inboundPortUri)throws Exception;
	void subscribe(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception;
	void modifyFilter(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception;
	void unsubscribe(String topic,String inboundPortUri)throws Exception;


}
