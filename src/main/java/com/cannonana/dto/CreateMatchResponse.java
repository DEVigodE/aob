package com.cannonana.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMatchResponse {
    public String matchId;
    public int hostPort;
}
