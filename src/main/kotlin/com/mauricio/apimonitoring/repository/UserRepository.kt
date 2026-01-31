package com.mauricio.apimonitoring.repository

import com.mauricio.apimonitoring.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID>
