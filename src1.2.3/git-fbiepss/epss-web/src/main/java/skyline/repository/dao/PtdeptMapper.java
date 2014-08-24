package skyline.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import skyline.repository.model.Ptdept;
import skyline.repository.model.PtdeptExample;

public interface PtdeptMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int countByExample(PtdeptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int deleteByExample(PtdeptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int deleteByPrimaryKey(String deptid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int insert(Ptdept record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int insertSelective(Ptdept record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    List<Ptdept> selectByExample(PtdeptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    Ptdept selectByPrimaryKey(String deptid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int updateByExampleSelective(@Param("record") Ptdept record, @Param("example") PtdeptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int updateByExample(@Param("record") Ptdept record, @Param("example") PtdeptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int updateByPrimaryKeySelective(Ptdept record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTDEPT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int updateByPrimaryKey(Ptdept record);
}