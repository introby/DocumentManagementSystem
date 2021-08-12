package by.intro.dms.service.impl;

import by.intro.dms.mapper.DocumentMapper;
import by.intro.dms.model.*;
import by.intro.dms.model.request.DocumentRequest;
import by.intro.dms.model.response.DocumentsListResponse;
import by.intro.dms.model.tables.Documents;
import by.intro.dms.repository.DocumentCriteriaRepository;
import by.intro.dms.repository.DocumentRepository;
import by.intro.dms.repository.DocumentSpecificationRepository;
import by.intro.dms.service.CurrencyService;
import by.intro.dms.service.DocumentService;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static by.intro.dms.service.PaginationUtils.buildPaginationInfo;

@Service
public class DocumentServiceJooqImpl extends DocumentService {

    private final DSLContext dsl;
    private final CurrencyService currencyService;

    public DocumentServiceJooqImpl(DocumentRepository documentRepository,
                                   EntityManager entityManager,
                                   DocumentCriteriaRepository documentCriteriaRepository,
                                   DocumentSpecificationRepository documentSpecificationRepository,
                                   DocumentMapper documentMapper,
                                   DSLContext dsl,
                                   @Qualifier("feignCurrencyServiceImpl") CurrencyService currencyService) {
        super(documentRepository, entityManager, documentCriteriaRepository, documentSpecificationRepository, documentMapper);
        this.dsl = dsl;
        this.currencyService = currencyService;
    }

    @Override
    public DocumentsListResponse getDocumentsSpecification(DocumentRequest documentRequest) {
        DocumentPage documentPage = documentRequest.getDocumentPage();
        if (Objects.isNull(documentPage)) {
            documentPage = new DocumentPage();
        }
        int pageSize = documentPage.getPageSize();
        int pageNumber = documentPage.getPageNumber();
        Sort.Direction sortDirection = documentPage.getSortDirection();
        Field<Object> sortField = DSL.field(documentPage.getSortBy().getJooqFieldName());
        List<Document> documents = dsl.selectFrom(Documents.DOCUMENTS)
                .where(condition(documentRequest))
                .orderBy(setSortingDirection(sortField, sortDirection))
                .limit(pageSize).offset(pageSize * pageNumber)
                .fetch()
                .into(Document.class);

        int totalElements = findCountByExpression(documentRequest);
        int totalPages = (int) Math.ceil(1.0 * totalElements / pageSize);

        CurrencyEnum currencyEnum = documentRequest.getDocumentSearchCriteria().getCurrencyEnum();
        List<Document> finalDocumentList;
        if (Objects.nonNull(currencyEnum) && currencyEnum != CurrencyEnum.EUR) {
            finalDocumentList = priceConverter(documents, currencyEnum);
        } else {
            finalDocumentList = documents;
        }

        return DocumentsListResponse.builder()
                .documents(documentMapper.toDtoList(finalDocumentList))
                .paginationInfo(buildPaginationInfo(totalElements, totalPages, pageNumber, pageSize))
                .currency(documentRequest.getDocumentSearchCriteria().getCurrencyEnum().name())
                .build();
    }

    private int findCountByExpression(DocumentRequest documentRequest) {
        return dsl.fetchCount(dsl.select()
                .from(Documents.DOCUMENTS)
                .where(condition(documentRequest))
        );
    }

    @NotNull
    private List<Document> priceConverter(List<Document> documents, CurrencyEnum currencyEnum) {

        documents.forEach(document -> {
            document.setPrice(document.getPrice() * currencyService.convertCurrency(currencyEnum));
            document.setCurrencyEnum(currencyEnum);
        });
        return documents;
    }

    public Condition condition(DocumentRequest documentRequest) {
        DocumentSearchCriteria documentSearchCriteria = documentRequest.getDocumentSearchCriteria();
        String documentName = documentSearchCriteria.getDocumentName();
        String consumer = documentSearchCriteria.getConsumer();
        LocalDate dateFrom = documentSearchCriteria.getDateFrom();
        LocalDate dateTo = documentSearchCriteria.getDateTo();
        List<DocStatus> docStatus = documentSearchCriteria.getDocStatus();

        Condition result = DSL.trueCondition();
        if (documentName != null) {
            result = result.and(Documents.DOCUMENTS.DOCUMENT_NAME.likeIgnoreCase("%" + documentName + "%"));
        }
        if (consumer != null) {
            result = result.and(Documents.DOCUMENTS.CONSUMER.equalIgnoreCase(consumer));
        }
        if (dateFrom != null && dateTo != null) {
            result = result.and(Documents.DOCUMENTS.CONTRACT_TERM.between(dateFrom, dateTo));
        }
        if (docStatus != null) {
            result = result.and(Documents.DOCUMENTS.STATUS.in(docStatus));
        }
        return result;
    }

    public SortField<?> setSortingDirection(Field field, Sort.Direction sortDirection) {
        if (sortDirection == Sort.Direction.ASC) {
            return field.asc();
        }
        else {
            return field.desc();
        }
    }

}
