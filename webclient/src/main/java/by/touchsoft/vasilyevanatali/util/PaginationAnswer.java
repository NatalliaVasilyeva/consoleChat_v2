package by.touchsoft.vasilyevanatali.util;

import java.util.List;

/**
 * @author Natali
 *
 * Class contain method what return list of object by page
 */
public enum PaginationAnswer {
    INSTANCE;

    /**
     *
     * @param objects - some list of objects
     * @param pageSize - page size
     * @param pageNumber - page number
     * @return - list of objects
     */
    public List<?> takeObjectList(List<?> objects, int pageSize, int pageNumber) {
        return objects.subList(pageSize * (pageNumber - 1), pageSize * pageNumber > objects.size() ? objects.size() : pageSize * pageNumber);
    }

}
