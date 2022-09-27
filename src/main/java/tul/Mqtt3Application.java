package tul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tul.mqtt.client.EmqClient;
import tul.mqtt.enums.QosEnum;
import tul.mqtt.properties.MqttProperties;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Mqtt3Application {

    public static void main(String[] args) {
        SpringApplication.run(Mqtt3Application.class, args);
    }


    @Autowired
    private EmqClient emqClient;

    @Autowired
    private MqttProperties properties;

    @PostConstruct
    public void init() {

        emqClient.connect(properties.getUsername(), properties.getPassword());

        emqClient.subscribe("mzdsub/#", QosEnum.QoS0);

    }
}
