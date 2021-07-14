package by.intro.dms.controller;

import by.intro.dms.model.Document;
import by.intro.dms.model.DocumentRequest;
import by.intro.dms.repository.DocumentSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class DocumentSpecificationSearchController {

    private final DocumentSpecificationRepository documentSpecificationRepository;

    public DocumentSpecificationSearchController(DocumentSpecificationRepository documentSpecificationRepository) {
        this.documentSpecificationRepository = documentSpecificationRepository;
    }

    @PostMapping
    public ResponseEntity<Page<Document>> getDocuments(@RequestBody DocumentRequest documentRequest) {
        return new ResponseEntity<>(documentSpecificationRepository.findAllWithSpecification(
                documentRequest),
                HttpStatus.OK);
    }
}
