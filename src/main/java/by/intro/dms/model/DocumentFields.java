package by.intro.dms.model;

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

    String jooqFieldName;
    String criteriaFieldName;

    DocumentFields(String jooqFieldName, String criteriaFieldName) {
        this.jooqFieldName = jooqFieldName;
        this.criteriaFieldName = criteriaFieldName;
    }

    public String getJooqFieldName() {
        return jooqFieldName;
    }

    public String getCriteriaFieldName() {
        return criteriaFieldName;
    }
}
