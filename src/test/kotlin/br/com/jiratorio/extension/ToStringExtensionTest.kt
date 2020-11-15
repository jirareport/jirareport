package br.com.jiratorio.extension

import br.com.jiratorio.testlibrary.junit.testtype.UnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

@UnitTest
class ToStringExtensionTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun `test complex toString`() {
        class Phone(
            private val number: String
        ) {
            override fun toString() =
                toStringBuilder(Phone::number)
        }

        class Person(
            private val name: String,
            private val age: Int,
            private val phones: List<Phone>
        ) {
            override fun toString() =
                toStringBuilder(Person::name, Person::age, Person::phones)
        }

        val phones = listOf(Phone("123"), Phone("321"), Phone("543"))
        val personToString = Person("Leonardo Ferreira", 23, phones).toString()

        log.info("result={}", personToString)

        assertThat(personToString)
            .isEqualTo("Person(name=Leonardo Ferreira, age=23, phones=[Phone(number=123), Phone(number=321), Phone(number=543)])")
    }

}
