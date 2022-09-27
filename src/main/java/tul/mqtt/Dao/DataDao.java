package tul.mqtt.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import tul.mqtt.domain.*;

@Mapper
public interface DataDao {

//    @Insert("insert into tb_outtemp values (null,#{tempOut},now())")
//    void saveOut(OutTemp outTemp);
//
//    @Insert("insert into tb_intemp values (null,#{tempIn},now())")
//    void saveIn(InTemp inTemp);


    @Insert("insert into tb_temp values (null,#{temp},now())")
    void saveTemp(Temp temp);

    @Insert("insert into tb_humi values (null,#{humi},now())")
    void saveHumi(Humi humi);

    @Insert("insert into tb_light values (null,#{light},now())")
    void saveLight(Light light);

    @Insert("insert into tb_Ic values (null,#{ic},now())")
    void saveIc(Ic ic);

}
