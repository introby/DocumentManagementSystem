package by.intro.dms.controller;

import by.intro.dms.model.Document;
import by.intro.dms.model.DocumentRequest;
import by.intro.dms.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cr-search")
public class DocumentCriteriaSearchController {
    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<Page<Document>> getDocuments(@RequestBody DocumentRequest documentRequest) {
        return new ResponseEntity<>(documentService.getDocuments(documentRequest),
                HttpStatus.OK);
    }

}
