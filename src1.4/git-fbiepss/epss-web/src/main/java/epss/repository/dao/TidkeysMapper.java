package epss.repository.dao;

import epss.repository.model.Tidkeys;
import epss.repository.model.TidkeysExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TidkeysMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int countByExample(TidkeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int deleteByExample(TidkeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int insert(Tidkeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int insertSelective(Tidkeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    List<Tidkeys> selectByExample(TidkeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    Tidkeys selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int updateByExampleSelective(@Param("record") Tidkeys record, @Param("example") TidkeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int updateByExample(@Param("record") Tidkeys record, @Param("example") TidkeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int updateByPrimaryKeySelective(Tidkeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tidkeys
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    int updateByPrimaryKey(Tidkeys record);
}