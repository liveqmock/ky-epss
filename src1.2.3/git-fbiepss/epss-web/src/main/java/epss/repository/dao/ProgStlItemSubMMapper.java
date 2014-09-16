package epss.repository.dao;

import epss.repository.model.ProgStlItemSubM;
import epss.repository.model.ProgStlItemSubMExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgStlItemSubMMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int countByExample(ProgStlItemSubMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int deleteByExample(ProgStlItemSubMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int insert(ProgStlItemSubM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int insertSelective(ProgStlItemSubM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    List<ProgStlItemSubM> selectByExample(ProgStlItemSubMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    ProgStlItemSubM selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByExampleSelective(@Param("record") ProgStlItemSubM record, @Param("example") ProgStlItemSubMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByExample(@Param("record") ProgStlItemSubM record, @Param("example") ProgStlItemSubMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByPrimaryKeySelective(ProgStlItemSubM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_M
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByPrimaryKey(ProgStlItemSubM record);
}