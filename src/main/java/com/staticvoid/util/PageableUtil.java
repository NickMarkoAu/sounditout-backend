package com.staticvoid.util;

import org.springframework.data.domain.Pageable;

public class PageableUtil {

    public static final Pageable DEFAULT_PAGEABLE = Pageable.ofSize(20);
}
