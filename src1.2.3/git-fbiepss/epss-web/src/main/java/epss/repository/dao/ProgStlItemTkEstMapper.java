package epss.repository.dao;

import epss.repository.model.ProgStlItemTkEst;
import epss.repository.model.ProgStlItemTkEstExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgStlItemTkEstMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int countByExample(ProgStlItemTkEstExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int deleteByExample(ProgStlItemTkEstExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int insert(ProgStlItemTkEst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int insertSelective(ProgStlItemTkEst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    List<ProgStlItemTkEst> selectByExample(ProgStlItemTkEstExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    ProgStlItemTkEst selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByExampleSelective(@Param("record") ProgStlItemTkEst record, @Param("example") ProgStlItemTkEstExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByExample(@Param("record") ProgStlItemTkEst record, @Param("example") ProgStlItemTkEstExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByPrimaryKeySelective(ProgStlItemTkEst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PROG_STL_ITEM_TK_EST
     *
     * @mbggenerated Mon Sep 15 23:42:56 CST 2014
     */
    int updateByPrimaryKey(ProgStlItemTkEst record);
}