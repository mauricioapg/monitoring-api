package com.mauricio.apimonitoring.repository

import com.mauricio.apimonitoring.domain.ApiCheckHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ApiCheckHistoryRepository : JpaRepository<ApiCheckHistoryEntity, UUID>{

}
