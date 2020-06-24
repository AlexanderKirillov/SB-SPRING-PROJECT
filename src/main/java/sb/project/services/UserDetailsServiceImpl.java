package sb.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sb.project.domain.Users;
import sb.project.repositories.UsersRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<Users> user = usersRepository.findByUserName(userName);

        user.orElseThrow(() -> new UsernameNotFoundException(userName + " не найден!"));

        return user.map(UserDetailsImpl::new).get();
    }

}
