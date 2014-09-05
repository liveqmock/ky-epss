package epss.repository.dao;

import epss.repository.model.EsInitStl;
import epss.repository.model.EsInitStlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EsInitStlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int countByExample(EsInitStlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int deleteByExample(EsInitStlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int insert(EsInitStl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int insertSelective(EsInitStl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    List<EsInitStl> selectByExample(EsInitStlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    EsInitStl selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int updateByExampleSelective(@Param("record") EsInitStl record, @Param("example") EsInitStlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int updateByExample(@Param("record") EsInitStl record, @Param("example") EsInitStlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int updateByPrimaryKeySelective(EsInitStl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_STL
     *
     * @mbggenerated Fri Sep 05 08:42:34 CST 2014
     */
    int updateByPrimaryKey(EsInitStl record);
}