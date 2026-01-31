package com.mauricio.apimonitoring.domain

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "monitored_api")
class MonitoredApiEntity(

    @Id
    @GeneratedValue
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var url: String,

    @Column(nullable = false)
    var method: String,

    @ElementCollection
    @CollectionTable(
        name = "monitored_api_headers",
        joinColumns = [JoinColumn(name = "api_id")]
    )
    var headers: MutableList<HeaderEmbeddable> = mutableListOf(),

    @Column(name = "expected_status", nullable = false)
    var expectedStatus: Int,

    @Column(name = "timeout_ms", nullable = false)
    var timeoutMs: Int,

    @Column(name = "interval_minutes", nullable = false)
    var intervalMinutes: Int,

    @Column(nullable = false)
    var active: Boolean = true,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "created_by", nullable = false)
    var createdBy: String
)
