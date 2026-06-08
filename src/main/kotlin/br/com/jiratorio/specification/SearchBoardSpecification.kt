package br.com.jiratorio.specification

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.request.SearchBoardRequest
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

class SearchBoardSpecification(
    private val searchBoardRequest: SearchBoardRequest,
    private val currentUser: String
) : Specification<BoardEntity> {

    override fun toPredicate(
        from: Root<BoardEntity>,
        query: CriteriaQuery<*>?,
        builder: CriteriaBuilder
    ): Predicate {
        val predicates = mutableListOf<Predicate>()

        if (searchBoardRequest.name != null) {
            predicates.add(
                builder.like(builder.lower(from.get("name")), "%${searchBoardRequest.name}%".lowercase())
            )
        }

        if (searchBoardRequest.owner.isNullOrBlank()) {
            predicates.add(builder.equal(from.get<String>("owner"), currentUser))
        } else if (searchBoardRequest.owner != "all") {
            predicates.add(builder.equal(from.get<String>("owner"), searchBoardRequest.owner))
        }

        return builder.and(*predicates.toTypedArray())
    }

}
