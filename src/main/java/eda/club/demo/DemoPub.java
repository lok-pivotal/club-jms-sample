package eda.club.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DemoPub {

    Logger logger = LoggerFactory.getLogger(DemoPub.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${club.jms.topic.pub}")
    private String topicName;

    @Scheduled(fixedRate = 1000)
    public void run() throws Exception {
        String msg = "Hello World: " + UUID.randomUUID();
        logger.info("about to send message=" + msg + " to topic=" + topicName);
        this.jmsTemplate.convertAndSend(topicName, msg);
    }

}
