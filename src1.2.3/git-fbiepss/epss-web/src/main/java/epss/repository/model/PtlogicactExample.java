package epss.repository.model;

import java.util.ArrayList;
import java.util.List;

public class PtlogicactExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public PtlogicactExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
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
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
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

        public Criteria andLogiccodeIsNull() {
            addCriterion("LOGICCODE is null");
            return (Criteria) this;
        }

        public Criteria andLogiccodeIsNotNull() {
            addCriterion("LOGICCODE is not null");
            return (Criteria) this;
        }

        public Criteria andLogiccodeEqualTo(String value) {
            addCriterion("LOGICCODE =", value, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeNotEqualTo(String value) {
            addCriterion("LOGICCODE <>", value, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeGreaterThan(String value) {
            addCriterion("LOGICCODE >", value, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeGreaterThanOrEqualTo(String value) {
            addCriterion("LOGICCODE >=", value, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeLessThan(String value) {
            addCriterion("LOGICCODE <", value, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeLessThanOrEqualTo(String value) {
            addCriterion("LOGICCODE <=", value, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeLike(String value) {
            addCriterion("LOGICCODE like", value, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeNotLike(String value) {
            addCriterion("LOGICCODE not like", value, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeIn(List<String> values) {
            addCriterion("LOGICCODE in", values, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeNotIn(List<String> values) {
            addCriterion("LOGICCODE not in", values, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeBetween(String value1, String value2) {
            addCriterion("LOGICCODE between", value1, value2, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogiccodeNotBetween(String value1, String value2) {
            addCriterion("LOGICCODE not between", value1, value2, "logiccode");
            return (Criteria) this;
        }

        public Criteria andLogicclassIsNull() {
            addCriterion("LOGICCLASS is null");
            return (Criteria) this;
        }

        public Criteria andLogicclassIsNotNull() {
            addCriterion("LOGICCLASS is not null");
            return (Criteria) this;
        }

        public Criteria andLogicclassEqualTo(String value) {
            addCriterion("LOGICCLASS =", value, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassNotEqualTo(String value) {
            addCriterion("LOGICCLASS <>", value, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassGreaterThan(String value) {
            addCriterion("LOGICCLASS >", value, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassGreaterThanOrEqualTo(String value) {
            addCriterion("LOGICCLASS >=", value, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassLessThan(String value) {
            addCriterion("LOGICCLASS <", value, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassLessThanOrEqualTo(String value) {
            addCriterion("LOGICCLASS <=", value, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassLike(String value) {
            addCriterion("LOGICCLASS like", value, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassNotLike(String value) {
            addCriterion("LOGICCLASS not like", value, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassIn(List<String> values) {
            addCriterion("LOGICCLASS in", values, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassNotIn(List<String> values) {
            addCriterion("LOGICCLASS not in", values, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassBetween(String value1, String value2) {
            addCriterion("LOGICCLASS between", value1, value2, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicclassNotBetween(String value1, String value2) {
            addCriterion("LOGICCLASS not between", value1, value2, "logicclass");
            return (Criteria) this;
        }

        public Criteria andLogicmethodIsNull() {
            addCriterion("LOGICMETHOD is null");
            return (Criteria) this;
        }

        public Criteria andLogicmethodIsNotNull() {
            addCriterion("LOGICMETHOD is not null");
            return (Criteria) this;
        }

        public Criteria andLogicmethodEqualTo(String value) {
            addCriterion("LOGICMETHOD =", value, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodNotEqualTo(String value) {
            addCriterion("LOGICMETHOD <>", value, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodGreaterThan(String value) {
            addCriterion("LOGICMETHOD >", value, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodGreaterThanOrEqualTo(String value) {
            addCriterion("LOGICMETHOD >=", value, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodLessThan(String value) {
            addCriterion("LOGICMETHOD <", value, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodLessThanOrEqualTo(String value) {
            addCriterion("LOGICMETHOD <=", value, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodLike(String value) {
            addCriterion("LOGICMETHOD like", value, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodNotLike(String value) {
            addCriterion("LOGICMETHOD not like", value, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodIn(List<String> values) {
            addCriterion("LOGICMETHOD in", values, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodNotIn(List<String> values) {
            addCriterion("LOGICMETHOD not in", values, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodBetween(String value1, String value2) {
            addCriterion("LOGICMETHOD between", value1, value2, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicmethodNotBetween(String value1, String value2) {
            addCriterion("LOGICMETHOD not between", value1, value2, "logicmethod");
            return (Criteria) this;
        }

        public Criteria andLogicdescIsNull() {
            addCriterion("LOGICDESC is null");
            return (Criteria) this;
        }

        public Criteria andLogicdescIsNotNull() {
            addCriterion("LOGICDESC is not null");
            return (Criteria) this;
        }

        public Criteria andLogicdescEqualTo(String value) {
            addCriterion("LOGICDESC =", value, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescNotEqualTo(String value) {
            addCriterion("LOGICDESC <>", value, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescGreaterThan(String value) {
            addCriterion("LOGICDESC >", value, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescGreaterThanOrEqualTo(String value) {
            addCriterion("LOGICDESC >=", value, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescLessThan(String value) {
            addCriterion("LOGICDESC <", value, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescLessThanOrEqualTo(String value) {
            addCriterion("LOGICDESC <=", value, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescLike(String value) {
            addCriterion("LOGICDESC like", value, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescNotLike(String value) {
            addCriterion("LOGICDESC not like", value, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescIn(List<String> values) {
            addCriterion("LOGICDESC in", values, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescNotIn(List<String> values) {
            addCriterion("LOGICDESC not in", values, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescBetween(String value1, String value2) {
            addCriterion("LOGICDESC between", value1, value2, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicdescNotBetween(String value1, String value2) {
            addCriterion("LOGICDESC not between", value1, value2, "logicdesc");
            return (Criteria) this;
        }

        public Criteria andLogicenabledIsNull() {
            addCriterion("LOGICENABLED is null");
            return (Criteria) this;
        }

        public Criteria andLogicenabledIsNotNull() {
            addCriterion("LOGICENABLED is not null");
            return (Criteria) this;
        }

        public Criteria andLogicenabledEqualTo(String value) {
            addCriterion("LOGICENABLED =", value, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledNotEqualTo(String value) {
            addCriterion("LOGICENABLED <>", value, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledGreaterThan(String value) {
            addCriterion("LOGICENABLED >", value, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledGreaterThanOrEqualTo(String value) {
            addCriterion("LOGICENABLED >=", value, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledLessThan(String value) {
            addCriterion("LOGICENABLED <", value, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledLessThanOrEqualTo(String value) {
            addCriterion("LOGICENABLED <=", value, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledLike(String value) {
            addCriterion("LOGICENABLED like", value, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledNotLike(String value) {
            addCriterion("LOGICENABLED not like", value, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledIn(List<String> values) {
            addCriterion("LOGICENABLED in", values, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledNotIn(List<String> values) {
            addCriterion("LOGICENABLED not in", values, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledBetween(String value1, String value2) {
            addCriterion("LOGICENABLED between", value1, value2, "logicenabled");
            return (Criteria) this;
        }

        public Criteria andLogicenabledNotBetween(String value1, String value2) {
            addCriterion("LOGICENABLED not between", value1, value2, "logicenabled");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PTLOGICACT
     *
     * @mbggenerated do_not_delete_during_merge Fri Aug 22 09:11:21 CST 2014
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PTLOGICACT
     *
     * @mbggenerated Fri Aug 22 09:11:21 CST 2014
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