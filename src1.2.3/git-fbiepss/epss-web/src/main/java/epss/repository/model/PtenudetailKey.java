package epss.repository.model;

public class PtenudetailKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PTENUDETAIL.ENUITEMVALUE
     *
     * @mbggenerated Wed Aug 27 15:55:19 CST 2014
     */
    private String enuitemvalue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PTENUDETAIL.ENUTYPE
     *
     * @mbggenerated Wed Aug 27 15:55:19 CST 2014
     */
    private String enutype;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PTENUDETAIL.ENUITEMVALUE
     *
     * @return the value of EPSS.PTENUDETAIL.ENUITEMVALUE
     *
     * @mbggenerated Wed Aug 27 15:55:19 CST 2014
     */
    public String getEnuitemvalue() {
        return enuitemvalue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PTENUDETAIL.ENUITEMVALUE
     *
     * @param enuitemvalue the value for EPSS.PTENUDETAIL.ENUITEMVALUE
     *
     * @mbggenerated Wed Aug 27 15:55:19 CST 2014
     */
    public void setEnuitemvalue(String enuitemvalue) {
        this.enuitemvalue = enuitemvalue == null ? null : enuitemvalue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PTENUDETAIL.ENUTYPE
     *
     * @return the value of EPSS.PTENUDETAIL.ENUTYPE
     *
     * @mbggenerated Wed Aug 27 15:55:19 CST 2014
     */
    public String getEnutype() {
        return enutype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PTENUDETAIL.ENUTYPE
     *
     * @param enutype the value for EPSS.PTENUDETAIL.ENUTYPE
     *
     * @mbggenerated Wed Aug 27 15:55:19 CST 2014
     */
    public void setEnutype(String enutype) {
        this.enutype = enutype == null ? null : enutype.trim();
    }
}