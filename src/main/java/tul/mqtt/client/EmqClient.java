package tul.mqtt.client;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tul.mqtt.enums.QosEnum;
import tul.mqtt.properties.MqttProperties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class EmqClient {
    private static final Logger log= LoggerFactory.getLogger(EmqClient.class);

    private IMqttClient mqttClient;


    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttCallback mqttCallback;


    @PostConstruct
    public void init(){
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(mqttProperties.getBrokerUrl(),
                    mqttProperties.getClientId(),
                    memoryPersistence);
        } catch (MqttException e) {
            log.error("初始化客户端mqttClient对象失败,errormsg={},brokerUrl={},clientId={}",e.getMessage(),mqttProperties.getBrokerUrl(),mqttProperties.getClientId());
        }
    }

    public void connect(String username,String password){

        MqttConnectOptions options=new MqttConnectOptions();

        options.setAutomaticReconnect(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);

        mqttClient.setCallback(mqttCallback);

        try {
            mqttClient.connect(options);
        } catch (MqttException e) {
            log.error("mqtt客户端连接服务失败，失败原因{}",e.getMessage());
        }

    }

    public MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        //options.setUserName(PropertiesUtil.MQTT_USER_NAME);
        //options.setPassword(PropertiesUtil.MQTT_PASSWORD.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(30);
        // 设置会话心跳时间
        options.setKeepAliveInterval(30);
        // 是否清除session
        options.setCleanSession(false);
        log.info("--生成mqtt配置对象");
        return options;
    }



    @PreDestroy
    public void disConnect(){
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            log.error("断开连接产生异常，异常信息",e.getMessage());
        }
    }


    public void reConnect(){
        try {
            mqttClient.reconnect();
        } catch (MqttException e) {
            log.error("重连失败，失败原因{}",e.getMessage());
        }

    }

    public void publish(String topic, String msg, QosEnum qos, boolean retain){


        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(msg.getBytes());
        mqttMessage.setQos(qos.value());
        mqttMessage.setRetained(retain);

        try {
            mqttClient.publish(topic,mqttMessage);
        } catch (MqttException e) {
            log.error("发布消息失败，errormsg={},topic={},msg={},qos={},retain{}",e.getMessage(),topic,msg,qos.value(),retain);
        }
    }


    public void subscribe(String topicFilter,QosEnum qos){
        try {
            mqttClient.subscribe(topicFilter,qos.value());
        } catch (MqttException e) {
            log.error("订阅主题失败，errormsg={},topicFilter={},qos={}",e.getMessage(),topicFilter,qos.value());
        }
    }

    public void unSubscribe(String topicFilter){
        try {
            mqttClient.unsubscribe(topicFilter);
        } catch (MqttException e) {
            log.error("取消订阅失败，erroemsg={},topicfilter={}",e.getMessage(),topicFilter);
        }
    }
}
