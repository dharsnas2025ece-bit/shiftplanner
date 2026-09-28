package com.example.shiftplanner.service;

import com.example.shiftplanner.entity.Employee;
import com.example.shiftplanner.entity.Roster;
import com.example.shiftplanner.entity.Shift;
import com.example.shiftplanner.entity.SwapRequest;
import com.example.shiftplanner.repository.EmployeeRepository;
import com.example.shiftplanner.repository.RosterRepository;
import com.example.shiftplanner.repository.ShiftRepository;
import com.example.shiftplanner.repository.SwapRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SwapRequestService {

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @Autowired
    private RosterRepository rosterRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ShiftRepository shiftRepository;


    // =========================================================
    // CREATE SWAP REQUEST
    // =========================================================

    public SwapRequest createSwapRequest(SwapRequest swapRequest) {

        if (swapRequest.getRequester() == null ||
                swapRequest.getRequester().getId() == null) {

            throw new RuntimeException(
                    "Requester is required"
            );
        }

        if (swapRequest.getColleague() == null ||
                swapRequest.getColleague().getId() == null) {

            throw new RuntimeException(
                    "Colleague is required"
            );
        }

        if (swapRequest.getRequesterShift() == null ||
                swapRequest.getRequesterShift().getId() == null) {

            throw new RuntimeException(
                    "Requester shift is required"
            );
        }

        if (swapRequest.getColleagueShift() == null ||
                swapRequest.getColleagueShift().getId() == null) {

            throw new RuntimeException(
                    "Colleague shift is required"
            );
        }


        Long requesterId =
                swapRequest.getRequester().getId();

        Long colleagueId =
                swapRequest.getColleague().getId();

        Long requesterShiftId =
                swapRequest.getRequesterShift().getId();

        Long colleagueShiftId =
                swapRequest.getColleagueShift().getId();


        // =====================================================
        // CHECK 1 - SAME EMPLOYEE
        // =====================================================

        if (requesterId.equals(colleagueId)) {

            throw new RuntimeException(
                    "Requester and colleague cannot be the same employee"
            );
        }


        // =====================================================
        // CHECK 2 - SAME SHIFT
        // =====================================================

        if (requesterShiftId.equals(colleagueShiftId)) {

            throw new RuntimeException(
                    "Cannot request a swap using the same shift"
            );
        }


        // =====================================================
        // GET EMPLOYEES FROM DATABASE
        // =====================================================

        Employee requester =
                employeeRepository.findById(requesterId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Requester employee not found"
                                )
                        );

        Employee colleague =
                employeeRepository.findById(colleagueId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Colleague employee not found"
                                )
                        );


        // =====================================================
        // GET SHIFTS FROM DATABASE
        // =====================================================

        Shift requesterShift =
                shiftRepository.findById(requesterShiftId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Requester shift not found"
                                )
                        );

        Shift colleagueShift =
                shiftRepository.findById(colleagueShiftId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Colleague shift not found"
                                )
                        );


        // =====================================================
        // CHECK 3 - REQUESTER MUST HAVE THAT SHIFT
        // =====================================================

        Roster requesterRoster =
                findRoster(requesterId, requesterShiftId);

        if (requesterRoster == null) {

            throw new RuntimeException(
                    "Requester does not have the selected shift in the roster"
            );
        }


        // =====================================================
        // CHECK 4 - COLLEAGUE MUST HAVE THAT SHIFT
        // =====================================================

        Roster colleagueRoster =
                findRoster(colleagueId, colleagueShiftId);

        if (colleagueRoster == null) {

            throw new RuntimeException(
                    "Colleague does not have the selected shift in the roster"
            );
        }


        // =====================================================
        // CHECK 5 - BOTH SHIFTS MUST NOT HAVE SAME TIMING
        // =====================================================

        if (requesterShift.getShiftDate()
                .equals(colleagueShift.getShiftDate())
                &&
                requesterShift.getStartTime()
                        .equals(colleagueShift.getStartTime())
                &&
                requesterShift.getEndTime()
                        .equals(colleagueShift.getEndTime())) {

            throw new RuntimeException(
                    "Cannot request a swap because both shifts have the same date and time"
            );
        }


        // =====================================================
        // CREATE REQUEST
        // =====================================================

        SwapRequest request = new SwapRequest();

        request.setRequester(requester);
        request.setColleague(colleague);

        request.setRequesterShift(requesterShift);
        request.setColleagueShift(colleagueShift);

        request.setColleagueStatus("PENDING");
        request.setManagerStatus("PENDING");

        return swapRequestRepository.save(request);
    }


    // =========================================================
    // GET ALL SWAP REQUESTS
    // =========================================================

    public List<SwapRequest> getAllSwapRequests() {

        return swapRequestRepository.findAll();
    }


    // =========================================================
    // GET SWAP REQUEST BY ID
    // =========================================================

    public Optional<SwapRequest> getSwapRequestById(Long id) {

        return swapRequestRepository.findById(id);
    }


    // =========================================================
    // UPDATE SWAP REQUEST
    // =========================================================

    public SwapRequest updateSwapRequest(
            Long id,
            SwapRequest swapRequest) {

        Optional<SwapRequest> existingRequest =
                swapRequestRepository.findById(id);

        if (existingRequest.isPresent()) {

            SwapRequest request =
                    existingRequest.get();

            request.setRequester(
                    swapRequest.getRequester()
            );

            request.setColleague(
                    swapRequest.getColleague()
            );

            request.setRequesterShift(
                    swapRequest.getRequesterShift()
            );

            request.setColleagueShift(
                    swapRequest.getColleagueShift()
            );

            return swapRequestRepository.save(request);
        }

        return null;
    }


    // =========================================================
    // COLLEAGUE ACCEPT
    // =========================================================

    public SwapRequest colleagueAccept(Long id) {

        SwapRequest request =
                findRequest(id);

        if (!"PENDING".equals(
                request.getColleagueStatus())) {

            throw new RuntimeException(
                    "This swap request is no longer pending"
            );
        }

        request.setColleagueStatus("ACCEPTED");

        return swapRequestRepository.save(request);
    }


    // =========================================================
    // COLLEAGUE REJECT
    // =========================================================

    public SwapRequest colleagueReject(Long id) {

        SwapRequest request =
                findRequest(id);

        if (!"PENDING".equals(
                request.getColleagueStatus())) {

            throw new RuntimeException(
                    "This swap request is no longer pending"
            );
        }

        request.setColleagueStatus("REJECTED");

        request.setManagerStatus("REJECTED");

        return swapRequestRepository.save(request);
    }


    // =========================================================
    // MANAGER APPROVE
    // =========================================================

    public SwapRequest managerApprove(Long id) {

        SwapRequest request =
                findRequest(id);


        // Manager cannot approve before colleague accepts

        if (!"ACCEPTED".equals(
                request.getColleagueStatus())) {

            throw new RuntimeException(
                    "Manager cannot approve before colleague accepts the swap"
            );
        }


        // Manager cannot approve twice

        if ("APPROVED".equals(
                request.getManagerStatus())) {

            throw new RuntimeException(
                    "This swap request is already approved"
            );
        }


        Long requesterId =
                request.getRequester().getId();

        Long colleagueId =
                request.getColleague().getId();

        Long requesterShiftId =
                request.getRequesterShift().getId();

        Long colleagueShiftId =
                request.getColleagueShift().getId();


        // Find current roster entries

        Roster requesterRoster =
                findRoster(
                        requesterId,
                        requesterShiftId
                );

        Roster colleagueRoster =
                findRoster(
                        colleagueId,
                        colleagueShiftId
                );


        if (requesterRoster == null) {

            throw new RuntimeException(
                    "Requester roster not found"
            );
        }


        if (colleagueRoster == null) {

            throw new RuntimeException(
                    "Colleague roster not found"
            );
        }


        // =====================================================
        // CHECK OVERLAPPING SHIFT AFTER SWAP
        // =====================================================

        Shift newRequesterShift =
                colleagueRoster.getShift();

        Shift newColleagueShift =
                requesterRoster.getShift();


        if (hasOverlappingShift(
                requesterId,
                newRequesterShift,
                requesterRoster.getId())) {

            throw new RuntimeException(
                    "Swap cannot be approved because requester would have overlapping shifts"
            );
        }


        if (hasOverlappingShift(
                colleagueId,
                newColleagueShift,
                colleagueRoster.getId())) {

            throw new RuntimeException(
                    "Swap cannot be approved because colleague would have overlapping shifts"
            );
        }


        // =====================================================
        // EXCHANGE SHIFTS
        // =====================================================

        requesterRoster.setShift(
                request.getColleagueShift()
        );

        colleagueRoster.setShift(
                request.getRequesterShift()
        );


        rosterRepository.save(requesterRoster);

        rosterRepository.save(colleagueRoster);


        // =====================================================
        // UPDATE REQUEST STATUS
        // =====================================================

        request.setManagerStatus("APPROVED");

        return swapRequestRepository.save(request);
    }


    // =========================================================
    // MANAGER REJECT
    // =========================================================

    public SwapRequest managerReject(Long id) {

        SwapRequest request =
                findRequest(id);

        if (!"ACCEPTED".equals(
                request.getColleagueStatus())) {

            throw new RuntimeException(
                    "Manager can reject only after colleague responds"
            );
        }

        request.setManagerStatus("REJECTED");

        return swapRequestRepository.save(request);
    }


    // =========================================================
    // FIND SWAP REQUEST
    // =========================================================

    private SwapRequest findRequest(Long id) {

        Optional<SwapRequest> request =
                swapRequestRepository.findById(id);

        if (request.isEmpty()) {

            throw new RuntimeException(
                    "Swap request not found"
            );
        }

        return request.get();
    }


    // =========================================================
    // FIND ROSTER FOR EMPLOYEE + SHIFT
    // =========================================================

    private Roster findRoster(
            Long employeeId,
            Long shiftId) {

        List<Roster> rosters =
                rosterRepository.findAll();

        for (Roster roster : rosters) {

            if (roster.getEmployee() == null ||
                    roster.getShift() == null) {

                continue;
            }

            if (roster.getEmployee().getId()
                    .equals(employeeId)
                    &&
                    roster.getShift().getId()
                            .equals(shiftId)) {

                return roster;
            }
        }

        return null;
    }


    // =========================================================
    // CHECK OVERLAPPING SHIFTS
    // =========================================================

    private boolean hasOverlappingShift(
            Long employeeId,
            Shift newShift,
            Long ignoredRosterId) {

        List<Roster> rosters =
                rosterRepository.findAll();

        for (Roster roster : rosters) {

            if (roster.getEmployee() == null ||
                    roster.getShift() == null) {

                continue;
            }

            // Ignore the roster that is being swapped

            if (roster.getId()
                    .equals(ignoredRosterId)) {

                continue;
            }

            // Only check the same employee

            if (!roster.getEmployee().getId()
                    .equals(employeeId)) {

                continue;
            }

            Shift existingShift =
                    roster.getShift();


            // Only check same date

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

                return true;
            }
        }

        return false;
    }


    // =========================================================
    // DELETE
    // =========================================================

    public void deleteSwapRequest(Long id) {

        swapRequestRepository.deleteById(id);
    }
}