package com.ibizabroker.lms;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.List;

@SpringBootApplication
@EnableScheduling // <-- THÊM DÒNG NÀY
public class LibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner initUsers(UsersRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            // Logic tạo Role và User (giữ nguyên)
            Role roleAdmin = roleRepository.findByRoleName("ROLE_ADMIN").orElseGet(() -> {
                Role r = new Role();
                r.setRoleName("ROLE_ADMIN");
                return roleRepository.save(r);
            });
            Role roleUser = roleRepository.findByRoleName("ROLE_USER").orElseGet(() -> {
                Role r = new Role();
                r.setRoleName("ROLE_USER");
                return roleRepository.save(r);
            });

            if (userRepository.findByUsername("admin").isEmpty()) {
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setName("Admin User");
                admin.getRoles().add(roleAdmin);
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                Users u = new Users();
                u.setUsername("user");
                u.setPassword(passwordEncoder.encode("user123"));
                u.setName("Normal User");
                u.getRoles().add(roleUser);
                userRepository.save(u);
            }
        };
    }

    // === CẬP NHẬT BEAN NÀY VỚI DANH SÁCH SÁCH RẤT LỚN ===
    @Bean
    @Transactional
    CommandLineRunner initBooks(BooksRepository booksRepository) {
        return args -> {
            if (booksRepository.count() == 0) {
                System.out.println("Seeding a large list of initial books...");

                List<Books> initialBooks = List.of(
                    // Classics & Fiction
                    createBook("To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, "978-0061120084", 5),
                    createBook("1984", "George Orwell", "Dystopian", 1949, "978-0451524935", 7),
                    createBook("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 1925, "978-0743273565", 4),
                    createBook("The Lord of the Rings", "J.R.R. Tolkien", "Fantasy", 1954, "978-0618640157", 6),
                    createBook("Pride and Prejudice", "Jane Austen", "Romance", 1813, "978-1503290563", 8),
                    createBook("The Catcher in the Rye", "J.D. Salinger", "Fiction", 1951, "978-0316769488", 3),

                    // Non-Fiction & History
                    createBook("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "History", 2011, "978-0062316097", 10),
                    createBook("Educated", "Tara Westover", "Memoir", 2018, "978-0399590504", 6),
                    createBook("Becoming", "Michelle Obama", "Memoir", 2018, "978-1524763138", 8),
                    createBook("The Diary of a Young Girl", "Anne Frank", "Biography", 1947, "978-0553296983", 5),
                    createBook("A Short History of Nearly Everything", "Bill Bryson", "Science", 2003, "978-0767908184", 7),

                    // Sci-Fi & Fantasy
                    createBook("Dune", "Frank Herbert", "Science Fiction", 1965, "978-0441013593", 6),
                    createBook("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "Science Fiction", 1979, "978-0345391803", 9),
                    createBook("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Fantasy", 1997, "978-0590353427", 12),
                    createBook("A Game of Thrones", "George R.R. Martin", "Fantasy", 1996, "978-0553593716", 5),
                    createBook("Fahrenheit 451", "Ray Bradbury", "Dystopian", 1953, "978-1451673319", 6),

                    // Self-Help & Business
                    createBook("The 7 Habits of Highly Effective People", "Stephen R. Covey", "Self-Help", 1989, "978-1982137274", 10),
                    createBook("How to Win Friends and Influence People", "Dale Carnegie", "Self-Help", 1936, "978-0671027032", 11),
                    createBook("Rich Dad Poor Dad", "Robert T. Kiyosaki", "Finance", 1997, "978-1612680194", 8),
                    createBook("Thinking, Fast and Slow", "Daniel Kahneman", "Psychology", 2011, "978-0374533557", 5),
                    createBook("Atomic Habits", "James Clear", "Self-Help", 2018, "978-0735211292", 15),

                    // Technology & Computer Science
                    createBook("Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", "Software", 2008, "978-0132350884", 7),
                    createBook("The Pragmatic Programmer: Your Journey to Mastery", "Andrew Hunt", "Software", 1999, "978-0201616224", 6),
                    createBook("Design Patterns: Elements of Reusable Object-Oriented Software", "Erich Gamma", "Software", 1994, "978-0201633610", 4),
                    createBook("Introduction to Algorithms", "Thomas H. Cormen", "Computer Science", 1990, "978-0262033848", 3),
                    createBook("Artificial Intelligence: A Modern Approach", "Stuart Russell", "AI", 1995, "978-0136042594", 4)
                );

                booksRepository.saveAll(initialBooks);
                System.out.println("Finished seeding " + initialBooks.size() + " books.");
            }
        };
    }

    // Hàm tiện ích để tạo đối tượng sách
    private Books createBook(String name, String author, String genre, int year, String isbn, int copies) {
        Books book = new Books();
        book.setName(name);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPublishedYear(year);
        book.setIsbn(isbn);
        book.setNumberOfCopiesAvailable(copies);
        return book;
    }

    // Cần một constructor cho Role để làm cho code ở trên gọn hơn
    // Bạn có thể thêm constructor này vào file lms/entity/Role.java
    /*
    public Role(String roleName) {
        this.roleName = roleName;
    }
    */
}