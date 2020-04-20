package br.com.jiratorio.domain.entity

import br.com.jiratorio.domain.duedate.DueDateType
import br.com.jiratorio.domain.impediment.ImpedimentType
import br.com.jiratorio.domain.issueperiodnameformat.IssuePeriodNameFormat
import br.com.jiratorio.extension.equalsComparing
import br.com.jiratorio.extension.toStringBuilder
import org.hibernate.annotations.Type
import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Board(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var externalId: Long,

    @Column(nullable = false)
    var name: String,

    var startColumn: String? = null,

    var endColumn: String? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var fluxColumn: MutableList<String>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var ignoreIssueType: MutableList<String>? = null,

    var epicCF: String? = null,

    var estimateCF: String? = null,

    var systemCF: String? = null,

    var projectCF: String? = null,

    @Column(name = "DUE_DATE_CF")
    var dueDateCF: String? = null,

    var ignoreWeekend: Boolean? = null,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL])
    var leadTimeConfigs: MutableList<LeadTimeConfig>? = null,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL])
    var holidays: MutableList<Holiday>? = null,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL])
    var issues: MutableList<Issue>? = null,

    @Enumerated(EnumType.STRING)
    var impedimentType: ImpedimentType? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var impedimentColumns: MutableList<String>? = null,

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    var dynamicFields: MutableSet<DynamicFieldConfig>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var touchingColumns: MutableList<String>? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var waitingColumns: MutableList<String>? = null,

    @Enumerated(EnumType.STRING)
    var dueDateType: DueDateType? = null,

    var useLastOccurrenceWhenCalculateLeadTime: Boolean = false,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var issuePeriodNameFormat: IssuePeriodNameFormat = IssuePeriodNameFormat.INITIAL_AND_FINAL_DATE

) : BaseEntity() {

    override fun toString() =
        toStringBuilder(
            Board::id,
            Board::externalId,
            Board::name
        )

    override fun equals(other: Any?) =
        equalsComparing(other, Board::id)

    override fun hashCode() =
        Objects.hash(id)

}
