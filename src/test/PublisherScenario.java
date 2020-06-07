package test;

import java.util.ArrayList;

import gestMessages.components.Publisher;
import messages.Message;
import messages.MessageI;

public class PublisherScenario {
	
	private static final int SIZECHAOS = 10000;

	public static void testBasic1(Publisher pub) throws Exception
	{

		if (pub.getPublisherId() > 1)
			return;
		Thread.sleep(10); // attend que le sub s'abonne
		MessageI m = new Message("banane");
		MessageI m2 = new Message("message2");
		MessageI m3 = new Message("message3");
		
		pub.publish(m, new String[] {"A","B","C"});
		Thread.sleep(10); // attend que le sub se desabonne
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
		
		Thread.sleep(10); // attend que le sub s'abonne
		if (pub.getPublisherId() == 1)
			pub.publish(m, new String[] {"A","B","C"});
		else
			pub.publish(m2, new String[] {"A","B","C"});
		pub.logMessage("fin scenario basique2");
	}
	
	public static void testNormal1(Publisher pub) throws Exception
	{

		if (pub.getPublisherId() > 2)
			return;
		MessageI[] messages1 = {new Message("1-message1"), new Message("1-message2"), new Message("1-banane"), new Message("1-message4")};
		MessageI[] messages2 = {new Message("2-message1"), new Message("2-message3"), new Message("2-message3"), new Message("2-message4")};
		MessageI[] messages3 = {new Message("3-message1"), new Message("3-message3"), new Message("3-message3"), new Message("3-message4")};
		MessageI[] messages4 = {new Message("4-message1"), new Message("4-message3"), new Message("4-message3"), new Message("4-message4")};
		messages1[1].getProperties().putProp("size", 4);messages1[3].getProperties().putProp("size", 8);
		messages1[0].getProperties().putProp("size", 12);
		Thread.sleep(100); // attend que les subs s'abonnent
		for (int i= 0; i< 4; i++)
		{
			if (pub.getPublisherId() == 1)
			{
				pub.publish(messages1[i], new String[] {"A","B","C"});
			}
			else if (pub.getPublisherId() == 2)
			{
				pub.publish(messages2[i], new String[] {"A","B","C"});
			}else if (pub.getPublisherId() == 3)
			{
				pub.publish(messages3[i], new String[] {"A","B","C"});
			}else 
			{
				pub.publish(messages4[i], new String[] {"A","B","C"});
			}
		}
		pub.logMessage("fin scenario normal1");
	}

	public static void testNormal2(Publisher pub) throws Exception
	{
		System.out.println("[PublisherScenario:testNormal2] start");

		if (pub.getPublisherId() > 4)
			return;
		testSmallChaos(pub);
		return;
		/*
		MessageI[] allMessages1 = {new Message("1-pomme"), new Message("1-peche"), new Message("1-banane"), new Message("1-fraise")};
		MessageI[] allMessages2 = {new Message("2-pomme"), new Message("2-peche"), new Message("2-banane"), new Message("2-fraise")};
		MessageI[] allMessages3 = {new Message("3-pomme"), new Message("3-peche"), new Message("3-banane"), new Message("3-fraise")};
		MessageI[] allMessages4 = {new Message("4-pomme"), new Message("4-peche"), new Message("4-banane"), new Message("4-fraise")};
		allMessages1[1].getProperties().putProp("size", 4);allMessages1[3].getProperties().putProp("size", 8);
		allMessages1[0].getProperties().putProp("size", 12);
		Thread.sleep(100); // attend que les subs s'abonnent
		for (int i= 0; i < 4; i++)
		{
			int tmp = (int)(Math.random() * 4);
			if (pub.getPublisherId() == 1)
			{
				pub.publish(allMessages1[tmp], new String[] {"A","B","C"});
			}
			else if (pub.getPublisherId() == 2)
			{
				pub.publish(allMessages2[tmp], new String[] {"A","B","C"});
			}else if (pub.getPublisherId() == 3)
			{
				pub.publish(allMessages3[tmp], new String[] {"A","B","C"});
			}else 
			{
				pub.publish(allMessages4[tmp], new String[] {"A","B","C"});
			}
		}
		pub.logMessage("fin scenario normal2");
		System.out.println("fin scenario normal2");
	*/}


	 static String[]  createTopics(String []ref)
	{
		int rand = (int)(Math.random() * ref.length) + 1;
		ArrayList<String> liste = new ArrayList<>();
		String []res = new String[rand];
		
		for(int i = 0; i < rand ; i++)
		{
			int tmp = (int)(Math.random() * (ref.length - i));
			int k = 0;
			for (int j = 0; j < ref.length && k < tmp; j++)
			{
				if (!liste.contains(ref[j]))
					k++;
			}
			liste.add(ref[k]);
			res[i] = ref[k];
		} 
		
		
		return res;
	}
	
	private static Message[] CreateMessages(int id,String []reference)
	{
		
		
		ArrayList<Message> messages = new ArrayList<>();
		int size = (int)(Math.random() * reference.length) + 1;
		Message []res = new Message[size];
		Message Msg = null;
		
		for (int i = 0; i < size; i++) {
			int tmp = (int)(Math.random() * (reference.length - i));
			int k = 0;
			for (int j = 0; j < reference.length && k < tmp; j++)
			{
				Msg = new Message(id + reference[j]);
				if (!messages.contains(Msg))
					k++;
			}
			messages.add(Msg);
			res[i] = Msg;
		}
		for (int i = 0; i < size; i++) {
			if (Math.random() * 3 > 2)
				res[i].getProperties().putProp("randInt", (int)(Math.random() * 21));
		}
		return (res);
	}
	
	public static void testSmallChaos(Publisher pub) throws Exception
	{
		System.out.println("[PublisherScenario:testSmallChaos] start");

		String[] Contents = {"-Pomme", "-banane", "-fraise", "-peche", "-poire"};
		String[] Topics = {"fruits", "legume", "pays", "villes", "continents"};
		String[] subTopic;
		Message[] messages;
		int rand1, randSleep = 100;
		
		for (int i = 0; i < SIZECHAOS; i++)
		{
			rand1 = (int)(Math.random() * 5);
			randSleep = (int)(Math.random() * 100) + 1;
			subTopic = createTopics(Topics);
			
			if (rand1 == 0)
			{
				if (subTopic.length == 1)
					pub.createTopic(subTopic[0]);
				else
					pub.createTopics(subTopic);
			}
			else if (rand1 == 1)
			{
				rand1 = (int)(Math.random() * 5);
				pub.destroyTopic(Topics[rand1]);
			}
			else
			{
				messages = CreateMessages(pub.getPublisherId(), Contents);
				rand1 = (int)(Math.random() * 5);
				if (messages.length == 1)
				{
					if (subTopic.length == 1)
						pub.publish(messages[0], subTopic[0]);
					else
						pub.publish(messages[0], subTopic);
				}
				else
				{
					if (subTopic.length == 1)
						pub.publish(messages, subTopic[0]);
					else
						pub.publish(messages, subTopic);
				}					
			}
			Thread.sleep(randSleep);
		}
	}
		
	public static void testCompletPublisher(Publisher pub) throws Exception
	{
		if (pub.getPublisherId() > 1)
			return;
		
		
		pub.logMessage("fin scenario TestCompletPublisher");
		System.out.println("[PublisherScenario:testCompletPublisher] end");

		
	}
}
