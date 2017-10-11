package com.gljr.jifen.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class VirtualProductExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VirtualProductExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andVpIdIsNull() {
            addCriterion("vp_id is null");
            return (Criteria) this;
        }

        public Criteria andVpIdIsNotNull() {
            addCriterion("vp_id is not null");
            return (Criteria) this;
        }

        public Criteria andVpIdEqualTo(Integer value) {
            addCriterion("vp_id =", value, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdNotEqualTo(Integer value) {
            addCriterion("vp_id <>", value, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdGreaterThan(Integer value) {
            addCriterion("vp_id >", value, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("vp_id >=", value, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdLessThan(Integer value) {
            addCriterion("vp_id <", value, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdLessThanOrEqualTo(Integer value) {
            addCriterion("vp_id <=", value, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdIn(List<Integer> values) {
            addCriterion("vp_id in", values, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdNotIn(List<Integer> values) {
            addCriterion("vp_id not in", values, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdBetween(Integer value1, Integer value2) {
            addCriterion("vp_id between", value1, value2, "vpId");
            return (Criteria) this;
        }

        public Criteria andVpIdNotBetween(Integer value1, Integer value2) {
            addCriterion("vp_id not between", value1, value2, "vpId");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andValidFromIsNull() {
            addCriterion("valid_from is null");
            return (Criteria) this;
        }

        public Criteria andValidFromIsNotNull() {
            addCriterion("valid_from is not null");
            return (Criteria) this;
        }

        public Criteria andValidFromEqualTo(Date value) {
            addCriterionForJDBCDate("valid_from =", value, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromNotEqualTo(Date value) {
            addCriterionForJDBCDate("valid_from <>", value, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromGreaterThan(Date value) {
            addCriterionForJDBCDate("valid_from >", value, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("valid_from >=", value, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromLessThan(Date value) {
            addCriterionForJDBCDate("valid_from <", value, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("valid_from <=", value, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromIn(List<Date> values) {
            addCriterionForJDBCDate("valid_from in", values, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromNotIn(List<Date> values) {
            addCriterionForJDBCDate("valid_from not in", values, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("valid_from between", value1, value2, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidFromNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("valid_from not between", value1, value2, "validFrom");
            return (Criteria) this;
        }

        public Criteria andValidToIsNull() {
            addCriterion("valid_to is null");
            return (Criteria) this;
        }

        public Criteria andValidToIsNotNull() {
            addCriterion("valid_to is not null");
            return (Criteria) this;
        }

        public Criteria andValidToEqualTo(Date value) {
            addCriterionForJDBCDate("valid_to =", value, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToNotEqualTo(Date value) {
            addCriterionForJDBCDate("valid_to <>", value, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToGreaterThan(Date value) {
            addCriterionForJDBCDate("valid_to >", value, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("valid_to >=", value, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToLessThan(Date value) {
            addCriterionForJDBCDate("valid_to <", value, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("valid_to <=", value, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToIn(List<Date> values) {
            addCriterionForJDBCDate("valid_to in", values, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToNotIn(List<Date> values) {
            addCriterionForJDBCDate("valid_to not in", values, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("valid_to between", value1, value2, "validTo");
            return (Criteria) this;
        }

        public Criteria andValidToNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("valid_to not between", value1, value2, "validTo");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Integer value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Integer value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Integer value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Integer value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Integer value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Integer value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Integer> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Integer> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Integer value1, Integer value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Integer value1, Integer value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountIsNull() {
            addCriterion("remaining_amount is null");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountIsNotNull() {
            addCriterion("remaining_amount is not null");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountEqualTo(Integer value) {
            addCriterion("remaining_amount =", value, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountNotEqualTo(Integer value) {
            addCriterion("remaining_amount <>", value, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountGreaterThan(Integer value) {
            addCriterion("remaining_amount >", value, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountGreaterThanOrEqualTo(Integer value) {
            addCriterion("remaining_amount >=", value, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountLessThan(Integer value) {
            addCriterion("remaining_amount <", value, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountLessThanOrEqualTo(Integer value) {
            addCriterion("remaining_amount <=", value, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountIn(List<Integer> values) {
            addCriterion("remaining_amount in", values, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountNotIn(List<Integer> values) {
            addCriterion("remaining_amount not in", values, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountBetween(Integer value1, Integer value2) {
            addCriterion("remaining_amount between", value1, value2, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andRemainingAmountNotBetween(Integer value1, Integer value2) {
            addCriterion("remaining_amount not between", value1, value2, "remainingAmount");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

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