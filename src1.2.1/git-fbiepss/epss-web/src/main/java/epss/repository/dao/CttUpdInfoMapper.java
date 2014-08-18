package epss.repository.dao;

import epss.repository.model.CttUpdInfo;
import epss.repository.model.CttUpdInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CttUpdInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int countByExample(CttUpdInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int deleteByExample(CttUpdInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int insert(CttUpdInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int insertSelective(CttUpdInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    List<CttUpdInfo> selectByExample(CttUpdInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    CttUpdInfo selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int updateByExampleSelective(@Param("record") CttUpdInfo record, @Param("example") CttUpdInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int updateByExample(@Param("record") CttUpdInfo record, @Param("example") CttUpdInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int updateByPrimaryKeySelective(CttUpdInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    int updateByPrimaryKey(CttUpdInfo record);
}