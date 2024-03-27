package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}