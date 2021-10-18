package org.springframework.core.io.Resource;

import org.springframework.context.annotation.Import;

@Import(value=SqlServiceContext.class)
public @interface EnableSqlService {
}
