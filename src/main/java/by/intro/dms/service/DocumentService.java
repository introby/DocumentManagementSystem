package by.intro.dms.service;

import by.intro.dms.model.Document;
import by.intro.dms.model.DocumentPage;
import by.intro.dms.model.DocumentRequest;
import by.intro.dms.model.DocumentSearchCriteria;
import by.intro.dms.repository.DocumentCriteriaRepository;
import by.intro.dms.repository.DocumentRepository;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.print.Doc;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DocumentCriteriaRepository documentCriteriaRepository;

    public DocumentService() {
    }

    public Page<Document> findByKeyword(boolean isDeleted, String findValue, PageRequest pageRequest) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedDocumentFilter");
        filter.setParameter("isDeleted", isDeleted);
        Page<Document> products =  documentRepository.findByKeyword(findValue, pageRequest);
        session.disableFilter("deletedDocumentFilter");
        return products;
    }

    public Document findById(Long documentId) {
        Optional<Document> dop = documentRepository.findById(documentId);
        return dop.get();
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    public void deleteById(Long documentId) {
        documentRepository.deleteById(documentId);
    }

    public Page<Document> getDocuments(DocumentRequest documentRequest) {
        return documentCriteriaRepository.findAllWithFilters(documentRequest.getDocumentPage(),
                documentRequest.getDocumentSearchCriteria());
    }
}
