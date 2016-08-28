package me.xuzhe;

import me.xuzhe.quickstart.model.User;
import me.xuzhe.quickstart.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jason on 16/8/25.
 */
public class AppTest01 {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testAdd() {
        System.out.println("inside AppTest01");
        Session session = null;
        for (int i = 0; i < 10; i++) {
            try {
                session = HibernateUtil.openSession();
                session.beginTransaction();

                // 真正业务逻辑
                User u = new User();
                u.setBorn(sdf.parse("1982-10-30"));
                u.setNickname("jiji");
                u.setUsername("xuzhe");
                u.setPassword("123");
                //以上状态是 transient 状态(仅在内存中有数据)
                session.save(u);
                //以上状态是 persistent 状态(在数据表和内存中均有数据),此时被 session 所管理,当commit时,会把 session中的对象和目前对象进行比较,如果不一致,会继续发出 sql(update) 语句
                // end of 真正业务逻辑
                session.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                if (session != null) session.getTransaction().rollback();
            } finally {
                HibernateUtil.closeSession(session);
            }
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
