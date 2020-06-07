package test;


import exceptions.InexistentPropertyException;
import gestMessages.components.Subscriber;
import messages.MessageFilterI;
import messages.MessageI;
import messages.Properties;

public class SubscriberScenario {

	private static final int SIZECHAOS = 10000;

	public static void testCompletTopicSubcribe(Subscriber sub)
	{
		if (sub.getSubscriberId() == 1)
			testCompletTopicSubcribeFirst(sub);
	}
	
	
	public static void testBasic1(Subscriber sub) throws Exception
	{
		if (sub.getSubscriberId() > 1)
			return;
		
		sub.createTopic("A");
		sub.createTopic("B");	
		sub.subscribe("A", sub.getReceptionPlugin().receptionInboundPortURI);
		sub.subscribe("B", sub.getReceptionPlugin().receptionInboundPortURI);
		
		Thread.sleep(10); // attend un message
		sub.unsubscribe("B", sub.getReceptionPlugin().receptionInboundPortURI);
	}
	
		class FiltreSize implements MessageFilterI{
		String name;
		
		public FiltreSize(String name) {
			this.name = name;
					
		}
		

		public boolean filter(MessageI m)  {
			Properties p = m.getProperties();
			Integer res = null;
			try {
				res = p.getIntProp("size");
			} catch (InexistentPropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return res > 5;
		}
	}	
	
