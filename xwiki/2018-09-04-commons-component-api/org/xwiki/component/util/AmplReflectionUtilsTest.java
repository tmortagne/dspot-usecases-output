package org.xwiki.component.util;


import java.util.List;


public class AmplReflectionUtilsTest {
    private static interface TestInterfaceSimple {}

    private static interface TestInterface<A, B> {}

    private static interface TestInterface2<A, B> extends AmplReflectionUtilsTest.TestInterface<A, B> {}

    private static class TestClass<A, B> implements AmplReflectionUtilsTest.TestInterface2<A, B> {}

    private static class TestClass2<A> extends AmplReflectionUtilsTest.TestClass<A, Integer> {}

    private static class TestClass3 extends AmplReflectionUtilsTest.TestClass2<List<String>> {}

    private static class TestClass4<T> extends AmplReflectionUtilsTest.TestClass2<T> implements AmplReflectionUtilsTest.TestInterfaceSimple {}

    private class AbstractTestFieldClass {
        @SuppressWarnings("unused")
        private Object superField;
    }

    private class TestFieldClass extends AmplReflectionUtilsTest.AbstractTestFieldClass {
        @SuppressWarnings("unused")
        private Object field;
    }
}

