package tul.mqtt.client;

import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tul.mqtt.Dao.DataDao;
import tul.mqtt.domain.*;

import javax.xml.crypto.Data;

@Component
public class MessageCallback implements MqttCallback {
    @Autowired
    private DataDao dataDao;

    private IMqttClient mqttClient;

    @Autowired
    private EmqClient emqClient;

    private static final Logger log= (Logger) LoggerFactory.getLogger(MessageCallback.class);
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("丢失了对服务器的连接");
//        try {
//            if (null != mqttClient && !mqttClient.isConnected()) {
//                mqttClient.reconnect();
//                log.error("尝试重新连接");
//            } else {
//                mqttClient.connect(emqClient.getOptions());
//                log.error("尝试建立新连接");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("订阅者订阅到了消息，topic={},messageid={},qos={},payload={}",
                topic,
                mqttMessage.getId(),
                mqttMessage.getQos(),
                new String(mqttMessage.getPayload()));

        String str = new String(mqttMessage.getPayload());

        JSONObject jsonObject = JSONObject.parseObject(str);

        Date date = JSONObject.toJavaObject(jsonObject, Date.class);

        Temp temp = new Temp();
        temp.setTemp(date.getTemp());
        dataDao.saveTemp(temp);


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        int messageId = iMqttDeliveryToken.getMessageId();
        String[] topics = iMqttDeliveryToken.getTopics();
        log.info("消息发布完成，messageid={},topics={}",messageId,topics);
    }
}
