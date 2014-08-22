package skyline.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import skyline.repository.model.Ptoper;
import skyline.repository.model.PtoperExample;

public interface PtoperMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    int countByExample(PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    int deleteByExample(PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    int insert(Ptoper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    int insertSelective(Ptoper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    List<Ptoper> selectByExample(PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    int updateByExampleSelective(@Param("record") Ptoper record, @Param("example") PtoperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTOPER
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    int updateByExample(@Param("record") Ptoper record, @Param("example") PtoperExample example);
}