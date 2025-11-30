package com.uniClub.dto.clubDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubMemberStatsResponse {
    private Long clubId;
    private String clubName;
    private int memberCount;
}
