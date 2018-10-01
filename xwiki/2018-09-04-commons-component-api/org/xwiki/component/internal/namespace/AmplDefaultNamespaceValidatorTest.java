package org.xwiki.component.internal.namespace;


import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.xwiki.component.namespace.NamespaceNotAllowedException;


public class AmplDefaultNamespaceValidatorTest {
    private DefaultNamespaceValidator validator = new DefaultNamespaceValidator();

    @Test
    public void checkAllowed() throws NamespaceNotAllowedException {
        this.validator.checkAllowed(Arrays.asList("namespace"), "namespace");
    }
}

