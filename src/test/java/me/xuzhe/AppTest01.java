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


            User u = new User();
            u.setBorn(sdf.parse("1982-10-30"));
            u.setNickname("jiji");
            u.setUsername("xuzhe");
            u.setPassword("123");
            session.save(u);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            HibernateUtil.closeSession(session);
        }


    }
}
