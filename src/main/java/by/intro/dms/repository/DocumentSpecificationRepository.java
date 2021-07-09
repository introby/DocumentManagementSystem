package by.intro.dms.repository;

import by.intro.dms.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class DocumentSpecificationRepository {

    private final DocumentRepository documentRepository;

    public DocumentSpecificationRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Page<Document> findAllWithSpecification(DocumentRequest documentRequest) {

        Pageable pageable = getPageable(documentRequest.getDocumentPage());
        DocumentSearchCriteria documentSearchCriteria = documentRequest.getDocumentSearchCriteria();
        List<Specification<Document>> specificationList = new ArrayList<>();

        if (Objects.nonNull(documentSearchCriteria.getDocumentName())) {
            specificationList.add(documentNameLike(documentSearchCriteria.getDocumentName()));
        }

        if (Objects.nonNull(documentSearchCriteria.getConsumer())) {
            specificationList.add(consumerEqual(documentSearchCriteria.getConsumer()));
        }

        if (Objects.nonNull(documentSearchCriteria.getDateFrom())
                && Objects.nonNull(documentSearchCriteria.getDateTo())) {
            specificationList.add(dateBetween(documentSearchCriteria.getDateFrom(),
                    documentSearchCriteria.getDateTo()));
        }

        if (Objects.nonNull(documentSearchCriteria.getDocStatus())) {
            specificationList.add(docStatusMultiselect(documentSearchCriteria.getDocStatus()));
        }

        return documentRepository.findAll(
                Specification.where(specificationList.stream().reduce((Specification::and)).get()),
                pageable
        );
    }

    private Specification<Document> documentNameLike(String documentName) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(Document_.DOCUMENT_NAME), "%" + documentName + "%");
    }

    private Specification<Document> consumerEqual(String consumer) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Document_.CONSUMER), consumer);
    }

    private Specification<Document> dateBetween(LocalDate dateFrom, LocalDate dateTo) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(Document_.CONTRACT_TERM), dateFrom, dateTo);
    }

    private Specification<Document> docStatusMultiselect(List<DocStatus> docStatusList) {

        List<Specification<Document>> listStatus = new ArrayList<>();

        for (int i = 0; i < docStatusList.size(); i++) {
            int finalI = i;
            Specification<Document> spec = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(Document_.DOC_STATUS), docStatusList.get(finalI));
            listStatus.add(spec);
        }

        return listStatus.stream().reduce((Specification::or)).get();
    }

    private Pageable getPageable(DocumentPage documentPage) {
        Sort sort = Sort.by(documentPage.getSortDirection(), documentPage.getSortBy());
        return PageRequest.of(documentPage.getPageNumber(), documentPage.getPageSize(), sort);
    }
}
