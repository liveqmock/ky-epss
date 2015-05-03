package epss.repository.dao;

import epss.repository.model.ProgStlItemTkMea;
import epss.repository.model.ProgStlItemTkMeaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgStlItemTkMeaMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int countByExample(ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int deleteByExample(ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int insert(ProgStlItemTkMea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int insertSelective(ProgStlItemTkMea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    List<ProgStlItemTkMea> selectByExample(ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    ProgStlItemTkMea selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int updateByExampleSelective(@Param("record") ProgStlItemTkMea record, @Param("example") ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int updateByExample(@Param("record") ProgStlItemTkMea record, @Param("example") ProgStlItemTkMeaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int updateByPrimaryKeySelective(ProgStlItemTkMea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_tk_mea
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    int updateByPrimaryKey(ProgStlItemTkMea record);
}