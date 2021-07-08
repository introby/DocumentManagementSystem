package by.intro.dms.repository;

import by.intro.dms.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>,
        JpaSpecificationExecutor<Document> {

    @Query(value = "SELECT d FROM Document d WHERE d.documentName LIKE %?1% " +
            "OR d.supplier LIKE %?1% " +
            "OR d.consumer LIKE %?1% " +
            "OR cast(d.createdAt AS text) LIKE %?1% " +
            "OR cast(d.contractTerm AS text) LIKE %?1% " +
            "OR cast(d.documentId AS text) LIKE %?1%")
    Page<Document> findByKeyword(String findValue, PageRequest pageRequest);

}
