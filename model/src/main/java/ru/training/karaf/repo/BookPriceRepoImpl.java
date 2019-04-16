package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.BookPrice;
import ru.training.karaf.model.BookPriceDO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BookPriceRepoImpl implements BookPriceRepo {

    private JpaTemplate template;

    public BookPriceRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    @Override
    public List<? extends BookPrice> getAll() {
        return template.txExpr(em -> em.createNamedQuery(BookPriceDO.GET_ALL, BookPriceDO.class).getResultList())
                .stream()
                .map(BookPriceImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public void create(int price, long bookId) {
        BookPriceDO bookPriceToCreate = new BookPriceDO();
        bookPriceToCreate.setPrice(price);

        //Timestamp date = new Timestamp(System.currentTimeMillis());
        LocalDateTime date = LocalDateTime.now();
        bookPriceToCreate.setDate(date);
        //bookPriceToCreate.setDate(0);

        BookDO bookDO = template.txExpr(em -> em.find(BookDO.class, bookId));
        bookDO.getPrice().add(bookPriceToCreate);
        bookPriceToCreate.setBook(bookDO);
        template.tx(em -> em.persist(bookPriceToCreate));
        template.tx(em -> em.merge(bookDO));
        //template.tx(em -> em.merge(bookPriceToCreate));
    }

    @Override
    public List<BookPrice> getByBook(long bookId) {
        return template.txExpr(em -> em.createNamedQuery(BookPriceDO.GET_ALL_BY_BOOK, BookPriceDO.class).setParameter("bookid", bookId)
                .getResultList())
                .stream()
                .map(BookPriceImpl::new)
                .collect(Collectors.toList());
    }

}
class BookPriceImpl implements BookPrice {

    private BookPriceDO bookPriceDO;

    public BookPriceImpl(BookPriceDO bookPriceDO) {
        this.bookPriceDO = bookPriceDO;
    }

    @Override
    public long getId() {
        return bookPriceDO.getId();
    }

    @Override
    public int getPrice() {
        return bookPriceDO.getPrice();
    }

    @Override
    public LocalDateTime getDate() {
        return bookPriceDO.getDate();
    }

    @Override
    public Book getBook() {
        return new BookImpl(bookPriceDO.getBook());
    }
}