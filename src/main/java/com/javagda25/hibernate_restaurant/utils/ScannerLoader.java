package com.javagda25.hibernate_restaurant.utils;

import com.javagda25.hibernate_restaurant.Invoice;
import com.javagda25.hibernate_restaurant.Product;

import java.util.Optional;
import java.util.Scanner;

public class ScannerLoader {
    private final static Scanner scanner = new Scanner(System.in);

    public void initiate() {
        EntityDao entityDao = new EntityDao();
        InvoiceDao invoiceDao = new InvoiceDao();
        String line;
        do {
            System.out.println("Give command: \n[addInvoice][addProduct][listInvoice][listInvoiceProducts][listProducts]\n" +
                    "[getBill][getUnpaid][getLastWeek][setAsPaid][handInvoice][getTodaySum][exit]");
            line = scanner.nextLine();
            if (line.equalsIgnoreCase("addInvoice")) {
                addInvoice(entityDao);
            } else if (line.equalsIgnoreCase("addProduct")) {
                addProduct(entityDao);
            } else if (line.equalsIgnoreCase("listInvoice")) {
                entityDao.getall(Invoice.class).forEach(System.out::println);
            } else if (line.equalsIgnoreCase("listProducts")) {
                entityDao.getall(Product.class).forEach(System.out::println);
            } else if (line.equalsIgnoreCase("listInvoiceProducts")) {
                System.out.println("List products of which invoice?: ");
                Long invoiceId = Long.parseLong(scanner.nextLine());
                invoiceDao.getProductsOfInvoice(invoiceId).forEach(System.out::println);
            } else if (line.equalsIgnoreCase("getBill")) {
                System.out.println("Get bill of which invoice?");
                Long invoiceId = Long.parseLong(scanner.nextLine());
                System.out.println("Money to pay: " + invoiceDao.getBillById(invoiceId));
            } else if (line.equalsIgnoreCase("getUnpaid")) {
                invoiceDao.getAllUnpaid().forEach(System.out::println);
            } else if (line.equalsIgnoreCase("getLastWeek")) {
                invoiceDao.getAllFromLastWeek().forEach(System.out::println);
            } else if (line.equalsIgnoreCase("setAsPaid")) {
                System.out.println("Give ID of invoice to set as paid: ");
                Long id = Long.parseLong(scanner.nextLine());
                invoiceDao.setInvoicePaid(id);
            } else if (line.equalsIgnoreCase("handInvoice")) {
                System.out.println("Give ID of invoice to hand out: ");
                Long id = Long.parseLong(scanner.nextLine());
                invoiceDao.handInvoice(id);
            } else if (line.equalsIgnoreCase("getTodaySum")) {
                System.out.println("Today's spendings reached: " + invoiceDao.getTotalPaymentsToday());
            }

        } while (!line.equalsIgnoreCase("exit"));

    }


    private static void addInvoice(EntityDao dao) {
        Invoice invoice = new Invoice();
        System.out.println("Give client's name: ");
        invoice.setClientName(scanner.nextLine());

        dao.saveOrUpdate(invoice);
    }

    private void addProduct(EntityDao dao) {
        Product product = new Product();
        System.out.println("Add to which invoice?");
        Long invoiceId = Long.parseLong(scanner.nextLine());


        Optional<Invoice> invoice = dao.getById(Invoice.class, invoiceId);
        if (invoice.isPresent()) {
            if (invoice.get().isIfPaid()) {
                System.err.println("This invoice was already paid for. You can't add products anymore!");
                return;
            } else {
                invoice.ifPresent(product::setInvoice);

                System.out.println("Add product's name: ");
                product.setName(scanner.nextLine());
                System.out.println("Add product's price: ");
                product.setPrice(Double.parseDouble(scanner.nextLine()));
                System.out.println("What's the product's tax?");
                product.setTax(Double.parseDouble(scanner.nextLine()));
                System.out.println("Add product's quantity: ");
                product.setStock(Integer.parseInt(scanner.nextLine()));

                dao.saveOrUpdate(product);
            }
        }
    }
}
