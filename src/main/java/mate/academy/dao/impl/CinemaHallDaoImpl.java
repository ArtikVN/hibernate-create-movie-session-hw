package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.CinemaHallDao;
import mate.academy.lib.Dao;
import mate.academy.model.CinemaHall;
import mate.academy.util.HibernateUtil;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class CinemaHallDaoImpl implements CinemaHallDao {
    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(cinemaHall);
            transaction.commit();
        } catch (HibernateError error) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Cant add cinemaHall - " + cinemaHall, error);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return cinemaHall;
    }

    @Override
    public Optional<CinemaHall> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.get(CinemaHall.class, id));
        } catch (HibernateError error) {
            throw new RuntimeException("Can't get cinemaHall by id - " + id, error);
        }
    }

    @Override
    public List<CinemaHall> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
           return session.createQuery("from CinemaHall", CinemaHall.class).getResultList();
       } catch (HibernateError error) {
            throw new RuntimeException("Can't get all cinemaHalls from DB", error);
        }
    }
}