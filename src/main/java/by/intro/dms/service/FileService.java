package by.intro.dms.service;

import by.intro.dms.exception.NoEntityException;
import by.intro.dms.model.file.File;
import by.intro.dms.repository.FileRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    public File findById(ObjectId id) throws NoEntityException {
        return fileRepository.findById(id).orElseThrow(() -> new NoEntityException("File not found"));
    }

    public File add(File file) {
        return fileRepository.insert(file);
    }

    public File update(File file) {
        return fileRepository.save(file);
    }

    public void delete(ObjectId id) {
        fileRepository.deleteById(id);
    }
}
