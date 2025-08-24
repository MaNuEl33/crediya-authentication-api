package co.com.crediya.api.dtos;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponseDto {
    Integer status;
    String message;
}
