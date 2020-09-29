package com.realestate.security.authentication;

import com.google.common.collect.Lists;
import com.realestate.model.people.Client;
import com.realestate.model.people.RealEstateAgent;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.RealEstateAgentRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.realestate.security.ApplicationUserRole.*;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final RealEstateAgentRepository agentRepository;

    public ApplicationUserService(PasswordEncoder passwordEncoder,
                                  ClientRepository clientRepository,
                                  RealEstateAgentRepository agentRepository) {
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return selectApplicationUserByUsername(username).orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username))
                );
    }

    private Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
                new ApplicationUser(
                        "user",
                        passwordEncoder.encode("user"),
                        USER.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "admin",
                        passwordEncoder.encode("admin"),
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );

        List<Client> clients = clientRepository.findAll();
        List<RealEstateAgent> agents = agentRepository.findAll();

        for(Client client: clients) {
            applicationUsers.add(new ApplicationUser(
                    client.getLogin(),
                    passwordEncoder.encode(client.getPassword()),
                    CLIENT.getGrantedAuthorities(),
                    true,
                    true,
                    true,
                    true
            ));
        }

        for(RealEstateAgent agent: agents) {
            applicationUsers.add(new ApplicationUser(
                    agent.getLogin(),
                    passwordEncoder.encode(agent.getPassword()),
                    AGENT.getGrantedAuthorities(),
                    true,
                    true,
                    true,
                    true
            ));
        }

        return applicationUsers;
    }
}
