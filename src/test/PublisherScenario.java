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
		MessageI m = new Message("message1");
		MessageI m2 = new Message("message2");
		MessageI m3 = new Message("message3");
		
		pub.publish(m, new String[] {"A","B","C"});
		Thread.sleep(2000); // attend que le sub se desabonne
		pub.publish(m2, "B");
		pub.publish(m3, new String[] {"A","B","C"});
		pub.destroyTopic("A");
		pub.destroyTopic("B");
		pub.destroyTopic("C");
		pub.logMessage("fin scenario basique1");
		
	}

	public static void testBasic2(Publisher pub) throws Exception
	{

		if (pub.getPublisherId() > 2)
			return;
		MessageI m = new Message("message1");
		MessageI m2 = new Message("message2");
		m2.getProperties().putProp("size", 8);
		
		Thread.sleep(100); // attend que le sub s'abonne
		if (pub.getPublisherId() == 1)
			pub.publish(m, new String[] {"A","B","C"});
		else
			pub.publish(m2, new String[] {"A","B","C"});
		pub.logMessage("fin scenario basique2");
	}

	
	public static void testCompletPublisher(Publisher pub) throws Exception
	{
		if (pub.getPublisherId() > 1)
			return;
		
		
		pub.logMessage("fin scenario TestCompletPublisher");
	}
}
