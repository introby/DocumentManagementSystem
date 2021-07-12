package by.intro.dms.repository;

import by.intro.dms.model.*;
import by.intro.dms.model.Currency;
import by.intro.dms.service.CurrencyService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class DocumentSpecificationRepository {

    private final DocumentRepository documentRepository;

    public DocumentSpecificationRepository(DocumentRepository documentRepository, CurrencyService currencyService) {
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

        Currency currency = documentSearchCriteria.getCurrency();

        if (Objects.nonNull(currency) && !"EUR".equals(currency.name())) {
            Map<String, Float> rates = new HashMap<>();
            Page<Document> pageList = documentRepository.findAll(
                    Specification.where(specificationList.stream().reduce((Specification::and)).get()), pageable);
            try {
                rates = CurrencyService.getRates();
            } catch (IOException e) {
                e.printStackTrace();
            }

            float eurToByn = rates.get("EUR") / rates.get("BYN");
            float eurToUsd = rates.get("EUR") / rates.get("USD");

            if ("BYN".equals(currency.name())) {
                pageList.stream().forEach(document -> {
                    document.setPrice(document.getPrice() * eurToByn);
                    document.setCurrency(Currency.BYN);
                });
            } else {
                pageList.stream().forEach(document -> {
                    document.setPrice(document.getPrice() * eurToUsd);
                    document.setCurrency(Currency.USD);
                });
            }

            return pageList;
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
