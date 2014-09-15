package epss.repository.dao;

import epss.repository.model.FlowCtrlHis;
import epss.repository.model.FlowCtrlHisExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FlowCtrlHisMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int countByExample(FlowCtrlHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int deleteByExample(FlowCtrlHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int insert(FlowCtrlHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int insertSelective(FlowCtrlHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    List<FlowCtrlHis> selectByExample(FlowCtrlHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    FlowCtrlHis selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int updateByExampleSelective(@Param("record") FlowCtrlHis record, @Param("example") FlowCtrlHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int updateByExample(@Param("record") FlowCtrlHis record, @Param("example") FlowCtrlHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int updateByPrimaryKeySelective(FlowCtrlHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.FLOW_CTRL_HIS
     *
     * @mbggenerated Mon Sep 15 11:47:29 CST 2014
     */
    int updateByPrimaryKey(FlowCtrlHis record);
}