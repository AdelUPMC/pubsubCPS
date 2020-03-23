package test;

import gestMessages.components.Publisher;
import messages.Message;
import messages.MessageI;

public class PublisherScenario {

	public static void testBasic1(Publisher pub) throws Exception
	{

		if (pub.getPublisherId() > 1)
			return;
		Thread.sleep(1000); // attend que le sub s'abonne
		MessageI m = new Message("Basic message");
		pub.publish(m, "Basic");
	}
	
	
}
