package com.mauricio.apimonitoring.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mauricio.apimonitoring.enum.HttpMethodEnum
import jakarta.persistence.*
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
    @JsonIgnoreProperties(
        value = [
            "hibernateLazyInitializer",
            "handler"
        ]
    )
    var user: UserEntity,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var url: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var method: HttpMethodEnum,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "monitored_api_headers",
        joinColumns = [JoinColumn(name = "api_id")]
    )
    var headers: MutableList<HeaderEmbeddable> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "monitored_api_params",
        joinColumns = [JoinColumn(name = "api_id")]
    )
    var params: MutableList<ParamsEmbeddable> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "monitored_api_responsible_emails",
        joinColumns = [JoinColumn(name = "api_id")]
    )
    @Column(name = "email")
    var responsibleEmails: MutableList<String> = mutableListOf(),

    @Column(name = "expected_status", nullable = false)
    var expectedStatus: Int,

    @Column(name = "timeout_ms", nullable = false)
    var timeoutMs: Int,

    @Column(name = "interval_minutes", nullable = false)
    var intervalMinutes: Int,

    @Column(name = "max_failure_threshold", nullable = false)
    var maxFailureThreshold: Int,

    @Column(name = "time_to_set_offline", nullable = false)
    var timeToSetOffline: Int? = 3,

    @Column(nullable = false)
    var active: Boolean = true,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)