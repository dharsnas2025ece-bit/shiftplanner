package com.example.shiftplanner.controller;

import com.example.shiftplanner.entity.Shift;
import com.example.shiftplanner.service.ShiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    // Create Shift
    @PostMapping
    public Shift createShift(
            @Valid @RequestBody Shift shift) {

        return shiftService.createShift(shift);
    }

    // Get All Shifts
    @GetMapping
    public List<Shift> getAllShifts() {

        return shiftService.getAllShifts();
    }

    // Get Shift By ID
    @GetMapping("/{id}")
    public ResponseEntity<Shift> getShiftById(
            @PathVariable Long id) {

        return shiftService.getShiftById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update Shift
    @PutMapping("/{id}")
    public ResponseEntity<Shift> updateShift(
            @PathVariable Long id,
            @Valid @RequestBody Shift shift) {

        Shift updatedShift =
                shiftService.updateShift(id, shift);

        if (updatedShift != null) {
            return ResponseEntity.ok(updatedShift);
        }

        return ResponseEntity.notFound().build();
    }


}