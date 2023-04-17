package com.example.thejavatest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;


@ExtendWith(FindSlowTestExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @Order(1)
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

    @Order(2)
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

    @Order(3)
    @Test
    @DisplayName("tagging1 - fast")
//    @Tag("fast")
    @FastTest
    void tagging_1(){
        try {
            Thread.sleep(1005l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("tagging2 -slow")
//    @Tag("slow")
    @SlowTest
    void tagging_2(){


    }

    @DisplayName("스터디 만들기")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetition}")
    void repeatTest(RepetitionInfo repetitionInfo){
        System.out.println("test " + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }


    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName}={0}")
    @ValueSource(strings = {"날씨가", "많이", "춰워지고", "있네요"})
    @EmptySource
    @NullSource
    @NullAndEmptySource
     void parameterizedTest(String message){
        System.out.println(message);
    }

    @DisplayName("스터디 만들기 - integer")
    @ParameterizedTest(name = "{index} {displayName}={0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTest(@ConvertWith(StudyConverter.class) Study study){
        System.out.println(study.getLimit());
    }


    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName}={0}")
    @CsvSource({"10, '자바스터디'", "20, '스프링'"})
    void parameterizedTest_CsvSource(Integer limit, String name){
        System.out.println(new Study(limit, name));
    }


    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName}={0}")
    @CsvSource({"10, '자바스터디'", "20, '스프링'"})
    void parameterizedTest_ArgumentAccessor(ArgumentsAccessor argumentsAccessor){
        System.out.println(new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1)));
    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName}={0}")
    @CsvSource({"10, '자바스터디'", "20, '스프링'"})
    void parameterizedTest_Aggregator(@AggregateWith(StudyAggregator.class) Study study){
        System.out.println(study);
    }


    static class StudyAggregator implements ArgumentsAggregator{

        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        }

    }


    static class StudyConverter extends SimpleArgumentConverter{

        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            assertEquals(Study.class, aClass, "Can only convert to Study");
            return new Study(Integer.parseInt(o.toString()));
        }

    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName}={0}")
    @CsvSource({"java,10"})
    @EmptySource
    @NullSource
    @NullAndEmptySource
    void parameterizedTestCsvSource(String message){
        System.out.println(message);
    }




//    @Disabled
    @Test
    @DisplayName("스터디 만들기 어게인")
    void create_new_study_again(){
//        System.out.println(2);
    }

}
