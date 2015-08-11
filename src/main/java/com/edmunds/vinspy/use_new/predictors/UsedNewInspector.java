package com.edmunds.vinspy.use_new.predictors;

import com.google.common.base.Preconditions;
import com.edmunds.vinspy.use_new.model.ExtractedAttr;
import com.edmunds.vinspy.use_new.model.Inventory;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author alina
 */
public class UsedNewInspector {
    public static final String NEW = "new";
    public static final String USED = "used";
    public static final Integer MILLAGE_BORDER = 5000;

    private final FilteredClassifier classifier;
    private final Instances modelDefinition;


    public UsedNewInspector(FilteredClassifier classifier, Instances modelDefinition) {
        Preconditions.checkNotNull(classifier);
        Preconditions.checkNotNull(modelDefinition);

        this.classifier = classifier;
        this.modelDefinition = modelDefinition;
    }

    protected Instance buildInventoryInstance(Instances sourceDataSet, final Inventory inventory) {
        Instances dataSet = sourceDataSet.stringFreeStructure();

        double[] instanceValues = new double[dataSet.numAttributes()];
        instanceValues[0] = 0;
        instanceValues[1] = isPresent((String)inventory.getAttributes().get(ExtractedAttr.TITLE), NEW);
        instanceValues[2] = isPresent((String) inventory.getAttributes().get(ExtractedAttr.TITLE), USED);
        instanceValues[3] = isPresent(inventory.getLocations().get(0).getUrl(), NEW);
        instanceValues[4] = isPresent(inventory.getLocations().get(0).getUrl(), USED);
        instanceValues[5] = convertYearToNominal((String)inventory.getAttributes().get(ExtractedAttr.YEAR));
        instanceValues[6] = (inventory.getAttributes().get(ExtractedAttr.MSRP) != null) ? 1 : 0;
        instanceValues[7] = convertMillageToNominal((String)inventory.getAttributes().get(ExtractedAttr.MSRP));
        return buildInstance(dataSet, instanceValues);
    }


    private int isPresent(String text, String word) {
        return (StringUtils.isEmpty(text)) ? 0 : (text.toLowerCase().contains(word)) ? 1 : 0;
    }

   private int convertYearToNominal(String year) {
       return (year == null) ? -1 : ((Integer.parseInt(year) > (DateTime.now().getYear() - 4)) ? 1 : 0);
   }

    private int convertMillageToNominal(String millage) {
        return (millage == null) ? -1 : ((Integer.parseInt(millage) < MILLAGE_BORDER) ? 1 : 0);
    }
    public String predictInventoryType(final Inventory inventory) throws Exception {
        Instance reviewInstance = buildInventoryInstance(modelDefinition, inventory);
        double classPrediction = classifier.classifyInstance(reviewInstance);
        reviewInstance.setClassValue(classPrediction);
        System.out.println(reviewInstance);
        return reviewInstance.classAttribute().value((int) classPrediction);

    }
    public static Instance buildInstance(Instances dataSet, double[] instanceValues) {
        dataSet.setRelationName("inventory");

        Instance reviewInstance = new Instance(1.0, instanceValues);
        dataSet.add(reviewInstance);
        dataSet.setClassIndex(0);
        reviewInstance.setDataset(dataSet);

        return reviewInstance;
    }


}
