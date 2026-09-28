package com.example.shiftplanner.controller;

import com.example.shiftplanner.entity.SwapRequest;
import com.example.shiftplanner.service.SwapRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swap-requests")
public class SwapRequestController {

    @Autowired
    private SwapRequestService swapRequestService;

    // Create Swap Request
    @PostMapping
    public SwapRequest createSwapRequest(
            @Valid @RequestBody SwapRequest swapRequest) {

        return swapRequestService.createSwapRequest(swapRequest);
    }

    // Get All Swap Requests
    @GetMapping
    public List<SwapRequest> getAllSwapRequests() {

        return swapRequestService.getAllSwapRequests();
    }

    // Get Swap Request By ID
    @GetMapping("/{id}")
    public ResponseEntity<SwapRequest> getSwapRequestById(
            @PathVariable Long id) {

        return swapRequestService.getSwapRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update Swap Request
    @PutMapping("/{id}")
    public ResponseEntity<SwapRequest> updateSwapRequest(
            @PathVariable Long id,
            @Valid @RequestBody SwapRequest swapRequest) {

        SwapRequest updatedRequest =
                swapRequestService.updateSwapRequest(
                        id, swapRequest);

        if (updatedRequest != null) {
            return ResponseEntity.ok(updatedRequest);
        }

        return ResponseEntity.notFound().build();
    }

    // Colleague Accept
    @PutMapping("/{id}/colleague/accept")
    public SwapRequest colleagueAccept(
            @PathVariable Long id) {

        return swapRequestService.colleagueAccept(id);
    }

    // Colleague Reject
    @PutMapping("/{id}/colleague/reject")
    public SwapRequest colleagueReject(
            @PathVariable Long id) {

        return swapRequestService.colleagueReject(id);
    }

    // Manager Approve
    @PutMapping("/{id}/manager/approve")
    public SwapRequest managerApprove(
            @PathVariable Long id) {

        return swapRequestService.managerApprove(id);
    }

    // Manager Reject
    @PutMapping("/{id}/manager/reject")
    public SwapRequest managerReject(
            @PathVariable Long id) {

        return swapRequestService.managerReject(id);
    }

    // Delete Swap Request
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSwapRequest(
            @PathVariable Long id) {

        swapRequestService.deleteSwapRequest(id);

        return ResponseEntity.ok(
                "Swap request deleted successfully");
    }
}