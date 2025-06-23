package org.example.security;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    // Inner class to build UserDetails
    private static class UserDetailsImpl implements UserDetails {
        private final String username;
        private final String password;

        public UserDetailsImpl(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public static UserDetailsImpl build(User user) {
            return new UserDetailsImpl(
                    user.getUsername(),
                    user.getPassword()
            );
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        // Implement other required UserDetails methods
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return null; // Add roles/authorities if needed
        }
    }
}