package epss.repository.dao;

import epss.repository.model.ProgStlItemTkMea;
import epss.repository.model.ProgStlItemTkMeaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgStlItemTkMeaMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int countByExample(ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int deleteByExample(ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int insert(ProgStlItemTkMea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int insertSelective(ProgStlItemTkMea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    List<ProgStlItemTkMea> selectByExample(ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    ProgStlItemTkMea selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByExampleSelective(@Param("record") ProgStlItemTkMea record, @Param("example") ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByExample(@Param("record") ProgStlItemTkMea record, @Param("example") ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByPrimaryKeySelective(ProgStlItemTkMea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_MEA
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByPrimaryKey(ProgStlItemTkMea record);
}