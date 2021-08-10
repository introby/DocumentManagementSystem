package by.intro.dms.controller;

import by.intro.dms.exception.NoEntityException;
import by.intro.dms.mapper.FileMapperImpl;
import by.intro.dms.model.file.File;
import by.intro.dms.model.file.dto.FileDto;
import by.intro.dms.service.FileService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/files", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<FileDto> addFile(@RequestBody final FileDto fileDto) {
        FileDto addedFileDto = fileService.add(fileDto);
        return new ResponseEntity<>(addedFileDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FileDto>> getFiles() {
        List<FileDto> files = fileService.findAll();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable final ObjectId id) throws NoEntityException {
        FileDto fileDto = fileService.findById(id);
        return new ResponseEntity<>((fileDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAuthority('accounts:write')")
    public ResponseEntity<FileDto> deleteFile(@PathVariable final ObjectId id) {
        fileService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<FileDto> editFile(@PathVariable final ObjectId id,
                                            @RequestBody final FileDto fileDto) throws NoEntityException {

        FileDto editedFile = fileService.update(id, fileDto);
        return new ResponseEntity<>(editedFile, HttpStatus.OK);
    }
}
