package com.trivadis.cdi.eager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Singleton;

public class EagerExtension implements Extension {
	private List<Bean<?>> eagerBeansList = new ArrayList<Bean<?>>();

	public EagerExtension() {
	}

	public <T> void collect(@Observes ProcessBean<T> event) {
		if (event.getAnnotated().isAnnotationPresent(Eager.class)
				&& (event.getAnnotated().isAnnotationPresent(
						ApplicationScoped.class) //
				|| event.getAnnotated().isAnnotationPresent(Singleton.class))) {
			eagerBeansList.add(event.getBean());
		}
	}

	public void load(@Observes AfterDeploymentValidation event,
			BeanManager beanManager) {
		for (Bean<?> bean : eagerBeansList) {
			Object reference = beanManager.getReference(bean,
					bean.getBeanClass(),
					beanManager.createCreationalContext(bean));// .toString();

			// note: toString() is important to instantiate the bean
			reference.toString();

			LOG.info((new StringBuilder()).append("eagerly initialized bean: ")
					.append(bean.getBeanClass()).toString());
		}
	}

	private static Logger LOG = Logger
			.getLogger(EagerExtension.class.getName());

}
