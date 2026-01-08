package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.entity.RenewalRequest;
import com.ibizabroker.lms.entity.RenewalStatus;
import com.ibizabroker.lms.service.CirculationService;
import com.ibizabroker.lms.dao.RenewalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/renewals")
@CrossOrigin("http://localhost:4200/")
@RequiredArgsConstructor
public class AdminRenewalController {

    private final RenewalRequestRepository renewalRepo;
    private final CirculationService circulationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RenewalRequest>> list(@RequestParam(required = false) String status){
        if(status == null) return ResponseEntity.ok(renewalRepo.findAll());
        return ResponseEntity.ok(renewalRepo.findByStatus(RenewalStatus.valueOf(status.toUpperCase())));
    }

    @SuppressWarnings("null")
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RenewalRequest> approve(@PathVariable Long id, @RequestBody(required = false) NotePayload payload){
        circulationService.approveRenewal(id, payload!=null?payload.note:null);
        return ResponseEntity.ok(renewalRepo.findById(id).orElseThrow());
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RenewalRequest> reject(@PathVariable Long id, @RequestBody(required = false) NotePayload payload){
        return ResponseEntity.ok(circulationService.rejectRenewal(id, payload!=null?payload.note:null));
    }

    public static class NotePayload { public String note; }
}
