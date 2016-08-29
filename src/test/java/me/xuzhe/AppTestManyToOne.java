package me.xuzhe;

import me.xuzhe.quickstart.model.Classroom;
import me.xuzhe.quickstart.model.Student;
import me.xuzhe.quickstart.model.User;
import me.xuzhe.quickstart.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jason on 16/8/25.
 */
public class AppTestManyToOne {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testAdd01() {
        System.out.println("inside AppTest01");
        Session session = null;

        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();

            // 真正业务逻辑
            // 先添加1,还是先添加多? 这里先添加1看看。
            Classroom cr = new Classroom();
            cr.setGrade(2012);
            cr.setName("哲学系教室");
            session.save(cr);

            Student s1 = new Student();
            s1.setName("zhang3");
            s1.setNo(003);
            s1.setClassroom(cr);
            session.save(s1);

            Student s2 = new Student();
            s2.setName("li4");
            s2.setNo(004);
            s2.setClassroom(cr);
            session.save(s2);

            // end of 真正业务逻辑
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    /**
     * 先创建多的对象(student),
     * 再创建1的对象(classroom),
     * 会导致创建5条语句(多两个update)
     * 所以不是最佳实践
     * 参考:
     * Hibernate: insert into t_student (name, no, cid) values (?, ?, ?)
     * Hibernate: insert into t_student (name, no, cid) values (?, ?, ?)
     * Hibernate: insert into t_classroom (name, grade) values (?, ?)
     * Hibernate: update t_student set name=?, no=?, cid=? where id=?
     * Hibernate: update t_student set name=?, no=?, cid=? where id=?
     */
    @Test
    public void testAdd02() {
        System.out.println("inside AppTest01");
        Session session = null;

        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();

            // 真正业务逻辑
            // 先添加1,还是先添加多? 这里先添加1看看。

            Student s1 = new Student();
            s1.setName("zhang3");
            s1.setNo(003);
            session.save(s1);

            Student s2 = new Student();
            s2.setName("li4");
            s2.setNo(004);
            session.save(s2);

            Classroom cr = new Classroom();
            cr.setGrade(2012);
            cr.setName("哲学系教室");
            s1.setClassroom(cr);
            s2.setClassroom(cr);
            session.save(cr);


            // end of 真正业务逻辑
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    /**
     * java.lang.IllegalStateException: org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance beforeQuery flushing: me.xuzhe.quickstart.model.Classroom
     at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:144)
     at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:155)
     at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:162)
     at org.hibernate.internal.SessionImpl.doFlush(SessionImpl.java:1411)
     at org.hibernate.internal.SessionImpl.managedFlush(SessionImpl.java:475)
     at org.hibernate.internal.SessionImpl.flushBeforeTransactionCompletion(SessionImpl.java:3168)
     at org.hibernate.internal.SessionImpl.beforeTransactionCompletion(SessionImpl.java:2382)
     at org.hibernate.engine.jdbc.internal.JdbcCoordinatorImpl.beforeTransactionCompletion(JdbcCoordinatorImpl.java:467)
     at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl.beforeCompletionCallback(JdbcResourceLocalTransactionCoordinatorImpl.java:146)
     at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl.access$100(JdbcResourceLocalTransactionCoordinatorImpl.java:38)
     at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.commit(JdbcResourceLocalTransactionCoordinatorImpl.java:220)
     at org.hibernate.engine.transaction.internal.TransactionImpl.commit(TransactionImpl.java:68)
    两条sql语句,但是由于classroom是transient对象,所以报错。
     */
    @Test
    public void testAdd03() {
        System.out.println("inside AppTest01");
        Session session = null;

        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();

            // 真正业务逻辑
            // 先添加1,还是先添加多? 这里先添加1看看。
            Classroom cr = new Classroom();
            cr.setGrade(2012);
            cr.setName("哲学系教室");
            // 先不save,也就是cr现在是transient对象
            // session.save(cr);

            Student s1 = new Student();
            s1.setName("zhang3");
            s1.setNo(003);
            session.save(s1);

            Student s2 = new Student();
            s2.setName("li4");
            s2.setNo(004);
            session.save(s2);

            s1.setClassroom(cr);
            s2.setClassroom(cr);


            // end of 真正业务逻辑
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    @Test
    public void testLoad() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            // 实际的业务逻辑
            Student s = (Student) session.load(Student.class, 1);
            // 在classroom的tostring()方法实现前,只发一条sql,
            // 实现后,发两条sql(因为tostring需要打印classroom除了id外的更多信息)
            System.out.println(s);
            // end of 实际的业务逻辑

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    @Test
    public void testUpdate() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();

            // 真正业务逻辑
            User u = session.load(User.class, 1);
            u.setNickname("new nick name");
            session.save(u);
            // end of 真正业务逻辑

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    @Test
    public void testDelte() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();

            // 真正业务逻辑
            User u = new User();
            u.setId(1);
            session.delete(u);
            // end of 真正业务逻辑

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    @Test
    public void testList() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            // 实际的业务逻辑
            List<User> users = session.createQuery("from User").list();// 主意括号中的User是对象。这里的 query 是对象查询语句
            for (User u : users) {
                System.out.println(u);
            }

            // end of 实际的业务逻辑

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    /**
     * 分页
     */
    @Test
    public void testPager() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            // 实际的业务逻辑
            List<User> users = session.createQuery("from User").setFirstResult(0).setMaxResults(5).list();
            for (User u : users) {
                System.out.println(u);
            }

            // end of 实际的业务逻辑

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }
}
