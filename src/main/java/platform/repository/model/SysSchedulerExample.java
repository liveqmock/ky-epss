package platform.repository.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SysSchedulerExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public SysSchedulerExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andJobidIsNull() {
            addCriterion("JOBID is null");
            return (Criteria) this;
        }

        public Criteria andJobidIsNotNull() {
            addCriterion("JOBID is not null");
            return (Criteria) this;
        }

        public Criteria andJobidEqualTo(Integer value) {
            addCriterion("JOBID =", value, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidNotEqualTo(Integer value) {
            addCriterion("JOBID <>", value, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidGreaterThan(Integer value) {
            addCriterion("JOBID >", value, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidGreaterThanOrEqualTo(Integer value) {
            addCriterion("JOBID >=", value, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidLessThan(Integer value) {
            addCriterion("JOBID <", value, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidLessThanOrEqualTo(Integer value) {
            addCriterion("JOBID <=", value, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidIn(List<Integer> values) {
            addCriterion("JOBID in", values, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidNotIn(List<Integer> values) {
            addCriterion("JOBID not in", values, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidBetween(Integer value1, Integer value2) {
            addCriterion("JOBID between", value1, value2, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobidNotBetween(Integer value1, Integer value2) {
            addCriterion("JOBID not between", value1, value2, "jobid");
            return (Criteria) this;
        }

        public Criteria andJobnameIsNull() {
            addCriterion("JOBNAME is null");
            return (Criteria) this;
        }

        public Criteria andJobnameIsNotNull() {
            addCriterion("JOBNAME is not null");
            return (Criteria) this;
        }

        public Criteria andJobnameEqualTo(String value) {
            addCriterion("JOBNAME =", value, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameNotEqualTo(String value) {
            addCriterion("JOBNAME <>", value, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameGreaterThan(String value) {
            addCriterion("JOBNAME >", value, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameGreaterThanOrEqualTo(String value) {
            addCriterion("JOBNAME >=", value, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameLessThan(String value) {
            addCriterion("JOBNAME <", value, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameLessThanOrEqualTo(String value) {
            addCriterion("JOBNAME <=", value, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameLike(String value) {
            addCriterion("JOBNAME like", value, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameNotLike(String value) {
            addCriterion("JOBNAME not like", value, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameIn(List<String> values) {
            addCriterion("JOBNAME in", values, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameNotIn(List<String> values) {
            addCriterion("JOBNAME not in", values, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameBetween(String value1, String value2) {
            addCriterion("JOBNAME between", value1, value2, "jobname");
            return (Criteria) this;
        }

        public Criteria andJobnameNotBetween(String value1, String value2) {
            addCriterion("JOBNAME not between", value1, value2, "jobname");
            return (Criteria) this;
        }

        public Criteria andCronexpressionIsNull() {
            addCriterion("CRONEXPRESSION is null");
            return (Criteria) this;
        }

        public Criteria andCronexpressionIsNotNull() {
            addCriterion("CRONEXPRESSION is not null");
            return (Criteria) this;
        }

        public Criteria andCronexpressionEqualTo(String value) {
            addCriterion("CRONEXPRESSION =", value, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionNotEqualTo(String value) {
            addCriterion("CRONEXPRESSION <>", value, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionGreaterThan(String value) {
            addCriterion("CRONEXPRESSION >", value, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionGreaterThanOrEqualTo(String value) {
            addCriterion("CRONEXPRESSION >=", value, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionLessThan(String value) {
            addCriterion("CRONEXPRESSION <", value, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionLessThanOrEqualTo(String value) {
            addCriterion("CRONEXPRESSION <=", value, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionLike(String value) {
            addCriterion("CRONEXPRESSION like", value, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionNotLike(String value) {
            addCriterion("CRONEXPRESSION not like", value, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionIn(List<String> values) {
            addCriterion("CRONEXPRESSION in", values, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionNotIn(List<String> values) {
            addCriterion("CRONEXPRESSION not in", values, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionBetween(String value1, String value2) {
            addCriterion("CRONEXPRESSION between", value1, value2, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andCronexpressionNotBetween(String value1, String value2) {
            addCriterion("CRONEXPRESSION not between", value1, value2, "cronexpression");
            return (Criteria) this;
        }

        public Criteria andSuccesscountIsNull() {
            addCriterion("SUCCESSCOUNT is null");
            return (Criteria) this;
        }

        public Criteria andSuccesscountIsNotNull() {
            addCriterion("SUCCESSCOUNT is not null");
            return (Criteria) this;
        }

        public Criteria andSuccesscountEqualTo(Integer value) {
            addCriterion("SUCCESSCOUNT =", value, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountNotEqualTo(Integer value) {
            addCriterion("SUCCESSCOUNT <>", value, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountGreaterThan(Integer value) {
            addCriterion("SUCCESSCOUNT >", value, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountGreaterThanOrEqualTo(Integer value) {
            addCriterion("SUCCESSCOUNT >=", value, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountLessThan(Integer value) {
            addCriterion("SUCCESSCOUNT <", value, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountLessThanOrEqualTo(Integer value) {
            addCriterion("SUCCESSCOUNT <=", value, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountIn(List<Integer> values) {
            addCriterion("SUCCESSCOUNT in", values, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountNotIn(List<Integer> values) {
            addCriterion("SUCCESSCOUNT not in", values, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountBetween(Integer value1, Integer value2) {
            addCriterion("SUCCESSCOUNT between", value1, value2, "successcount");
            return (Criteria) this;
        }

        public Criteria andSuccesscountNotBetween(Integer value1, Integer value2) {
            addCriterion("SUCCESSCOUNT not between", value1, value2, "successcount");
            return (Criteria) this;
        }

        public Criteria andFailcountIsNull() {
            addCriterion("FAILCOUNT is null");
            return (Criteria) this;
        }

        public Criteria andFailcountIsNotNull() {
            addCriterion("FAILCOUNT is not null");
            return (Criteria) this;
        }

        public Criteria andFailcountEqualTo(Integer value) {
            addCriterion("FAILCOUNT =", value, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountNotEqualTo(Integer value) {
            addCriterion("FAILCOUNT <>", value, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountGreaterThan(Integer value) {
            addCriterion("FAILCOUNT >", value, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("FAILCOUNT >=", value, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountLessThan(Integer value) {
            addCriterion("FAILCOUNT <", value, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountLessThanOrEqualTo(Integer value) {
            addCriterion("FAILCOUNT <=", value, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountIn(List<Integer> values) {
            addCriterion("FAILCOUNT in", values, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountNotIn(List<Integer> values) {
            addCriterion("FAILCOUNT not in", values, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountBetween(Integer value1, Integer value2) {
            addCriterion("FAILCOUNT between", value1, value2, "failcount");
            return (Criteria) this;
        }

        public Criteria andFailcountNotBetween(Integer value1, Integer value2) {
            addCriterion("FAILCOUNT not between", value1, value2, "failcount");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeIsNull() {
            addCriterion("LASTEXECUTETIME is null");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeIsNotNull() {
            addCriterion("LASTEXECUTETIME is not null");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeEqualTo(Date value) {
            addCriterion("LASTEXECUTETIME =", value, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeNotEqualTo(Date value) {
            addCriterion("LASTEXECUTETIME <>", value, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeGreaterThan(Date value) {
            addCriterion("LASTEXECUTETIME >", value, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LASTEXECUTETIME >=", value, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeLessThan(Date value) {
            addCriterion("LASTEXECUTETIME <", value, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeLessThanOrEqualTo(Date value) {
            addCriterion("LASTEXECUTETIME <=", value, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeIn(List<Date> values) {
            addCriterion("LASTEXECUTETIME in", values, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeNotIn(List<Date> values) {
            addCriterion("LASTEXECUTETIME not in", values, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeBetween(Date value1, Date value2) {
            addCriterion("LASTEXECUTETIME between", value1, value2, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andLastexecutetimeNotBetween(Date value1, Date value2) {
            addCriterion("LASTEXECUTETIME not between", value1, value2, "lastexecutetime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("STATUS is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("STATUS =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("STATUS <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("STATUS >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("STATUS >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("STATUS <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("STATUS <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("STATUS like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("STATUS not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("STATUS in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("STATUS not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("STATUS between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("STATUS not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andMailonfailIsNull() {
            addCriterion("MAILONFAIL is null");
            return (Criteria) this;
        }

        public Criteria andMailonfailIsNotNull() {
            addCriterion("MAILONFAIL is not null");
            return (Criteria) this;
        }

        public Criteria andMailonfailEqualTo(String value) {
            addCriterion("MAILONFAIL =", value, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailNotEqualTo(String value) {
            addCriterion("MAILONFAIL <>", value, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailGreaterThan(String value) {
            addCriterion("MAILONFAIL >", value, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailGreaterThanOrEqualTo(String value) {
            addCriterion("MAILONFAIL >=", value, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailLessThan(String value) {
            addCriterion("MAILONFAIL <", value, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailLessThanOrEqualTo(String value) {
            addCriterion("MAILONFAIL <=", value, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailLike(String value) {
            addCriterion("MAILONFAIL like", value, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailNotLike(String value) {
            addCriterion("MAILONFAIL not like", value, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailIn(List<String> values) {
            addCriterion("MAILONFAIL in", values, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailNotIn(List<String> values) {
            addCriterion("MAILONFAIL not in", values, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailBetween(String value1, String value2) {
            addCriterion("MAILONFAIL between", value1, value2, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andMailonfailNotBetween(String value1, String value2) {
            addCriterion("MAILONFAIL not between", value1, value2, "mailonfail");
            return (Criteria) this;
        }

        public Criteria andInformationIsNull() {
            addCriterion("INFORMATION is null");
            return (Criteria) this;
        }

        public Criteria andInformationIsNotNull() {
            addCriterion("INFORMATION is not null");
            return (Criteria) this;
        }

        public Criteria andInformationEqualTo(String value) {
            addCriterion("INFORMATION =", value, "information");
            return (Criteria) this;
        }

        public Criteria andInformationNotEqualTo(String value) {
            addCriterion("INFORMATION <>", value, "information");
            return (Criteria) this;
        }

        public Criteria andInformationGreaterThan(String value) {
            addCriterion("INFORMATION >", value, "information");
            return (Criteria) this;
        }

        public Criteria andInformationGreaterThanOrEqualTo(String value) {
            addCriterion("INFORMATION >=", value, "information");
            return (Criteria) this;
        }

        public Criteria andInformationLessThan(String value) {
            addCriterion("INFORMATION <", value, "information");
            return (Criteria) this;
        }

        public Criteria andInformationLessThanOrEqualTo(String value) {
            addCriterion("INFORMATION <=", value, "information");
            return (Criteria) this;
        }

        public Criteria andInformationLike(String value) {
            addCriterion("INFORMATION like", value, "information");
            return (Criteria) this;
        }

        public Criteria andInformationNotLike(String value) {
            addCriterion("INFORMATION not like", value, "information");
            return (Criteria) this;
        }

        public Criteria andInformationIn(List<String> values) {
            addCriterion("INFORMATION in", values, "information");
            return (Criteria) this;
        }

        public Criteria andInformationNotIn(List<String> values) {
            addCriterion("INFORMATION not in", values, "information");
            return (Criteria) this;
        }

        public Criteria andInformationBetween(String value1, String value2) {
            addCriterion("INFORMATION between", value1, value2, "information");
            return (Criteria) this;
        }

        public Criteria andInformationNotBetween(String value1, String value2) {
            addCriterion("INFORMATION not between", value1, value2, "information");
            return (Criteria) this;
        }

        public Criteria andJobactionIsNull() {
            addCriterion("JOBACTION is null");
            return (Criteria) this;
        }

        public Criteria andJobactionIsNotNull() {
            addCriterion("JOBACTION is not null");
            return (Criteria) this;
        }

        public Criteria andJobactionEqualTo(String value) {
            addCriterion("JOBACTION =", value, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionNotEqualTo(String value) {
            addCriterion("JOBACTION <>", value, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionGreaterThan(String value) {
            addCriterion("JOBACTION >", value, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionGreaterThanOrEqualTo(String value) {
            addCriterion("JOBACTION >=", value, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionLessThan(String value) {
            addCriterion("JOBACTION <", value, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionLessThanOrEqualTo(String value) {
            addCriterion("JOBACTION <=", value, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionLike(String value) {
            addCriterion("JOBACTION like", value, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionNotLike(String value) {
            addCriterion("JOBACTION not like", value, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionIn(List<String> values) {
            addCriterion("JOBACTION in", values, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionNotIn(List<String> values) {
            addCriterion("JOBACTION not in", values, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionBetween(String value1, String value2) {
            addCriterion("JOBACTION between", value1, value2, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobactionNotBetween(String value1, String value2) {
            addCriterion("JOBACTION not between", value1, value2, "jobaction");
            return (Criteria) this;
        }

        public Criteria andJobmethodIsNull() {
            addCriterion("JOBMETHOD is null");
            return (Criteria) this;
        }

        public Criteria andJobmethodIsNotNull() {
            addCriterion("JOBMETHOD is not null");
            return (Criteria) this;
        }

        public Criteria andJobmethodEqualTo(String value) {
            addCriterion("JOBMETHOD =", value, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodNotEqualTo(String value) {
            addCriterion("JOBMETHOD <>", value, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodGreaterThan(String value) {
            addCriterion("JOBMETHOD >", value, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodGreaterThanOrEqualTo(String value) {
            addCriterion("JOBMETHOD >=", value, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodLessThan(String value) {
            addCriterion("JOBMETHOD <", value, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodLessThanOrEqualTo(String value) {
            addCriterion("JOBMETHOD <=", value, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodLike(String value) {
            addCriterion("JOBMETHOD like", value, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodNotLike(String value) {
            addCriterion("JOBMETHOD not like", value, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodIn(List<String> values) {
            addCriterion("JOBMETHOD in", values, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodNotIn(List<String> values) {
            addCriterion("JOBMETHOD not in", values, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodBetween(String value1, String value2) {
            addCriterion("JOBMETHOD between", value1, value2, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobmethodNotBetween(String value1, String value2) {
            addCriterion("JOBMETHOD not between", value1, value2, "jobmethod");
            return (Criteria) this;
        }

        public Criteria andJobparamIsNull() {
            addCriterion("JOBPARAM is null");
            return (Criteria) this;
        }

        public Criteria andJobparamIsNotNull() {
            addCriterion("JOBPARAM is not null");
            return (Criteria) this;
        }

        public Criteria andJobparamEqualTo(String value) {
            addCriterion("JOBPARAM =", value, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamNotEqualTo(String value) {
            addCriterion("JOBPARAM <>", value, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamGreaterThan(String value) {
            addCriterion("JOBPARAM >", value, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamGreaterThanOrEqualTo(String value) {
            addCriterion("JOBPARAM >=", value, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamLessThan(String value) {
            addCriterion("JOBPARAM <", value, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamLessThanOrEqualTo(String value) {
            addCriterion("JOBPARAM <=", value, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamLike(String value) {
            addCriterion("JOBPARAM like", value, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamNotLike(String value) {
            addCriterion("JOBPARAM not like", value, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamIn(List<String> values) {
            addCriterion("JOBPARAM in", values, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamNotIn(List<String> values) {
            addCriterion("JOBPARAM not in", values, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamBetween(String value1, String value2) {
            addCriterion("JOBPARAM between", value1, value2, "jobparam");
            return (Criteria) this;
        }

        public Criteria andJobparamNotBetween(String value1, String value2) {
            addCriterion("JOBPARAM not between", value1, value2, "jobparam");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated do_not_delete_during_merge Fri Jul 22 13:16:43 CST 2011
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table SYS_SCHEDULER
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}