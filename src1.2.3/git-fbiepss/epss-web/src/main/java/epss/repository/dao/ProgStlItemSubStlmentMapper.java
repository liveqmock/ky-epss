package epss.repository.dao;

import epss.repository.model.ProgStlItemSubStlment;
import epss.repository.model.ProgStlItemSubStlmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgStlItemSubStlmentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int countByExample(ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int deleteByExample(ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int insert(ProgStlItemSubStlment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int insertSelective(ProgStlItemSubStlment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    List<ProgStlItemSubStlment> selectByExample(ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    ProgStlItemSubStlment selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByExampleSelective(@Param("record") ProgStlItemSubStlment record, @Param("example") ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByExample(@Param("record") ProgStlItemSubStlment record, @Param("example") ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByPrimaryKeySelective(ProgStlItemSubStlment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_SUB_STLMENT
     *
     * @mbggenerated Mon Sep 15 16:15:46 CST 2014
     */
    int updateByPrimaryKey(ProgStlItemSubStlment record);
}