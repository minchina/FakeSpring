package org.ncmao.myspring;


import org.ncmao.myspring.entity.ClassPathXmlApplicationContext;
import org.ncmao.myspring.entity.Student;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParseTest {

    @org.junit.Test
    public void  shoudLoadApplicationContext() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("xml/applicationContext.xml");
        Student student = (Student)classPathXmlApplicationContext.getBean("student");
        assertThat(student.getName(), is("123"));
    }
}
