package skyline.repository.model;

import java.util.ArrayList;
import java.util.List;

public class PtresourceExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public PtresourceExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
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
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
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

        public Criteria andResidIsNull() {
            addCriterion("RESID is null");
            return (Criteria) this;
        }

        public Criteria andResidIsNotNull() {
            addCriterion("RESID is not null");
            return (Criteria) this;
        }

        public Criteria andResidEqualTo(String value) {
            addCriterion("RESID =", value, "resid");
            return (Criteria) this;
        }

        public Criteria andResidNotEqualTo(String value) {
            addCriterion("RESID <>", value, "resid");
            return (Criteria) this;
        }

        public Criteria andResidGreaterThan(String value) {
            addCriterion("RESID >", value, "resid");
            return (Criteria) this;
        }

        public Criteria andResidGreaterThanOrEqualTo(String value) {
            addCriterion("RESID >=", value, "resid");
            return (Criteria) this;
        }

        public Criteria andResidLessThan(String value) {
            addCriterion("RESID <", value, "resid");
            return (Criteria) this;
        }

        public Criteria andResidLessThanOrEqualTo(String value) {
            addCriterion("RESID <=", value, "resid");
            return (Criteria) this;
        }

        public Criteria andResidLike(String value) {
            addCriterion("RESID like", value, "resid");
            return (Criteria) this;
        }

        public Criteria andResidNotLike(String value) {
            addCriterion("RESID not like", value, "resid");
            return (Criteria) this;
        }

        public Criteria andResidIn(List<String> values) {
            addCriterion("RESID in", values, "resid");
            return (Criteria) this;
        }

        public Criteria andResidNotIn(List<String> values) {
            addCriterion("RESID not in", values, "resid");
            return (Criteria) this;
        }

        public Criteria andResidBetween(String value1, String value2) {
            addCriterion("RESID between", value1, value2, "resid");
            return (Criteria) this;
        }

        public Criteria andResidNotBetween(String value1, String value2) {
            addCriterion("RESID not between", value1, value2, "resid");
            return (Criteria) this;
        }

        public Criteria andParentresidIsNull() {
            addCriterion("PARENTRESID is null");
            return (Criteria) this;
        }

        public Criteria andParentresidIsNotNull() {
            addCriterion("PARENTRESID is not null");
            return (Criteria) this;
        }

        public Criteria andParentresidEqualTo(String value) {
            addCriterion("PARENTRESID =", value, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidNotEqualTo(String value) {
            addCriterion("PARENTRESID <>", value, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidGreaterThan(String value) {
            addCriterion("PARENTRESID >", value, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidGreaterThanOrEqualTo(String value) {
            addCriterion("PARENTRESID >=", value, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidLessThan(String value) {
            addCriterion("PARENTRESID <", value, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidLessThanOrEqualTo(String value) {
            addCriterion("PARENTRESID <=", value, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidLike(String value) {
            addCriterion("PARENTRESID like", value, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidNotLike(String value) {
            addCriterion("PARENTRESID not like", value, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidIn(List<String> values) {
            addCriterion("PARENTRESID in", values, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidNotIn(List<String> values) {
            addCriterion("PARENTRESID not in", values, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidBetween(String value1, String value2) {
            addCriterion("PARENTRESID between", value1, value2, "parentresid");
            return (Criteria) this;
        }

        public Criteria andParentresidNotBetween(String value1, String value2) {
            addCriterion("PARENTRESID not between", value1, value2, "parentresid");
            return (Criteria) this;
        }

        public Criteria andResnameIsNull() {
            addCriterion("RESNAME is null");
            return (Criteria) this;
        }

        public Criteria andResnameIsNotNull() {
            addCriterion("RESNAME is not null");
            return (Criteria) this;
        }

        public Criteria andResnameEqualTo(String value) {
            addCriterion("RESNAME =", value, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameNotEqualTo(String value) {
            addCriterion("RESNAME <>", value, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameGreaterThan(String value) {
            addCriterion("RESNAME >", value, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameGreaterThanOrEqualTo(String value) {
            addCriterion("RESNAME >=", value, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameLessThan(String value) {
            addCriterion("RESNAME <", value, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameLessThanOrEqualTo(String value) {
            addCriterion("RESNAME <=", value, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameLike(String value) {
            addCriterion("RESNAME like", value, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameNotLike(String value) {
            addCriterion("RESNAME not like", value, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameIn(List<String> values) {
            addCriterion("RESNAME in", values, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameNotIn(List<String> values) {
            addCriterion("RESNAME not in", values, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameBetween(String value1, String value2) {
            addCriterion("RESNAME between", value1, value2, "resname");
            return (Criteria) this;
        }

        public Criteria andResnameNotBetween(String value1, String value2) {
            addCriterion("RESNAME not between", value1, value2, "resname");
            return (Criteria) this;
        }

        public Criteria andRestypeIsNull() {
            addCriterion("RESTYPE is null");
            return (Criteria) this;
        }

        public Criteria andRestypeIsNotNull() {
            addCriterion("RESTYPE is not null");
            return (Criteria) this;
        }

        public Criteria andRestypeEqualTo(String value) {
            addCriterion("RESTYPE =", value, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeNotEqualTo(String value) {
            addCriterion("RESTYPE <>", value, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeGreaterThan(String value) {
            addCriterion("RESTYPE >", value, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeGreaterThanOrEqualTo(String value) {
            addCriterion("RESTYPE >=", value, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeLessThan(String value) {
            addCriterion("RESTYPE <", value, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeLessThanOrEqualTo(String value) {
            addCriterion("RESTYPE <=", value, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeLike(String value) {
            addCriterion("RESTYPE like", value, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeNotLike(String value) {
            addCriterion("RESTYPE not like", value, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeIn(List<String> values) {
            addCriterion("RESTYPE in", values, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeNotIn(List<String> values) {
            addCriterion("RESTYPE not in", values, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeBetween(String value1, String value2) {
            addCriterion("RESTYPE between", value1, value2, "restype");
            return (Criteria) this;
        }

        public Criteria andRestypeNotBetween(String value1, String value2) {
            addCriterion("RESTYPE not between", value1, value2, "restype");
            return (Criteria) this;
        }

        public Criteria andResenabledIsNull() {
            addCriterion("RESENABLED is null");
            return (Criteria) this;
        }

        public Criteria andResenabledIsNotNull() {
            addCriterion("RESENABLED is not null");
            return (Criteria) this;
        }

        public Criteria andResenabledEqualTo(String value) {
            addCriterion("RESENABLED =", value, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledNotEqualTo(String value) {
            addCriterion("RESENABLED <>", value, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledGreaterThan(String value) {
            addCriterion("RESENABLED >", value, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledGreaterThanOrEqualTo(String value) {
            addCriterion("RESENABLED >=", value, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledLessThan(String value) {
            addCriterion("RESENABLED <", value, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledLessThanOrEqualTo(String value) {
            addCriterion("RESENABLED <=", value, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledLike(String value) {
            addCriterion("RESENABLED like", value, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledNotLike(String value) {
            addCriterion("RESENABLED not like", value, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledIn(List<String> values) {
            addCriterion("RESENABLED in", values, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledNotIn(List<String> values) {
            addCriterion("RESENABLED not in", values, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledBetween(String value1, String value2) {
            addCriterion("RESENABLED between", value1, value2, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResenabledNotBetween(String value1, String value2) {
            addCriterion("RESENABLED not between", value1, value2, "resenabled");
            return (Criteria) this;
        }

        public Criteria andResdescIsNull() {
            addCriterion("RESDESC is null");
            return (Criteria) this;
        }

        public Criteria andResdescIsNotNull() {
            addCriterion("RESDESC is not null");
            return (Criteria) this;
        }

        public Criteria andResdescEqualTo(String value) {
            addCriterion("RESDESC =", value, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescNotEqualTo(String value) {
            addCriterion("RESDESC <>", value, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescGreaterThan(String value) {
            addCriterion("RESDESC >", value, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescGreaterThanOrEqualTo(String value) {
            addCriterion("RESDESC >=", value, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescLessThan(String value) {
            addCriterion("RESDESC <", value, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescLessThanOrEqualTo(String value) {
            addCriterion("RESDESC <=", value, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescLike(String value) {
            addCriterion("RESDESC like", value, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescNotLike(String value) {
            addCriterion("RESDESC not like", value, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescIn(List<String> values) {
            addCriterion("RESDESC in", values, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescNotIn(List<String> values) {
            addCriterion("RESDESC not in", values, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescBetween(String value1, String value2) {
            addCriterion("RESDESC between", value1, value2, "resdesc");
            return (Criteria) this;
        }

        public Criteria andResdescNotBetween(String value1, String value2) {
            addCriterion("RESDESC not between", value1, value2, "resdesc");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PTRESOURCE
     *
     * @mbggenerated do_not_delete_during_merge Thu Aug 21 20:41:09 CST 2014
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PTRESOURCE
     *
     * @mbggenerated Thu Aug 21 20:41:09 CST 2014
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