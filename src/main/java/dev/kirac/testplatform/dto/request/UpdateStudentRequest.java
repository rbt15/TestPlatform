package dev.kirac.testplatform.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStudentRequest {
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Size(max = 20, message = "Student number must be less than 20 characters")
    private String studentNumber;
}
