package com.uniClub.util.pageable;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@UtilityClass
public class PageUtil {

    public boolean isNullOrEmpty(String value){
        return value == null || value.isEmpty();
    }

    public Pageable toPageable(PageableRequest request){
        if (!isNullOrEmpty(request.getColumnName())){
            Sort sort = request.isAsc()
                    ? Sort.by(Sort.Direction.ASC, request.getColumnName())
                    : Sort.by(Sort.Direction.DESC, request.getColumnName());
            return PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
        }

        return PageRequest.of(request.getPageNumber(), request.getPageSize());
    }

    public <T> PageableEntity<T> toPageableResponse(Page<?> page, List<T> content){

        PageableEntity<T> response = new PageableEntity();
        response.setContent(content);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }
}
