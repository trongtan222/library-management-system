package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.BookLocationRepository;
import com.ibizabroker.lms.dto.BookLocationDto;
import com.ibizabroker.lms.entity.BookLocation;
import com.ibizabroker.lms.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Feature 4: Location Tracking Service
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookLocationService {

    private final BookLocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<BookLocation> getAllLocations() {
        return locationRepository.findAll();
    }

    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public BookLocation getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy vị trí với ID: " + id));
    }

    @Transactional(readOnly = true)
    public BookLocation getLocationByShelfCode(String shelfCode) {
        return locationRepository.findByShelfCode(shelfCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy vị trí với mã kệ: " + shelfCode));
    }

    @Transactional(readOnly = true)
    public List<BookLocation> getLocationsByFloor(Integer floor) {
        return locationRepository.findByFloor(floor);
    }

    @Transactional(readOnly = true)
    public List<BookLocation> searchLocations(Integer floor, String zone, String shelfCode) {
        return locationRepository.findWithFilters(floor, zone, shelfCode);
    }

    @Transactional(readOnly = true)
    public List<Integer> getAllFloors() {
        return locationRepository.findAllFloors();
    }

    @Transactional(readOnly = true)
    public List<String> getAllZones() {
        return locationRepository.findAllZones();
    }

    public BookLocation createLocation(BookLocationDto dto) {
        BookLocation location = new BookLocation();
        location.setFloor(dto.getFloor());
        location.setZone(dto.getZone());
        location.setShelfCode(dto.getShelfCode());
        location.setRowNumber(dto.getRowNumber());
        location.setPosition(dto.getPosition());
        location.setDescription(dto.getDescription());
        return locationRepository.save(location);
    }

    @SuppressWarnings("null")
    public BookLocation updateLocation(Long id, BookLocationDto dto) {
        BookLocation location = getLocationById(id);
        if (dto.getFloor() != null) location.setFloor(dto.getFloor());
        if (dto.getZone() != null) location.setZone(dto.getZone());
        if (dto.getShelfCode() != null) location.setShelfCode(dto.getShelfCode());
        if (dto.getRowNumber() != null) location.setRowNumber(dto.getRowNumber());
        if (dto.getPosition() != null) location.setPosition(dto.getPosition());
        if (dto.getDescription() != null) location.setDescription(dto.getDescription());
        return locationRepository.save(location);
    }

    @SuppressWarnings("null")
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy vị trí với ID: " + id);
        }
        locationRepository.deleteById(id);
    }
}
