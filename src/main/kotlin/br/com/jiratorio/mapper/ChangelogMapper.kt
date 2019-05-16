package br.com.jiratorio.mapper

import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.domain.response.ChangelogResponse
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class ChangelogMapper {

    private val dateTimePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    fun changelogToChangelogResponse(changelog: Changelog): ChangelogResponse {
        return ChangelogResponse(
            from = changelog.from,
            to = changelog.to,
            startDate = changelog.created.format(dateTimePattern),
            endDate = changelog.endDate.format(dateTimePattern),
            leadTime = changelog.leadTime
        )
    }

    fun changelogToChangelogResponse(changelog: List<Changelog>): List<ChangelogResponse> {
        return changelog.map { changelogToChangelogResponse(it) }
    }

}
