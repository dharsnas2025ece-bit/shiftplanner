package com.example.shiftplanner.controller;

import com.example.shiftplanner.entity.Roster;
import com.example.shiftplanner.service.RosterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rosters")
public class RosterController {

    @Autowired
    private RosterService rosterService;

    // Create Roster
    @PostMapping
    public Roster createRoster(
            @Valid @RequestBody Roster roster) {

        return rosterService.createRoster(roster);
    }

    // Get All Rosters
    @GetMapping
    public List<Roster> getAllRosters() {

        return rosterService.getAllRosters();
    }

    // Get Roster By ID
    @GetMapping("/{id}")
    public ResponseEntity<Roster> getRosterById(
            @PathVariable Long id) {

        return rosterService.getRosterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update Roster
    @PutMapping("/{id}")
    public ResponseEntity<Roster> updateRoster(
            @PathVariable Long id,
            @Valid @RequestBody Roster roster) {

        Roster updatedRoster =
                rosterService.updateRoster(id, roster);

        if (updatedRoster != null) {
            return ResponseEntity.ok(updatedRoster);
        }

        return ResponseEntity.notFound().build();
    }

    // Delete Roster
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoster(
            @PathVariable Long id) {

        rosterService.deleteRoster(id);

        return ResponseEntity.ok(
                "Roster deleted successfully");
    }
}