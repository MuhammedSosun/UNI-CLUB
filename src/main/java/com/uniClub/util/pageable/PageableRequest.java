package com.uniClub.util.pageable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableRequest {

    private int pageNumber = 0;

    private int pageSize = 10;

    private String columnName;

    private boolean asc = true;
}
