package br.com.leonardoferreira.jirareport.matcher;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IdMatcher extends BaseMatcher<Long> {

    private Long expected;

    public static IdMatcher is(final Long expected) {
        return new IdMatcher(expected);
    }

    @Override
    public boolean matches(final Object item) {
        return item != null && expected.equals(Long.parseLong(String.valueOf(item)));
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("value should is equal to ").appendText(String.valueOf(expected));
    }
}
