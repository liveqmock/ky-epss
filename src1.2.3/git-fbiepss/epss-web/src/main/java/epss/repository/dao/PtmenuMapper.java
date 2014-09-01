package epss.repository.dao;

import epss.repository.model.Ptmenu;
import epss.repository.model.PtmenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PtmenuMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int countByExample(PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int deleteByExample(PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int insert(Ptmenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int insertSelective(Ptmenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    List<Ptmenu> selectByExample(PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    Ptmenu selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int updateByExampleSelective(@Param("record") Ptmenu record, @Param("example") PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int updateByExample(@Param("record") Ptmenu record, @Param("example") PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int updateByPrimaryKeySelective(Ptmenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.PTMENU
     *
     * @mbggenerated Sun Aug 31 11:35:08 CST 2014
     */
    int updateByPrimaryKey(Ptmenu record);
}