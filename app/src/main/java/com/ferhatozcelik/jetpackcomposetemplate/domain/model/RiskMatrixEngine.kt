package com.ferhatozcelik.jetpackcomposetemplate.domain.model

enum class Severity(val weight: Int) {
    NEGLIGIBLE(1),
    MINOR(2),
    MODERATE(3),
    MAJOR(4),
    CATASTROPHIC(5)
}

enum class Probability(val weight: Int) {
    RARE(1),
    UNLIKELY(2),
    POSSIBLE(3),
    LIKELY(4),
    FREQUENT(5)
}

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

data class RiskResult(
    val score: Int,
    val level: RiskLevel
)

object RiskMatrixEngine {
    fun calculateRiskScore(severity: Severity, probability: Probability): RiskResult {
        val score = severity.weight * probability.weight
        val level = when (score) {
            in 1..4 -> RiskLevel.LOW
            in 5..9 -> RiskLevel.MEDIUM
            in 10..15 -> RiskLevel.HIGH
            else -> RiskLevel.CRITICAL
        }
        return RiskResult(score, level)
    }
}
