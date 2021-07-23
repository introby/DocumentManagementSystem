package by.intro.dms.service;

import by.intro.dms.model.TestItem;
import by.intro.dms.repository.TestCacheRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestCacheService {

    private final TestCacheRepository testCacheRepository;

    public TestCacheService(TestCacheRepository testCacheRepository) {
        this.testCacheRepository = testCacheRepository;
    }

    public TestItem add(TestItem testItem) {
        return testCacheRepository.insert(testItem);
    }

    @Cacheable("items")
    public TestItem findByName(String name) {
        return testCacheRepository.findByName(name).orElseGet(this::getItemFromRemoteService);
    }

    public TestItem getItemFromRemoteService() {
        String name = "name";
        String content = "content from remote service";
        TestItem testItem = new TestItem();
        testItem.setName(name);
        testItem.setContent(content);
        add(testItem);
        return testItem;
    }
}
