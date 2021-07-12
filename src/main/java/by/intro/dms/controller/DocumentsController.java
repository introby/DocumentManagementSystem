package by.intro.dms.controller;

import by.intro.dms.model.Document;
import by.intro.dms.service.DocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/documents")
public class DocumentsController {

    private final DocumentService documentService;

    public DocumentsController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('accounts:read')")
    public String createDocumentForm(Document document) {
        return "documents/new";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('accounts:read')")
    public String createDocument(@Valid Document document, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "documents/new";

        document.setCreatedAt(LocalDate.now());
        documentService.saveDocument(document);
        return "redirect:/documents";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('accounts:read')")
    public String viewDocument(@PathVariable("id") Long id, Model model) {
        Document document = documentService.findById(id);
        model.addAttribute("document", document);
        return "documents/view";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('accounts:read')")
    public String editDocumentForm(@PathVariable("id") Long id, Model model) {
        Document document = documentService.findById(id);
        model.addAttribute("document", document);
        return "documents/edit";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('accounts:read')")
    public String editDocument(@Valid Document document, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "documents/edit";

        documentService.saveDocument(document);
        return "redirect:/documents";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('accounts:read')")
    public String deleteUser(@PathVariable("id") Long id) {
        documentService.deleteById(id);
        return "redirect:/documents";
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('accounts:read')")
    public String findByKeyword(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("sortDir") Optional<String> sortDir,
            @RequestParam("name") Optional<String> name,
            @RequestParam("keyword") Optional<String> keyword
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String sortField = sort.orElse("documentId");
        String direction = sortDir.orElse("ASC");
        String findName = name.orElse("documentName");
        String searchValue = keyword.orElse("_");

        Page<Document> allDocs = documentService.findByKeyword(
                false,
                searchValue,
                PageRequest.of(
                        currentPage - 1,
                        pageSize,
                        Sort.Direction.fromString(direction),
                        sortField
                )
        );

        model.addAttribute("allDocs", allDocs);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", direction);
        model.addAttribute("reverseSortDir", "ASC".equals(direction) ? "DESC" : "ASC");
        model.addAttribute("searchValue", searchValue);

        int totalPages = allDocs.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "documents/documents";

    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('accounts:write')")
    public String findDeletedByKeyword(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("sort") Optional<String> sort,
            @RequestParam("sortDir") Optional<String> sortDir,
            @RequestParam("name") Optional<String> name,
            @RequestParam("keyword") Optional<String> keyword
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String sortField = sort.orElse("documentId");
        String direction = sortDir.orElse("ASC");
        String findName = name.orElse("documentName");
        String searchValue = keyword.orElse("_");

        Page<Document> deletedDocs = documentService.findByKeyword(
                true,
                searchValue,
                PageRequest.of(
                        currentPage - 1,
                        pageSize,
                        Sort.Direction.fromString(direction),
                        sortField
                )
        );

        model.addAttribute("deletedDocs", deletedDocs);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", direction);
        model.addAttribute("reverseSortDir", "ASC".equals(direction) ? "DESC" : "ASC");
        model.addAttribute("searchValue", searchValue);

        int totalPages = deletedDocs.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "documents/deleted";

    }
}
