package epss.repository.model;

import java.util.ArrayList;
import java.util.List;

public class EsInitPowerExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public EsInitPowerExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
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
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
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

        public Criteria andLastUpdDateIsNull() {
            addCriterion("LAST_UPD_DATE is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateIsNotNull() {
            addCriterion("LAST_UPD_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateEqualTo(String value) {
            addCriterion("LAST_UPD_DATE =", value, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateNotEqualTo(String value) {
            addCriterion("LAST_UPD_DATE <>", value, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateGreaterThan(String value) {
            addCriterion("LAST_UPD_DATE >", value, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateGreaterThanOrEqualTo(String value) {
            addCriterion("LAST_UPD_DATE >=", value, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateLessThan(String value) {
            addCriterion("LAST_UPD_DATE <", value, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateLessThanOrEqualTo(String value) {
            addCriterion("LAST_UPD_DATE <=", value, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateLike(String value) {
            addCriterion("LAST_UPD_DATE like", value, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateNotLike(String value) {
            addCriterion("LAST_UPD_DATE not like", value, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateIn(List<String> values) {
            addCriterion("LAST_UPD_DATE in", values, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateNotIn(List<String> values) {
            addCriterion("LAST_UPD_DATE not in", values, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateBetween(String value1, String value2) {
            addCriterion("LAST_UPD_DATE between", value1, value2, "lastUpdDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdDateNotBetween(String value1, String value2) {
            addCriterion("LAST_UPD_DATE not between", value1, value2, "lastUpdDate");
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

        public Criteria andNoteIsNull() {
            addCriterion("NOTE is null");
            return (Criteria) this;
        }

        public Criteria andNoteIsNotNull() {
            addCriterion("NOTE is not null");
            return (Criteria) this;
        }

        public Criteria andNoteEqualTo(String value) {
            addCriterion("NOTE =", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotEqualTo(String value) {
            addCriterion("NOTE <>", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteGreaterThan(String value) {
            addCriterion("NOTE >", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteGreaterThanOrEqualTo(String value) {
            addCriterion("NOTE >=", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLessThan(String value) {
            addCriterion("NOTE <", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLessThanOrEqualTo(String value) {
            addCriterion("NOTE <=", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLike(String value) {
            addCriterion("NOTE like", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotLike(String value) {
            addCriterion("NOTE not like", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteIn(List<String> values) {
            addCriterion("NOTE in", values, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotIn(List<String> values) {
            addCriterion("NOTE not in", values, "note");
            return (Criteria) this;
        }

        public Criteria andNoteBetween(String value1, String value2) {
            addCriterion("NOTE between", value1, value2, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotBetween(String value1, String value2) {
            addCriterion("NOTE not between", value1, value2, "note");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated do_not_delete_during_merge Mon Jul 14 15:00:00 CST 2014
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.ES_INIT_POWER
     *
     * @mbggenerated Mon Jul 14 15:00:00 CST 2014
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