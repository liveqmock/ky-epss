package epss.repository.dao;

import epss.repository.model.TkcttInfo;
import epss.repository.model.TkcttInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TkcttInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int countByExample(TkcttInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int deleteByExample(TkcttInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int insert(TkcttInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int insertSelective(TkcttInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    List<TkcttInfo> selectByExample(TkcttInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    TkcttInfo selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int updateByExampleSelective(@Param("record") TkcttInfo record, @Param("example") TkcttInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int updateByExample(@Param("record") TkcttInfo record, @Param("example") TkcttInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int updateByPrimaryKeySelective(TkcttInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.TKCTT_INFO
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    int updateByPrimaryKey(TkcttInfo record);
}