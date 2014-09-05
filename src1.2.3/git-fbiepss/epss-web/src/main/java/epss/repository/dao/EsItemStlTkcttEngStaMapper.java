package epss.repository.dao;

import epss.repository.model.EsItemStlTkcttEngSta;
import epss.repository.model.EsItemStlTkcttEngStaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EsItemStlTkcttEngStaMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int countByExample(EsItemStlTkcttEngStaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int deleteByExample(EsItemStlTkcttEngStaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int insert(EsItemStlTkcttEngSta record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int insertSelective(EsItemStlTkcttEngSta record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    List<EsItemStlTkcttEngSta> selectByExample(EsItemStlTkcttEngStaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    EsItemStlTkcttEngSta selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int updateByExampleSelective(@Param("record") EsItemStlTkcttEngSta record, @Param("example") EsItemStlTkcttEngStaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int updateByExample(@Param("record") EsItemStlTkcttEngSta record, @Param("example") EsItemStlTkcttEngStaExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int updateByPrimaryKeySelective(EsItemStlTkcttEngSta record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_ITEM_STL_TKCTT_ENG_STA
     *
     * @mbggenerated Fri Sep 05 12:05:34 CST 2014
     */
    int updateByPrimaryKey(EsItemStlTkcttEngSta record);
}