package Common;

import weka.core.Instances;

public class DistanceMeasure {
    Instances data;
    int measure;
    public static final int MANHATTAN = 0;
    public static final int EUCLIDEAN = 1;
    public DistanceMeasure(Instances paraData, int paraMeasure) {
        data = paraData;
        measure = paraMeasure;
    }//Of the constructor
    public double distance(double[] paraFirstArray, double[] paraSecondArray) {
        double resultDistance = 0;
        switch (measure) {
            case MANHATTAN:
                resultDistance = manhattan(paraFirstArray, paraSecondArray);
                break;
            case EUCLIDEAN:
                resultDistance = euclidean(paraFirstArray, paraSecondArray);
                break;
            default:
                System.out.println("Internal error! Unsupported distance measure " + measure
                        + " in DistanceMeasure.distance(double[], double[])");
        }//Of switch

        return resultDistance;
    }//Of distance


    public double distance(int paraFirstIndex, int paraSecondIndex) {
        //Step 1. Transform to double arrays.
        double[] tempFirstArray = new double[data.numAttributes() - 1];
        double[] tempSecondArray = new double[data.numAttributes() - 1];
        for (int i = 0; i < tempFirstArray.length; i++) {
            tempFirstArray[i] = data.instance(paraFirstIndex).value(i);
            tempSecondArray[i] = data.instance(paraSecondIndex).value(i);
        }//Of for i

        //Step 2. Compute the distance.
        return distance(tempFirstArray, tempSecondArray);
    }//Of distance


    public double distance(int paraIndex, double[] paraArray) {
        double[] tempFirstArray = new double[data.numAttributes() - 1];
        for (int i = 0; i < tempFirstArray.length; i++) {
            tempFirstArray[i] = data.instance(paraIndex).value(i);
        }//Of for i

        return distance(tempFirstArray, paraArray);
    }//Of distance


    public static double manhattan(double[] paraFirstArray, double[] paraSecondArray) {
        double resultDistance = 0;
        for (int i = 0; i < paraFirstArray.length; i++) {
            resultDistance += Math.abs(paraFirstArray[i] - paraSecondArray[i]);
        }//Of for i
        return resultDistance;
    }//Of manhattan


    public static double euclidean(double[] paraFirstArray, double[] paraSecondArray) {
        double resultDistance = 0;
        for (int i = 0; i < paraFirstArray.length; i++) {
            resultDistance += (paraFirstArray[i] - paraSecondArray[i]) * (paraFirstArray[i] - paraSecondArray[i]);
        }//Of for i

        return Math.sqrt(resultDistance);
    }//Of euclidean
}