	public static void testBasic2(Subscriber sub) throws Exception
	{
		if (sub.getSubscriberId() > 2)
			return;
		MessageFilterI f = new MessageFilterI() {
			public boolean filter(MessageI m) {
				Properties p = m.getProperties();
				Integer res = null;
				try {
					res = p.getIntProp("size");
				} catch (InexistentPropertyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return res > 5;
			}
		};
		if (sub.getSubscriberId() == 1)
		{
		sub.createTopic("A");
		sub.createTopic("C");	
		sub.subscribe("A", sub.getReceptionPlugin().receptionInboundPortURI);
		sub.subscribe("B", sub.getReceptionPlugin().receptionInboundPortURI);
		}
		else
			sub.subscribe("C", f, sub.getReceptionPlugin().receptionInboundPortURI);
	}
	
	public static void testNormal1(Subscriber sub) throws Exception
	{
		if (sub.getSubscriberId() > 4)
			return;
		MessageFilterI f = new MessageFilterI() {
			public boolean filter(MessageI m) {
				Properties p = m.getProperties();
				Integer res = null;
				try {
					res = p.getIntProp("size");
				} catch (InexistentPropertyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return res > 5;
			}
		};
		if (sub.getSubscriberId() == 1)
		{
		sub.createTopic("A");
		sub.createTopic("C");	
		sub.subscribe("A", sub.getReceptionPlugin().receptionInboundPortURI);
		sub.subscribe("B", sub.getReceptionPlugin().receptionInboundPortURI);
		}
		else
			sub.subscribe("C", f, sub.getReceptionPlugin().receptionInboundPortURI);
	}
	
	public static void testNormal2(Subscriber sub) throws Exception
	{
		System.out.println("[SubsrciberScenario:TestNormal2]");
		if (sub.getSubscriberId() > 4)
			return;
		testSmallChaos(sub);
		return;
		/*
		 * MessageFilterI f = new MessageFilterI() {
			public boolean filter(MessageI m) {
				Properties p = m.getProperties();
				Integer res = p.getIntProp("size");
				return res > 5;
			}
		};
		if (sub.getSubscriberId() == 1)
		{
		sub.createTopic("A");
		sub.createTopic("C");	
		sub.subscribe("A", sub.getReceptionPlugin().receptionInboundPortURI);
		sub.subscribe("B", sub.getReceptionPlugin().receptionInboundPortURI);
		}
		else
			sub.subscribe("C", f, sub.getReceptionPlugin().receptionInboundPortURI);
			*/
	}
	
	public static void testSmallChaos(Subscriber sub) throws Exception
	{
		System.out.println("[SubsrciberScenario:TestSmallChaos] start");
		String[] Topics = {"fruits", "legume", "pays", "villes", "continents"};
		String[] subTopic;
		int rand1, randSleep;
		
		MessageFilterI f = new MessageFilterI()
		{
			public boolean filter(MessageI m)
			{
				Properties p = m.getProperties();
				Integer res = null;
				try {
					res = p.getIntProp("randInt");
				} catch (InexistentPropertyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return res < (Math.random() * 42) || true;
			}
		};
		MessageFilterI f2 = new MessageFilterI()
		{
			public boolean filter(MessageI m)
			{
				Properties p = m.getProperties();
				Integer res = null;
				try {
					res = p.getIntProp("randInt");
				} catch (InexistentPropertyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return res % 2 == 0 || true;
			}
		};
		
		for (int i = 0; i < SIZECHAOS; i++)
		{
			rand1 = (int)(Math.random() * 8);
			randSleep = (int)(Math.random() * 100) + 1;
			subTopic = PublisherScenario.createTopics(Topics);
			
			if (rand1 == 0)
			{
				if (subTopic.length == 1)
					sub.createTopic(subTopic[0]);
				else
					sub.createTopics(subTopic);
			}
			else if (rand1 == 1)
			{
				rand1 = (int)(Math.random() * 5);
				sub.destroyTopic(Topics[rand1]);
			}
			else if (rand1 == 2)
				sub.unsubscribe(Topics[(int)(Math.random() * Topics.length)], sub.getReceptionPlugin().receptionInboundPortURI);
			else if (rand1 == 3)
				sub.modifyFilter(Topics[(int)(Math.random() * Topics.length)], f2, sub.getReceptionPlugin().receptionInboundPortURI);
			else
			{
				if (Math.random() > 0.3)
				{
					if (subTopic.length == 1)
						sub.subscribe(subTopic[0], sub.getReceptionPlugin().receptionInboundPortURI);
					else
						sub.subscribe(subTopic, sub.getReceptionPlugin().receptionInboundPortURI);
				}
				else
					sub.subscribe(subTopic[0], f, sub.getReceptionPlugin().receptionInboundPortURI);									
			}
			Thread.sleep(randSleep);
		}
	}
		
	
	private static void testCompletTopicSubcribeFirst(Subscriber sub)
	{
		String s = "testSubsciber";
		String s2 = "testSubscibe2";
		String s3 = "testSubscibe3";
		String s4 = "testSubscibe4";
		try {
			sub.logMessage("[testSubscribe] recupere les topics etant deja creer");
			String []allOldTopics = sub.getTopics();
			sub.logMessage("[testSubscribe] supprimme tous les topics deja existants");
			for (String string : allOldTopics) {
				sub.destroyTopic(string);
			}
			
			assert !sub.isTopic(s);
			sub.logMessage("[testSubscribe] detruit topic non existant");
			sub.destroyTopic(s); 
			sub.logMessage("[testSubscribe] crée topic non existant");
			sub.createTopic(s);
			sub.logMessage("[testSubscribe] crée topic  existant");
			sub.createTopic(s);
			sub.logMessage("[testSubscribe] detruit topic  existant");
			sub.destroyTopic(s); // assure que ce topic n'existe pas
			assert !sub.isTopic(s);
			
			sub.logMessage("[testSubscribe] abonnement a un topic non existant (doit le creer)");
			sub.subscribe(s, sub.getReceptionPlugin().receptionInboundPortURI);
			sub.logMessage("[testSubscribe] desabonnement a un topic où il est deja abonner");
			sub.unsubscribe(s, sub.getReceptionPlugin().receptionInboundPortURI);
			sub.logMessage("[testSubscribe] abonnement a un topic existant");
			sub.subscribe(s, sub.getReceptionPlugin().receptionInboundPortURI);
			sub.logMessage("[testSubscribe] abonnement a un topic ou il est deja abonner");
			sub.subscribe(s, sub.getReceptionPlugin().receptionInboundPortURI);
			sub.logMessage("[testSubscribe] desabonnement a un topic où il n'est pas abonner");
			sub.unsubscribe(s2, sub.getReceptionPlugin().receptionInboundPortURI);
			sub.logMessage("[testSubscribe] desabonnement a un topic où il est abonner");
			sub.unsubscribe(s, sub.getReceptionPlugin().receptionInboundPortURI);
			
			sub.logMessage("[testSubscribe] creation de 3 topics (dont le premier deja existant)");
			sub.createTopics(new String[] {s,s2,s3});
			
			
			sub.logMessage("[testSubscribe] abonnement 4 topics dont 1 non existant");
			sub.subscribe(new String[] {s, s2,s3, s4},sub.getReceptionPlugin().receptionInboundPortURI);
			sub.logMessage("[testSubscribe] verification si le 4eme a bien ete creer");
			assert sub.isTopic(s4);
			sub.logMessage("[testSubscribe] Tout les test sont terminer");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
