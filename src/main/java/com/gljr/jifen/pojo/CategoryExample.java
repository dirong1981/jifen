package com.gljr.jifen.pojo;

import java.util.ArrayList;
import java.util.List;

public class CategoryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CategoryExample() {
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

        public Criteria andCIdIsNull() {
            addCriterion("c_id is null");
            return (Criteria) this;
        }

        public Criteria andCIdIsNotNull() {
            addCriterion("c_id is not null");
            return (Criteria) this;
        }

        public Criteria andCIdEqualTo(String value) {
            addCriterion("c_id =", value, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdNotEqualTo(String value) {
            addCriterion("c_id <>", value, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdGreaterThan(String value) {
            addCriterion("c_id >", value, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdGreaterThanOrEqualTo(String value) {
            addCriterion("c_id >=", value, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdLessThan(String value) {
            addCriterion("c_id <", value, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdLessThanOrEqualTo(String value) {
            addCriterion("c_id <=", value, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdLike(String value) {
            addCriterion("c_id like", value, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdNotLike(String value) {
            addCriterion("c_id not like", value, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdIn(List<String> values) {
            addCriterion("c_id in", values, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdNotIn(List<String> values) {
            addCriterion("c_id not in", values, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdBetween(String value1, String value2) {
            addCriterion("c_id between", value1, value2, "cId");
            return (Criteria) this;
        }

        public Criteria andCIdNotBetween(String value1, String value2) {
            addCriterion("c_id not between", value1, value2, "cId");
            return (Criteria) this;
        }

        public Criteria andCNameIsNull() {
            addCriterion("c_name is null");
            return (Criteria) this;
        }

        public Criteria andCNameIsNotNull() {
            addCriterion("c_name is not null");
            return (Criteria) this;
        }

        public Criteria andCNameEqualTo(String value) {
            addCriterion("c_name =", value, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameNotEqualTo(String value) {
            addCriterion("c_name <>", value, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameGreaterThan(String value) {
            addCriterion("c_name >", value, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameGreaterThanOrEqualTo(String value) {
            addCriterion("c_name >=", value, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameLessThan(String value) {
            addCriterion("c_name <", value, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameLessThanOrEqualTo(String value) {
            addCriterion("c_name <=", value, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameLike(String value) {
            addCriterion("c_name like", value, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameNotLike(String value) {
            addCriterion("c_name not like", value, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameIn(List<String> values) {
            addCriterion("c_name in", values, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameNotIn(List<String> values) {
            addCriterion("c_name not in", values, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameBetween(String value1, String value2) {
            addCriterion("c_name between", value1, value2, "cName");
            return (Criteria) this;
        }

        public Criteria andCNameNotBetween(String value1, String value2) {
            addCriterion("c_name not between", value1, value2, "cName");
            return (Criteria) this;
        }

        public Criteria andCParentIdIsNull() {
            addCriterion("c_parent_id is null");
            return (Criteria) this;
        }

        public Criteria andCParentIdIsNotNull() {
            addCriterion("c_parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andCParentIdEqualTo(String value) {
            addCriterion("c_parent_id =", value, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdNotEqualTo(String value) {
            addCriterion("c_parent_id <>", value, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdGreaterThan(String value) {
            addCriterion("c_parent_id >", value, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdGreaterThanOrEqualTo(String value) {
            addCriterion("c_parent_id >=", value, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdLessThan(String value) {
            addCriterion("c_parent_id <", value, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdLessThanOrEqualTo(String value) {
            addCriterion("c_parent_id <=", value, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdLike(String value) {
            addCriterion("c_parent_id like", value, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdNotLike(String value) {
            addCriterion("c_parent_id not like", value, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdIn(List<String> values) {
            addCriterion("c_parent_id in", values, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdNotIn(List<String> values) {
            addCriterion("c_parent_id not in", values, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdBetween(String value1, String value2) {
            addCriterion("c_parent_id between", value1, value2, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCParentIdNotBetween(String value1, String value2) {
            addCriterion("c_parent_id not between", value1, value2, "cParentId");
            return (Criteria) this;
        }

        public Criteria andCLogoIsNull() {
            addCriterion("c_logo is null");
            return (Criteria) this;
        }

        public Criteria andCLogoIsNotNull() {
            addCriterion("c_logo is not null");
            return (Criteria) this;
        }

        public Criteria andCLogoEqualTo(String value) {
            addCriterion("c_logo =", value, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoNotEqualTo(String value) {
            addCriterion("c_logo <>", value, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoGreaterThan(String value) {
            addCriterion("c_logo >", value, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoGreaterThanOrEqualTo(String value) {
            addCriterion("c_logo >=", value, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoLessThan(String value) {
            addCriterion("c_logo <", value, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoLessThanOrEqualTo(String value) {
            addCriterion("c_logo <=", value, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoLike(String value) {
            addCriterion("c_logo like", value, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoNotLike(String value) {
            addCriterion("c_logo not like", value, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoIn(List<String> values) {
            addCriterion("c_logo in", values, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoNotIn(List<String> values) {
            addCriterion("c_logo not in", values, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoBetween(String value1, String value2) {
            addCriterion("c_logo between", value1, value2, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCLogoNotBetween(String value1, String value2) {
            addCriterion("c_logo not between", value1, value2, "cLogo");
            return (Criteria) this;
        }

        public Criteria andCStateIsNull() {
            addCriterion("c_state is null");
            return (Criteria) this;
        }

        public Criteria andCStateIsNotNull() {
            addCriterion("c_state is not null");
            return (Criteria) this;
        }

        public Criteria andCStateEqualTo(Integer value) {
            addCriterion("c_state =", value, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateNotEqualTo(Integer value) {
            addCriterion("c_state <>", value, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateGreaterThan(Integer value) {
            addCriterion("c_state >", value, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("c_state >=", value, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateLessThan(Integer value) {
            addCriterion("c_state <", value, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateLessThanOrEqualTo(Integer value) {
            addCriterion("c_state <=", value, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateIn(List<Integer> values) {
            addCriterion("c_state in", values, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateNotIn(List<Integer> values) {
            addCriterion("c_state not in", values, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateBetween(Integer value1, Integer value2) {
            addCriterion("c_state between", value1, value2, "cState");
            return (Criteria) this;
        }

        public Criteria andCStateNotBetween(Integer value1, Integer value2) {
            addCriterion("c_state not between", value1, value2, "cState");
            return (Criteria) this;
        }

        public Criteria andCSortIsNull() {
            addCriterion("c_sort is null");
            return (Criteria) this;
        }

        public Criteria andCSortIsNotNull() {
            addCriterion("c_sort is not null");
            return (Criteria) this;
        }

        public Criteria andCSortEqualTo(String value) {
            addCriterion("c_sort =", value, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortNotEqualTo(String value) {
            addCriterion("c_sort <>", value, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortGreaterThan(String value) {
            addCriterion("c_sort >", value, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortGreaterThanOrEqualTo(String value) {
            addCriterion("c_sort >=", value, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortLessThan(String value) {
            addCriterion("c_sort <", value, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortLessThanOrEqualTo(String value) {
            addCriterion("c_sort <=", value, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortLike(String value) {
            addCriterion("c_sort like", value, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortNotLike(String value) {
            addCriterion("c_sort not like", value, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortIn(List<String> values) {
            addCriterion("c_sort in", values, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortNotIn(List<String> values) {
            addCriterion("c_sort not in", values, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortBetween(String value1, String value2) {
            addCriterion("c_sort between", value1, value2, "cSort");
            return (Criteria) this;
        }

        public Criteria andCSortNotBetween(String value1, String value2) {
            addCriterion("c_sort not between", value1, value2, "cSort");
            return (Criteria) this;
        }

        public Criteria andCCreatorIsNull() {
            addCriterion("c_creator is null");
            return (Criteria) this;
        }

        public Criteria andCCreatorIsNotNull() {
            addCriterion("c_creator is not null");
            return (Criteria) this;
        }

        public Criteria andCCreatorEqualTo(String value) {
            addCriterion("c_creator =", value, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorNotEqualTo(String value) {
            addCriterion("c_creator <>", value, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorGreaterThan(String value) {
            addCriterion("c_creator >", value, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorGreaterThanOrEqualTo(String value) {
            addCriterion("c_creator >=", value, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorLessThan(String value) {
            addCriterion("c_creator <", value, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorLessThanOrEqualTo(String value) {
            addCriterion("c_creator <=", value, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorLike(String value) {
            addCriterion("c_creator like", value, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorNotLike(String value) {
            addCriterion("c_creator not like", value, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorIn(List<String> values) {
            addCriterion("c_creator in", values, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorNotIn(List<String> values) {
            addCriterion("c_creator not in", values, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorBetween(String value1, String value2) {
            addCriterion("c_creator between", value1, value2, "cCreator");
            return (Criteria) this;
        }

        public Criteria andCCreatorNotBetween(String value1, String value2) {
            addCriterion("c_creator not between", value1, value2, "cCreator");
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