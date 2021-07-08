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
            predicates.add(criteriaBuilder.like(documentRoot.get("documentName"),
                    "%" + documentSearchCriteria.getDocumentName() + "%")
            );
        }

        if (Objects.nonNull(documentSearchCriteria.getConsumer())) {
            predicates.add(criteriaBuilder.equal(documentRoot.get("consumer"),
                    documentSearchCriteria.getConsumer())
            );
        }

        if (Objects.nonNull(documentSearchCriteria.getDateFrom())
                && Objects.nonNull(documentSearchCriteria.getDateTo())) {
            predicates.add(criteriaBuilder.between(documentRoot.get("contractTerm"),
                    documentSearchCriteria.getDateFrom(),
                    documentSearchCriteria.getDateTo())
            );
        }

        List<DocStatus> docStatus = documentSearchCriteria.getDocStatus();

        if (Objects.nonNull(docStatus)) {

            List<Predicate> listStatus = new ArrayList<>();
            for (int i = 0; i < docStatus.size(); i++) {
                listStatus.add(criteriaBuilder.equal(documentRoot.get("docStatus"), docStatus.get(i)));
            }

            Predicate docStatusPred = listStatus.stream().reduce(criteriaBuilder::or).get();

            predicates.add(docStatusPred);

        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(DocumentPage documentPage,
                          CriteriaQuery<Document> criteriaQuery,
                          Root<Document> documentRoot) {
        if (documentPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(documentRoot.get(documentPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(documentRoot.get(documentPage.getSortBy())));
        }
    }

    private Pageable getPageable(DocumentPage documentPage) {
        Sort sort = Sort.by(documentPage.getSortDirection(), documentPage.getSortBy());
        return PageRequest.of(documentPage.getPageNumber(), documentPage.getPageSize(), sort);
    }

    private long getDocumentCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Document> countRoot = countQuery.from(Document.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
