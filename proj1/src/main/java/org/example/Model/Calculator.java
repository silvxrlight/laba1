package org.example.Model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Calculator {
    DataHandler dataHandler = new DataHandler();
    List<Double> geometricMean = new ArrayList<>();
    List<Double> arithmeticMean = new ArrayList<>();
    List<Double> stdDeviation = new ArrayList<>();
    List<Double> R = new ArrayList<>();
    List<Integer> amount = new ArrayList<>();
    List<Double> variance = new ArrayList<>();
    List<Double> max = new ArrayList<>();
    List<Double> min = new ArrayList<>();
    List<Double> varianceRatio = new ArrayList<>();
    List<Pair<Double, Double>> limits = new ArrayList<>();

    //ковариация
    double[][] covarianceMatrix;

    //вспомогательные данные
    List<Double> avg = new ArrayList<>();
    double confidenceLevel = 0.9;
    List<Pair> pairs = new ArrayList<>();

    public void calculateStats(DataHandler dataHandler){
        this.dataHandler = dataHandler;
        List<List<Double>> selections = dataHandler.selection;

        // Очистка списков перед новым расчетом(для кайфа)
        geometricMean.clear();
        arithmeticMean.clear();
        stdDeviation.clear();
        R.clear();
        amount.clear();
        variance.clear();
        max.clear();
        min.clear();
        varianceRatio.clear();
        limits.clear();
        avg.clear();

        // делаем матрицу ковариации
        int b = 0;
        if (!selections.isEmpty()) {
            b = selections.get(0).size();
            covarianceMatrix = new double[b][b];
        }

        //расчет среднее геометрического/ арифметического + стандартного отклонения + размах + количество в выборке +
        // + дисперсия + максимум/минимум
        for (List<Double> selection: selections){
            double[] array = new double[selection.size()];
            for(int numIt = 0; numIt < array.length; numIt++){array[numIt] = selection.get(numIt);}
            DescriptiveStatistics stats = new DescriptiveStatistics(array);
            geometricMean.add(stats.getGeometricMean());
            arithmeticMean.add(stats.getMean());
            stdDeviation.add(stats.getStandardDeviation());
            R.add(stats.getMax() - stats.getMin());
            amount.add(array.length);
            variance.add(stats.getVariance());
            max.add(stats.getMax());
            min.add(stats.getMin());
            //расчет доверительного интервала. Формула: (xср - t(s/sqrt(n)); xср + t(s/sqrt(n))). при больших выборках
            // o~s
            TDistribution TDist = new TDistribution(array.length-1);
            double t = TDist.inverseCumulativeProbability(1 - (1 - confidenceLevel) / 2);
            Pair<Double, Double> limits_ = new ImmutablePair<>(stats.getMean() - t * (stats.getStandardDeviation() / Math.sqrt(array.length)), stats.getMean() + t * (stats.getStandardDeviation() / Math.sqrt(array.length)));
            limits.add(limits_);
            avg.add(stats.getSum() / array.length);
        }

        // Проверяем, что есть данные для расчёта ковариации
        if (selections.isEmpty() || selections.get(0).isEmpty()) {
            return;
        }

        int a = selections.size();
        b = selections.get(0).size();

        //расчет ковариации для всех пар случайных чисел. Формула: 1/n(S(xi-xср)(yi-yср))
        Covariance covarianceCalc = new Covariance();
        double[][] array = new double[b][a];
        for(int i = 0; i < a; i++){
            for(int j = 0; j < b; j++){
                array[j][i] = dataHandler.selection.get(i).get(j);
            }
        }
        for(int i = 0; i < b; i++){
            covarianceMatrix[i][i] = covarianceCalc.covariance(array[i], array[i]);
            for(int j = i + 1; j < b; j++){
                double covariance = covarianceCalc.covariance(array[i], array[j]);
                covarianceMatrix[i][j] = covariance;
                covarianceMatrix[j][i] = covariance;
            }
        }

        // расчет коэффициента вариации
        for(int varIt = 0; varIt < stdDeviation.size(); varIt++) {
            varianceRatio.add((stdDeviation.get(varIt) / avg.get(varIt)) * 100);
        }
    }
}