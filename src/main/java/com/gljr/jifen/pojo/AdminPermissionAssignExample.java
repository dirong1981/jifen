package com.gljr.jifen.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminPermissionAssignExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AdminPermissionAssignExample() {
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

        public Criteria andAidIsNull() {
            addCriterion("aid is null");
            return (Criteria) this;
        }

        public Criteria andAidIsNotNull() {
            addCriterion("aid is not null");
            return (Criteria) this;
        }

        public Criteria andAidEqualTo(Integer value) {
            addCriterion("aid =", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidNotEqualTo(Integer value) {
            addCriterion("aid <>", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidGreaterThan(Integer value) {
            addCriterion("aid >", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidGreaterThanOrEqualTo(Integer value) {
            addCriterion("aid >=", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidLessThan(Integer value) {
            addCriterion("aid <", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidLessThanOrEqualTo(Integer value) {
            addCriterion("aid <=", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidIn(List<Integer> values) {
            addCriterion("aid in", values, "aid");
            return (Criteria) this;
        }

        public Criteria andAidNotIn(List<Integer> values) {
            addCriterion("aid not in", values, "aid");
            return (Criteria) this;
        }

        public Criteria andAidBetween(Integer value1, Integer value2) {
            addCriterion("aid between", value1, value2, "aid");
            return (Criteria) this;
        }

        public Criteria andAidNotBetween(Integer value1, Integer value2) {
            addCriterion("aid not between", value1, value2, "aid");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeIsNull() {
            addCriterion("permission_code is null");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeIsNotNull() {
            addCriterion("permission_code is not null");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeEqualTo(Integer value) {
            addCriterion("permission_code =", value, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeNotEqualTo(Integer value) {
            addCriterion("permission_code <>", value, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeGreaterThan(Integer value) {
            addCriterion("permission_code >", value, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("permission_code >=", value, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeLessThan(Integer value) {
            addCriterion("permission_code <", value, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeLessThanOrEqualTo(Integer value) {
            addCriterion("permission_code <=", value, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeIn(List<Integer> values) {
            addCriterion("permission_code in", values, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeNotIn(List<Integer> values) {
            addCriterion("permission_code not in", values, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeBetween(Integer value1, Integer value2) {
            addCriterion("permission_code between", value1, value2, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andPermissionCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("permission_code not between", value1, value2, "permissionCode");
            return (Criteria) this;
        }

        public Criteria andAssignTimeIsNull() {
            addCriterion("assign_time is null");
            return (Criteria) this;
        }

        public Criteria andAssignTimeIsNotNull() {
            addCriterion("assign_time is not null");
            return (Criteria) this;
        }

        public Criteria andAssignTimeEqualTo(Date value) {
            addCriterion("assign_time =", value, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeNotEqualTo(Date value) {
            addCriterion("assign_time <>", value, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeGreaterThan(Date value) {
            addCriterion("assign_time >", value, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("assign_time >=", value, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeLessThan(Date value) {
            addCriterion("assign_time <", value, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeLessThanOrEqualTo(Date value) {
            addCriterion("assign_time <=", value, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeIn(List<Date> values) {
            addCriterion("assign_time in", values, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeNotIn(List<Date> values) {
            addCriterion("assign_time not in", values, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeBetween(Date value1, Date value2) {
            addCriterion("assign_time between", value1, value2, "assignTime");
            return (Criteria) this;
        }

        public Criteria andAssignTimeNotBetween(Date value1, Date value2) {
            addCriterion("assign_time not between", value1, value2, "assignTime");
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