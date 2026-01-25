package com.uniClub.Club.clubDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveClubDTO {
    private String clubName;
    private Long eventCount;
}
