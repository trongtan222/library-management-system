package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dto.BookLocationDto;
import com.ibizabroker.lms.entity.BookLocation;
import com.ibizabroker.lms.service.BookLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Feature 4: Location Tracking Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookLocationController {

    private final BookLocationService locationService;

    // ============ PUBLIC ENDPOINTS ============

    @GetMapping("/public/locations")
    public ResponseEntity<List<BookLocation>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/public/locations/{id}")
    public ResponseEntity<BookLocation> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @GetMapping("/public/locations/search")
    public ResponseEntity<List<BookLocation>> searchLocations(
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String shelfCode) {
        return ResponseEntity.ok(locationService.searchLocations(floor, zone, shelfCode));
    }

    @GetMapping("/public/locations/shelf/{shelfCode}")
    public ResponseEntity<BookLocation> getLocationByShelfCode(@PathVariable String shelfCode) {
        return ResponseEntity.ok(locationService.getLocationByShelfCode(shelfCode));
    }

    @GetMapping("/public/locations/floors")
    public ResponseEntity<List<Integer>> getAllFloors() {
        return ResponseEntity.ok(locationService.getAllFloors());
    }

    @GetMapping("/public/locations/zones")
    public ResponseEntity<List<String>> getAllZones() {
        return ResponseEntity.ok(locationService.getAllZones());
    }

    // ============ ADMIN ENDPOINTS ============

    @PostMapping("/admin/locations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookLocation> createLocation(@Valid @RequestBody BookLocationDto dto) {
        return ResponseEntity.ok(locationService.createLocation(dto));
    }

    @PutMapping("/admin/locations/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookLocation> updateLocation(
            @PathVariable Long id,
            @RequestBody BookLocationDto dto) {
        return ResponseEntity.ok(locationService.updateLocation(id, dto));
    }

    @DeleteMapping("/admin/locations/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok(Map.of("message", "Xóa vị trí thành công"));
    }
}
