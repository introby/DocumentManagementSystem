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
    private final FileMapperImpl fileMapper;

    public FileController(FileService fileService, FileMapperImpl fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @PostMapping
    public ResponseEntity<FileDto> addFile(@RequestBody final FileDto fileDto) {
        File file = fileService.add(fileMapper.dtoToFile(fileDto));
        return new ResponseEntity<>(fileMapper.fileToFileDto(file), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FileDto>> getFiles() {
        List<File> files = fileService.findAll();
        List<FileDto> dtoList = fileMapper.toDtoList(files);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable final ObjectId id) throws NoEntityException {
        File file = fileService.findById(id);
        return new ResponseEntity<>(fileMapper.fileToFileDto(file), HttpStatus.OK);
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
        File file = fileService.findById(id);
        file.setCreationDate(fileDto.getCreationDate());
        file.setContent(fileDto.getContent());
        File editedFile = fileService.update(file);
        return new ResponseEntity<>(fileMapper.fileToFileDto(editedFile), HttpStatus.OK);
    }
}
