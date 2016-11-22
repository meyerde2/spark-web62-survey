package app.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
public class User {

    @Getter @Setter int id;
    @Getter @Setter String username;
    @Getter @Setter String firstname;
    @Getter @Setter String lastname;
    @Getter @Setter String salt;
    @Getter @Setter String hashedPassword;
    @Getter @Setter int role;
    @Getter @Setter int openGameId;

    public User(int id, String username, String salt, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.firstname = null;
        this.lastname = null;
        this.role = 0;
        this.openGameId = 0;
    }

}
