package com.example.libraryapplication.service.serviceImplementation;

import com.example.libraryapplication.dao.*;
import com.example.libraryapplication.dataModel.*;
import com.example.libraryapplication.dto.BookDTO;
import com.example.libraryapplication.service.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final GenreDAO genreDAO;
    private final BookCopyDAO bookCopyDAO;
    private final BookAuthorDAO bookAuthorDAO;
    private final BookGenreDAO bookGenreDAO;

    public BookServiceImpl(BookDAO bookDAO, AuthorDAO authorDAO, GenreDAO genreDAO, BookCopyDAO bookCopyDAO, BookAuthorDAO bookAuthorDAO, BookGenreDAO bookGenreDAO) {
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.genreDAO = genreDAO;
        this.bookCopyDAO = bookCopyDAO;

        this.bookAuthorDAO = bookAuthorDAO;
        this.bookGenreDAO = bookGenreDAO;
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookDAO.getBook(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    @Override
    public void createBook(BookDTO bookDTO) {

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setISBN(bookDTO.getISBN());
        book.setNumberOfCopies(bookDTO.getNumberOfCopies());

        if (bookDTO.getBookAuthorIds() != null && !bookDTO.getBookAuthorIds().isEmpty()) {
            List<Long> uniqueAuthorIds = bookDTO.getBookAuthorIds().stream()
                    .distinct()
                    .toList();

            List<Author> authors = authorDAO.getAllAuthors().stream()
                    .filter(author -> uniqueAuthorIds.contains(author.getId()))
                    .toList();

            if (authors.size() != uniqueAuthorIds.size()) {
                throw new IllegalArgumentException("One or more authors not found");
            }

            List<BookAuthor> bookAuthors = new ArrayList<>();
            for (Author author : authors) {
                BookAuthor bookAuthor = bookAuthorDAO.findByBookIdAndAuthorId(book.getId(), author.getId())
                        .stream()
                        .findFirst()
                        .orElseGet(() -> {
                            BookAuthor newBookAuthor = new BookAuthor();
                            newBookAuthor.setBook(book);
                            newBookAuthor.setAuthor(author);
                            return newBookAuthor;
                        });
                bookAuthors.add(bookAuthor);
            }
            book.setAuthors(bookAuthors);
        }

        if (bookDTO.getGenreIds() != null && !bookDTO.getGenreIds().isEmpty()) {
            List<Long> uniqueGenreIds = bookDTO.getGenreIds().stream()
                    .distinct()
                    .toList();

            List<Genre> genres = genreDAO.getAllGenres().stream()
                    .filter(genre -> uniqueGenreIds.contains(genre.getId()))
                    .toList();

            if (genres.size() != uniqueGenreIds.size()) {
                throw new IllegalArgumentException("One or more genres not found");
            }

            List<BookGenre> bookGenres = new ArrayList<>();
            for (Genre genre : genres) {
                BookGenre bookGenre = bookGenreDAO.getBookGenresByBookIdAndGenreId(book.getId(), genre.getId())
                        .stream()
                        .findFirst()
                        .orElseGet(() -> {
                            BookGenre newBookGenre = new BookGenre();
                            newBookGenre.setBook(book);
                            newBookGenre.setGenre(genre);
                            return newBookGenre;
                        });
                bookGenres.add(bookGenre);
            }
            book.setGenres(bookGenres);
        }

        if (bookDTO.getNumberOfCopies() != null && bookDTO.getNumberOfCopies() >= 0) {
            List<BookCopy> bookCopies = new ArrayList<>();
            for (int i = 0; i < bookDTO.getNumberOfCopies(); i++) {
                BookCopy bookCopy = new BookCopy();
                bookCopy.setBook(book);
                bookCopy.setAvailable(true);
                bookCopies.add(bookCopy);
            }
            book.setBookCopies(bookCopies);
        } else {
            throw new IllegalArgumentException("It must be a positive number");
        }

        bookDAO.saveBook(book);
    }



    @Override
    public void deleteBook(Long bookId) {
        bookDAO.deleteBook(bookId);
    }

    @Override
    public void editBook(Long bookId, BookDTO bookDTO) {
        System.out.println("Editing book: " + bookDTO.getTitle());

        // Retrieve the existing Book by ID or create a new one
        Book book = bookDAO.getBook(bookId).orElse(new Book());

        // Update the Book's details
        book.setTitle(bookDTO.getTitle());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setISBN(bookDTO.getISBN());
        book.setNumberOfCopies(bookDTO.getNumberOfCopies());

        // Handle Authors
        if (bookDTO.getBookAuthorIds() != null && !bookDTO.getBookAuthorIds().isEmpty()) {
            List<Long> uniqueAuthorIds = bookDTO.getBookAuthorIds().stream()
                    .distinct()
                    .toList();

            List<Author> authors = authorDAO.getAllAuthors().stream()
                    .filter(author -> uniqueAuthorIds.contains(author.getId()))
                    .toList();

            if (authors.size() != uniqueAuthorIds.size()) {
                throw new IllegalArgumentException("One or more authors not found");
            }

            // Update BookAuthor objects and set them to the Book
            List<BookAuthor> bookAuthors = new ArrayList<>();
            for (Author author : authors) {
                BookAuthor bookAuthor = bookAuthorDAO.findByBookIdAndAuthorId(book.getId(), author.getId())
                        .stream()
                        .findFirst()
                        .orElseGet(() -> {
                            BookAuthor newBookAuthor = new BookAuthor();
                            newBookAuthor.setBook(book);
                            newBookAuthor.setAuthor(author);
                            return newBookAuthor;
                        });
                bookAuthor.setBook(book);
                bookAuthor.setAuthor(author);
                bookAuthors.add(bookAuthor);
            }
            book.setAuthors(bookAuthors);
        }

        if (bookDTO.getGenreIds() != null && !bookDTO.getGenreIds().isEmpty()) {
            List<Long> uniqueGenreIds = bookDTO.getGenreIds().stream()
                    .distinct()
                    .toList();

            List<Genre> genres = genreDAO.getAllGenres().stream()
                    .filter(genre -> uniqueGenreIds.contains(genre.getId()))
                    .toList();

            if (genres.size() != uniqueGenreIds.size()) {
                throw new IllegalArgumentException("One or more genres not found");
            }

            List<BookGenre> bookGenres = new ArrayList<>();
            for (Genre genre : genres) {
                BookGenre bookGenre = bookGenreDAO.getBookGenresByBookIdAndGenreId(book.getId(), genre.getId())
                        .stream()
                        .findFirst()
                        .orElseGet(() -> {
                            BookGenre newBookGenre = new BookGenre();
                            newBookGenre.setBook(book);
                            newBookGenre.setGenre(genre);
                            return newBookGenre;
                        });
                bookGenre.setBook(book);
                bookGenre.setGenre(genre);
                bookGenres.add(bookGenre);
            }
            book.setGenres(bookGenres);
        }

        // Handle Book Copies
        if (bookDTO.getNumberOfCopies() != null && bookDTO.getNumberOfCopies() >= 0) {
            // If the number of copies has changed, handle it accordingly
            long copiesToAdd = bookDTO.getNumberOfCopies() - book.getNumberOfCopies();
            if (copiesToAdd > 0) {
                for (int i = 0; i < copiesToAdd; i++) {
                    BookCopy bookCopy = new BookCopy();
                    bookCopy.setBook(book);
                    bookCopy.setAvailable(true);
                    // Don't save yet, we will save the whole Book later
                }
            } else if (copiesToAdd < 0) {
                // Remove excess copies (or deactivate them)
                List<BookCopy> bookCopies = book.getBookCopies();
                for (int i = 0; i < Math.abs(copiesToAdd); i++) {
                    BookCopy excessBookCopy = bookCopies.get(i);
                    bookCopyDAO.deleteBookCopy(excessBookCopy.getId());
                }
            }
        } else {
            throw new IllegalArgumentException("It must be a positive number of copies");
        }

        // Save the Book (and its related entities) in one transaction
        System.out.println("Saving updated book with its related entities...");
        bookDAO.saveBook(book);  // Hibernate will handle saving Book, Authors, Genres, and Copies
        System.out.println(book.getBookCopies().size() + " copies updated");
    }

}
