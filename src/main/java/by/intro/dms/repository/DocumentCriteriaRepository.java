package by.intro.dms.repository;

import by.intro.dms.model.DocStatus;
import by.intro.dms.model.Document;
import by.intro.dms.model.DocumentPage;
import by.intro.dms.model.DocumentSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class DocumentCriteriaRepository {

    private final String DOCUMENT_NAME = "documentName";
    private final String CONSUMER = "consumer";
    private final String CONTRACT_TERM = "contractTerm";
    private final String DOC_STATUS = "docStatus";

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;


    public DocumentCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Document> findAllWithFilters(DocumentPage documentPage,
                                             DocumentSearchCriteria documentSearchCriteria) {
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        Root<Document> documentRoot = criteriaQuery.from(Document.class);
        Predicate predicate = getPredicate(documentSearchCriteria, documentRoot);
        criteriaQuery.where(predicate);
        setOrder(documentPage, criteriaQuery, documentRoot);

        TypedQuery<Document> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(documentPage.getPageNumber() * documentPage.getPageSize());
        typedQuery.setMaxResults(documentPage.getPageSize());

        Pageable pageable = getPageable(documentPage);

        long documentCount = getDocumentCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, documentCount);
    }

    private Predicate getPredicate(DocumentSearchCriteria documentSearchCriteria,
                                   Root<Document> documentRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(documentSearchCriteria.getDocumentName())) {
            String docName = String.format("%%%s%%", documentSearchCriteria.getDocumentName());
            predicates.add(criteriaBuilder.like(documentRoot.get(DOCUMENT_NAME), docName)
            );
        }


        if (Objects.nonNull(documentSearchCriteria.getConsumer())) {
            predicates.add(criteriaBuilder.equal(documentRoot.get(CONSUMER),
                    documentSearchCriteria.getConsumer())
            );
        }

        if (Objects.nonNull(documentSearchCriteria.getDateFrom())
                && Objects.nonNull(documentSearchCriteria.getDateTo())) {
            predicates.add(criteriaBuilder.between(documentRoot.get(CONTRACT_TERM),
                    documentSearchCriteria.getDateFrom(),
                    documentSearchCriteria.getDateTo())
            );
        }

        List<DocStatus> docStatus = documentSearchCriteria.getDocStatus();

        if (Objects.nonNull(docStatus)) {

            docStatus
                    .stream()
                    .map(ds -> criteriaBuilder.equal(documentRoot.get(DOC_STATUS), ds))
                    .reduce(criteriaBuilder::or)
                    .ifPresent(predicates::add);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(DocumentPage documentPage,
                          CriteriaQuery<Document> criteriaQuery,
                          Root<Document> documentRoot) {
        if (documentPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(documentRoot.get(documentPage.getSortBy().getCriteriaFieldName())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(documentRoot.get(documentPage.getSortBy().getCriteriaFieldName())));
        }
    }

    private Pageable getPageable(DocumentPage documentPage) {
        Sort sort = Sort.by(documentPage.getSortDirection(), documentPage.getSortBy().getCriteriaFieldName());
        return PageRequest.of(documentPage.getPageNumber(), documentPage.getPageSize(), sort);
    }

    private long getDocumentCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Document> countRoot = countQuery.from(Document.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
