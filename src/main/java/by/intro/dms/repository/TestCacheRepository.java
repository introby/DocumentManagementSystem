package by.intro.dms.repository;

import by.intro.dms.model.TestItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestCacheRepository extends MongoRepository<TestItem, ObjectId> {
    Optional<TestItem> findByName(String name);
}
