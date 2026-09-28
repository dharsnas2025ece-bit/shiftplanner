package com.example.shiftplanner.repository;
import com.example.shiftplanner.entity.Roster;
import com.example.shiftplanner.repository.RosterRepository;
import com.example.shiftplanner.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RosterRepository extends JpaRepository<Roster, Long> {

}