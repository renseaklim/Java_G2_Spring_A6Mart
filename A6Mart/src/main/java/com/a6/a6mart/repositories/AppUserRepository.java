package com.a6.a6mart.repositories;

import com.a6.a6mart.api.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    public AppUser findByEmail(String email);
    @Query("SELECT COUNT(u) FROM AppUser u WHERE u.role = 'Staff'")
    int countUsersByRoleStaff();
}
