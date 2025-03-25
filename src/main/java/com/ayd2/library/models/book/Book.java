package com.ayd2.library.models.book;

import com.ayd2.library.models.author.Author;
import com.ayd2.library.models.publisher.Publisher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column()
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_book_author"))
    private Author author;

    @ManyToOne
    @JoinColumn(name = "publisher_id", foreignKey = @ForeignKey(name = "fk_book_publisher"))
    private Publisher publisher;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column
    private Integer quantity;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column
    private Integer availableCopies;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "image_url", length = 255)
    private String imageUrl;
}