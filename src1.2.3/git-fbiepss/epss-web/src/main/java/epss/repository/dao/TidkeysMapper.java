package epss.repository.dao;

import epss.repository.model.TidKeys;
import epss.repository.model.TidKeysExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TidKeysMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int countByExample(TidKeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int deleteByExample(TidKeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int insert(TidKeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int insertSelective(TidKeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    List<TidKeys> selectByExample(TidKeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    TidKeys selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int updateByExampleSelective(@Param("record") TidKeys record, @Param("example") TidKeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int updateByExample(@Param("record") TidKeys record, @Param("example") TidKeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int updateByPrimaryKeySelective(TidKeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TID_KEYS
     *
     * @mbggenerated Tue Mar 31 15:25:49 CST 2015
     */
    int updateByPrimaryKey(TidKeys record);
}