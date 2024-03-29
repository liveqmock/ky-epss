package platform.repository.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CpSequenceExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public CpSequenceExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
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
     * This method corresponds to the database table CP_SEQUENCE
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
     * This method corresponds to the database table CP_SEQUENCE
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CP_SEQUENCE
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
     * This class corresponds to the database table CP_SEQUENCE
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

        public Criteria andSeqnameIsNull() {
            addCriterion("SEQNAME is null");
            return (Criteria) this;
        }

        public Criteria andSeqnameIsNotNull() {
            addCriterion("SEQNAME is not null");
            return (Criteria) this;
        }

        public Criteria andSeqnameEqualTo(String value) {
            addCriterion("SEQNAME =", value, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameNotEqualTo(String value) {
            addCriterion("SEQNAME <>", value, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameGreaterThan(String value) {
            addCriterion("SEQNAME >", value, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameGreaterThanOrEqualTo(String value) {
            addCriterion("SEQNAME >=", value, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameLessThan(String value) {
            addCriterion("SEQNAME <", value, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameLessThanOrEqualTo(String value) {
            addCriterion("SEQNAME <=", value, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameLike(String value) {
            addCriterion("SEQNAME like", value, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameNotLike(String value) {
            addCriterion("SEQNAME not like", value, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameIn(List<String> values) {
            addCriterion("SEQNAME in", values, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameNotIn(List<String> values) {
            addCriterion("SEQNAME not in", values, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameBetween(String value1, String value2) {
            addCriterion("SEQNAME between", value1, value2, "seqname");
            return (Criteria) this;
        }

        public Criteria andSeqnameNotBetween(String value1, String value2) {
            addCriterion("SEQNAME not between", value1, value2, "seqname");
            return (Criteria) this;
        }

        public Criteria andMinvalueIsNull() {
            addCriterion("MINVALUE is null");
            return (Criteria) this;
        }

        public Criteria andMinvalueIsNotNull() {
            addCriterion("MINVALUE is not null");
            return (Criteria) this;
        }

        public Criteria andMinvalueEqualTo(Long value) {
            addCriterion("MINVALUE =", value, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueNotEqualTo(Long value) {
            addCriterion("MINVALUE <>", value, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueGreaterThan(Long value) {
            addCriterion("MINVALUE >", value, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueGreaterThanOrEqualTo(Long value) {
            addCriterion("MINVALUE >=", value, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueLessThan(Long value) {
            addCriterion("MINVALUE <", value, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueLessThanOrEqualTo(Long value) {
            addCriterion("MINVALUE <=", value, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueIn(List<Long> values) {
            addCriterion("MINVALUE in", values, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueNotIn(List<Long> values) {
            addCriterion("MINVALUE not in", values, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueBetween(Long value1, Long value2) {
            addCriterion("MINVALUE between", value1, value2, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMinvalueNotBetween(Long value1, Long value2) {
            addCriterion("MINVALUE not between", value1, value2, "minvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueIsNull() {
            addCriterion("MAXVALUE is null");
            return (Criteria) this;
        }

        public Criteria andMaxvalueIsNotNull() {
            addCriterion("MAXVALUE is not null");
            return (Criteria) this;
        }

        public Criteria andMaxvalueEqualTo(Long value) {
            addCriterion("MAXVALUE =", value, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueNotEqualTo(Long value) {
            addCriterion("MAXVALUE <>", value, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueGreaterThan(Long value) {
            addCriterion("MAXVALUE >", value, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueGreaterThanOrEqualTo(Long value) {
            addCriterion("MAXVALUE >=", value, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueLessThan(Long value) {
            addCriterion("MAXVALUE <", value, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueLessThanOrEqualTo(Long value) {
            addCriterion("MAXVALUE <=", value, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueIn(List<Long> values) {
            addCriterion("MAXVALUE in", values, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueNotIn(List<Long> values) {
            addCriterion("MAXVALUE not in", values, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueBetween(Long value1, Long value2) {
            addCriterion("MAXVALUE between", value1, value2, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andMaxvalueNotBetween(Long value1, Long value2) {
            addCriterion("MAXVALUE not between", value1, value2, "maxvalue");
            return (Criteria) this;
        }

        public Criteria andCycleIsNull() {
            addCriterion("CYCLE is null");
            return (Criteria) this;
        }

        public Criteria andCycleIsNotNull() {
            addCriterion("CYCLE is not null");
            return (Criteria) this;
        }

        public Criteria andCycleEqualTo(String value) {
            addCriterion("CYCLE =", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleNotEqualTo(String value) {
            addCriterion("CYCLE <>", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleGreaterThan(String value) {
            addCriterion("CYCLE >", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleGreaterThanOrEqualTo(String value) {
            addCriterion("CYCLE >=", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleLessThan(String value) {
            addCriterion("CYCLE <", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleLessThanOrEqualTo(String value) {
            addCriterion("CYCLE <=", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleLike(String value) {
            addCriterion("CYCLE like", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleNotLike(String value) {
            addCriterion("CYCLE not like", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleIn(List<String> values) {
            addCriterion("CYCLE in", values, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleNotIn(List<String> values) {
            addCriterion("CYCLE not in", values, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleBetween(String value1, String value2) {
            addCriterion("CYCLE between", value1, value2, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleNotBetween(String value1, String value2) {
            addCriterion("CYCLE not between", value1, value2, "cycle");
            return (Criteria) this;
        }

        public Criteria andStepvalueIsNull() {
            addCriterion("STEPVALUE is null");
            return (Criteria) this;
        }

        public Criteria andStepvalueIsNotNull() {
            addCriterion("STEPVALUE is not null");
            return (Criteria) this;
        }

        public Criteria andStepvalueEqualTo(BigDecimal value) {
            addCriterion("STEPVALUE =", value, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueNotEqualTo(BigDecimal value) {
            addCriterion("STEPVALUE <>", value, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueGreaterThan(BigDecimal value) {
            addCriterion("STEPVALUE >", value, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("STEPVALUE >=", value, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueLessThan(BigDecimal value) {
            addCriterion("STEPVALUE <", value, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("STEPVALUE <=", value, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueIn(List<BigDecimal> values) {
            addCriterion("STEPVALUE in", values, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueNotIn(List<BigDecimal> values) {
            addCriterion("STEPVALUE not in", values, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("STEPVALUE between", value1, value2, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andStepvalueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("STEPVALUE not between", value1, value2, "stepvalue");
            return (Criteria) this;
        }

        public Criteria andCacheIsNull() {
            addCriterion("CACHE is null");
            return (Criteria) this;
        }

        public Criteria andCacheIsNotNull() {
            addCriterion("CACHE is not null");
            return (Criteria) this;
        }

        public Criteria andCacheEqualTo(BigDecimal value) {
            addCriterion("CACHE =", value, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheNotEqualTo(BigDecimal value) {
            addCriterion("CACHE <>", value, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheGreaterThan(BigDecimal value) {
            addCriterion("CACHE >", value, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("CACHE >=", value, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheLessThan(BigDecimal value) {
            addCriterion("CACHE <", value, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheLessThanOrEqualTo(BigDecimal value) {
            addCriterion("CACHE <=", value, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheIn(List<BigDecimal> values) {
            addCriterion("CACHE in", values, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheNotIn(List<BigDecimal> values) {
            addCriterion("CACHE not in", values, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CACHE between", value1, value2, "cache");
            return (Criteria) this;
        }

        public Criteria andCacheNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CACHE not between", value1, value2, "cache");
            return (Criteria) this;
        }

        public Criteria andYearIsNull() {
            addCriterion("YEAR is null");
            return (Criteria) this;
        }

        public Criteria andYearIsNotNull() {
            addCriterion("YEAR is not null");
            return (Criteria) this;
        }

        public Criteria andYearEqualTo(String value) {
            addCriterion("YEAR =", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotEqualTo(String value) {
            addCriterion("YEAR <>", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearGreaterThan(String value) {
            addCriterion("YEAR >", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearGreaterThanOrEqualTo(String value) {
            addCriterion("YEAR >=", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLessThan(String value) {
            addCriterion("YEAR <", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLessThanOrEqualTo(String value) {
            addCriterion("YEAR <=", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLike(String value) {
            addCriterion("YEAR like", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotLike(String value) {
            addCriterion("YEAR not like", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearIn(List<String> values) {
            addCriterion("YEAR in", values, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotIn(List<String> values) {
            addCriterion("YEAR not in", values, "year");
            return (Criteria) this;
        }

        public Criteria andYearBetween(String value1, String value2) {
            addCriterion("YEAR between", value1, value2, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotBetween(String value1, String value2) {
            addCriterion("YEAR not between", value1, value2, "year");
            return (Criteria) this;
        }

        public Criteria andCurvalueIsNull() {
            addCriterion("CURVALUE is null");
            return (Criteria) this;
        }

        public Criteria andCurvalueIsNotNull() {
            addCriterion("CURVALUE is not null");
            return (Criteria) this;
        }

        public Criteria andCurvalueEqualTo(Long value) {
            addCriterion("CURVALUE =", value, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueNotEqualTo(Long value) {
            addCriterion("CURVALUE <>", value, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueGreaterThan(Long value) {
            addCriterion("CURVALUE >", value, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueGreaterThanOrEqualTo(Long value) {
            addCriterion("CURVALUE >=", value, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueLessThan(Long value) {
            addCriterion("CURVALUE <", value, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueLessThanOrEqualTo(Long value) {
            addCriterion("CURVALUE <=", value, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueIn(List<Long> values) {
            addCriterion("CURVALUE in", values, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueNotIn(List<Long> values) {
            addCriterion("CURVALUE not in", values, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueBetween(Long value1, Long value2) {
            addCriterion("CURVALUE between", value1, value2, "curvalue");
            return (Criteria) this;
        }

        public Criteria andCurvalueNotBetween(Long value1, Long value2) {
            addCriterion("CURVALUE not between", value1, value2, "curvalue");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table CP_SEQUENCE
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
     * This class corresponds to the database table CP_SEQUENCE
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