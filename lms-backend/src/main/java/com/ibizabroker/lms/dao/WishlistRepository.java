package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(Users user);
    boolean existsByUserAndBook(Users user, Books book);
    Optional<Wishlist> findByUserAndBook(Users user, Books book);
    void deleteByUserAndBook(Users user, Books book);
}