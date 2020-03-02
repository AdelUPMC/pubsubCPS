package gestMessages.interfaces;

public interface ManagementImplementationI{
	void createTopic(String topic) throws Exception;
	void createTopics(String[] topics)throws Exception;
	void destroyTopic(String topic)throws Exception;
	boolean isTopic(String topic)throws Exception;
	String[] getTopics()throws Exception;
	
		
	
}
