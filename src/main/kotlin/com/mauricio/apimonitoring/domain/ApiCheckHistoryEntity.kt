package com.mauricio.apimonitoring.domain

import com.mauricio.apimonitoring.enum.StatusApiEnum
import jakarta.persistence.*
import org.springframework.http.HttpStatus
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

    @Column(name = "response_time_ms")
    var responseTimeMs: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: StatusApiEnum,

    var message: String? = null,

    var details: String? = null,

    @Column(name = "checked_at", nullable = false)
    var checkedAt: LocalDateTime = LocalDateTime.now()
)
