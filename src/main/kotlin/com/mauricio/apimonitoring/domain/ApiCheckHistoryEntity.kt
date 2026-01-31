package com.mauricio.apimonitoring.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "api_check_history")
class ApiCheckHistoryEntity(

    @Id
    @GeneratedValue
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id", nullable = false)
    var api: MonitoredApiEntity,

    @Column(name = "status_code")
    var statusCode: Int? = null,

    @Column(name = "response_time_ms")
    var responseTimeMs: Int? = null,

    @Column(nullable = false)
    var success: Boolean,

    @Column(name = "checked_at", nullable = false)
    var checkedAt: LocalDateTime = LocalDateTime.now()
)
