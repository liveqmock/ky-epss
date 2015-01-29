package epss.repository.dao;

import epss.repository.model.ProgStlItemSubStlment;
import epss.repository.model.ProgStlItemSubStlmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgStlItemSubStlmentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int countByExample(ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int deleteByExample(ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int insert(ProgStlItemSubStlment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int insertSelective(ProgStlItemSubStlment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    List<ProgStlItemSubStlment> selectByExample(ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    ProgStlItemSubStlment selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int updateByExampleSelective(@Param("record") ProgStlItemSubStlment record, @Param("example") ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int updateByExample(@Param("record") ProgStlItemSubStlment record, @Param("example") ProgStlItemSubStlmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int updateByPrimaryKeySelective(ProgStlItemSubStlment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_stlment
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    int updateByPrimaryKey(ProgStlItemSubStlment record);
}