package by.intro.dms.controller;

import by.intro.dms.model.request.DocumentRequest;
import by.intro.dms.model.response.DocumentsListResponse;
import by.intro.dms.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/cr-search", produces = MediaType.APPLICATION_JSON_VALUE)
public class DocumentCriteriaSearchController {

    private final DocumentService documentService;

    public DocumentCriteriaSearchController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public DocumentsListResponse getDocuments(@RequestBody DocumentRequest documentRequest) {

        return documentService.getDocumentsCriteria(documentRequest);
    }

}
