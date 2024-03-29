package epss.repository.dao;

import epss.repository.model.EsInitPowerHis;
import epss.repository.model.EsInitPowerHisExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EsInitPowerHisMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int countByExample(EsInitPowerHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int deleteByExample(EsInitPowerHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int insert(EsInitPowerHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int insertSelective(EsInitPowerHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    List<EsInitPowerHis> selectByExample(EsInitPowerHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int updateByExampleSelective(@Param("record") EsInitPowerHis record, @Param("example") EsInitPowerHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int updateByExample(@Param("record") EsInitPowerHis record, @Param("example") EsInitPowerHisExample example);
}