package epss.repository.dao;

import epss.repository.model.EsInitPower;
import epss.repository.model.EsInitPowerExample;
import epss.repository.model.EsInitPowerKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EsInitPowerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int countByExample(EsInitPowerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int deleteByExample(EsInitPowerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int deleteByPrimaryKey(EsInitPowerKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int insert(EsInitPower record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int insertSelective(EsInitPower record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    List<EsInitPower> selectByExample(EsInitPowerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    EsInitPower selectByPrimaryKey(EsInitPowerKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int updateByExampleSelective(@Param("record") EsInitPower record, @Param("example") EsInitPowerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int updateByExample(@Param("record") EsInitPower record, @Param("example") EsInitPowerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int updateByPrimaryKeySelective(EsInitPower record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    int updateByPrimaryKey(EsInitPower record);
}