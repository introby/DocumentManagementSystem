package by.intro.dms.repository;

import by.intro.dms.model.*;
import by.intro.dms.model.request.DocumentRequest;
import by.intro.dms.service.CurrencyService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final CurrencyService currencyService;

    public DocumentSpecificationRepository(DocumentRepository documentRepository,
                                           @Qualifier("feignCurrencyServiceImpl") CurrencyService currencyService) {
        this.documentRepository = documentRepository;
        this.currencyService = currencyService;
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

        CurrencyEnum currencyEnum = documentSearchCriteria.getCurrencyEnum();

        Page<Document> result = documentRepository.findAll(
                Specification.where(specificationList.stream().reduce((Specification::and)).get()),
                pageable
        );

        if (Objects.nonNull(currencyEnum) && currencyEnum != CurrencyEnum.EUR) {
            convertPriceToOtherCurrency(result, currencyEnum);
        }
        return result;
    }

    @NotNull
    private Page<Document> convertPriceToOtherCurrency(Page<Document> result, CurrencyEnum currencyEnum) {

        result.stream().forEach(document -> {
            document.setPrice(document.getPrice() * currencyService.convertCurrency(currencyEnum));
            document.setCurrencyEnum(currencyEnum);
        });

        return result;
    }

    private Specification<Document> documentNameLike(String documentName) {

        String docName = String.format("%%%s%%", documentName);

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(Document_.DOCUMENT_NAME), docName);
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
        Sort sort = Sort.by(documentPage.getSortDirection(), documentPage.getSortBy().getCriteriaFieldName());
        return PageRequest.of(documentPage.getPageNumber(), documentPage.getPageSize(), sort);
    }
}
