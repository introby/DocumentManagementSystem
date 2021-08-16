package by.intro.dms.service;

import by.intro.dms.mapper.DocumentMapper;
import by.intro.dms.model.Document;
import by.intro.dms.model.request.DocumentRequest;
import by.intro.dms.model.response.DocumentsListResponse;
import by.intro.dms.repository.DocumentCriteriaRepository;
import by.intro.dms.repository.DocumentRepository;
import by.intro.dms.repository.DocumentSpecificationRepository;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import static by.intro.dms.service.PaginationUtils.buildPaginationInfo;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final EntityManager entityManager;
    private final DocumentCriteriaRepository documentCriteriaRepository;
    private final DocumentSpecificationRepository documentSpecificationRepository;
    private final DocumentMapper documentMapper;

    public DocumentService(DocumentRepository documentRepository,
                           EntityManager entityManager,
                           DocumentCriteriaRepository documentCriteriaRepository,
                           DocumentSpecificationRepository documentSpecificationRepository,
                           DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.entityManager = entityManager;
        this.documentCriteriaRepository = documentCriteriaRepository;
        this.documentSpecificationRepository = documentSpecificationRepository;
        this.documentMapper = documentMapper;
    }

    protected DocumentMapper getDocumentMapper() {
        return documentMapper;
    }

    public Page<Document> findByKeyword(boolean isDeleted, String findValue, PageRequest pageRequest) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedDocumentFilter");
        filter.setParameter("isDeleted", isDeleted);
        Page<Document> products = documentRepository.findByKeyword(findValue, pageRequest);
        session.disableFilter("deletedDocumentFilter");
        return products;
    }

    public Document findById(Long documentId) {
        return documentRepository.findById(documentId).orElseThrow();
    }

    public Document findBySupplier(String supplier) {
        return documentRepository.findDocumentBySupplier(supplier);
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    public void deleteById(Long documentId) {
        documentRepository.deleteById(documentId);
    }

    public Page<Document> getDocumentsPageCriteria(DocumentRequest documentRequest) {
        return documentCriteriaRepository.findAllWithFilters(documentRequest.getDocumentPage(),
                documentRequest.getDocumentSearchCriteria());
    }

    public DocumentsListResponse getDocumentsCriteria(DocumentRequest documentRequest) {
        Page<Document> documentsPage = getDocumentsPageCriteria(documentRequest);
        return DocumentsListResponse.builder()
                .documents(documentMapper.toDtoList(documentsPage.toList()))
                .paginationInfo(buildPaginationInfo(documentsPage))
                .build();
    }

    public Page<Document> getDocumentsPageSpecification(DocumentRequest documentRequest) {
        return documentSpecificationRepository.findAllWithSpecification(documentRequest);
    }

    public DocumentsListResponse getDocumentsSpecification(DocumentRequest documentRequest) {
        Page<Document> documentsPage = getDocumentsPageSpecification(documentRequest);
        return DocumentsListResponse.builder()
                .documents(documentMapper.toDtoList(documentsPage.toList()))
                .paginationInfo(buildPaginationInfo(documentsPage))
                .currency(documentRequest.getDocumentSearchCriteria().getCurrencyEnum().name())
                .build();
    }

}
