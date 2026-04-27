package com.mauricio.apimonitoring.repository

import com.mauricio.apimonitoring.domain.ApiCheckHistoryEntity
import com.mauricio.apimonitoring.domain.MonitoredApiEntity
import com.mauricio.apimonitoring.enum.StatusApiEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ApiCheckHistoryRepository : JpaRepository<ApiCheckHistoryEntity, UUID>{

    fun findTop10ByApiIdOrderByCheckedAtDesc(apiId: UUID?): List<ApiCheckHistoryEntity>

    @Modifying
    @Query("DELETE FROM ApiCheckHistoryEntity h WHERE h.api = :api")
    fun deleteByApi(@Param("api") api: MonitoredApiEntity)

}
