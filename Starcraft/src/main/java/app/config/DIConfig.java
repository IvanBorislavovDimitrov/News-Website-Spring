package app.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import app.repositories.*;
import app.models.*;

@Configuration
public class DIConfig {

    @Bean
    public SessionFactory provideSessionFactory() {
        return HibernateUtils.getSessionFactory();
    }

    @Bean(name = "User")
    @Autowired
    public GenericRepository<User> provideUserGenericRepository(SessionFactory sessionFactory) {
        GenericRepository<User> repo = new HibernateRepository<>(sessionFactory);
        repo.setEntityClass(User.class);

        return repo;
    }

    @Bean(name = "Article")
    @Autowired
    public GenericRepository<Article> provideNewGenericRepository(SessionFactory sessionFactory) {
        GenericRepository<Article> repo = new HibernateRepository<>(sessionFactory);
        repo.setEntityClass(Article.class);

        return repo;
    }

    @Bean(name = "Comment")
    @Autowired
    public GenericRepository<Comment> provideCommentGenericRepository(SessionFactory sessionFactory) {
        GenericRepository<Comment> repo = new HibernateRepository<>(sessionFactory);
        repo.setEntityClass(Comment.class);

        return repo;
    }

    @Bean(name = "Privilege")
    @Autowired
    public GenericRepository<Privilege> providePrivilegeGenericRepository(SessionFactory sessionFactory) {
        GenericRepository<Privilege> repo = new HibernateRepository<>(sessionFactory);
        repo.setEntityClass(Privilege.class);

        return repo;
    }
}
