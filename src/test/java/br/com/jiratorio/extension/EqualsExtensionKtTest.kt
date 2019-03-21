package br.com.jiratorio.extension

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Objects

class EqualsExtensionKtTest {

    @Test
    fun `test complex equals, results true`() {
        val firstPerson = Person(
            "Leonardo", 23,
            listOf(Phone(1L, "123"), Phone(2L, "321"), Phone(3L, "543"))
        )

        val secondPerson = Person(
            "Leonardo", 22,
            listOf(Phone(1L, "123"), Phone(2L, "321"), Phone(3L, "543"))
        )

        Assertions.assertThat(firstPerson === secondPerson).isFalse()
        Assertions.assertThat(firstPerson.phones === secondPerson.phones).isFalse()
        Assertions.assertThat(firstPerson == secondPerson).isTrue()
    }

    @Test
    fun `test complex equals, results false`() {
        val firstPerson = Person(
            "Leonardo", 23,
            listOf(Phone(1L, "123"), Phone(2L, "321"), Phone(3L, "543"))
        )

        val secondPerson = Person(
            "Ferreira", 23,
            listOf(Phone(1L, "123"), Phone(2L, "321"), Phone(3L, "543"))
        )

        Assertions.assertThat(firstPerson === secondPerson).isFalse()
        Assertions.assertThat(firstPerson.phones === secondPerson.phones).isFalse()
        Assertions.assertThat(firstPerson == secondPerson).isFalse()
    }

    internal inner class Person(
        val name: String,
        val age: Int,
        var phones: List<Phone>
    ) {
        override fun equals(other: Any?) =
            equalsBuilder(other, Person::name, Person::phones)

        override fun hashCode() =
            Objects.hash(name, phones)
    }

    internal inner class Phone(
        val id: Long,
        val number: String
    ) {
        override fun equals(other: Any?) =
            equalsBuilder(other, Phone::id, Phone::number)

        override fun hashCode() =
            Objects.hash(id, number)

    }
}
