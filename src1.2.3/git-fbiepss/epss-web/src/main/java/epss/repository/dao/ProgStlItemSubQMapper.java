package epss.repository.dao;

import epss.repository.model.ProgStlItemSubQ;
import epss.repository.model.ProgStlItemSubQExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgStlItemSubQMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int countByExample(ProgStlItemSubQExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int deleteByExample(ProgStlItemSubQExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int insert(ProgStlItemSubQ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int insertSelective(ProgStlItemSubQ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    List<ProgStlItemSubQ> selectByExample(ProgStlItemSubQExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    ProgStlItemSubQ selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByExampleSelective(@Param("record") ProgStlItemSubQ record, @Param("example") ProgStlItemSubQExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByExample(@Param("record") ProgStlItemSubQ record, @Param("example") ProgStlItemSubQExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByPrimaryKeySelective(ProgStlItemSubQ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_Q
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByPrimaryKey(ProgStlItemSubQ record);
}