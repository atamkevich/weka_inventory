package com.edmunds.vinspy.use_new.config;

import com.edmunds.vinspy.use_new.predictors.ClassificationManager;
import com.edmunds.vinspy.use_new.predictors.UsedNewInspector;
import org.javatuples.Pair;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;

/**
 * @author alina
 */
@Configuration
@Import(MongoConfig.class)
@ComponentScan(basePackages = {"com.edmunds.vinspy.use_new"})
public class ClassificationConfig {
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    ClassificationManager classifierManager() {
        return new ClassificationManager();
    }

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    UsedNewInspector usedNewInspector(ClassificationManager classifierManager) {
        Pair<FilteredClassifier, Instances> container = classifierManager.loadClassifier();
        return new UsedNewInspector(container.getValue0(), container.getValue1());
    }
}
