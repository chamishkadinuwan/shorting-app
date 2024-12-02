public class PerformanceEvaluator {
    private Sorting sorter = new Sorting();

    public long evaluateSorting(double[] array, String algorithm) {
        long startTime = System.nanoTime();
        switch (algorithm) {
            case "Insertion Sort": sorter.insertionSort(array); break;
            case "Shell Sort": sorter.shellSort(array); break;
            case "Merge Sort": sorter.mergeSort(array); break;
            case "Quick Sort": sorter.quickSort(array, 0, array.length - 1); break;
            case "Heap Sort": sorter.heapSort(array); break;
        }
        return System.nanoTime() - startTime;
    }

}
