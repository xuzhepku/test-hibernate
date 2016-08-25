package me.xuzhe.quickstart.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * Created by jason on 16/8/24.
 */
public class HibernateUtil {
    private final static SessionFactory FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Configuration cfg = new Configuration().configure();
        SessionFactory factory = cfg.buildSessionFactory();
        return factory;
    }

    public static SessionFactory getSessionFactory() {
        return FACTORY;
    }

    public static Session openSession() {
        return FACTORY.openSession();
    }

    public static void closeSession(Session session) {
        if (session != null) session.close();
    }
}
