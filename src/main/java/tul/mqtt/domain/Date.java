package tul.mqtt.domain;

import lombok.Data;

@Data
public class Date {

    private String temp;
    private String humi;
    private String light;
    private String ic;
    private String LED1;
    private String LED2;
    private String fan;

}
