package gestMessages.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface ManagementImplementationI extends OfferedI,RequiredI{
	void createTopic(String topic) throws Exception;
	void createTopics(String[] topics)throws Exception;
	void destroyTopic(String topic)throws Exception;
	boolean isTopic(String topic)throws Exception;
	String[] getTopics()throws Exception;
	
		
	
}
