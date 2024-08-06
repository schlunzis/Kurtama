package org.schlunzis.kurtama.server.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.schlunzis.kurtama.common.IUser;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStore implements IUserStore {

    private final UserRepository userRepository;

    @Override
    public UUID createUser(DBUser user) {
        DBUser dbUser = userRepository.save(user);
        return dbUser.getId();
    }

    @Override
    public Optional<DBUser> getUser(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public Optional<DBUser> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean deleteUser(IUser user) {
        try {
            userRepository.deleteById(user.getId());
        } catch (IllegalArgumentException | OptimisticEntityLockException e) {
            return false;
        }
        return true;
    }
}
