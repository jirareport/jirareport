package br.com.jiratorio.domain.issue

data class MinimalIssuePeriod(
    val id: Long,
    val name: String,
    val jql: String,
    val leadTime: Double,
    val throughput: Int,
    val wipAvg: Double,
    val avgPctEfficiency: Double,
)

/*
            SELECT DISTINCT issuePeriod
            FROM IssuePeriodEntity issuePeriod
            LEFT JOIN FETCH issuePeriod.columnTimeAverages   
            LEFT JOIN FETCH issuePeriod.issues issues
            LEFT JOIN FETCH issues.leadTimes leadTimes
            LEFT JOIN FETCH leadTimes.leadTimeConfig
            WHERE issuePeriod.board.id = :boardId
            AND issuePeriod.startDate >= :startDate
            AND issuePeriod.endDate <= :endDate
            ORDER BY issuePeriod.startDate 
 */
