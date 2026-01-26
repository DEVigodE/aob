package com.cannonana.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMatchContainerResponse {
    String matchId;
    int port;
}
