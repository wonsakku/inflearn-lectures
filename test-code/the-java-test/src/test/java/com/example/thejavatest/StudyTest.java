package com.example.thejavatest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @BeforeAll
    static void beforeAll(){
        System.out.println("before all");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("after all");
    }

    @BeforeEach
    void beforeEach(){
        System.out.println("before each");
    }

    @AfterEach
    void afterEach(){
        System.out.println("after each");
    }

    @Test
    @DisplayName("스터디 만들기")
    void create_new_study(){

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        assertEquals("limit은 0보다 커야 한다.", exception.getMessage());


//        assertTimeout(Duration.ofMillis(100), () -> {
        assertTimeoutPreemptively(Duration.ofMillis(1000), () -> {
            new Study(10);
            Thread.sleep(300);
        });
//        assertAll(
//                () -> assertNotNull(study),
//                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
//                        () -> "스터디를 처음 만들면 " + StudyStatus.DRAFT + " 상태다"),
//                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
//        );
    }

    @Test
    @DisplayName("JUnit5 조건에 따라 테스트 실행하기")
    @EnabledOnOs({OS.WINDOWS, OS.MAC})
//    @DisabledOnOs({OS.WINDOWS, OS.MAC})
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11})
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    void create_new_study_1(){
        final String systemEnv = System.getenv("TEST_ENV");
        System.out.println(systemEnv);
//        assumeTrue("LOCAL".equalsIgnoreCase(systemEnv));
        assumingThat("LOCAL".equalsIgnoreCase(systemEnv), () -> {
            System.out.println("local");
            final Study actual = new Study(100);
            org.assertj.core.api.Assertions.assertThat(actual.getLimit()).isGreaterThanOrEqualTo(100);
        });
    }

    @Test
    @DisplayName("tagging1 - fast")
//    @Tag("fast")
    @FastTest
    void tagging_1(){

    }

    @Test
    @DisplayName("tagging2 -slow")
//    @Tag("slow")
    @SlowTest
    void tagging_2(){

    }




//    @Disabled
    @Test
    @DisplayName("스터디 만들기 어게인")
    void create_new_study_again(){
//        System.out.println(2);
    }

}
