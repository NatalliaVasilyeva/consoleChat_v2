package by.touchsoft.vasilyevanatali.Util;

import java.util.List;

public enum PaginationAnswer {
    INSTANCE;


    public List<?> takeObjectList(List<?> objects, int pageSize, int pageNumber){
        return objects.subList(pageSize * (pageNumber - 1), pageSize * pageNumber > objects.size() ? objects.size() : pageSize * pageNumber);
    }

}
