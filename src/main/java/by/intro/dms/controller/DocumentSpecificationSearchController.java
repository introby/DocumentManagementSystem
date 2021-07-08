package by.intro.dms.controller;

import by.intro.dms.model.Document;
import by.intro.dms.model.DocumentPage;
import by.intro.dms.model.DocumentRequest;
import by.intro.dms.model.DocumentSearchCriteria;
import by.intro.dms.repository.DocumentSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class DocumentSpecificationSearchController {

    private final DocumentSpecificationRepository documentSpecificationRepository;

    public DocumentSpecificationSearchController(DocumentSpecificationRepository documentSpecificationRepository) {
        this.documentSpecificationRepository = documentSpecificationRepository;
    }

//    @GetMapping
//    public ResponseEntity<Page<Document>> getDocuments(DocumentSearchCriteria documentSearchCriteria,
//                                                       DocumentPage documentPage) {
//        return new ResponseEntity<>(documentSpecificationRepository.findAllWithSpecification(
//                documentSearchCriteria, documentPage),
//                HttpStatus.OK);
//    }

    @PostMapping
    public ResponseEntity<Page<Document>> getDocuments(@RequestBody DocumentRequest documentRequest) {
        return new ResponseEntity<>(documentSpecificationRepository.findAllWithSpecification(
                documentRequest),
                HttpStatus.OK);
    }
}
