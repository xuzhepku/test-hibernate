package me.xuzhe;

import me.xuzhe.quickstart.model.User;
import me.xuzhe.quickstart.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.Test;

import java.text.SimpleDateFormat;

/**
 * Created by jason on 16/8/25.
 */
public class AppTest01 {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testAdd() {
        System.out.println("inside AppTest01");
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();

            // 真正业务逻辑
            User u = new User();
            u.setBorn(sdf.parse("1982-10-30"));
            u.setNickname("jiji");
            u.setUsername("xuzhe");
            u.setPassword("123");
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
    public void testLoad() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            User u = (User) session.load(User.class, 1);
            System.out.println(u);
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

}
