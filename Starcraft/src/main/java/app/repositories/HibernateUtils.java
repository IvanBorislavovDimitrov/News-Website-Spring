package app.repositories;

import app.models.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    static {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Article.class);
        configuration.addAnnotatedClass(Comment.class);
        configuration.addAnnotatedClass(Privilege.class);
        configuration.addAnnotatedClass(Video.class);
        configuration.addAnnotatedClass(VideoComment.class);

        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();

        serviceRegistryBuilder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}