package com.example.libraryapplication.serviceTest;

import com.example.libraryapplication.dao.BookCopyDAO;
import com.example.libraryapplication.dataModel.BookCopy;
import com.example.libraryapplication.service.serviceImplementation.BookCopyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookCopyServiceImplTest {

    @Mock
    private BookCopyDAO bookCopyDAO;

    @InjectMocks
    private BookCopyServiceImpl bookCopyService;

    private BookCopy bookCopy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookCopy = new BookCopy();
        bookCopy.setId(1L);
        bookCopy.setAvailable(true);
    }

    @Test
    void testGetBookCopyById() {
        
        Long bookCopyId = 1L;
        when(bookCopyDAO.getBookCopyById(bookCopyId)).thenReturn(Optional.of(bookCopy));

        
        Optional<BookCopy> result = bookCopyService.getBookCopyById(bookCopyId);

        
        assertTrue(result.isPresent(), "Book copy should be present");
        assertEquals(bookCopy, result.get(), "The book copy should match the mock object");
        verify(bookCopyDAO, times(1)).getBookCopyById(bookCopyId); // Verify interaction with DAO
    }

    @Test
    void testGetBookCopyByIdNotFound() {
        
        Long bookCopyId = 1L;
        when(bookCopyDAO.getBookCopyById(bookCopyId)).thenReturn(Optional.empty());

        
        Optional<BookCopy> result = bookCopyService.getBookCopyById(bookCopyId);

        
        assertFalse(result.isPresent(), "Book copy should not be present");
        verify(bookCopyDAO, times(1)).getBookCopyById(bookCopyId); // Verify interaction with DAO
    }

    @Test
    void testGetAllBookCopies() {
        
        BookCopy bookCopy2 = new BookCopy();
        bookCopy2.setId(2L);
        bookCopy2.setAvailable(true);

        when(bookCopyDAO.getAllBookCopies()).thenReturn(Arrays.asList(bookCopy, bookCopy2));

        
        List<BookCopy> result = bookCopyService.getAllBookCopies();

        
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "There should be two book copies");
        verify(bookCopyDAO, times(1)).getAllBookCopies(); // Verify interaction with DAO
    }
}
