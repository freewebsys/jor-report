package test;

import java.util.ArrayList;
import java.util.List;



/**
 *
 *
 * @author
 * @version
  */
public class Student {
    private String name;
    private int age;
    private String sex;
    private int mark;

    /**
     * Creates a new Student object.
     *
     * @param name
     * @param age
     * @param sex
     * @param mark
     */
    public Student(String name, int age, String sex, int mark) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.mark = mark;
    }

    /**
    * Creates a new Student object.
    *
    * @param name
    * @param age
    * @param mark
    */
    public Student(String name, int age, int mark) {
        this.name = name;
        this.age = age;
        this.mark = mark;
    }

    /**
     *
     *
     * @return
     */
    public List getMembers() {
    	
        List result = new ArrayList();
        result.add(new Person("父亲", "崔爸", 40, "男"));
        result.add(new Person("母亲", "崔妈", 38, "女"));

        return result;
    }

    /**
     *
     *
     * @return
     */
    public int getAge() {
        return age;
    }

    /**
     *
     *
     * @return
     */
    public int getMark() {
        return mark;
    }

    /**
     *
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     *
     * @return
     */
    public String getSex() {
        return sex;
    }

    /**
     *
     *
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }
}
