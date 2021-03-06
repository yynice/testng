package org.testng.junit;

import org.junit.runner.Description;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.Utils;

/**
 *
 * @author lukas
 */
public class JUnit4TestMethod extends JUnitTestMethod {

    public JUnit4TestMethod(JUnitTestClass owner, Description desc) {
        super(owner, desc.getMethodName(), getMethod(desc), desc);
    }

    private static ConstructorOrMethod getMethod(Description desc) {
        Class<?> c = desc.getTestClass();
        String method = desc.getMethodName();
        if (JUnit4SpockMethod.isSpockClass(c)) {
            return new JUnit4SpockMethod(desc);
        }
        if (method == null) {
            return new JUnit4ConfigurationMethod(c);
        }
        // remove [index] from method name in case of parameterized test
        int idx = method.indexOf('[');
        if (idx != -1) {
            method = method.substring(0, idx);
        }
        try {
            return new ConstructorOrMethod(c.getMethod(method));
        } catch (Throwable t) {
            Utils.log("JUnit4TestMethod", 2,
                    "Method '" + method + "' not found in class '" + c.getName() + "': " + t.getMessage());
            return null;
        }
    }

    @Override
    public boolean isTest() {
        return !(m_method instanceof JUnit4ConfigurationMethod);
    }

    @Override
    public String toString() {
        return m_method.toString();
    }
}
