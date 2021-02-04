package engine.service;

import engine.dao.UserRepository;
import engine.entity.User;
import engine.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    UserRepository repository;

    public boolean addUser(User user) {
        User userFromDB = repository.findByEmail(user.getEmail());
        if (userFromDB != null) {
            throw new UserAlreadyExistException();
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
            repository.save(user);
            return true;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
