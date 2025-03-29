package com.ayd2.library.services.file;

import com.ayd2.library.dto.file.FileImportErrorDTO;
import com.ayd2.library.dto.file.FileImportResponseDTO;
import com.ayd2.library.models.file.EntityType;
import com.ayd2.library.models.file.FieldType;
import com.ayd2.library.services.book.BookService;
import com.ayd2.library.services.degree.DegreeService;
import com.ayd2.library.services.loan.LoanService;
import com.ayd2.library.services.student.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Service
public class FileImportServiceImpl implements FileImportService {

    private final Map<EntityType, BiFunction<String, EntityType, List<FileImportErrorDTO>>> entityProcessors;

    public FileImportServiceImpl(BookService bookService, DegreeService degreeService,
                                 StudentService studentService, LoanService loanService) {
        this.entityProcessors = Map.of(
            EntityType.LIBRO, bookService::createFromText,
            EntityType.ESTUDIANTE, studentService::createFromText,
            EntityType.PRESTAMO, loanService::createFromText,
            EntityType.CARRERA, degreeService::createFromText
        );
    }

    @Override
    public FileImportResponseDTO importData(MultipartFile file) throws Exception {
        List<FileImportErrorDTO> errors = new ArrayList<>();
        int books = 0, students = 0, loans = 0, degrees = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            EntityType currentEntity = null;
            StringBuilder entityData = new StringBuilder();
            int lineNumber = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                EntityType newEntity = parseEntityType(line);
                if (newEntity != null) {
                    books += processEntity(entityData, currentEntity, errors, lineNumber);
                    currentEntity = newEntity;
                    entityData.setLength(0); // Reset StringBuilder
                }
                entityData.append(line).append("\n");
            }

            books += processEntity(entityData, currentEntity, errors, lineNumber);
        }

        return new FileImportResponseDTO(books, students, loans, degrees, errors);
    }

    private EntityType parseEntityType(String line) {
        return switch (line) {
            case "LIBRO" -> EntityType.LIBRO;
            case "ESTUDIANTE" -> EntityType.ESTUDIANTE;
            case "PRESTAMO" -> EntityType.PRESTAMO;
            case "CARRERA" -> EntityType.CARRERA;
            default -> null;
        };
    }

    private int processEntity(StringBuilder entityData, EntityType entityType, List<FileImportErrorDTO> errors, int lineNumber) {
        if (entityType == null || entityData.length() == 0) return 0;

        boolean success = Optional.ofNullable(entityProcessors.get(entityType))
                .map(processor -> processor.apply(entityData.toString(), errors))
                .orElse(false);

        if (!success) {
            errors.add(new FileImportErrorDTO(lineNumber, null, entityType, "Error processing entity"));
            return 0;
        }
        return 1;
    }
}
