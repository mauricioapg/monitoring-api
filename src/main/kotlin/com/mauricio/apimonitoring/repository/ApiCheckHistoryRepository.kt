package com.mauricio.apimonitoring.repository

import com.mauricio.apimonitoring.domain.ApiCheckHistoryEntity
import com.mauricio.apimonitoring.enum.StatusApiEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ApiCheckHistoryRepository : JpaRepository<ApiCheckHistoryEntity, UUID>{

    fun findTop1ByIdAndStatusOrderByCheckedAtDesc(id: UUID?, status: StatusApiEnum): ApiCheckHistoryEntity?

    fun findTop10ByApiIdOrderByCheckedAtDesc(apiId: UUID?): List<ApiCheckHistoryEntity>

}
