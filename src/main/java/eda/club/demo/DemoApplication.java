package eda.club.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

@EnableJms
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// override default jms container factory with topic behaviour
	@Bean
	public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory,
															   DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(true); // true for topic
		// This provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory);
		// perhaps externalize other factory configuration
		return factory;
	}

	// override default jms container factory with queue behaviour
	@Bean
	public JmsListenerContainerFactory<?> queueListenerFactory(ConnectionFactory connectionFactory,
															   DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(false); // false for queue
		// This provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory);
		// perhaps externalize other factory configuration
		return factory;
	}

	// sets the pubSubDomain on the destination when jmstemplate send message
	@Bean
	public DynamicDestinationResolver destinationResolver() {
		return new DynamicDestinationResolver() {
			@Override
			public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException, JMSException {
				// may consider adding condition, when for topic or queue
				//if (destinationName.contains(".topic."))
				pubSubDomain = true;
				return super.resolveDestinationName(session, destinationName, pubSubDomain);
			}
		};
	}

//
//	@Bean // Serialize message content to json using TextMessage
//	public MessageConverter jacksonJmsMessageConverter() {
//		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//		converter.setTargetType(MessageType.TEXT);
//		converter.setTypeIdPropertyName("_type");
//		return converter;
//	}
//
}
