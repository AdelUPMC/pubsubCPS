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
	public static final String	SCENARIO_NORMAL2 = "scenario-normal-2";
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
		case SCENARIO_NORMAL2:
			execute_scenario_normal2();
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
	/*
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
	private void execute_scenario_normal2() throws Exception
	{
		System.out.println("Execution du scenario normal v2");
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL2));
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL2));
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL2));
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_NORMAL2));
		
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL2));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL2));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL2));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL2));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_NORMAL2));
		
		cvm.toggleTracing(this.subscribersURI.get(0));
		cvm.toggleTracing(this.subscribersURI.get(1));
		cvm.toggleTracing(this.subscribersURI.get(2));
		cvm.toggleTracing(this.subscribersURI.get(3));
		cvm.toggleTracing(this.publishersURI.get(0));
		cvm.toggleTracing(this.publishersURI.get(1));
		cvm.toggleTracing(this.publishersURI.get(2));
		cvm.toggleTracing(this.publishersURI.get(3));
	}
	
	/*
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
	
	/*
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

	public String getBrokerURI() {
		return brokerURI;
	}

	public ArrayList<String> getSubscribersURI() {
		return subscribersURI;
	}

	public ArrayList<String> getPublishersURI() {
		return publishersURI;
	}
	public static TestScenario getMySelf()
	{
		return TestScenario.myself;
	}
}
