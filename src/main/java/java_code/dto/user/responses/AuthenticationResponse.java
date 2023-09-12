package java_code.dto.user.responses;

import lombok.Builder;

@Builder
public record AuthenticationResponse (String accessToken){}
