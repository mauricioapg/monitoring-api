package com.mauricio.apimonitoring.repository

import com.mauricio.apimonitoring.domain.MonitoredApiEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MonitoredApiRepository : JpaRepository<MonitoredApiEntity, UUID>
