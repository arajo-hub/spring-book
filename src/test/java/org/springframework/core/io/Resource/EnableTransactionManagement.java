package org.springframework.core.io.Resource;

import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.TransactionManagementConfigurationSelector;

@Import(TransactionManagementConfigurationSelector.class)
public @interface EnableTransactionManagement {
}
