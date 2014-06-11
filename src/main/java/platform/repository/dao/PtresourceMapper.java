package platform.repository.dao;

import platform.repository.model.Ptresource;
import platform.repository.model.PtresourceExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface PtresourceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    int countByExample(PtresourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    int deleteByExample(PtresourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    int insert(Ptresource record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    int insertSelective(Ptresource record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    List<Ptresource> selectByExample(PtresourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    int updateByExampleSelective(@Param("record") Ptresource record, @Param("example") PtresourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    int updateByExample(@Param("record") Ptresource record, @Param("example") PtresourceExample example);
}