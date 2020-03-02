package gestMessages.interfaces;

import messages.MessageFilterI;


public interface SubscriptionImplementationI{
	void subscribe(String topic, String inboundPortUri) throws Exception;
	void subscribe(String[] topics, String inboundPortUri)throws Exception;
	void subscribe(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception;
	void modifyFilter(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception;
	void unsubscribe(String topic,String inboundPortUri)throws Exception;


}
