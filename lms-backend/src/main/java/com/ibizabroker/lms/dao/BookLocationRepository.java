package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.BookLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookLocationRepository extends JpaRepository<BookLocation, Long> {

    Optional<BookLocation> findByShelfCode(String shelfCode);
    
    List<BookLocation> findByFloor(Integer floor);
    
    List<BookLocation> findByZone(String zone);
    
    List<BookLocation> findByFloorAndZone(Integer floor, String zone);
    
    @Query("SELECT DISTINCT b.floor FROM BookLocation b ORDER BY b.floor")
    List<Integer> findAllFloors();
    
    @Query("SELECT DISTINCT b.zone FROM BookLocation b WHERE b.zone IS NOT NULL ORDER BY b.zone")
    List<String> findAllZones();
    
    @Query("SELECT b FROM BookLocation b WHERE " +
           "(:floor IS NULL OR b.floor = :floor) AND " +
           "(:zone IS NULL OR b.zone = :zone) AND " +
           "(:shelfCode IS NULL OR b.shelfCode LIKE %:shelfCode%)")
    List<BookLocation> findWithFilters(
        @Param("floor") Integer floor,
        @Param("zone") String zone,
        @Param("shelfCode") String shelfCode
    );
}
