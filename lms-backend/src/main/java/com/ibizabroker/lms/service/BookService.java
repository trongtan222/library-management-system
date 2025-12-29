package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.AuthorRepository;
import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dao.CategoryRepository;
import com.ibizabroker.lms.dto.BookCreateDto;
import com.ibizabroker.lms.dto.BookUpdateDto;
import com.ibizabroker.lms.entity.Author;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.entity.Category;
import com.ibizabroker.lms.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BooksRepository booksRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }
    
    @Transactional(readOnly = true)
        public Books getBookById(Integer id) {
            return booksRepository.findById(id)
                 .orElseThrow(() -> new NotFoundException("Book with id " + id + " does not exist."));
        }
    
    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Books createBook(BookCreateDto dto) {
        Books book = new Books();
        book.setName(dto.getName());
        book.setNumberOfCopiesAvailable(dto.getNumberOfCopiesAvailable());
        book.setPublishedYear(dto.getPublishedYear());
        book.setIsbn(dto.getIsbn());
        book.setCoverUrl(dto.getCoverUrl());

        Set<Author> authors = authorRepository.findByIdIn(dto.getAuthorIds());
        if(authors.size() != dto.getAuthorIds().size()) {
            throw new NotFoundException("Một hoặc nhiều ID tác giả không tìm thấy.");
        }
        book.setAuthors(authors);

        Set<Category> categories = categoryRepository.findByIdIn(dto.getCategoryIds());
         if(categories.size() != dto.getCategoryIds().size()) {
            throw new NotFoundException("Một hoặc nhiều ID thể loại không tìm thấy.");
        }
        book.setCategories(categories);

        return booksRepository.save(book);
    }

    public Books updateBook(Integer id, BookUpdateDto dto) {
        Books book = getBookById(id);

        if (dto.getName() != null) book.setName(dto.getName());
        if (dto.getNumberOfCopiesAvailable() != null) book.setNumberOfCopiesAvailable(dto.getNumberOfCopiesAvailable());
        if (dto.getPublishedYear() != null) book.setPublishedYear(dto.getPublishedYear());
        if (dto.getIsbn() != null) book.setIsbn(dto.getIsbn());
        if (dto.getCoverUrl() != null) book.setCoverUrl(dto.getCoverUrl());

        if (dto.getAuthorIds() != null && !dto.getAuthorIds().isEmpty()) {
            Set<Author> authors = authorRepository.findByIdIn(dto.getAuthorIds());
            book.setAuthors(authors);
        }
        
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            Set<Category> categories = categoryRepository.findByIdIn(dto.getCategoryIds());
            book.setCategories(categories);
        }
        
        return booksRepository.save(book);
    }

    public void deleteBook(Integer id) {
        Books book = getBookById(id);
        booksRepository.delete(book);
    }
}