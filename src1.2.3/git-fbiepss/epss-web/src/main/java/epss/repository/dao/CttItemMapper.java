package epss.repository.dao;

import epss.repository.model.CttItem;
import epss.repository.model.CttItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CttItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int countByExample(CttItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int deleteByExample(CttItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int insert(CttItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int insertSelective(CttItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    List<CttItem> selectByExample(CttItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    CttItem selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByExampleSelective(@Param("record") CttItem record, @Param("example") CttItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByExample(@Param("record") CttItem record, @Param("example") CttItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByPrimaryKeySelective(CttItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_ITEM
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByPrimaryKey(CttItem record);
}