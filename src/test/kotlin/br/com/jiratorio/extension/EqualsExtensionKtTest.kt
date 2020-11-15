package br.com.jiratorio.extension

import br.com.jiratorio.testlibrary.junit.testtype.UnitTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Objects

@UnitTest
class EqualsExtensionKtTest {

    @Test
    fun `test complex equals, results true`() {
        val firstPerson = Person(
            name = "Leonardo",
            age = 23,
            phones = listOf(
                Phone(id = 1L, number = "123"),
                Phone(id = 2L, number = "321"),
                Phone(id = 3L, number = "543")
            )
        )

        val secondPerson = Person(
            name = "Leonardo",
            age = 22,
            phones = listOf(
                Phone(id = 1L, number = "123"),
                Phone(id = 2L, number = "321"),
                Phone(id = 3L, number = "543")
            )
        )

        Assertions.assertThat(firstPerson == secondPerson).isTrue()
        Assertions.assertThat(firstPerson.phones == secondPerson.phones).isTrue()
        Assertions.assertThat(firstPerson === secondPerson).isFalse()
    }

    @Test
    fun `test complex equals, results false`() {
        val firstPerson = Person(
            name = "Leonardo",
            age = 23,
            phones = listOf(
                Phone(id = 1L, number = "123"),
                Phone(id = 2L, number = "321"),
                Phone(id = 3L, number = "345")
            )
        )

        val secondPerson = Person(
            name = "Ferreira",
            age = 23,
            phones = listOf(
                Phone(id = 1L, number = "123"),
                Phone(id = 2L, number = "321"),
                Phone(id = 3L, number = "543")
            )
        )

        Assertions.assertThat(firstPerson == secondPerson).isFalse()
        Assertions.assertThat(firstPerson.phones == secondPerson.phones).isFalse()
        Assertions.assertThat(firstPerson === secondPerson).isFalse()
    }

    inner class Person(
        val name: String,
        val age: Int,
        val phones: List<Phone>
    ) {
        override fun equals(other: Any?) =
            equalsComparing(other, Person::name, Person::phones)

        override fun hashCode() =
            Objects.hash(name, phones)
    }

    inner class Phone(
        val id: Long,
        val number: String
    ) {
        override fun equals(other: Any?) =
            equalsComparing(other, Phone::id, Phone::number)

        override fun hashCode() =
            Objects.hash(id, number)

    }
}
