package gestMessages.interfaces;

public interface ManagementImplementationI{
	public void createTopic(String topic) throws Exception;
	public void createTopics(String[] topics)throws Exception;
	public void destroyTopic(String topic)throws Exception;
	public boolean isTopic(String topic)throws Exception;
	public String[] getTopics()throws Exception;
	public String getPublicationPortURI()throws Exception;
	
		
	
}
