package com.edmunds.vinspy.use_new.repository;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mongodb.WriteResult;
import com.edmunds.vinspy.use_new.model.BaseEntity;
import com.edmunds.vinspy.use_new.model.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import com.edmunds.vinspy.use_new.utils.NotFoundException;
import com.edmunds.vinspy.use_new.utils.QueryHelper;


/**
 * Basic implementation of a repository for {@link BaseEntity} classes.
 * Contains methods to work with underlying data store.
 *
 * @param <T> - type of entity
 * @author atamkevich
 */
public abstract class BaseRepository<T extends BaseEntity> {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Function<T, T> sequenceMapper = new Function<T, T>() {
        @Override
        public T apply(T t) {
            if (t.getId() == null) {
                t.setId(getNextSequenceValue(getEntityClass().getSimpleName()));
            }
            return t;
        }
    };

    public List<T> findAll(Query query) {
        return mongoTemplate.find(query, getEntityClass());
    }

    public long count(Query query) {
        return mongoTemplate.count(query, getEntityClass());
    }

    public List<T> findAll() {
        return mongoTemplate.findAll(getEntityClass());
    }

    public List<T> findAll(Query query, Pageable pagingConfig, Sort sort) {
        return mongoTemplate.find(query.with(pagingConfig).with(sort), getEntityClass());
    }

    public T addOrUpdate(T entity, String coll) {
        if (entity.getId() == null) {
            entity.setId(getNextSequenceValue(getEntityClass().getSimpleName()));
        }
        mongoTemplate.save(entity, coll);
        return entity;
    }

    public List<T> saveAll(List<T> entityList) {
        List<T> entities = Lists.newArrayList(Lists.transform(entityList, sequenceMapper));
        mongoTemplate.insertAll(entities);
        return entities;
    }

    public void upsert(Query query, Update update) {
        mongoTemplate.upsert(query, update, getEntityClass());
    }

    public int update(Query query, Update update, Boolean multi) {
        WriteResult writeResult = multi ? mongoTemplate.updateMulti(query, update, getEntityClass()) :
                mongoTemplate.updateFirst(query, update, getEntityClass());
        return writeResult.getN();
    }

    public T findAndModify(Query query, Update update, FindAndModifyOptions options) {
        return mongoTemplate.findAndModify(query, update, options, getEntityClass());
    }

    public T findOne(Query query) throws NotFoundException {
        return mongoTemplate.findOne(query, getEntityClass());
    }

    public T findById(Long id) throws NotFoundException {
        T one = mongoTemplate.findOne(QueryHelper.byId(id), getEntityClass());
        if (one == null) {
            throw new NotFoundException(String.format("Entity of type %s with id='%s' was not found", getEntityClass().getSimpleName(), id));
        }
        return one;
    }

    public void delete(Long id) {
        delete(QueryHelper.byId(id));
    }

    public void delete(Query query) {
        mongoTemplate.remove(query, getEntityClass());
    }

    public void deleteAll() {
        mongoTemplate.dropCollection(getEntityClass());
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    private Long getNextSequenceValue(String sequenceName) {
        Sequence sequence = mongoTemplate.findAndModify(
                new Query(Criteria.where("name").is(sequenceName)),
                new Update().inc("value", 1L),
                new FindAndModifyOptions().returnNew(true).upsert(true), Sequence.class);

        if (sequence == null) {
            throw new RuntimeException("Cannot generate unique identifier for entity");
        }

        return sequence.getValue();
    }

    protected MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    protected IndexOperations indexOps() {
        return getMongoTemplate().indexOps(getEntityClass());
    }
}
