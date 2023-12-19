package ks.msx.web_page.service;

import ks.msx.web_page.entity.UserDTO;
import ks.msx.web_page.entity.UserEntity;
import ks.msx.web_page.entity.user_permisions.Role;
import ks.msx.web_page.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow();
    }

    public void registerUser(UserDTO userDTO){
        userRepository.save(UserEntity
                .builder()
                        .username(userDTO.getUsername())
                        .password(userDTO.getPassword())
                        .role(Role.USER)
                        .isAccountNonLocked(true)
                .build());
    }

    public void blockUser(Long id){
        UserEntity user = userRepository.findById(id).orElseThrow();
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    public void unblockUser(Long id){
        UserEntity user = userRepository.findById(id).orElseThrow();
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }
}
