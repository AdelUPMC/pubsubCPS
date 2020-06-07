package gestMessages;

import java.util.ArrayList;

import fr.sorbonne_u.components.AbstractComponent;
import gestMessages.components.Broker;



/*
 * Design pattern singleton car il ne peu avoir que 1 scenario
 */
public class TestScenario {
	public static final String	SCENARIO_BASIC1 = "scenario-basic-1";
	public static final String	SCENARIO_BASIC2 = "scenario-basic-2";
	public static final String	SCENARIO_NORMAL1 = "scenario-normal-1";
	public static final String	SCENARIO_NORMAL = "scenario-normal";
	public static final String	SCENARIO_BIGGER = "scenario-bigger";
	public static final String	SCENARIO_INSANE = "scenario-INSANE";
	public static final String  TestCompletPublisher = "testCompletPublisher-1";
	public static final String	TestCompletTopSub = "TestCompletTopSub-1";

	public static TestScenario myself = null;
	
	private CVM cvm;
	private String brokerURI;
	private ArrayList<String> subscribersURI;
	private ArrayList<String> publishersURI;
	
	/**
	 * Execute l'un des scenario cree a partir d'un identifiant 
	 * execute le premier scenario basique si l'identifiant n'est pas reconnus
	 * 
	 * @param cvm
	 * @param scenario String servant d'identifiant de scenario
	 * @throws Exception
	 */
	private TestScenario(CVM cvm, String scenario) throws Exception
	{
		this.cvm = cvm;
		this.subscribersURI = new ArrayList<>();
		this.publishersURI = new ArrayList<>();
		
		this.brokerURI =
				AbstractComponent.createComponent(Broker.class.getCanonicalName(),
						new Object[]{CVM.BROKER_COMPONENT_URI,CVM.PublicationInboundPortURI,CVM.ManagementInboundPortURI}) ;
		cvm.toggleTracing(this.brokerURI);
		switch (scenario) {
		case SCENARIO_BASIC1:
			execute_scenario_basic1();
			break;
		case SCENARIO_BASIC2:
			execute_scenario_basic2();
			break;
		case SCENARIO_NORMAL1:
			execute_scenario_normal1();
			break;
		case SCENARIO_NORMAL:
			execute_scenario_normal();
			break;
		case SCENARIO_BIGGER:
			execute_scenario_bigger();
			break;
		case SCENARIO_INSANE:
			execute_scenario_insane();
			break;
		case TestCompletTopSub:
			execute_testCompletSubscriber();
			break;
		case TestCompletPublisher:
			execute_testCompletPub();
			break;
		default:
			execute_scenario_basic1();
			break;
		}
	}
	

	/**
	 *	Si n'a jamais été appellé  lance les scenarios
	 * @param cvm
	 * @param scenario
	 * @throws Exception
	 */
	public static synchronized void execute(CVM cvm, String scenario) throws Exception
	{
		if (myself == null)
		{		
			myself = new TestScenario(cvm, scenario);
		}
	}
	
