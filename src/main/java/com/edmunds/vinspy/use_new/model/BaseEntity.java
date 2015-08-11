package com.edmunds.vinspy.use_new.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Objects;

/**
 * @param <T> - represents type for id of entity
 * @author Vitaly_Krasovsky
 */
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1986L;

    @Id
    protected T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
