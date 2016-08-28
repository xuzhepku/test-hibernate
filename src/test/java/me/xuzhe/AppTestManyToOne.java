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
     Hibernate: insert into t_student (name, no, cid) values (?, ?, ?)
     Hibernate: insert into t_classroom (name, grade) values (?, ?)
     Hibernate: update t_student set name=?, no=?, cid=? where id=?
     Hibernate: update t_student set name=?, no=?, cid=? where id=?
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

    @Test
    public void testLoad() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            // 实际的业务逻辑
            User u = (User) session.load(User.class, 1);
            System.out.println(u);
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
