package springbook.learningtest.junit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//public class JUnitTest {
//
//    static JUnitTest testObject;
//
//    @Test
//    public void test1() {
//        assertThat(this).isNotEqualTo(testObject);
//        testObject = this;
//    }
//
//    @Test
//    public void test2() {
//        assertThat(this).isNotEqualTo(testObject);
//        testObject = this;
//    }
//
//    @Test
//    public void test3() {
//        assertThat(this).isNotEqualTo(testObject);
//        testObject = this;
//    }
//
//}

// 개선한 JUnit 테스트 오브젝트 생성에 대한 학습 테스트
//public class JUnitTest {
//
//    static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
//
//    @Test
//    public void test1() {
//        assertFalse(testObjects.contains(this));
//        testObjects.add(this);
//    }
//
//    @Test
//    public void test2() {
//        assertFalse(testObjects.contains(this));
//        testObjects.add(this);
//    }
//
//    @Test
//    public void test3() {
//        assertFalse(testObjects.contains(this));
//        testObjects.add(this);
//    }
//
//}

// 스프링 테스트 컨텍스트에 대한 학습 테스트
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class JUnitTest {

    @Autowired
    ApplicationContext context;

    static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        assertFalse(testObjects.contains(this));
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test
    public void test2() {
        assertFalse(testObjects.contains(this));
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test
    public void test3() {
        assertFalse(testObjects.contains(this));
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

}