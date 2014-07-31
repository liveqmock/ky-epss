package epss.repository.dao;

import epss.repository.model.OperRes;
import epss.repository.model.OperResExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OperResMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.OPER_RES
     *
     * @mbggenerated Wed Jul 30 11:49:32 CST 2014
     */
    int countByExample(OperResExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.OPER_RES
     *
     * @mbggenerated Wed Jul 30 11:49:32 CST 2014
     */
    int deleteByExample(OperResExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.OPER_RES
     *
     * @mbggenerated Wed Jul 30 11:49:32 CST 2014
     */
    int insert(OperRes record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.OPER_RES
     *
     * @mbggenerated Wed Jul 30 11:49:32 CST 2014
     */
    int insertSelective(OperRes record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.OPER_RES
     *
     * @mbggenerated Wed Jul 30 11:49:32 CST 2014
     */
    List<OperRes> selectByExample(OperResExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.OPER_RES
     *
     * @mbggenerated Wed Jul 30 11:49:32 CST 2014
     */
    int updateByExampleSelective(@Param("record") OperRes record, @Param("example") OperResExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.OPER_RES
     *
     * @mbggenerated Wed Jul 30 11:49:32 CST 2014
     */
    int updateByExample(@Param("record") OperRes record, @Param("example") OperResExample example);
}