package epss.repository.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProgStlItemSubFExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public ProgStlItemSubFExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
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
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
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

        public Criteria andPkidIsNull() {
            addCriterion("PKID is null");
            return (Criteria) this;
        }

        public Criteria andPkidIsNotNull() {
            addCriterion("PKID is not null");
            return (Criteria) this;
        }

        public Criteria andPkidEqualTo(String value) {
            addCriterion("PKID =", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidNotEqualTo(String value) {
            addCriterion("PKID <>", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidGreaterThan(String value) {
            addCriterion("PKID >", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidGreaterThanOrEqualTo(String value) {
            addCriterion("PKID >=", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidLessThan(String value) {
            addCriterion("PKID <", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidLessThanOrEqualTo(String value) {
            addCriterion("PKID <=", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidLike(String value) {
            addCriterion("PKID like", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidNotLike(String value) {
            addCriterion("PKID not like", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidIn(List<String> values) {
            addCriterion("PKID in", values, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidNotIn(List<String> values) {
            addCriterion("PKID not in", values, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidBetween(String value1, String value2) {
            addCriterion("PKID between", value1, value2, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidNotBetween(String value1, String value2) {
            addCriterion("PKID not between", value1, value2, "pkid");
            return (Criteria) this;
        }

        public Criteria andPeriodNoIsNull() {
            addCriterion("PERIOD_NO is null");
            return (Criteria) this;
        }

        public Criteria andPeriodNoIsNotNull() {
            addCriterion("PERIOD_NO is not null");
            return (Criteria) this;
        }

        public Criteria andPeriodNoEqualTo(String value) {
            addCriterion("PERIOD_NO =", value, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoNotEqualTo(String value) {
            addCriterion("PERIOD_NO <>", value, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoGreaterThan(String value) {
            addCriterion("PERIOD_NO >", value, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoGreaterThanOrEqualTo(String value) {
            addCriterion("PERIOD_NO >=", value, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoLessThan(String value) {
            addCriterion("PERIOD_NO <", value, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoLessThanOrEqualTo(String value) {
            addCriterion("PERIOD_NO <=", value, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoLike(String value) {
            addCriterion("PERIOD_NO like", value, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoNotLike(String value) {
            addCriterion("PERIOD_NO not like", value, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoIn(List<String> values) {
            addCriterion("PERIOD_NO in", values, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoNotIn(List<String> values) {
            addCriterion("PERIOD_NO not in", values, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoBetween(String value1, String value2) {
            addCriterion("PERIOD_NO between", value1, value2, "periodNo");
            return (Criteria) this;
        }

        public Criteria andPeriodNoNotBetween(String value1, String value2) {
            addCriterion("PERIOD_NO not between", value1, value2, "periodNo");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidIsNull() {
            addCriterion("SUBCTT_PKID is null");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidIsNotNull() {
            addCriterion("SUBCTT_PKID is not null");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidEqualTo(String value) {
            addCriterion("SUBCTT_PKID =", value, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidNotEqualTo(String value) {
            addCriterion("SUBCTT_PKID <>", value, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidGreaterThan(String value) {
            addCriterion("SUBCTT_PKID >", value, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidGreaterThanOrEqualTo(String value) {
            addCriterion("SUBCTT_PKID >=", value, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidLessThan(String value) {
            addCriterion("SUBCTT_PKID <", value, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidLessThanOrEqualTo(String value) {
            addCriterion("SUBCTT_PKID <=", value, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidLike(String value) {
            addCriterion("SUBCTT_PKID like", value, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidNotLike(String value) {
            addCriterion("SUBCTT_PKID not like", value, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidIn(List<String> values) {
            addCriterion("SUBCTT_PKID in", values, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidNotIn(List<String> values) {
            addCriterion("SUBCTT_PKID not in", values, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidBetween(String value1, String value2) {
            addCriterion("SUBCTT_PKID between", value1, value2, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttPkidNotBetween(String value1, String value2) {
            addCriterion("SUBCTT_PKID not between", value1, value2, "subcttPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidIsNull() {
            addCriterion("SUBCTT_ITEM_PKID is null");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidIsNotNull() {
            addCriterion("SUBCTT_ITEM_PKID is not null");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidEqualTo(String value) {
            addCriterion("SUBCTT_ITEM_PKID =", value, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidNotEqualTo(String value) {
            addCriterion("SUBCTT_ITEM_PKID <>", value, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidGreaterThan(String value) {
            addCriterion("SUBCTT_ITEM_PKID >", value, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidGreaterThanOrEqualTo(String value) {
            addCriterion("SUBCTT_ITEM_PKID >=", value, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidLessThan(String value) {
            addCriterion("SUBCTT_ITEM_PKID <", value, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidLessThanOrEqualTo(String value) {
            addCriterion("SUBCTT_ITEM_PKID <=", value, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidLike(String value) {
            addCriterion("SUBCTT_ITEM_PKID like", value, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidNotLike(String value) {
            addCriterion("SUBCTT_ITEM_PKID not like", value, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidIn(List<String> values) {
            addCriterion("SUBCTT_ITEM_PKID in", values, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidNotIn(List<String> values) {
            addCriterion("SUBCTT_ITEM_PKID not in", values, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidBetween(String value1, String value2) {
            addCriterion("SUBCTT_ITEM_PKID between", value1, value2, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andSubcttItemPkidNotBetween(String value1, String value2) {
            addCriterion("SUBCTT_ITEM_PKID not between", value1, value2, "subcttItemPkid");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtIsNull() {
            addCriterion("ADD_UP_TO_AMT is null");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtIsNotNull() {
            addCriterion("ADD_UP_TO_AMT is not null");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtEqualTo(BigDecimal value) {
            addCriterion("ADD_UP_TO_AMT =", value, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtNotEqualTo(BigDecimal value) {
            addCriterion("ADD_UP_TO_AMT <>", value, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtGreaterThan(BigDecimal value) {
            addCriterion("ADD_UP_TO_AMT >", value, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ADD_UP_TO_AMT >=", value, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtLessThan(BigDecimal value) {
            addCriterion("ADD_UP_TO_AMT <", value, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ADD_UP_TO_AMT <=", value, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtIn(List<BigDecimal> values) {
            addCriterion("ADD_UP_TO_AMT in", values, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtNotIn(List<BigDecimal> values) {
            addCriterion("ADD_UP_TO_AMT not in", values, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ADD_UP_TO_AMT between", value1, value2, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andAddUpToAmtNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ADD_UP_TO_AMT not between", value1, value2, "addUpToAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtIsNull() {
            addCriterion("THIS_STAGE_AMT is null");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtIsNotNull() {
            addCriterion("THIS_STAGE_AMT is not null");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtEqualTo(BigDecimal value) {
            addCriterion("THIS_STAGE_AMT =", value, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtNotEqualTo(BigDecimal value) {
            addCriterion("THIS_STAGE_AMT <>", value, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtGreaterThan(BigDecimal value) {
            addCriterion("THIS_STAGE_AMT >", value, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("THIS_STAGE_AMT >=", value, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtLessThan(BigDecimal value) {
            addCriterion("THIS_STAGE_AMT <", value, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtLessThanOrEqualTo(BigDecimal value) {
            addCriterion("THIS_STAGE_AMT <=", value, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtIn(List<BigDecimal> values) {
            addCriterion("THIS_STAGE_AMT in", values, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtNotIn(List<BigDecimal> values) {
            addCriterion("THIS_STAGE_AMT not in", values, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("THIS_STAGE_AMT between", value1, value2, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andThisStageAmtNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("THIS_STAGE_AMT not between", value1, value2, "thisStageAmt");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagIsNull() {
            addCriterion("ARCHIVED_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagIsNotNull() {
            addCriterion("ARCHIVED_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagEqualTo(String value) {
            addCriterion("ARCHIVED_FLAG =", value, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagNotEqualTo(String value) {
            addCriterion("ARCHIVED_FLAG <>", value, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagGreaterThan(String value) {
            addCriterion("ARCHIVED_FLAG >", value, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagGreaterThanOrEqualTo(String value) {
            addCriterion("ARCHIVED_FLAG >=", value, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagLessThan(String value) {
            addCriterion("ARCHIVED_FLAG <", value, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagLessThanOrEqualTo(String value) {
            addCriterion("ARCHIVED_FLAG <=", value, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagLike(String value) {
            addCriterion("ARCHIVED_FLAG like", value, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagNotLike(String value) {
            addCriterion("ARCHIVED_FLAG not like", value, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagIn(List<String> values) {
            addCriterion("ARCHIVED_FLAG in", values, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagNotIn(List<String> values) {
            addCriterion("ARCHIVED_FLAG not in", values, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagBetween(String value1, String value2) {
            addCriterion("ARCHIVED_FLAG between", value1, value2, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andArchivedFlagNotBetween(String value1, String value2) {
            addCriterion("ARCHIVED_FLAG not between", value1, value2, "archivedFlag");
            return (Criteria) this;
        }

        public Criteria andCreatedByIsNull() {
            addCriterion("CREATED_BY is null");
            return (Criteria) this;
        }

        public Criteria andCreatedByIsNotNull() {
            addCriterion("CREATED_BY is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedByEqualTo(String value) {
            addCriterion("CREATED_BY =", value, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByNotEqualTo(String value) {
            addCriterion("CREATED_BY <>", value, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByGreaterThan(String value) {
            addCriterion("CREATED_BY >", value, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByGreaterThanOrEqualTo(String value) {
            addCriterion("CREATED_BY >=", value, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByLessThan(String value) {
            addCriterion("CREATED_BY <", value, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByLessThanOrEqualTo(String value) {
            addCriterion("CREATED_BY <=", value, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByLike(String value) {
            addCriterion("CREATED_BY like", value, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByNotLike(String value) {
            addCriterion("CREATED_BY not like", value, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByIn(List<String> values) {
            addCriterion("CREATED_BY in", values, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByNotIn(List<String> values) {
            addCriterion("CREATED_BY not in", values, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByBetween(String value1, String value2) {
            addCriterion("CREATED_BY between", value1, value2, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedByNotBetween(String value1, String value2) {
            addCriterion("CREATED_BY not between", value1, value2, "createdBy");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNull() {
            addCriterion("CREATED_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNotNull() {
            addCriterion("CREATED_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeEqualTo(String value) {
            addCriterion("CREATED_TIME =", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotEqualTo(String value) {
            addCriterion("CREATED_TIME <>", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThan(String value) {
            addCriterion("CREATED_TIME >", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThanOrEqualTo(String value) {
            addCriterion("CREATED_TIME >=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThan(String value) {
            addCriterion("CREATED_TIME <", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThanOrEqualTo(String value) {
            addCriterion("CREATED_TIME <=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLike(String value) {
            addCriterion("CREATED_TIME like", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotLike(String value) {
            addCriterion("CREATED_TIME not like", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIn(List<String> values) {
            addCriterion("CREATED_TIME in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotIn(List<String> values) {
            addCriterion("CREATED_TIME not in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeBetween(String value1, String value2) {
            addCriterion("CREATED_TIME between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotBetween(String value1, String value2) {
            addCriterion("CREATED_TIME not between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdByIsNull() {
            addCriterion("LAST_UPD_BY is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdByIsNotNull() {
            addCriterion("LAST_UPD_BY is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdByEqualTo(String value) {
            addCriterion("LAST_UPD_BY =", value, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByNotEqualTo(String value) {
            addCriterion("LAST_UPD_BY <>", value, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByGreaterThan(String value) {
            addCriterion("LAST_UPD_BY >", value, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByGreaterThanOrEqualTo(String value) {
            addCriterion("LAST_UPD_BY >=", value, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByLessThan(String value) {
            addCriterion("LAST_UPD_BY <", value, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByLessThanOrEqualTo(String value) {
            addCriterion("LAST_UPD_BY <=", value, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByLike(String value) {
            addCriterion("LAST_UPD_BY like", value, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByNotLike(String value) {
            addCriterion("LAST_UPD_BY not like", value, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByIn(List<String> values) {
            addCriterion("LAST_UPD_BY in", values, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByNotIn(List<String> values) {
            addCriterion("LAST_UPD_BY not in", values, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByBetween(String value1, String value2) {
            addCriterion("LAST_UPD_BY between", value1, value2, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdByNotBetween(String value1, String value2) {
            addCriterion("LAST_UPD_BY not between", value1, value2, "lastUpdBy");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeIsNull() {
            addCriterion("LAST_UPD_TIME is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeIsNotNull() {
            addCriterion("LAST_UPD_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeEqualTo(String value) {
            addCriterion("LAST_UPD_TIME =", value, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeNotEqualTo(String value) {
            addCriterion("LAST_UPD_TIME <>", value, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeGreaterThan(String value) {
            addCriterion("LAST_UPD_TIME >", value, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeGreaterThanOrEqualTo(String value) {
            addCriterion("LAST_UPD_TIME >=", value, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeLessThan(String value) {
            addCriterion("LAST_UPD_TIME <", value, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeLessThanOrEqualTo(String value) {
            addCriterion("LAST_UPD_TIME <=", value, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeLike(String value) {
            addCriterion("LAST_UPD_TIME like", value, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeNotLike(String value) {
            addCriterion("LAST_UPD_TIME not like", value, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeIn(List<String> values) {
            addCriterion("LAST_UPD_TIME in", values, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeNotIn(List<String> values) {
            addCriterion("LAST_UPD_TIME not in", values, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeBetween(String value1, String value2) {
            addCriterion("LAST_UPD_TIME between", value1, value2, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andLastUpdTimeNotBetween(String value1, String value2) {
            addCriterion("LAST_UPD_TIME not between", value1, value2, "lastUpdTime");
            return (Criteria) this;
        }

        public Criteria andRecVersionIsNull() {
            addCriterion("REC_VERSION is null");
            return (Criteria) this;
        }

        public Criteria andRecVersionIsNotNull() {
            addCriterion("REC_VERSION is not null");
            return (Criteria) this;
        }

        public Criteria andRecVersionEqualTo(Integer value) {
            addCriterion("REC_VERSION =", value, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionNotEqualTo(Integer value) {
            addCriterion("REC_VERSION <>", value, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionGreaterThan(Integer value) {
            addCriterion("REC_VERSION >", value, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("REC_VERSION >=", value, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionLessThan(Integer value) {
            addCriterion("REC_VERSION <", value, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionLessThanOrEqualTo(Integer value) {
            addCriterion("REC_VERSION <=", value, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionIn(List<Integer> values) {
            addCriterion("REC_VERSION in", values, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionNotIn(List<Integer> values) {
            addCriterion("REC_VERSION not in", values, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionBetween(Integer value1, Integer value2) {
            addCriterion("REC_VERSION between", value1, value2, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRecVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("REC_VERSION not between", value1, value2, "recVersion");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("REMARK is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("REMARK is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("REMARK =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("REMARK <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("REMARK >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("REMARK >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("REMARK <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("REMARK <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("REMARK like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("REMARK not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("REMARK in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("REMARK not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("REMARK between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("REMARK not between", value1, value2, "remark");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated do_not_delete_during_merge Wed Jan 28 14:28:53 CST 2015
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table prog_stl_item_sub_f
     *
     * @mbggenerated Wed Jan 28 14:28:53 CST 2015
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