package com.gljr.jifen.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreCouponOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StoreCouponOrderExample() {
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

        public Criteria andUcIdIsNull() {
            addCriterion("uc_id is null");
            return (Criteria) this;
        }

        public Criteria andUcIdIsNotNull() {
            addCriterion("uc_id is not null");
            return (Criteria) this;
        }

        public Criteria andUcIdEqualTo(Integer value) {
            addCriterion("uc_id =", value, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdNotEqualTo(Integer value) {
            addCriterion("uc_id <>", value, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdGreaterThan(Integer value) {
            addCriterion("uc_id >", value, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("uc_id >=", value, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdLessThan(Integer value) {
            addCriterion("uc_id <", value, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdLessThanOrEqualTo(Integer value) {
            addCriterion("uc_id <=", value, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdIn(List<Integer> values) {
            addCriterion("uc_id in", values, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdNotIn(List<Integer> values) {
            addCriterion("uc_id not in", values, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdBetween(Integer value1, Integer value2) {
            addCriterion("uc_id between", value1, value2, "ucId");
            return (Criteria) this;
        }

        public Criteria andUcIdNotBetween(Integer value1, Integer value2) {
            addCriterion("uc_id not between", value1, value2, "ucId");
            return (Criteria) this;
        }

        public Criteria andTrxIdIsNull() {
            addCriterion("trx_id is null");
            return (Criteria) this;
        }

        public Criteria andTrxIdIsNotNull() {
            addCriterion("trx_id is not null");
            return (Criteria) this;
        }

        public Criteria andTrxIdEqualTo(Integer value) {
            addCriterion("trx_id =", value, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdNotEqualTo(Integer value) {
            addCriterion("trx_id <>", value, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdGreaterThan(Integer value) {
            addCriterion("trx_id >", value, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("trx_id >=", value, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdLessThan(Integer value) {
            addCriterion("trx_id <", value, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdLessThanOrEqualTo(Integer value) {
            addCriterion("trx_id <=", value, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdIn(List<Integer> values) {
            addCriterion("trx_id in", values, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdNotIn(List<Integer> values) {
            addCriterion("trx_id not in", values, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdBetween(Integer value1, Integer value2) {
            addCriterion("trx_id between", value1, value2, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxIdNotBetween(Integer value1, Integer value2) {
            addCriterion("trx_id not between", value1, value2, "trxId");
            return (Criteria) this;
        }

        public Criteria andTrxCodeIsNull() {
            addCriterion("trx_code is null");
            return (Criteria) this;
        }

        public Criteria andTrxCodeIsNotNull() {
            addCriterion("trx_code is not null");
            return (Criteria) this;
        }

        public Criteria andTrxCodeEqualTo(String value) {
            addCriterion("trx_code =", value, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeNotEqualTo(String value) {
            addCriterion("trx_code <>", value, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeGreaterThan(String value) {
            addCriterion("trx_code >", value, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeGreaterThanOrEqualTo(String value) {
            addCriterion("trx_code >=", value, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeLessThan(String value) {
            addCriterion("trx_code <", value, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeLessThanOrEqualTo(String value) {
            addCriterion("trx_code <=", value, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeLike(String value) {
            addCriterion("trx_code like", value, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeNotLike(String value) {
            addCriterion("trx_code not like", value, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeIn(List<String> values) {
            addCriterion("trx_code in", values, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeNotIn(List<String> values) {
            addCriterion("trx_code not in", values, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeBetween(String value1, String value2) {
            addCriterion("trx_code between", value1, value2, "trxCode");
            return (Criteria) this;
        }

        public Criteria andTrxCodeNotBetween(String value1, String value2) {
            addCriterion("trx_code not between", value1, value2, "trxCode");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdIsNull() {
            addCriterion("dtchain_block_id is null");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdIsNotNull() {
            addCriterion("dtchain_block_id is not null");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdEqualTo(String value) {
            addCriterion("dtchain_block_id =", value, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdNotEqualTo(String value) {
            addCriterion("dtchain_block_id <>", value, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdGreaterThan(String value) {
            addCriterion("dtchain_block_id >", value, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdGreaterThanOrEqualTo(String value) {
            addCriterion("dtchain_block_id >=", value, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdLessThan(String value) {
            addCriterion("dtchain_block_id <", value, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdLessThanOrEqualTo(String value) {
            addCriterion("dtchain_block_id <=", value, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdLike(String value) {
            addCriterion("dtchain_block_id like", value, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdNotLike(String value) {
            addCriterion("dtchain_block_id not like", value, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdIn(List<String> values) {
            addCriterion("dtchain_block_id in", values, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdNotIn(List<String> values) {
            addCriterion("dtchain_block_id not in", values, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdBetween(String value1, String value2) {
            addCriterion("dtchain_block_id between", value1, value2, "dtchainBlockId");
            return (Criteria) this;
        }

        public Criteria andDtchainBlockIdNotBetween(String value1, String value2) {
            addCriterion("dtchain_block_id not between", value1, value2, "dtchainBlockId");
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