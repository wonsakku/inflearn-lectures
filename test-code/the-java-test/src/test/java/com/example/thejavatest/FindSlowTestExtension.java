package com.example.thejavatest;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.event.annotation.AfterTestExecution;

import java.lang.reflect.Method;

public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static long THRESHOLD = 1000L;

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        final ExtensionContext.Store store = getStore(extensionContext);
        store.put("START_TIME", System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {

        final Method requiredTestMethod = extensionContext.getRequiredTestMethod();
        final SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);

        final String testMethodName = extensionContext.getRequiredTestMethod().getName();
        final ExtensionContext.Store store = getStore(extensionContext);
        long startTime = store.remove("START_TIME", long.class);
        long duration = System.currentTimeMillis() - startTime;

        if(duration > THRESHOLD && annotation == null){
            System.out.printf("Please consider mark method [%s] with @SlowTest.\n", testMethodName);
        }
    }

    private static ExtensionContext.Store getStore(ExtensionContext extensionContext) {
        final String testClassName = extensionContext.getRequiredTestClass().getName();
        final String testMethodName = extensionContext.getRequiredTestMethod().getName();
        return extensionContext.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
    }


}
