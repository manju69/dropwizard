package domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<TData, TErrors> {
    private boolean success;
    private String message;
    private TData Data;
    private TErrors errors;

    public static <TData> ApiResponse success(TData data) {
        return new ApiResponse(true, null, data, null);
    }

    public static <TData> ApiResponse success(TData data, String message) {return new ApiResponse(true, message, data, null);}

    public static ApiResponse failed(String message) {
        return new ApiResponse(false, message, null, null);
    }

    public static <TErrors> ApiResponse failed(TErrors errors, String message) {return new ApiResponse(false, message, null, errors);}
}

