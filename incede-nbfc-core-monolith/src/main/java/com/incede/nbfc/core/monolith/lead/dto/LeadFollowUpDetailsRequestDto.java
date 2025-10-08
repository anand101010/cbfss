package com.incede.nbfc.core.monolith.lead.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class LeadFollowUpDetailsRequestDto
{


        @NotNull(message = "StaffId ID must not be Null")
        private int staffId;

        @NotNull(message = "Follow-up type ID must not be null")
        private UUID followUpTypeId;

        @NotNull(message = "Follow-up date must not be null")
        private LocalDate followUpDate;

        private LocalDate nextFollowUpDate;

        private String followUpNotes;

        private Boolean isActive;



        public int getStaffId() {
            return staffId;
        }

        public void setStaffId(int staffId) {
            this.staffId = staffId;
        }
        public UUID getFollowUpTypeId() {
            return followUpTypeId;
        }
        public void setFollowUpTypeId(UUID followUpTypeId) {
            this.followUpTypeId = followUpTypeId;
        }

        public LocalDate getFollowUpDate() {
            return followUpDate;
        }
        public void setFollowUpDate(LocalDate followUpDate) {
            this.followUpDate = followUpDate;
        }

        public LocalDate getNextFollowUpDate() {
            return nextFollowUpDate;
        }
        public void setNextFollowUpDate(LocalDate nextFollowUpDate) {
            this.nextFollowUpDate = nextFollowUpDate;
        }

        public String getFollowUpNotes() {
            return followUpNotes;
        }
        public void setFollowUpNotes(String followUpNotes) {
            this.followUpNotes = followUpNotes;
        }

        public Boolean getIsActive() {
            return isActive;
        }
        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }
    }



