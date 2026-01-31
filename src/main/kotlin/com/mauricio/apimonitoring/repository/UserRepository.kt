package com.mauricio.apimonitoring.repository

import com.mauricio.apimonitoring.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID>{

    fun findByEmail(email: String): UserEntity?

}
