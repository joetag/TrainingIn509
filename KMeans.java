import weka.core.*;

import java.io.FileReader;
import java.util.Arrays;
import Common.*;
public class KMeans {
    Instances data;
    int measure;
    int numInstances;
    int numConditions;
    DistanceMeasure distanceMeasure;
    public KMeans(String fileName){
        data = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            data = new Instances(fileReader);
            fileReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        numInstances = data.numInstances();
        numConditions = data.numAttributes() - 1;
        distanceMeasure = new DistanceMeasure(data, DistanceMeasure.MANHATTAN);
    }

    public double[][] getInitialCentersRandom(int[] paraBlock, int paraK){
        double[][] resultCenters = new double[paraK][numConditions];
        int[] tempBlocks = new int[paraBlock.length];
        int tempIndex;
        int tempInt;
        for (int i = 0; i < paraK; i++) {
            tempIndex = common.random.nextInt(paraBlock.length);
            tempInt = tempBlocks[i];
            tempBlocks[i] = tempBlocks[tempIndex];
            tempBlocks[tempIndex] = tempInt;
        }

        for (int i = 0; i < paraK; i++) {
            for (int j = 0; j < numConditions; j++) {
                resultCenters[i][j] = data.instance(i).value(j);
            }
        }
        return resultCenters;
    }

    public int[][] clusterInK(int[] paraBlocks, int paraK, double[][] paraCenters){
        double[][] tempLastCenters = null;
        double[][] tempCurrentCenters = paraCenters;
        int[][] tempLastBlocks = new int[paraK][paraBlocks.length];
        int[][] tempCurrentBlocks = new int[paraK][paraBlocks.length];
        int[] tempLastBlocksLength = new int[paraK];
        int[] tempCurrentBlocksLengths = new int[paraK];
        double tempMinDistance;
        int tempMinDistanceIndex;
        double tempCurrentDistance;
        while(!doubleMatricesEqual(tempLastCenters, tempCurrentCenters)){
            Arrays.fill(tempCurrentBlocksLengths, 0);
            for (int i = 0; i < paraBlocks.length; i++) {
                tempMinDistance = Double.MAX_VALUE;
                tempMinDistanceIndex = -1;
                for (int j = 0; j < paraK; j++) {
                    tempCurrentDistance = distanceMeasure.distance(paraBlocks[i], tempCurrentCenters[j]);
                        if (tempMinDistance > tempCurrentDistance){
                            tempMinDistance = tempCurrentDistance;
                            tempMinDistanceIndex = j;
                        }//of if
                    }//of j
                    tempCurrentBlocks[tempMinDistanceIndex][tempCurrentBlocksLengths[tempMinDistanceIndex]++] = paraBlocks[i];
                }//of i
            tempLastCenters = tempCurrentCenters;
            tempCurrentCenters = new double[paraK][numConditions];
            for (int i = 0; i < paraK; i++) {
                for (int j = 0; j < tempCurrentBlocksLengths[i]; j++) {
                    for (int k = 0; k < numConditions; k++) {
                        tempCurrentCenters[i][k] += data.instance(
                                tempCurrentBlocks[i][j]).value(k) / tempCurrentBlocksLengths[i];
                    }
                }
            }

        }//of while
        System.out.println("The lengths are: " + Arrays.toString(tempCurrentBlocksLengths));
        int[][] resultBlocks = new int[paraK][];
        for (int i = 0; i < paraK; i++) {
            resultBlocks[i] = new int[tempCurrentBlocksLengths[i]];
            for (int j = 0; j < resultBlocks[i].length; j++) {
                resultBlocks[i][j] = tempCurrentBlocks[i][j];
            }
        }
        return resultBlocks;
    }

    public int[][] clusterInK(int[] paraBlocks, int paraK){
        double[][] tempCenters = getInitialCentersRandom(paraBlocks, paraK);
        return clusterInK(paraBlocks, paraK, tempCenters);
    }
    public int[][] clusterInTwo(int[] paraBlock){
        //Step 1. Initialize.
        int[] tempPair = new int[2];
        int[] tempBestPair = new int[2];
        double tempDistance;
        double tempMaxDistance = -1;

        //Step 2. Find a pair with pseudo-longest distance.
        for (int i = 0; i < 1000; i++) {
            tempPair[0] = common.random.nextInt(paraBlock.length);
            tempPair[1] = common.random.nextInt(paraBlock.length);

            tempDistance = distanceMeasure.distance(tempPair[0], tempPair[1]);
            if (tempMaxDistance < tempDistance) {
                tempMaxDistance = tempDistance;
                tempBestPair[0] = tempPair[0];
                tempBestPair[1] = tempPair[1];
            }//Of if
        }//Of for i

        //Step 3. Copy data.
        double[][] tempCenters = new double[2][numConditions];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < numConditions; j++) {
                tempCenters[i][j] = data.instance(tempBestPair[i]).value(j);
            }//Of for j
        }//Of for i

        //Step 4. Cluster using another method with the same name.
        return clusterInK(paraBlock, 2, tempCenters);
    }
    public int[][] clusterInK(int paraK) {
        int[] tempBlock = new int[numInstances];
        for (int i = 0; i < tempBlock.length; i++) {
            tempBlock[i] = i;
        }//Of for i

        return clusterInK(tempBlock, paraK);
    }//Of clusterInK

    /**
     ******************
     * Cluster into k blocks.
     ******************
    public int[][] clusterInK(int[] paraBlock, int paraK) {
    //Step 1. Initialize. Random select original centers.
    double[][] tempCenters = new double[paraK][numConditions];
    for (int i = 0; i < paraK; i++) {
    for (int j = 0; j < numConditions; j++) {
    tempCenters[i][j] = i / (paraK + 0);
    }//Of for j
    }//Of for i

    //Step 2. Cluster using another method with the same name.
    return clusterInK(paraBlock, paraK, tempCenters);
    }//Of clusterInK
     */

    public boolean doubleMatricesEqual(double[][] paraFirstMatrix, double[][] paraSecondMatrix){
        boolean flag = true;
        if (paraFirstMatrix == null || paraSecondMatrix == null){
            return false;
        }
        for (int i = 0; i < paraFirstMatrix.length; i++) {
            for (int j = 0; j < paraFirstMatrix[i].length; j++) {
                if (Math.abs(paraFirstMatrix[i][j] - paraSecondMatrix[i][j]) > 1e-6){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }
    public static void main(String[] args) {
        System.out.println("Hello, kMeans!");
        KMeans tempKMeans = new KMeans("src/main/data/iris.arff");

        //tempKMeans.testGetInitialCentersRandom();

        int[][] tempBlocks = tempKMeans.clusterInK(3);
        System.out.println(Arrays.deepToString(tempBlocks));
    }//Of main
}
