package com.sp.web;

import com.sp.web.domain.User;
import com.sp.web.domain.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Initialisiert die Datenbank.
 */
@Service
public class InitialiseDatabase implements InitializingBean {
    /**
     * initialises a user Repository.
     */
    private final UserRepository userRepository;
    /**
     * initialisiert die Datenbankverbindung,
     * sodass in afterPropertiesSet daten geändert werden können.
     * @param userRepo es soll auf das userReop zugegriffen werden.
     */
    @Autowired
    public InitialiseDatabase(final UserRepository userRepo) {
        this.userRepository = userRepo;
    }
    /**
     * initialises some users into the database.
     * important: currently also deletes the entire user database everytime
     */
    @Override
    public void afterPropertiesSet() {
        this.userRepository.deleteAll();
        User user = new User("a",
            "{bcrypt}$2a$10$d0Cm9hP08FyuLrVqMs7J6eihqyd19LYp2CCXgi.aQk/dei/XQ1w9C",
            "a@",
            "Jost",
            "as",
            23,
            "Universität Bielefeld"
        );
        user.addRole("ROLE_admin");
        user.setAccountNonLocked(true);
        User usera = new User("Tom3",
            "{bcrypt}$2a$04$xIshy4lNZcjo.330dU1EN.xrBqanX8s.qGmUpgS0p0Gm.7Rh63Tyy",
            "Müller@bla",
            "Thomas",
            "asd",
            24,
            "Universität Bielefeld");
        
        User userb = new User("Jonas2",
            "{bcrypt}$2a$12$AzozDXaHR9gYhQisg0x.deQ5piO3m2OjprZ7Onl4H1Md.kxs4LhcK",
            "email@email.com",
            "Vorname",
            "fds",
            56,
            "Universität Bielefeld");
        this.userRepository.save(user);
        this.userRepository.save(usera);
        this.userRepository.save(userb);


    }
}
