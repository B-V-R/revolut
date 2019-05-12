package org.vighnesh.revolut.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {
    Long fromId;
    Long toId;
    BigDecimal amount;

    public Transaction() {
    }

    public Transaction(Long fromId, Long toId, BigDecimal amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o) || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(fromId, that.fromId) &&
                Objects.equals(toId, that.toId) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, amount);
    }
}


