package com.example.shiftplanner.service;

import com.example.shiftplanner.entity.Roster;
import com.example.shiftplanner.entity.Shift;
import com.example.shiftplanner.repository.RosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RosterService {

    @Autowired
    private RosterRepository rosterRepository;

    public Roster createRoster(Roster roster) {

        if (roster.getEmployee() == null ||
                roster.getEmployee().getId() == null) {

            throw new RuntimeException(
                    "Employee is required"
            );
        }

        if (roster.getShift() == null ||
                roster.getShift().getId() == null) {

            throw new RuntimeException(
                    "Shift is required"
            );
        }

        List<Roster> existingRosters =
                rosterRepository.findAll();

        for (Roster existingRoster : existingRosters) {

            if (existingRoster.getEmployee() == null ||
                    existingRoster.getShift() == null) {
                continue;
            }

            if (!existingRoster.getEmployee().getId()
                    .equals(roster.getEmployee().getId())) {
                continue;
            }

            Shift existingShift =
                    existingRoster.getShift();

            Shift newShift =
                    roster.getShift();

            if (!existingShift.getShiftDate()
                    .equals(newShift.getShiftDate())) {
                continue;
            }

            boolean overlaps =
                    newShift.getStartTime()
                            .isBefore(
                                    existingShift.getEndTime()
                            )
                            &&
                            newShift.getEndTime()
                                    .isAfter(
                                            existingShift.getStartTime()
                                    );

            if (overlaps) {

                throw new RuntimeException(
                        "Employee already has an overlapping shift on this day"
                );
            }
        }

        if (roster.getStatus() == null ||
                roster.getStatus().isBlank()) {

            roster.setStatus("ASSIGNED");
        }

        return rosterRepository.save(roster);
    }

    public List<Roster> getAllRosters() {
        return rosterRepository.findAll();
    }

    public Optional<Roster> getRosterById(Long id) {
        return rosterRepository.findById(id);
    }

    public Roster updateRoster(
            Long id,
            Roster roster) {

        Optional<Roster> existingRoster =
                rosterRepository.findById(id);

        if (existingRoster.isPresent()) {

            Roster r = existingRoster.get();

            r.setEmployee(roster.getEmployee());
            r.setShift(roster.getShift());
            r.setStatus(roster.getStatus());

            return rosterRepository.save(r);
        }

        return null;
    }

    public void deleteRoster(Long id) {

        if (!rosterRepository.existsById(id)) {

            throw new RuntimeException(
                    "Roster not found"
            );
        }

        rosterRepository.deleteById(id);
    }
}