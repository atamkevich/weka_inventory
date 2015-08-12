package com.edmunds.vinspy.use_new.predictors;

import static weka.core.converters.ConverterUtils.DataSource;


import com.edmunds.vinspy.use_new.model.Inventory;
import com.google.common.io.Closeables;
import com.edmunds.vinspy.use_new.config.ClassificationConfig;
import org.apache.commons.collections.CollectionUtils;
import org.javatuples.Pair;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.edmunds.vinspy.use_new.repository.InventoryRepository;
/**
 * Classifier manager. Contains logic to store/load/init weka classifier,
 *
 * @author alina
 */
public class ClassificationManager {

    public static final String INVENTORY_TRAIN_DATA = "/train_result.arff";
    public static final String INVENTORY_CLASSIFIER = "src/main/resources/inventory.classifier";
    /**
     * Type of classifiers in Sentinel.
     */
    public enum ClassifierType {

        RANDOM_FOREST() {
            @Override
            public Classifier init() {
                RandomForest originClassifier = new RandomForest();
                originClassifier.setSeed(5);
                return originClassifier;
            }
        };

        public abstract Classifier init();

    }

    public void buildClassifier(ClassifierType classifierType, File storageFile) throws Exception {
        FilteredClassifier classifier = new FilteredClassifier();
        Classifier originClassifier = classifierType.init();
        classifier.setClassifier(originClassifier);

        Instances trainDataSet = ClassificationManager.loadInstances(locateTrainDataFile(), 0);
        classifier.buildClassifier(trainDataSet);

        Instances modelDefinition = trainDataSet.stringFreeStructure();
        storeClassifier(storageFile, classifier, modelDefinition);
    }


    public static Instances loadInstances(File arffStorage, int classIndex) throws Exception {
        DataSource dataSource = new DataSource(arffStorage.getPath());
        return dataSource.getDataSet(classIndex);
    }

    public Pair<FilteredClassifier, Instances> loadClassifier() {
        Pair<FilteredClassifier, Instances> result = this.load(locateStorageFile());
        return result;
    }

    public void storeClassifier(File classifierStorage, Classifier classifier, Instances instances) throws Exception {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(classifierStorage));
            objectOutputStream.writeObject(classifier);
            if (instances != null) {
                objectOutputStream.writeObject(instances);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            objectOutputStream.close();
        }
    }

    protected Pair<FilteredClassifier, Instances> load(File storage) {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(storage));
            FilteredClassifier classifier = (FilteredClassifier) objectInputStream.readObject();
            Instances instances = tryLoadInstances(objectInputStream, storage);

            return new Pair<FilteredClassifier, Instances>(classifier, instances);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Closeables.closeQuietly(objectInputStream);
        }
    }


    protected File locateTrainDataFile() {
        return new File(ClassificationManager.class.getResource(INVENTORY_TRAIN_DATA).getFile());
    }

    protected File locateStorageFile() {
        return new File(ClassificationManager.class.getResource("/inventory.classifier").getFile());
    }


    private Instances tryLoadInstances(ObjectInputStream inputStream, File classifierStorage) {
        try {
            return (Instances) inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            GenericApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClassificationConfig.class);

            File inventoryClassifier = new File(INVENTORY_CLASSIFIER);

            ClassificationManager classifierManager = applicationContext.getBean(ClassificationManager.class);
            classifierManager.buildClassifier(ClassifierType.RANDOM_FOREST, inventoryClassifier);


            InventoryRepository inventoryRepository = applicationContext.getBean(InventoryRepository.class);
            UsedNewInspector usedNewInspector = applicationContext.getBean(UsedNewInspector.class);
            List<Inventory> all = null;
            boolean processingFlag = true;
            int page = 0;
            int size = 50;
            while (processingFlag) {
                all = inventoryRepository.findAll(new Query().limit(size).skip(page * size));
                for (Inventory inventory: all) {
                    inventory.setInventoryTypeWeka(usedNewInspector.predictInventoryType(inventory));
                    inventoryRepository.addOrUpdate(inventory, "inventory3");
                }
                page ++;
                if ((all == null) || all.isEmpty()) {
                    processingFlag = false;
                }
            }
            System.out.println("end");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
