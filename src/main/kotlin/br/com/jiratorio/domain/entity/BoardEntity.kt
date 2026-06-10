package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.DueDateType
import br.com.jiratorio.domain.ImpedimentType
import br.com.jiratorio.domain.IssuePeriodNameFormat
import br.com.jiratorio.extension.equalsComparing
import br.com.jiratorio.extension.toStringBuilder
import java.util.Objects
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table

@Entity
@Table(name = "board")
data class BoardEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var externalId: Long,

    @Column(nullable = false)
    var name: String,

    var startColumn: String? = null,

    var endColumn: String? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "board_flux_column", joinColumns = [JoinColumn(name = "board_id")])
    @OrderColumn(name = "idx")
    @Column(name = "value", nullable = false)
    var fluxColumn: MutableList<String> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "board_ignore_issue_type", joinColumns = [JoinColumn(name = "board_id")])
    @OrderColumn(name = "idx")
    @Column(name = "value", nullable = false)
    var ignoreIssueType: MutableList<String> = mutableListOf(),

    var epicCF: String? = null,

    var estimateCF: String? = null,

    var systemCF: String? = null,

    var projectCF: String? = null,

    @Column(name = "DUE_DATE_CF")
    var dueDateCF: String? = null,

    var ignoreWeekend: Boolean? = null,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL])
    var leadTimeConfigs: MutableSet<LeadTimeConfigEntity>? = null,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL])
    var holidays: MutableList<HolidayEntity>? = null,

    @Enumerated(EnumType.STRING)
    var impedimentType: ImpedimentType? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "board_impediment_columns", joinColumns = [JoinColumn(name = "board_id")])
    @OrderColumn(name = "idx")
    @Column(name = "value", nullable = false)
    var impedimentColumns: MutableList<String> = mutableListOf(),

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    var dynamicFields: MutableSet<DynamicFieldConfigEntity>? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "board_touching_columns", joinColumns = [JoinColumn(name = "board_id")])
    @OrderColumn(name = "idx")
    @Column(name = "value", nullable = false)
    var touchingColumns: MutableList<String> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "board_waiting_columns", joinColumns = [JoinColumn(name = "board_id")])
    @OrderColumn(name = "idx")
    @Column(name = "value", nullable = false)
    var waitingColumns: MutableList<String> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    var dueDateType: DueDateType? = null,

    var useLastOccurrenceWhenCalculateLeadTime: Boolean = false,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var issuePeriodNameFormat: IssuePeriodNameFormat = IssuePeriodNameFormat.INITIAL_AND_FINAL_DATE,

    var additionalFilter: String? = null

) : BaseEntity() {

    override fun toString() =
        toStringBuilder(
            BoardEntity::id,
            BoardEntity::externalId,
            BoardEntity::name
        )

    override fun equals(other: Any?) =
        equalsComparing(other, BoardEntity::id)

    override fun hashCode() =
        Objects.hash(id)

}
