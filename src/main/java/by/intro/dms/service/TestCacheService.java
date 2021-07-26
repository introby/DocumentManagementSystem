package by.intro.dms.service;

import by.intro.dms.model.TestItem;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestCacheService {

    private final GenericCache<String, TestItem> cache;

    public TestCacheService(GenericCache cache) {
        this.cache = cache;
    }

    @Cacheable(cacheNames = "items")
    public TestItem getSingleItem(String name) {
        return this.cache.get(name).orElseGet(() -> getItemFromRemoteService(name));
    }

    public TestItem getItemFromRemoteService(String name) {
        String content = String.format("%s_%s", name, LocalDateTime.now());
        TestItem testItem = new TestItem();
        testItem.setName(name);
        testItem.setContent(content);
        this.cache.put(name, testItem);
        return testItem;
    }
}
