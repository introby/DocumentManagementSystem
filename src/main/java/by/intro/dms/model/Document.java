package by.intro.dms.model;

import by.intro.dms.converter.LocalDateStringConverter;
import by.intro.dms.converter.StringLocalDateConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "documents")
@SQLDelete(sql = "UPDATE documents SET deleted = true WHERE document_id=?")
@FilterDef(name = "deletedDocumentFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedDocumentFilter", condition = "deleted = :isDeleted")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 200, message = "Name should be between 1 and 200 characters")
    @Column(name = "document_name")
    private String documentName;
    @JsonSerialize(converter = LocalDateStringConverter.class)
    @JsonDeserialize(converter = StringLocalDateConverter.class)
    @Column(name = "created_at")
    private LocalDate createdAt;
    @NotEmpty(message = "Supplier should not be empty")
    @Size(min = 1, max = 200, message = "Supplier should be between 1 and 200 characters")
    @Column(name = "supplier")
    private String supplier;
    @NotEmpty(message = "Consumer should not be empty")
    @Size(min = 1, max = 200, message = "Consumer should be between 1 and 200 characters")
    @Column(name = "consumer")
    private String consumer;
    @JsonSerialize(converter = LocalDateStringConverter.class)
    @JsonDeserialize(converter = StringLocalDateConverter.class)
    @NotNull(message = "Contract term should not be empty")
    @Column(name = "contract_term")
    private LocalDate contractTerm;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private DocStatus docStatus;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Float price = 0.0f;
    @Transient
    private CurrencyEnum currencyEnum = CurrencyEnum.EUR;
    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;


    public Document(String documentName, LocalDate createdAt, String supplier, String consumer,
                    LocalDate contractTerm, DocStatus docStatus, String description, Float price, CurrencyEnum currencyEnum) {
        this.documentName = documentName;
        this.createdAt = createdAt;
        this.supplier = supplier;
        this.consumer = consumer;
        this.contractTerm = contractTerm;
        this.docStatus = docStatus;
        this.description = description;
        this.price = price;
        this.currencyEnum = currencyEnum;
    }

    public Document() {

    }
}
