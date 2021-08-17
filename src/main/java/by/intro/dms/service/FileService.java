package by.intro.dms.service;

import by.intro.dms.exception.NoEntityException;
import by.intro.dms.mapper.FileMapperImpl;
import by.intro.dms.model.file.File;
import by.intro.dms.model.file.dto.FileDto;
import by.intro.dms.repository.FileRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class.getName());
    private static final String TOPIC_NAME = "new-files";

    private final FileRepository fileRepository;
    private final FileMapperImpl fileMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public FileService(FileRepository fileRepository,
                       FileMapperImpl fileMapper,
                       @Qualifier("kafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) { //KafkaTemplate from autoconfiguration
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<FileDto> findAll() {
        List<File> files = fileRepository.findAll();
        return fileMapper.toDtoList(files);
    }

    public FileDto findById(ObjectId id) throws NoEntityException {
        File file = fileRepository.findById(id).orElseThrow(() -> new NoEntityException("File not found"));
        return fileMapper.fileToFileDto(file);
    }

    public FileDto add(FileDto fileDto) {
        File file = fileMapper.dtoToFile(fileDto);
        File insertedFile = fileRepository.insert(file);
        sendMessage(fileDto);
        return fileMapper.fileToFileDto(insertedFile);
    }

    public FileDto update(ObjectId id, FileDto fileDto) throws NoEntityException {
        FileDto fileDtoForEditing = findById(id);
        fileDtoForEditing.setCreationDate(fileDto.getCreationDate());
        fileDtoForEditing.setContent(fileDto.getContent());
        File file = fileMapper.dtoToFile(fileDtoForEditing);
        File savedFile = fileRepository.save(file);
        return fileMapper.fileToFileDto(savedFile);
    }

    public void delete(ObjectId id) {
        fileRepository.deleteById(id);
    }

    public void sendMessage(FileDto dto) {
        kafkaTemplate.send(TOPIC_NAME, dto);
    }

    @KafkaListener(id = "Files", topics = {TOPIC_NAME})
    public void consume(FileDto dto) {
        LOG.info("=> consumed {}", dto);
    }
}
