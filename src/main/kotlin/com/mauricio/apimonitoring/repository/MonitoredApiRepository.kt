package com.mauricio.apimonitoring.repository

import com.mauricio.apimonitoring.domain.MonitoredApiEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface MonitoredApiRepository : JpaRepository<MonitoredApiEntity, UUID>{

    fun findByUrl(url: String?): Optional<MonitoredApiEntity>

}
