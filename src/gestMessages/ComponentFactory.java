package gestMessages;

import fr.sorbonne_u.components.AbstractComponent;
import gestMessages.components.Publisher;
import gestMessages.components.Subscriber;

public class ComponentFactory {

	private static int numberSubsribers = 1;
	private static int numberPublishers = 1;
	
	protected static String createSubscriber(String managementOBPURI, String scenario) throws Exception
	{
		String uri = "SUBSCRIBERURI-" + numberSubsribers;
		//String receptionIBPuri = "SUBRECEPTIONIBPURI" + numberSubsribers;
		
		numberSubsribers++;
		System.out.println("[factory:creatSubscriber]");
		return 
				AbstractComponent.createComponent(
						Subscriber.class.getCanonicalName(),
						new Object[]{uri,1,0, scenario});	
	}

	protected  static String createPublisher(String publicationoutboundPortURI, String managementoutboundPortURI, String scenario) throws Exception
	{
		String uri = "PUBLISHERURI-" + numberPublishers;
		
		numberPublishers++;
		return 
				AbstractComponent.createComponent(
						Publisher.class.getCanonicalName(),
						new Object[]{1,0, scenario});	
	}

}
