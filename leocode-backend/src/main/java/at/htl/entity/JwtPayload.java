package at.htl.entity;

import java.time.LocalDateTime;

public class JwtPayload {
    public LocalDateTime iat;
    public LocalDateTime exp;
    public String iss;

    public JwtPayload(LocalDateTime iat, LocalDateTime exp, String iss) {
        this.iat = iat;
        this.exp = exp;
        this.iss = iss;
    }

    public JwtPayload() {

    }
}
