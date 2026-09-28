package com.example.shiftplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "swap_requests")
public class SwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Requester is required")
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Employee requester;

    @NotNull(message = "Colleague is required")
    @ManyToOne
    @JoinColumn(name = "colleague_id")
    private Employee colleague;

    @NotNull(message = "Requester shift is required")
    @ManyToOne
    @JoinColumn(name = "requester_shift_id")
    private Shift requesterShift;

    @NotNull(message = "Colleague shift is required")
    @ManyToOne
    @JoinColumn(name = "colleague_shift_id")
    private Shift colleagueShift;

    private String colleagueStatus;

    private String managerStatus;

    public SwapRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getRequester() {
        return requester;
    }

    public void setRequester(Employee requester) {
        this.requester = requester;
    }

    public Employee getColleague() {
        return colleague;
    }

    public void setColleague(Employee colleague) {
        this.colleague = colleague;
    }

    public Shift getRequesterShift() {
        return requesterShift;
    }

    public void setRequesterShift(Shift requesterShift) {
        this.requesterShift = requesterShift;
    }

    public Shift getColleagueShift() {
        return colleagueShift;
    }

    public void setColleagueShift(Shift colleagueShift) {
        this.colleagueShift = colleagueShift;
    }

    public String getColleagueStatus() {
        return colleagueStatus;
    }

    public void setColleagueStatus(String colleagueStatus) {
        this.colleagueStatus = colleagueStatus;
    }

    public String getManagerStatus() {
        return managerStatus;
    }

    public void setManagerStatus(String managerStatus) {
        this.managerStatus = managerStatus;
    }
}