package epss.repository.model;

import java.util.ArrayList;
import java.util.List;

public class EsInitPowerHisExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public EsInitPowerHisExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
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
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
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

        public Criteria andPowerTypeIsNull() {
            addCriterion("POWER_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andPowerTypeIsNotNull() {
            addCriterion("POWER_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andPowerTypeEqualTo(String value) {
            addCriterion("POWER_TYPE =", value, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeNotEqualTo(String value) {
            addCriterion("POWER_TYPE <>", value, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeGreaterThan(String value) {
            addCriterion("POWER_TYPE >", value, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeGreaterThanOrEqualTo(String value) {
            addCriterion("POWER_TYPE >=", value, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeLessThan(String value) {
            addCriterion("POWER_TYPE <", value, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeLessThanOrEqualTo(String value) {
            addCriterion("POWER_TYPE <=", value, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeLike(String value) {
            addCriterion("POWER_TYPE like", value, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeNotLike(String value) {
            addCriterion("POWER_TYPE not like", value, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeIn(List<String> values) {
            addCriterion("POWER_TYPE in", values, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeNotIn(List<String> values) {
            addCriterion("POWER_TYPE not in", values, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeBetween(String value1, String value2) {
            addCriterion("POWER_TYPE between", value1, value2, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerTypeNotBetween(String value1, String value2) {
            addCriterion("POWER_TYPE not between", value1, value2, "powerType");
            return (Criteria) this;
        }

        public Criteria andPowerPkidIsNull() {
            addCriterion("POWER_PKID is null");
            return (Criteria) this;
        }

        public Criteria andPowerPkidIsNotNull() {
            addCriterion("POWER_PKID is not null");
            return (Criteria) this;
        }

        public Criteria andPowerPkidEqualTo(String value) {
            addCriterion("POWER_PKID =", value, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidNotEqualTo(String value) {
            addCriterion("POWER_PKID <>", value, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidGreaterThan(String value) {
            addCriterion("POWER_PKID >", value, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidGreaterThanOrEqualTo(String value) {
            addCriterion("POWER_PKID >=", value, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidLessThan(String value) {
            addCriterion("POWER_PKID <", value, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidLessThanOrEqualTo(String value) {
            addCriterion("POWER_PKID <=", value, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidLike(String value) {
            addCriterion("POWER_PKID like", value, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidNotLike(String value) {
            addCriterion("POWER_PKID not like", value, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidIn(List<String> values) {
            addCriterion("POWER_PKID in", values, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidNotIn(List<String> values) {
            addCriterion("POWER_PKID not in", values, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidBetween(String value1, String value2) {
            addCriterion("POWER_PKID between", value1, value2, "powerPkid");
            return (Criteria) this;
        }

        public Criteria andPowerPkidNotBetween(String value1, String value2) {
            addCriterion("POWER_PKID not between", value1, value2, "powerPkid");
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

        public Criteria andStatusFlagIsNull() {
            addCriterion("STATUS_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andStatusFlagIsNotNull() {
            addCriterion("STATUS_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andStatusFlagEqualTo(String value) {
            addCriterion("STATUS_FLAG =", value, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagNotEqualTo(String value) {
            addCriterion("STATUS_FLAG <>", value, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagGreaterThan(String value) {
            addCriterion("STATUS_FLAG >", value, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagGreaterThanOrEqualTo(String value) {
            addCriterion("STATUS_FLAG >=", value, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagLessThan(String value) {
            addCriterion("STATUS_FLAG <", value, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagLessThanOrEqualTo(String value) {
            addCriterion("STATUS_FLAG <=", value, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagLike(String value) {
            addCriterion("STATUS_FLAG like", value, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagNotLike(String value) {
            addCriterion("STATUS_FLAG not like", value, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagIn(List<String> values) {
            addCriterion("STATUS_FLAG in", values, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagNotIn(List<String> values) {
            addCriterion("STATUS_FLAG not in", values, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagBetween(String value1, String value2) {
            addCriterion("STATUS_FLAG between", value1, value2, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andStatusFlagNotBetween(String value1, String value2) {
            addCriterion("STATUS_FLAG not between", value1, value2, "statusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagIsNull() {
            addCriterion("PRE_STATUS_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagIsNotNull() {
            addCriterion("PRE_STATUS_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagEqualTo(String value) {
            addCriterion("PRE_STATUS_FLAG =", value, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagNotEqualTo(String value) {
            addCriterion("PRE_STATUS_FLAG <>", value, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagGreaterThan(String value) {
            addCriterion("PRE_STATUS_FLAG >", value, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagGreaterThanOrEqualTo(String value) {
            addCriterion("PRE_STATUS_FLAG >=", value, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagLessThan(String value) {
            addCriterion("PRE_STATUS_FLAG <", value, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagLessThanOrEqualTo(String value) {
            addCriterion("PRE_STATUS_FLAG <=", value, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagLike(String value) {
            addCriterion("PRE_STATUS_FLAG like", value, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagNotLike(String value) {
            addCriterion("PRE_STATUS_FLAG not like", value, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagIn(List<String> values) {
            addCriterion("PRE_STATUS_FLAG in", values, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagNotIn(List<String> values) {
            addCriterion("PRE_STATUS_FLAG not in", values, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagBetween(String value1, String value2) {
            addCriterion("PRE_STATUS_FLAG between", value1, value2, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andPreStatusFlagNotBetween(String value1, String value2) {
            addCriterion("PRE_STATUS_FLAG not between", value1, value2, "preStatusFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagIsNull() {
            addCriterion("DELETED_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagIsNotNull() {
            addCriterion("DELETED_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagEqualTo(String value) {
            addCriterion("DELETED_FLAG =", value, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagNotEqualTo(String value) {
            addCriterion("DELETED_FLAG <>", value, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagGreaterThan(String value) {
            addCriterion("DELETED_FLAG >", value, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagGreaterThanOrEqualTo(String value) {
            addCriterion("DELETED_FLAG >=", value, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagLessThan(String value) {
            addCriterion("DELETED_FLAG <", value, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagLessThanOrEqualTo(String value) {
            addCriterion("DELETED_FLAG <=", value, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagLike(String value) {
            addCriterion("DELETED_FLAG like", value, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagNotLike(String value) {
            addCriterion("DELETED_FLAG not like", value, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagIn(List<String> values) {
            addCriterion("DELETED_FLAG in", values, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagNotIn(List<String> values) {
            addCriterion("DELETED_FLAG not in", values, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagBetween(String value1, String value2) {
            addCriterion("DELETED_FLAG between", value1, value2, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andDeletedFlagNotBetween(String value1, String value2) {
            addCriterion("DELETED_FLAG not between", value1, value2, "deletedFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagIsNull() {
            addCriterion("END_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andEndFlagIsNotNull() {
            addCriterion("END_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andEndFlagEqualTo(String value) {
            addCriterion("END_FLAG =", value, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagNotEqualTo(String value) {
            addCriterion("END_FLAG <>", value, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagGreaterThan(String value) {
            addCriterion("END_FLAG >", value, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagGreaterThanOrEqualTo(String value) {
            addCriterion("END_FLAG >=", value, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagLessThan(String value) {
            addCriterion("END_FLAG <", value, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagLessThanOrEqualTo(String value) {
            addCriterion("END_FLAG <=", value, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagLike(String value) {
            addCriterion("END_FLAG like", value, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagNotLike(String value) {
            addCriterion("END_FLAG not like", value, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagIn(List<String> values) {
            addCriterion("END_FLAG in", values, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagNotIn(List<String> values) {
            addCriterion("END_FLAG not in", values, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagBetween(String value1, String value2) {
            addCriterion("END_FLAG between", value1, value2, "endFlag");
            return (Criteria) this;
        }

        public Criteria andEndFlagNotBetween(String value1, String value2) {
            addCriterion("END_FLAG not between", value1, value2, "endFlag");
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

        public Criteria andCreatedDateIsNull() {
            addCriterion("CREATED_DATE is null");
            return (Criteria) this;
        }

        public Criteria andCreatedDateIsNotNull() {
            addCriterion("CREATED_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedDateEqualTo(String value) {
            addCriterion("CREATED_DATE =", value, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateNotEqualTo(String value) {
            addCriterion("CREATED_DATE <>", value, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateGreaterThan(String value) {
            addCriterion("CREATED_DATE >", value, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateGreaterThanOrEqualTo(String value) {
            addCriterion("CREATED_DATE >=", value, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateLessThan(String value) {
            addCriterion("CREATED_DATE <", value, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateLessThanOrEqualTo(String value) {
            addCriterion("CREATED_DATE <=", value, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateLike(String value) {
            addCriterion("CREATED_DATE like", value, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateNotLike(String value) {
            addCriterion("CREATED_DATE not like", value, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateIn(List<String> values) {
            addCriterion("CREATED_DATE in", values, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateNotIn(List<String> values) {
            addCriterion("CREATED_DATE not in", values, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateBetween(String value1, String value2) {
            addCriterion("CREATED_DATE between", value1, value2, "createdDate");
            return (Criteria) this;
        }

        public Criteria andCreatedDateNotBetween(String value1, String value2) {
            addCriterion("CREATED_DATE not between", value1, value2, "createdDate");
            return (Criteria) this;
        }

        public Criteria andSpareFieldIsNull() {
            addCriterion("SPARE_FIELD is null");
            return (Criteria) this;
        }

        public Criteria andSpareFieldIsNotNull() {
            addCriterion("SPARE_FIELD is not null");
            return (Criteria) this;
        }

        public Criteria andSpareFieldEqualTo(String value) {
            addCriterion("SPARE_FIELD =", value, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldNotEqualTo(String value) {
            addCriterion("SPARE_FIELD <>", value, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldGreaterThan(String value) {
            addCriterion("SPARE_FIELD >", value, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldGreaterThanOrEqualTo(String value) {
            addCriterion("SPARE_FIELD >=", value, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldLessThan(String value) {
            addCriterion("SPARE_FIELD <", value, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldLessThanOrEqualTo(String value) {
            addCriterion("SPARE_FIELD <=", value, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldLike(String value) {
            addCriterion("SPARE_FIELD like", value, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldNotLike(String value) {
            addCriterion("SPARE_FIELD not like", value, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldIn(List<String> values) {
            addCriterion("SPARE_FIELD in", values, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldNotIn(List<String> values) {
            addCriterion("SPARE_FIELD not in", values, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldBetween(String value1, String value2) {
            addCriterion("SPARE_FIELD between", value1, value2, "spareField");
            return (Criteria) this;
        }

        public Criteria andSpareFieldNotBetween(String value1, String value2) {
            addCriterion("SPARE_FIELD not between", value1, value2, "spareField");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated do_not_delete_during_merge Fri Apr 25 17:26:31 CST 2014
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.ES_INIT_POWER_HIS
     *
     * @mbggenerated Fri Apr 25 17:26:31 CST 2014
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