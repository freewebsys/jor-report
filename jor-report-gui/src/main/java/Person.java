

/**
 *
 *
 * @author
 * @version server.
  */
public class Person {
	
    private String relations;
    private String name;
    private int age;
    private String sex;

    /**
     * Creates a new Student object.
     *
     * @param name
     * @param age
     * @param sex
     * @param mark
     */
    public Person(String relations, String name, int age, String sex) {
        this.relations = relations;
        this.name = name;
        this.age = age;
        this.sex = sex;
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

    /**
     *
     *
     * @return
     */
    public String getRelations() {
        return relations;
    }

    /**
     *
     *
     * @param relations
     */
    public void setRelations(String relations) {
        this.relations = relations;
    }
}
