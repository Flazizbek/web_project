package uz.pdp.user;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private Long userId;
    private String lastname;
    private String firstname;
    private String email;
    private String password;
    private Long projectId;
}