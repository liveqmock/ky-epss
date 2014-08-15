package epss.repository.model;

import java.util.ArrayList;
import java.util.List;

public class CttUpdInfoExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public CttUpdInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
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
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
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

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("ID like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("ID not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdIsNull() {
            addCriterion("TKCTT_UPD is null");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdIsNotNull() {
            addCriterion("TKCTT_UPD is not null");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdEqualTo(String value) {
            addCriterion("TKCTT_UPD =", value, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdNotEqualTo(String value) {
            addCriterion("TKCTT_UPD <>", value, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdGreaterThan(String value) {
            addCriterion("TKCTT_UPD >", value, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdGreaterThanOrEqualTo(String value) {
            addCriterion("TKCTT_UPD >=", value, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdLessThan(String value) {
            addCriterion("TKCTT_UPD <", value, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdLessThanOrEqualTo(String value) {
            addCriterion("TKCTT_UPD <=", value, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdLike(String value) {
            addCriterion("TKCTT_UPD like", value, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdNotLike(String value) {
            addCriterion("TKCTT_UPD not like", value, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdIn(List<String> values) {
            addCriterion("TKCTT_UPD in", values, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdNotIn(List<String> values) {
            addCriterion("TKCTT_UPD not in", values, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdBetween(String value1, String value2) {
            addCriterion("TKCTT_UPD between", value1, value2, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttUpdNotBetween(String value1, String value2) {
            addCriterion("TKCTT_UPD not between", value1, value2, "tkcttUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdIsNull() {
            addCriterion("CSTPL_UPD is null");
            return (Criteria) this;
        }

        public Criteria andCstplUpdIsNotNull() {
            addCriterion("CSTPL_UPD is not null");
            return (Criteria) this;
        }

        public Criteria andCstplUpdEqualTo(String value) {
            addCriterion("CSTPL_UPD =", value, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdNotEqualTo(String value) {
            addCriterion("CSTPL_UPD <>", value, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdGreaterThan(String value) {
            addCriterion("CSTPL_UPD >", value, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdGreaterThanOrEqualTo(String value) {
            addCriterion("CSTPL_UPD >=", value, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdLessThan(String value) {
            addCriterion("CSTPL_UPD <", value, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdLessThanOrEqualTo(String value) {
            addCriterion("CSTPL_UPD <=", value, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdLike(String value) {
            addCriterion("CSTPL_UPD like", value, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdNotLike(String value) {
            addCriterion("CSTPL_UPD not like", value, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdIn(List<String> values) {
            addCriterion("CSTPL_UPD in", values, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdNotIn(List<String> values) {
            addCriterion("CSTPL_UPD not in", values, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdBetween(String value1, String value2) {
            addCriterion("CSTPL_UPD between", value1, value2, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andCstplUpdNotBetween(String value1, String value2) {
            addCriterion("CSTPL_UPD not between", value1, value2, "cstplUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdIsNull() {
            addCriterion("SUBCTT_UPD is null");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdIsNotNull() {
            addCriterion("SUBCTT_UPD is not null");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdEqualTo(String value) {
            addCriterion("SUBCTT_UPD =", value, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdNotEqualTo(String value) {
            addCriterion("SUBCTT_UPD <>", value, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdGreaterThan(String value) {
            addCriterion("SUBCTT_UPD >", value, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdGreaterThanOrEqualTo(String value) {
            addCriterion("SUBCTT_UPD >=", value, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdLessThan(String value) {
            addCriterion("SUBCTT_UPD <", value, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdLessThanOrEqualTo(String value) {
            addCriterion("SUBCTT_UPD <=", value, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdLike(String value) {
            addCriterion("SUBCTT_UPD like", value, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdNotLike(String value) {
            addCriterion("SUBCTT_UPD not like", value, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdIn(List<String> values) {
            addCriterion("SUBCTT_UPD in", values, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdNotIn(List<String> values) {
            addCriterion("SUBCTT_UPD not in", values, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdBetween(String value1, String value2) {
            addCriterion("SUBCTT_UPD between", value1, value2, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttUpdNotBetween(String value1, String value2) {
            addCriterion("SUBCTT_UPD not between", value1, value2, "subcttUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdIsNull() {
            addCriterion("SUBCTT_Q_UPD is null");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdIsNotNull() {
            addCriterion("SUBCTT_Q_UPD is not null");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdEqualTo(String value) {
            addCriterion("SUBCTT_Q_UPD =", value, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdNotEqualTo(String value) {
            addCriterion("SUBCTT_Q_UPD <>", value, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdGreaterThan(String value) {
            addCriterion("SUBCTT_Q_UPD >", value, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdGreaterThanOrEqualTo(String value) {
            addCriterion("SUBCTT_Q_UPD >=", value, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdLessThan(String value) {
            addCriterion("SUBCTT_Q_UPD <", value, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdLessThanOrEqualTo(String value) {
            addCriterion("SUBCTT_Q_UPD <=", value, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdLike(String value) {
            addCriterion("SUBCTT_Q_UPD like", value, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdNotLike(String value) {
            addCriterion("SUBCTT_Q_UPD not like", value, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdIn(List<String> values) {
            addCriterion("SUBCTT_Q_UPD in", values, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdNotIn(List<String> values) {
            addCriterion("SUBCTT_Q_UPD not in", values, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdBetween(String value1, String value2) {
            addCriterion("SUBCTT_Q_UPD between", value1, value2, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttQUpdNotBetween(String value1, String value2) {
            addCriterion("SUBCTT_Q_UPD not between", value1, value2, "subcttQUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdIsNull() {
            addCriterion("SUBCTT_M_UPD is null");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdIsNotNull() {
            addCriterion("SUBCTT_M_UPD is not null");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdEqualTo(String value) {
            addCriterion("SUBCTT_M_UPD =", value, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdNotEqualTo(String value) {
            addCriterion("SUBCTT_M_UPD <>", value, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdGreaterThan(String value) {
            addCriterion("SUBCTT_M_UPD >", value, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdGreaterThanOrEqualTo(String value) {
            addCriterion("SUBCTT_M_UPD >=", value, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdLessThan(String value) {
            addCriterion("SUBCTT_M_UPD <", value, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdLessThanOrEqualTo(String value) {
            addCriterion("SUBCTT_M_UPD <=", value, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdLike(String value) {
            addCriterion("SUBCTT_M_UPD like", value, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdNotLike(String value) {
            addCriterion("SUBCTT_M_UPD not like", value, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdIn(List<String> values) {
            addCriterion("SUBCTT_M_UPD in", values, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdNotIn(List<String> values) {
            addCriterion("SUBCTT_M_UPD not in", values, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdBetween(String value1, String value2) {
            addCriterion("SUBCTT_M_UPD between", value1, value2, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttMUpdNotBetween(String value1, String value2) {
            addCriterion("SUBCTT_M_UPD not between", value1, value2, "subcttMUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdIsNull() {
            addCriterion("SUBCTT_P_UPD is null");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdIsNotNull() {
            addCriterion("SUBCTT_P_UPD is not null");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdEqualTo(String value) {
            addCriterion("SUBCTT_P_UPD =", value, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdNotEqualTo(String value) {
            addCriterion("SUBCTT_P_UPD <>", value, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdGreaterThan(String value) {
            addCriterion("SUBCTT_P_UPD >", value, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdGreaterThanOrEqualTo(String value) {
            addCriterion("SUBCTT_P_UPD >=", value, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdLessThan(String value) {
            addCriterion("SUBCTT_P_UPD <", value, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdLessThanOrEqualTo(String value) {
            addCriterion("SUBCTT_P_UPD <=", value, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdLike(String value) {
            addCriterion("SUBCTT_P_UPD like", value, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdNotLike(String value) {
            addCriterion("SUBCTT_P_UPD not like", value, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdIn(List<String> values) {
            addCriterion("SUBCTT_P_UPD in", values, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdNotIn(List<String> values) {
            addCriterion("SUBCTT_P_UPD not in", values, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdBetween(String value1, String value2) {
            addCriterion("SUBCTT_P_UPD between", value1, value2, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andSubcttPUpdNotBetween(String value1, String value2) {
            addCriterion("SUBCTT_P_UPD not between", value1, value2, "subcttPUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdIsNull() {
            addCriterion("TKCTT_QS_UPD is null");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdIsNotNull() {
            addCriterion("TKCTT_QS_UPD is not null");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdEqualTo(String value) {
            addCriterion("TKCTT_QS_UPD =", value, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdNotEqualTo(String value) {
            addCriterion("TKCTT_QS_UPD <>", value, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdGreaterThan(String value) {
            addCriterion("TKCTT_QS_UPD >", value, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdGreaterThanOrEqualTo(String value) {
            addCriterion("TKCTT_QS_UPD >=", value, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdLessThan(String value) {
            addCriterion("TKCTT_QS_UPD <", value, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdLessThanOrEqualTo(String value) {
            addCriterion("TKCTT_QS_UPD <=", value, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdLike(String value) {
            addCriterion("TKCTT_QS_UPD like", value, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdNotLike(String value) {
            addCriterion("TKCTT_QS_UPD not like", value, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdIn(List<String> values) {
            addCriterion("TKCTT_QS_UPD in", values, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdNotIn(List<String> values) {
            addCriterion("TKCTT_QS_UPD not in", values, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdBetween(String value1, String value2) {
            addCriterion("TKCTT_QS_UPD between", value1, value2, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQsUpdNotBetween(String value1, String value2) {
            addCriterion("TKCTT_QS_UPD not between", value1, value2, "tkcttQsUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdIsNull() {
            addCriterion("TKCTT_QM_UPD is null");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdIsNotNull() {
            addCriterion("TKCTT_QM_UPD is not null");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdEqualTo(String value) {
            addCriterion("TKCTT_QM_UPD =", value, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdNotEqualTo(String value) {
            addCriterion("TKCTT_QM_UPD <>", value, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdGreaterThan(String value) {
            addCriterion("TKCTT_QM_UPD >", value, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdGreaterThanOrEqualTo(String value) {
            addCriterion("TKCTT_QM_UPD >=", value, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdLessThan(String value) {
            addCriterion("TKCTT_QM_UPD <", value, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdLessThanOrEqualTo(String value) {
            addCriterion("TKCTT_QM_UPD <=", value, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdLike(String value) {
            addCriterion("TKCTT_QM_UPD like", value, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdNotLike(String value) {
            addCriterion("TKCTT_QM_UPD not like", value, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdIn(List<String> values) {
            addCriterion("TKCTT_QM_UPD in", values, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdNotIn(List<String> values) {
            addCriterion("TKCTT_QM_UPD not in", values, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdBetween(String value1, String value2) {
            addCriterion("TKCTT_QM_UPD between", value1, value2, "tkcttQmUpd");
            return (Criteria) this;
        }

        public Criteria andTkcttQmUpdNotBetween(String value1, String value2) {
            addCriterion("TKCTT_QM_UPD not between", value1, value2, "tkcttQmUpd");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated do_not_delete_during_merge Fri Aug 15 16:08:09 CST 2014
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table EPSS.CTT_UPD_INFO
     *
     * @mbggenerated Fri Aug 15 16:08:09 CST 2014
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