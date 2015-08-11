package com.edmunds.vinspy.use_new.repository;



import com.edmunds.vinspy.use_new.model.Inventory;
import org.springframework.stereotype.Repository;

import java.util.List;
;

/**
 * Inventory Repository.
 *
 * @author atamkevich
 */
@Repository
public class InventoryRepository extends BaseRepository<Inventory> {


    @Override
    public Inventory addOrUpdate(Inventory entity, String collection) {
        if (entity.getId() == null) {
            throw new IllegalStateException();
        }
        return super.addOrUpdate(entity, collection);
    }

    @Override
    public List<Inventory> saveAll(List<Inventory> entityList) {
        throw new IllegalStateException("Inventory allowed to be created only via crawling process");
    }
}
