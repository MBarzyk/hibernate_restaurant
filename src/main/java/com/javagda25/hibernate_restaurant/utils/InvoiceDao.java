package com.javagda25.hibernate_restaurant.utils;

import com.javagda25.hibernate_restaurant.Invoice;
import com.javagda25.hibernate_restaurant.Product;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceDao {
    EntityDao entityDao = new EntityDao();

    public List<Invoice> getAllUnpaid() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoice> criteriaQuery = cb.createQuery(Invoice.class);
            Root<Invoice> root = criteriaQuery.from(Invoice.class);

            criteriaQuery.select(root).where(cb.equal(root.get("ifPaid"), 0));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public List<Invoice> getAllFromLastWeek() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoice> criteriaQuery = cb.createQuery(Invoice.class);
            Root<Invoice> root = criteriaQuery.from(Invoice.class);

            LocalDateTime now = LocalDateTime.now();
            criteriaQuery.select(root).where(cb.between(root.get("dateOfCreation"), now.minusDays(7), now));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public List<Product> getProductsOfInvoice(Long id) {
        List<Product> products = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();

        try (Session session = factory.openSession()) {
            getInvoiceById(id, session).ifPresent(invoice -> products.addAll(invoice.getProductList()));
        }
        return products;
    }

    public void setInvoicePaid(Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        Invoice invoice = null;

        try (Session session = factory.openSession()) {
            Optional<Invoice> optionalInvoice = getInvoiceById(id, session);
            transaction = session.beginTransaction();
            if (optionalInvoice.isPresent()) {
                invoice = optionalInvoice.get();
                if (!invoice.isIfPaid()) {
                    invoice.setIfPaid(true);
                    invoice.setDateOfPayment(LocalDateTime.now());
                    session.saveOrUpdate(invoice);
                    transaction.commit();
                } else {
                    System.err.println("This invoice was already paid for!");
                }
            }

        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
    public void handInvoice (Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        Invoice invoice = null;

        try (Session session = factory.openSession()) {
            Optional<Invoice> optionalInvoice = getInvoiceById(id, session);
            transaction = session.beginTransaction();
            if (optionalInvoice.isPresent()) {
                invoice = optionalInvoice.get();
                if (invoice.getDateOfRelease() == null) {
                    invoice.setDateOfRelease(LocalDateTime.now());
                    session.saveOrUpdate(invoice);
                    transaction.commit();
                } else {
                    System.err.println("This invoice was already handed out!");
                }
            }

        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }


    public Double getBillById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return getInvoiceById(id, session).map(Invoice::getBillValue).orElse(null);
        }
    }

    public Double getTotalPaymentsToday () {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = cb.createQuery(Double.class);
            Root<Double> root = criteriaQuery.from(Double.class);


            LocalDateTime now = LocalDateTime.now();
            criteriaQuery.select(cb.sum(root.get("billValue"))).where(cb.between(root.get("dateOfCreation"), now.toLocalDate().atStartOfDay(), now));

            return session.createQuery(criteriaQuery).getSingleResult();
        }
    }

    private Optional<Invoice> getInvoiceById(Long id, Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Invoice> criteriaQuery = cb.createQuery(Invoice.class);
        Root<Invoice> root = criteriaQuery.from(Invoice.class);

        return entityDao.getById(Invoice.class, id);
    }
}