	/**
	 * 	scenario basique 1 
	 *  1 sub et 1 pub
	 *  creation des topics 'A' , 'B', 'C'
	 * 	sub1 abonner  au topic 'A' et 'B'
	 *  pub1 publie un message avec le topic 'A', 'B' et 'C'
	 * 	sub1 ce desabonne du topic 'B'
	 * 	pub1 publie un message avec le topic 'B'
	 *  pub1 publie un message avec le topic 'A', 'B' et 'C'
	 *  destruction des topics 'A' , 'B', 'C'

	 * @throws Exception
	 */
	private void execute_scenario_basic1() throws Exception
	{
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_BASIC1));
		cvm.toggleTracing(this.subscribersURI.get(0));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_BASIC1));
		cvm.toggleTracing(this.publishersURI.get(0));
	}
	
	/**
	 *	Scenario basique 2 
	 * 	 2 sub et 2 pub 
	 * 	creation des topics 'A' et 'C'
	 * 	sub1 abonner  au topic 'A' et 'B' 
	 * 	sub2 abonner au topic 'B' et 'C' (filtre sur la "size")
	 * 	pub1 publie un message avec le topic 'A' , 'B' et 'C' (sans filtre)
	 * 	pub2 publie un message sur 'A' et 'C' avec une propriete size = 10
	 * 
	 * @throws Exception
	 */
	private void execute_scenario_basic2() throws Exception
	{
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_BASIC2));
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_BASIC2));
		
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_BASIC2));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_BASIC2));
		
		cvm.toggleTracing(this.subscribersURI.get(0));
		cvm.toggleTracing(this.subscribersURI.get(1));
		cvm.toggleTracing(this.publishersURI.get(0));
		cvm.toggleTracing(this.publishersURI.get(1));
	}
	
	/**
	 *	Scenario Normal1 
	 * 	 4 sub et 4 pub 
	 * 
	 * @throws Exception
	 */
	private void execute_scenario_normal1() throws Exception
	{
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL1));
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL1));
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL1));
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL1));
		
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL1));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL1));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL1));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL1));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL1));
		
		cvm.toggleTracing(this.subscribersURI.get(0));
		cvm.toggleTracing(this.subscribersURI.get(1));
		cvm.toggleTracing(this.publishersURI.get(0));
		cvm.toggleTracing(this.publishersURI.get(1));
		cvm.toggleTracing(this.subscribersURI.get(2));
		cvm.toggleTracing(this.subscribersURI.get(3));
		cvm.toggleTracing(this.publishersURI.get(2));
		cvm.toggleTracing(this.publishersURI.get(3));
	}
	
	/**
	 * 	 4 sub et 4 pub 
	 * 
	 * 	creation des topics 'A' et 'C'
	 * 	sub1 abonner  au topic 'A' et 'B' 
	 * 	sub2 abonner au topic 'B' et 'C' (filtre sur la "size")
	 * 
	 * pub1 publie un message avec le topic 'A' , 'B' et 'C' (sans filtre)
	 * pub2 publie un message sur 'A' et 'C' avec une propriete size = 10
	 * 
	 */
	private void execute_scenario_normal() throws Exception
	{
		System.out.println("Execution du scenario normal v2");
		for(int i = 0; i < 5; i++)
			this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL));
		for(int i = 0; i < 5; i++)
			this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL));
		
		cvm.toggleTracing(this.subscribersURI.get(0));
		cvm.toggleTracing(this.subscribersURI.get(1));
		cvm.toggleTracing(this.subscribersURI.get(2));
		
		cvm.toggleTracing(this.publishersURI.get(0));
		cvm.toggleTracing(this.publishersURI.get(1));
		cvm.toggleTracing(this.publishersURI.get(2));
		
	}
	
	/**
	 * 100 subscriber 50 publisher
	 * fonction aleatoire lancer en boucle  avec des parametre aussi aleatoire sur avec tout les publisher et subscriber creer 
	 * @throws Exception
	 */
	private void execute_scenario_bigger() throws Exception
	{
		for(int i = 0; i < 100; i++)
			this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_BIGGER));
		for(int i = 0; i < 50; i++)
			this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_BIGGER));

		cvm.toggleTracing(this.subscribersURI.get(0));
		cvm.toggleTracing(this.subscribersURI.get(1));
		cvm.toggleTracing(this.subscribersURI.get(this.subscribersURI.size() - 1));
		
		cvm.toggleTracing(this.publishersURI.get(0));
		cvm.toggleTracing(this.publishersURI.get(1));
		cvm.toggleTracing(this.publishersURI.get(this.publishersURI.size() - 1));		
	}

	/**
	 * 
	 * 10000 subscriber 1000 publisher
	 * fonction aleatoire lancer en boucle  avec des parametre aussi aleatoire sur avec tout les publisher et subscriber creer 
	 * @throws Exception
	 */
	private void execute_scenario_insane() throws Exception
	{
		for(int i = 0; i < 10000; i++)
			this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_INSANE));
		for(int i = 0; i < 1000; i++)
			this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_INSANE));

		cvm.toggleTracing(this.subscribersURI.get(0));
		cvm.toggleTracing(this.subscribersURI.get(1));
		cvm.toggleTracing(this.subscribersURI.get(this.subscribersURI.size() - 1));
		
		cvm.toggleTracing(this.publishersURI.get(0));
		cvm.toggleTracing(this.publishersURI.get(1));
		cvm.toggleTracing(this.publishersURI.get(this.publishersURI.size() - 1));		
	}
	
	/**
	 * 
	 * juste 1 sub
	 * Test toutes les fonctions requiere par subscriber de maniere exhaustif 
	 *	test (subscribe, unsubscribe, createTopic, createTopics, destryTopic, isTopic et getTopics)
	 */
	private void execute_testCompletSubscriber() throws Exception
	{
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, TestCompletTopSub));
		cvm.toggleTracing(this.subscribersURI.get(0));
	}

	/**
	 * 1 sub et 1 pub
	 * Test toutes les fonctions requiere par publisher de maniere exhaustif 
	 * utilise un sub deja abonner a 2 topic dont 1 avec un fitre
	 * test (createTopic, createTopics, destryTopic, isTopic, getTopics
	 * et tous les publish)
	 */
	private void execute_testCompletPub() throws Exception
	{
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_BASIC1));
		cvm.toggleTracing(this.subscribersURI.get(0));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, TestCompletPublisher));
		cvm.toggleTracing(this.publishersURI.get(0));	
	}

	/**
	 * 
	 * @return
	 */
	public String getBrokerURI()
	{
		return brokerURI;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getSubscribersURI()
	{
		return subscribersURI;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getPublishersURI()
	{
		return publishersURI;
	}

	/**
	 * 
	 * @return
	 */
	public static TestScenario getMySelf()
	{
		return TestScenario.myself;
	}
}
