package com.ayd2.library.services.book;

import com.ayd2.library.dto.books.*;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.mappers.book.BookMapper;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.models.author.Author;
import com.ayd2.library.models.book.Book;
import com.ayd2.library.models.publisher.Publisher;
import com.ayd2.library.repositories.author.AuthorRepository;
import com.ayd2.library.repositories.book.BookRepository;
import com.ayd2.library.repositories.publisher.PublisherRepository;
import com.ayd2.library.services.s3.S3Service;
import com.ayd2.library.specifications.BookSpecs;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.exception.SdkException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final S3Service s3Service;
    private final BookMapper bookMapper;
    private final GenericPageMapper bookPageMapper;

    @Override
    public BookResponseDTO createBook(NewBookRequestDTO bookRequestDTO)
            throws DuplicatedEntityException, NotFoundException, IOException, SdkException {
        String cleanedIsbn = bookRequestDTO.isbn().replaceAll("-", "");
        if (bookRepository.existsByIsbn(bookRequestDTO.isbn())) {
            throw new DuplicatedEntityException("A book with the ISBN '" + bookRequestDTO.isbn() + "' already exists.");
        }

        if (bookRepository.existsByCode(bookRequestDTO.code())) {
            throw new DuplicatedEntityException("A book with the code '" + bookRequestDTO.code() + "' already exists.");
        }

        MultipartFile imageFile = bookRequestDTO.imageFile();

        Author author = authorRepository.findById(bookRequestDTO.authorId())
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + bookRequestDTO.authorId()));

        Publisher publisher = publisherRepository.findById(bookRequestDTO.publisherId())
                .orElseThrow(
                        () -> new NotFoundException("Publisher not found with id: " + bookRequestDTO.publisherId()));

        Book book = bookMapper.toBook(bookRequestDTO);

        book.setAvailableCopies(book.getQuantity());
        book.setIsbn(cleanedIsbn);
        book.setAuthor(author);
        book.setPublisher(publisher);

        if (imageFile != null && !imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String imageUrl = s3Service.uploadFile(uuid.toString(), imageFile);
            book.setImageUrl(imageUrl);
        }

        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookResponseDTO(savedBook);
    }

    @Override
    public BookResponseDTO updateBook(UUID id, UpdateBookRequestDTO bookRequestDTO)
            throws NotFoundException, IOException, SdkException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));

        if (bookRequestDTO.authorId() != null) {
            Author author = authorRepository.findById(bookRequestDTO.authorId())
                    .orElseThrow(() -> new NotFoundException("Author not found with id: " + bookRequestDTO.authorId()));
            existingBook.setAuthor(author);
        }

        if (bookRequestDTO.publisherId() != null) {
            Publisher publisher = publisherRepository.findById(bookRequestDTO.publisherId())
                    .orElseThrow(() -> new NotFoundException(
                            "Publisher not found with id: " + bookRequestDTO.publisherId()));
            existingBook.setPublisher(publisher);
        }

        if (bookRequestDTO.quantity() != null) {
            int quantityDifference = bookRequestDTO.quantity() - existingBook.getQuantity();
            int newAvailableCopies = existingBook.getAvailableCopies() + quantityDifference;
            existingBook.setAvailableCopies(newAvailableCopies);
        }

        MultipartFile imageFile = bookRequestDTO.imageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String imageUrl = s3Service.uploadFile(uuid.toString(), imageFile);
            existingBook.setImageUrl(imageUrl);
        }

        bookMapper.updateBookFromDTO(bookRequestDTO, existingBook);

        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toBookResponseDTO(updatedBook);
    }

    @Override
    public BookResponseDetailDTO getBook(UUID id) throws NotFoundException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        return bookMapper.toBookResponseDetailDTO(book);
    }

    @Override
    public PagedResponseDTO<BookResponseDTO> getAllBooks(Pageable pageable) {
        Page<Book> page = bookRepository.findAll(pageable);
        Page<BookResponseDTO> dtoPage = page.map(bookMapper::toBookResponseDTO);
        return bookPageMapper.toPagedResponse(dtoPage);
    }

    @Override
    public void deleteBook(UUID id) throws NotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found with id: " + id);

        }
        bookRepository.deleteById(id);
    }

    @Override
    public PagedResponseDTO<BookResponseDTO> searchBooks(BookSearchRequestDTO request, Pageable pageable) {
        Specification<Book> spec = Specification
                .where(BookSpecs.titleContains(request.stringSearch()))
                .or(BookSpecs.codeEquals(request.stringSearch()))
                .or(BookSpecs.isbnEquals(request.stringSearch()))
                .and(BookSpecs.minPrice(request.minPrice()))
                .and(BookSpecs.maxPrice(request.maxPrice()))
                .and(BookSpecs.minQuantity(request.minQuantity()))
                .and(BookSpecs.maxQuantity(request.maxQuantity()))
                .and(BookSpecs.minAvailableCopies(request.minAvailableCopies()))
                .and(BookSpecs.maxAvailableCopies(request.maxAvailableCopies()))
                .and(BookSpecs.publicationDateAfter(request.publicationStartDate()))
                .and(BookSpecs.publicationDateBefore(request.publicationEndDate()))
                .and(BookSpecs.hasAuthorIds(request.authorIds()))
                .and(BookSpecs.hasPublisherIds(request.publisherIds()));

        Page<Book> page = bookRepository.findAll(spec, pageable);
        Page<BookResponseDTO> dtoPage = page.map(bookMapper::toBookResponseDTO);

        return bookPageMapper.toPagedResponse(dtoPage);
    }

}
