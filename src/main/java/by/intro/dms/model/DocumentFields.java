package by.intro.dms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DocumentFields {

    DOCUMENT_NAME("document_name", "documentName"),
    DOCUMENT_ID("document_id", "documentId"),
    CREATED_AT("created_at", "createdAt"),
    SUPPLIER("supplier", "supplier"),
    CONSUMER("consumer", "consumer"),
    CONTACT_TERM("contact_term", "contactTerm"),
    STATUS("status", "status"),
    DESCRIPTION("description", "description"),
    PRICE("price", "price");

    private final String jooqFieldName;
    private final String criteriaFieldName;

}
