package com.gljr.jifen.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StoreCouponExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StoreCouponExample() {
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

        public Criteria andSiIdIsNull() {
            addCriterion("si_id is null");
            return (Criteria) this;
        }

        public Criteria andSiIdIsNotNull() {
            addCriterion("si_id is not null");
            return (Criteria) this;
        }

        public Criteria andSiIdEqualTo(Integer value) {
            addCriterion("si_id =", value, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdNotEqualTo(Integer value) {
            addCriterion("si_id <>", value, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdGreaterThan(Integer value) {
            addCriterion("si_id >", value, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("si_id >=", value, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdLessThan(Integer value) {
            addCriterion("si_id <", value, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdLessThanOrEqualTo(Integer value) {
            addCriterion("si_id <=", value, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdIn(List<Integer> values) {
            addCriterion("si_id in", values, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdNotIn(List<Integer> values) {
            addCriterion("si_id not in", values, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdBetween(Integer value1, Integer value2) {
            addCriterion("si_id between", value1, value2, "siId");
            return (Criteria) this;
        }

        public Criteria andSiIdNotBetween(Integer value1, Integer value2) {
            addCriterion("si_id not between", value1, value2, "siId");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedIsNull() {
            addCriterion("max_generated is null");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedIsNotNull() {
            addCriterion("max_generated is not null");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedEqualTo(Integer value) {
            addCriterion("max_generated =", value, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedNotEqualTo(Integer value) {
            addCriterion("max_generated <>", value, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedGreaterThan(Integer value) {
            addCriterion("max_generated >", value, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_generated >=", value, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedLessThan(Integer value) {
            addCriterion("max_generated <", value, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedLessThanOrEqualTo(Integer value) {
            addCriterion("max_generated <=", value, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedIn(List<Integer> values) {
            addCriterion("max_generated in", values, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedNotIn(List<Integer> values) {
            addCriterion("max_generated not in", values, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedBetween(Integer value1, Integer value2) {
            addCriterion("max_generated between", value1, value2, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andMaxGeneratedNotBetween(Integer value1, Integer value2) {
            addCriterion("max_generated not between", value1, value2, "maxGenerated");
            return (Criteria) this;
        }

        public Criteria andIntegralIsNull() {
            addCriterion("integral is null");
            return (Criteria) this;
        }

        public Criteria andIntegralIsNotNull() {
            addCriterion("integral is not null");
            return (Criteria) this;
        }

        public Criteria andIntegralEqualTo(Integer value) {
            addCriterion("integral =", value, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralNotEqualTo(Integer value) {
            addCriterion("integral <>", value, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralGreaterThan(Integer value) {
            addCriterion("integral >", value, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralGreaterThanOrEqualTo(Integer value) {
            addCriterion("integral >=", value, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralLessThan(Integer value) {
            addCriterion("integral <", value, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralLessThanOrEqualTo(Integer value) {
            addCriterion("integral <=", value, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralIn(List<Integer> values) {
            addCriterion("integral in", values, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralNotIn(List<Integer> values) {
            addCriterion("integral not in", values, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralBetween(Integer value1, Integer value2) {
            addCriterion("integral between", value1, value2, "integral");
            return (Criteria) this;
        }

        public Criteria andIntegralNotBetween(Integer value1, Integer value2) {
            addCriterion("integral not between", value1, value2, "integral");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyIsNull() {
            addCriterion("equal_money is null");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyIsNotNull() {
            addCriterion("equal_money is not null");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyEqualTo(Integer value) {
            addCriterion("equal_money =", value, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyNotEqualTo(Integer value) {
            addCriterion("equal_money <>", value, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyGreaterThan(Integer value) {
            addCriterion("equal_money >", value, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyGreaterThanOrEqualTo(Integer value) {
            addCriterion("equal_money >=", value, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyLessThan(Integer value) {
            addCriterion("equal_money <", value, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyLessThanOrEqualTo(Integer value) {
            addCriterion("equal_money <=", value, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyIn(List<Integer> values) {
            addCriterion("equal_money in", values, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyNotIn(List<Integer> values) {
            addCriterion("equal_money not in", values, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyBetween(Integer value1, Integer value2) {
            addCriterion("equal_money between", value1, value2, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andEqualMoneyNotBetween(Integer value1, Integer value2) {
            addCriterion("equal_money not between", value1, value2, "equalMoney");
            return (Criteria) this;
        }

        public Criteria andValidityTypeIsNull() {
            addCriterion("validity_type is null");
            return (Criteria) this;
        }

        public Criteria andValidityTypeIsNotNull() {
            addCriterion("validity_type is not null");
            return (Criteria) this;
        }

        public Criteria andValidityTypeEqualTo(Integer value) {
            addCriterion("validity_type =", value, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeNotEqualTo(Integer value) {
            addCriterion("validity_type <>", value, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeGreaterThan(Integer value) {
            addCriterion("validity_type >", value, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("validity_type >=", value, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeLessThan(Integer value) {
            addCriterion("validity_type <", value, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeLessThanOrEqualTo(Integer value) {
            addCriterion("validity_type <=", value, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeIn(List<Integer> values) {
            addCriterion("validity_type in", values, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeNotIn(List<Integer> values) {
            addCriterion("validity_type not in", values, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeBetween(Integer value1, Integer value2) {
            addCriterion("validity_type between", value1, value2, "validityType");
            return (Criteria) this;
        }

        public Criteria andValidityTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("validity_type not between", value1, value2, "validityType");
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

        public Criteria andValidDaysIsNull() {
            addCriterion("valid_days is null");
            return (Criteria) this;
        }

        public Criteria andValidDaysIsNotNull() {
            addCriterion("valid_days is not null");
            return (Criteria) this;
        }

        public Criteria andValidDaysEqualTo(Integer value) {
            addCriterion("valid_days =", value, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysNotEqualTo(Integer value) {
            addCriterion("valid_days <>", value, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysGreaterThan(Integer value) {
            addCriterion("valid_days >", value, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysGreaterThanOrEqualTo(Integer value) {
            addCriterion("valid_days >=", value, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysLessThan(Integer value) {
            addCriterion("valid_days <", value, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysLessThanOrEqualTo(Integer value) {
            addCriterion("valid_days <=", value, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysIn(List<Integer> values) {
            addCriterion("valid_days in", values, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysNotIn(List<Integer> values) {
            addCriterion("valid_days not in", values, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysBetween(Integer value1, Integer value2) {
            addCriterion("valid_days between", value1, value2, "validDays");
            return (Criteria) this;
        }

        public Criteria andValidDaysNotBetween(Integer value1, Integer value2) {
            addCriterion("valid_days not between", value1, value2, "validDays");
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