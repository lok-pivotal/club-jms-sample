package eda.club.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class DemoSub {

    Logger logger = LoggerFactory.getLogger(DemoSub.class);

    @JmsListener(destination = "${club.jms.queue.sub}", containerFactory = "queueListenerFactory")
    public void run(String msg) throws Exception {
        logger.info("receving message " + msg);
        return;
    }

}
