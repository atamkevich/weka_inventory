package com.edmunds.vinspy.use_new.utils;


import com.google.common.collect.Maps;
import org.javatuples.Pair;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.Map;


/**
 * Helper class to construct {@link org.springframework.data.mongodb.core.query.Query}.
 *
 * @author atamkevich
 */
public abstract class QueryHelper {

    private final static Maps.EntryTransformer<String, Object, Criteria> criteriaMapper = new Maps.EntryTransformer<String, Object, Criteria>() {

        @Override
        public Criteria transformEntry(String key, Object value) {
            return criteriaFor(key, value);
        }
    };

    public static Query convertMapToOrQuery(Map<String, Object> params) {
        return new Query(new Criteria().orOperator(buildCriteria(params)));
    }

    private static Criteria[] buildCriteria(Map<String, Object> params) {
        Criteria[] criteriaBuff = new Criteria[]{};
        return Maps.transformEntries(params, criteriaMapper).values().toArray(criteriaBuff);
    }

    private static Criteria criteriaFor(String key, Object value) {
        if (value instanceof Collection) {
            return Criteria.where(key).in((Collection<?>) value);
        }

        if (value instanceof Pair) {
            return Criteria.where(key).gte((((Pair) value).getValue0())).lte(((Pair) value).getValue1());
        }

        return Criteria.where(key).is(value);
    }

    public static Query byId(Long id) {
        return Query.query(Criteria.where("_id").is(id));
    }

    public static Sort getSort(String sortType, String sortBy) {
        return (sortBy != null && sortType != null) ?
            new Sort(Sort.Direction.fromString(sortType), sortBy) : null;
    }


}
