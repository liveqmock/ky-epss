package epss.repository.model;

public class Ptenudetail extends PtenudetailKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ptenudetail.PKID
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ptenudetail.TID
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    private String tid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ptenudetail.ENUITEMLABEL
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    private String enuitemlabel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ptenudetail.ENUITEMDESC
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    private String enuitemdesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ptenudetail.DISPNO
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    private Integer dispno;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ptenudetail.ENUITEMEXPAND
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    private String enuitemexpand;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ptenudetail.PKID
     *
     * @return the value of ptenudetail.PKID
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ptenudetail.PKID
     *
     * @param pkid the value for ptenudetail.PKID
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ptenudetail.TID
     *
     * @return the value of ptenudetail.TID
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public String getTid() {
        return tid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ptenudetail.TID
     *
     * @param tid the value for ptenudetail.TID
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ptenudetail.ENUITEMLABEL
     *
     * @return the value of ptenudetail.ENUITEMLABEL
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public String getEnuitemlabel() {
        return enuitemlabel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ptenudetail.ENUITEMLABEL
     *
     * @param enuitemlabel the value for ptenudetail.ENUITEMLABEL
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public void setEnuitemlabel(String enuitemlabel) {
        this.enuitemlabel = enuitemlabel == null ? null : enuitemlabel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ptenudetail.ENUITEMDESC
     *
     * @return the value of ptenudetail.ENUITEMDESC
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public String getEnuitemdesc() {
        return enuitemdesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ptenudetail.ENUITEMDESC
     *
     * @param enuitemdesc the value for ptenudetail.ENUITEMDESC
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public void setEnuitemdesc(String enuitemdesc) {
        this.enuitemdesc = enuitemdesc == null ? null : enuitemdesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ptenudetail.DISPNO
     *
     * @return the value of ptenudetail.DISPNO
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public Integer getDispno() {
        return dispno;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ptenudetail.DISPNO
     *
     * @param dispno the value for ptenudetail.DISPNO
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public void setDispno(Integer dispno) {
        this.dispno = dispno;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ptenudetail.ENUITEMEXPAND
     *
     * @return the value of ptenudetail.ENUITEMEXPAND
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public String getEnuitemexpand() {
        return enuitemexpand;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ptenudetail.ENUITEMEXPAND
     *
     * @param enuitemexpand the value for ptenudetail.ENUITEMEXPAND
     *
     * @mbggenerated Fri May 01 00:30:15 CST 2015
     */
    public void setEnuitemexpand(String enuitemexpand) {
        this.enuitemexpand = enuitemexpand == null ? null : enuitemexpand.trim();
    }
}