package org.schlunzis.kurtama.server.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserStore implements IUserStore {

    private final UserRepository userRepository;

    @Override
    public UUID createUser(DBUser user) {
        if (userRepository.count() == 0) // ensure that the first user is an admin
            user.getRoles().add(Role.ADMIN);
        DBUser dbUser = userRepository.save(user);
        return dbUser.getId();
    }

    @Override
    public Optional<DBUser> getUser(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public Optional<DBUser> getUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<DBUser> getUser(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public boolean deleteUser(IUser user) {
        return deleteUser(user.getId());
    }

    @Override
    public boolean deleteUser(UUID id) {
        try {
            userRepository.deleteById(id);
        } catch (IllegalArgumentException | OptimisticEntityLockException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<IUser> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> (IUser) user)
                .collect(Collectors.toList());
    }
}
