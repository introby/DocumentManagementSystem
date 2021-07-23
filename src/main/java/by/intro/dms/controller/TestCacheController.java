package by.intro.dms.controller;

import by.intro.dms.model.TestItem;
import by.intro.dms.service.TestCacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/cache-test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestCacheController {

    private final TestCacheService testCacheService;

    public TestCacheController(TestCacheService testCacheService) {
        this.testCacheService = testCacheService;
    }

    @GetMapping(value = "{name}")
    public ResponseEntity<TestItem> getItem(@PathVariable("name") String name) {
        TestItem item = testCacheService.findByName(name);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
