package org.connector.dao;

import org.connector.dao.query.entities.InnerClass;
import org.connector.dao.query.entities.SomeClass;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CouchDbEntitiesFactory {

    private static final SomeClass fooClass = new SomeClass("test field", "extra", "1:1");
    private static final SomeClass fooClass2 = new SomeClass("test field2", "Extra", "1:2", Arrays.asList(new InnerClass("1", "1"), new InnerClass("prop 2", "2")));
    private static final SomeClass fooClass3 = new SomeClass("test field3", "exxxxtra", "1:3");
    private static final SomeClass fooClass4 = new SomeClass("test field4", "extra1", "1:4", Arrays.asList(new InnerClass("prop 1", "1"),
                new InnerClass("prop 2", "2"), new InnerClass("prop 3", "3") , new InnerClass("prop 5", "5")));
    private static final SomeClass fooClass5 = new SomeClass("test field5", "extra2", "1:5", Arrays.asList(new InnerClass("3", "3"), new InnerClass("4", "4")));
    private static final SomeClass fooClass6 = new SomeClass("test field6", "extra3", "1:6");


    public static AbstractCouchDbIntegrationTest.ChildCouchEntity createChild(String name) {
        return AbstractCouchDbIntegrationTest.ChildCouchEntity.builder()
                .names(name)
                .version(new Random().nextInt())
                .weight(new Random().nextDouble())
                .build();
    }

    public static AbstractCouchDbIntegrationTest.ParentCouchEntity createParent(String name, List<String> childIds) {
        return AbstractCouchDbIntegrationTest.ParentCouchEntity.builder()
                .names(name)
                .price(new Random().nextDouble())
                .amount(new Random().nextInt())
                .description("bug test menu")
                .childrenIds(childIds)
                .build();
    }

    public static List<SomeClass> createFooClass(){
        return Arrays.asList(fooClass, fooClass2, fooClass3, fooClass4, fooClass5, fooClass6);
    }


}
