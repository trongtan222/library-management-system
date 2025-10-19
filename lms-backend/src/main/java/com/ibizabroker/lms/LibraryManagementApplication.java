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
import java.util.Random;

@SpringBootApplication
@EnableScheduling
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
                admin.setEmail("admin@lms.com");
                admin.getRoles().add(roleAdmin);
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                Users u = new Users();
                u.setUsername("user");
                u.setPassword(passwordEncoder.encode("user123"));
                u.setName("Normal User");
                u.setEmail("user@lms.com");
                u.getRoles().add(roleUser);
                userRepository.save(u);
            }
        };
    }

    @Bean
    @Transactional
    CommandLineRunner initBooks(BooksRepository booksRepository) {
        return args -> {
            if (booksRepository.count() == 0) {
                System.out.println("Seeding a large list of initial books with Vietnamese genres...");

                List<Books> initialBooks = List.of(
                    // Classics & Fiction
                    createBook("To Kill a Mockingbird", "Harper Lee", "Tiểu thuyết", 1960, "978-0061120084", 5),
                    createBook("1984", "George Orwell", "Phản địa đàng", 1949, "978-0451524935", 7),
                    createBook("The Great Gatsby", "F. Scott Fitzgerald", "Tiểu thuyết", 1925, "978-0743273565", 4),
                    createBook("The Lord of the Rings", "J.R.R. Tolkien", "Giả tưởng", 1954, "978-0618640157", 6),
                    createBook("Pride and Prejudice", "Jane Austen", "Lãng mạn", 1813, "978-1503290563", 8),
                    createBook("The Catcher in the Rye", "J.D. Salinger", "Tiểu thuyết", 1951, "978-0316769488", 3),
                    createBook("Moby Dick", "Herman Melville", "Phiêu lưu", 1851, "978-1503280786", 4),
                    createBook("War and Peace", "Leo Tolstoy", "Tiểu thuyết lịch sử", 1869, "978-1420952138", 2),
                    createBook("The Alchemist", "Paulo Coelho", "Giả tưởng", 1988, "978-0061122416", 15),

                    // Non-Fiction & History
                    createBook("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "Lịch sử", 2011, "978-0062316097", 10),
                    createBook("Educated", "Tara Westover", "Hồi ký", 2018, "978-0399590504", 6),
                    createBook("Becoming", "Michelle Obama", "Hồi ký", 2018, "978-1524763138", 8),
                    createBook("The Diary of a Young Girl", "Anne Frank", "Tiểu sử", 1947, "978-0553296983", 5),
                    createBook("A Short History of Nearly Everything", "Bill Bryson", "Khoa học", 2003, "978-0767908184", 7),
                    createBook("The Guns of August", "Barbara W. Tuchman", "Lịch sử", 1962, "978-0345386236", 4),

                    // Sci-Fi & Fantasy
                    createBook("Dune", "Frank Herbert", "Khoa học viễn tưởng", 1965, "978-0441013593", 6),
                    createBook("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "Khoa học viễn tưởng", 1979, "978-0345391803", 9),
                    createBook("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Giả tưởng", 1997, "978-0590353427", 12),
                    createBook("A Game of Thrones", "George R.R. Martin", "Giả tưởng", 1996, "978-0553593716", 5),
                    createBook("Fahrenheit 451", "Ray Bradbury", "Phản địa đàng", 1953, "978-1451673319", 6),
                    createBook("Brave New World", "Aldous Huxley", "Phản địa đàng", 1932, "978-0060850524", 5),
                    createBook("The Name of the Wind", "Patrick Rothfuss", "Giả tưởng", 2007, "978-0756404741", 8),

                    // Self-Help & Business
                    createBook("The 7 Habits of Highly Effective People", "Stephen R. Covey", "Phát triển bản thân", 1989, "978-1982137274", 10),
                    createBook("How to Win Friends and Influence People", "Dale Carnegie", "Phát triển bản thân", 1936, "978-0671027032", 11),
                    createBook("Rich Dad Poor Dad", "Robert T. Kiyosaki", "Tài chính", 1997, "978-1612680194", 8),
                    createBook("Thinking, Fast and Slow", "Daniel Kahneman", "Tâm lý học", 2011, "978-0374533557", 5),
                    createBook("Atomic Habits", "James Clear", "Phát triển bản thân", 2018, "978-0735211292", 15),
                    createBook("The Lean Startup", "Eric Ries", "Kinh doanh", 2011, "978-0307887894", 9),

                    // Technology & Computer Science
                    createBook("Clean Code", "Robert C. Martin", "Công nghệ phần mềm", 2008, "978-0132350884", 7),
                    createBook("The Pragmatic Programmer", "Andrew Hunt", "Công nghệ phần mềm", 1999, "978-0201616224", 6),
                    createBook("Design Patterns", "Erich Gamma", "Công nghệ phần mềm", 1994, "978-0201633610", 4),
                    createBook("Introduction to Algorithms", "Thomas H. Cormen", "Khoa học máy tính", 1990, "978-0262033848", 3),
                    createBook("Artificial Intelligence: A Modern Approach", "Stuart Russell", "Trí tuệ nhân tạo", 1995, "978-0136042594", 4),
                    createBook("Cracking the Coding Interview", "Gayle Laakmann McDowell", "Khoa học máy tính", 2015, "978-0984782857", 10),

                    // Thriller & Mystery
                    createBook("The Girl with the Dragon Tattoo", "Stieg Larsson", "Trinh thám", 2005, "978-0307473479", 7),
                    createBook("Gone Girl", "Gillian Flynn", "Trinh thám", 2012, "978-0307588371", 8),
                    createBook("The Da Vinci Code", "Dan Brown", "Bí ẩn", 2003, "978-0307474278", 10),
                    createBook("And Then There Were None", "Agatha Christie", "Bí ẩn", 1939, "978-0312330873", 9),

                    // Philosophy
                    createBook("Meditations", "Marcus Aurelius", "Triết học", 180, "978-0140449334", 6),
                    createBook("Thus Spoke Zarathustra", "Friedrich Nietzsche", "Triết học", 1883, "978-0140441185", 3),
                    createBook("Sophie's World", "Jostein Gaarder", "Triết học", 1991, "978-0374530716", 5),

                    // Vietnamese Literature
                    createBook("Số Đỏ", "Vũ Trọng Phụng", "Trào phúng", 1936, "978-6049692408", 7),
                    createBook("Dế Mèn Phiêu Lưu Ký", "Tô Hoài", "Thiếu nhi", 1941, "978-6042058315", 15),
                    createBook("Tắt Đèn", "Ngô Tất Tố", "Hiện thực xã hội", 1937, "978-6049533367", 6),
                    createBook("Cho Tôi Xin Một Vé Đi Tuổi Thơ", "Nguyễn Nhật Ánh", "Thiếu nhi", 2008, "978-6042188708", 20),
                    createBook("Đất Rừng Phương Nam", "Đoàn Giỏi", "Phiêu lưu", 1957, "978-6042083232", 10)
                );

                booksRepository.saveAll(initialBooks);
                System.out.println("Finished seeding " + initialBooks.size() + " books.");
            }
        };
    }

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
}