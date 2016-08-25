package me.xuzhe;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import me.xuzhe.quickstart.model.User;

import java.util.Date;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        /* hibernate 5中不可用
        Configuration cfg = new Configuration().configure();
        ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
        SessionFactory factory = cfg.buildSessionFactory(sr);
        */
        SessionFactory sf = new Configuration().configure().buildSessionFactory();

        // 创建session
        Session session = null;

        try {
            session = sf.openSession();

            // 开启事务
            session.beginTransaction();

            User user = new User();
            user.setUsername("username3");
            user.setPassword("123");
            user.setNickname("nickname");
            user.setBorn(new Date());
            session.save(user);
            // 提交事务
            session.getTransaction().commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) session.close();
        }

    }
}
