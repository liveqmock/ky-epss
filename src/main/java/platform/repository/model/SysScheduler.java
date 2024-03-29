package platform.repository.model;

import java.util.Date;

public class SysScheduler {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.JOBID
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private Integer jobid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.JOBNAME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String jobname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.CRONEXPRESSION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String cronexpression;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.SUCCESSCOUNT
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private Integer successcount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.FAILCOUNT
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private Integer failcount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.LASTEXECUTETIME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private Date lastexecutetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.STATUS
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.MAILONFAIL
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String mailonfail;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.INFORMATION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String information;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.JOBACTION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String jobaction;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.JOBMETHOD
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String jobmethod;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER.JOBPARAM
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String jobparam;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.JOBID
     *
     * @return the value of SYS_SCHEDULER.JOBID
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public Integer getJobid() {
        return jobid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.JOBID
     *
     * @param jobid the value for SYS_SCHEDULER.JOBID
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setJobid(Integer jobid) {
        this.jobid = jobid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.JOBNAME
     *
     * @return the value of SYS_SCHEDULER.JOBNAME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getJobname() {
        return jobname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.JOBNAME
     *
     * @param jobname the value for SYS_SCHEDULER.JOBNAME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setJobname(String jobname) {
        this.jobname = jobname == null ? null : jobname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.CRONEXPRESSION
     *
     * @return the value of SYS_SCHEDULER.CRONEXPRESSION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getCronexpression() {
        return cronexpression;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.CRONEXPRESSION
     *
     * @param cronexpression the value for SYS_SCHEDULER.CRONEXPRESSION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setCronexpression(String cronexpression) {
        this.cronexpression = cronexpression == null ? null : cronexpression.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.SUCCESSCOUNT
     *
     * @return the value of SYS_SCHEDULER.SUCCESSCOUNT
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public Integer getSuccesscount() {
        return successcount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.SUCCESSCOUNT
     *
     * @param successcount the value for SYS_SCHEDULER.SUCCESSCOUNT
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setSuccesscount(Integer successcount) {
        this.successcount = successcount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.FAILCOUNT
     *
     * @return the value of SYS_SCHEDULER.FAILCOUNT
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public Integer getFailcount() {
        return failcount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.FAILCOUNT
     *
     * @param failcount the value for SYS_SCHEDULER.FAILCOUNT
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setFailcount(Integer failcount) {
        this.failcount = failcount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.LASTEXECUTETIME
     *
     * @return the value of SYS_SCHEDULER.LASTEXECUTETIME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public Date getLastexecutetime() {
        return lastexecutetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.LASTEXECUTETIME
     *
     * @param lastexecutetime the value for SYS_SCHEDULER.LASTEXECUTETIME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setLastexecutetime(Date lastexecutetime) {
        this.lastexecutetime = lastexecutetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.STATUS
     *
     * @return the value of SYS_SCHEDULER.STATUS
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.STATUS
     *
     * @param status the value for SYS_SCHEDULER.STATUS
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.MAILONFAIL
     *
     * @return the value of SYS_SCHEDULER.MAILONFAIL
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getMailonfail() {
        return mailonfail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.MAILONFAIL
     *
     * @param mailonfail the value for SYS_SCHEDULER.MAILONFAIL
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setMailonfail(String mailonfail) {
        this.mailonfail = mailonfail == null ? null : mailonfail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.INFORMATION
     *
     * @return the value of SYS_SCHEDULER.INFORMATION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getInformation() {
        return information;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.INFORMATION
     *
     * @param information the value for SYS_SCHEDULER.INFORMATION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setInformation(String information) {
        this.information = information == null ? null : information.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.JOBACTION
     *
     * @return the value of SYS_SCHEDULER.JOBACTION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getJobaction() {
        return jobaction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.JOBACTION
     *
     * @param jobaction the value for SYS_SCHEDULER.JOBACTION
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setJobaction(String jobaction) {
        this.jobaction = jobaction == null ? null : jobaction.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.JOBMETHOD
     *
     * @return the value of SYS_SCHEDULER.JOBMETHOD
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getJobmethod() {
        return jobmethod;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.JOBMETHOD
     *
     * @param jobmethod the value for SYS_SCHEDULER.JOBMETHOD
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setJobmethod(String jobmethod) {
        this.jobmethod = jobmethod == null ? null : jobmethod.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER.JOBPARAM
     *
     * @return the value of SYS_SCHEDULER.JOBPARAM
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getJobparam() {
        return jobparam;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER.JOBPARAM
     *
     * @param jobparam the value for SYS_SCHEDULER.JOBPARAM
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setJobparam(String jobparam) {
        this.jobparam = jobparam == null ? null : jobparam.trim();
    }
}