package com.example.shiftplanner.service;

import com.example.shiftplanner.entity.Shift;
import com.example.shiftplanner.entity.Roster;
import com.example.shiftplanner.repository.ShiftRepository;
import com.example.shiftplanner.repository.RosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private RosterRepository rosterRepository;

    public Shift createShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(Long id) {
        return shiftRepository.findById(id);
    }

    public Shift updateShift(Long id, Shift shift) {

        Optional<Shift> existingShift =
                shiftRepository.findById(id);

        if (existingShift.isPresent()) {

            Shift s = existingShift.get();

            s.setShiftName(shift.getShiftName());
            s.setShiftDate(shift.getShiftDate());
            s.setStartTime(shift.getStartTime());
            s.setEndTime(shift.getEndTime());

            return shiftRepository.save(s);
        }

        return null;
    }

    public void deleteShift(Long id) {

        List<Roster> rosters =
                rosterRepository.findAll();

        for (Roster roster : rosters) {

            if (roster.getShift() != null &&
                    roster.getShift().getId().equals(id)) {

                rosterRepository.deleteById(roster.getId());
            }
        }

        shiftRepository.deleteById(id);
    }
}