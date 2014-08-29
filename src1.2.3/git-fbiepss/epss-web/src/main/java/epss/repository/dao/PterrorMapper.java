package epss.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import epss.repository.model.Pterror;
import epss.repository.model.PterrorExample;

public interface PterrorMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int countByExample(PterrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int deleteByExample(PterrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int insert(Pterror record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int insertSelective(Pterror record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    List<Pterror> selectByExample(PterrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int updateByExampleSelective(@Param("record") Pterror record, @Param("example") PterrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTERROR
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    int updateByExample(@Param("record") Pterror record, @Param("example") PterrorExample example);
}