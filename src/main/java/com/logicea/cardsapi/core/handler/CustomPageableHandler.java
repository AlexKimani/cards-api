package com.logicea.cardsapi.core.handler;

import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.exception.ServiceException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ToString
@EqualsAndHashCode
public class CustomPageableHandler implements Pageable {
    private final int limit;
    private final int offset;
    private final Sort sort;

    /**
     * Creates a new {@link CustomPageableHandler} with sort parameters applied.
     *
     * @param offset zero-based offset.
     * @param limit  the size of the elements to be returned.
     * @param sort   can be {@literal null}.
     */
    public CustomPageableHandler(int offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new ServiceException(ErrorCode.ERROR_1501.getMessage());
        }

        if (limit < 1) {
            throw new ServiceException(ErrorCode.ERROR_1502.getMessage());
        }
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    /**
     * Creates a new {@link CustomPageableHandler} with sort parameters applied.
     *
     * @param offset     zero-based offset.
     * @param limit      the size of the elements to be returned.
     * @param direction  the direction of the {@link Sort} to be specified, can be {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or empty.
     */
    public CustomPageableHandler(int offset, int limit, Sort.Direction direction, String... properties) {
        this(offset, limit, Sort.by(direction, properties));
    }

    /**
     * Creates a new {@link CustomPageableHandler} with sort parameters applied.
     *
     * @param offset zero-based offset.
     * @param limit  the size of the elements to be returned.
     */
    public CustomPageableHandler(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    /**
     * Returns the page to be returned.
     *
     * @return the page to be returned or throws {@link UnsupportedOperationException} if the object is
     * {@link #isUnpaged()}.
     * @throws UnsupportedOperationException if the object is {@link #isUnpaged()}.
     */
    @Override
    public int getPageNumber() {
        return Math.toIntExact(this.offset / this.limit);
    }

    /**
     * Returns the number of items to be returned.
     *
     * @return the number of items of that page or throws {@link UnsupportedOperationException} if the object is
     * {@link #isUnpaged()}.
     * @throws UnsupportedOperationException if the object is {@link #isUnpaged()}.
     */
    @Override
    public int getPageSize() {
        return this.limit;
    }

    /**
     * Returns the offset to be taken according to the underlying page and page size.
     *
     * @return the offset to be taken or throws {@link UnsupportedOperationException} if the object is
     * {@link #isUnpaged()}.
     * @throws UnsupportedOperationException if the object is {@link #isUnpaged()}.
     */
    @Override
    public long getOffset() {
        return this.offset;
    }

    /**
     * Returns the sorting parameters.
     *
     * @return
     */
    @Override
    public Sort getSort() {
        return this.sort;
    }

    /**
     * Returns the {@link Pageable} requesting the next {@link Page}.
     *
     * @return
     */
    @Override
    public Pageable next() {
        return new CustomPageableHandler((int) (getOffset() + getPageSize()), getPageSize(), getSort());
    }

    public CustomPageableHandler previous() {
        return hasPrevious() ? new CustomPageableHandler((int) (getOffset() - getPageSize()), getPageSize(), getSort()) : this;
    }

    /**
     * Returns the previous {@link Pageable} or the first {@link Pageable} if the current one already is the first one.
     *
     * @return
     */
    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    /**
     * Returns the {@link Pageable} requesting the first page.
     *
     * @return
     */
    @Override
    public Pageable first() {
        return new CustomPageableHandler(0, getPageSize(), getSort());
    }

    /**
     * Creates a new {@link Pageable} with {@code pageNumber} applied.
     *
     * @param pageNumber
     * @return a new {@link PageRequest} or throws {@link UnsupportedOperationException} if the object is
     * {@link #isUnpaged()} and the {@code pageNumber} is not zero.
     * @throws UnsupportedOperationException if the object is {@link #isUnpaged()}.
     * @since 2.5
     */
    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    /**
     * Returns whether there's a previous {@link Pageable} we can access from the current one. Will return
     * {@literal false} in case the current {@link Pageable} already refers to the first page.
     *
     * @return
     */
    @Override
    public boolean hasPrevious() {
        return this.offset > this.limit;
    }
}
