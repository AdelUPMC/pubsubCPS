package gestMessages.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface ManagementImplementationI extends OfferedI,RequiredI{
	void createTopic(String topic);
	void createTopics(String[] topics);
	void destroyTopic(String topic);
	boolean isTopic(String topic);
	String[] getTopics();
	
		
	
}
